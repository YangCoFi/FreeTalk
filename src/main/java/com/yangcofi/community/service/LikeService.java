package com.yangcofi.community.service;

import com.yangcofi.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @ClassName LikeService
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/30 21:35
 **/
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞的业务方法
    public void like(int userId, int entityType, int entityId, int entityUserId){     //userId是谁点的赞
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        //判断当前这个用户点没点过赞 userId在不在这个集合里就可以判断你点没点过赞
//        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (isMember){
//            redisTemplate.opsForSet().remove(entityLikeKey, userId);
//        }else {
//            redisTemplate.opsForSet().add(entityLikeKey, userId);
//        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);         //被赞的人的userId 实体的作者
                Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);   //Redis事务特殊 查询一定要在事务之外 写在事务之内是不会立即得到结果的

                operations.multi();

                if (isMember){
                    operations.opsForSet().remove(entityLikeKey, userId);       //对实体为key的数据进行处理
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId){
        //看key对应的值里面有几个userId 有几个就是几个数量
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体点赞的状态 即他是否点过赞 这里用int 因为将来业务会拓展 还会有踩
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    //查询某个用户获得的赞的数量
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}
