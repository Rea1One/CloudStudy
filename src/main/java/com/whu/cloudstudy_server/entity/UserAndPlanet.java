package com.whu.cloudstudy_server.entity;

import java.sql.Timestamp;

/**
 * @author 叶瑞雯
 * @date 2020-02-29
 */

public class UserAndPlanet {
    private Integer id;
    private Integer userId;
    private Integer planetId;
    private Timestamp enterTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId=userId;
    }

    public Integer getPlanetId(){
        return planetId;
    }

    public void setPlanetId(Integer planetId){
        this.planetId=planetId;
    }

    public Timestamp getEnterTime(){
        return enterTime;
    }

    public void setEnterTime(Timestamp enterTime){
        this.enterTime=enterTime;
    }
}
