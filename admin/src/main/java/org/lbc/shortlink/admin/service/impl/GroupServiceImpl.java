package org.lbc.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.lbc.shortlink.admin.common.biz.user.UserContext;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.common.convention.exception.ServiceException;
import org.lbc.shortlink.admin.dao.entity.GroupDO;
import org.lbc.shortlink.admin.dao.mapper.GroupMapper;
import org.lbc.shortlink.admin.dto.req.GroupModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.req.GroupSortReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;
import org.lbc.shortlink.admin.service.GroupService;
import org.lbc.shortlink.admin.utils.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        GroupDO group = baseMapper.selectOne(Wrappers.lambdaQuery(GroupDO.class).eq(GroupDO::getGid, requestParam.getGid()));
        if (group == null) {
            throw new ClientException("分组不存在");
        }
        Wrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid());
        GroupDO newGroup = new GroupDO();
        newGroup.setName(requestParam.getName());
        int num = baseMapper.update(newGroup, updateWrapper);
        if (num != 1) {
            throw new ServiceException("修改失败，请稍后重试");
        }
    }

    @Override
    public void delete(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);
        GroupDO group = baseMapper.selectOne(queryWrapper);
        if (group == null) {
            throw new ClientException("分组不存在");
        }
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);
        GroupDO delGroup = new GroupDO();
        delGroup.setDelFlag(1);
        int num = baseMapper.update(delGroup, updateWrapper);
        if (num != 1) {
            throw new ServiceException("删除失败，请稍后重试");
        }
    }

    @Override
    public void sortGroup(List<GroupSortReqDTO> requestParam) {
        if (requestParam == null || requestParam.isEmpty()) {
            throw new ClientException("排序分组不能为空");
        }
        String username = UserContext.getUsername();
        List<String> gids = requestParam.stream()
                .map(GroupSortReqDTO::getGid)
                .collect(Collectors.toList());

        Set<String> gidSet = new HashSet<>(gids);
        if (gidSet.size() != gids.size()) {
            throw new ClientException("存在重复的分组ID");
        }

        Long count = lambdaQuery()
                .in(GroupDO::getGid, gidSet)
                .eq(GroupDO::getUsername, username)
                .count();

        if (count != gidSet.size()) {
            throw new ClientException("存在无权操作或不存在的数据");
        }
        baseMapper.batchUpdateSortOrder(requestParam, UserContext.getUsername());
    }
}
