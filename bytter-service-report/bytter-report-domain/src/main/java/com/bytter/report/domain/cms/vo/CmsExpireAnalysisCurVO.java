package com.bytter.report.domain.cms.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class CmsExpireAnalysisCurVO implements Serializable {

    private String curId;

    private BigDecimal leftMoney;

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
}
