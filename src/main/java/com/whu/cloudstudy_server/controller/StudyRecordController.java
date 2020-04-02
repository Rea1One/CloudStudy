package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.UserAndPlanet;
import com.whu.cloudstudy_server.service.PlanetService;
import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.service.StudyRecordService;
import com.whu.cloudstudy_server.service.StudyRecordServiceGuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2020-03-03
 */

@RestController
@RequestMapping(value = "/galaxy/planet")
public class StudyRecordController {
    @Autowired
    private StudyRecordServiceGuo recordService;

    @PostMapping(value = "/start")
    public Response startStudy(Integer userId, Integer planetId) {
        Response response;
        int ret = recordService.startStudy(userId, planetId);
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "已开始自习", null);
                break;
            case -2:
                response = new Response(-2, "插入记录失败", null);
                break;
            default:
                response = new Response(-3, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/stop")
    public Response stopStudy(Integer userId, Integer planetId) {
        Response response;
        int ret = recordService.stopStudy(userId, planetId, 1);
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "用户不存在", null);
                break;
            case -2:
                response = new Response(-2, "用户没有开始学习的记录", null);
                break;
            case -3:
                response = new Response(-3, "插入学习记录数据失败", null);
                break;
            case -4:
                response = new Response(-4, "更新用户学习时间失败", null);
                break;
            default:
                response = new Response(-5, "失败", null);
                break;
        }
        return response;
    }

    @Autowired
    StudyRecordService studyRecordService;

    @PostMapping(value = "/startBroadcast")
    public Response startBroadcast(Integer userId, Integer planetId) {
        Response response;
        int ret = studyRecordService.startBroadcast(userId, planetId);
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "正在学习", null);
                break;
            case -2:
                response = new Response(-1, "正在直播", null);
                break;
            case -3:
                response = new Response(-2, "插入学习记录失败", null);
                break;
            default:
                response = new Response(-3, "失败", null);
                break;
        }
        return response;
    }

    @Autowired
    PlanetService planetService;

    @PostMapping(value = "/watchBroadcast")
    public Response watchBroadcast(Integer userId, Integer planetId) {
        Response response;
        if(!studyRecordService.isOnBroadcast(planetId)){
            response = new Response(-4, "主播未上线", null);
            return response;
        }
        int ret = recordService.startStudy(userId, planetId);
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "正在学习", null);
                break;
            case -2:
                response = new Response(-2, "插入学习记录失败", null);
                break;
            default:
                response = new Response(-3, "失败", null);
                break;
        }
        return response;
    }
}