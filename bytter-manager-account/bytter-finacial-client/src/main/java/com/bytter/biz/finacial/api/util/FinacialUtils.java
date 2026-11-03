package com.bytter.biz.finacial.api.util;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import java.io.Serializable;
/**
 * 核算模块工具类
 * @author liyh
 * @since 2017-6-21
 */
public class FinacialUtils implements Serializable {
    /**
     * 功能介绍 ：产生全局业务ID。
     */
    public static String createGlobalId() {
        return DateUtils.date2Str(DateUtils.yyyymmddhhmmss()) + StringUtils.getUUId();
    }
}
