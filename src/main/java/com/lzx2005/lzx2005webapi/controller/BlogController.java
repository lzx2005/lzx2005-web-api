package com.lzx2005.lzx2005webapi.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzx2005.lzx2005webapi.dao.BlogMapper;
import com.lzx2005.lzx2005webapi.model.Blog;
import com.lzx2005.lzx2005webapi.model.BlogExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lizhengxian on 2018/3/19.
 */
@RestController("/blog")
public class BlogController {

    @Autowired
    private BlogMapper blogMapper;


    @GetMapping
    public PageInfo<Blog> blogs(int page,int pageSize){
        PageInfo<Blog> blogs = PageHelper
                .startPage(page, pageSize)
                .doSelectPageInfo(()-> blogMapper.selectByExample(new BlogExample()));
        return blogs;
    }
}
