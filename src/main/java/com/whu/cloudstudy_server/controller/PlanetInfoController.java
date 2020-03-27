package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.PlanetServiceHu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-03-05
 */

@RestController
@RequestMapping(value = "/galaxy")
public class PlanetInfoController{
    @Autowired
    private PlanetServiceHu planetService;

    //创建星球
    @PostMapping(value = "/createPlanet")
    public Response createPlanet(Integer creatorId,String name,String introduction,Integer galaxy,Integer category,Integer password){
        int ret=planetService.createPlanet(creatorId,name,introduction,galaxy,category,password);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "创建星球失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }

    //修改星球信息
    @PostMapping(value = "/modifyPlanetInfo")
    public Response modifyPlanetInfo(Integer id,String name,String introduction){
        int ret=planetService.modifyPlanetInfo(id,name,introduction);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "修改成功", null);
                break;
            case -1:
                response = new Response(-1, "修改星球失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }
}