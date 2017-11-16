package com.common.email.download;

import com.common.email.bean.EmailContacts;
import com.common.email.bean.EmailMessage;
import com.common.email.bean.HtmlMail;
import com.common.email.bean.MailAttachment;
import com.common.email.common.util.EmailParsingUtil;
import com.common.email.common.util.FileUtil;
import com.common.email.common.util.PdfUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.*;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :11:35:35
 */
public class EmailSync {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSync.class);

    /**
     * 同步邮件
     *
     * @param username 用户名
     * @param pwd      密码
     * @param host     host
     * @param protocol 协议
     * @param is_ssl   是否加密
     * @param port   端口
     * @throws Throwable
     */
    public void syncMail(String username, String pwd, String host, String protocol,int port, boolean is_ssl) throws Throwable {
        Properties props = System.getProperties();
        if (is_ssl) {
            //ssl方式发送邮件
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail."+protocol+".ssl.enable", "true");
            props.put("mail."+protocol+".ssl.socketFactory", sf);
        }
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail.mime.address.strict", "false");
        props.setProperty("mail." + protocol + ".port", port+"");
        props.setProperty("mail.mime.multipart.ignoreexistingboundaryparameter", "true");

        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore(protocol);
        store.connect(host, username, pwd);
        Folder folder = store.getFolder("INBOX");

        if ("imap".equals(protocol)) {
            imap(folder, username);
        } else if ("pop3".equals(protocol)) {
            pop3(folder, username);
        }
    }


    private void imap(Folder folder, String account) throws MessagingException {
        folder.open(Folder.READ_WRITE);
        int messageNo = folder.getMessageCount();
        int unreadMessageCount = folder.getUnreadMessageCount();
        LOG.info("{} has:{} 总数:{}未读", account, messageNo, unreadMessageCount);
        for (int i = 0; i < unreadMessageCount; i++) {
            try {
                isOpen(folder);
                Message message = folder.getMessage(messageNo--);
                String messageUid = ((MimeMessage) message).getMessageID();
                if (messageUid == null) {//垃圾邮件messageiId就会为空
                    LOG.info("messageUid is null:Subject:{}", message.getSubject());
                    continue;
                }
                HtmlMail htmlMail = download(message);
                EmailMessage emailMessage = merge(htmlMail, message, account);
                System.out.println(emailMessage);
                message.setFlag(Flags.Flag.SEEN, true);//设置已读
            } catch (Exception e) {
                LOG.error("process mail err", e);
            }
        }
    }

    private void pop3(Folder folder, String account) throws MessagingException {
        folder.open(Folder.READ_WRITE);
        int messageNo = folder.getMessageCount();
        LOG.info("{} has:{} 总数", account, messageNo);
        while (messageNo > 0) {
            try {
                isOpen(folder);
                Message message = folder.getMessage(messageNo--);
                String messageUid = ((MimeMessage) message).getMessageID();
                if (messageUid == null) {//垃圾邮件messageiId就会为空
                    LOG.info("messageUid is null:Subject:{}", message.getSubject());
                    continue;
                }
                HtmlMail htmlMail = download(message);
                EmailMessage emailMessage = merge(htmlMail, message, account);
                System.out.println(emailMessage);
            } catch (Exception e) {
                LOG.error("process mail err", e);
            }
            //TODO 测试需要下载一封
            break;
        }
    }

    /**
     * 封装下载邮件对象
     *
     * @param message
     * @return
     */
    public HtmlMail download(Message message) {
        Map<String, String> imagePath = new HashMap<>();
        HtmlMail mail = new HtmlMail();
        mail.setType(HtmlMail.MailType.IN);
        try {
            String subject = message.getSubject();
            subject = subject == null ? "" : subject;
            StringBuilder content = new StringBuilder();

            List<MailAttachment> atts = new ArrayList<>();
            saveContent(content, mail, message.getContent(), atts, imagePath);
            String text = EmailParsingUtil.appendImagePath(content, imagePath);

            mail.setAtts(atts);
            mail.setSubject(subject);
            mail.setState(HtmlMail.State.UNREAD);
            mail.setContent(text);

            List<EmailContacts> sender = EmailParsingUtil.convertAddres("from", mail.getId(), message.getFrom());
            List<EmailContacts> cc = EmailParsingUtil.toStringAddress(message, Message.RecipientType.CC, mail.getId());
            List<EmailContacts> tos = EmailParsingUtil.toStringAddress(message, Message.RecipientType.TO, mail.getId());
            List<EmailContacts> bcc = EmailParsingUtil.toStringAddress(message, Message.RecipientType.BCC, mail.getId());

            mail.setCc(cc);
            mail.setBcc(bcc);
            mail.setReceiver(tos);
            mail.setSender(sender);
            mail.setTime(message.getSentDate());
        } catch (Exception e) {
            LOG.error("process mail err", e);
            throw new RuntimeException(e);
        }
        return mail;
    }

    /**
     * 下载邮件内容和附件
     *
     * @param sb
     * @param mail
     * @param content
     * @param atts
     * @param imagePath
     * @throws Exception
     */
    public void saveContent(StringBuilder sb, HtmlMail mail, Object content,
                            List<MailAttachment> atts, Map<String, String> imagePath) throws Exception {
        if (content instanceof Multipart) {
            Multipart multiPart = (Multipart) content;
            int attCount = multiPart.getCount();
            for (int i = 0; i < attCount; i++) {

                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);

                String type = part.getContentType().split(";")[0].toLowerCase();
                String disposition = part.getDisposition();

                Object mcontent = part.getContent();
                if (part.isMimeType("multipart/alternative")) {
                    processAlternative(sb, mail, mcontent, atts, imagePath);
                } else if (Part.ATTACHMENT.equalsIgnoreCase(disposition) || type.contains("application/")) {
                    atts.add(processAtt(mail.getId(), part));
                } else if (part.isMimeType("text/*")) {
                    sb.append(mcontent);
                } else if (mcontent instanceof Multipart) {
                    saveContent(sb, mail, mcontent, atts, imagePath);
                } else if (type.contains("image/")) {
                    DataInputStream in = new DataInputStream(part.getInputStream());
                    String fileName = part.getFileName();
                    if (fileName == null || "".equals(fileName)) {
                        continue;
                    }
                    fileName = MimeUtility.decodeText(fileName);
                    String saveFileName = FileUtil.save(fileName, in, true);
                    saveFileName = "{imagepath}/" + saveFileName;
                    imagePath.put(fileName, saveFileName);
                } else {
                    sb.append(mcontent);
                }
            }
        } else {
            sb.append(content);
        }
    }


    /**
     * 合并
     *
     * @param htmlMail
     * @param message
     * @param account
     * @return
     * @throws MessagingException
     */
    public EmailMessage merge(HtmlMail htmlMail, Message message, String account) throws MessagingException {
        EmailMessage msg = new EmailMessage();
        MimeMessage mimeMessage = ((MimeMessage) message);

        msg.setMailUuid(htmlMail.getId());
        msg.setMsgNum(message.getMessageNumber());
        msg.setMsgSid(mimeMessage.getMessageID());
        msg.setMsgUid(mimeMessage.getMessageID());
        msg.setSubject(htmlMail.getSubject());
        Date current = new Date();
        msg.setReceivedTime(message.getReceivedDate() == null ? current : message.getReceivedDate());
        msg.setSentTime(message.getSentDate());
        msg.setStatus(HtmlMail.State.UNREAD);
        msg.setCreateTime(current);

        msg.setHtmlContent(htmlMail.getContent());
        Document document = Jsoup.parse(htmlMail.getContent());
        msg.setPlainContent(document.text());

        String pdfContent = getPdfContent(htmlMail.getAtts());
        msg.setPdfContent(pdfContent);
        msg.setFrom(analyzeMail(htmlMail.getSender()));
        msg.setTo(analyzeMail(htmlMail.getReceiver()));
        msg.setCc(analyzeMail(htmlMail.getCc()));
        msg.setBcc(analyzeMail(htmlMail.getBcc()));
        msg.setAccount(account);
        return msg;
    }

    private String analyzeMail(List<EmailContacts> list) {
        StringBuffer sb = new StringBuffer();
        int i = 1;
        for (EmailContacts contacts : list) {
            sb.append(contacts.getMail());
            if (i != list.size()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    private String getPdfContent(List<MailAttachment> attachmentList) {
        StringBuilder sb = new StringBuilder();
        if (attachmentList == null || attachmentList.size() <= 0) {
            return sb.toString();
        }
        for (MailAttachment attachment : attachmentList) {
            String name = attachment.getOriginalName();
            if (!name.endsWith(".pdf") && !name.endsWith(".PDF")) {
                continue;
            }
            try {
                File file = FileUtil.getFile(attachment.getPath());
                sb.append(PdfUtil.getPDFContent(new FileInputStream(file)));
            } catch (IOException e) {
                LOG.error(String.format("pdf-%s解析出错", name), e);
            } catch (Exception e) {
                LOG.error(String.format("pdf-%s解析出错", name), e);
            }
        }
        return sb.toString();
    }


    private void processAlternative(StringBuilder sb, HtmlMail mail, Object content,
                                    List<MailAttachment> atts, Map<String, String> imagePath) throws Exception {
        Multipart part = (Multipart) content;
        int cc = part.getCount();
        for (int i = 0; i < cc; i++) {
            BodyPart body = part.getBodyPart(i);
            if (body.isMimeType("text/plain")) {
                sb.append(body.getContent().toString().trim());
            } else if (body.isMimeType("text/html")) {
                sb.append(body.getContent());
            } else {
                saveContent(sb, mail, content, atts, imagePath);
            }
        }
    }

    private MailAttachment processAtt(long uuid, MimeBodyPart part) {
        try {
            String fileName = part.getFileName();
            if (fileName == null || "".equals(fileName)) {
                return null;
            }
            fileName = MimeUtility.decodeText(fileName);
            InputStream is = part.getInputStream();
            String save = FileUtil.save(fileName, is, true);
            MailAttachment att = new MailAttachment();
            att.setOriginalName(fileName);
            att.setPath(save);
            att.setFileType(part.getContentType());
            att.setMailId(uuid);
            return att;
        } catch (Exception e) {
            LOG.error("process mail err", e);
            throw new RuntimeException(e);
        }
    }

    private void isOpen(Folder folder) throws MessagingException {
        if (!folder.isOpen()) {//防止Folder is not Open
            folder.open(Folder.READ_WRITE);
        }
    }

}
