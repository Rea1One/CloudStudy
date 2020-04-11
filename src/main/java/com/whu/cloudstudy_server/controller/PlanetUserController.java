package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.service.PlanetServiceHu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Author: 胡龙晨
 * Date: 2020-03-20
 */

@RestController
@RequestMapping(value = "/user")
public class PlanetUserController {
    @Autowired
    private PlanetServiceHu planetService;

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
