package com.lzx2005.lzx2005webapi.service.impl;

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

            try {
                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
