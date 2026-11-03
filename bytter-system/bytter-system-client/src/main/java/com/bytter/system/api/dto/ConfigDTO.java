package com.bytter.system.api.dto;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * Created by zhoul on 2017/5/23.
 */
public class ConfigDTO implements Serializable {
    private String value;
    public ConfigDTO(String value) {
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        this.value = value.trim();
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return getValue();
    }
    /**
     * 获取去掉[]的字符串
     *
     * @return
     */
    public String toFormatString() {
        return value = value.replace("[", "").replace("]", "");
    }
    public Integer toInteger() {
        return toInteger(0);
    }
    public Integer toInteger(Integer defaultValue) {
        if (StringUtils.isBlank(getValue())) {
            return defaultValue;
        }
        return Integer.parseInt(getValue());
    }
    public Long toLong() {
        return toLong(0L);
    }
    public Long toLong(Long defaultValue) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Long.parseLong(getValue());
    }
    public Boolean toBoolean() {
        return toBoolean(null);
    }
    public Boolean toBoolean(Boolean defaultValue) {
        if (StringUtils.isBlank(getValue())) {
            return defaultValue;
        }
        if ("1".equals(value) || "true".equals(value)) {
            return Boolean.TRUE;
        } else if ("0".equals(value) || "false".equals(value)) {
            return Boolean.FALSE;
        }
        return false;
    }
    public Date toDate() {
        return toDate(null);
    }
    public Date toDate(Date defaultValue) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Date toDateTime() {
        return toDateTime(null);
    }
    public Date toDateTime(Date defaultValue) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(value.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String> toList() {
        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.replace("[", "").replace("]", "");
            return Arrays.asList(value.split(CommonConstant.COMMA_SPLIT));
        }
        return new ArrayList<String>(0);
    }
}
