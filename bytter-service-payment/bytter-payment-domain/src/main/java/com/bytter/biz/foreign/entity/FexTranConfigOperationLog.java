package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 操作日志
 */
@Entity
@Table(name = "fex_tran_config_operation_log", schema = "")
public class FexTranConfigOperationLog extends IdEntity implements java.io.Serializable {
    private static final long serialVersionUID = 7287928273418866659L;
    /**
     * 操作页面  0:交易页面配置  1:交易指令配置
     */
    private String operationPage;
    /**
     * 操作内容
     */
    private String operationContent;
    /**
     * 修改前数据
     */
    private String beforeData;
    /**
     * 修改后数据
     */
    private String afterData;
    /**
     * 创建人ID
     */
    private String createBy;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 是否删除 0否 1是
     */
    private String isDelete;

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "operation_page")
    public String getOperationPage() {
        return operationPage;
    }

    public void setOperationPage(String operationPage) {
        this.operationPage = operationPage;
    }

    @Column(name = "operation_content")
    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    @Column(name = "before_data")
    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    @Column(name = "after_data")
    public String getAfterData() {
        return afterData;
    }


    public void setAfterData(String afterData) {
        this.afterData = afterData;
    }

    @Column(name = "create_by")
    public String getCreateBy() {
        return createBy;
    }


    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_name")
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Column(name = "is_delete")
    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
