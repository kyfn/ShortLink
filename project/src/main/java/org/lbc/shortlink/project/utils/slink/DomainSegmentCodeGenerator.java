package org.lbc.shortlink.project.utils.slink;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.project.common.convention.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 按 domain 生成 6 位短码。
 */
@Component
@RequiredArgsConstructor
public class DomainSegmentCodeGenerator {

    private final SegmentAllocateService segmentAllocateService;

    private final ConcurrentHashMap<String, Segment> segmentMap = new ConcurrentHashMap<>();
    // 按域名加锁，全局锁会锁住全部域名申请新号段，域名锁只会锁当前域名申请新号段
    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public String nextCode(String domain) {
        domain = normalizeDomain(domain);

        long seq = nextSeq(domain);
        return Base62Util.encode(seq);
    }

    private long nextSeq(String domain) {
        Segment segment = segmentMap.get(domain);
        long seq = segment == null ? -1 : segment.next();

        if (seq != -1) {
            return seq;
        }
        // computeIfAbsent 如果 lockMap 里已经有 a.com 的锁，就拿出来用；
        ReentrantLock lock = lockMap.computeIfAbsent(domain, key -> new ReentrantLock());
        lock.lock();

        try {
            // 再次拿号段是“双重检查”，多个线程在排队申请号段，第一个拿到锁申请后，
            // 后续的线程拿到锁后不应该再申请号段，而是从一申请的号段里面取号
            segment = segmentMap.get(domain);
            seq = segment == null ? -1 : segment.next();

            if (seq != -1) {
                return seq;
            }

            Segment newSegment = segmentAllocateService.allocate(domain);
            segmentMap.put(domain, newSegment);

            seq = newSegment.next();
            if (seq == -1) {
                throw new ServiceException("新号段不可用，domain=" + domain);
            }

            return seq;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 避免同一个域名因为格式不一样被当成不同域名。
     * https://a.com https://a.com/ https://a.com/// 如果不处理，它们会被当成三个不同 key。
     */
    private String normalizeDomain(String domain) {
        if (StrUtil.isBlank(domain)) {
            throw new IllegalArgumentException("domain 不能为空");
        }

        domain = StrUtil.trim(domain);
        while (StrUtil.endWith(domain, "/")) {
            domain = StrUtil.removeSuffix(domain, "/");
        }
        return domain;
    }
}


