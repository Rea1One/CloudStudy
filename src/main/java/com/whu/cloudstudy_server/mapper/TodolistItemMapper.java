package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.TodolistItem;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-04-05
 */

public interface TodolistItemMapper {
    List<TodolistItem> findTodolistItemByTodolistId(Integer todolistId);
    int insertTodolistItem(TodolistItem todolistItem);
    int deleteTodolistItemById(Integer id);
    int updateTodolistItemInfo(TodolistItem todolistItem);
}

