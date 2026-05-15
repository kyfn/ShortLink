package org.lbc.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
        String fsUrl = StrUtil.concat(true,domain, "/", uri);
        ShortLinkDO shortLink = ShortLinkDO.builder()
                .gid(requestParam.getGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .domain(domain)
                .createdType(requestParam.getCreatedType())
                .originUrl(requestParam.getOriginUrl())
                .statusEnable(1)
                .shortUri(uri)
                .fullShortUrl(fsUrl)
                .remark(requestParam.getRemark())
                .build();
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
