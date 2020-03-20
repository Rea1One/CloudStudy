package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.*;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-02-27
 */

@Service
public class FindCodeService{
    @Autowired(required = false)
    private UserMapper userMapper;

    /**
     * 找回密码
     *
     * @param email
     * @param newPassword
     * @return
     */
    public int findPassword(String email,String newPassword){
        User user=UserMapper.findUserByEmail(String email);
        user.setPassword(newPassword);
        int cnt = userMapper.updateUserInfo(user);
        if (cnt > = 0) {
            return 0;
        }
        else {
            return -1;
        }
    }
}