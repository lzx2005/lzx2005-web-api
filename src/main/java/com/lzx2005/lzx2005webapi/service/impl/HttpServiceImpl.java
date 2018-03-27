package com.lzx2005.lzx2005webapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.lzx2005.lzx2005webapi.service.HttpService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Lizhengxian on 2018/3/27.
 */
@Service
public class HttpServiceImpl implements HttpService{
    private String to = "4484de3309ae42dc1f50", ken = "88201d2c53f10c0c1279";
    private BasicHeader authorization = new BasicHeader("Authorization", "bearer " + to + ken);

    @Override
    public JSONArray getReposByPage(String username, int page, int pageSize) throws IOException {
        String s = get("https://api.github.com/users/" + username + "/repos?sort=updated&type=owner&page=" + page + "&per_page="+pageSize);
        if (!StringUtils.isEmpty(s)) {
            return JSONArray.parseArray(s);
        } else {
            return new JSONArray();
        }
    }

    @Override
    public String get(String url) throws IOException {
        System.out.println("request : " + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(authorization);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
            }
        } finally {
            response.close();
        }
        return null;
    }
}
