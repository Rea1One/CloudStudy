package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Message;

import java.util.List;

/**
 * @author 胡龙晨
 * @date 2020-02-26
 */

public interface MessageMapper{
    List<Message> findMessageByReceiverId(Integer receiverId);
    int insertMessage(Message message);
    int deleteMessage(Integer receiverId);
}

