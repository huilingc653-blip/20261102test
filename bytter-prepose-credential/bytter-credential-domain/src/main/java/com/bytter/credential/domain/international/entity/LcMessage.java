package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**信用证日志表**/
@Entity
@Table(name = "LC_MESSAGE", schema = "")
public class LcMessage extends IdEntity implements java.io.Serializable{
    /**信用证ID**/
    private String lcId;
    /**业务单据编号**/
    private String billNo;
    /**消息类型**/
    private Integer messageType;
    /**消息内容**/
    private String message;
    /**创建日期**/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**创建人登录名称**/
    private String createBy;
    @Column(name ="LC_ID")
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
    @Column(name ="BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name ="MESSAGE_TYPE")
    public Integer getMessageType() {
        return messageType;
    }
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    @Column(name ="MESSAGE")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
