package com.common.email.common.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:common-email
 * @Author: old
 * @CreateTime:2017-11-16 :14:19:19
 */
public class PdfUtil {

    public static String getPdfFileText(String fileName) throws IOException {
        PdfReader reader = new PdfReader(fileName);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        StringBuilder content = new StringBuilder(2048);
        TextExtractionStrategy strategy;
        for (int page = 0; page <= reader.getNumberOfPages(); page++) {
            strategy = parser.processContent(page, new SimpleTextExtractionStrategy());
            content.append(strategy.getResultantText());
        }
        return content.toString();
    }

    public static String getPdfFileText(InputStream inputStream) throws IOException {
        PdfReader reader = new PdfReader(inputStream);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        StringBuilder content = new StringBuilder(2048);
        TextExtractionStrategy strategy;
        for (int page = 0; page <= reader.getNumberOfPages(); page++) {
            strategy = parser.processContent(page, new SimpleTextExtractionStrategy());
            content.append(strategy.getResultantText());
        }
        return content.toString();
    }

    public static String getPDFContent(InputStream inputStream) throws Exception {
        PdfReader reader = new PdfReader(inputStream);
        int pageCount = reader.getNumberOfPages();// 获得页数
        // 存放读取出的文档内容
        StringBuilder content = new StringBuilder(2048);
        for (int pageIndex =0; pageIndex <= pageCount; pageIndex++) {
            // 读取第i页的文档内容
            content.append(PdfTextExtractor.getTextFromPage(reader, pageIndex));
        }
        return content.toString();
    }
}
