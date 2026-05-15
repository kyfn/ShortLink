package org.lbc.shortlink.project.utils.slink;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.project.common.convention.exception.ServiceException;
import org.lbc.shortlink.project.dao.mapper.LinkSequenceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 专门负责从数据库申请新号段。
 *
 * 必须独立成一个 Service：
 * 避免同类内部调用导致 @Transactional 不生效。
 */
@Service
@RequiredArgsConstructor
public class SegmentAllocateService {

    /**
     * 每次申请 1000 个号。
     * 创建量高可以调大，比如 5000 或 10000。
     */
    private static final int STEP = 1000;

    private final LinkSequenceMapper linkSequenceMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Segment allocate(String domain) {
        long now = DateUtil.currentSeconds();

        linkSequenceMapper.init(domain, now);

        int updated = linkSequenceMapper.allocateSegment(domain, STEP, now);
        if (updated != 1) {
            throw new ServiceException("申请短链号段失败，domain=" + domain);
        }

        Long max = linkSequenceMapper.getLastInsertId();
        if (max == null || max <= 0) {
            throw new ServiceException("获取短链号段失败，domain=" + domain);
        }

        long start = max - STEP + 1;
        if (start > Base62Util.MAX_6_VALUE) {
            throw new ServiceException("当前域名下 6 位短码容量已用完，domain=" + domain);
        }

        return new Segment(start, Math.min(max, Base62Util.MAX_6_VALUE));
    }
}
