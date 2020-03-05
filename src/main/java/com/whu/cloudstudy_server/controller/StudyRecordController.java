package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.Response;
import com.whu.cloudstudy_server.service.StudyRecordServiceGuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郭瑞景
 * @date 2020-03-03
 */

@RestController
@RequestMapping(value = "/galaxy/planet")
public class StudyRecordController {
    @Autowired
    private StudyRecordServiceGuo recordService;

    @PostMapping(value = "/start")
    public Response startStudy(Integer userId, Integer planetId) {
        Response response;
        int cnt = recordService.startStudy(userId, planetId);
        if (cnt > 0) {
            response = new Response(0, "成功", null);
        } else {
            response = new Response(-1, "插入记录失败", null);
        }
        return response;
    }

    @PostMapping(value = "/stop")
    public Response stopStudy(Integer userId, Integer planetId) {
        Response response;
        int ret = recordService.stopStudy(userId, planetId);
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
}
