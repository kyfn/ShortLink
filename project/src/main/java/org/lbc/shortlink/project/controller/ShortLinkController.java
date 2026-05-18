package org.lbc.shortlink.project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.project.common.convention.result.Result;
import org.lbc.shortlink.project.common.convention.result.Results;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.project.dto.resp.PageDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkRespDTO;
import org.lbc.shortlink.project.service.ShortLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    /**
     * 创建新的短链接
     * @param requestParam 创建参数对象
     */
    @PostMapping("/api/slink/v1/link")
    public Result<ShortLinkCreateRespDTO> createLink(
            @RequestHeader(value = "Module-Id", required = false, defaultValue = "project") String source,
            @RequestBody @Valid ShortLinkReqDTO requestParam
    ) {
        return Results.success(shortLinkService.createLink(requestParam, source));
    }

    /**
     * 根据分组 id 查询当前分组下的短链接集合
     * @param gid 分组 id
     * @return 短链接集合
     */
    @GetMapping("/api/slink/v1/{gid}/links")
    public Result<PageDTO<ShortLinkRespDTO>> getLinksByGid(
            @PathVariable("gid") @NotBlank String gid,
            @RequestParam(value = "pageNum", required = false) @NotNull Integer pageNum,
            @RequestParam(value = "pageSize", required = false) @NotNull Integer pageSize
    ) {
        return Results.success(shortLinkService.getLinksByGid(gid, pageNum, pageSize));
    }
}
