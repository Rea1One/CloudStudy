package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.PlanetMapper;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class StudyRecordService {
    @Autowired(required = false)
    private StudyRecordMapper recordMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private PlanetMapper planetMapper;

    /**
     * 开始学习
     *
     * @param userId
     * @param planetId
     * @return 0: 成功
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
     * @param operation 1: 普通用户停止自习  3: 主播停止直播
     * @return 0: 成功
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
        long currTime = System.currentTimeMillis();
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

    /**
     * 开始直播
     *
     * @param userId
     * @param planetId
     * @return 插入StudyRecord的记录条数
     */
    @Transactional
    public int startBroadcast(Integer userId, Integer planetId) {
        StudyRecord startStudyRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 0);  // 最近一条开始学习的记录
        StudyRecord stopStudyRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 1);  // 最近一条结束学习的记录
        if (startStudyRecord != null && stopStudyRecord == null) {
            return -1;  // 已开始学习
        }
        if (startStudyRecord != null && stopStudyRecord != null) {
            long startTime = startStudyRecord.getTime().getTime();
            long stopTime = stopStudyRecord.getTime().getTime();
            if (startTime > stopTime) {
                return -1;  // 已开始学习
            }
        }
        StudyRecord startBroadcastRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 2);  // 最近一条开始直播的记录
        StudyRecord stopBroadcastRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(userId, 3);  // 最近一条结束直播的记录
        if (startBroadcastRecord != null && stopBroadcastRecord == null) {
            return -2;  // 已开始直播
        }
        if (startBroadcastRecord != null && stopBroadcastRecord != null) {
            long startTime = startBroadcastRecord.getTime().getTime();
            long stopTime = stopBroadcastRecord.getTime().getTime();
            if (startTime > stopTime) {
                return -2;  // 已开始直播
            }
        }
        StudyRecord record = new StudyRecord();
        record.setOperation(2);
        record.setPlanetId(planetId);
        record.setUsreId(userId);
        int cnt = recordMapper.insertStudyRecord(record);
        if (cnt > 0) {
            return 0;
        } else {
            return -3;  // 插入记录失败
        }
    }

    /**
     * 判断主播是否在线
     *
     * @param planetId
     * @return true/false
     */
    public boolean isOnBroadcast(Integer planetId) {
        Planet p = planetMapper.findPlanetById(planetId);
        int creatorId = p.getCreatorId();
        StudyRecord startRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(creatorId, 2);  // 最近一条开始直播的记录
        StudyRecord stopRecord = recordMapper.findLatestStudyRecordByUserIdAndOperation(creatorId, 3);  // 最近一条结束直播的记录
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

    /**
     * 获取一周内每天的自习时长
     * @param id 用户 id
     * @param batchNum
     * @return List<Long>
     */
    @Transactional
    public List<Long> getStudyTimePerDay(Integer id, Integer batchNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(); //清除缓存
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -batchNum * 7 - 6);//获取查询的第一天
        List<Long> studyTime = new ArrayList<Long>();
        for (int i = 0; i < 7; i++) {
            Timestamp startTime = new Timestamp(getDailyStartTime(calendar.getTimeInMillis()));
            Timestamp endTime = new Timestamp(getDailyEndTime(calendar.getTimeInMillis()));
            List<StudyRecord> records = recordMapper.findAllByUserIdAndTimeBetween(id, startTime, endTime);
            if (records.size() == 0) {
                studyTime.add((long) 0);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                continue;
            }
            if (batchNum == 0 && i == 6 && (records.get(records.size() - 1).getOperation() == 0))
                records.remove(records.size() - 1);
            long dailyTotal = 0;
            for (int j = 0; j < records.size(); j += 2) {
                long time = (records.get(j + 1).getTime().getTime() - records.get(j).getTime().getTime()) / (long) (1000 * 60);
                dailyTotal += time;
            }
            studyTime.add(dailyTotal);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return studyTime;
    }

    /**
     * 获取用户在每个星系的自习统计数据
     * @param id 用户 id
     * @param category 0：当日，1：当周，2：当月
     * @return Map<Integer, Long>
     */
    @Transactional
    public Map<Integer, Long> getStudyTimeInGalaxy(Integer id, Integer category) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(); //清除缓存
        calendar.setTimeInMillis(System.currentTimeMillis());
        Timestamp startTime, stopTime;
        switch (category) {
            case 0:
                startTime = new Timestamp(getDailyStartTime(calendar.getTimeInMillis()));
                stopTime = new Timestamp(getDailyEndTime(calendar.getTimeInMillis()));
                break;
            case 1:
                startTime = new Timestamp(getWeekStartTime(calendar.getTimeInMillis()));
                stopTime = new Timestamp(getWeekEndTime(calendar.getTimeInMillis()));
                break;
            case 2:
                startTime = new Timestamp(getMonthStartTime(calendar.getTimeInMillis()));
                stopTime = new Timestamp(getMonthEndTime(calendar.getTimeInMillis()));
                break;
            default:
                return null;
        }
        Map<Integer, Long> studyTime = new HashMap<Integer, Long>();
        for (int i = 0; i < 6; i++) {
            List<StudyRecord> records = recordMapper.findAllByUserIdAndGalaxyAndTimeBetween(id, i, startTime, stopTime);
            if (records.size() == 0) {
                studyTime.put(i, (long) 0);
                continue;
            }
            if (records.get(records.size() - 1).getOperation() == 0) records.remove(records.size() - 1);
            long total = 0;
            for (int j = 0; j < records.size(); j += 2) {
                long time = (records.get(j + 1).getTime().getTime() - records.get(j).getTime().getTime()) / (long) (1000 * 60);
                total += time;
            }
            studyTime.put(i, total);
        }
        return studyTime;
    }

    /**
     * 根据 StudyRecord 表获取用户总自习时长
     * @param userId
     * @return 用户总自习时长（分钟）
     */
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
            int cnt = userMapper.updateUserInfo(user);
            if (cnt == 0) {
                return -2;  // 修改自习时长失败
            }
        }
        return totalMin;
    }


    /**
     * 获取指定某一天的开始时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    private long getDailyStartTime(Long timeStamp) {
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
    private long getDailyEndTime(Long timeStamp) {
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
    private Long getWeekStartTime(Long timeStamp) {
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
    private Long getWeekEndTime(Long timeStamp) {
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
    private Long getMonthStartTime(Long timeStamp) {
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
    private Long getMonthEndTime(Long timeStamp) {
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
