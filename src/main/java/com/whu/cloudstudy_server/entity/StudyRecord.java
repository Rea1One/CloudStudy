package com.whu.cloudstudy_server.entity;

import java.sql.Timestamp;

/**
 * Author: 郭瑞景
 * Date: 2020-03-02
 */
public class StudyRecord {
    private Integer id;
    private Integer userId;
    private Integer planetId;
    private Integer operation;
    private Timestamp time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsreId() {
        return userId;
    }

    public void setUsreId(Integer usreId) {
        this.userId = usreId;
    }

    public Integer getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Integer planetId) {
        this.planetId = planetId;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
