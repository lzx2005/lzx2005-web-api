package com.lzx2005.lzx2005webapi.service;

import java.io.IOException;

/**
 * Created by Lizhengxian on 2018/3/21.
 */
public interface GithubLoaderService {

    void startLoadUserInfo(String username);
    void startLoadReposInfo(String username);
    void startLoadLanguages(String username) throws IOException;
}
