package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: AbPoolCorp
 * @Description: 票据池单位
 * @author wangzy
 * @date 2019/12/30 15:17
 *
 */
@Entity
@Table(name = "AB_POOL_CORP", schema = "")
@SuppressWarnings("serial")
public class AbPoolCorp extends IdEntity implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    /**单位ID**/
    private String corpId;
    /**单位CODE（冗余）**/
    private String corpCode;
    /**单位名称（冗余）**/
    private String corpName;
    /**上级单位ID**/
    private String parentId;
    /**状态 1启用，0禁用*/
    private String validSign;
    /**层级*/
    private Integer bustypeLevel;
    /**创建人id**/
    private String createId;
    /**创建日期	CREATE_DATE**/
    private Date createTime;
    /**更新人id**/
    private String updateId;
    /**更新日期**/
    private Date updateTime;
    @Column(name ="corp_id")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="corp_code")
    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    @Column(name ="corp_name")
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name ="PARENT_ID")
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name ="BUSTYPE_LEVEL")
    public Integer getBustypeLevel() {
        return bustypeLevel;
    }
    public void setBustypeLevel(Integer bustypeLevel) {
        this.bustypeLevel = bustypeLevel;
    }
    @Column(name ="CREATE_ID")
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name ="CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name ="UPDATE_ID")
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name ="UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
