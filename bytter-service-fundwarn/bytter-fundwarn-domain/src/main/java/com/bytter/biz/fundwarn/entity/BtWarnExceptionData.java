package com.bytter.biz.fundwarn.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
* @Description 异常告警表
* @author zhous
* @date 2024/8/22
*/
@Entity
@Table(name = "BT_WARN_EXCEPTION_DATA", schema = "")
@SuppressWarnings("serial")
public class BtWarnExceptionData extends IdEntity implements java.io.Serializable {

    /** 发送时间 */
    private Date sendDate;

    /** 发送状态 0-发送成功 1-发送失败 */
    private String sendStatus;

    /** 渠道 0-企业微信 1-钉钉 */
    private String channel;

    /** 告警类型 0-应用接口 1-前置机服务 */
    private String warnType;

    /** 告警内容 */
    private String warnContent;

    /** 请求报文 */
    private String reqMsg;

    /** 响应报文 */
    private String respMsg;

    @Column(name = "SEND_DATE")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Column(name = "SEND_STATUS")
    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Column(name = "CHANNEL")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Column(name = "WARN_TYPE")
    public String getWarnType() {
        return warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    @Column(name = "WARN_CONTENT")
    public String getWarnContent() {
        return warnContent;
    }

    public void setWarnContent(String warnContent) {
        this.warnContent = warnContent;
    }

    @Column(name = "REQ_MSG")
    public String getReqMsg() {
        return reqMsg;
    }

    public void setReqMsg(String reqMsg) {
        this.reqMsg = reqMsg;
    }

    @Column(name = "RESP_MSG")
    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
