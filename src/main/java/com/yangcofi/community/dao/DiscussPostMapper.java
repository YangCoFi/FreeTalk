package com.yangcofi.community.dao;

import com.yangcofi.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit, @Param("orderMode") int orderMode);

    // @Param注解用于给参数取别名
    // 如果只有一个参数,并且在<if>里使用,则必须加别名。
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    //查询帖子的详情，其实就是根据帖子的id查询到帖子的详细信息 配置文件的 <select id=就是这个方法名
    DiscussPost selectDiscussPostById(@Param("id") int id);

    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    //修改帖子类型
    int updateType(@Param("id") int id, @Param("type") int type);

    //修改帖子状态
    int updateStatus(@Param("id") int id, @Param("status") int status);

    int updateScore(@Param("id") int id, @Param("score") double score);
}
