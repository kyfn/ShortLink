package org.lbc.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import org.lbc.shortlink.project.dao.entity.ShortLinkDO;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkRespDTO;

import java.util.List;

public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建新的短链接
     * @param requestParam 创建参数对象
     */
    void createLink(@Valid ShortLinkReqDTO requestParam);

    /**
     * 根据分组 id 查询当前分组下的短链接集合
     * @param gid 分组 id
     * @return 短链接集合
     */
    List<ShortLinkRespDTO> getLinkByGid(String gid);
}
