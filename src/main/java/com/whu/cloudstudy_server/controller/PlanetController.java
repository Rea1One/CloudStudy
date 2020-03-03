package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.Response;
import com.whu.cloudstudy_server.entity.CustomizedUser;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.PlanetServiceGuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 郭瑞景
 * @date 2020-03-03
 */

@RestController
@RequestMapping(value = "/galaxy")
public class PlanetController {
    @Autowired
    private PlanetServiceGuo planetService;

    @PostMapping(value = "/searchPlanet")
    public Response searchPlanet(String keyword) {
        Response response;
        List<Planet> planets = planetService.searchPlanet(keyword);
        if (planets.size() == 0) {
            response = new Response(-1, "无搜索结果", null);
            return response;
        }
        response = new Response(0, "成功", planets);
        return response;
    }

    @PostMapping(value = "/planet/enterPlanet")
    public Response enterPlanet(Integer id) {
        return planetService.enterPlanet(id);
    }
}
