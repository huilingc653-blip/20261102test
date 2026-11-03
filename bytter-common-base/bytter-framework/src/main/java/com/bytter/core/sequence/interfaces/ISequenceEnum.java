package com.bytter.core.sequence.interfaces;
import com.bytter.core.sequence.entity.MultiTable;
import com.bytter.core.sequence.enums.SequenceDateEnum;
import com.bytter.core.sequence.enums.SequenceTypeEnum;
/**
 * <br>
 * <b>功能：</b> 流水号生成枚举类接口，可用于扩展  <br>
 * <b>作者：</b> chenlw <br>
 * <b>日期：</b> 2021-01-18  <br>
 */
public interface ISequenceEnum {
    /**
     * 获取 流水枚举类型
     *
     * @return 流水枚举类型
     */
    SequenceTypeEnum getSequenceTypeEnum();
    /**
     * 获取枚举名称。
     *
     * @return 枚举名称。
     */
    String getEnumName();
    /**
     * 获取 流水前缀
     *
     * @return 流水前缀
     */
    String getPrefix();
    /**
     * 获取 流水号长度
     *
     * @return 流水号长度
     */
    Integer getSequenceLength();
    /**
     * 获取  业务表名称
     *
     * @return 业务表名称
     */
    String getTableName();
    /**
     * 获取 业务字段名称
     *
     * @return 业务字段名称
     */
    String getFieldName();
    /**
     * 获取 流水日期类型
     *
     * @return 流水日期类型
     */
    SequenceDateEnum getDateEnum();
    /**
     * 设置 流水日期类型
     *
     * @param dateEnum 流水日期类型
     */
    void setDateEnum(SequenceDateEnum dateEnum);
    /**
     * 需要根据多个数据库表查询最大值
     *
     * @return 多个表信息
     */
    MultiTable[] getMultiTables();
    /**
     * 需要根据多个数据库表查询最大值
     *
     * @param multiTables 多个表信息
     */
    void setMultiTables(MultiTable[] multiTables);
    /**
     * 是否为连续流水号
     *
     * @return 是否为连续流水号
     */
    boolean isContinueSequence();
}
