package org.spring.springboot.dao;

import org.apache.ibatis.annotations.Param;
import org.spring.springboot.domain.User;

import java.util.List;

/**
 *
 * 用户 User  接口类
 *
 */
public interface UserDao {

    //根据用户名查询用户信息
    User fingUserByUserName(@Param("loginName") String loginName);

    //查询所有的用户信息
    List<User>selecetAll(User user);

   /* //根据手机号码查询用户信息
    User fingUserByPhoneNum(String phoneNum);*/

    //删除用户信息
    void deleteUserById(Long id);

    //修改用户信息
    void updateUserInfo(User user);

    //注册添加用户信息
    void addUserInfo(User user);

    //查询总数
    Long getCountAll();

    //分页查询用户信息
    List<User>selecetPage();

}
