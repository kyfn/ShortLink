package org.lbc.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lbc.shortlink.project.dao.entity.ShortLinkDO;
import org.lbc.shortlink.project.dao.mapper.ShortLinkMapper;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.project.service.ShortLinkService;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Override
    public void createLink(ShortLinkReqDTO requestParam) {
//        ShortLinkDO shortLink = ShortLinkDO.builder()
//                .gid(requestParam.getGid())
//                .vaildDateType(requestParam.getVaildDateType())
//                .shortUri()
//                .build();
    }
}
