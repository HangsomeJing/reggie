package com.itheima.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.User;

public interface UserService extends IService<User> {
    /**
     * 发送邮箱
     * @param to 目标对象
     * @param subject 主题
     * @param context 内容
     */
    void sendMsg(String to,String subject,String context);
}
