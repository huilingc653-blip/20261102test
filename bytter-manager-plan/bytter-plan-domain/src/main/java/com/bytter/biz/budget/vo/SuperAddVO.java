package com.bytter.biz.budget.vo;

import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
public class SuperAddVO extends FbsBudgetInputPreVO implements java.io.Serializable {
    /**
     * 计划明细
     */
    private List<FbsBudgetDtlAbsVO> dtlList;
    //获取资金计划上报信息
    private FbsBudgetVO fbsBudgetPage;

    /**
     * 是否是汇总 0-否 1-是
     */
    private String isCollect;
    private String busName;
    private List<FbsBudgetInputPreVO> childCorpData;
    //汇总时是否允许修改直属下级单位
    private String allowEditDirectUderCorp;
    //汇总时是否允许跨级修改下级单位
    private String allowEditUderCorp;
    //汇总时是否允许修改上报金额
    private String allowEditCollectMoney;
    private String forwardUrl;
    private Object resolveList;
    private HashMap<Integer, BigDecimal> months = new HashMap<>();
    private Integer minMonth;
    private HashMap<Integer, BigDecimal> weeks = new HashMap<>();
    private Integer weekNum;
    private Integer minWeek;
    private FbsBudgetSuperadd fbsBudgetSuperaddPage;
    private String taskId;
}
