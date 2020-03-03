package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.mapper.PlanetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-03-02
 */

@Service
public class PlanetService {
    @Autowired(required = false)
    private PlanetMapper planetMapper;

    public Planet findPlanetById(Integer id){
        return planetMapper.findPlanetById(id);
    }

    public List<Planet> findPlanetByGalaxy(Integer galaxy, Integer batchNum){
        return planetMapper.findPlanetByGalaxy(galaxy,batchNum);
    }

    public void updatePlanetInfo(Planet planet){
        planetMapper.updatePlanetInfo(planet);
    }
}
