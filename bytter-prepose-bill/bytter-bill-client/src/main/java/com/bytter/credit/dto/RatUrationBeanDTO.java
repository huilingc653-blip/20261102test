package com.bytter.credit.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * 授信占用DTO
 *
 * @author liyh
 */
@Data
public class RatUrationBeanDTO implements Serializable {
    private String id;
    /**
     * 占用编号
     */
    private String urationCode;
    /**
     * 录入单号
     */
    private String oriRatCode;
    /**
     * 系统授信品种
     */
    @BtCompositeSearch(fieldName = "ru.type")
    private String type;
    private String typeName;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "ru.corp_id")
    private String corpId;
    private String corpName;
    /**
     * 额度类型
     */
    private String useType;
    /**
     * 占用授信类型
     */
    private String ratType;
    /**
     * 授信额度编号
     */
    @BtCompositeSearch(fieldName = "rb.rat_code")
    private String ratCode;
    /**
     * 品种标识
     */
    private String itemFlag;
    /**
     * 品种标识
     */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    private String itemName;
    /**
     * 银行编号
     */
    @BtCompositeSearch(fieldName = "rb.bank_code")
    private String bankCode;
    private String bankName;
    private String bankType;
    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = "ru.cur_code")
//    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /**
     * 占用金额
     */
    private java.math.BigDecimal money;
    /**
     * 敞口金额
     */
    private java.math.BigDecimal openMoney;
    /**
     * 开始日期
     */
    @BtCompositeSearch(fieldName = "ru.begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date beginDate;
    /**
     * 结束日期
     */
    @BtCompositeSearch(fieldName = "ru.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 票据号
     */
    private String billNo;
    /**
     * 票据id
     */
    private String billId;
    /**
     * 单品种
     */
    private String singItemCode;
    private String singItemName;
    private String remark;
    /**
     * 结转后新的授信编号
     */
    private String newRatCode;
    /**
     * 被结转的授信编号
     */
    private String oldDisCode;
    /**
     * 结转后新的分配编号
     */
    private String newDisCode;
    /**
     * 授信模式
     **/
    private String ratMode;
    /**
     * 结转类型
     */
    private String changeType;
    private Integer itemOrder;
    /**
     * 业务品种
     */
    private String billName;
    /**
     * 额度类型 0-不循环 1-循环
     */
    private String creditType;
}
