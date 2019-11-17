package com.yangcofi.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Event
 * @Description TODO
 * @Author YangC
 * @Date 2019/9/9 9:57
 **/
public class Event {

    private String topic;
    private int userId;         //事件触发者 张三给李四点赞 这就是张三
    private int entityType;
    private int entityId;
    private int entityUserId;   //实体作者      李四
    //其他的数据存在一个Map里，让他具有可扩展性
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;            //在set topic之后，还会set其他的属性 加一个很多参数的构造器会很麻烦
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
