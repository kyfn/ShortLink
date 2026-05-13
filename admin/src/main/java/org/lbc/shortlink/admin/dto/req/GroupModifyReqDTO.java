package org.lbc.shortlink.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupModifyReqDTO {
    @NotBlank(message = "分组 id 不能为空")
    private String gid;
    @NotBlank(message = "分组名称不能为空")
    private String name;
}
