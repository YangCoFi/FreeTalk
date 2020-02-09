package com.yangcofi.community.event;

import com.alibaba.fastjson.JSONObject;
import com.yangcofi.community.entity.DiscussPost;
import com.yangcofi.community.entity.Event;
import com.yangcofi.community.entity.Message;
import com.yangcofi.community.service.DiscussPostService;
import com.yangcofi.community.service.ElasticsearchService;
import com.yangcofi.community.service.MessageService;
import com.yangcofi.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName EventConsumer
 * @Description TODO
 * @Author YangC
 * @Date 2019/9/9 10:17
 **/
@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    //处理 最终是要给某个人发消息 就是往message表插入数据
    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    //查到的数据需要存到ES服务器里
    @Autowired
    private ElasticsearchService elasticsearchService;

    @Value("${wk.image.command}")
    private String wkImageCommand;

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})             //这个方法可以消费3个主题
    public void handleCommentMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }

        //发站内信
        Message message = new Message();        //id是自增长 不用设置
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());    //存主题
        message.setCreateTime(new Date());
        //设置内容
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (! event.getData().isEmpty()){
            for (Map.Entry<String, Object> entry : event.getData().entrySet()){
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    //消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }

    //消费删帖事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }
        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

    //消费分享事件
    @KafkaListener(topics = TOPIC_SHARE)
    public void handleShareMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }

        String htmlUrl = (String) event.getData().get("htmlUrl");
        String fileName = (String) event.getData().get("fileName");
        String suffix = (String) event.getData().get("suffix");

        String cmd = wkImageCommand + " --quality 75 "
                + htmlUrl + " " + wkImageStorage + "/" + fileName + suffix;
        try {
            Runtime.getRuntime().exec(cmd);
            logger.info("生成长图成功： " + cmd);
        } catch (IOException e) {
            logger.error("生成长图失败： " + e.getMessage());
        }
    }
}
