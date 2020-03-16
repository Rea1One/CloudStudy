package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Author: 叶瑞雯
 * Date: 2020-03-02
 */

@Service
public class StudyRecordService {
    @Autowired(required = false)
    private StudyRecordMapper studyRecordMapper;

    public List<StudyRecord> findStudyRecordByPlanetId(Integer planetId, Integer batchNum){
        return studyRecordMapper.findStudyRecordByPlanetId(planetId,batchNum);
    }

    /* 3开始, 4结束 */

    /**
     * 开始直播
     *
     * @param userId
     * @param planetId
     * @return 插入StudyRecord的记录条数
     */
    @Transactional
    public int startBroadcast(Integer userId, Integer planetId) {
        StudyRecord startRecord = studyRecordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 2);  // 最近一条开始直播的记录
        StudyRecord stopRecord = studyRecordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 3);  // 最近一条结束直播的记录
        if (startRecord != null && stopRecord == null) {
            return -1;  // 已开始直播
        }
        if (startRecord != null && stopRecord != null) {
            long startTime = startRecord.getTime().getTime();
            long stopTime = stopRecord.getTime().getTime();
            if (startTime > stopTime) {
                return -1;  // 已开始直播
            }
        }
        StudyRecord record = new StudyRecord();
        record.setOperation(2);
        record.setPlanetId(planetId);
        record.setUsreId(userId);
        int cnt =  studyRecordMapper.insertStudyRecord(record);
        if (cnt > 0) {
            return 0;
        } else {
            return -2;  // 插入记录失败
        }
    }

    @Autowired
    PlanetService planetService;
    /**
     * 判断主播是否在线
     *
     * @param planetId
     * @return true/false
     */
    public boolean isOnBroadcast(Integer planetId){
        Planet p=planetService.findPlanetById(planetId);
        int creatorId=p.getCreatorId();
        StudyRecord startRecord = studyRecordMapper.findLatestStudyRecordByUserIdAndOperation(creatorId, 2);  // 最近一条开始直播的记录
        StudyRecord stopRecord = studyRecordMapper.findLatestStudyRecordByUserIdAndOperation(creatorId, 3);  // 最近一条结束直播的记录
        if (startRecord != null && stopRecord == null) {
            return true;  // 正在直播
        }
        if (startRecord != null && stopRecord != null) {
            long startTime = startRecord.getTime().getTime();
            long stopTime = stopRecord.getTime().getTime();
            if (startTime > stopTime) {
                return true;  // 正在直播
            }
        }
        return false;
    }

    public List<StudyRecord> findAllByUserIdAndTimeBetween(Integer userId, Timestamp startTime, Timestamp stopTime,Integer batchNum){
        return studyRecordMapper.findAllByUserIdAndTimeBetween(userId,startTime,stopTime);
    }
}
