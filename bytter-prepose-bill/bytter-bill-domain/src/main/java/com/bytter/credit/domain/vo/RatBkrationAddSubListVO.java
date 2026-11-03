package com.bytter.credit.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class RatBkrationAddSubListVO {
    private List<?> rations;
    private Integer total;
    private Integer curr;
    private RatBkrationAddListVO addVO;
}
