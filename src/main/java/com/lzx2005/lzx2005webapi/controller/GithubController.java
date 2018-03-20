package com.lzx2005.lzx2005webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.dto.ControllerResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
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
@RequestMapping("/github")
public class GithubController {

    private String to = "4484de3309ae42dc1f50",ken = "88201d2c53f10c0c1279";
    private BasicHeader authorization = new BasicHeader("Authorization", "bearer " + to + ken);

    @GetMapping("/info")
    public ControllerResult info(@RequestParam String username,@RequestParam(required = false,defaultValue = "1") int page) throws IOException {
        String s = get("https://api.github.com/users/" + username + "/repos?sort=updated&type=owner&page=" + page + "&per_page=10");
        return ControllerResult.ok(JSONArray.parseArray(s));
    }

    @GetMapping("/languages")
    public ControllerResult languages(@RequestParam String username) throws IOException {
        boolean hasNext = true;
        int page = 1;
        JSONObject languageObject = new JSONObject();
        while (hasNext){
            ControllerResult info = this.info(username,page);
            if (info != null) {
                JSONArray data = (JSONArray) info.getData();
                if(data.size()==0){
                    hasNext=false;
                }
                for (int i = 0; i < data.size(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
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
            }
        }

        return ControllerResult.ok(languageObject);
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
