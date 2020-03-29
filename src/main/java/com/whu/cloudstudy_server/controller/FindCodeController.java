package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.service.FindCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-02-27
 */

@RestController
@RequestMapping(value = "/user")
public class FindCodeController{
    @Autowired
    private FindCodeService findCodeService;

    //找回密码
    @PostMapping(value = "/findPassword")
    public Response findPassword(String email,String newPassword){
        int ret = findCodeService.findPassword(email,newPassword);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "修改成功", null);
                break;
            case -1:
                response = new Response(-1, "修改失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }
}
