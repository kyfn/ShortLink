package org.lbc.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.lbc.shortlink.admin.common.database.BaseDO;

import java.io.Serializable;

/**
 * (TGroup)实体类
 *
 * @author makejava
 * @since 2026-05-12 17:32:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 851111031902317151L;
    /**
     * ID
     */
    private Long id;
    /**
     * 分组标识
     */
    private String gid;
    /**
     * 分组名称
     */
    private String name;
    /**
     * 创建的分组用户名
     */
    private String username;
    /**
     * 排序序号
     */
    private Long sortOrder;
}

