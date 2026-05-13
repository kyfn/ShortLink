package org.lbc.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lbc.shortlink.admin.common.biz.user.UserContext;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.common.convention.exception.ServiceException;
import org.lbc.shortlink.admin.dao.entity.GroupDO;
import org.lbc.shortlink.admin.dao.mapper.GroupMapper;
import org.lbc.shortlink.admin.dto.req.GroupModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;
import org.lbc.shortlink.admin.service.GroupService;
import org.lbc.shortlink.admin.utils.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    private final GroupMapper groupMapper;

    @Override
    public GroupRespDTO create(GroupReqDTO requestParam) {
        GroupDO group = GroupDO.builder()
                .name(requestParam.getName())
                .username(UserContext.getUsername())
                .gid(RandomGenerator.generate())
                .sortOrder(0L)
                .build();
        int num = baseMapper.insert(group);
        if (num != 1) {
            throw new ServiceException("短链接分组创建失败");
        }
        return BeanUtil.toBean(group, GroupRespDTO.class);
    }

    @Override
    public List<GroupRespDTO> getAll() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder)
                .orderByDesc(GroupDO::getUpdateTime);
        List<GroupDO> groupDOS = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(groupDOS, GroupRespDTO.class);
    }

    @Override
    public void modify(GroupModifyReqDTO requestParam) {
        GroupDO group = groupMapper.selectOne(Wrappers.<GroupDO>lambdaQuery().eq(GroupDO::getGid, requestParam.getGid()));
        if (group == null) {
            throw new ClientException("分组不存在");
        }
        Wrapper<GroupDO> updateWrapper = Wrappers.<GroupDO>lambdaUpdate()
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid());
        GroupDO newGroup = new GroupDO();
        newGroup.setName(requestParam.getName());
        int num = groupMapper.update(newGroup, updateWrapper);
        if (num != 1) {
            throw new ServiceException("修改失败，请稍后重试");
        }
    }

    @Override
    public void delete(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.<GroupDO>lambdaQuery()
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid);
        GroupDO group = groupMapper.selectOne(queryWrapper);
        if (group == null) {
            throw new ClientException("分组不存在");
        }
        int num = groupMapper.delete(queryWrapper);
        if (num != 1) {
            throw new ServiceException("删除失败，请稍后重试");
        }
    }
}
