package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.TodolistServiceGuo;
import com.whu.cloudstudy_server.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2020-04-06
 */

@RestController
@RequestMapping("/other")
public class TodolistControllerGuo {
    @Autowired
    TodolistServiceGuo listService;

    @PostMapping("/createList")
    public Response createList(Integer id, String name) {
        Response response;
        int ret = listService.createList(id, name);
        if (ret == 0) {
            response = new Response(0, "成功", null);
        } else {
            response = new Response(-1, "失败", null);
        }
        return response;
    }

    @PostMapping("/todoList/addItem")
    public Response addItem(Integer id, String content, Integer priority) {
        Response response;
        int ret = listService.addItem(id, content, priority);
        if (ret == 0) {
            response = new Response(0, "成功", null);
        } else if (ret == -1){
            response = new Response(-1, "清单不存在", null);
        } else if (ret == -2) {
            response = new Response(-2, "添加条目失败", null);
        } else {
            response = new Response(-3, "失败", null);
        }
        return response;
    }

    @PostMapping("/todoList/checkItem")
    public Response checkItem(Integer id) {
        Response response;
        int ret = listService.checkItem(id);
        if (ret == 0) {
            response = new Response(0, "成功", null);
        } else if (ret == -1) {
            response = new Response(-1, "条目不存在", null);
        } else if (ret == -2) {
            response = new Response(-2, "勾选条目失败", null);
        } else {
            response = new Response(-3, "失败", null);
        }
        return response;
    }

    @PostMapping("/todoList/uncheckItem")
    public Response uncheckItem(Integer id) {
        Response response;
        int ret = listService.uncheckItem(id);
        if (ret == 0) {
            response = new Response(0, "成功", null);
        } else if (ret == -1) {
            response = new Response(-1, "条目不存在", null);
        } else if (ret == -2) {
            response = new Response(-2, "反勾选条目失败", null);
        } else {
            response = new Response(-3, "失败", null);
        }
        return response;
    }
}
