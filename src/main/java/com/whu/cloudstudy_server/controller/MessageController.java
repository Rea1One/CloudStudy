package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Message;
import com.whu.cloudstudy_server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: 胡龙晨
 * Date: 2020-03-05
 */

@RestController
@RequestMapping(value = "/user")
public class MessageController{
    @Autowired
    private MessageService messageService;

    //给某个用户留言
    @PostMapping(value = "/leaveMessage")
    public Response leaveMessage(Integer senderId,Integer receiverId,String content){
        int ret=messageService.leaveMessage(senderId,receiverId,content);
        Response response;
        switch (ret) {
            case 0:
                response = new Response(0, "留言成功", null);
                break;
            case -1:
                response = new Response(-1, "留言失败", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }

    //查看留言列表
    @PostMapping(value = "/queryAllMessage")
    public Response queryAllMessage (Integer id) {
        List<Message> messages = messageService.queryAllMessage(id);
        Response response;
        if (messages.size() == 0) {
            response = new Response(-1, "无结果", null);
            return response;
        }
        response = new Response(0, "成功", messages);
        return response;
    }

    //清空留言列表
    @PostMapping(value = "/clearMessage")
    public Response clearMessage(Integer id){
        int ret=messageService.clearMessage(id);
        Response response;
        switch(ret){
            case 0:
                response = new Response(0,"删除成功",null);
                break;
            case -1:
                response = new Response(-1, "无留言可删除", null);
                break;
            default:
                response = new Response(-2, "失败", null);
                break;
        }
        return response;
    }
}