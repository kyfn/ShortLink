package org.lbc.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (TGroup)实体类
 *
 * @author makejava
 * @since 2026-05-12 17:32:46
 */
@Data
@TableName("t_group")
public class GroupDO implements Serializable {
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
     * 创建时间戳
     */
    private Long createTime;
/**
     * 修改时间戳
     */
    private Long updateTime;
/**
     * 删除标识 0:未删除 1：已删除
     */
    private Integer delFlag;

}

