package com.bytter.biz.budget.vo;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class BudgetProcessReportVO implements java.io.Serializable {
    private Date beginDate;
    private Date endDate;
    private FbsBudget fbsBudget;
    private FbsBudgetVO fbsBudgetPage;
    private List<FbsBudgetProcessReportVO> reportList;
    private String itemCode;
    private List<FbsBudgetDtlAbsVO> dtlList;
    private List<Map<String, BigDecimal>> addList;
    private List<String> titleList;
    private String corpId;
    private String timeType;
    private String corpName;
    private String  forwardUrl;

}
