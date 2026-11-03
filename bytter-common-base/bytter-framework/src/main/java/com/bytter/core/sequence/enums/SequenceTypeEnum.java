package com.bytter.core.sequence.enums;
/**
 * 流水号类型枚举类
 * 代码如下
 * 1、普通流水号
 * 2、
 * 3、银行流水号
 *
 * @author Covenanter
 * @date 17/5/27
 */
public enum SequenceTypeEnum {
    /**
     * 一般的流水号，可设置带前缀，不带前缀；带日期，不带日期
     */
    NORMAL,
    /**
     * 特殊代码放在最前面
     */
    SPECIAL_PREFIX,
    /**
     * 什么都没有的流水类型
     */
    BANK;
}
