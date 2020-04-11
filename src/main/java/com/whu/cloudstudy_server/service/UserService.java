package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.entity.Message;
import com.whu.cloudstudy_server.entity.Planet;
import com.whu.cloudstudy_server.entity.StudyRecord;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.MessageMapper;
import com.whu.cloudstudy_server.mapper.StudyRecordMapper;
import com.whu.cloudstudy_server.mapper.UserAndPlanetMapper;
import com.whu.cloudstudy_server.mapper.UserMapper;
import com.whu.cloudstudy_server.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;


/**
 * Author: 叶瑞雯
 * Date: 2020-02-25
 */

@Service
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private MessageMapper messageMapper;

    @Autowired(required = false)
    private UserAndPlanetMapper userAndPlanetMapper;

    @Autowired(required = false)
    private StudyRecordMapper studyRecordMapper;

    public User findUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    @Transactional
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Transactional
    public int deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }

    @Transactional
    public int updateUserInfo(User user) {
        return userMapper.updateUserInfo(user);
    }

    private LinkedHashMap<Integer, String> codeMap = new LinkedHashMap<>();  // 验证码-邮箱

    /**
     * 获取验证码
     *
     * @param email 用户邮箱
     * @return 0: 成功
     */
    public int sendCode(String email, Integer type) {
        User user = userMapper.findUserByEmail(email);
        if (type.equals(0) && user != null) {  // 注册
            return -1;  // 邮箱已存在
        }
        if (type.equals(1) && user == null) {  // 找回密码
            return -2;  // 用户不存在
        }
        EmailUtil emailUtil = new EmailUtil();

        // 6位验证码
        Random random = new Random();
        Integer randomCode = random.nextInt(999999) % 900000 + 100000;

        // 发送邮件
        String content = randomCode.toString();
        boolean isSuccess = emailUtil.sendMessage(email, "【云自习室】验证", content);
        if (!isSuccess) {  // 邮件发送失败
            return -3;
        }
        codeMap.put(randomCode, email);
        return 0;
    }

    /**
     * 注册
     *
     * @param name
     * @param password
     * @param gender
     * @param introduction
     * @param email
     * @param code
     * @param age
     * @return
     */
    @Transactional
    public int register(String name, String password, Integer gender,
                        String introduction, String email, Integer code, Integer age) {
        boolean isMatch = false;
        // 逆序遍历codeMap找到发给该邮箱的验证码
        ListIterator<Map.Entry<Integer, String>> iterator = new ArrayList<>(
                codeMap.entrySet()).listIterator(codeMap.size());
        while (iterator.hasPrevious()) {
            Map.Entry<Integer, String> entry = iterator.previous();
            if (entry.getValue().equals(email) && entry.getKey().equals(code)) {
                isMatch = true;
                codeMap.remove(code);  // 删掉匹配的这一项
                break;
            }
        }
        if (!isMatch) {  // 验证码错误
            return -1;
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setSex(gender);
        user.setSignature(introduction);
        user.setEmail(email);
        user.setAge(age);
        int cnt = userMapper.insertUser(user);
        if (cnt > 0) {
            return 0;
        }
        else {
            return -2;  // 插入用户数据失败
        }
    }

    public int validateCode(String email, Integer code) {
        // 逆序遍历codeMap找到发给该邮箱的验证码
        ListIterator<Map.Entry<Integer, String>> iterator = new ArrayList<>(
                codeMap.entrySet()).listIterator(codeMap.size());
        while (iterator.hasPrevious()) {
            Map.Entry<Integer, String> entry = iterator.previous();
            if (entry.getValue().equals(email) && entry.getKey().equals(code)) {
                codeMap.remove(code);  // 删掉匹配的这一项
                return 0;
            }
        }
        return -1;  // 验证码错误
    }

    /**
     * 修改头像
     * @param id 用户 id
     * @param data 新图片
     * @return 新图片的 url 地址
     */
    @Transactional
    public String changeProfile(Integer id, MultipartFile data) {
        User user = findUserById(id);
        if (user == null) return null;  // 用户不存在
        String photoName = id.toString() + ".jpg";
        String path = "/CloudStudy/Images/" + photoName;
        File uploaded = new File(path);
        try {
            data.transferTo(uploaded);
            String imageUrl = "http://106.13.41.151:8088/image/" + photoName;
            System.out.println(imageUrl);
            user.setPhoto(imageUrl);
            int cnt = updateUserInfo(user);
            if (cnt > 0) {
                return imageUrl;
            } else {
                return null;  // 更新用户头像失败
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 图像上传失败
        }
    }

    /**
     * 找回密码
     * @param email
     * @param newPassword
     * @return
     */
    public int findPassword(String email,String newPassword){
        User user=userMapper.findUserByEmail(email);
        user.setPassword(newPassword);
        int cnt = userMapper.updateUserInfo(user);
        if (cnt > 0) {
            return 0;
        }
        else {
            return -1;
        }
    }

    /**
     * 给某用户留言
     * @param senderId
     * @param receiverId
     *  @param content
     * @return
     */
    public int leaveMessage(Integer senderId,Integer receiverId,String content){
        Message message=new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        int cnt = messageMapper.insertMessage(message);
        if (cnt >  0) {
            return 0;
        }
        else {
            return -1;
        }
    }

    /**
     * 查看留言列表
     *
     * @param id
     * @return
     */
    public List<Message> queryAllMessage(Integer id){
        return messageMapper.findMessageByReceiverId(id);
    }

    /**
     * 清空留言列表
     * @param id
     * @return
     */
    public int clearMessage(Integer id){
        int cnt=messageMapper.deleteMessage(id);
        if(cnt>0){
            return 0;
        }
        else{
            return -1;
        }
    }

    public List<Planet> findPlanetByUserId(Integer userId){
        return userAndPlanetMapper.findPlanetByUserId(userId);
    }

    @Transactional
    public List<Planet> getThreeMostPlanet(Integer id){
        Calendar calendar=Calendar.getInstance();
        calendar.clear(); //清除缓存
        calendar.setTimeInMillis(System.currentTimeMillis());
        Timestamp startTime=new Timestamp(getWeekStartTime(calendar.getTimeInMillis()));
        Timestamp stopTime=new Timestamp(getWeekEndTime(calendar.getTimeInMillis()));
        List<Planet> planets=userAndPlanetMapper.findPlanetByUserId(id);
        if(planets==null) return new ArrayList<>();
        Map<Planet,Long> planetAndTime=new HashMap<>();
        for(Planet p:planets){
            List<StudyRecord> records=studyRecordMapper.findAllByUserIdAndPlanetIdAndTimeBetween(id,p.getId(),startTime,stopTime);
            if(records.size()==0) continue;
            if(records.get(records.size()-1).getOperation()==0) records.remove(records.size()-1);
            long total=0;
            for(int j=0;j<records.size();j+=2){
                long time=(records.get(j+1).getTime().getTime()-records.get(j).getTime().getTime()) / (long)(1000*60);
                total+=time;
            }
            planetAndTime.put(p,total);
        }
        List<Map.Entry<Planet,Long>> rank = new ArrayList<>(planetAndTime.entrySet());
        Collections.sort(rank,new Comparator<Map.Entry<Planet,Long>>() {
            //按键值降序排序
            public int compare(Map.Entry<Planet,Long> o1,
                               Map.Entry<Planet,Long> o2) {
                return -(o1.getValue().compareTo(o2.getValue()));
            }
        });
        List<Planet> result=new ArrayList<>();
        for(Map.Entry<Planet,Long> r:rank)
            result.add(r.getKey());
        if(result.size()<3) return result;
        return result.subList(0,3);
    }

    /**
     * 获取当周开始时间戳
     * @param timeStamp 毫秒级时间戳
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
     * @param timeStamp 毫秒级时间戳
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
}
