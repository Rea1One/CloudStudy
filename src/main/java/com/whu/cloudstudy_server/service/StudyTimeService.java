package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2020-04-09
 */

@Service
public class StudyTimeService {
    @Autowired(required = false)
    private StudyRecordMapper recordMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

    @Transactional
    public Integer getTotalStudyTimeFromRecord(Integer userId) {
        List<StudyRecord> records = recordMapper.findAllByUserId(userId);
        long totalMS = 0, period;
        for (int i = 0; i < records.size(); i += 2) {
            period = records.get(i + 1).getTime().getTime() - records.get(i).getTime().getTime();
            totalMS = period + totalMS;
        }
        Integer totalMin = Math.toIntExact(totalMS) / 60000;

        // 如果不一致则更新用户学习时长
        User user = userMapper.findUserById(userId);
        if (user == null) {
            return -1;  // 用户不存在
        }
        if (!user.getStudyTime().equals(totalMin)) {
            user.setStudyTime(totalMin);
        }
        return totalMin;
    }
}
