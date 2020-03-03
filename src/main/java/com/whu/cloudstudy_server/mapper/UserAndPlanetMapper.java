package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.UserAndPlanet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-03-01
 */

@Mapper
public interface UserAndPlanetMapper {
    List<Planet> findPlanetByUserId(Integer userId);
    void insertUserAndPlanet(UserAndPlanet userAndPlanet);
    void deleteUserAndPlanet(UserAndPlanet userAndPlanet);
}
