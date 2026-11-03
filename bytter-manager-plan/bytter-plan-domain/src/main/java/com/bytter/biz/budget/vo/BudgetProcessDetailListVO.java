package com.bytter.biz.budget.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@Data
public class BudgetProcessDetailListVO implements Serializable {
    String corpName;
    List<FbsBudgetProcessVO> fbsBudgetProcessVOList;
}
