package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.*;
import com.whu.cloudstudy_server.mapper.PlanetMapper;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import com.whu.cloudstudy_server.mapper.UserMapper;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class PlanetService {
    @Autowired(required = false)
    private PlanetMapper planetMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserAndPlanetMapper uapMapper;

    @Autowired(required = false)
    private StudyRecordMapper recordMapper;

    /**
     * 创建星球
     *
     * @param creatorId
     * @param name
     * @param introduction
     * @param galaxy
     * @param category
     * @param password
     * @return Response
     */
    @Transactional
    public Response<Object> createPlanet(Integer creatorId, String name, String introduction, Integer galaxy, Integer category, @Nullable Integer password) {
        Planet planet = new Planet();
        String randcode = returnCode();
        planet.setCode(randcode);
        planet.setCreatorId(creatorId);
        planet.setName(name);
        planet.setIntroduction(introduction);
        planet.setGalaxy(galaxy);
        planet.setCategory(category);
        if (planet.getCategory() == 1) {
            if (password == null) {
                return new Response<>(-1, "请输入密码", null);
            } else {
                planet.setPassword(password);
            }
        } else {
            if (password != null) {
                return new Response<>(-2, "当前星球不应设置密码", null);
            }
        }
        User user = userMapper.findUserById(creatorId);
        int studytime = user.getStudyTime();
        int count = planetMapper.findPlanetCountByUserId(creatorId);
        if (studytime / 120 - count < 1) {
            return new Response<>(-3, "额度不足", null);
        }
        int cnt = planetMapper.insertPlanet(planet);
        int planetId = planet.getId();
        UserAndPlanet userAndPlanet = new UserAndPlanet();
        userAndPlanet.setUserId(creatorId);
        userAndPlanet.setPlanetId(planetId);
        int cnt2 = uapMapper.insertUserAndPlanet(userAndPlanet);
        List<Object> returnIdAndCode = new ArrayList<>();
        returnIdAndCode.add(planetId);
        returnIdAndCode.add(randcode);
        if (cnt + cnt2 >= 0) {
            return new Response<>(0, "成功", returnIdAndCode);
        } else {
            return new Response<>(-4, "失败", null);
        }
    }

    public List<Planet> findPlanetByGalaxy(Integer galaxy, Integer batchNum) {
        return planetMapper.findPlanetByGalaxy(galaxy, batchNum);
    }

    /**
     * 修改星球信息
     *
     * @param id
     * @param name
     * @param introduction
     * @return
     */
    @Transactional
    public int modifyPlanetInfo(Integer id, String name, String introduction) {
        Planet planet = planetMapper.findPlanetById(id);
        planet.setName(name);
        planet.setIntroduction(introduction);
        int cnt = planetMapper.updatePlanetInfo(planet);
        if (cnt > 0) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 加入星球
     * @param userId
     * @param planetId
     * @param password
     * @return Response
     */
    @Transactional
    public Response<Object> joinPlanet(Integer userId, Integer planetId, @Nullable Integer password) {
        Response<Object> response;
        Planet planet = planetMapper.findPlanetById(planetId);
        if (planet == null) {
            response = new Response<>(-1, "失败", null);
            return response;
        }
        if (planet.getCategory() == 1) {
            if(planet.getPopulation()==6){
                response = new Response<>(-4, "星球人数已满", null);
                return response;
            }
            if (password == null) {
                response = new Response<>(-2, "请输入密码", null);
                return response;
            } else if (password.intValue() != planet.getPassword().intValue()) {
                response = new Response<>(-3, "密码错误", null);
                return response;
            }
        }
        UserAndPlanet userAndPlanet = new UserAndPlanet();
        userAndPlanet.setUserId(userId);
        userAndPlanet.setPlanetId(planetId);
        int cnt1 = uapMapper.insertUserAndPlanet(userAndPlanet);
        if (cnt1 <= 0) {
            response = new Response<>(-5, "插入用户-星球关系数据失败", null);
            return response;
        }
        int n = planet.getPopulation() + 1;
        planet.setPopulation(n);
        int cnt2 = planetMapper.updatePlanetInfo(planet);
        if (cnt2 <= 0) {
            response = new Response<>(-6, "更新星球信息失败", null);
            return response;
        }
        response = new Response<>(0, "加入成功", null);
        return response;
    }

    /**
     * 获取星球动态
     * @param id
     * @param batchNum
     * @return Response
     */
    public Response<Object> getPlanetFeed(Integer id, Integer batchNum) {
        Response<Object> response;
        List<List<Object>> result = new ArrayList<>();
        List<StudyRecord> studyRecords = recordMapper.findStudyRecordByPlanetId(id, batchNum * 15);
        if (studyRecords == null) {
            response = new Response<>(-2, "失败", null);
            return response;
        }
        if (studyRecords.isEmpty()) {
            response = new Response<>(-1, "无更多数据", null);
            return response;
        }
        for (StudyRecord sr : studyRecords) {
            User user = userMapper.findUserById(sr.getUsreId());
            processInfo(user);
            List<Object> item = new ArrayList<>();
            item.add(user);
            item.add(sr.getTime());
            item.add(sr.getOperation());
            result.add(item);
        }
        response = new Response<>(0, "成功", result);
        return response;
    }

    /**
     * 根据关键词搜索星球
     *
     * @param keyword
     * @return 查找结果: List<Planet>
     */
    public List<Planet> searchPlanet(String keyword) {
        Planet singleResult = planetMapper.findPlanetByCode(keyword);
        if (singleResult != null) {
            List<Planet> result = new LinkedList<>();
            result.add(singleResult);
            return result;
        } else {
            keyword = "%" + keyword + "%";
            return planetMapper.findPlanetByName(keyword);
        }
    }

    /**
     * 用户进入星球
     *
     * @param planetId
     * @param userId
     * @return Response<Object>
     */
    public Response<Object> enterPlanet(Integer planetId, Integer userId) {
        Response<Object> response;
        List<User> users = uapMapper.findAllUserByPlanetId(planetId);
        Planet currPlanet = planetMapper.findPlanetById(planetId);
        if (users.size() == 0 || currPlanet == null) {
            response = new Response<>(-1, "星球不存在", null);
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
                        user.getPhoto(), user.getStudyTime(), user.getRegisterTime(), "休息中");
                processInfo(cUser);
                cUsers.add(cUser);
            } else {
                Integer status = record.getOperation();
                if (status.equals(0) || status.equals(2)) {  // 自习中
                    long currTime = System.currentTimeMillis();
                    long mSec = currTime - record.getTime().getTime();
                    int minutes = Math.toIntExact(mSec) / 60000;
                    int h = minutes / 60;
                    int m = minutes % 60;
                    String statusInfo = "已自习 ";
                    if (h == 0) {
                        statusInfo += m;
                    } else {
                        statusInfo = statusInfo + h + "h" + m;
                    }
                    statusInfo += "min";
                    CustomizedUser cUser = new CustomizedUser(user.getId(), user.getName(), null,
                            user.getSex(), user.getAge(), user.getSignature(), user.getEmail(),
                            user.getPhoto(), user.getStudyTime(), user.getRegisterTime(), statusInfo);
                    processInfo(cUser);
                    cUsers.add(cUser);
                } else if (status.equals(1) || status.equals(3)) {  // 休息中
                    CustomizedUser cUser = new CustomizedUser(user.getId(), user.getName(), null,
                            user.getSex(), user.getAge(), user.getSignature(), user.getEmail(),
                            user.getPhoto(), user.getStudyTime(), user.getRegisterTime(), "休息中");
                    processInfo(cUser);
                    cUsers.add(cUser);
                }
            }
        }
        if (cUsers.size() == 0) {
            response = new Response<>(-2, "获取用户列表失败", null);
            return response;
        }
        retData.add(cUsers);
        response = new Response<>(0, "成功", retData);
        return response;
    }

    /**
     * 用户退出星球
     *
     * @param planetId
     * @param userId
     * @return 0: 成功
     */
    @Transactional
    public int leavePlanet(Integer planetId, Integer userId) {
        // 删除学习记录
        List<StudyRecord> records = recordMapper.findAllByUserIdAndPlanetId(userId, planetId);
        for (StudyRecord record : records) {
            recordMapper.deleteStudyRecordById(record.getId());
        }

        // 删除用户星球关系记录
        UserAndPlanet target = uapMapper.findUAPByPlanetIdAndUserId(planetId, userId);
        if (target == null) {
            return -1;  // 记录不存在
        }
        int cnt = uapMapper.deleteUserAndPlanet(target);
        if (cnt > 0) {
            return 0;  // 成功
        } else {
            return -2;  // 删除记录失败
        }
    }


    //生成code 8位随机数
    private static String randomCode() {
        int maxNum = 10;
        int i;
        int count = 0;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 8) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    //检验code是否有重复
    public String returnCode() {
        String rancode = randomCode();
        String rancode1 = planetMapper.selectByRancode(rancode);//生成的随机数进入数据库中查询一下，看时候有相同的。
        if (rancode1 != null) {//如果有相同的数据
            return returnCode();//再次调用方法，生成一个随机数
        } else {//否则
            return rancode;//获得不重复的随机数据
        }
    }

    /**
     * 处理返回用户信息, 隐藏密码和邮箱
     *
     * @param user
     */
    private void processInfo(User user) {
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
