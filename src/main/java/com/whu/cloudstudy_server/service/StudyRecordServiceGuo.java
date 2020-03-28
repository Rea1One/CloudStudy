package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Author: 郭瑞景
 * Date: 2020-03-03
 */

@Service
public class StudyRecordServiceGuo {
    @Autowired(required = false)
    private StudyRecordMapper recordMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

    /* 0开始, 1结束 */

    /**
     * 开始学习
     *
     * @param userId
     * @param planetId
     * @return 插入StudyRecord的记录条数
     */
    @Transactional
    public int startStudy(Integer userId, Integer planetId) {
        StudyRecord startRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 0);  // 最近一条开始学习的记录
        StudyRecord stopRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 1);  // 最近一条结束学习的记录
        if (startRecord != null && stopRecord == null) {
            return -1;  // 已开始自习
        }
        if (startRecord != null) {  // startRecord != null && stopRecord != null
            long startTime = startRecord.getTime().getTime();
            long stopTime = stopRecord.getTime().getTime();
            if (startTime > stopTime) {
                return -1;  // 已开始自习
            }
        }
        StudyRecord record = new StudyRecord();
        record.setOperation(0);
        record.setPlanetId(planetId);
        record.setUsreId(userId);
        int cnt = recordMapper.insertStudyRecord(record);
        if (cnt > 0) {
            return 0;
        } else {
            return -2;  // 插入记录失败
        }
    }

    /**
     * 结束学习
     *
     * @param userId
     * @param planetId
     * @param operation 1: 正常用户停止自习  3: 主播停止直播
     * @return 错误/成功代码
     */
    @Transactional
    public int stopStudy(Integer userId, Integer planetId, Integer operation) {
        StudyRecord startRecord = null, stopRecord1 = null;
        if (operation.equals(1)) {
            startRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 0);  // 最近一条开始学习的记录
            stopRecord1 = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 1);  // 最近一条结束学习的记录
        } else if (operation.equals(3)) {
            startRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 2);  // 最近一条开始直播的记录
            stopRecord1 = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 3);  // 最近一条结束直播的记录
        }
        if (startRecord == null) {
            return -1;  // 未开始自习
        }
        long startTime = startRecord.getTime().getTime();
        if (stopRecord1 != null) {  // startRecord != null && stopRecord1 != null
            long stopTime = stopRecord1.getTime().getTime();
            if (startTime < stopTime) {
                return -1;  // 未开始自习
            }
        }
        // 判断自习时长是否超过 1 min
        long currTime = System.currentTimeMillis() + 8 * 60 * 60000;
        long mSec = currTime - startTime;
        Integer studyTime = Math.toIntExact(mSec) / 60000;  // 自习时长 (min)
        if (studyTime < 1) {
            int cnt = recordMapper.deleteStudyRecordById(startRecord.getId());
            if (cnt > 0) {
                return 0;
            } else {
                return -5;  // 删除学习记录失败
            }
        } else {
            StudyRecord stopRecord = new StudyRecord();
            stopRecord.setOperation(operation);
            stopRecord.setPlanetId(planetId);
            stopRecord.setUsreId(userId);
            stopRecord.setTime(new Timestamp(currTime));  // Bug here. Time will be null without this statement.
            int cntRecord = recordMapper.insertStudyRecord(stopRecord);
            if (cntRecord <= 0) {
                return -2;  // 插入学习记录数据失败
            }
            // 更改用户表学习时长
            User user = userMapper.findUserById(userId);
            if (user == null) {
                return -3;  // 用户不存在
            }
            Integer prevStudyTime = user.getStudyTime();
            user.setStudyTime(prevStudyTime + studyTime);
            int cntUser = userMapper.updateUserInfo(user);
            if (cntUser > 0) {  // 成功
                return 0;
            } else {
                return -4;  // 更新用户学习时间失败
            }
        }
    }
}
