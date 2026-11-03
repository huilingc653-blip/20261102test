package com.bytter.credit.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class RatCreditCooperationBankVO extends ExcelModel implements Serializable, IBusinessKey {

    private String id;

    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户行名称
     */
    @Excel(name = "*授信银行名称")
    @BtCompositeSearch(fieldName = "rccb.BANK_NAME")
    private String bankName;
    /**
     * 授信银行合作情况：0-未合作、1-已合作、2-计划合作
     */
    @BtCompositeSearch(fieldName = "rccb.COOPERATION_TYPE")
    private String cooperationType;

    /**
     * 授信银行合作情况名称
     */
    @Excel(name = "*合作情况")
    private String cooperationTypeName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createDate;

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "ratCreditCooperationBankService";
    }
}
