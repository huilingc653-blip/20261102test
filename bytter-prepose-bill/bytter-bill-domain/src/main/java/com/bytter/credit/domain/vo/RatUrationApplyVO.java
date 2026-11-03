package com.bytter.credit.domain.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 授信占用VO
 *
 * @author fangcf 2025-06-04
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class RatUrationApplyVO extends ExcelModel implements Serializable, IBusinessKey {

    private String id;
    /**
     * 占用授信品种
     */
    private String itemCode;
    /**
     * 占用授信品种
     */
    @Excel(name = "*占用授信品种")
    private String itemCodeName;
    /**
     * 授信品种综合标识
     */
    private String itemFlag;
    /**
     * 授信类型名称：单品种或综合品种
     */
    private String itemFlagName;
    /**
     * 业务品种（id）
     */
    private String billName;
    /**
     * 业务品种名称
     */
    @Excel(name = "业务品种")
    private String raceName;
    /**
     * 业务币种(必填)
     */
    private String initCurCode;
    /**
     * 是否启用银行授信敞口
     */
    private String isOpenRat;
    /**
     * 是否启用银行授信敞口
     */
    @Excel(name = "*是否启用银行授信敞口")
    private String isOpenRatName;
    /**
     * 业务币种名称
     */
    @Excel(name = "*业务币种")
    private String initCurName;
    /**
     * 业务金额
     */
    @Excel(name = "*业务金额")
    private BigDecimal billMoney;
    /**
     * 保证金币种(必填)
     */
    private String bondCurId;
    /**
     * 保证金币种名称(必填)
     */
    @Excel(name = "保证金币种")
    private String bondCurName;
    /**
     * 保证金比例
     */
    @Excel(name = "保证金比例")
    private BigDecimal assurepercent;
    /**
     * 保证金金额
     */
    private BigDecimal bondMoney;
    /**
     * 占用授信金额
     */
    private BigDecimal money;
    /**
     * 担保方式
     */
    private String guaWay;
    /**
     * 担保方式名称
     */
    @Excel(name = "担保方式")
    private String guaWayName;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*开始日期")
    private Date beginDate;
    /**
     * 预计结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*预计结束日期")
    private Date endDate;
    /**
     * 占用期限
     */
    private Integer days;
    /**
     * 合同号
     */
    @Excel(name = "*合同号")
    private String contactNo;
    /**
     * 信用证号
     */
    @Excel(name = "信用证号")
    private String lcNo;
    /**
     * 票据（包）号码
     */
    @Excel(name = "票据（包）号码")
    private String billNo;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
    /**
     * 授信品种排序
     */
    private Integer itemOrder;
    /**
     * 额度类型
     */
    private String creditType;
    /**
     * 授信品种最高额度
     */
    private BigDecimal maxMoney;

    private List<RatItemDisVO> itemDisList;

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "ratUrationApplyService";
    }
}
