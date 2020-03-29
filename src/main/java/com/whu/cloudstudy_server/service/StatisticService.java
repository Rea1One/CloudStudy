package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * Author: 叶瑞雯
 * Date: 2020-03-16
 */

@Service
public class StatisticService {
    @Autowired(required = false)
    StudyRecordMapper studyRecordMapper;

    @Transactional
    public List<Long> getStudyTimePerDay(Integer id,Integer batchNum){
        Calendar calendar=Calendar.getInstance();
        calendar.clear(); //清除缓存
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -batchNum*7-6);//获取查询的第一天
        List<Long> studyTime=new ArrayList<Long>();
        for(int i=0;i<7;i++){
            Timestamp startTime=new Timestamp(getDailyStartTime(calendar.getTimeInMillis()));
            Timestamp endTime=new Timestamp(getDailyEndTime(calendar.getTimeInMillis()));
            List<StudyRecord> records=studyRecordMapper.findAllByUserIdAndTimeBetween(id,startTime,endTime);
            if(records.size()==0){
                studyTime.add((long) 0);
                calendar.add(Calendar.DAY_OF_MONTH,1);
                continue;
            }
            if(batchNum==0&&i==6&&(records.get(records.size()-1).getOperation()==0)) records.remove(records.size()-1);
            long dailyTotal=0;
            for(int j=0;j<records.size();j+=2){
                long time=(records.get(j+1).getTime().getTime()-records.get(j).getTime().getTime()) / (long)(1000*60);
                dailyTotal+=time;
            }
            studyTime.add(dailyTotal);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return studyTime;
    }

    @Transactional
    public Map<Integer,Long> getStudyTimeInGalaxy(Integer id,Integer category){
        Calendar calendar=Calendar.getInstance();
        calendar.clear(); //清除缓存
        calendar.setTimeInMillis(System.currentTimeMillis());
        Timestamp startTime,stopTime;
        switch (category){
            case 0:
                startTime=new Timestamp(getDailyStartTime(calendar.getTimeInMillis()));
                stopTime=new Timestamp(getDailyEndTime(calendar.getTimeInMillis()));
                break;
            case 1:
                startTime=new Timestamp(getWeekStartTime(calendar.getTimeInMillis()));
                stopTime=new Timestamp(getWeekEndTime(calendar.getTimeInMillis()));
                break;
            case 2:
                startTime=new Timestamp(getMonthStartTime(calendar.getTimeInMillis()));
                stopTime=new Timestamp(getMonthEndTime(calendar.getTimeInMillis()));
                break;
            default:
                return null;
        }
        Map<Integer,Long> studyTime=new HashMap<Integer, Long>();
        for(int i=0;i<6;i++){
            List<StudyRecord> records=studyRecordMapper.findAllByUserIdAndGalaxyAndTimeBetween(id,i,startTime,stopTime);
            if(records.size()==0){
                studyTime.put(i,(long)0);
                continue;
            }
            if(records.get(records.size()-1).getOperation()==0) records.remove(records.size()-1);
            long total=0;
            for(int j=0;j<records.size();j+=2){
                long time=(records.get(j+1).getTime().getTime()-records.get(j).getTime().getTime()) / (long)(1000*60);
                total+=time;
            }
            studyTime.put(i,total);
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

    /**
     * 获取当周开始时间戳
     */
    public static Long getWeekStartTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当周的结束时间戳
     */
    public static Long getWeekEndTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月开始时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static Long getMonthStartTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月的结束时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    public static Long getMonthEndTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
}
