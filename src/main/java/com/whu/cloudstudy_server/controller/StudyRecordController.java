package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.StudyRecordService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class StudyRecordController {
    @Autowired
    private StudyRecordService recordService;

    @PostMapping(value = "/galaxy/planet/start")
    public Response<Object> startStudy(Integer userId, Integer planetId) {
        Response<Object> response;
        int ret = recordService.startStudy(userId, planetId);
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "已开始自习", null);
                break;
            case -2:
                response = new Response<>(-2, "插入记录失败", null);
                break;
            default:
                response = new Response<>(-3, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/galaxy/planet/startBroadcast")
    public Response<Object> startBroadcast(Integer userId, Integer planetId) {
        Response<Object> response;
        int ret = recordService.startBroadcast(userId, planetId);
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "正在学习", null);
                break;
            case -2:
                response = new Response<>(-1, "正在直播", null);
                break;
            case -3:
                response = new Response<>(-2, "插入学习记录失败", null);
                break;
            default:
                response = new Response<>(-3, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/galaxy/planet/watchBroadcast")
    public Response<Object> watchBroadcast(Integer userId, Integer planetId) {
        Response<Object> response;
        if (!recordService.isOnBroadcast(planetId)) {
            response = new Response<>(-4, "主播未上线", null);
            return response;
        }
        int ret = recordService.startStudy(userId, planetId);
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "正在学习", null);
                break;
            case -2:
                response = new Response<>(-2, "插入学习记录失败", null);
                break;
            default:
                response = new Response<>(-3, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/other/queryStatistics1")
    public Response<Object> queryDailyStudyTime(Integer id, Integer batchNum) {
        Response<Object> response;
        List<Long> studyTime = recordService.getStudyTimePerDay(id, batchNum);
        if (studyTime == null) {
            response = new Response<>(-1, "失败", null);
            return response;
        }
        response = new Response<>(0, "成功", studyTime);
        return response;
    }

    @PostMapping(value = "/other/queryStatistics2")
    public Response<Object> queryStudyTimeInGalaxy(Integer id, Integer category) {
        Response<Object> response;
        Map<Integer, Long> studyTime = recordService.getStudyTimeInGalaxy(id, category);
        if (studyTime == null) {
            response = new Response<>(-1, "失败", null);
            return response;
        }
        response = new Response<>(0, "成功", studyTime);
        return response;
    }

    @PostMapping("/other/getTotalStudyTime")
    public Response<Object> getTotalStudyTimeFromRecord(Integer userId) {
        Response<Object> response;
        Integer totalTime = recordService.getTotalStudyTimeFromRecord(userId);
        if (totalTime == -1) {
            response = new Response<>(-1, "用户不存在", null);
            return response;
        }
        if (totalTime == -2) {
            response = new Response<>(-2, "修改自习时长失败", null);
            return response;
        }
        response = new Response<>(0, "成功", totalTime);
        return response;
    }
}
