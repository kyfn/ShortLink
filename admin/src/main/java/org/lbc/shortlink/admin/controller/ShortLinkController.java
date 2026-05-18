package org.lbc.shortlink.admin.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.dto.resp.PageRespDTO;
import org.lbc.shortlink.admin.remote.dto.ShortLinkProjectClient;
import org.lbc.shortlink.admin.remote.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.lbc.shortlink.admin.remote.dto.resp.ShortLinkRespDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkProjectClient shortLinkProjectClient;

    /**
     * 创建新的短链接
     * @param requestParam 创建参数对象
     */
    @PostMapping("/api/slink/v1/link")
    public Result<ShortLinkCreateRespDTO> createLink(@RequestBody @Valid ShortLinkReqDTO requestParam) {
        return shortLinkProjectClient.createLink(requestParam);
    }

    /**
     * 根据分组 id 查询当前分组下的短链接集合
     * @param gid 分组 id
     * @return 短链接集合
     */
    @GetMapping("/api/slink/v1/admin/{gid}/links")
    public Result<PageRespDTO<ShortLinkRespDTO>> getLinksByGid(
            @PathVariable("gid") String gid,
            @PathParam("pageNum") Integer pageNum,
            @PathParam("pageSize") Integer pageSize
    ) {
        return shortLinkProjectClient.getLinksByGid(gid, pageNum, pageSize);
    }
}
