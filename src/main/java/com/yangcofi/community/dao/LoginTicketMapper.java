package com.yangcofi.community.dao;

import com.yangcofi.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @InterfaceName LoginTicketMapper
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/15 11:52
 **/
@Mapper
@Deprecated         //声明一下这个组件不推荐使用
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(@Param("ticket") String ticket);

    //不要删除数据 改个状态
    @Update({
//            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket}  "
//            "<if test=\"ticket!=null\"> ",
//            "and 1=1 ",
//            "</if> ",
//            "</script>"
    })
    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);

}
