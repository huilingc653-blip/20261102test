package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NisBillheadPoolListResp implements Serializable {
    private Date endWishPayday;
    private String showIsPayed;
    private String editPayerAcc;
    private String chargeBackType;
    private String isOpenLimit;
}
