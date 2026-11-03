package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @version V1.0
 * @Title: 合同主表
 * @Package com.bytter.biz.contract.entity
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-4 下午2:53:20
 */
@Entity
@Table(name = "CON_INFO", schema = "")
@SuppressWarnings("serial")
public class ConInfo extends IdEntity implements java.io.Serializable {
    /**
     * 合同编号
     */
    private String conNo;
    /**
     * 合同名称
     */
    private String conName;
    /**
     * 合同发起方单位ID
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 单位编码
     */
    private String corpCode;
    /**
     * 签署方式
     */
    private String signType;
    /**
     * 合同类型ID
     */
    private String typeId;
    /**
     * 合同生效日期
     */
    private Date beginDate;
    /**
     * 合同失效日期
     */
    private Date endDate;
    /**
     * 履约次数
     */
    private Integer performanceNum;
    /**
     * 变更次数
     */
    private Integer changeNum;
    /**
     * 合同状态
     */
    private Integer status;
    /**
     * 合同附件ID
     */
    private String attachmentId;
    /**
     * 起草日期
     */
    private Date draftDate;
    /**
     * 签署日期
     */
    private Date signDate;
    /**
     * 合同挂接标记 1被挂接 0和空不被挂接
     */
    private String hookSign;
    private String dataSource;
    @Column(name = "CON_NO", nullable = true, length = 50)
    public String getConNo() {
        return conNo;
    }
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }
    @Column(name = "CON_NAME", nullable = true, length = 100)
    public String getConName() {
        return conName;
    }
    public void setConName(String conName) {
        this.conName = conName;
    }
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "SIGN_TYPE", nullable = true, length = 20)
    public String getSignType() {
        return signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    @Column(name = "TYPE_ID", nullable = true, length = 32)
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    @Column(name = "BEGIN_DATE", nullable = true)
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "PERFORMANCE_NUM", nullable = true, length = 6)
    public Integer getPerformanceNum() {
        return performanceNum;
    }
    public void setPerformanceNum(Integer performanceNum) {
        this.performanceNum = performanceNum;
    }
    @Column(name = "CHANGE_NUM", nullable = true, length = 6)
    public Integer getChangeNum() {
        return changeNum;
    }
    public void setChangeNum(Integer changeNum) {
        this.changeNum = changeNum;
    }
    @Column(name = "STATUS", nullable = true, length = 6)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "DRAFT_DATE", nullable = true)
    public Date getDraftDate() {
        return draftDate;
    }
    public void setDraftDate(Date draftDate) {
        this.draftDate = draftDate;
    }
    @Column(name = "SIGN_DATE", nullable = true)
    public Date getSignDate() {
        return signDate;
    }
    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }
    @Column(name = "DATA_SOURCE")
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    @Column(name = "HOOK_SIGN")
    public String getHookSign() {
        return hookSign;
    }
    public void setHookSign(String hookSign) {
        this.hookSign = hookSign;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
}
