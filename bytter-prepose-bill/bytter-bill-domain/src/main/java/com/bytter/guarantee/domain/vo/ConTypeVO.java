package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 *
 * @Title: 合同类型管理VO
 * @Package com.bytter.biz.contract.vo
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-5 下午5:29:25
 * @version V1.0
 */
public class ConTypeVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @Dict(tableName = "CON_TYPE", valueFieldName = "ID", showFiledName = "CON_NAME", inject = "typeName")
    private String id;
    private String cid;
    private String corpName;
    private String corpCode;
    /** 合同类型代码 */
    @BtCompositeSearch(fieldName = "ct.con_code")
    private String typeCode;
    /** 合同类型名称 */
    @BtCompositeSearch(fieldName = "ct.con_name")
    private String typeName;
    /** 合同类型名称 */
    @BtCompositeSearch(fieldName = "ct.con_name")
    private String name;
    /** 单位Id */
    @BtCompositeSearch(fieldName = "ct.corp_id")
    private String corpId;
    /** 是否共享：0否1是 */
    @Dict(dictGroup = "sf_yn", inject = "shareSignName")
    private String shareSign;
    private String shareSignName;
    /** 有效标志 */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /** 备注 */
    private String rmk;
    /** 创建人id */
    private String createId;
    /** 创建日期 */
    private Date createTime;
    /** 更新人id */
    private String updateId;
    /** 更新日期 */
    private Date updateTime;
    /** 是否可以删除 */
    private String delFlag;
    /** 操作资源权限 */
    private String opResource;
    /** 合同查询默认值 */
    private String typeIdVal;
    /** 产品系统合同类型 */
    private String pdCode;
    /** 类型详细信息VO */
    private List<ConTypeFieldVO> conTypeFieldVO;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getShareSign() {
        return shareSign;
    }
    public void setShareSign(String shareSign) {
        this.shareSign = shareSign;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public List<ConTypeFieldVO> getConTypeFieldVO() {
        return conTypeFieldVO;
    }
    public void setConTypeFieldVO(List<ConTypeFieldVO> conTypeFieldVO) {
        this.conTypeFieldVO = conTypeFieldVO;
    }
    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
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
    public String getShareSignName() {
        return shareSignName;
    }
    public void setShareSignName(String shareSignName) {
        this.shareSignName = shareSignName;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getOpResource() {
        return opResource;
    }
    public void setOpResource(String opResource) {
        this.opResource = opResource;
    }
    public String getPdCode() {
        return pdCode;
    }
    public void setPdCode(String pdCode) {
        this.pdCode = pdCode;
    }
    public String getTypeIdVal() {
        return typeIdVal;
    }
    public void setTypeIdVal(String typeIdVal) {
        this.typeIdVal = typeIdVal;
    }
    @Override
    public String toString() {
        return "ConTypeVO [id=" + id + ", cid=" + cid + ", corpName=" + corpName + ", corpCode=" + corpCode
                + ", typeCode=" + typeCode + ", typeName=" + typeName + ", corpId=" + corpId + ", shareSign="
                + shareSign + ", shareSignName=" + shareSignName + ", validSign=" + validSign + ", validSignName="
                + validSignName + ", rmk=" + rmk + ", createId=" + createId + ", createTime=" + createTime
                + ", updateId=" + updateId + ", updateTime=" + updateTime + ", delFlag=" + delFlag + ", opResource="
                + opResource + ", conTypeFieldVO=" + conTypeFieldVO + "]";
    }
}
