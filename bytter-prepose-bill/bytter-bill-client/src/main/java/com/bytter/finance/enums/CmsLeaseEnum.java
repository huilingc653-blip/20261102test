package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
public enum CmsLeaseEnum implements IMessageEnum {
    /** 当前合同不可展期 */
    LEASE_EXTENDY_CHECK("8001"),
    /** 租赁物类别编码和租赁物名称不能重复! */
    CMS_BASEDATA_LEASETYPE_EXISTENCE("8002"),
    /** 此租赁合同有其他业务正在进行审批操作 */
    CMS_BASEDATA_CHECK("8003"),
    /** 租赁合同编号已存在 */
    LEASE_INFO_NO_CHECK("8004"),
    /** 此租赁物类别被其他业务引用，不可删除! */
    CMS_BASEDATA_LEASETYPE_ISTENCE("8005"),
    /** 租金计划不能为空 */
    CMS_LEASE_LEASECAPITALPLAN_EMPTY("8040"),
    /** 付租日期不能为空 */
    CMS_LEASE_LEASEPAY_PAYDATE_EMPTY("8041"),
    /** 付租日期不能小于起租日期 */
    CMS_LEASE_LEASEPAY_PAYDATE_beginDate("8042"),
    /** 付租日期不能大于到期日期 */
    CMS_LEASE_LEASEPAY_PAYDATE_endDate("8043"),
    /** 计划付租日期不能为空 */
    CMS_LEASE_LEASEPAY_PAYPLANDATE_EMPTY("8044"),
    /** 计划付租日期不能小于起租日期 */
    CMS_LEASE_LEASEPAY_PAYPLANDATE_beginDate("8045"),
    /** 计划付租日期不能大于于到期日期 */
    CMS_LEASE_LEASEPAY_PAYPLANDATE_endDate("8046"),
    /** 租赁租金金额不能为空 */
    CMS_LEASE_LEASEPAY_MONEYT_EMPTY("8047"),
    /** 租赁租金金额不能大于租金余额 */
    CMS_LEASE_LEASEPAY_MONEY_SENDBALANCE("8048"),
    /** 实付利息不能为空 */
    CMS_LEASE_LEASEPAY_INTEREST_EMPTY("8049"),
    /** 金额（总金额）不能为空 */
    CMS_LEASE_LEASEPAY_LEASEMONEY_EMPTY("8050"),
    /** 收款帐号不能为空 */
    CMS_LEASE_LEASEPAY_ONLINEPAYMENTACC_EMPTY("8051"),
    /** 收款账户名称不能为空 */
    CMS_LEASE_LEASEPAY_ONLINEACCNAME_EMPTY("8052"),
    /** 收款开户行名称不能为空 */
    CMS_LEASE_LEASEPAY_ONLINEBANKNAME_EMPTY("8053"),
    /** 用途不能为空 */
    CMS_LEASE_LEASEPAY_PURPOSE_EMPTY("8054");
    CmsLeaseEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
