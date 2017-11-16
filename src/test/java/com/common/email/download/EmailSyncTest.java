package com.common.email.download;

import com.common.email.constant.CommonEmailConstant;
import org.junit.Test;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:31:31
 */
public class EmailSyncTest {
    @Test
    public void syncMail_imap() throws Exception {
        //imap方式
        //下载所有未读邮件 并设置成已读
        try {
            //ssl
            new EmailSync().syncMail("邮箱地址", "密码", CommonEmailConstant.qq_imap_host, "imap", 993, true);
            //no sll
            new EmailSync().syncMail("邮箱地址", "密码", CommonEmailConstant.qq_imap_host, "imap", 143, false);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void pop3() throws Exception {
        //pop3方式，在测试过程中不能使用110端口，只能使用ssl端口
        //下载邮箱内所有邮件
        try {
            new EmailSync().syncMail("邮箱地址", "密码", CommonEmailConstant.qq_pop_host, "pop3", 995, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}