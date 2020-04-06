package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Todolist;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-04-05
 */

public interface TodolistMapper {
    int insertTodolist(Todolist todolist);
    int deleteTodolistById(Integer id);
    List<Todolist> findTodolistByUserId(Integer userId);
    Todolist findListById(Integer id);
}

