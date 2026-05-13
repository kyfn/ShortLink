package org.lbc.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.constant.RedisCacheConstant;
import org.lbc.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.common.enums.ServiceErrorCodeEnums;
import org.lbc.shortlink.admin.common.enums.UserErrorCodeEnums;
import org.lbc.shortlink.admin.dao.entity.UserDO;
import org.lbc.shortlink.admin.dao.mapper.UserMapper;
import org.lbc.shortlink.admin.dto.req.UserLoginReqDTO;
import org.lbc.shortlink.admin.dto.req.UserPasswordModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserPhoneModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.lbc.shortlink.admin.dto.resp.UserActualRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserRespDTO;
import org.lbc.shortlink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnums.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public UserActualRespDTO getActualUserByUsername(String username) {
        UserRespDTO user = getUserByUsername(username);
        UserActualRespDTO result = new UserActualRespDTO();
        BeanUtils.copyProperties(user, result);
        return result;
    }

    @Override
    public void checkUsernameExists(String username) {
        boolean isExists = userRegisterCachePenetrationBloomFilter.contains(username);
        if (isExists) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST);
        }
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        boolean isExists = userRegisterCachePenetrationBloomFilter.contains(requestParam.getUsername());
        if (isExists) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY + requestParam.getUsername());

        try {
            if (lock.tryLock()) {
                UserDO user = BeanUtil.toBean(requestParam, UserDO.class);
                int num = baseMapper.insert(user);
                if (num != 1) {
                    throw new ClientException(UserErrorCodeEnums.USER_NULL);
                }
                userRegisterCachePenetrationBloomFilter.add(user.getUsername());
                return;
            }
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void modifyPassword(UserPasswordModifyReqDTO requestParam) {
        // TODO 验证单是否为当前用户
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        String oldPassword = baseMapper.selectOne(queryWrapper).getPassword();
        if (!requestParam.getOldPassword().equals(oldPassword)) {
            throw new ClientException(UserErrorCodeEnums.USER_PASSWORD_ERROR);
        }
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                        .eq(UserDO::getUsername, requestParam.getUsername())
                .set(UserDO::getPassword, requestParam.getNewPassword());
        int num = baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
        if (num != 1) {
            throw new ClientException(ServiceErrorCodeEnums.SERVICE_SQL_MODIFY_ERROR);
        }
    }

    @Override
    public void modifyPhone(UserPhoneModifyReqDTO requestParam) {
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .set(UserDO::getPhone, requestParam.getPhone());
        int num = baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
        if (num != 1) {
            throw new ClientException(ServiceErrorCodeEnums.SERVICE_SQL_MODIFY_ERROR);
        }
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // TODO 用户名校验是否存在
        Wrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new ClientException(UserErrorCodeEnums.USER_NULL);
        }
        String key = "login_"+requestParam.getUsername();
        Boolean flag = stringRedisTemplate.hasKey(key);
        if (flag) {
            throw new ClientException("用户已登录");
        }
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(key, uuid, JSON.toJSONString(user));
        stringRedisTemplate.expire(key, 30L, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        String key = "login_"+username;
        return stringRedisTemplate.opsForHash().get(key, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        Boolean isLogin = checkLogin(username, token);
        if (!isLogin) {
            throw new ClientException("用户未登录");
        }
        String key = "login_"+username;
        Boolean isSuccess = stringRedisTemplate.delete(key);
        if (!isSuccess) {
            throw new ClientException("退出登录失败");
        }
    }
}
