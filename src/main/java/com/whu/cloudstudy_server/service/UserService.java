package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author 叶瑞雯
 * @date 2020-02-25
 */

@Service
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    public User findUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void updateUserInfo(User user) { userMapper.updateUserInfo(user); }
}
