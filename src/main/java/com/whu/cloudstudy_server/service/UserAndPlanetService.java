package com.whu.cloudstudy_server.service;
//import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.UserAndPlanet;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-03-01
 */

@Service
public class UserAndPlanetService {
    @Autowired(required = false)
    private UserAndPlanetMapper userAndPlanetMapper;

//    public List<Planet> findPlanetByUserId(Integer userId){
//      return userAndPlanetMapper.findPlanetByUserId(userId);
//    }

    public void insertUserAndPlanet(UserAndPlanet userAndPlanet){
        userAndPlanetMapper.insertUserAndPlanet(userAndPlanet);
    }

    public void deleteUserAndPlanet(UserAndPlanet userAndPlanet){
        userAndPlanetMapper.deleteUserAndPlanet(userAndPlanet);
    }
}
