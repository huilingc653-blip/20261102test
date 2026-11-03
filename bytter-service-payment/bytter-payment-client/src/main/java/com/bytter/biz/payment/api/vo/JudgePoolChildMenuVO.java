package com.bytter.biz.payment.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class
JudgePoolChildMenuVO implements Serializable {
    private Boolean hasChildPool;
    private Boolean poolNoUse;
    private Boolean  isUsePool;
    private Boolean   accBalance;
    private Boolean   existFlag;
    private Boolean  useAccFlag;
    private Boolean relevanceFlag;
    private Boolean  childAccFlag;
}
