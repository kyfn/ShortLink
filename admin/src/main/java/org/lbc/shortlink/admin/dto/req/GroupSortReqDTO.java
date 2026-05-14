package org.lbc.shortlink.admin.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupSortReqDTO
{
    @NotBlank(message = "分组ID不能为空")
    private String gid;

    @NotNull(message = "排序值不能为空")
    @Min(value = 0, message = "排序值不能小于0")
    private Long sortOrder;
}
