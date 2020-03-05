package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.entity.UserAndPlanet;
import com.whu.cloudstudy_server.service.PlanetService;
import com.whu.cloudstudy_server.service.StudyRecordService;
import com.whu.cloudstudy_server.service.UserAndPlanetService;
import com.whu.cloudstudy_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/galaxy")
public class UserAndPlanetController {

    @PostMapping(value = "/queryAllPlanet")
    public Response queryAllPlanet(Integer galaxy, Integer batchNum) {
        Response response;
        List<Planet> planets = planetService.findPlanetByGalaxy(galaxy, batchNum * 10);
        if (planets == null) {
            response = new Response(-2, "失败", null);
            return response;
        }
        if (planets.isEmpty()) {
            response = new Response(-1, "无更多数据", null);
            return response;
        }
        for (Planet p : planets) p.setPassword(null);
        response = new Response(0, "成功", planets);
        return response;
    }

    @Autowired
    private PlanetService planetService;

    @Autowired
    private UserAndPlanetService userAndPlanetService;

    @PostMapping(value = "/joinPlanet")
    public Response joinPlanet(Integer userId, Integer planetId, @Nullable Integer password) {
        Response response;
        Planet planet = planetService.findPlanetById(planetId);
        if (planet == null) {
            response = new Response(-1, "失败", null);
            return response;
        }
        if (planet.getCategory() == 1) {
            if (password == null) {
                response = new Response(-2, "请输入密码", null);
                return response;
            } else if (password.intValue() != planet.getPassword().intValue()) {
                response = new Response(-3, "密码错误", null);
                return response;
            }
        }
        UserAndPlanet userAndPlanet = new UserAndPlanet();
        userAndPlanet.setUserId(userId);
        userAndPlanet.setPlanetId(planetId);
        int cnt1 = userAndPlanetService.insertUserAndPlanet(userAndPlanet);
        if (cnt1 <= 0) {
            response = new Response(-4, "插入用户-星球关系数据失败", null);
            return response;
        }
        int n = planet.getPopulation() + 1;
        planet.setPopulation(n);
        int cnt2 = planetService.updatePlanetInfo(planet);
        if (cnt2 <= 0) {
            response = new Response(-5, "更新星球信息失败", null);
            return response;
        }
        response = new Response(0, "加入成功", null);
        return response;
    }

    @Autowired
    private StudyRecordService studyRecordService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/getPlanetFeed")
    public Response getPlanetFeed(Integer id, Integer batchNum) {
        Response response;
        List<List<Object>> result = new ArrayList<>();
        List<StudyRecord> studyRecords = studyRecordService.findStudyRecordByPlanetId(id, batchNum * 10);
        if (studyRecords == null) {
            response = new Response(-2, "失败", null);
            return response;
        }
        if (studyRecords.isEmpty()) {
            response = new Response(-1, "无更多数据", null);
            return response;
        }
        for (StudyRecord sr : studyRecords) {
            User user = userService.findUserById(sr.getUsreId());
            processInfo(user);
            List<Object> item = new ArrayList<>();
            item.add(user);
            item.add(sr.getTime());
            item.add(sr.getOperation());
            result.add(item);
        }
        response = new Response(0, "成功", result);
        return response;
    }

    //处理返回用户信息 隐藏密码和邮箱
    private void processInfo(User user) {
        user.setPassword(null);
        int i;
        String before = user.getEmail();
        String after = before.substring(0, 3);
        for (i = 3; i < before.length(); i++) {
            if (before.charAt(i) == '@') break;
            after += "*";
        }
        after += before.substring(i);
        user.setEmail(after);
    }
}
