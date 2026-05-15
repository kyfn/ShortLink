package org.lbc.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_link_sequence")
public class LinkSequenceDO {

    /**
     * 域名，作为主键。
     */
    @TableId(value = "domain", type = IdType.INPUT)
    private String domain;

    /**
     * 当前已经分配出去的最大序号。
     */
    private Long currentValue;

    private Long createTime;

    private Long updateTime;
}
