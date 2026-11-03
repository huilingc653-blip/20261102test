package com.bytter.biz.foreign.vo.v1;

import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("外汇交易表详情页")
public class FexTranReqPageVO {

    private FexTranReqVO fexTranReqPage;

    private String sysDate;

    private String dealer;

    private String budgetSel;

    private String payItem;

    private String businessName;

    private String processSysname;

    private String busType;

    private List<FexTranCustInfoVO> custFields;

    private List<FexCashFlowVO> cashFlowList;

    private String opType;

    private FexTranDealVO fexTranDealPage;
}
