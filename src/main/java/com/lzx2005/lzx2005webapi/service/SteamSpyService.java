package com.lzx2005.lzx2005webapi.service;

/**
 * SteamSpy爬虫
 * Created by Lizhengxian on 2018/4/24.
 */
public interface SteamSpyService {

    /**
     * 读取首页
     */
    void readIndex(String cookies);
}
