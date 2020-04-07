package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.PlanetServiceGuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2020-03-03
 */

@RestController
@RequestMapping(value = "/galaxy")
public class PlanetController {
    @Autowired
    private PlanetServiceGuo planetService;

    // 搜索星球
    @PostMapping(value = "/searchPlanet")
    public Response<Object> searchPlanet(String keyword) {
        Response<Object> response;
        List<Planet> planets = planetService.searchPlanet(keyword);
        if (planets.size() == 0) {
            response = new Response<>(-1, "无搜索结果", null);
            return response;
        }
        response = new Response<>(0, "成功", planets);
        return response;
    }

    // 进入星球
    @PostMapping(value = "/planet/enterPlanet")
    public Response<Object> enterPlanet(Integer planetId, Integer userId) {
        return planetService.enterPlanet(planetId, userId);
    }

    // 退出星球
    @PostMapping(value = "/leavePlanet")
    public Response<Object> leavePlanet(Integer planetId, Integer userId) {
        Response<Object> response;
        int ret = planetService.leavePlanet(planetId, userId);
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "记录不存在", null);
                break;
            case -2:
                response = new Response<>(-2, "删除记录失败", null);
                break;
            default:
                response = new Response<>(-3, "失败", null);
                break;
        }
        return response;
    }
}
