package com.yangcofi.community.controller;

import com.yangcofi.community.entity.Event;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.event.EventProducer;
import com.yangcofi.community.service.LikeService;
import com.yangcofi.community.util.CommunityConstant;
import com.yangcofi.community.util.CommunityUtil;
import com.yangcofi.community.util.HostHolder;
import com.yangcofi.community.util.RedisKeyUtil;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LikeController
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/31 8:47
 **/
@Controller
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody                   //点赞是异步请求 不用刷新整个页面
    public String like(int entityType, int entityId, int entityUserId, int postId){
        User user = hostHolder.getUser();           //这里不用判断用户是否登录 用拦截器实现就可以 我们后面自己实现
        //点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        //数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        //状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        //触发点赞事件
        if (likeStatus == 1){       //点赞的时候才去触发点赞事件
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }

        if (entityType  == ENTITY_TYPE_POST){
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, postId);
        }
        //返回一个json格式的数据
        return CommunityUtil.getJSONString(0, null, map);       //状态0成功 消息为空 map数据返回给页面
    }
}
