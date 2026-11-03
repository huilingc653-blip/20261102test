package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 * @author zhouj
 * @date 2018/5/18
 */
@Entity
@Table(name = "ERP_HIS_DTL_TEMPLATE",catalog = "")
public class ErpHisDtlTemplate extends IdEntity implements Serializable{
    private String serialId;
    private String type;
    private String templateId;
    private String sendFrequency;
    private String sendReson;
    private String sendBackNo;
    private String reverse1;
    private String reverse2;
    private Long status;
    private Timestamp updateDate;
    private String updateDy;
    
    public ErpHisDtlTemplate(){
        super();
    }
    public ErpHisDtlTemplate(String serialId, String templateId, String dealType, Long status) {
        super();
        this.serialId = serialId;
        this.templateId = templateId;
        this.type = dealType;
        this.status = status;
    }
    @Basic
    @Column(name = "SERIAL_ID", nullable = true, length = 32)
    public String getSerialId() {
        return serialId;
    }
    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }
    @Basic
    @Column(name = "TYPE", nullable = true, length = 20)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Basic
    @Column(name = "TEMPLATE_ID", nullable = true, length = 32)
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    @Basic
    @Column(name = "SEND_FREQUENCY", nullable = true, length = 100)
    public String getSendFrequency() {
        return sendFrequency;
    }
    public void setSendFrequency(String sendFrequency) {
        this.sendFrequency = sendFrequency;
    }
    @Basic
    @Column(name = "SEND_RESON", nullable = true, length = 100)
    public String getSendReson() {
        return sendReson;
    }
    public void setSendReson(String sendReson) {
        this.sendReson = sendReson;
    }
    @Basic
    @Column(name = "SEND_BACK_NO", nullable = true, length = 100)
    public String getSendBackNo() {
        return sendBackNo;
    }
    public void setSendBackNo(String sendBackNo) {
        this.sendBackNo = sendBackNo;
    }
    @Basic
    @Column(name = "REVERSE1", nullable = true, length = 100)
    public String getReverse1() {
        return reverse1;
    }
    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }
    @Basic
    @Column(name = "REVERSE2", nullable = true, length = 100)
    public String getReverse2() {
        return reverse2;
    }
    public void setReverse2(String reverse2) {
        this.reverse2 = reverse2;
    }
    @Basic
    @Column(name = "STATUS", nullable = true, precision = 0)
    public Long getStatus() {
        return status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    @Column(name = "UPDATE_DATE")
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
    @Column(name = "UPDATE_DY")
	public String getUpdateDy() {
		return updateDy;
	}
	public void setUpdateDy(String updateDy) {
		this.updateDy = updateDy;
	}
    
}
