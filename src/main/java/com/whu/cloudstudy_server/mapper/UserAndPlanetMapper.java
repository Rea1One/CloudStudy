package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.entity.UserAndPlanet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Author: 叶瑞雯
 * Date: 2020-03-01
 */

@Mapper
public interface UserAndPlanetMapper {
    List<Planet> findPlanetByUserId(Integer userId);
    List<User> findAllUserByPlanetId(Integer planetId);
    UserAndPlanet findUAPByPlanetIdAndUserId(Integer planetId, Integer userId);
    int insertUserAndPlanet(UserAndPlanet userAndPlanet);
    int deleteUserAndPlanet(UserAndPlanet userAndPlanet);
}
