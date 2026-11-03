package com.bytter.biz.vo;

import com.bytter.biz.budget.vo.FbsBudgetProcessCommonVO;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 资金计划执行编辑页面数据
 */
@Data
public class EditItemDetailVO implements Serializable {

    private String msg;
    private List<FbsBudgetProcessCommonVO> listProcess;


    private String corpCode;
    private FbsBudgetProcessVO fbsBudgetProcessPage;
}
