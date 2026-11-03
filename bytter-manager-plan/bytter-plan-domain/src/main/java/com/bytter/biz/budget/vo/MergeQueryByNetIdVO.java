package com.bytter.biz.budget.vo;

import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MergeQueryByNetIdVO {
    private String netId;
    private String timeType;
    private String beginDate;
    private String showItem;
    private String showMoney;
    private String maxLevelShow;
    private List<CorpDTO> corpDate;
    private CorpDTO topCorp;
    private String corpId;
    private Date seachBeginDate;
    private Date seachEndDate;
    private String corpName;
    private BtCurrencyDTO btCurrencyDTO;
    private String name;
    private String html;
    private String collectName;
    private Object timeMap;
    private Object itemList;
    private Object dtlMap;
    private Object colSize;
    private BtCurrencyDTO cur;
    private Boolean bMoneyShow;
    private Boolean bsuperMoneyShow;
    private Boolean btempMoneyShow;
    private Object corpMap;
    private Object maxDateCount;
    private Object itemMap;
    private Object moneyMap;
    private Object collectMap;
    private String current;
    private String year;
    private Map weekInMonth;
    private String forwardUrl;
}
