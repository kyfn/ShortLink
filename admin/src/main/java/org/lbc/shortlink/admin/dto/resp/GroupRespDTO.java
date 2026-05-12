package org.lbc.shortlink.admin.dto.resp;

import lombok.Data;

@Data
public class GroupRespDTO {
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
    /**
     * 删除标识 0:未删除 1：已删除
     */
    private Integer delFlag;
}
