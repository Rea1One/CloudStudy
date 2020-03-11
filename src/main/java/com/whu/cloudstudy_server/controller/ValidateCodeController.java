package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2020-02-24
 */

@RestController
@RequestMapping(value = "/user")
public class ValidateCodeController {
    @Autowired
    private ValidateCodeService codeService;

    @PostMapping(value = "/sendValidateCode")
    public Response sendValidateCode(String email, Integer type) {
        int ret = codeService.sendCode(email, type);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "邮箱重复", null);
                break;
            case -2:
                response = new Response(-2, "用户不存在", null);
                break;
            case -3:
                response = new Response(-3, "验证码发送失败", null);
                break;
            default:
                response = new Response(-4, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/register")
    public Response register(String name, String password, Integer gender,
                             String introduction, String email, Integer code, Integer age) {
        int ret = codeService.register(name, password, gender, introduction, email, code, age);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "验证码错误", null);
                break;
            case -2:
                response = new Response(-2, "插入用户数据失败", null);
                break;
            default:
                response = new Response(-3, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/validateCode")
    public Response validateCode(String email, Integer code) {
        int ret = codeService.validateCode(email, code);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "成功", null);
                break;
            case -1:
                response = new Response(-1, "验证码错误", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }
}
