package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.entity.TodolistItem;
import com.whu.cloudstudy_server.service.TodolistService;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: 叶瑞雯
 * Date: 2020-04-05
 */

@RestController
@RequestMapping(value = "/other")
public class TodolistController {

    @Autowired
    TodolistService todolistService;

    @PostMapping(value = "/getList")
    public Response queryAllTodoList(Integer userId){
        Response response;
        List<Object> result=todolistService.findTodolistAndItem(userId);
        if(result==null){
            response=new Response(-1, "失败", null);
            return response;
        }
        response=new Response(0, "成功", result);
        return response;
    }

    @PostMapping(value = "/todoList/changePriority")
    public Response changePriority(Integer id,Integer priority){
        Response response;
        TodolistItem todolistItem=todolistService.findTodolistItemById(id);
        if(todolistItem==null){
            response=new Response(-1, "无该条目", null);
            return response;
        }
        todolistItem.setPriority(priority);
        int cnt=todolistService.updateTodolistItemInfo(todolistItem);
        if(cnt<=0){
            response=new Response(-2, "改表失败", null);
            return response;
        }
        response=new Response(0, "修改成功", null);
        return response;
    }

    @PostMapping(value = "/todoList/deleteItem")
    public Response deleteTodoListItem(Integer id){
        Response response;
        int cnt=todolistService.deleteTodolistItemById(id);
        if(cnt<=0){
            response=new Response(-1, "删除条目失败", null);
            return response;
        }
        response=new Response(0, "删除条目成功", null);
        return response;
    }

    @PostMapping(value = "/deleteList")
    public Response deleteTodoList(Integer id){
        Response response;
        int cnt=todolistService.deleteTodolistById(id);
        if(cnt<=0){
            response=new Response(-1, "删除列表失败", null);
            return response;
        }
        response=new Response(0, "删除列表成功", null);
        return response;
    }

}
