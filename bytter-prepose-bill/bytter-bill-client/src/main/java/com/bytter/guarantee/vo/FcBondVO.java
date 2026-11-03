package com.bytter.guarantee.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class FcBondVO implements Serializable {

    private String id;

    /** 业务单号 */
    private String billNo;
    /** 单位ID */
    private String corpId;
    /** 银行账号ID */
    private String bankAccId;
    /** 存入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    /** 是否已付款 */
    private String isPayed;
    /** 保证金金额 */
    private BigDecimal bondMoney;
    /** 备注 */
    private String remark;
    /** 状态 */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**币别*/
    private String curId;
    /** 附件ID */
    private String attachmentId;
    /**存出账号*/
    private String outBankAcc;
    /**保证金类型*/
    private String bondTypeId;
    /**利率*/
    private BigDecimal interestRate;
    /**到期日期*/
    private Date endDate;
    /**是否存入定期*/
    private String isRegular;
    /**期限*/
    private String dayLimit;
    /**用途*/
    private String purpose;
    /**单位名称*/
    private String corpName;
    /**币别名称*/
    private String curName;
    /**银行账号*/
    private String bankAcc;
    /**账户名称*/
    private String accName;
    /**开户行行号*/
    private String bankCode;
    /**存入银行名称*/
    private String bankName;

}
