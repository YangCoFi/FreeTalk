package com.yangcofi.community.dao;

import com.yangcofi.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName CommentMapper
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/21 16:10
 **/
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId, @Param("offset") int offset, @Param("limit") int limit);          //查询帖子的评论呢 还是评论的评论呢

    //查询数据的条目数
    int selectCountByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(@Param("id") int id);
}
