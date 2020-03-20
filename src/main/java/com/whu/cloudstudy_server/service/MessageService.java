package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.util.Response;
import com.whu.cloudstudy_server.entity.Message;
import com.whu.cloudstudy_server.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Author: 胡龙晨
 * Date: 2020-02-27
 */

@Service
public class MessageService{
    @Autowired(required = false)
    private MessageMapper messageMapper;

    /**
     * 给某用户留言
     *
     * @param senderId
     * @param receiverId
     *  @param content
     * @return
     */
    public int leaveMessage(Integer senderId,Integer receiverId,String content){
        Message message=new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        int cnt = MessageMapper.insertMessage(message);
        if (cnt >  0) {
            return 0;
        }
        else {
            return -1;
        }
    }

    /**
     * 查看留言列表
     *
     * @param id
     * @return
     */
    public List<Message> queryAllMessage(Integer id){
        Message singleResult=messageMapper.findMessageByReceiverId(id);
        if(singleResult != null){
            List<Message> result=new LinkedList<>();
            result.add(singleResult);
            return result;
        }
        else{
            id="%"+id+"%";
            return messageMapper.findMessageByReceiverId(id);
        }
    }

    /**
     * 清空留言列表
     *
     * @param id
     * @return
     */
    public int clearMessage(Integer id){
        int cnt=MessageMapper.deleteMessage(id);
        if(cnt>0){
            return 0;
        }
        else{
            return -1;
        }
    }
}