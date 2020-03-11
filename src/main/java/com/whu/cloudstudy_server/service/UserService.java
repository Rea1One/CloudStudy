package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Author: 叶瑞雯
 * Date: 2020-02-25
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

    @Transactional
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Transactional
    public int deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }

    @Transactional
    public int updateUserInfo(User user) { return userMapper.updateUserInfo(user); }
}
