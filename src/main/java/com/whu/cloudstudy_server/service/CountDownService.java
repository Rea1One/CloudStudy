package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.mapper.UserMapper;
import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.CountDown;
import com.whu.cloudstudy_server.mapper.CountDownMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-04-06
 */

@Service
public class CountDownService {
    @Autowired(required = false)
    private CountDownMapper countDownMapper;

    /**
     * 创建倒计时
     *
     * @param userId
     * @param name
     * @param remark
     * @param endTime
     * @return
     */

    public int createCountDown(Integer userId,String name,String remark,String endTime){
        CountDown countDown = new CountDown();
        countDown.setUserId(userId);
        countDown.setName(name);
        countDown.setRemark(remark);
        countDown.setEndTime(endTime);
        int cnt = countDownMapper.insertCountDown(countDown);
        if(cnt > 0){
            return 0;
        }
        else{
            return -1;
        }
    }

    /**
     * 修改倒计时
     *
     * @param id
     * @param name
     * @param remark
     * @param endTime
     * @return
     */

    public int modifyCountDown(Integer id,String name,String remark,String endTime){
        CountDown countDown = countDownMapper.findCountDownById(id);
        countDown.setName(name);
        countDown.setRemark(remark);
        countDown.setEndTime(endTime);
        int cnt = countDownMapper.updateCountDownInfo(countDown);
        if(cnt > 0){
            return 0;
        }
        else{
            return -1;
        }
    }

    /**
     * 获取倒计时
     *
     * @param userId
     * @return List<CountDown>
     */

    public List<CountDown> getCountDown(Integer userId){
        List<CountDown> singleResult = countDownMapper.findCountDownByUserId(userId);
        return singleResult;
    }

    /**
     * 删除倒计时
     *
     * @param id
     * @return List<CountDown>
     */
    public int deleteCountDown(Integer id){
        int cnt = countDownMapper.deleteCountDownById(id);
        if(cnt > 0){
            return 0;
        }
        else{
            return -1;
        }
    }
}
