package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.ProfileService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: 郭瑞景
 * Date: 2020-03-10
 */
@RestController
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/changeProfile")
    public Response<Object> changeProfile(Integer id, MultipartFile data) {
        Response<Object> response;
        String ret = profileService.changeProfile(id, data);
        if (ret != null) {
            response = new Response<>(0, "成功", ret);
        } else {
            response = new Response<>(-1, "失败", null);
        }
        return response;
    }
}
