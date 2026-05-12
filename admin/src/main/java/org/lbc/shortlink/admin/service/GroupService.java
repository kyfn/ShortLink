package org.lbc.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lbc.shortlink.admin.dao.entity.GroupDO;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;

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
}
