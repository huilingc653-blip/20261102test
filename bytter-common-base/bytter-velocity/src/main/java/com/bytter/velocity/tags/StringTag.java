package com.bytter.velocity.tags;

import com.bytter.core.utils.StringUtils;
import org.apache.velocity.tools.config.DefaultKey;
/**
 * Created by zhoul on 2017/6/14.
 */
@DefaultKey("string")
public class StringTag extends StringUtils {
    /**
     * 将字符串转为小写，主要提供Velocity使用
     * 
     * @param str
     * @return
     */
    public static String toLowerCase(String str) {
        return str.toLowerCase();
    }
    /**
     * 将字符串转为大写，主要提供Velocity使用
     * 
     * @param str
     * @return
     */
    public static String toUpperCase(String str) {
        return str.toUpperCase();
    }
}
