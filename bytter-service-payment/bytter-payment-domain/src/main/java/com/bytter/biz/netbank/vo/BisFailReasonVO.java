package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
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
public class BisFailReasonVO implements Serializable {
    @Dict(tableName="BIS_FAIL_REASON",valueFieldName="id",showFiledName="FAIL_NAME",inject="failName")
    @ApiModelProperty("ID")
    private String id;

    /**失败原因编码*/
    @BtCompositeSearch(fieldName = "b.FAIL_CODE")
    @ApiModelProperty("失败原因编码")
    private String failCode;

    /**失败原因名称*/
    @BtCompositeSearch(fieldName = "b.FAIL_NAME")
    @ApiModelProperty("失败原因名称")
    private String failName;

    @ApiModelProperty("失败原因显示名称")
    private String failNameShow;

    /**关键字*/
    @ApiModelProperty("关键字")
    @BtCompositeSearch(fieldName = "b.KEY_WORDS")
    private String keyWords;

    /**关联行为 1-指令重发 2-指令打回 3-指令作废*/
    @ApiModelProperty("关联行为 1-指令重发 2-指令打回 3-指令作废")
    @BtCompositeSearch(fieldName = "b.ACTION_CODE")
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

    /**银企接口*/
    @ApiModelProperty("银企接口")
    @BtCompositeSearch(fieldName = "b.BIF_CODE")
    @Dict(dictGroup = "bifCode",inject = "bifName")//用于排序
    private String bifCode;

    @ApiModelProperty("银企接口名称")
    private String bifName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFailCode() {
        return failCode;
    }
    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }
    public String getFailName() {
        return failName;
    }
    public void setFailName(String failName) {
        this.failName = failName;
    }
    public String getFailNameShow() {
        return failNameShow;
    }
    public void setFailNameShow(String failNameShow) {
        this.failNameShow = failNameShow;
    }
    public String getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    public String getActionCode() {
        return actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
    }
}
