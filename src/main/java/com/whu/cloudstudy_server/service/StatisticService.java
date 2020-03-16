package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Author: 叶瑞雯
 * Date: 2020-03-16
 */

@Service
public class StatisticService {
    @Autowired(required = false)
    StudyRecordMapper studyRecordMapper;

    @Transactional
    public List<Long> getStudyTimePerDay(Integer id,Integer batchNum,Integer month){
        Calendar calendar=Calendar.getInstance();
        calendar.clear(); //清除缓存
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-batchNum*7);//获取查询的最后一天
        int dayNum=0;
        if(batchNum<4){
            dayNum=7;
            calendar.add(Calendar.DAY_OF_MONTH,-6);//获取查询的第一天
        }
        else if(batchNum==4){
            dayNum=calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-28;
            calendar.set(Calendar.DAY_OF_MONTH,0);//获取查询的第一天
        }
        List<Long> studyTime=new ArrayList<Long>();
        for(int i=0;i<dayNum;i++){
            Timestamp startTime=new Timestamp(getDailyStartTime(calendar.getTimeInMillis()));
            Timestamp endTime=new Timestamp(getDailyEndTime(calendar.getTimeInMillis()));
            List<StudyRecord> records=studyRecordMapper.findAllByUserIdAndTimeBetween(id,startTime,endTime);
            long dailyTotal=0;
            int m=0;
            if(records.size()==0){
                studyTime.add((long) 0);
                calendar.add(Calendar.DAY_OF_MONTH,1);
                continue;
            }
            if(records.get(0).getOperation()==1){//若当天第一条记录即为结束学习
                m=1;
                dailyTotal+=(records.get(0).getTime().getTime()-startTime.getTime()) / (long)(1000*60);
            }
            if(records.get(records.size()-1).getOperation()==0){//若当天最后一条学习记录为开始学习
                m=1;
                dailyTotal+=(endTime.getTime()-records.get(records.size()-1).getTime().getTime()) / (long)(1000*60);
            }
            for(int j=m;j<records.size()-m;j+=2){
                long time=(records.get(j+1).getTime().getTime()-records.get(j).getTime().getTime()) / (long)(1000*60);
                dailyTotal+=time;
            }
            studyTime.add(dailyTotal);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return studyTime;
    }

    /**
     * 获取指定某一天的开始时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static long getDailyStartTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定某一天的结束时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static long getDailyEndTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
}
