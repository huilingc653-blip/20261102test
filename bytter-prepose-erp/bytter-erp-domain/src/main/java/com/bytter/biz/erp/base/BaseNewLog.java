package com.bytter.biz.erp.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <br>
 * <b>功能：</b>统一设置日志类。。不需要买个接口创建日志类只需New一个新的这个对象设置logName即可<br>
 * <b>更新者：</b><br>
 * <b>日期：</b> <br>
 * <b>更新内容：</b><br>
 */
public class BaseNewLog implements IBaseNewLog {

	private Logger logger = null;

	public BaseNewLog(String logName) {
		logger = LoggerFactory.getLogger(logName);
	}

	public void debug(String msg) {
		logger.debug(msg);
	}

	public void info(String msg) {
		logger.info(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}

	public void error(String msg,Exception e) {
		logger.error(msg);
		printStackTrace(e);
	}

	public synchronized void printStackTrace(Exception e) {
		// logger.error("错误信息：" + e.getMessage());
		//
		// StackTraceElement[] trace = e.getStackTrace();
		// for (int i = 0; i < trace.length; i++)
		// logger.error("\tat " + trace[i]);

		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);// 将出错的栈信息输出到printWriter中
			pw.flush();
			sw.flush();
			logger.error("错误信息：" + sw.toString());
		} catch (Exception ex) {
			logger.error("printStackTrace()转换错误");
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
	}
}

