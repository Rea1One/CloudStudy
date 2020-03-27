package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.*;
import com.whu.cloudstudy_server.mapper.PlanetMapper;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-03-08
 */

@Service
public class PlanetServiceHu{
    @Autowired(required = false)
    private PlanetMapper planetMapper;

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
    public int createPlanet(Integer creatorId,String name,String introduction,Integer galaxy,Integer category,Integer password){
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
        Planet planet=new Planet();
        planet.setCode(randomCode());
        planet.setCreatorId(creatorId);
        planet.setName(name);
        planet.setIntroduction(introduction);
        planet.setGalaxy(galaxy);
        planet.setCategory(category);
        planet.setPassword(password);
        int cnt = planetMapper.insertPlanet(planet);
        if (cnt > 0) {
            return 0;
        }
        else {
            return -1;
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
        Planet planet=PlanetMapper.findPlanetById(id);
        planet.setName(name);
        planet.setIntroduction(introduction);
        int cnt = planetMapper.updatePlanetInfo(planet);
        if(cnt>=0){
            return 0;
        }
        else{
            return -1;
        }
    }
}