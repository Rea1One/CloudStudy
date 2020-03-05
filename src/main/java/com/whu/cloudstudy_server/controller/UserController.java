package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.Response;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.service.UserAndPlanetService;
import com.whu.cloudstudy_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 叶瑞雯
 * @date 2020-02-25
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/findUser")
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

    @PostMapping(value = "/login")
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

    @PostMapping(value = "/modifyUserInfo")
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

    @PostMapping(value = "/changePassword")
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

    @Autowired
    private UserAndPlanetService userAndPlanetService;

    @PostMapping(value = "/queryAllPlanet")
    public Response queryAllPlanet(Integer id) {
        Response response;
        List<Planet> planets = userAndPlanetService.findPlanetByUserId(id);
        if (planets == null) {
            response = new Response(-1, "失败", null);
            return response;
        }
        for (Planet p : planets) p.setPassword(null);
        response = new Response(0, "成功", planets);
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
