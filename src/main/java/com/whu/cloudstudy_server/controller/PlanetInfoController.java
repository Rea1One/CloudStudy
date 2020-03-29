package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.UserAndPlanet;
import com.whu.cloudstudy_server.service.UserAndPlanetService;
import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.PlanetServiceHu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


/**
 * Author: 胡龙晨
 * Date: 2020-03-05
 */

@RestController
@RequestMapping(value = "/galaxy")
public class PlanetInfoController{
    @Autowired
    private PlanetServiceHu planetService;
    private UserAndPlanetService userAndPlanetService;

    //创建星球
    @PostMapping(value = "/createPlanet")
    public Response createPlanet(Integer creatorId,String name,String introduction,Integer galaxy,Integer category,@Nullable Integer password){
        int ret=planetService.createPlanet(creatorId,name,introduction,galaxy,category,password);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "请输入密码", null);
                break;
            case -2:
                response = new Response(-2, "当前星球不应设置密码", null);
                break;
            case -3:
                response = new Response(-3, "创建星球失败", null);
                break;
            default:
                response = new Response(-4, "失败", null);
                break;
        }
        return response;
    }
}