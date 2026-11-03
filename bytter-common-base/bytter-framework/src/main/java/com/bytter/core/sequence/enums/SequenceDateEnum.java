package com.bytter.core.sequence.enums;
import java.text.SimpleDateFormat;
/**
 * @author zhoul
 * @date 2017/6/6
 */
public enum SequenceDateEnum {
    /**
     * 生成流水中的日期为20190101
     */
    yyyyMMdd(new SimpleDateFormat("yyyyMMdd")),
    /**
     * 生成流水中的日期为201901
     */
    yyyyMM(new SimpleDateFormat("yyyyMM")),
    /**
     * 生成流水中的日期为190101
     */
    yyMMdd(new SimpleDateFormat("yyMMdd")),
    /**
     * 生成流水中的日期为201901
     */
    yyMM(new SimpleDateFormat("yyMM"));
    private SimpleDateFormat simpleDateFormat;
    SequenceDateEnum(SimpleDateFormat date) {
        this.simpleDateFormat = date;
    }
    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
