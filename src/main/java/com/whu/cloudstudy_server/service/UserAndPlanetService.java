package com.whu.cloudstudy_server.service;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.UserAndPlanet;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: 叶瑞雯
 * Date: 2020-03-01
 */

@Service
public class UserAndPlanetService {
    @Autowired(required = false)
    private UserAndPlanetMapper userAndPlanetMapper;

    public List<Planet> findPlanetByUserId(Integer userId){
      return userAndPlanetMapper.findPlanetByUserId(userId);
    }

    @Transactional
    public int insertUserAndPlanet(UserAndPlanet userAndPlanet){
        int userId=userAndPlanet.getUserId();
        int planetId=userAndPlanet.getPlanetId();
        List<Planet> planets=userAndPlanetMapper.findPlanetByUserId(userId);
        for(Planet p:planets){
            if(p.getId()==planetId) return -1;
        }
        return userAndPlanetMapper.insertUserAndPlanet(userAndPlanet);
    }

    @Transactional
    public int deleteUserAndPlanet(UserAndPlanet userAndPlanet){
        return userAndPlanetMapper.deleteUserAndPlanet(userAndPlanet);
    }
}
