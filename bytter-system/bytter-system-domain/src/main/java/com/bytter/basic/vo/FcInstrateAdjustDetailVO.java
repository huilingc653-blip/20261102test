package com.bytter.basic.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FcInstrateAdjustDetailVO implements Serializable {
    private FcInstrateAdjustSimpleVO fcInstrateAdjustPage;
    private String corpCode;
    private String poolSign;
    private String    netCode;
    private String  isView;
}
