package com.lzx2005.lzx2005webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.lzx2005.lzx2005webapi.constant.RedisKey;
import com.lzx2005.lzx2005webapi.dto.ControllerResult;
import com.lzx2005.lzx2005webapi.service.SteamSpyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * SteamSpy爬虫
 * Created by Lizhengxian on 2018/4/24.
 */
@RestController
@RequestMapping("/steam")
public class SteamSpyController {
    private static final Logger logger = LoggerFactory.getLogger(SteamSpyController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SteamSpyService steamSpyService;

    @GetMapping("/trending")
    public ControllerResult trending(){
        JSONArray jsonArray = (JSONArray) redisTemplate.opsForValue().get(RedisKey.STEAM_TRENGIND);
        return ControllerResult.ok(jsonArray);
    }
    @GetMapping("/recent")
    public ControllerResult recent(){
        JSONArray jsonArray = (JSONArray) redisTemplate.opsForValue().get(RedisKey.STEAM_RECENT);
        return ControllerResult.ok(jsonArray);
    }
    @GetMapping("/top")
    public ControllerResult top(){
        JSONArray jsonArray = (JSONArray) redisTemplate.opsForValue().get(RedisKey.STEAM_TOP);
        return ControllerResult.ok(jsonArray);
    }

    @GetMapping("/read")
    public ControllerResult read(HttpServletRequest request){
        //todo 这个可以改成异步任务
        String cookie = request.getHeader("Cookie");
        logger.info("cookie={}",cookie);
        logger.info("开始读取SteamSpy数据...");
        steamSpyService.readIndex(cookie);
        logger.info("读取完毕");
        return ControllerResult.ok(null);
    }



}
