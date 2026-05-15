package org.lbc.shortlink.project.common.database;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class BaseDO {
    /**
     * 创建时间戳
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 修改时间戳
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    /**
     * 删除标识 0:未删除 1：已删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
}
