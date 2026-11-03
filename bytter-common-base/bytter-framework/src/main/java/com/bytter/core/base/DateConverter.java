package com.bytter.core.base;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
/**
 * 前端日期字符串统一转为日期类型 目前支持yyyy-MM-dd 与yyyy-MM-dd HH:mm:ss
 * 其他暂不支持
 */
public class DateConverter implements Converter<String, Date> {
    private final static Pattern DATE_FMT_REG = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");
    private final static Pattern DATE_TIME_REG = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    private final static Pattern DATE_TIME_M_REG = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$");
    @Override
    public Date convert(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        if(date.contains("中国标准时间")){
            SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            SimpleDateFormat format1=new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
            String formattemp=date.replace("GMT","").replace("\\(.*\\)","");
            try {
                date=format.format(format1.parse(formattemp));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        date = date.trim();
        if (DATE_TIME_M_REG.matcher(date).matches()) {
            return DateUtil.formatDateYYYY_MM_DD_T(date.split("\\.")[0]);
        } else if (DATE_TIME_REG.matcher(date).matches()) {
            return DateUtil.formatDateYYYY_MM_DD_T(date);
        } else if (DATE_FMT_REG.matcher(date).matches()) {
            return DateUtil.formatDateYYYY_MM_DD(date);
        } else {
            AssertHelper.isTrue(false, CommonMessageEnum.COMMON_DATE_FORMAT_MESSAGE_ERROR);
        }
        return null;
    }
}
