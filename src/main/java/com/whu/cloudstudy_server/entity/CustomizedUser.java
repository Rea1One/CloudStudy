package com.whu.cloudstudy_server.entity;

import com.whu.cloudstudy_server.entity.User;

import java.sql.Timestamp;

/**
 * @author 郭瑞景
 * @date 2020-03-03
 */
public class CustomizedUser extends User {
    private String statusInfo;

    public CustomizedUser() {
    }

    public CustomizedUser(Integer id, String name, String password, Integer sex, Integer age, String signature,
                          String email, String photo, Integer studyTime, Timestamp registerTime, String statusInfo) {
        super(id, name, password, sex, age, signature, email, photo, studyTime, registerTime);
        this.statusInfo = statusInfo;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }
}
