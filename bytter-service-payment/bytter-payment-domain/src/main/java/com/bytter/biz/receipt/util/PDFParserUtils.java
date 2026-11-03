package com.bytter.biz.receipt.util;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pzc  2025/08/28
 * @since 1.0.0
 */
public class PDFParserUtils {


    public static String formatBankAccount(String bankAcc) {

        // 使用substring进行字符串截取并拼接
        return bankAcc.substring(0, 3) + "-" +
                bankAcc.substring(3, 9) + "-" +
                bankAcc.substring(9);
    }

    public static void main(String[] args) {
        String bankAcc = "539004077011";
        if (bankAcc.length() != 12) {
            throw new IllegalArgumentException("银行账号必须是12位数字");
        }
        System.out.println(formatBankAccount(bankAcc));
    }

    public static String extractBetweenMarkers(String text,String marker) {
        // 构建动态正则表达式，考虑中文冒号
        String[] markerArr=marker.split("\\{\\}");

        String regex = Pattern.quote(markerArr[0]) + "(.*?)" + Pattern.quote(markerArr[1]);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if(matcher.find()) {
            return matcher.group(1).trim();
        }
        return "未找到日期信息";
    }

    public static String readPdfFileText(String fileName) throws IOException {
        FileInputStream fileInputStream =null;
        PDDocument pdDocument =null;
      try{
          // 创建输入流对象
          fileInputStream = new FileInputStream(fileName);
          // 创建解析器对象
          PDFParser pdfParser = new PDFParser(new RandomAccessBuffer(fileInputStream));
          pdfParser.parse();
          // pdf文档
          pdDocument = pdfParser.getPDDocument();
          // pdf文本操作对象,使用该对象可以获取所读取pdf的一些信息
          PDFTextStripper pdfTextStripper = new PDFTextStripper();
          String content = pdfTextStripper.getText(pdDocument);
          // PDDocument对象时使用完后必须要关闭
          return content;
      }finally {
          if (fileInputStream!=null){
              fileInputStream.close();
          }
          if (pdDocument!=null){
              pdDocument.close();
          }
      }
    }

    public static String readPdfFileText(byte[] fileByte) throws IOException {
        ByteArrayInputStream fileInputStream = null;
        PDDocument pdDocument =null;
        try{
            fileInputStream=new ByteArrayInputStream(fileByte);
            // 创建解析器对象
            PDFParser pdfParser = new PDFParser(new RandomAccessBuffer(fileInputStream));
            pdfParser.parse();
            // pdf文档
            pdDocument = pdfParser.getPDDocument();
            // pdf文本操作对象,使用该对象可以获取所读取pdf的一些信息
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String content = pdfTextStripper.getText(pdDocument);
            // PDDocument对象时使用完后必须要关闭
            return content;
        }finally {
            if (fileInputStream!=null){
                fileInputStream.close();
            }
            if (pdDocument!=null){
                pdDocument.close();
            }
        }
    }
}



