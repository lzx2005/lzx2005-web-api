package com.lzx2005.lzx2005webapi.dao;

import com.lzx2005.lzx2005webapi.model.BlogType;
import com.lzx2005.lzx2005webapi.model.BlogTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int countByExample(BlogTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int deleteByExample(BlogTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int deleteByPrimaryKey(Long blogTypeId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int insert(BlogType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int insertSelective(BlogType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    List<BlogType> selectByExample(BlogTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    BlogType selectByPrimaryKey(Long blogTypeId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int updateByExampleSelective(@Param("record") BlogType record, @Param("example") BlogTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int updateByExample(@Param("record") BlogType record, @Param("example") BlogTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int updateByPrimaryKeySelective(BlogType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog_type
     *
     * @mbggenerated Mon Mar 19 16:13:31 CST 2018
     */
    int updateByPrimaryKey(BlogType record);
}