package org.spring.springboot.service;


import org.spring.springboot.domain.User;

import java.util.List;

/*
 * 用户user逻辑接口类
 *
 * Created by bysocket on 07/02/2017.
 *
 * */


public interface UserService {

    //根据用户名查询用户信息
    User fingUserByUserName(String loginName);

    //根据手机号码查询用户信息
    User fingUserByPhoneNum(String phoneNum);

    //删除用户信息
    void deleteUserById(Long id);

    //修改用户信息
    void updateUserInfo(User user);

    //注册添加用户信息
    void addUserInfo(User user);

    //查询所有的用户信息
    List<User> selecetAll(User user);

    //分页查询
    List<User> getList(int pageNum, int pageSize,User user);

    //获取总数
    int getCountAll();

}
