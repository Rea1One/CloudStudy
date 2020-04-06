package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.CountDown;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-04-05
 */

public interface CountDownMapper {
    CountDown findCountDownById(Integer id);
    List<CountDown> findCountDownByUserId(Integer userId);
    int insertCountDown(CountDown countDown);
    int deleteCountDownById(Integer id);
    int updateCountDownInfo(CountDown countDown);
}

