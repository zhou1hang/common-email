package com.common.email.common.util;

import com.common.email.bean.EmailContacts;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.NewsAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:16:16
 */
public class EmailParsingUtil {

    public static List<EmailContacts> toStringAddress(Message message, Message.RecipientType type, long mailId)
            throws MessagingException {
        Address[] to = message.getRecipients(type);
        return convertAddres(type.toString(), mailId, to);
    }

    public static List<EmailContacts> convertAddres(String type, long mailId, Address[] to) {
        List<EmailContacts> recvs = new ArrayList<>();
        if (to == null) {
            return recvs;
        }

        for (Address address : to) {
            EmailContacts item = new EmailContacts();
            item.setMailId(mailId);
            item.setType(type);
            if (address instanceof InternetAddress) {
                InternetAddress addr = (InternetAddress) address;
                item.setMail(addr.getAddress());
                item.setName(addr.getPersonal());
            } else if (address instanceof NewsAddress) {
                NewsAddress addr = (NewsAddress) address;
                item.setName(addr.getNewsgroup());
                item.setMail(addr.getHost());
            }
            recvs.add(item);
        }
        return recvs;
    }

    public static String appendImagePath(StringBuilder sb, Map<String, String> imagePath) {
        String conetext = sb.toString();
        for (String key : imagePath.keySet()) {
            String fileName = key.substring(0, key.lastIndexOf("."));
            conetext = conetext.replace("cid:" + fileName, imagePath.get(key));
        }
        return conetext;
    }
}
