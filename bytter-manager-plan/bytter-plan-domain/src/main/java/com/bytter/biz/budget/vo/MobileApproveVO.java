package com.bytter.biz.budget.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MobileApproveVO implements Serializable {
    private List<FbsBudgetDtlAbsVO> dltList;
    private FbsBudgetVO fbsBudgetPage;
    private String taskId;
}
