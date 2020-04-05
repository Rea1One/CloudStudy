package com.whu.cloudstudy_server.entity;

import java.sql.Timestamp;

/**
 * Author: 叶瑞雯
 * Date: 2020-04-05
 */

public class Todolist {
    private Integer id;
    private Integer userId;
    private String name;
    private Timestamp createTime;

    public void setId(Integer id){
        this.id=id;
    }

    public Integer getId(){
        return id;
    }

    public void setUserId(Integer userId){
        this.userId=userId;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setCreateTime(Timestamp createTime){
        this.createTime=createTime;
    }

    public Timestamp getCreateTime(){
        return createTime;
    }
}
