package com.lzx2005.lzx2005webapi.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzx2005.lzx2005webapi.dto.ControllerResult;
import com.lzx2005.lzx2005webapi.dao.BlogMapper;
import com.lzx2005.lzx2005webapi.model.BlogExample;
import com.lzx2005.lzx2005webapi.model.BlogWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Created by Lizhengxian on 2018/3/19.
 */
@RestController()
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogMapper blogMapper;


    @GetMapping("/list")
    public PageInfo<BlogWithBLOBs> blogs(@RequestParam(required = false,defaultValue = "1") int page,
                                         @RequestParam(required = false,defaultValue = "10") int pageSize){
        BlogExample blogExample = new BlogExample();
        blogExample.setOrderByClause("create_time desc");
        PageInfo<BlogWithBLOBs> blogs = PageHelper
                .startPage(page, pageSize)
                .doSelectPageInfo(()-> blogMapper.selectByExampleWithBLOBs(blogExample));
        blogs.getList().stream().map(blogWithBLOBs -> {
            blogWithBLOBs.setContent("");
            return blogWithBLOBs;
        }).collect(Collectors.toList());
        return blogs;
    }



    @GetMapping("/{blogId}")
    public ControllerResult blog(@PathVariable("blogId") long blogId){
        BlogWithBLOBs blogWithBLOBs = blogMapper.selectByPrimaryKey(blogId);
        return ControllerResult.ok(blogWithBLOBs);
    }
}
