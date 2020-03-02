package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Planet;

/**
 * @author 郭瑞景
 * @date 2020-03-02
 */
public interface PlanetMapper {
    Planet findPlanetById(Integer id);
    Planet findPlanetByCode(String code);
    void insertPlanet(Planet planet);
    void updatePlanetInfo(Planet planet);
    void deletePlanet(Integer id);
}
