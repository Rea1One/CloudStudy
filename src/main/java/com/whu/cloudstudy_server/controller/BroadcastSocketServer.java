package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.StudyRecordServiceGuo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: 郭瑞景
 * Date: 2020-03-16
 */

@ServerEndpoint("/broadcastServer/{userId}/{planetId}/{userName}/{profileUrl}/{type}")
@Component
public class BroadcastSocketServer {

    private Session session;

    private Integer userId;

    private Integer planetId;

    private Timer timer;

    private long prevTime;

    // 存放所有连接服务的客户端，根据星球 id 将用户分组
    private static Map<Integer, List<BroadcastSocketServer>> users = new ConcurrentHashMap<>();

    private static Log log = LogFactory.getLog(BroadcastSocketServer.class);

    private static StudyRecordServiceGuo recordService;

    @Autowired
    public void setService(StudyRecordServiceGuo recordService) {
        BroadcastSocketServer.recordService = recordService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId, @PathParam("planetId") Integer planetId,
                       @PathParam("userName") String userName, @PathParam("profileUrl") String profileUrl, @PathParam("type") int type) {
        this.session = session;
        this.userId = userId;
        this.planetId = planetId;
        prevTime = System.currentTimeMillis();
        if (users.containsKey(planetId)) {  // 该星球已存在
            users.get(planetId).add(this);  // 存入该用户连接
        } else {  // 该星球未存在
            List<BroadcastSocketServer> userList = new LinkedList<>();
            userList.add(this);
            users.put(planetId, userList);
        }
        log.info("User: " + userId + ", connect successfully");
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                checkTimeout();
            }
        };
        timer.scheduleAtFixedRate(task, new Date(), 1000);

        // 0：不是主播  1：是主播
        // 通知其他用户 “我”(非主播) 加入了直播间
        if (type == 0) {
            JSONObject jo = new JSONObject();
            jo.put("id", planetId);
            jo.put("username", userName);
            jo.put("userid", userId);
            jo.put("headshot", profileUrl);
            sendMessage(planetId, jo.toString());
        }
    }

    @OnClose
    public void onClose() {
        users.get(planetId).remove(this);
        log.info("User: " + userId + ", disconnect successfully");
        stopStudyBySocket(userId, planetId);
        timer.cancel();
        System.gc();
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String jsonMsg) {
        JSONObject jo = new JSONObject(jsonMsg);
        Integer planetId = jo.getInt("id");
        if (planetId.equals(0)) {  // id 为 0 时用于监测用户是否在线
            prevTime = System.currentTimeMillis();
            log.info("Heartbeat received");
        } else {
            sendMessage(planetId, jo.toString());
            log.info("Group message sent");
        }
    }

    private void sendMessage(Integer planetId, String jsonMsg) {
        List<BroadcastSocketServer> userList = users.get(planetId);
        for (BroadcastSocketServer user : userList) {
            user.session.getAsyncRemote().sendText(jsonMsg);
        }
    }

    private void stopStudyBySocket(Integer userId, Integer planetId) {
        int ret = recordService.stopStudy(userId, planetId);
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
