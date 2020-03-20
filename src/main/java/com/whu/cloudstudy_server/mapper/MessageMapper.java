package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 胡龙晨
 * @date 2020-02-26
 */

@Mapper
public interface MessageMapper{
    List<Message> findMessageByReceiverId(Integer receiverId);
    int insertMessage(Message message);
    int deleteMessage(Integer receiverId);
}