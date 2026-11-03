package com.bytter.biz.vo;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.biz.budget.vo.FbsBudgetInputPreVO;
import com.bytter.biz.budget.vo.FbsBudgetRollMonthWeekVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class FbsBudgetInputContentVO implements Serializable {

    /**
     * 消息
     */
    private String msg;
    /**
     * 时间类型
     */
    private String timeType;
    /**
     * 显示类型
     */
    private String showType;
    /**
     * 周整月计划上报树形展示
     */
    private LinkedHashMap<String, List<FbsBudgetInputPreVO>> map;
    private Integer curPeriod;
    private String layId;
    /**
     * 计划上报树形展示
     */
    private List<FbsBudgetInputPreVO> curDate;
    /**
     * 计划上报树形展示
     */
    private List<FbsBudgetInputPreVO> nextDate;
    /**
     * 原跳转路径
     */
    private String forwardUrl;
    private FbsBudgetVO fbsBudgetPage;
    /**
     * 计划明细
     */
    private List<FbsBudgetDtlAbsVO> dtlList;
    /**
     * 是否是汇总 0-否 1-是
     */
    private Object isCollect;
    private String target;
    private String busName;
    private List<FbsBudgetInputPreVO> childCorpData;
    private String showNetAmount;

    private String allowEditDirectUderCorp;
    //汇总时是否允许跨级修改下级单位
    private String allowEditUderCorp;
    //汇总时是否允许修改上报金额
    private String allowEditCollectMoney;

    private Object resolveList;
    private Object months;
    //mRollSubperiod-月滚动模式 滚动分解子周期
    private int mRollSubperiod;

    private Object weeks;
    private int weekNum;

    /**
     * 计划结束时间
     */
    private Date endDateRoll;

    private List<FbsBudgetRollMonthWeekVO> rollMonthList;
    private List<FbsBudget> fbsBudgetList;

    private List<String> monthsHid;

    private int wRollSubperiod;
    private List<String> weeksHid;
    private List<String> days;


    private List<String> quarters;
    private int qRollSubperiod;
    private List<String> quartersHid;
    private String saveMethod;
    private int minMonth;

    private FbsBudgetSuperadd superaddPage;

    private HashMap<Integer, BigDecimal> resolves;

    private LinkedHashMap<String, List<FbsBudgetDtlAbsVO>> childCorpMap;

    private Integer childCorpSize;

    private List<FbsBudgetDtlAbsVO> dltList;
    private String taskId;
    private String collectFlag;

    private int minWeek;
}
