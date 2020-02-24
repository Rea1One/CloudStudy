package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 郭瑞景
 * @date 2020-02-24
 */

@Mapper
public interface UserMapper {
    User findUserById(Integer id);
    User findUserByEmail(String email);
    List<User> findUserByName(String name);
    void insertUser(User user);
    void deleteUser(Integer id);
    void updateUserInfo(User user);
}
