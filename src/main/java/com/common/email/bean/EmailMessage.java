package com.common.email.bean;

import java.util.Date;
import java.util.List;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:04:04
 */
public class EmailMessage {
    /**邮件唯一id*/
    private Long mailUuid;
    /**邮箱*/
    private String account;
    /**主键ID*/
    private int mailId;
    /**邮件UID(唯一)*/
    private String msgUid;
    /**邮件ID(唯一)*/
    private String msgSid;
    /**邮件的序列号(对邮件删除时会变)*/
    private int msgNum;
    /**邮件标题*/
    private String subject;
    /**发件人*/
    private String from;
    /**收件人*/
    private String to;
    /**抄送人*/
    private String cc;
    /**密送人*/
    private String bcc;
    /**邮件内容*/
    private String plainContent;
    /**邮件内容*/
    private String htmlContent;
    /**解析附件的pdf的内容*/
    private String pdfContent;
    /**收件时间*/
    private Date receivedTime;
    /**发件时间*/
    private Date sentTime;
    /**邮件状态: 0-未读 1-已阅 2-已转发 3-已回复 4-已删除*/
    private int status;
    /**创建时间*/
    private Date createTime;

    private List<MailAttachment> attachments;

    public List<MailAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MailAttachment> attachments) {
        this.attachments = attachments;
    }

    public Long getMailUuid() {
        return mailUuid;
    }

    public void setMailUuid(Long mailUuid) {
        this.mailUuid = mailUuid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getMailId() {
        return mailId;
    }

    public void setMailId(int mailId) {
        this.mailId = mailId;
    }

    public String getMsgUid() {
        return msgUid;
    }

    public void setMsgUid(String msgUid) {
        this.msgUid = msgUid;
    }

    public String getMsgSid() {
        return msgSid;
    }

    public void setMsgSid(String msgSid) {
        this.msgSid = msgSid;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getPlainContent() {
        return plainContent;
    }

    public void setPlainContent(String plainContent) {
        this.plainContent = plainContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getPdfContent() {
        return pdfContent;
    }

    public void setPdfContent(String pdfContent) {
        this.pdfContent = pdfContent;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
