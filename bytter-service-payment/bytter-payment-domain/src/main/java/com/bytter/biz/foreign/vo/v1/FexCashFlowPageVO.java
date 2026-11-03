package com.bytter.biz.foreign.vo.v1;

import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("现金流查询数据")
@Data
public class FexCashFlowPageVO {

    private FexCashFlowVO fexCashFlowPage;

    private String busType;

    private FexTranReqVO fexTranReqPage;

    private List custFields;

    private CorpDTO corp;

    private BtBankAccDTO corpBankAcc;

}
