package com.bmts.heating.monitor.dirver.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName: SimpleMailUtil
 * @Description: 邮件处理工具类
 * @Author: pxf
 * @Date: 2021/11/3 15:02
 * @Version: 1.0
 */
@Component
public class SimpleMailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMail(String subject, String[] toUsers, String text) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom("devel@tscc.com.cn");
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo(toUsers);
        // 设置邮件抄送人，可以有多个抄送人
        // message.setCc("12****32*qq.com");
        // 设置隐秘抄送人，可以有多个
        // message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(text);
        // 发送邮件
        javaMailSender.send(message);
    }
}
