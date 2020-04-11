package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.Message;
import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Author: 叶瑞雯
 * Date: 2020-02-25
 */

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/findUser")
    public Response findUser(Integer id) {
        Response response;
        User user = userService.findUserById(id);
        if (user == null) {
            response = new Response(-1, "失败", null);
            return response;
        }
        processInfo(user);
        response = new Response(0, "成功", user);
        return response;
    }

    @PostMapping(value = "/user/login")
    public Response login(String email, String password) {
        Response response;
        User user = userService.findUserByEmail(email);
        if (user == null) {
            response = new Response(-2, "邮箱不存在", null);
            return response;
        }
        if (!password.equals(user.getPassword())) {
            response = new Response(-1, "密码错误", null);
            return response;
        }
        processInfo(user);
        response = new Response(0, "登录成功", user);
        return response;
    }

    @PostMapping(value = "/user/modifyUserInfo")
    public Response modifyUserInfo(Integer id, Integer type, String content) {
        Response response;
        User user = userService.findUserById(id);
        if (user != null) {
            switch (type) {
                case 0: // 修改用户名
                    user.setName(content);
                    break;
                case 1: // 修改性别
                    user.setSex(Integer.parseInt(content));
                    break;
                case 2: // 修改年龄
                    user.setAge(Integer.parseInt(content));
                    break;
                case 3: // 修改个性签名
                    user.setSignature(content);
                    break;
                default:
                    response = new Response(-1, "失败", null);
                    return response;
            }
            int cnt = userService.updateUserInfo(user);
            if (cnt > 0) {
                response = new Response(0, "成功", null);
            } else {
                response = new Response(-2, "更新用户信息失败", null);
            }
        } else {
            response = new Response(-3, "用户不存在", null);
        }
        return response;
    }

    @PostMapping(value = "/user/changePassword")
    public Response changePassword(Integer id, String oldPassword, String newPassword) {
        Response response;
        User user = userService.findUserById(id);
        if (user == null) {
            response = new Response(-2, "用户不存在", null);
            return response;
        }
        if (!oldPassword.equals(user.getPassword())) {
            response = new Response(-1, "旧密码错误", null);
            return response;
        }
        user.setPassword(newPassword);
        int cnt = userService.updateUserInfo(user);
        if (cnt <= 0) {
            response = new Response(-3, "更新用户信息失败", null);
            return response;
        }
        response = new Response(0, "修改成功", null);
        return response;
    }

    @PostMapping(value = "/user/queryAllPlanet")
    public Response queryAllPlanet(Integer id) {
        Response response;
        List<Planet> planets = userService.findPlanetByUserId(id);
        if (planets == null) {
            response = new Response(-1, "失败", null);
            return response;
        }
        response = new Response(0, "成功", planets);
        return response;
    }

    @PostMapping(value = "/user/sendValidateCode")
    public Response<Object> sendValidateCode(String email, Integer type) {
        int ret = userService.sendCode(email, type);
        Response<Object> response;
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "邮箱重复", null);
                break;
            case -2:
                response = new Response<>(-2, "用户不存在", null);
                break;
            case -3:
                response = new Response<>(-3, "验证码发送失败", null);
                break;
            default:
                response = new Response<>(-4, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/user/register")
    public Response<Object> register(String name, String password, Integer gender,
                                     String introduction, String email, Integer code, Integer age) {
        int ret = userService.register(name, password, gender, introduction, email, code, age);
        Response<Object> response;
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "验证码错误", null);
                break;
            case -2:
                response = new Response<>(-2, "插入用户数据失败", null);
                break;
            default:
                response = new Response<>(-3, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/user/validateCode")
    public Response<Object> validateCode(String email, Integer code) {
        int ret = userService.validateCode(email, code);
        Response<Object> response;
        switch (ret) {
            case 0:
                response = new Response<>(0, "成功", null);
                break;
            case -1:
                response = new Response<>(-1, "验证码错误", null);
                break;
            default:
                response = new Response<>(-2, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping("/user/changeProfile")
    public Response<Object> changeProfile(Integer id, MultipartFile data) {
        Response<Object> response;
        String ret = userService.changeProfile(id, data);
        if (ret != null) {
            response = new Response<>(0, "成功", ret);
        } else {
            response = new Response<>(-1, "失败", null);
        }
        return response;
    }

    //找回密码
    @PostMapping(value = "/user/findPassword")
    public Response findPassword(String email,String newPassword){
        int ret = userService.findPassword(email,newPassword);
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

    //给某个用户留言
    @PostMapping(value = "/user/leaveMessage")
    public Response leaveMessage(Integer senderId,Integer receiverId,String content){
        int ret=userService.leaveMessage(senderId,receiverId,content);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "留言成功", null);
                break;
            case -1:
                response = new Response(-1, "留言失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }

    //查看留言列表
    @PostMapping(value = "/user/queryAllMessage")
    public Response queryAllMessage (Integer id) {
        List<Message> messages = userService.queryAllMessage(id);
        Response response;
        if (messages.size() == 0) {
            response = new Response(-1, "无结果", null);
            return response;
        }
        response = new Response(0, "成功", messages);
        return response;
    }

    //清空留言列表
    @PostMapping(value = "/user/clearMessage")
    public Response clearMessage(Integer id){
        int ret=userService.clearMessage(id);
        Response response;
        switch(ret){
            case 0:
                response = new Response(0,"删除成功",null);
                break;
            case -1:
                response = new Response(-1, "无留言可删除", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }

    @PostMapping(value = "/other/queryThreeMost")
    public Response queryThreeMost(Integer id){
        Response response;
        List<Planet> planets=userService.getThreeMostPlanet(id);
        if(planets==null){
            response=new Response(-1,"失败", null);
            return response;
        }
        response=new Response(0,"成功", planets);
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
