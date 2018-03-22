package com.lzx2005.lzx2005webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.dto.ControllerResult;
import com.lzx2005.lzx2005webapi.service.GithubLoaderService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Lizhengxian on 2018/3/20.
 */
@RestController
@RequestMapping("/github/loader")
public class GithubLoaderController {

    @Autowired
    private GithubLoaderService githubLoaderService;

    @GetMapping("/user")
    public ControllerResult user(@RequestParam String username) {
        githubLoaderService.startLoadUserInfo(username);
        return ControllerResult.ok(null).setMsg("开始从Github读取用户"+username+"的用户信息数据");
    }
//
//    @GetMapping("/info")
//    public ControllerResult info(@RequestParam String username,@RequestParam(required = false,defaultValue = "1") int page) throws IOException {
//        String s = get("https://api.github.com/users/" + username + "/repos?sort=updated&type=owner&page=" + page + "&per_page=10");
//        return ControllerResult.ok(JSONArray.parseArray(s));
//    }
//
    @GetMapping("/language")
    public ControllerResult language(@RequestParam String username) throws IOException {
        githubLoaderService.startLoadLanguages(username);
        return ControllerResult.ok(null).setMsg("开始从Github读取用户"+username+"的Language信息数据");
    }

}
