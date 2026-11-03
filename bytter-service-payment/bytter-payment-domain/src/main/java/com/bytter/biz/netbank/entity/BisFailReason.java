package com.bytter.biz.netbank.entity;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 交易失败原因表
 * @author huangx
 * @date 2019-05-17 14:26:13
 * @version 1.0
 *
 */
@ApiModel("交易失败原因表")
@Entity
@Table(name = "BIS_FAIL_REASON", schema = "")
@SuppressWarnings("serial")
public class BisFailReason extends IdEntity implements java.io.Serializable {
    /**失败原因编码*/
    @ApiModelProperty("失败原因编码")
    private String failCode;

    /**失败原因名称*/
    @ApiModelProperty("失败原因名称")
    private String failName;

    /**关键字*/
    @ApiModelProperty("关键字")
    private String keyWords;

    /**关联行为 1-指令重发 2-指令打回 3-指令作废*/
    @ApiModelProperty("关联行为 1-指令重发 2-指令打回 3-指令作废")
    private String actionCode;

    /**创建人登录名称*/
    @ApiModelProperty("创建人登录名称")
    private String createBy;

    /**创建日期*/
    @ApiModelProperty("创建日期")
    private Date createDate;

    /**更新人登录名称*/
    @ApiModelProperty("更新人登录名称")
    private String updateBy;

    /**更新日期*/
    @ApiModelProperty("更新日期")
    private Date updateDate;

    /**接口代码*/
    @ApiModelProperty("接口代码")
    private String bifCode;
    @Column(name ="FAIL_CODE",nullable=false,length=12)
    public String getFailCode() {
        return failCode;
    }
    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }
    @Column(name ="FAIL_NAME",nullable=false,length=50)
    public String getFailName() {
        return failName;
    }
    public void setFailName(String failName) {
        this.failName = failName;
    }
    @Column(name ="KEY_WORDS",nullable=true,length=1000)
    public String getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    @Column(name ="ACTION_CODE",nullable=true,length=1)
    public String getActionCode() {
        return actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name ="BIF_CODE",nullable=true)
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
}
