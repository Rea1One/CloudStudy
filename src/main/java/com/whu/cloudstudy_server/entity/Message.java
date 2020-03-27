package com.whu.cloudstudy_server.entity;

import java.sql.Timestamp;

/**
 * @author 胡龙晨
 * @date 2020-02-26
 */
public class Message{
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Timestamp createTime;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getSenderId(){
        return senderId;
    }

    public void setSenderId(Integer senderId){
        this.senderId = senderId;
    }

    public Integer getReceiverId(){
        return receiverId;
    }

    public void setReceiverId(Integer receiverId){
        this.receiverId = receiverId;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Timestamp getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Timestamp createTime){
        this.createTime = createTime;
    }
}
