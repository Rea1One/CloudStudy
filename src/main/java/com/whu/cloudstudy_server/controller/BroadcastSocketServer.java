package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.StudyRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: 郭瑞景
 * Date: 2020-03-16
 */

@ServerEndpoint(value = "/broadcastServer/{userId}/{planetId}/{userName}/{profileUrl}/{type}")
@Component
public class BroadcastSocketServer {
    private Session session;
    private Integer userId;
    private Integer planetId;
    private boolean isFrontCam;  // 前置摄像头为 true, 后置摄像头为 false
    private Integer type;  // 1是主播  0不是主播
    private Timer timer;
    private long prevTime;

    // 存放所有连接服务的客户端，根据星球 id 将用户分组
    private static Map<Integer, List<BroadcastSocketServer>> users = new ConcurrentHashMap<>();

    private static Log log = LogFactory.getLog(BroadcastSocketServer.class);

    private static StudyRecordService recordService;

    @Autowired
    public void setService(StudyRecordService recordService) {
        BroadcastSocketServer.recordService = recordService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId,
                       @PathParam("planetId") Integer planetId, @PathParam("userName") String userName,
                       @PathParam("profileUrl") String profileUrl, @PathParam("type") int type) {
        this.session = session;
        this.userId = userId;
        this.planetId = planetId;
        this.type = type;
        this.isFrontCam = true;
        String headshot = "http://106.13.41.151:8088/image/" + profileUrl;
        prevTime = System.currentTimeMillis();
        if (users.containsKey(planetId)) {  // 该星球已存在
            users.get(planetId).add(this);  // 存入该用户连接
        } else {  // 该星球未存在
            List<BroadcastSocketServer> userList = new LinkedList<>();
            userList.add(this);
            users.put(planetId, userList);
        }
        log.info("User " + userId + ", broadcast connection success, online count: " + users.size());

        // 开启计时器
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                checkTimeout();
            }
        };
        timer.scheduleAtFixedRate(task, new Date(), 1000);

        // 0：不是主播  1：是主播
        // 1. 通知其他用户 “我”(非主播) 加入了直播间
        // 2. 获取主播摄像头状态
        if (type == 0) {
            // 通知其他用户 “我”(非主播) 加入了直播间
            JSONObject jo = new JSONObject();
            jo.put("id", 1);
            jo.put("username", userName);
            jo.put("userid", userId);
            jo.put("headshot", headshot);
            sendMessage(planetId, jo.toString());

            // 获取主播摄像头状态
            BroadcastSocketServer broadcaster = getBroadcaster(planetId);
            if (broadcaster != null) {
                JSONObject joCam = new JSONObject();
                joCam.put("id", 4);
                joCam.put("camstate", broadcaster.isFrontCam);
                sendMessageTo(joCam.toString(), this);
            }
            else {
                log.info("getBroadcaster(planetId) 获得的主播对象为空");
            }
        }
    }

    @OnClose
    public void onClose() {
        users.get(planetId).remove(this);
        if (users.get(planetId).size() == 0) {
            users.remove(planetId);
        }
        log.info("User " + userId + ", disconnect successfully");
        stopStudyBySocket(userId, planetId);
        timer.cancel();
//        System.gc();
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(byte[] message) {
        String jsonMsg = new String(message, StandardCharsets.UTF_8);
        myOnMessage(jsonMsg);
    }

    @OnMessage
    public void onMessage(String jsonMsg) {
        myOnMessage(jsonMsg);
    }

    /**
     * 根据接收到的 id 执行相应动作
     * id == 0 --> 监测用户是否在线 (heartbeat)
     * id == -1 --> 主播下线, 关闭星球内其他人的连接
     * id == 2 --> 群发弹幕
     * id == 4 --> 广播主播的摄像头状态
     *
     * @param jsonMsg 接收到的 JSON 字符串
     */
    private void myOnMessage(String jsonMsg) {
        log.info("User " + userId + ", received message: " + jsonMsg);
        JSONObject jo = new JSONObject(jsonMsg);
        Integer id = jo.getInt("id");
        if (id.equals(0)) {  // id 为 0 时用于监测用户是否在线
            prevTime = System.currentTimeMillis();
        } else if (id.equals(-1)) {  // 主播下线
            JSONObject jo2 = new JSONObject();
            jo2.put("id", 3);
            sendMessage(planetId, jo2.toString());
        } else if (id.equals(2)) {  // 群发弹幕
            String headshot = "http://106.13.41.151:8088/image/" + jo.getString("headshot");
            jo.put("headshot", headshot);
            sendMessage(planetId, jo.toString());
        } else if (id.equals(4)) {  // 广播主播的摄像头状态
            if (type == 1) {
                isFrontCam = jo.getBoolean("camstate");
                sendMessage(planetId, jo.toString());
            }
        }
    }

    /**
     * 将 message 发送给星球内其他用户
     *
     * @param planetId
     * @param message
     */
    private void sendMessage(Integer planetId, String message) {
        List<BroadcastSocketServer> userList = users.get(planetId);
        for (BroadcastSocketServer user : userList) {
            if (user.equals(this)) continue;  // 不给自己发
            // 不能放在循环外面否则只能把消息发给第一个用户，其他用户收到的消息是空的
            ByteBuffer bBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            user.session.getAsyncRemote().sendBinary(bBuffer);
        }
        log.info("User " + userId + ", sent message: " + message);
    }

    /**
     * 将 message 发送给指定用户
     * @param message
     * @param dest
     */
    private void sendMessageTo(String message, BroadcastSocketServer dest) {
        ByteBuffer bBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        dest.session.getAsyncRemote().sendBinary(bBuffer);
        log.info("User " + userId + ", sent message: " + message);
    }

    private void stopStudyBySocket(Integer userId, Integer planetId) {
        Integer operation;
        if (type == 1) {  // 主播
            operation = 3;
        } else {  // 普通用户
            operation = 1;
        }
        int ret = recordService.stopStudy(userId, planetId, operation);
        switch (ret) {
            case 0:
                log.info("User " + userId + ", stop study successfully");
                break;
            case -1:
                log.info("User " + userId + ", was not studying");
                break;
            case -2:
                log.info("User " + userId + ", StudyRecord insertion failed");
                break;
            case -3:
                log.info("User " + userId + ", does not exist");
                break;
            case -4:
                log.info("User " + userId + ", update study time failed");
                break;
            case -5:
                log.info("User " + userId + ", StudyRecord deletion failed");
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

    /**
     * 获取直播星球的主播
     * @param planetId
     * @return
     */
    private BroadcastSocketServer getBroadcaster(Integer planetId) {
        List<BroadcastSocketServer> userList = users.get(planetId);
        for (BroadcastSocketServer user : userList) {
            if (user.type == 1) {
                return user;
            }
        }
        return null;
    }
}
