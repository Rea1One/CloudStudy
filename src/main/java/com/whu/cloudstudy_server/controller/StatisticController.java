package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.StatisticService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Author: 叶瑞雯
 * Date: 2020-03-16
 */

@RestController
@RequestMapping(value = "/other")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @PostMapping(value = "/queryStatistics1")
    public Response queryDailyStudyTime(Integer id, Integer batchNum){
        Response response;
        List<Long> studyTime=statisticService.getStudyTimePerDay(id,batchNum);
        if(studyTime==null){
            response=new Response(-1,"失败", null);
            return response;
        }
        response=new Response(0, "成功", studyTime);
        return response;
    }

    @PostMapping(value = "/queryStatistics2")
    public Response queryStudyTimeInGalaxy(Integer id, Integer category){
        Response response;
        Map studyTime=statisticService.getStudyTimeInGalaxy(id,category);
        if(studyTime==null){
            response=new Response(-1,"失败", null);
            return response;
        }
        response=new Response(0, "成功", studyTime);
        return response;
    }

    @PostMapping(value = "/queryThreeMost")
    public Response queryThreeMost(Integer id){
        Response response;
        List<Planet> planets=statisticService.getThreeMostPlanet(id);
        if(planets==null){
            response=new Response(-1,"失败", null);
            return response;
        }
        response=new Response(0,"成功", planets);
        return response;
    }
}
