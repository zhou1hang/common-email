package com.common.email.send;


import com.common.email.common.util.VelocityUtil;
import com.common.email.constant.CommonEmailConstant;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :10:29:29
 */
public class DerictEmailTest {

    @Test
    public void send_qq() throws Exception {
        //TODO 使用qq邮箱发送邮件必须使用ssl方式发送邮件，官方也有详细说明
        DerictEmail.send("发件人邮箱", "发件人密码",
                CommonEmailConstant.qq_smtp_host, 465, "smtp", "收件人邮箱", true, false);
    }


    @Test
    public void send_163() throws Exception {
        //163发送邮箱 就可以是非加密方式
        DerictEmail.send("发件人邮箱", "发件人密码",
                CommonEmailConstant.smtp_163_host, 25, "smtp", "收件人邮箱", false, false);
    }

    @Test
    public void gmail() throws Exception {
        //使用google邮箱账号发送邮件  就必须使用代理
        //google发送邮件必须使用ssl方式
        DerictEmail.send("发件人邮箱", "密码",
                CommonEmailConstant.smtp_gmail_host, 465, "smtp", "收件人邮箱", true, true);
    }

    @Test
    public void velocity() throws Exception {
        //Velocity渲染模板
        List<String> list = new ArrayList<>();
        list.add("测试1");
        list.add("测试2");
        String content = VelocityUtil.getTemplateResult("mould/list.vm", null, list);
        DerictEmail.send("发件人邮箱", "密码",
                CommonEmailConstant.smtp_163_host, 25, "smtp", "收件人邮箱", content);
    }
}