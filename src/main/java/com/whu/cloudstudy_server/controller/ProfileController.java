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
    public Response changeProfile(Integer id, MultipartFile data) {
        Response response;
        String ret = profileService.changeProfile(id, data);
//        switch (ret) {
//            case 0:
//                response = new Response(0, "成功", null);
//                break;
//            case -1:
//                response = new Response(-1, "用户不存在", null);
//                break;
//            case -2:
//                response = new Response(-2, "更新用户头像失败", null);
//                break;
//            case -3:
//                response = new Response(-3, "图像上传失败", null);
//                break;
//            default:
//                response = new Response(-4, "失败", null);
//                break;
//        }
        if (ret != null) {
            response = new Response(0, "成功", ret);
        } else {
            response = new Response(-1, "失败", null);
        }
        return response;
    }
}
