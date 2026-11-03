package com.bytter.biz.netbank.dto;

import com.bytter.biz.netbank.entity.FcReceiptType;
import lombok.Data;

import java.io.Serializable;

@Data
public class FcReceiptTypeDTO extends FcReceiptType implements Serializable {
    /**有效标志*/
    private String validSignName;
}
