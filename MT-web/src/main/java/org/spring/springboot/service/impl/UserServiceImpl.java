package org.spring.springboot.service.impl;

import com.github.pagehelper.PageHelper;
import org.spring.springboot.dao.UserDao;
import org.spring.springboot.domain.User;
import org.spring.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User fingUserByUserName(String loginName) {
       return userDao.fingUserByUserName(loginName);
    }

    @Override
    public User fingUserByPhoneNum(String phoneNum) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {
        userDao.deleteUserById(id);
    }

    @Override
    public void updateUserInfo(User user) {
        user.setUpdateTime(new Date());
        userDao.updateUserInfo(user);
    }

    @Override
    public void addUserInfo(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userDao.addUserInfo(user);
    }

    @Override
    public List<User> selecetAll(User user){
            return userDao.selecetAll(user);
    }


    /**
     *
     * @Title: getList
     * @param pageNum 当前页
     * @param pageSize 当前页面展示数目
     * @return
     * @throws Exception
     */
    @Override
    public List<User> getList(int pageNum, int pageSize,User user) {

        //使用分页插件,核心代码就这一行
        PageHelper.startPage(pageNum, pageSize);
        return userDao.selecetAll(user);
    }

    @Override
    public int getCountAll() {
        return userDao.getCountAll().intValue();
    }
}
