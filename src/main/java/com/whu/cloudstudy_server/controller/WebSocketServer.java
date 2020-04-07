package com.whu.cloudstudy_server.controller;


import com.whu.cloudstudy_server.service.StudyRecordServiceGuo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: 郭瑞景
 * Date: 2020-03-09
 */

/**
 * @ServerEndpoint 这个注解的作用：
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个 WebSocket 的服务端
 * 注解的值为客户端连接访问的 url
 */
@ServerEndpoint("/websocket/{userId}/{planetId}")
@Component
public class WebSocketServer {
    // 与某个客户端的连接对话，需要通过它来给客户端发送消息
    private Session session;
    // 标识当前连接客户端的用户
    private Integer userId;

    private Integer planetId;

    private Timer timer;

    private long prevTime;
    // 存放所有连接服务的客户端
    private static Map<Integer, WebSocketServer> clients = new ConcurrentHashMap<>();

    private static Log log = LogFactory.getLog(WebSocketServer.class);

    private static StudyRecordServiceGuo recordService;

    @Autowired
    public void setService(StudyRecordServiceGuo recordService) {
        WebSocketServer.recordService = recordService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId, @PathParam("planetId") Integer planetId) {
        this.session = session;
        this.userId = userId;
        this.planetId = planetId;
        prevTime = System.currentTimeMillis();
        // userId 用来表示唯一客户端，如果需要指定发送，需要指定发送通过 userId 来区分
        clients.put(userId, this);
        log.info("User: " + userId + ", connect successfully, online count: " + clients.size());
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                checkTimeout();
            }
        };
        timer.scheduleAtFixedRate(task, new Date(), 1000);
    }

    @OnClose
    public void onClose() {
        clients.remove(userId);
        log.info("User: " + userId + ", disconnect successfully, online count: " + clients.size());
        stopStudyBySocket(userId, planetId);
        timer.cancel();
        System.gc();
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(byte[] message) {
        log.info("Received message: " + new String(message, StandardCharsets.UTF_8));
        prevTime = System.currentTimeMillis();
    }


    private void stopStudyBySocket(Integer userId, Integer planetId) {
        int ret = recordService.stopStudy(userId, planetId, 1);
        switch (ret) {
            case 0:
                log.info("User: " + userId + ", stop study successfully");
                break;
            case -1:
                log.info("User: " + userId + ", was not studying");
                break;
            case -2:
                log.info("User: " + userId + ", StudyRecord insertion failed");
                break;
            case -3:
                log.info("User: " + userId + ", does not exist");
                break;
            case -4:
                log.info("User: " + userId + ", update study time failed");
                break;
            case -5:
                log.info("User: " + userId + ", StudyRecord deletion failed");
                break;
            default:
                break;
        }
    }

    private void checkTimeout() {
        long currTime = System.currentTimeMillis();
        if (currTime - prevTime > 10000) {
            onClose();
        }
    }
}
