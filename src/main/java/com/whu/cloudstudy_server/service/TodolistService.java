package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Todolist;
import com.whu.cloudstudy_server.entity.TodolistItem;
import com.whu.cloudstudy_server.mapper.TodolistItemMapper;
import com.whu.cloudstudy_server.mapper.TodolistMapper;
import com.whu.cloudstudy_server.util.Response;
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

    /**
     * 创建清单
     *
     * @param id 用户 id
     * @param name 清单名称
     * @return Response<Object>
     */
    @Transactional
    public Response<Object> createList(Integer id, String name) {
        Todolist list = new Todolist();
        list.setName(name);
        list.setUserId(id);
        int cnt = todolistMapper.insertTodolist(list);
        if (cnt > 0) {
            return new Response<>(0, "成功", list.getId());
        }
        else {
            return new Response<>(-1, "失败", null);
        }
    }

    /**
     * 在清单内添加条目
     *
     * @param id 清单 id
     * @param content 内容
     * @param priority 优先级
     * @return Response<Object>
     */
    @Transactional
    public Response<Object> addItem(Integer id, String content, Integer priority) {
        Todolist list = todolistMapper.findListById(id);
        if (list == null) {  // 清单不存在
            return new Response<>(-1, "清单不存在", null);
        }
        TodolistItem item = new TodolistItem();
        item.setContent(content);
        item.setIsFinished(0);  // 0 未完成, 1 已完成
        item.setPriority(priority);
        item.setTodolistId(id);
        int cnt = todolistItemMapper.insertTodolistItem(item);
        if (cnt > 0) {
            return new Response<>(0, "成功", item.getId());
        }
        else {  // 添加条目失败
            return new Response<>(-2, "添加条目失败", null);
        }
    }

    /**
     * 勾选清单条目
     *
     * @param id 清单 id
     * @return 0: 成功
     */
    @Transactional
    public int checkItem(Integer id) {
        TodolistItem item = todolistItemMapper.findTodolistItemById(id);
        if (item == null) {  // 条目不存在
            return -1;
        }
        item.setIsFinished(1);
        int cnt = todolistItemMapper.updateTodolistItemInfo(item);
        if (cnt > 0) {
            return 0;
        }
        else {  // 勾选条目失败
            return -2;
        }
    }

    /**
     * 反勾选清单条目
     *
     * @param id 清单 id
     * @return 0: 成功
     */
    @Transactional
    public int uncheckItem(Integer id) {
        TodolistItem item = todolistItemMapper.findTodolistItemById(id);
        if (item == null) {  // 条目不存在
            return -1;
        }
        item.setIsFinished(0);
        int cnt = todolistItemMapper.updateTodolistItemInfo(item);
        if (cnt > 0) {
            return 0;
        }
        else {  // 反勾选条目失败
            return -2;
        }
    }

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
