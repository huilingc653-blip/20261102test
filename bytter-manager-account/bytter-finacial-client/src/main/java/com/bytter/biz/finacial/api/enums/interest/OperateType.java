package com.bytter.biz.finacial.api.enums.interest;
/**
 *利息操作类型枚举
 * @author caozx
 * @since 2017-7-5
 */
public enum OperateType {
	/**
     * 计息
     */
    INTEREST,
    /**
     * 删除
     */
    DELETE,
    /**
     * 提交
     */
    SUBMIT,
    /**
     * 复核
     */
    CHECK,
    /**
     * 打回
     */
    BACK,
    /**
     * 新增
     */
    ADD,
    /**
     * 测算
     */
    CALCULATE,
    /**
     * 冲预提
     */
    RUSH,
    /**
     * 个别计息
     */
    PART_INTEREST,
    /**
     * 汇总
     */
    SUM
}
