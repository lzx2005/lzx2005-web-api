package com.lzx2005.lzx2005webapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.constant.RedisKey;
import com.lzx2005.lzx2005webapi.dto.ControllerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lizhengxian on 2018/3/21.
 */
@RestController
@RequestMapping("/github")
public class GithubController {
    private static final Logger logger = LoggerFactory.getLogger(GithubController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/user")
    public ControllerResult user(@RequestParam String username) {
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(RedisKey.USER_INFO);
        String value = (String) boundHashOperations.get(username);
        if(!StringUtils.isEmpty(value)){
            return ControllerResult.ok(JSONObject.parseObject(value));
        }else{
            return ControllerResult.ok(null).setCode(404).setMsg("没有找到该用户的信息，请先从github中获取");
        }
    }
}
