package com.lzx2005.lzx2005webapi.controller;

import com.lzx2005.lzx2005webapi.dao.BlogMapper;
import com.lzx2005.lzx2005webapi.model.Blog;
import com.lzx2005.lzx2005webapi.model.BlogExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Lizhengxian on 2018/3/19.
 */
@RestController
public class HelloController {

    @Autowired
    private BlogMapper blogMapper;

    @GetMapping("/")
    public String hello(){

        BlogExample blogExample = new BlogExample();
        List<Blog> blogs = blogMapper.selectByExample(blogExample);
        blogs.forEach(blog -> {
            System.out.println(blog.getTitle());
        });
        return "hello";
    }
}
