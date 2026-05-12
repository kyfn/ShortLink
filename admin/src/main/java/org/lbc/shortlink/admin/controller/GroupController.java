package org.lbc.shortlink.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.common.convention.result.Results;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;
import org.lbc.shortlink.admin.service.GroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@Validated
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/api/slink/v1/group")
    public Result<GroupRespDTO> create(@RequestBody @Valid GroupReqDTO requestParam) {
        GroupRespDTO result = groupService.create(requestParam);
        return Results.success(result);
    }

    @GetMapping("/api/slink/v1/group")
    public Result<List<GroupRespDTO>> getAll() {
        return Results.success(groupService.getAll());
    }
}
