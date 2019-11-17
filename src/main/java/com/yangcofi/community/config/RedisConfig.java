package com.yangcofi.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/30 11:18
 **/

@Configuration
public class RedisConfig {          //RedisTemplate是SpringDataRedis中对JedisApi的高度封装。SpringDataRedis 相对于 Jedis 来说可以方便地更换 Redis 的 Java客户端，比 Jedis 多了自动管理连接池的特性

    @Bean               //定义第三方的bean 你要将哪个对象装配到容器当中
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){     //在方法上声明了这个参数 自动将这个bean创建进来
        //利用template访问数据库 要想具有访问数据库的能力 就得创建链接 链接是由链接工厂创建的
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);     //让template有了工厂 具备了访问数据库的能力

        // 设置key的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置普通value的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        //为了让template里的这些参数生效
        template.afterPropertiesSet();
        return template;
    }
}
