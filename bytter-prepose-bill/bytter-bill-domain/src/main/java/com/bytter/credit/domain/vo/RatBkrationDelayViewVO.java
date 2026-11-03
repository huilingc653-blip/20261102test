package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 授信延期表详情页面数据
 */
@Data
public class RatBkrationDelayViewVO implements Serializable {
    private String delaySign;
    private Date delayDate;
    private String attachmentId;
    private RatBkrationPageVO ratBkrationPage;
    private String viewFlag;
    private String isGuarantee;
}
