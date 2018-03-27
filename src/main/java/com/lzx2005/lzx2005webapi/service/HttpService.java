package com.lzx2005.lzx2005webapi.service;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;

/**
 * Http请求相关
 * Created by Lizhengxian on 2018/3/27.
 */
public interface HttpService {

    String get(String username) throws IOException;

    JSONArray getReposByPage(String username, int page, int pageSize) throws IOException;
}
