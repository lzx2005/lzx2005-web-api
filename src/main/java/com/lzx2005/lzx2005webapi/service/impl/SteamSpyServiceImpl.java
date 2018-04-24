package com.lzx2005.lzx2005webapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzx2005.lzx2005webapi.constant.RedisKey;
import com.lzx2005.lzx2005webapi.service.SteamSpyService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * SteamSpy爬虫实现
 * Created by Lizhengxian on 2018/4/24.
 */
@Service
public class SteamSpyServiceImpl implements SteamSpyService {
    private static final Logger logger = LoggerFactory.getLogger(SteamSpyServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void readIndex(String cookies) {
        String url = "http://steamspy.com/";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";
        CloseableHttpClient client = HttpClientBuilder.create().setUserAgent(userAgent).build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", cookies);
        try {
            CloseableHttpResponse execute = client.execute(httpGet);
            HttpEntity entity = execute.getEntity();
            String html = EntityUtils.toString(entity);
            //logger.info(html);
            Document parse = Jsoup.parse(html);
            Elements tables = parse.getElementsByTag("table");
            logger.info("tableSize={}",tables.size());
            if(tables.size()>2){
                //第一张表：Trending 趋势表
                Elements trs = tables.get(0).getElementsByTag("tr");
                if (trs != null && trs.size() > 0) {
                    JSONArray trendings = new JSONArray(trs.size());
                    for (int i = 0; i < trs.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        Elements tds = trs.get(i).getElementsByTag("td");
                        if (tds.size() > 5) {
                            String id = tds.get(0).text();
                            Elements imgs = tds.get(1).getElementsByTag("img");
                            if(imgs.size()>0){
                                Element element = imgs.get(0);
                                String src = element.attr("src");
                                jsonObject.put("img",src);
                            }
                            String name = tds.get(1).text();
                            String releaseDate = tds.get(2).text();
                            String price = tds.get(3).text();
                            String score = tds.get(4).text();
                            String players = tds.get(5).text();
                            jsonObject.put("id", id);
                            jsonObject.put("name", name);
                            jsonObject.put("releaseDate", releaseDate);
                            jsonObject.put("price", price);
                            jsonObject.put("score", score);
                            jsonObject.put("players", players);
                            trendings.add(jsonObject);
                        }
                    }
                    redisTemplate.opsForValue().set(RedisKey.STEAM_TRENGIND, trendings);
                }

                //第二张表：Recent 新游表
                Elements trs1 = tables.get(1).getElementsByTag("tr");
                if (trs1 != null && trs1.size() > 0) {
                    JSONArray recent = new JSONArray(trs1.size());
                    for (int i = 0; i < trs1.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        Elements tds = trs1.get(i).getElementsByTag("td");
                        if (tds.size() > 6) {
                            String id = tds.get(0).text();
                            Elements imgs = tds.get(1).getElementsByTag("img");
                            if(imgs.size()>0){
                                Element element = imgs.get(0);
                                String src = element.attr("src");
                                jsonObject.put("img",src);
                            }
                            String name = tds.get(1).text();
                            String releaseDate = tds.get(2).text();
                            String price = tds.get(3).text();
                            String score = tds.get(4).text();
                            String players = tds.get(5).text();
                            String playTime = tds.get(6).text();
                            jsonObject.put("id", id);
                            jsonObject.put("name", name);
                            jsonObject.put("releaseDate", releaseDate);
                            jsonObject.put("price", price);
                            jsonObject.put("score", score);
                            jsonObject.put("players", players);
                            jsonObject.put("playTime", playTime);
                            recent.add(jsonObject);
                        }
                    }
                    redisTemplate.opsForValue().set(RedisKey.STEAM_RECENT, recent);
                }

                //第三张表：Top 销量表
                Elements trs2 = tables.get(2).getElementsByTag("tr");
                if (trs2 != null && trs2.size() > 0) {
                    JSONArray top = new JSONArray(trs2.size());
                    for (int i = 0; i < trs2.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        Elements tds = trs2.get(i).getElementsByTag("td");
                        if (tds.size() > 7) {
                            String id = tds.get(0).text();
                            Elements imgs = tds.get(1).getElementsByTag("img");
                            if(imgs.size()>0){
                                Element element = imgs.get(0);
                                String src = element.attr("src");
                                jsonObject.put("img",src);
                            }
                            String name = tds.get(1).text();
                            String releaseDate = tds.get(2).text();
                            String price = tds.get(3).text();
                            String score = tds.get(4).text();
                            String players = tds.get(5).text();
                            String playersPercent = tds.get(6).text();
                            String playTime = tds.get(7).text();
                            jsonObject.put("id", id);
                            jsonObject.put("name", name);
                            jsonObject.put("releaseDate", releaseDate);
                            jsonObject.put("price", price);
                            jsonObject.put("score", score);
                            jsonObject.put("players", players);
                            jsonObject.put("playersPercent", playersPercent);
                            jsonObject.put("playTime", playTime);
                            top.add(jsonObject);
                        }
                    }
                    redisTemplate.opsForValue().set(RedisKey.STEAM_TOP, top);
                }
            }
        } catch (IOException e) {
            logger.error("请求出错", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("释放资源出错", e);
            }
        }
    }
}
