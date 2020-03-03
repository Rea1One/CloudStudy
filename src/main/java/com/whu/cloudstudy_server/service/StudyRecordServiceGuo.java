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
 * @author 郭瑞景
 * @date 2020-03-03
 */

@Service
public class StudyRecordServiceGuo {
    @Autowired(required = false)
    private StudyRecordMapper recordMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

    /* 0开始, 1结束 */
    @Transactional
    public void startStudy(Integer userId, Integer planetId) {
        StudyRecord record = new StudyRecord();
        record.setOperation(0);
        record.setPlanetId(planetId);
        record.setUsreId(userId);
        recordMapper.insertStudyRecord(record);
    }

    @Transactional
    public int stopStudy(Integer userId, Integer planetId) {
        StudyRecord stopRecord = new StudyRecord();
        stopRecord.setOperation(1);
        stopRecord.setPlanetId(planetId);
        stopRecord.setUsreId(userId);
        stopRecord.setTime(new Timestamp(System.currentTimeMillis()));
        recordMapper.insertStudyRecord(stopRecord);  // Bug here. Time is null.

        // 更改用户表学习时长
        User user = userMapper.findUserById(userId);
        if (user == null) {
            return -1;  // 用户不存在
        }
        StudyRecord startRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 0);
        if (startRecord == null) {
            return -2;  // 用户没有开始学习的记录
        }
        long mSec = stopRecord.getTime().getTime() - startRecord.getTime().getTime();
        Integer studyTime = Math.toIntExact(mSec) / 60000;
        Integer prevStudyTime = user.getStudyTime();
        user.setStudyTime(prevStudyTime + studyTime);
        userMapper.updateUserInfo(user);
        return 0;
    }
}
