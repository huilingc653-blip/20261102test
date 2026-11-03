package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.util.Date;
/**
 * @author qinl
 * @version V1.0
 * @Title: VO
 * @Description: 账户性质表
 * @date 2019-11-15
 */
public class BtAccNatureVO implements Serializable {
    private String id;
    /**
     * 账户性质编码
     */
    private String natureCode;
    /**
     * 账户性质名称
     */
    private String natureName;
    private String natureNameShow;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
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
}
