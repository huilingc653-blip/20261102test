package com.bytter.system.vo;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * @author qinl
 * @version V1.0
 * @Title: VO
 * @Description: 账户性质表
 * @date 2019-11-15
 */
@ApiModel("账户性质表")
public class BtAccNatureVO implements Serializable {
    @ApiModelProperty("id")
	@Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
    private String id;
    /**
     * 账户性质编码
     */
    @ApiModelProperty("id")
    private String natureCode;
    /**
     * 账户性质名称
     */
    @ApiModelProperty("id")
    private String natureName;
    private String natureNameShow;
    /**
     * 备注
     */
    @ApiModelProperty("id")
    private String rmk;
    /**
     * 有效标志
     */
    @ApiModelProperty("id")
    private String validSign;
    @ApiModelProperty("id")
    private String validSignName;
    /**
     * 创建人登录名称
     */
    @ApiModelProperty("创建人登录名称")
    private String createBy;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    @ApiModelProperty("i更新人登录名称d")
    private String updateBy;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    private java.util.Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNatureCode() {
        return natureCode;
    }
    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }
    public String getNatureName() {
        return natureName;
    }
    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
    public String getNatureNameShow() {
        return natureNameShow;
    }
    public void setNatureNameShow(String natureNameShow) {
        this.natureNameShow = natureNameShow;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
}
