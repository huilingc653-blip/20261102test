package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 授信延期表新增页面数据
 */
@Data
public class RatBkrationDelayAddVO implements Serializable {
    private RatBkrationPageVO ratBkrationPage;
    private Date delayDate;
    private String isGuarantee;
}
