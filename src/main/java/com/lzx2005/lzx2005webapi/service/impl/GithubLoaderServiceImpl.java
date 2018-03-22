package com.lzx2005.lzx2005webapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.constant.RedisKey;
import com.lzx2005.lzx2005webapi.service.GithubLoaderService;
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

/**
 * Created by Lizhengxian on 2018/3/21.
 */
@Service
public class GithubLoaderServiceImpl implements GithubLoaderService{
    private static final Logger logger = LoggerFactory.getLogger(GithubLoaderServiceImpl.class);

    private String to = "4484de3309ae42dc1f50",ken = "88201d2c53f10c0c1279";
    private BasicHeader authorization = new BasicHeader("Authorization", "bearer " + to + ken);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Async
    public void startLoadUserInfo(String username) {
        BoundHashOperations userInfo = redisTemplate.boundHashOps(RedisKey.USER_INFO);
        userInfo.put(username,"loading");
        String url = "https://api.github.com/users/"+username;
        logger.info("开始获取用户{}信息：{}",username,url);
        String s = null;
        try {
            s = get(url);
        } catch (IOException e) {
            logger.error("获取用户信息出错",e);
        }
        if(!StringUtils.isEmpty(s)){
            userInfo.put(username,s);
        }else{
            userInfo.put(username,"failed");
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
        userLanguages.put(username,"loading");
        boolean hasNext = true;
        int page = 1;
        JSONObject languageObject = new JSONObject();
        try {
            while (hasNext){
                JSONArray jsonArray = getReposByPage(username,page);
                if (jsonArray.size()>0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean fork = jsonObject.getBoolean("fork");
                        if(!fork){
                            String languagesUrl = jsonObject.getString("languages_url");
                            String s = get(languagesUrl);
                            JSONObject lang = JSONObject.parseObject(s);
                            for(String key :lang.keySet()){
                                Integer integer = languageObject.getInteger(key);
                                if(integer!=null){
                                    integer+=lang.getInteger(key);
                                    languageObject.put(key,integer);
                                }else{
                                    languageObject.put(key,lang.getInteger(key));
                                }
                            }
                        }
                    }
                    page++;
                }else{
                    hasNext = false;
                }
            }
            userLanguages.put(username,languageObject.toJSONString());
        }catch (Exception e){
            logger.error("渲染出错",e);
            userLanguages.put(username,"failed");
        }

    }

    private JSONArray getReposByPage(String username, int page) throws IOException {
        String s = get("https://api.github.com/users/" + username + "/repos?sort=updated&type=owner&page=" + page + "&per_page=30");
        if(!StringUtils.isEmpty(s)){
            return JSONArray.parseArray(s);
        }else{
            return new JSONArray();
        }
    }

    private String get(String url) throws IOException {
        System.out.println("request : "+url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(authorization);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            //System.out.println(s);
            return EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } finally {
            response.close();
        }
    }
}
