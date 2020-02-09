package com.yangcofi.community.util;
/**
 * @ClassName RedisKeyUtil
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/30 21:27
 **/
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";   //
    private static final String PREFIX_FOLLOWER = "follower";   //以我关注的那个人为key
    private static final String PREFIX_KAPTCHA = "kaptcha";     //验证码
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";
    private static final String PREFIX_POST = "post";

    //某个实体的赞
    //like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户的赞
    //like:user:userId --> int
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE  + SPLIT + userId;
    }

    //某个用户关注的实体
    //followee:userId:entityType       ----> Zset  有序集合 里面存entityId，以当前时间的整数形式作为分数
    public static String getFolloweeKey(int userId, int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个实体拥有的粉丝
    //follower:entityType:entityId  --->Zset 里面存的是粉丝 也就是userId，同样以时间作为分数
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


    //拼验证码的key  获取验证码的时候，应该这个验证码和某个用户是相关的 要识别出来这个验证码是属于哪个用户的
    //当某个用户打开登录页面，看到验证码的时候，这个时候还没有登录，此时还不知道他是哪个User，传不了UserId。
    //可以在用户访问登录页面的时候，给他发一个凭证，随机生成的字符串，存到Cookie里，以这个字符串临时标识一下这个用户，很快让其过期
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //形成登录凭证的方法
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;          //将LoginTicketMapper废弃掉
    }

    //用户
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String date){
        return PREFIX_UV + SPLIT + date;
    }

    //区间UV
    public static String getUVKey(String startDate, String endDate){
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    //单日活跃用户
    public static String getDAUKey(String date){
        return PREFIX_DAU + SPLIT + date;
    }

    //区间活跃用户
    public static String getDAUKey(String startDate, String endDate){
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    //帖子分数
    public static String getPostScoreKey(){
        return PREFIX_POST + SPLIT + "score";
    }
}
