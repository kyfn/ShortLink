package org.lbc.shortlink.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.project.common.convention.result.Result;
import org.lbc.shortlink.project.common.convention.result.Results;
import org.lbc.shortlink.project.dto.req.ShortLinkReqDTO;
import org.lbc.shortlink.project.service.ShortLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    @PostMapping("/api/slink/v1/project/link")
    public Result<Void> createLink(@RequestBody @Valid ShortLinkReqDTO requestParam) {
        shortLinkService.createLink(requestParam);
        return Results.success();
    }
}
