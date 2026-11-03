package com.bytter.biz.budget.vo;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class FbsBudgetDetailViewVO extends FbsBudgetDetailVO {
    private String flag;
    private Object budgetDtl;
    private List<FbsBudgetDetailVO> dataList;
    private FbsBudget budget;
    private List<String> dtlColList;
    private List<String> dtlColValueList;
    private String beginDate;
    private String endDate;
    private FbsItemDTO inputItem;
    //原系统跳转URL
    private String forwardUrl;
}
