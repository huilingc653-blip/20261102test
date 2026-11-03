package com.bytter.core.utils;
import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * @author zhoul
 */
public class ExceptionStackUtils {
    public static String getStack(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
