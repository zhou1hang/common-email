package com.common.email.send;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @Description:直发邮件
 * @Author: old
 * @CreateTime:2017-11-16 :10:07:07
 */
public class DerictEmail {

    /**
     * 发送邮件
     * <p>采用ssl方式发送邮件</p>
     *
     * @param username 用户名
     * @param pwd      密码
     * @param host     host
     * @param port     端口
     * @param protocol 协议
     * @param is_ssl   是否加密
     * @param is_proxy 是否代理
     * @return
     */
    public static Object send(String username, String pwd, String host, int port, String protocol, String to, boolean is_ssl, boolean is_proxy) throws GeneralSecurityException, MessagingException, EmailException, UnsupportedEncodingException {

        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", host);
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", protocol);
        props.put("mail.smtp.starttls.enable", "true");
        if (is_ssl) {
            //ssl方式发送邮件
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        }

        if (is_proxy) {
            props.put("mail.smtp.socks.host", "设置代理");//代理host
            props.put("mail.smtp.socks.port", "设置端口");//代理端口
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        Session mailSession = Session.getDefaultInstance(props, null);
        Transport transport = mailSession.getTransport();
        transport.connect(host, port, username, pwd);

        HtmlEmail email = new HtmlEmail();
        email.setCharset("utf-8");//防止乱码
        email.setFrom(username);
        email.setHostName(host);
        email.addTo(to);
        email.setSubject("测试");
        email.setHtmlMsg("测试");
//         设置优先级(1:紧急   3:普通    5:低)
//            message.setHeader("X-Priority", "1");
//         要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
//            message.setHeader("Disposition-Notification-To", from);
//        Map<String, String> map = new HashMap<>();
//            map.put("X-Priority","2");
//            map.put("Disposition-Notification-To",from);
//        email.setHeaders(map);
        EmailAttachment att = new EmailAttachment();
        att.setName(MimeUtility.encodeText("图片.pdf", "GBK", "B"));//防止乱码 经量不要包含该特殊字符
        att.setPath("C:\\Users\\old\\Pictures\\1.png");
        email.attach(att);

        email.buildMimeMessage();
        Message m = email.getMimeMessage();
        m.saveChanges();
        transport.sendMessage(m, m.getAllRecipients());
        transport.close();
        return true;
    }


    /**
     *
     * @param username
     * @param pwd
     * @param host
     * @param port
     * @param protocol
     * @param to
     * @param content
     * @return
     * @throws GeneralSecurityException
     * @throws MessagingException
     * @throws EmailException
     * @throws UnsupportedEncodingException
     */
    public static Object send(String username, String pwd, String host, int port, String protocol, String to, String content) throws GeneralSecurityException, MessagingException, EmailException, UnsupportedEncodingException {

        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", host);
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", protocol);
        props.put("mail.smtp.starttls.enable", "true");


        Session mailSession = Session.getDefaultInstance(props, null);
        Transport transport = mailSession.getTransport();
        transport.connect(host, port, username, pwd);

        HtmlEmail email = new HtmlEmail();
        email.setCharset("utf-8");//防止乱码
        email.setFrom(username);
        email.setHostName(host);
        email.addTo(to);
        email.setSubject("测试");
        email.setHtmlMsg(content);

        email.buildMimeMessage();
        Message m = email.getMimeMessage();
        m.saveChanges();
        transport.sendMessage(m, m.getAllRecipients());
        transport.close();
        return true;
    }


}
