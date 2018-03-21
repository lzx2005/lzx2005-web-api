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
        if(value==null){
            return ControllerResult.ok(null).setCode(10001).setMsg("没有找到该用户的信息，请先从github中获取");
        }else if(value.equals("loading")) {
            return ControllerResult.ok(null).setCode(10002).setMsg("正在从github中获取信息，请稍后...");
        }else if(value.equals("failed")){
            return ControllerResult.ok(null).setCode(10003).setMsg("从github中获取信息失败，请尝试重新获取");
        }else{
            return ControllerResult.ok(JSONObject.parseObject(value));
        }
    }
}
