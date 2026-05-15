package org.lbc.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import org.lbc.shortlink.project.dao.entity.ShortLinkDO;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    void createLink(@Valid ShortLinkReqDTO requestParam);
}
