package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.StudyRecordServiceGuo;
import com.whu.cloudstudy_server.service.StudyTimeService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2020-04-09
 */

@RestController
@RequestMapping(value = "/other")
public class StudyTimeController {
    @Autowired
    private StudyTimeService timeService;

    @PostMapping("/getTotalStudyTime")
    public Response<Object> getTotalStudyTimeFromRecord(Integer userId) {
        Response<Object> response;
        Integer totalTime = timeService.getTotalStudyTimeFromRecord(userId);
        if (totalTime == -1) {
            response = new Response<>(-1, "用户不存在", null);
            return response;
        }
        response = new Response<>(0, "成功", totalTime);
        return response;
    }
}
