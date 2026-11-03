package com.bytter.credit.dto;

import java.io.Serializable;

/**
 * 更新授信分配信息占用额度入参
 */
public class RatUrationUpdateRatDisDTO implements Serializable {
    /**
     * 授信分配主键
     */
    private String disId;
    /**
     * 授信编号
     */
    private String ratCode;

    public String getDisId() {
        return disId;
    }

    public void setDisId(String disId) {
        this.disId = disId;
    }

    public String getRatCode() {
        return ratCode;
    }

    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
}
