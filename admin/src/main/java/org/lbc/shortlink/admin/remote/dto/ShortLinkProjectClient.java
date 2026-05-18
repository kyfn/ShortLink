package org.lbc.shortlink.admin.remote.dto;

import jakarta.validation.Valid;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.dto.resp.PageRespDTO;
import org.lbc.shortlink.admin.remote.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.lbc.shortlink.admin.remote.dto.resp.ShortLinkRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "shortlink-project", url = "http://localhost:8001")
public interface ShortLinkProjectClient {
    /**
     * 创建新的短链接
     * @param requestParam 创建参数对象
     */
    @PostMapping("/api/slink/v1/admin/link")
    Result<ShortLinkCreateRespDTO> createLink(@RequestBody @Valid ShortLinkReqDTO requestParam);

    @GetMapping("/api/slink/v1/{gid}/links")
    Result<PageRespDTO<ShortLinkRespDTO>> getLinksByGid(
            @PathVariable("gid") String gid,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize);
}
