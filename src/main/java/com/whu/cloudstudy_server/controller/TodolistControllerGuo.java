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
    public Response<Object> createList(Integer id, String name) {
        return listService.createList(id, name);
    }

    @PostMapping("/todoList/addItem")
    public Response<Object> addItem(Integer id, String content, Integer priority) {
        return listService.addItem(id, content, priority);
    }

    @PostMapping("/todoList/checkItem")
    public Response<Object> checkItem(Integer id) {
        Response<Object> response;
        int ret = listService.checkItem(id);
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
        int ret = listService.uncheckItem(id);
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
}
