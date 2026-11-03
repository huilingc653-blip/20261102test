package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 付款单-供应链金融债权凭证关联表（中间表）
 * <p>承载付款单与应付/应收债权凭证的关联，替代在凭证表上直接写 pay_no/pay_id。
 * 凭证类型：1=应付债权凭证（付款方式51），2=应收债权凭证（付款方式52）。
 * @author zhukr
 * @since 2026/8/18
 */
@Entity
@Table(name = "NIS_SCF_INFO", schema = "")
@SuppressWarnings("serial")
public class NisScfInfo extends IdEntity implements java.io.Serializable {

    /**付款单ID*/
    private String payId;

    /**付款单号*/
    private String payNo;

    /**凭证类型（1=应付债权凭证，2=应收债权凭证）*/
    private String voucherType;

    /**凭证ID*/
    private String voucherId;

    /**转让草稿ID（52-应收债权凭证转让暂存）*/
    private String transferId;

    /**供应链金融平台ID（51-未开立生成暂存凭证用）*/
    private String platformId;

    /**融资产品ID（51-未开立生成暂存凭证用）*/
    private String productId;

    @Column(name = "PAY_ID", length = 32)
    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    @Column(name = "PAY_NO", length = 50)
    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    @Column(name = "VOUCHER_TYPE", length = 2)
    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    @Column(name = "VOUCHER_ID", length = 32)
    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    @Column(name = "TRANSFER_ID", length = 32)
    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    @Column(name = "PLATFORM_ID", length = 32)
    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    @Column(name = "PRODUCT_ID", length = 32)
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
