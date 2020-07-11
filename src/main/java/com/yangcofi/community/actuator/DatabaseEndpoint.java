package com.yangcofi.community.actuator;

import com.yangcofi.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @ClassName DatabaseEndpoint
 * @Description TODO
 * @Author YangC
 * @Date 2020/5/30 15:26
 **/
@Component
@Endpoint(id = "database")
public class DatabaseEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndpoint.class);
    //尝试去获取一个链接 取得的就是ok的
    //可以把数据库连接池注入进来，getConnection
    @Autowired
    private DataSource dataSource;

    //get请求
    @ReadOperation
    public String checkConnection(){
        try (
                //写在小括号里可以自动关闭
                Connection conn = dataSource.getConnection()
        ){
            return CommunityUtil.getJSONString(0, "获取连接成功！");
        } catch (SQLException e) {
            logger.error("获取连接失败：" + e.getMessage());
            return CommunityUtil.getJSONString(1, "获取连接失败！");
        }
    }

}
