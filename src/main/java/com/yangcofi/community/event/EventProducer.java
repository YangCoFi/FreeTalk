package com.yangcofi.community.event;

import com.alibaba.fastjson.JSONObject;
import com.yangcofi.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName EventProducer
 * @Description TODO
 * @Author YangC
 * @Date 2019/9/9 10:10
 **/
@Component
public class EventProducer {            //生产者用KafkaTemplate去发送消息

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //处理事件
    public void fireEvent(Event event){
        //将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));     //应包含event事件中所有的数据 办法就是转换成Json 消费者将其还原
    }

}
