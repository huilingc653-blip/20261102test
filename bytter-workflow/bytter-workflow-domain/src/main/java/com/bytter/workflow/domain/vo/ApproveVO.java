package com.bytter.workflow.domain.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Corps;
import com.bytter.core.annotation.User;
import com.bytter.core.annotation.Users;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 提供给APP使用的VO
 * @history
 *  1. 2010-01-02  chenlw  标注单据配置相关的字段：invoiceId、invoiceName。
 */
public class ApproveVO implements Serializable {
    private String id;

    /**
     * invoiceId用于显示不同审批流程的业务号码：例如单据号，付款单号等。
     */
    private String invoiceId;

    /**
     * invoiceName是业务号码的名称，其值可为单据号，付款单号等。
     */
    private String invoiceName;

    private String businessId;
    private String categoryId;
    private String categoryName;

    @Corps({
            @Corp(inject = "corpCode", field = "code"),
            @Corp("corpName")
    })
    @Transient
    private String corpId;
    private String corpName;
    private String corpCode;

    @Users({
            @User(inject = "userCode", field = "code"),
            @User("username")
    })
    @Transient
    private String userId;
    private String username;
    private String userCode;

    private Integer status;
    private Long createTime;

    /**
     * 自定义显示字段列表
     */
    private List<ApproveExtendParamVo> extendParams;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /**
     * @deprecated by chenlw on 2020/01/02  单据配置字段不再使用
     * @return
     */
    @Deprecated
    public String getInvoiceId() {
        return invoiceId;
    }

    /**
     * @deprecated by chenlw on 2020/01/02  单据配置字段不再使用
     * @param invoiceId 单据配置字段
     */
    @Deprecated
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @deprecated by chenlw on 2020/01/02  单据配置字段不再使用
     * @return
     */
    @Deprecated
    public String getInvoiceName() {
        return invoiceName;
    }

    /**
     * @deprecated by chenlw on 2020/01/02  单据配置字段不再使用
     * @param invoiceName
     */
    @Deprecated
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public List<ApproveExtendParamVo> getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(List<ApproveExtendParamVo> extendParams) {
        this.extendParams = extendParams;
    }
}
