package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Todolist;
import com.whu.cloudstudy_server.entity.TodolistItem;
import com.whu.cloudstudy_server.mapper.TodolistItemMapper;
import com.whu.cloudstudy_server.mapper.TodolistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: 郭瑞景
 * Date: 2020-04-06
 */

@Service
public class TodolistServiceGuo {
    @Autowired(required = false)
    TodolistMapper listMapper;

    @Autowired(required = false)
    TodolistItemMapper itemMapper;

    @Transactional
    public int createList(Integer id, String name) {
        Todolist list = new Todolist();
        list.setName(name);
        list.setUserId(id);
        int cnt = listMapper.insertTodolist(list);
        if (cnt > 0) return 0;
        else return -1;
    }

    @Transactional
    public int addItem(Integer id, String content, Integer priority) {
        Todolist list = listMapper.findListById(id);
        if (list == null) return -1;  // 清单不存在
        TodolistItem item = new TodolistItem();
        item.setContent(content);
        item.setIsFinished(0);  // 0 未完成, 1 已完成
        item.setPriority(priority);
        item.setTodolistId(id);
        int cnt = itemMapper.insertTodolistItem(item);
        if (cnt > 0) return 0;
        else return -2;  // 添加条目失败
    }

    @Transactional
    public int checkItem(Integer id) {
        TodolistItem item = itemMapper.findTodolistItemById(id);
        if (item == null) return -1;  // 条目不存在
        item.setIsFinished(1);
        int cnt = itemMapper.updateTodolistItemInfo(item);
        if (cnt > 0) return 0;
        else return -2;  // 勾选条目失败
    }

    @Transactional
    public int uncheckItem(Integer id) {
        TodolistItem item = itemMapper.findTodolistItemById(id);
        if (item == null) return -1;  // 条目不存在
        item.setIsFinished(0);
        int cnt = itemMapper.updateTodolistItemInfo(item);
        if (cnt > 0) return 0;
        else return -2;  // 反勾选条目失败
    }
}
