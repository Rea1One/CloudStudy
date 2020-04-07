package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Author: 郭瑞景
 * Date: 2020-03-10
 */
@Service
public class ProfileService {

    @Autowired
    UserService userService;

    /**
     * 修改头像
     *
     * @param id 用户 id
     * @param data 新图片
     * @return 新图片的 url 地址
     */
    @Transactional
    public String changeProfile(Integer id, MultipartFile data) {
        User user = userService.findUserById(id);
        if (user == null) return null;  // 用户不存在
        String photoName = id.toString() + ".jpg";
        String path = "/CloudStudy/Images/" + photoName;
        File uploaded = new File(path);
        try {
            data.transferTo(uploaded);
            String imageUrl = "http://106.13.41.151:8088/image/" + photoName;
            System.out.println(imageUrl);
            user.setPhoto(imageUrl);
            int cnt = userService.updateUserInfo(user);
            if (cnt > 0) {
                return imageUrl;
            } else {
                return null;  // 更新用户头像失败
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 图像上传失败
        }
    }
}
