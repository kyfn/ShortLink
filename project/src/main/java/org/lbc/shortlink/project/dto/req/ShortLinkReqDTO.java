package org.lbc.shortlink.project.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lbc.shortlink.project.common.annotation.validdate.ValidDateRequired;
import org.lbc.shortlink.project.dto.ValidDate;

@Data
@EqualsAndHashCode(callSuper = false)
@ValidDateRequired
public class ShortLinkReqDTO implements ValidDate {
    //分组 id
    @NotBlank(message = "分组 id 不能为空")
    private String gid;
    //域名
    @NotBlank(message = "域名不能为空")
    private String domain;
    //原始链接
    @NotBlank(message = "原始链接不能为空")
    private String originUrl;
    @NotNull(message = "创建类型不能为空")
    //创建类型：0 接口创建，1 控制台创建
    private Integer createdType;
    @NotNull(message = "有效期类型不能为空")
    //有效期类型：0 永久有效 1 自定义
    private Integer vaildDateType;
    //有效期时间戳(秒)
    private Long validDate;
    //描述
    private String remark;
}
