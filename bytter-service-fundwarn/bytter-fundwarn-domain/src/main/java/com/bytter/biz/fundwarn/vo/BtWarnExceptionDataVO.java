package com.bytter.biz.fundwarn.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
* @Description 异常告警数据VO
* @author zhous
* @date 2024/8/21 17:16
*/
public class BtWarnExceptionDataVO  implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 发送时间 */
    private String id;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="a.send_date")
    private Date sendDate;

    /** 发送状态 0-发送成功 1-发送失败 */
    private String sendStatus;
    private String sendStatusName;

    /** 渠道 0-企业微信 1-钉钉 */
    @BtCompositeSearch(fieldName="a.channel")
    private String channel;
    private String channelName;

    /** 告警类型 0-应用接口 1-前置机服务 */
    @BtCompositeSearch(fieldName="a.warn_type")
    private String warnType;
    private String warnTypeName;

    /** 告警内容 */
    @BtCompositeSearch(fieldName="a.warn_content")
    private String warnContent;

    /** 请求报文 */
    private String reqMsg;

    /** 响应报文 */
    private String respMsg;

    public String getSendStatusName() {
        return sendStatusName;
    }

    public void setSendStatusName(String sendStatusName) {
        this.sendStatusName = sendStatusName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getWarnTypeName() {
        return warnTypeName;
    }

    public void setWarnTypeName(String warnTypeName) {
        this.warnTypeName = warnTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getWarnType() {
        return warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public String getWarnContent() {
        return warnContent;
    }

    public void setWarnContent(String warnContent) {
        this.warnContent = warnContent;
    }

    public String getReqMsg() {
        return reqMsg;
    }

    public void setReqMsg(String reqMsg) {
        this.reqMsg = reqMsg;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
