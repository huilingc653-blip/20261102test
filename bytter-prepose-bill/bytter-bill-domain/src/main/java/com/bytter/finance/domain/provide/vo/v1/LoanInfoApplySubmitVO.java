package com.bytter.finance.domain.provide.vo.v1;

import com.bytter.core.annotation.Currency;
import com.bytter.credit.dto.RatUrationDtlDTO;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class LoanInfoApplySubmitVO extends LoanInfoVO implements Serializable {

    private String saveType;
    private String myflag;
    /**
     * 占用ID，可回写给业务表
     */
    private String urationId;
    /**
     * 单位ID(必填)
     */
    private String corpId;
    /**
     * 授信编号(必填)
     */
    private String ratCode;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 授信品种
     */
    private String ratItemCode;
    private String ratItemName;
    /**
     * 系统授信品种类型
     */
    private String sysCode;
    /**
     * 业务模块代码(必填)
     */
    private String businessName;
    /**
     * 业务币别(必填)
     */
    private String curCode;
    /**
     * 合同号(手工占用时必填，外部挂接时选填)
     */
    private String contactNo;
    /**
     * 业务ID(必填)
     */
    private String billId;
    /**
     * 业务单号(选填)
     */
    private String billNo;
    /**
     * 占用额度(必填)
     */
    private BigDecimal money;
    /**
     * 保证金比例（选填，如果存在）
     */
    private BigDecimal bailRate;
    /**
     * 开始日期(必填)
     */
    private Date beginDate;
    /**
     * 结束日期(必填)
     */
    private Date endDate;
    /**
     * 占用明细 如果占用单项，不必填写，需填写itemCode 如果占用综合授信，则明细必须填写
     */
    private List<RatUrationDtlDTO> applyDtls;
    /**
     * 释放标识
     */
    private boolean releaseFlag = false;
    /**
     * 是否全部释放
     */
    private boolean allRelease = false;
    /**
     * 品种类型(不用填写)
     */
    private String itemFlag;
    /**
     * 获取操作用户
     */
    private String userId;
    /**
     * 业务品种ID
     */
    private String billName;
    /**
     * 业务品种名称
     */
    private String raceName;
    /**
     * 业务金额（必填）
     */
    private BigDecimal billMoney;
    /**
     * 保证金金额(必填)
     */
    private BigDecimal bondMoney;
    /**
     * 保证金币种(必填)
     */
    @Currency(inject = "bondCurName")
    private String bondCurId;
    private String bondCurName;
    /**
     * 是否启用授信敞口(必填，根据授信基础参数设置，需给默认值，1=启用，0=不启用)
     */
    private String isOpenRat;
    /**
     * 业务币种（手工占用时用此字段）
     */
    private String initCurCode;
    /**
     * 授信是否挂接担保
     */
    private String ratIsHangGua;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否释放不循环授信
     */
    private boolean releaseNonRecycleRat;
    /**
     * 额度类型 0-不循环 1-循环
     */
    private String creditType;
    /**
     * 外模块挂接授信查看时使用
     */
   // private String itemCode;
    /**
     * 外模块挂接授信查看时使用
     */
    private BigDecimal useMaxMoney;

    String isRmbRat;
}
