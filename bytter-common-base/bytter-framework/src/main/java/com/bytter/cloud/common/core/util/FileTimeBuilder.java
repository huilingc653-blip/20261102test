package com.bytter.cloud.common.core.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 文件时间输出对象
 *
 * @author limm 2022-06-27
 * @since 1.0.0
 */
public class FileTimeBuilder {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
    private static final Pattern TIMES_PATTERN = Pattern.compile("\\{times\\}");
    
    private StringBuilder logSb;
    
    private long loginTimes;
    
    private long startTime;
    
    private long endTime;
    
    private FileTimeBuilder(long loginTimes) {
        try {
            this.startTime = System.currentTimeMillis();
            this.loginTimes = loginTimes;
            this.logSb = new StringBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 构建对象
     *
     * @param loginTimes 等几次登录
     * @return 构建对象
     */
    public static FileTimeBuilder build(long loginTimes) {
        return new FileTimeBuilder(loginTimes);
    }
    
    /**
     * 添加日志
     *
     * @param message 消息格式化
     * @param objects 格式化消息对象列表
     * @return 返回构建对象
     */
    public FileTimeBuilder log(String message, Object... objects) {
        SimpleDateFormat sdf = this.getFormat();
        try {
            message = getTimes(message);
            logSb.append(
                    String.format(
                            Locale.CHINA, "%s %s",
                            sdf.format(new Date()),
                            String.format(Locale.CHINA, message, objects)
                    )
            );
            logSb.append("\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    
    private String getTimes(String message) {
        if (message == null || message.equalsIgnoreCase("")) {
            return message;
        }
        Matcher matcher = TIMES_PATTERN.matcher(message);
        if (matcher.find()) {
            message = matcher.replaceAll(String.valueOf(this.loginTimes));
        }
        return message;
    }
    
    private SimpleDateFormat getFormat() {
        return new SimpleDateFormat(DATE_FORMAT);
    }
    
    /**
     * 关闭操作
     *
     * @param fileName 文件名称
     * @param outTime 超出时间
     * @param message 消息格式化
     * @param objects 格式化消息对象列表
     * @return 返回日志信息
     */
    public String close(String fileName, Long outTime, String message, Object... objects) {
        this.log(message, objects);
        return this.close(fileName, outTime);
    }
    
    /**
     * 关闭操作
     *
     * @param fileName 文件名称
     * @param outTime 超出时间
     * @return 返回日志信息
     */
    public String close(String fileName, Long outTime) {
        this.endTime = System.currentTimeMillis();
        long useTime = this.endTime - this.startTime;
        this.printTime(useTime);
        this.logSb.append("\r\n").append("\r\n");
        String message = this.logSb.toString();
        if (outTime == null || useTime > outTime.longValue()) {
            if (fileName != null && !fileName.equalsIgnoreCase("")) {
                FileOutputStream outputStream = null;
                OutputStreamWriter outputStreamWriter = null;
                BufferedWriter bufferedWriter = null;
                try {
                    File file = new File(fileName);
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            if (!file.getParentFile().mkdirs()) {
                                return message;
                            }
                        }
                        if (!file.createNewFile()) {
                            return message;
                        }
                        outputStream = new FileOutputStream(file);
                    } else {
                        outputStream = new FileOutputStream(file, true);
                    }
                    outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(message);
                    bufferedWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStreamWriter != null) {
                        try {
                            outputStreamWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return message;
    }
    
    private void printTime(long time) {
        long temp = time;
        // 毫秒
        final int time1 = (int) temp % 1000;
        temp = temp / 1000;
        // 秒
        final int time2 = (int) temp % 60;
        temp = temp / 60;
        // 分钟
        final int time3 = (int) temp % 60;
        temp = temp / 60;
        // 小时
        final int time4 = (int) temp % 24;
        this.log("%s小时%s分钟%s秒%s毫秒", time4, time3, time2, time1);
    }
}
