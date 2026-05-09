package org.lbc.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.constant.RedisCacheConstant;
import org.lbc.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.common.enums.UserErrorCodeEnums;
import org.lbc.shortlink.admin.dao.entity.UserDO;
import org.lbc.shortlink.admin.dao.mapper.UserMapper;
import org.lbc.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.lbc.shortlink.admin.dto.resp.UserActualRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserRespDTO;
import org.lbc.shortlink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;

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
    public void userRegister(UserRegisterReqDTO requestParam) {
        boolean isExists = userRegisterCachePenetrationBloomFilter.contains(requestParam.getUsername());
        if (isExists) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY + requestParam.getUsername());

        try {
            if (lock.tryLock()) {
                UserDO user = BeanUtil.toBean(requestParam, UserDO.class);
                int num = baseMapper.insert(user);
                if (num < 1) {
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
}
