package com.yangcofi.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName CommunityUtil
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/13 14:26
 **/
public class CommunityUtil {
    //生成一个随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    //MD5加密(只能加密，不能解密)
    //加上一个随机的字符串 salt
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null){
            for (String key : map.keySet()){
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", "25");
        System.out.println(getJSONString(0, "OK", map));        //类似得到这样的值{"msg":"OK","code":0,"name":"zhangsan","age":"25"} 传给浏览器 浏览器将它转换成JS对象，然后可以得到每一个key对应的值 便于前后端交互
    }
}
