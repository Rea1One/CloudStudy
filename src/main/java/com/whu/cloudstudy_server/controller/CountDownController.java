package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.CountDown;
import com.whu.cloudstudy_server.service.CountDownService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-04-06
 */

@RestController
@RequestMapping(value = "/other")
public class CountDownController {
    @Autowired
    private CountDownService countDownService;

    //创建倒计时
    @PostMapping(value = "/createCountDown")
    public Response<Object> createCountDown(Integer userId,String name,String remark,String endTime){
        return countDownService.createCountDown(userId,name,remark,endTime);
    }

    //修改倒计时
    @PostMapping(value = "/modifyCountDown")
    public Response modifyCountDown(Integer id,String name,String remark,String endTime){
        int ret = countDownService.modifyCountDown(id,name,remark,endTime);
        Response response;
        switch (ret){
            case 0:
                response = new Response(0, "修改成功", null);
                break;
            case -1:
                response = new Response(-1, "修改失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }

    //获取倒计时
    @PostMapping(value = "/getCountDown")
    public Response getCountDown(Integer userId){
        List<CountDown> countDown = countDownService.getCountDown(userId);
        Response response;
        if (countDown.size() == 0) {
            response = new Response(-1, "无结果", null);
            return response;
        }
        response = new Response(0, "成功", countDown);
        return response;
    }

    //删除倒计时
    @PostMapping(value = "/deleteCountDown")
    public Response deleteCountDown(Integer id){
        int ret = countDownService.deleteCountDown(id);
        Response response;
        switch(ret){
            case 0:
                response = new Response(0,"删除成功",null);
                break;
            case -1:
                response = new Response(-1, "删除失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }
}
