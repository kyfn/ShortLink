package org.lbc.shortlink.project.utils.slink;

import java.util.concurrent.atomic.AtomicLong;

/**
 * JVM 内存中的一个号段。
 */
public class Segment {

    private final AtomicLong current;
    private final long max;

    public Segment(long start, long max) {
        this.current = new AtomicLong(start);
        this.max = max;
    }

    /**
     * 返回下一个序号。
     * 返回 -1 表示当前号段已经用完。
     */
    public long next() {
        long value = current.getAndIncrement();
        return value <= max ? value : -1;
    }
}


