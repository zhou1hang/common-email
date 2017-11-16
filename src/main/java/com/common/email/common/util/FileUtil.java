package com.common.email.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:22:22
 */
public class FileUtil {


    private static String path = "./att/";


    public static String save(String name, InputStream is, boolean closeSrc) throws IOException {
        String realName = UUIDUtil.randomChar(16) + "_" + name;
        File file = new File(path, realName);
        File pFile = file.getParentFile();
        if (!pFile.exists()) {
            pFile.mkdirs();
        }
        Files.copy(is, file.toPath());
        if (closeSrc) {
            is.close();
        }
        return realName;
    }

    public static File getFile(String name) {
        return new File(path, name);
    }


}
