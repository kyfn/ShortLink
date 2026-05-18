package org.lbc.shortlink.admin.remote.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShortLinkReqDTO {
    //分组 id
    @NotBlank(message = "分组 id 不能为空")
    private String gid;
    //域名
    @NotBlank(message = "域名不能为空")
    private String domain;
    //原始链接
    @NotBlank(message = "原始链接不能为空")
    private String originUrl;
    //网站图标
    private String favicon;
    @NotNull(message = "有效期类型不能为空")
    //有效期类型：0 永久有效 1 自定义
    private Integer validDateType;
    //有效期时间戳(秒)
    private Long validDate;
    //描述
    private String remark;
}
