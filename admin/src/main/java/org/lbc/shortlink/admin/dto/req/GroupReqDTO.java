package org.lbc.shortlink.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupReqDTO
{
    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    private String name;
}
