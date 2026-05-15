package org.lbc.shortlink.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.project.common.convention.result.Result;
import org.lbc.shortlink.project.common.convention.result.Results;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.lbc.shortlink.project.dto.resp.ShortLinkRespDTO;
import org.lbc.shortlink.project.service.ShortLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    /**
     * 创建新的短链接
     * @param requestParam 创建参数对象
     */
    @PostMapping("/api/slink/v1/project/link")
    public Result<ShortLinkCreateRespDTO> createLink(@RequestBody @Valid ShortLinkReqDTO requestParam) {
        return Results.success(shortLinkService.createLink(requestParam));
    }

    /**
     * 根据分组 id 查询当前分组下的短链接集合
     * @param gid 分组 id
     * @return 短链接集合
     */
    @GetMapping("/api/slink/v1/project/link/{gid}")
    public Result<List<ShortLinkRespDTO>> getLinkByGid(@PathVariable("gid") String gid) {
        return Results.success(shortLinkService.getLinkByGid(gid));
    }
}
