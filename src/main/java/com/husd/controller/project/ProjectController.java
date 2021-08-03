package com.husd.controller.project;

import com.husd.domain.Req;
import com.husd.domain.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 应用管理
 */
@RestController
@RequestMapping("/project")
@Api(tags = "项目管理")
public class ProjectController {

    @GetMapping("/listByProjectId")
    @ApiOperation(value = "查询项目")
    public Resp listByProjectId(@RequestBody Req req) {

        return new Resp();
    }

    @PostMapping("/list/detail")
    @ApiOperation(value = "查看项目详情")
    public Resp listDetail(@RequestBody Req req) {

        return new Resp();
    }

    @PutMapping("/add")
    @ApiOperation(value = "新增项目")
    public Resp add(@RequestBody Req req) {

        return new Resp();
    }
}
