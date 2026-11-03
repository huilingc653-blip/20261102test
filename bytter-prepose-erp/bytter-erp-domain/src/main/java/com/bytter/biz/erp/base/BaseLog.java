package com.bytter.biz.erp.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseLog {
    private static Logger logger = LoggerFactory.getLogger(BaseLog.class);
    public static void debug(Object msg) {
        debug(msg.toString());
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public synchronized static void printStackTrace(Exception e) {
        logger.info("错误信息：" + e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++)
            logger.info("\tat " + trace[i]);
    }
}
