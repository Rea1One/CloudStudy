package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 叶瑞雯
 * @date 2020-03-02
 */

@Service
public class StudyRecordService {
    @Autowired(required = false)
    private StudyRecordMapper studyRecordMapper;

    public StudyRecord findLatestStudyRecordByUserId(Integer userId){
        return studyRecordMapper.findLatestStudyRecordByUserId(userId);
    }

    public List<StudyRecord> findStudyRecordByPlanetId(Integer planetId, Integer batchNum){
        return studyRecordMapper.findStudyRecordByPlanetId(planetId,batchNum);
    }
}
