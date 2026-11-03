package com.bytter.credit.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.handler.ExcelModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行授信年度表
 *
 * @author loveCamille
 */
@Getter
@Setter
public class YearBankCreditPlanVO extends ExcelModel implements Serializable, IBusinessKey {

    private String id;

    /**
     * 单位id
     */
    @BtCompositeSearch(fieldName = "t1.corp_id")
    private String corpId;
    /**
     * 单位名
     */
    private String corpName;

    /**
     * 年度
     */
    @BtCompositeSearch(fieldName = "t1.year")
    private Integer year;

    /**
     * 银行id
     */
    @BtCompositeSearch(fieldName = "t1.bank_id")
    private String bankId;
    /**
     * 联行号
     */
    @BtCompositeSearch(fieldName = "t1.bank_code")
    private String bankCode;
    /**
     * 银行名
     */
    private String bankName;

    /**
     * 授信类型
     */
    @BtCompositeSearch(fieldName = "t1.credit_type")
    private String creditType;

    /**
     * 币别
     */
    private String curId;
    private String curName;


    /**
     * 计划授信金额
     */
    private BigDecimal creditAmount;

    /**
     * 是否正常使用
     */
    private Integer status;

    @Override
    public String getBusinessKey() {
        return getId();
    }

}
