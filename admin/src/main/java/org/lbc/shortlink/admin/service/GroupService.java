package org.lbc.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lbc.shortlink.admin.dao.entity.GroupDO;
import org.lbc.shortlink.admin.dto.req.GroupModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.req.GroupSortReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 创建短链接分组
     * @param requestParam {name 分组名 username 创建分组的用户名}
     * @return 短链接分组数据
     */
    GroupRespDTO create(GroupReqDTO requestParam);

    /**
     * 查询所有短链接分组
     * @return 分组数据集合
     */
    List<GroupRespDTO> getAll();

    /**
     * 修改分组名称
     * @param requestParam {gid 修改分组的 id name 新分组名称}
     */
    void modify(GroupModifyReqDTO requestParam);

    /**
     * 删除分组
     * @param gid 分组 id
     */
    void delete(String gid);

    /**
     * 分组排序
     * @param requestParam 分组排序列表[{gid, sortOrder}...]
     */
    void sortGroup(List<GroupSortReqDTO> requestParam);
}
