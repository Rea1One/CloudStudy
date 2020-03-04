package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.Response;
import com.whu.cloudstudy_server.entity.CustomizedUser;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.PlanetMapper;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 郭瑞景
 * @date 2020-03-02
 */

@Service
public class PlanetServiceGuo {
    @Autowired(required = false)
    private PlanetMapper planetMapper;
    @Autowired(required = false)
    private UserAndPlanetMapper uapMapper;
    @Autowired(required = false)
    private StudyRecordMapper recordMapper;

    public List<Planet> searchPlanet(String keyword) {
        Planet singleResult = planetMapper.findPlanetByCode(keyword);
        if (singleResult != null) {
            List<Planet> result = new LinkedList<>();
            result.add(singleResult);
            return result;
        } else {
            return planetMapper.findPlanetByName(keyword);
        }
    }

    public Response enterPlanet(Integer planetId, Integer userId) {
        Response response;
        List<User> users = uapMapper.findAllUserByPlanetId(planetId);
        Planet currPlanet = planetMapper.findPlanetById(planetId);
        if (users.size() == 0 || currPlanet == null) {
            response = new Response(-1, "星球不存在", null);
            return response;
        }
        List<Object> retData = new LinkedList<>();

        // 判断当前用户是否在该星球内
        List<Planet> planets = uapMapper.findPlanetByUserId(userId);
        boolean isInside = false;
        for (Planet planet : planets) {
            if (planet.getId().equals(currPlanet.getId())) {
                isInside = true;
                break;
            }
        }
        if (isInside) {  // 在星球内
            retData.add(1);
        } else {  // 不在星球内
            retData.add(0);
        }

        List<CustomizedUser> cUsers = new LinkedList<>();

        // 获取每个用户的自习状态
        for (User user : users) {
            StudyRecord record = recordMapper.findLatestStudyRecordByUserIdAndPlanetId(user.getId(), planetId);
            if (record == null) {
                CustomizedUser cUser = new CustomizedUser(user.getId(), user.getName(), null,
                        user.getSex(), user.getAge(), user.getSignature(), user.getEmail(),
                        user.getPhoto(), user.getStudyTime(), user.getRegisterTime(), "未自习");
                processInfo(cUser);
                cUsers.add(cUser);
            } else {
                Integer status = record.getOperation();
                if (status.equals(0)) {  // 自习中
                    long currTime = System.currentTimeMillis();
                    long mSec = currTime - record.getTime().getTime();
                    int minutes = Math.toIntExact(mSec) / 60000;
                    int h = minutes / 60;
                    int m = minutes % 60;
                    String statusInfo = "已自习";
                    if (h == 0) {
                        statusInfo += m;
                    } else {
                        statusInfo = statusInfo + h + "小时" + m;
                    }
                    statusInfo += "分钟";
                    CustomizedUser cUser = new CustomizedUser(user.getId(), user.getName(), null,
                            user.getSex(), user.getAge(), user.getSignature(), user.getEmail(),
                            user.getPhoto(), user.getStudyTime(), user.getRegisterTime(), statusInfo);
                    processInfo(cUser);
                    cUsers.add(cUser);
                } else if (status.equals(1)) {  // 未自习
                    CustomizedUser cUser = new CustomizedUser(user.getId(), user.getName(), null,
                            user.getSex(), user.getAge(), user.getSignature(), user.getEmail(),
                            user.getPhoto(), user.getStudyTime(), user.getRegisterTime(), "未自习");
                    processInfo(cUser);
                    cUsers.add(cUser);
                }
            }
        }
        if (cUsers.size() == 0) {
            response = new Response(-2, "获取用户列表失败", null);
            return response;
        }
        retData.add(cUsers);
        response = new Response(0, "成功", retData);
        return response;
    }

    // 退出星球
    public void leavePlanet(Integer planetId, Integer userId) {
        uapMapper.deleteUAPByUserIdAndPlanetId(planetId, userId);
    }

    //处理返回用户信息 隐藏密码和邮箱
    private void processInfo(CustomizedUser user) {
        int i;
        String before = user.getEmail();
        StringBuilder after = new StringBuilder(before.substring(0, 3));
        for (i = 3; i < before.length(); i++) {
            if (before.charAt(i) == '@') break;
            after.append("*");
        }
        after.append(before.substring(i));
        user.setEmail(after.toString());
    }
}
