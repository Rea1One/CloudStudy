package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.*;
import com.whu.cloudstudy_server.mapper.PlanetMapper;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Author: 胡龙晨
 * Date: 2020-03-08
 */

@Service
public class PlanetServiceHu{
    @Autowired(required = false)
    private PlanetMapper planetMapper;

    @Autowired(required = false)
    private UserAndPlanetMapper userAndPlanetMapper;

    @Autowired(required = false)
    private StudyRecordMapper studyRecordMapper;

    /**
     * 创建星球
     *
     * @param creatorId
     * @param name
     * @param introduction
     * @param galaxy
     * @param category
     * @param password
     * @return
     */

    public Response<Object> createPlanet(Integer creatorId,String name,String introduction,Integer galaxy,Integer category,@Nullable Integer password){
        Planet planet=new Planet();
        String randcode = returnCode();
        planet.setCode(randcode);
        planet.setCreatorId(creatorId);
        planet.setName(name);
        planet.setIntroduction(introduction);
        planet.setGalaxy(galaxy);
        planet.setCategory(category);
        if (planet.getCategory() == 1){
            if (password == null) {
                return new Response<>(-1, "请输入密码", null);
            }
            else{
                planet.setPassword(password);
            }
        }
        else {
            if(password != null){
                return new Response<>(-2, "当前星球不应设置密码", null);
            }
        }
        int cnt = planetMapper.insertPlanet(planet);
        planet = planetMapper.findPlanetByCode(randcode);
        int planetId=planet.getId();
        UserAndPlanet userAndPlanet =new UserAndPlanet();
        userAndPlanet.setUserId(creatorId);
        userAndPlanet.setPlanetId(planetId);
        int cnt2 = userAndPlanetMapper.insertUserAndPlanet(userAndPlanet);
        List returnIdAndCode = new ArrayList();
        returnIdAndCode.add(planetId);
        returnIdAndCode.add(randcode);
        if (cnt + cnt2 > 0) {
            return new Response<>(0, "成功", returnIdAndCode);
        }
        else {
            return new Response<>(-3, "创建星球失败", null);
        }
    }


    /**
     * 修改星球信息
     *
     * @param id
     * @param name
     * @param introduction
     * @return
     */
    public int modifyPlanetInfo(Integer id,String name,String introduction){
        Planet planet=planetMapper.findPlanetById(id);
        planet.setName(name);
        planet.setIntroduction(introduction);
        int cnt = planetMapper.updatePlanetInfo(planet);
        if(cnt > 0){
            return 0;
        }
        else{
            return -1;
        }
    }

    /**
     * 删除星球
     *
     * @param id
     * @return
     */
    public int destroyPlanet(Integer id){
        int cnt1 = planetMapper.deletePlanet(id);
        int cnt2 = userAndPlanetMapper.deleteUserAndPlanetByPlanetId(id);
        int cnt3 = studyRecordMapper.deleteStudyRecordByPlanetId(id);
        if(cnt1 + cnt2 + cnt3 > 0 ){
            return 0;
        }
        else{
            return 1;
        }
    }

    //生成code 8位随机数
    private static String randomCode() {
        int  maxNum = 10;
        int i;
        int count = 0;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 8){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    //检验code是否有重复
    public String returnCode(){
        String rancode=randomCode();
        String rancode1 = planetMapper.selectByRancode(rancode);//生成的随机数进入数据库中查询一下，看时候有相同的。
        if(rancode1 != null){//如果有相同的数据
            return returnCode();//再次调用方法，生成一个随机数
        }else{//否则
            return rancode;//获得不重复的随机数据
        }
    }
}
