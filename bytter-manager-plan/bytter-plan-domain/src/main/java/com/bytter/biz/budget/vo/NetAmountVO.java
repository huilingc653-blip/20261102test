package com.bytter.biz.budget.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NetAmountVO implements Serializable {

    private FbsBudgetVO fbsBudget;
    private List<FbsBudgetInputPreVO> childCorpData;
    private String showNetAmount;
}
