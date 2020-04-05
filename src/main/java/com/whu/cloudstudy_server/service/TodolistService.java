package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Todolist;
import com.whu.cloudstudy_server.entity.TodolistItem;
import com.whu.cloudstudy_server.mapper.TodolistItemMapper;
import com.whu.cloudstudy_server.mapper.TodolistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 叶瑞雯
 * Date: 2020-04-05
 */

@Service
public class TodolistService {
    @Autowired(required = false)
    TodolistMapper todolistMapper;

    @Autowired(required = false)
    TodolistItemMapper todolistItemMapper;

    @Transactional
    public List<Object> findTodolistAndItem(Integer userId){
        List<Todolist> todolists=todolistMapper.findTodolistByUserId(userId);
        if(todolists==null) return new ArrayList<>();
        List<Object> result=new ArrayList<>();
        for(Todolist t:todolists){
            List<TodolistItem> todolistItems=todolistItemMapper.findTodolistItemByTodolistId(t.getId());
            List<Object> temp=new ArrayList<>();
            temp.add(t);
            temp.add(todolistItems);
            result.add(temp);
        }
        return result;
    }

    @Transactional
    public int deleteTodolistById(Integer id){
        List<TodolistItem> todolistItems=todolistItemMapper.findTodolistItemByTodolistId(id);
        if(todolistItems==null) return todolistMapper.deleteTodolistById(id);
        for(TodolistItem t:todolistItems){
            int cnt=todolistItemMapper.deleteTodolistItemById(t.getId());
            if(cnt<=0) return cnt;
        }
        return todolistMapper.deleteTodolistById(id);
    }

    @Transactional
    public int deleteTodolistItemById(Integer id){
        return todolistItemMapper.deleteTodolistItemById(id);
    }

    @Transactional
    public int updateTodolistItemInfo(TodolistItem todolistItem){
        return todolistItemMapper.updateTodolistItemInfo(todolistItem);
    }

    @Transactional
    public TodolistItem findTodolistItemById(Integer id){
        return todolistItemMapper.findTodolistItemById(id);
    }
}
