package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Planet;

import java.util.List;

/**
 * @author 郭瑞景
 * @date 2020-03-02
 */
public interface PlanetMapper {
    Planet findPlanetById(Integer id);
    Planet findPlanetByCode(String code);
    List<Planet> findPlanetByName(String name);
    List<Planet> findPlanetByGalaxy(Integer galaxy,Integer batchNum);
    void insertPlanet(Planet planet);
    void updatePlanetInfo(Planet planet);
    void deletePlanet(Integer id);
}
