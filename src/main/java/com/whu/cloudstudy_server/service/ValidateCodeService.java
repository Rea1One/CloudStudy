package com.whu.cloudstudy_server.service;

import com.whu.cloudstudy_server.util.EmailUtil;
import com.whu.cloudstudy_server.entity.User;
import com.whu.cloudstudy_server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Author: 郭瑞景
 * Date: 2020-02-24
 */

@Service
public class ValidateCodeService {
    @Autowired(required = false)
    private UserMapper userMapper;
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
}
