package org.lbc.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.dao.entity.GroupDO;
import org.lbc.shortlink.admin.dao.mapper.GroupMapper;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;
import org.lbc.shortlink.admin.service.GroupService;
import org.lbc.shortlink.admin.utils.RandomGenerator;
import org.springframework.stereotype.Service;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    @Override
    public GroupRespDTO create(GroupReqDTO requestParam) {
        GroupDO group = GroupDO.builder()
                .name(requestParam.getName())
                .username(requestParam.getUsername())
                .gid(RandomGenerator.generate())
                .build();
        int num = baseMapper.insert(group);
        if (num != 1) {
            throw new ClientException("短链接分组创建失败");
        }
        return null;
    }
}
