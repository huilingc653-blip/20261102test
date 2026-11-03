package com.bytter.biz.bdlink.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: Entity
 * Description: BT_BANK_DTL
 *
 * @author onlineGenerator
 * @version 1.0
 * @since 2018-03-19 17:50:16
 */
@Data
public class BisAccDtlDTO implements Serializable {
    private String id;
    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "a.trans_Time")
    private Date transTime;
    /**
     * 摘要
     */
    @BtCompositeSearch(fieldName = "a.remark")
    private String remark;
    /*** 单位ID*/
    // @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /*** 银行类型*/
    private String bankTypeId;
    private String bankTypeName;
    /*** 本方账号*/
    @BtCompositeSearch(fieldName = "a.bank_acc")
    private String bankAcc;
    /*** 本方账户名*/
    private String accName;
    /*** 本方账户开户行*/
    private String bankName;
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 接口名称
     */
    private String bifName;
    /*** 借贷标志*/
    private String cdSign;
    private String cdSignName;
    /*** 红蓝标志*/
    private String rbSign;
    /*** 币别ID*/
    // @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curId;
    private String curName;
    /**
     * 用途
     */
    private String uses;
    /*** 附言*/
    private String postscript;
    /*** 对方账号*/
    private String oppAccNo;
    /*** 对方账户名*/
    private String oppAccName;
    /*** 对方账户开户行*/
    private String oppAccBank;
    /*** 交易金额*/
    @BtCompositeSearch(fieldName = "a.amt")
    private BigDecimal amt;
    /*** 余额*/
    private BigDecimal bal;
    /*** 计划状态*/
    private String fbsStatus;
    /*** 企业流水号*/
    private String voucherNo;
    /*** 银行流水号*/
    private String bankSerialNo;
    /*** 返回时间*/
    private Date returnTime;
    /*** 业务单号*/
    private String accNo;
    /*** 创建人登录名称*/
    private String createBy;
    /*** 创建日期*/
    private Date createDate;
    /*** 更新人登录名称*/
    private String updateBy;
    /*** 更新日期*/
    private Date updateDate;
    /*** 银行接口导出标识 */
    private String importSign;
    /*** 导出日期*/
    private Date importDate;
    /**
     * 资金池主体
     **/
    @BtCompositeSearch(fieldName = "c.id")
    private String poolId;
    /**
     * 记账状态
     **/
    private String tallySign;
    /**
     * 流水记账关联表Id
     **/
    private String accDtlId;
    /*** 付款方式*/
    private String settlType;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 原单处理方式 1-不处理原单、2-生成新单、3-打回原单
     */
    private String refundBlendType;
    private String refundBlendTypeName;
    /**
     * 业务单id
     */
    private String billId;
    /**
     * 合并后单据ID
     */
    private String mergeId;
    /**
     * 拆分前单据ID
     */
    private String splitId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 票据认领流水id
     */
    private String claimId;
    /**
     * 项目信息
     */
    private String projectRemark;
    
    private String autoSign;
	
	private String againSign;
}
