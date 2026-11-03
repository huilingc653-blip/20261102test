package com.bytter.credit.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class RatBkrationAdjustSubListVO {
    private List<?> adjustList;
    private Integer total;
    private Integer curr;
    private String businessId;
    private String businessType;
}
