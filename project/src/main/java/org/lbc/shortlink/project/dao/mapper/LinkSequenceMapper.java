package org.lbc.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.lbc.shortlink.project.dao.entity.LinkSequenceDO;


@Mapper
public interface LinkSequenceMapper extends BaseMapper<LinkSequenceDO> {

    /**
     * 初始化某个域名的号段记录。
     * 如果已存在，不修改 current_value。
     */
    void init(@Param("domain") String domain, @Param("now") Long now);

    /**
     * 申请一个号段。
     *
     * 例如：
     * current_value = 1000
     * step = 1000
     * 执行后 current_value = 2000
     *
     * 本次可用号段就是 1001 ~ 2000。
     */
    int allocateSegment(@Param("domain") String domain,
                        @Param("step") Integer step,
                        @Param("now") Long now);

    /**
     * 获取当前连接里的 LAST_INSERT_ID()。
     *
     * 必须和 allocateSegment 在同一个事务、同一个数据库连接里执行。
     */
    Long getLastInsertId();
}

