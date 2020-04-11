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

    @PostMapping("/createList")
    public Response<Object> createList(Integer id, String name) {
        return todolistService.createList(id, name);
    }

    @PostMapping("/todoList/addItem")
    public Response<Object> addItem(Integer id, String content, Integer priority) {
        return todolistService.addItem(id, content, priority);
    }

    @PostMapping(value = "/getList")
    public Response queryAllTodoList(Integer id){
        Response response;
        List<Object> result=todolistService.findTodolistAndItem(id);
        if(result==null){
            response=new Response(-1, "失败", null);
            return response;
        }
        response=new Response(0, "成功", result);
        return response;
    }

    @PostMapping("/todoList/checkItem")
    public Response<Object> checkItem(Integer id) {
        Response<Object> response;
        int ret = todolistService.checkItem(id);
        if (ret == 0) {
            response = new Response<>(0, "成功", null);
        } else if (ret == -1) {
            response = new Response<>(-1, "条目不存在", null);
        } else if (ret == -2) {
            response = new Response<>(-2, "勾选条目失败", null);
        } else {
            response = new Response<>(-3, "失败", null);
        }
        return response;
    }

    @PostMapping("/todoList/uncheckItem")
    public Response<Object> uncheckItem(Integer id) {
        Response<Object> response;
        int ret = todolistService.uncheckItem(id);
        if (ret == 0) {
            response = new Response<>(0, "成功", null);
        } else if (ret == -1) {
            response = new Response<>(-1, "条目不存在", null);
        } else if (ret == -2) {
            response = new Response<>(-2, "反勾选条目失败", null);
        } else {
            response = new Response<>(-3, "失败", null);
        }
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
