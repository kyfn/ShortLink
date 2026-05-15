package org.lbc.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.project.common.convention.exception.ServiceException;
import org.lbc.shortlink.project.dao.entity.ShortLinkDO;
import org.lbc.shortlink.project.dao.mapper.ShortLinkMapper;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkRespDTO;
import org.lbc.shortlink.project.service.ShortLinkService;
import org.lbc.shortlink.project.utils.slink.DomainSegmentCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final DomainSegmentCodeGenerator domainSegmentCodeGenerator;

    @Override
    public ShortLinkCreateRespDTO createLink(ShortLinkReqDTO requestParam) {
        String domain = requestParam.getDomain();
        String uri= domainSegmentCodeGenerator.nextCode(domain);
        String fsUrl = domain + "/" + uri;
        ShortLinkDO shortLink = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        shortLink.setShortUri(uri);
        shortLink.setFullShortUrl(fsUrl);
        shortLink.setStatusEnable(1);
        int num = baseMapper.insert(shortLink);
        if (num != 1) {
            throw new ServiceException("短链接创建失败");
        }
        return ShortLinkCreateRespDTO.builder()
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .fullShortUrl(fsUrl)
                .build();
    }

    @Override
    public List<ShortLinkRespDTO> getLinkByGid(String gid) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class).eq(ShortLinkDO::getGid, gid);
        List<ShortLinkDO> links = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(links, ShortLinkRespDTO.class);
    }
}
