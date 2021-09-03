package com.zj.server.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author zhoujian on 2021/8/28 0028 16:07
 */

@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

    @ApiOperation("基本资料菜单")
    @GetMapping("/employee/basic/hello")
    public String hello2(){
        return "基本资料";
    }

    @ApiOperation("高级资料菜单")
    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "高级资料";
    }
}
