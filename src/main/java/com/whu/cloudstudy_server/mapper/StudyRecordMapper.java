package com.whu.cloudstudy_server.mapper;

import com.whu.cloudstudy_server.entity.StudyRecord;

import java.sql.Timestamp;
import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2020-03-02
 */
public interface StudyRecordMapper {
    int insertStudyRecord(StudyRecord record);
    int deleteStudyRecordById(Integer id);
    StudyRecord findLatestStudyRecordByUserIdAndPlanetId(Integer userId, Integer planetId);  // 查询某个用户的学习状态
    StudyRecord findLatestStudyRecordByUserIdAndOperation(Integer userId, Integer operation);
    List<StudyRecord> findStudyRecordByPlanetId(Integer planetId, Integer batchNum);   //查询某个星球中的用户动态
    List<StudyRecord> findAllByUserIdAndTimeBetween(Integer userId, Timestamp startTime, Timestamp stopTime);
    List<StudyRecord> findAllByUserIdAndGalaxyAndTimeBetween(Integer userId,Integer galaxy,Timestamp startTime,Timestamp stopTime);
    List<StudyRecord> findAllByUserIdAndPlanetId(Integer userId, Integer planetId);
    List<StudyRecord> findAllByUserIdAndPlanetIdAndTimeBetween(Integer userId,Integer planetId,Timestamp startTime,Timestamp stopTime);
}
