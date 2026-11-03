package com.bytter.biz.netbank.vo;

import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class NisBillheadPoolSetPayerAccResp implements Serializable {
    private String corpId;
    private FcVoucherTypeVO voucherType;
}
