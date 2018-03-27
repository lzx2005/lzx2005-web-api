package com.lzx2005.lzx2005webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.constant.RedisKey;
import com.lzx2005.lzx2005webapi.dto.ControllerResult;
import com.lzx2005.lzx2005webapi.service.HttpService;
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

import java.io.IOException;
import java.util.Map;

/**
 * Created by Lizhengxian on 2018/3/21.
 */
@RestController
@RequestMapping("/github")
public class GithubController {
    private static final Logger logger = LoggerFactory.getLogger(GithubController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpService httpService;

    @GetMapping("/user")
    public ControllerResult user(@RequestParam String username) {
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(RedisKey.USER_INFO);
        String value = (String) boundHashOperations.get(username);
        return getResult(value);
    }

    @GetMapping("/language")
    public ControllerResult language(@RequestParam String username) {
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(RedisKey.USER_LANGUAGES);
        String value = (String) boundHashOperations.get(username);
        ControllerResult result = getResult(value);
        if(result.getCode()==200){
            JSONObject data = (JSONObject) result.getData();
            JSONArray jsonArray = new JSONArray();
            for(Map.Entry<String,Object> entry : data.getJSONObject("data").entrySet()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",entry.getKey());
                jsonObject.put("value",entry.getValue());
                jsonArray.add(jsonObject);
            }
            data.put("data", jsonArray);
            result.setData(data);
        }
        return result;
    }



    @GetMapping("/calender")
    public ControllerResult calender(@RequestParam String username) {
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(RedisKey.USER_CALENDAR);
        String value = (String) boundHashOperations.get(username);
        return getResult(value);
    }

    @GetMapping("/repos")
    public ControllerResult repos(@RequestParam String username,
                                  @RequestParam(defaultValue = "1",required = false) int page,
                                  @RequestParam(defaultValue = "10",required = false)int pageSize) throws IOException {
        JSONArray reposByPage = httpService.getReposByPage(username, page, pageSize);
        return ControllerResult.ok(reposByPage);
    }

    private ControllerResult getResult(String redisValue) {
        if (redisValue == null) {
            return ControllerResult.ok(null).setCode(10001).setMsg("没有找到该用户的信息，请先从github中获取");
        } else if (redisValue.equals("loading")) {
            return ControllerResult.ok(null).setCode(10002).setMsg("正在从github中获取信息，请稍后...");
        } else if (redisValue.equals("failed")) {
            return ControllerResult.ok(null).setCode(10003).setMsg("从github中获取信息失败，请尝试重新获取");
        } else {
            return ControllerResult.ok(JSONObject.parseObject(redisValue));
        }
    }
}
