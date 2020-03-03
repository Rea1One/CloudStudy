package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.StudyRecord;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 郭瑞景
 * @date 2020-03-02
 */
public interface StudyRecordMapper {
    void insertStudyRecord(StudyRecord record);
    StudyRecord findLatestStudyRecordByUserId(Integer userId);  // 查询某个用户的学习状态
    List<StudyRecord> findStudyRecordByPlanetId(Integer planetId, Integer batchNum);   //查询某个星球中的用户动态
    List<StudyRecord> findAllByUserIdAndTimeBetween(Integer userId, Timestamp startTime, Timestamp stopTime);
}
