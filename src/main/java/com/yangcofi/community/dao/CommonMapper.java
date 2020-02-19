package com.yangcofi.community.dao;

import com.yangcofi.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @InterfaceName CommonMapper
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/10 18:14
 **/
@Mapper
public interface CommonMapper {
    User getUserByEmail(@Param("email") String email);

    void updatePwdById(@Param("id") int id, @Param("password") String password);
}
