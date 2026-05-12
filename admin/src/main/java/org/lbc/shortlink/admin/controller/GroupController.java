package org.lbc.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.service.GroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@Validated
public class GroupController {

    private final GroupService groupService;
}
