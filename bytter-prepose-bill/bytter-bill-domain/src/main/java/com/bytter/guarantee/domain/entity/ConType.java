package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同类型表Entity
 * @Description: CON_TYPE合同类型表
 * @date 2019-11-04
 */
@Entity
@Table(name = "CON_TYPE", schema = "")
@SuppressWarnings("serial")
public class ConType extends IdEntity implements java.io.Serializable {
    /**
     * 合同类型代码
     */
    private String typeCode;
    /**
     * 合同类型名称
     */
    private String typeName;
    /**
     * 单位Id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 单位编码
     */
    private String corpNCode;
    /**
     * 是否共享：0否1是
     */
    private String shareSign;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人id
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 方法: 取得合同类型代码
     *
     * @return:合同类型代码
     */
    @Column(name = "CON_CODE", nullable = true, length = 10)
    public String getTypeCode() {
        return typeCode;
    }
    /**
     * 设置 合同类型代码
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    /**
     * 方法: 取得合同类型名称
     *
     * @return:合同类型名称
     */
    @Column(name = "CON_NAME", nullable = true, length = 100)
    public String getTypeName() {
        return typeName;
    }
    /**
     * 设置 合同类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    /**
     * 方法: 取得单位ID
     *
     * @return:单位ID
     */
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return corpId;
    }
    /**
     * 设置 单位ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得 是否共享：0否1是
     *
     * @return:是否共享：0否1是
     */
    @Column(name = "SHARE_SIGN", nullable = true, length = 1)
    public String getShareSign() {
        return shareSign;
    }
    /**
     * 设置 是否共享：0否1是
     */
    public void setShareSign(String shareSign) {
        this.shareSign = shareSign;
    }
    /**
     * 方法: 取得有效标志
     *
     * @return: java.lang.String 有效标志：0业务中1业务完成2已作废
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }
    /**
     * 设置 有效标志：0业务中1业务完成2已作废
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 方法: 取得 备注
     *
     * @return:
     */
    @Column(name = "RMK", nullable = true, length = 500)
    public String getRmk() {
        return rmk;
    }
    /**
     * 设置 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得 创建人名称
     *
     * @return: 创建人名称
     */
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    /**
     * 设置 创建人名称
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    /**
     * 方法: 取得 创建日期
     *
     * @return: 创建日期
     */
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得 更新人名称
     *
     * @return: 更新人名称
     */
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    /**
     * 设置 更新人名称
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得 更新日期
     *
     * @return:更新日期
     */
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "CORP_NAME", nullable = true)
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CORP_CODE", nullable = true)
    public String getCorpNCode() {
        return corpNCode;
    }
    public void setCorpNCode(String corpNCode) {
        this.corpNCode = corpNCode;
    }
    @Override
    public String toString() {
        return "ConType{" + "typeCode='" + typeCode + '\'' + ", typeName='" + typeName + '\'' + ", corpId='" + corpId
                + '\'' + ", shareSign='" + shareSign + '\'' + ", validSign='" + validSign + '\'' + ", rmk='" + rmk
                + '\'' + ", createId='" + createId + '\'' + ", createTime=" + createTime + ", updateId='" + updateId
                + '\'' + ", updateTime=" + updateTime + '}';
    }
}
