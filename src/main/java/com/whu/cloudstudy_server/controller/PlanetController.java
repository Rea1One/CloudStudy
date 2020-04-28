package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.PlanetService;
import com.whu.cloudstudy_server.service.StudyRecordService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class PlanetController {
    @Autowired
    private PlanetService planetService;

    @Autowired
    private StudyRecordService recordService;

    // 创建星球
    @PostMapping(value = "/galaxy/createPlanet")
    public Response<Object> createPlanet(Integer creatorId, String name, String introduction, Integer galaxy, Integer category, @Nullable Integer password) {
        return planetService.createPlanet(creatorId, name, introduction, galaxy, category, password);
    }

    // 查看星球列表
    @PostMapping(value = "/galaxy/queryAllPlanet")
    public Response<Object> queryAllPlanet(Integer galaxy, Integer batchNum) {
        Response<Object> response;
        List<Planet> planets = planetService.findPlanetByGalaxy(galaxy, batchNum * 10);
        if (planets == null) {
            response = new Response<>(-2, "失败", null);
            return response;
        }
        if (planets.isEmpty()) {
            response = new Response<>(-1, "无更多数据", null);
            return response;
        }
        response = new Response<>(0, "成功", planets);
        return response;
    }

    // 修改星球信息
    @PostMapping(value = "/user/modifyPlanetInfo")
    public Response<Object> modifyPlanetInfo(Integer id, String name, String introduction) {
        int ret = planetService.modifyPlanetInfo(id, name, introduction);
        Response<Object> response;
        switch (ret) {
            case 0:
                response = new Response<>(0, "修改成功", null);
                break;
            case -1:
                response = new Response<>(-1, "修改星球失败", null);
                break;
            default:
                response = new Response<>(-2, "失败", null);
                break;
        }
        return response;
    }

    // 加入星球
    @PostMapping("/galaxy/joinPlanet")
    public Response<Object> joinPlanet(Integer userId, Integer planetId, @Nullable Integer password) {
        return planetService.joinPlanet(userId, planetId, password);
    }

    // 获取星球动态
    @PostMapping(value = "/galaxy/getPlanetFeed")
    public Response<Object> getPlanetFeed(Integer id, Integer batchNum) {
        return planetService.getPlanetFeed(id, batchNum);
    }

    // 搜索星球
    @PostMapping(value = "/galaxy/searchPlanet")
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
    @PostMapping(value = "/galaxy/planet/enterPlanet")
    public Response<Object> enterPlanet(Integer planetId, Integer userId) {
        return planetService.enterPlanet(planetId, userId);
    }

    // 退出星球
    @PostMapping(value = "/galaxy/leavePlanet")
    public Response<Object> leavePlanet(Integer planetId, Integer userId) {
        Response<Object> response;
        int ret = planetService.leavePlanet(planetId, userId);
        recordService.getTotalStudyTimeFromRecord(userId);  // 更新用户总自习时长
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
            case -3:
                response = new Response<>(-3, "星球不存在", null);
                break;
            case -4:
                response = new Response<>(-4, "更新星球人数失败", null);
                break;
            default:
                response = new Response<>(-5, "失败", null);
                break;
        }
        return response;
    }
}
