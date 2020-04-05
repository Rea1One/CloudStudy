package com.whu.cloudstudy_server.entity;

import java.sql.Timestamp;

/**
 * Author: 叶瑞雯
 * Date: 2020-04-05
 */

public class CountDown {
    private Integer id;
    private Integer userId;
    private String name;
    private String remark;
    private Timestamp endTime;

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

    public void setRemark(String remark){
        this.remark=remark;
    }

    public String getRemark(){
        return remark;
    }

    public void setEndTime(Timestamp endTime){
        this.endTime=endTime;
    }

    public Timestamp getEndTime(){
        return endTime;
    }
}
