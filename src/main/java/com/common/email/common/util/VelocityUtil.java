package com.common.email.common.util;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.generic.DateTool;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :11:07:07
 */
public class VelocityUtil {

    private final static String DEFAUL_ENCODEING = "UTF-8";

    public static String getTemplateResult(String name, String encoding, List<String> list) throws Exception {
        if (name == null || "".equals(name)) {
            return "";
        }
        if (encoding == null || "".equals(encoding)) {
            encoding = DEFAUL_ENCODEING;
        }
        Properties properties = new Properties();
        properties.put("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine engine = new VelocityEngine();
        engine.init(properties);
        Template template = engine.getTemplate(name, encoding);
        Context context = new VelocityContext();
        context.put("date", new DateTool());
        context.put("list", list);
        Writer writer = new StringWriter(2048);
        template.merge(context, writer);
        return writer.toString();
    }

}
