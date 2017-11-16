package com.common.email.bean;

import javax.mail.Message;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:01:01
 */
public class EmailContacts {

    private Integer id;

    /***
     * 关联邮件的ID
     */
    private Long mailId;

    /**
     * 联系人
     */
    private String name;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 类型 {@link Message.RecipientType}
     */
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMailId() {
        return mailId;
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
