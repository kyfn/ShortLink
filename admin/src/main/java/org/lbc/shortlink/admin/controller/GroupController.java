package org.lbc.shortlink.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.common.convention.result.Results;
import org.lbc.shortlink.admin.dto.req.GroupModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.GroupReqDTO;
import org.lbc.shortlink.admin.dto.resp.GroupRespDTO;
import org.lbc.shortlink.admin.service.GroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@Validated
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增分组
     * @param requestParam 新增分组参数
     * @return
     */
    @PostMapping("/api/slink/v1/group")
    public Result<GroupRespDTO> create(@RequestBody @Valid GroupReqDTO requestParam) {
        GroupRespDTO result = groupService.create(requestParam);
        return Results.success(result);
    }

    @PutMapping("/api/slink/v1/group")
    public Result<Void> update(@RequestBody @Valid GroupModifyReqDTO requestParam) {
        groupService.modify(requestParam);
        return Results.success();
    }

    @DeleteMapping("/api/slink/v1/group/{gid}")
    public Result<Void> update(@PathVariable String gid) {
        groupService.delete(gid);
        return Results.success();
    }

    @GetMapping("/api/slink/v1/groups")
    public Result<List<GroupRespDTO>> getAll() {
        return Results.success(groupService.getAll());
    }
}
