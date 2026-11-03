package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import lombok.Data;

@Data
public class ChoInnerFinanceAddVO {

    private ChoInnerFinanceVO choInnerFinancePage;

    private ChoInnerFinanceVO choInnerFinanceRelatedPage;

    private Integer busType;

    private String sysDate;

    private String regularOnset;

    private String regularKeep;

    private String noticeOnset;

    private String noticeKeep;

    private String forwardUrl;

    private String flowStatus;

    private String cycleTime;

    private String taskId;
}
