package com.whu.cloudstudy_server.entity;

/**
 * Author: 叶瑞雯
 * Date: 2020-04-05
 */

public class TodolistItem {
    private Integer id;
    private Integer todolistId;
    private String content;
    private Integer priority;
    private Integer isFinished;

    public void setId(Integer id){
        this.id=id;
    }

    public Integer getId(){
        return id;
    }

    public void setTodolistId(Integer todolistId){
        this.todolistId=todolistId;
    }

    public Integer getTodolistId(){
        return todolistId;
    }

    public void setContent(String content){
        this.content=content;
    }

    public String getContent(){
        return content;
    }

    public void setPriority(Integer priority){
        this.priority=priority;
    }

    public Integer getPriority(){
        return priority;
    }

    public void setIsFinished(Integer isFinished){
        this.isFinished=isFinished;
    }

    private Integer getIsFinished(){
        return isFinished;
    }
}
