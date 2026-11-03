package com.bytter.biz.budget.vo;

import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FbsBudgetProcessListVO implements Serializable {

    FbsBudgetVO fbsBudgetVO;
    List<FbsBudgetProcessReportVO> reportList;
}
