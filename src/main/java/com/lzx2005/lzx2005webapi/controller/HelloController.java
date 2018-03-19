package com.lzx2005.lzx2005webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lizhengxian on 2018/3/19.
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "hello";
    }
}
