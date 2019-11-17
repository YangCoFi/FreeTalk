package com.yangcofi.community.controller;

import com.yangcofi.community.entity.Comment;
import com.yangcofi.community.entity.DiscussPost;
import com.yangcofi.community.entity.Event;
import com.yangcofi.community.event.EventProducer;
import com.yangcofi.community.service.CommentService;
import com.yangcofi.community.service.DiscussPostService;
import com.yangcofi.community.util.CommunityConstant;
import com.yangcofi.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @ClassName CommentController
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/24 9:46
 **/
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;      //为了得到当前用户的Id

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)      //因为我后面需要重定向的地址有帖子id 所以在这里面需要
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){       //除了评论内容以外 评论给什么实体Id,实体Type需要隐含传过来的
        comment.setUserId(hostHolder.getUser().getId());            //万一没登陆怎么办 直接添加会报错 这里我们后面会统一处理异常
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if (comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentById(comment.getEntityId()); //评论的实体ID就是目标
            event.setEntityUserId(target.getUserId());
        }
        //发布消息 处理事件
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_POST){
            //触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
