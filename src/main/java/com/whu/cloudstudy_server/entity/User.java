package com.whu.cloudstudy_server.entity;

import java.sql.Timestamp;

/**
 * Author: 郭瑞景
 * Date: 2020-02-24
 */

public class User {
    protected Integer id;
    protected String name;
    protected String password;
    protected Integer sex;
    protected Integer age;
    protected String signature;
    protected String email;
    protected String photo;
    protected Integer studyTime;
    protected Timestamp registerTime;

    public User() {
    }

    public User(Integer id, String name, String password, Integer sex, Integer age,
                String signature, String email, String photo, Integer studyTime, Timestamp registerTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.signature = signature;
        this.email = email;
        this.photo = photo;
        this.studyTime = studyTime;
        this.registerTime = registerTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }
}