package com.husd.controller.ping;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PING
 */
@Api(value = "/ping", tags = {"PING测试"})
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
