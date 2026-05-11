package org.lbc.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.lbc.shortlink.admin.dto.req.UserPasswordModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserPhoneModifyReqDTO;
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
    public void userPasswordModify(UserPasswordModifyReqDTO requestParam) {
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
    public void userPhoneModify(UserPhoneModifyReqDTO requestParam) {
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .set(UserDO::getPhone, requestParam.getPhone());
        int num = baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
        if (num != 1) {
            throw new ClientException(ServiceErrorCodeEnums.SERVICE_SQL_MODIFY_ERROR);
        }
    }
}
