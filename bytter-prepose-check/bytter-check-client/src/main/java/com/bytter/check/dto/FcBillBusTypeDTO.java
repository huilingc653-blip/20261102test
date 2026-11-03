package com.bytter.check.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FcBillBusTypeDTO  implements Serializable {
    private String id;

    private String busType;

    private String isFrom;
}
