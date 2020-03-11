package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
