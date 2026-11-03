package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NisBillheadPoolQueryResp implements Serializable {
    private Date endWishPayday;
    private String showIsPayed;
    private String opType;
    private Date showIsAutoPay;
}
