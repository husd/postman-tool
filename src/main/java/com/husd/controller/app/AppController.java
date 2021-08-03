package com.husd.controller.app;

import com.husd.domain.Req;
import com.husd.domain.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 应用管理
 */
@RestController
@RequestMapping("/app")
@Api(tags = "应用管理")
public class AppController {

    @GetMapping("/listByProjectId")
    @ApiOperation(value = "查询项目下的所有的应用")
    public Resp listByProjectId(@RequestBody Req req) {

        return new Resp();
    }

    @PostMapping("/list/detail")
    @ApiOperation(value = "查看应用详情")
    public Resp listDetail(@RequestBody Req req) {

        return new Resp();
    }

    @PutMapping("/add")
    @ApiOperation(value = "新增应用")
    public Resp add(@RequestBody Req req) {

        return new Resp();
    }
}
