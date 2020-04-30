package com.whu.cloudstudy_server.controller;

import com.whu.cloudstudy_server.service.StudyRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
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
 * Date: 2020-04-29
 */

@ServerEndpoint("/groupStudyServer/{userId}/{planetId}")
@Component
public class DiscussSocketServer {
    private Session session;
    private Integer userId;
    private Integer planetId;
    private boolean isFrontCam;
    private Timer timer;
    private long prevTime;

    // 存放所有连接服务的客户端，根据星球 id 将用户分组
    private static Map<Integer, List<DiscussSocketServer>> users = new ConcurrentHashMap<>();

    private static Log log = LogFactory.getLog(DiscussSocketServer.class);

    private static StudyRecordService recordService;

    @Autowired
    public void setService(StudyRecordService recordService) {
        DiscussSocketServer.recordService = recordService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId, @PathParam("planetId") Integer planetId) {
        this.session = session;
        this.userId = userId;
        this.planetId = planetId;
        this.isFrontCam = true;
        prevTime = System.currentTimeMillis();

        if (users.containsKey(planetId)) {  // 该星球已存在
            users.get(planetId).add(this);  // 存入该用户连接
        } else {  // 该星球未存在
            List<DiscussSocketServer> userList = new LinkedList<>();
            userList.add(this);
            users.put(planetId, userList);
        }
        log.info("User " + userId + ", group study connection success, online count: " + users.size());

        // 开启计时器
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                checkTimeout();
            }
        };
        timer.scheduleAtFixedRate(task, new Date(), 1000);

        // 将聊天室内其他用户摄像头状态反馈
        JSONArray arr = new JSONArray();
        List<DiscussSocketServer> userList = users.get(planetId);
        for (DiscussSocketServer user : userList) {
            if (!user.equals(this)) {
                JSONObject info = new JSONObject();
                info.put("userId", user.userId);
                info.put("camstate", user.isFrontCam);
                arr.put(info);
            }
        }
        JSONObject jsonBroadcast = new JSONObject();
        jsonBroadcast.put("id", 1);
        jsonBroadcast.put("info", arr);
        sendMessageTo(jsonBroadcast.toString(), this);
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
     * id == 1 --> 修改该用户服务器中的摄像头状态，并将修改后的信息广播
     *
     * @param jsonMsg
     */
    private void myOnMessage(String jsonMsg) {
        log.info("User " + userId + ", received message: " + jsonMsg);
        JSONObject jo = new JSONObject(jsonMsg);
        Integer id = jo.getInt("id");
        if (id.equals(0)) {  // 监测用户是否在线 (heartbeat)
            prevTime = System.currentTimeMillis();
        } else if (id.equals(1)) {  // 修改该用户服务器中的摄像头状态，并将修改后的信息广播
            isFrontCam = jo.getBoolean("camstate");
            JSONArray arr = new JSONArray();
            List<DiscussSocketServer> userList = users.get(planetId);
            for (DiscussSocketServer user : userList) {
                JSONObject info = new JSONObject();
                info.put("userId", user.userId);
                info.put("camstate", user.isFrontCam);
                arr.put(info);
            }
            JSONObject jsonBroadcast = new JSONObject();
            jsonBroadcast.put("id", 1);
            jsonBroadcast.put("info", arr);
            sendMessage(planetId, jsonBroadcast.toString());
        }
    }

    /**
     * 将 message 发送给星球内其他用户
     *
     * @param planetId
     * @param message
     */
    private void sendMessage(Integer planetId, String message) {
        List<DiscussSocketServer> userList = users.get(planetId);
        for (DiscussSocketServer user : userList) {
            if (user.equals(this)) continue;  // 不给自己发
            // 不能放在循环外面否则只能把消息发给第一个用户，其他用户收到的消息是空的
            ByteBuffer bBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            user.session.getAsyncRemote().sendBinary(bBuffer);
        }
        log.info("User " + userId + ", sent message: " + message);
    }

    /**
     * 将 message 发送给指定用户
     *
     * @param message
     * @param dest
     */
    private void sendMessageTo(String message, DiscussSocketServer dest) {
        ByteBuffer bBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        dest.session.getAsyncRemote().sendBinary(bBuffer);
        log.info("User " + userId + ", sent message: " + message);
    }

    private void stopStudyBySocket(Integer userId, Integer planetId) {
        int ret = recordService.stopStudy(userId, planetId, 1);
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
}
