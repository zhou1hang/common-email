package com.common.email.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:00:00
 */
public class HtmlMail {

    public static interface State {
        int UNREAD = 0;//未读
        int READ = 1;//已读
    }

    /**
     * 邮件类型
     */
    public static interface MailType {
        int OUT = 0; //  发送
        int IN = 1;// 接收
    }

    protected long id;
    /**
     * 时间
     */
    protected Date time;

    /*** 邮件类型 */
    protected int type = MailType.OUT;

    /**
     * 操作状态:0未读 1 已读
     */
    protected int state = State.READ;

    /**
     * 主题
     */
    protected String subject;

    /**
     * 邮件内容
     */
    protected String content;

    /**
     * 发送人
     */
    protected List<EmailContacts> sender = new ArrayList<>();

    /**
     * 接受列表
     */
    protected List<EmailContacts> receiver = new ArrayList<>();

    /**
     * 抄送列表
     */
    protected List<EmailContacts> cc = new ArrayList<>();

    /**
     * 密送列表
     */
    protected List<EmailContacts> bcc = new ArrayList<>();

    /**
     * 邮件附件
     */
    protected List<MailAttachment> atts = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<EmailContacts> getSender() {
        return sender;
    }

    public void setSender(List<EmailContacts> sender) {
        this.sender = sender;
    }

    public List<EmailContacts> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<EmailContacts> receiver) {
        this.receiver = receiver;
    }

    public List<EmailContacts> getCc() {
        return cc;
    }

    public void setCc(List<EmailContacts> cc) {
        this.cc = cc;
    }

    public List<EmailContacts> getBcc() {
        return bcc;
    }

    public void setBcc(List<EmailContacts> bcc) {
        this.bcc = bcc;
    }

    public List<MailAttachment> getAtts() {
        return atts;
    }

    public void setAtts(List<MailAttachment> atts) {
        this.atts = atts;
    }
}
