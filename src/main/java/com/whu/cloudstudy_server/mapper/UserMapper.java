package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2020-02-24
 */

@Mapper
public interface UserMapper {
    User findUserById(Integer id);
    User findUserByEmail(String email);
    List<User> findUserByName(String name);
    int insertUser(User user);
    int deleteUser(Integer id);
    int updateUserInfo(User user);
}
