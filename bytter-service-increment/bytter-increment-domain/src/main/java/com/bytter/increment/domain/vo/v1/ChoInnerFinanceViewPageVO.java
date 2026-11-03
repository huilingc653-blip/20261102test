package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import lombok.Data;

@Data
public class ChoInnerFinanceViewPageVO {

    private ChoInnerFinanceVO choInnerFinanceRelatedPage;

    private ChoInnerFinanceVO choInnerFinancePage;

    private String busType;
}
