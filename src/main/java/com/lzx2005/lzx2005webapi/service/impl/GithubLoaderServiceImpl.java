package com.lzx2005.lzx2005webapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.constant.RedisKey;
import com.lzx2005.lzx2005webapi.service.GithubLoaderService;
import com.lzx2005.lzx2005webapi.service.HttpService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by Lizhengxian on 2018/3/21.
 */
@Service
public class GithubLoaderServiceImpl implements GithubLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(GithubLoaderServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpService httpService;

    @Override
    @Async
    public void startLoadUserInfo(String username) {
        BoundHashOperations userInfo = redisTemplate.boundHashOps(RedisKey.USER_INFO);
        userInfo.put(username, "loading");
        String url = "https://api.github.com/users/" + username;
        logger.info("开始获取用户{}信息：{}", username, url);
        String s = null;
        try {
            s = httpService.get(url);
        } catch (IOException e) {
            logger.error("获取用户信息出错", e);
        }
        if (!StringUtils.isEmpty(s)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("updated", new Date());
            jsonObject.put("data", JSONObject.parseObject(s));
            userInfo.put(username, jsonObject.toJSONString());
        } else {
            userInfo.put(username, "failed");
        }
    }

    @Override
    @Async
    public void startLoadReposInfo(String username) {
    }

    @Override
    @Async
    public void startLoadLanguages(String username) {
        BoundHashOperations userLanguages = redisTemplate.boundHashOps(RedisKey.USER_LANGUAGES);
        userLanguages.put(username, "loading");
        boolean hasNext = true;
        int page = 1;
        JSONObject languageSave = new JSONObject();
        JSONObject languageObject = new JSONObject();
        try {
            while (hasNext) {
                JSONArray jsonArray = httpService.getReposByPage(username, page, 30);
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean fork = jsonObject.getBoolean("fork");
                        if (!fork) {
                            String languagesUrl = jsonObject.getString("languages_url");
                            String s = httpService.get(languagesUrl);
                            JSONObject lang = JSONObject.parseObject(s);
                            for (String key : lang.keySet()) {
                                Integer integer = languageObject.getInteger(key);
                                if (integer != null) {
                                    integer += lang.getInteger(key);
                                    languageObject.put(key, integer);
                                } else {
                                    languageObject.put(key, lang.getInteger(key));
                                }
                            }
                        }
                    }
                    page++;
                } else {
                    hasNext = false;
                }
            }
            languageSave.put("data", languageObject);
            languageSave.put("updated", new Date());
            userLanguages.put(username, languageSave.toJSONString());
        } catch (Exception e) {
            logger.error("渲染出错", e);
            userLanguages.put(username, "failed");
        }

    }

    @Override
    @Async
    public void startLoadCalendar(String username) {


        BoundHashOperations userCalendar = redisTemplate.boundHashOps(RedisKey.USER_CALENDAR);
        userCalendar.put(username, "loading");
        boolean hasNext = true;
        int page = 1;
        JSONObject calendarObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        dataObject.put("updated", new Date());
        JSONArray repos = new JSONArray();
        try {
            while (hasNext) {
                JSONArray jsonArray = httpService.getReposByPage(username, page, 30);
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean fork = jsonObject.getBoolean("fork");
                        if (!fork) {
                            String repoName = jsonObject.getString("name");
                            String url = "https://api.github.com/repos/" + username + "/" + repoName + "/stats/commit_activity";
                            String s = httpService.get(url);
                            JSONArray calendar = this.calendarFormatter(s);
                            for (int j = 0; j < calendar.size(); j++) {
                                JSONObject weekData = calendar.getJSONObject(j);
                                Long week = weekData.getLong("week");
                                JSONObject weekDataObject = calendarObject.getJSONObject(week + "");
                                if (weekDataObject == null) {
                                    calendarObject.put(week + "", weekData);
                                } else {
                                    JSONArray days = weekDataObject.getJSONArray("days");
                                    JSONArray days1 = weekData.getJSONArray("days");
                                    JSONArray dayNew = new JSONArray(days.size());
                                    for (int k = 0; k < days.size(); k++) {
                                        dayNew.add(days.getInteger(k) + days1.getInteger(k));
                                    }
                                    weekDataObject.put("days", dayNew);
                                    Integer total = weekDataObject.getInteger("total");
                                    Integer total1 = weekData.getInteger("total");
                                    weekDataObject.put("total", total + total1);
                                }
                            }
                            if(calendar.size()>0){
                                repos.add(repoName);
                            }
                        }
                    }
                    page++;
                } else {
                    hasNext = false;
                }
            }
            dataObject.put("recordRepos",repos);
            dataObject.put("data",calendarObject);
            userCalendar.put(username,dataObject.toJSONString());
        } catch (Exception e) {
            logger.error("渲染出错", e);
            //userLanguages.put(username,"failed");
        }
    }

    private JSONArray calendarFormatter(String returnString) {
        if(StringUtils.isEmpty(returnString)){
            return new JSONArray();
        }
        return JSONArray.parseArray(returnString);
    }
}
