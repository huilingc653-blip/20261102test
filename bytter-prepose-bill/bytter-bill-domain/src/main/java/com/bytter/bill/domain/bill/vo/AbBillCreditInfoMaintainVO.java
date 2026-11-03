package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行开票授信资料维护表
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AbBillCreditInfoMaintainVO extends ExcelModel implements java.io.Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     * 单位id
     */
    @BtCompositeSearch(fieldName = "m.CORP_ID")
    private String corpId;
    /**
     * 单位名称
     */
    @Excel(name = "*单位名称")
    private String corpName;
    /**
     * 银行类型
     */
    @BtCompositeSearch(fieldName = "m.BANK_TYPE_ID")
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    @Excel(name = "*银行名称")
    private String bankTypeName;
    /**
     * 授信类型：敞口和低风险
     */
    @BtCompositeSearch(fieldName = "m.CREDIT_TYPE")
    private String creditType;
    /**
     * 授信类型名称
     */
    @Excel(name = "*授信类型")
    private String creditTypeName;
    /**
     * 保证金比例
     */
    @Excel(name = "保证金比例")
    private BigDecimal bondRatio;
    /**
     * 开票手续费
     */
    @Excel(name = "开票手续费")
    private BigDecimal openBillRate;
    /**
     * 保证金利率
     */
    @Excel(name = "保证金利率")
    private String bondRate;
    /**
     * 是否能正常使用：1-是，0-否
     */
    private String isUsed;
    /**
     * 无
     */
    private String isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;

    @Override
    public String getBeanName() {
        return "abBillCreditMaintainService";
    }
}
