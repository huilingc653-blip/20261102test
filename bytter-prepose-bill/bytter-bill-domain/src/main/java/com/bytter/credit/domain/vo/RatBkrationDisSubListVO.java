package com.bytter.credit.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class RatBkrationDisSubListVO {
    private List<?> rations;
    private Integer total;
    private Integer curr;
    private RatBkrationDisVO disVO;
}
