package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: GuaContractOccup
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/4/30 19:45
 */
@Entity
@Table(name = "GUA_CONTRACT_OCCUP", schema = "")
public class GuaContractOccup extends IdEntity implements java.io.Serializable {
    /** 业务ID */
    private String businessId;
    /** 担保合同ID */
    private String contractId;
    /** 系统代码 */
    private String sysCode;
    /** 业务编号 */
    private String itemNo;
    /** 使用时间 */
    private Date useDate;
    /** 状态 */
    private Integer status;
    /** 担保备注 */
    private String guaRmk;
    /** 业务币别 */
    private String itemCurId;
    /** 业务金额 */
    private BigDecimal itemMoney;
    /** 业务开始时间 */
    private Date startDate;
    /** 业务结束时间 */
    private Date endDate;
    /** 数据类型 */
    private String dataType;
    /** 业务合同编号 */
    private String itemConCode;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务ID
     */
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             业务ID
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保合同ID
     */
    @Column(name = "CONTRACT_ID")
    public String getContractId() {
        return contractId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保合同ID
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 系统代码
     */
    @Column(name = "SYSCODE")
    public String getSysCode() {
        return sysCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 系统代码
     */
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务编号
     */
    @Column(name = "ITEM_NO")
    public String getItemNo() {
        return itemNo;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 业务编号
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 使用日期
     */
    @Column(name = "USE_DATE")
    public Date getUseDate() {
        return useDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @return: java.util.Date 使用日期
     */
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保备注
     */
    @Column(name = "GUA_RMK")
    public String getGuaRmk() {
        return guaRmk;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 担保备注
     */
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 系统代码
     */
    @Column(name = "ITEM_CURID")
    public String getItemCurId() {
        return itemCurId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 系统代码
     */
    public void setItemCurId(String itemCurId) {
        this.itemCurId = itemCurId;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 业务金额
     */
    @Column(name = "ITEM_MONEY")
    public BigDecimal getItemMoney() {
        return itemMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 业务金额
     */
    public void setItemMoney(BigDecimal itemMoney) {
        this.itemMoney = itemMoney;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 业务开始日期
     */
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @return: java.util.Date 业务结束日期
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 业务结束日期
     */
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @return: java.util.Date 业务结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    /**
     * 方法: 取得 java.lang.String
     * 
     * @return: java.lang.String 数据类型
     */
    @Column(name = "DADA_TYPE")
    public String getDataType() {
        return dataType;
    }
    /**
     * 方法: 设置 java.lang.String
     * 
     * @return: java.lang.String 数据类型
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务合同编号
     */
    @Column(name = "ITEM_CONCODE")
    public String getItemConCode() {
        return itemConCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 业务合同编号
     */
    public void setItemConCode(String itemConCode) {
        this.itemConCode = itemConCode;
    }
}
