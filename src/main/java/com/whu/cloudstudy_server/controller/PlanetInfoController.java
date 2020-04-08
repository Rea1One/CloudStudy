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
    public Response<Object> createPlanet(Integer creatorId,String name,String introduction,Integer galaxy,Integer category,@Nullable Integer password){
        return planetService.createPlanet(creatorId,name,introduction,galaxy,category,password);
    }
}