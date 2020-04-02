package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Planet;

import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2020-03-02
 */
public interface PlanetMapper {
    Planet findPlanetById(Integer id);
    Planet findPlanetByCode(String code);
    List<Planet> findPlanetByName(String name);
    List<Planet> findPlanetByGalaxy(Integer galaxy,Integer batchNum);
    int insertPlanet(Planet planet);
    int updatePlanetInfo(Planet planet);
    int deletePlanet(Integer id);
    String selectByRancode(String rancode);
}
