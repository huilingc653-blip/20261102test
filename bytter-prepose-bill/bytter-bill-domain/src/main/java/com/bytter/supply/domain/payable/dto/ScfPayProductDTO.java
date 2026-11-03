package com.bytter.supply.domain.payable.dto;

import lombok.Data;

@Data
public class ScfPayProductDTO {

    private String platformId;
    private String platformName;
    private String productId;
    private String productName;
    private int sort;
}
