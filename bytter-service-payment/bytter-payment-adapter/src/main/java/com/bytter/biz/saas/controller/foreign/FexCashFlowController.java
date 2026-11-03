package com.bytter.biz.saas.controller.foreign;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.service.*;
import com.bytter.biz.foreign.vo.*;
import com.bytter.biz.foreign.vo.v1.FexCashFlowPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.*;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 现金流表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/foreign/fexcashflow", tags = "工资册")
@RestController("v1FexCashFlowController")
@RequestMapping("/v1-payment/foreign/fexcashflow")
public class FexCashFlowController extends BaseController {
    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired
    private FexCashFlowCustInfoService fexCashFlowCustInfoService;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private FexSettleFillRuleAddService fexSettleFillRuleAddService;
    @Autowired(required = false)
    private IAccountGrantService accountGrantService;
    @Autowired(required = false)
    private IBisAccDtlService bisAccDtlService;


    /**
     * 外汇现金流表 table列表
     *
     * @return
     */
    @ApiOperation(value = "外汇现金流表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fexCashFlowService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 外汇买卖交易现金流 详情页
     *
     * @return
     */
    @ResponseBody
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FexCashFlowPageVO> view(@PathVariable("id") String id) {
        FexCashFlowPageVO pageVO = new FexCashFlowPageVO();
        FexCashFlowVO vo = new FexCashFlowVO();
        String foward = "Pay";
        if (StringUtils.isNotEmpty(id)) {
            vo = this.fexCashFlowService.getByIdUnion(id);
            pageVO.setFexCashFlowPage(vo);
            pageVO.setBusType(FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW);
            FexTranReqVO fexTranReq = new FexTranReqVO();
            if(vo!=null && StringUtils.isNotBlank(vo.getTranBillId())){
                fexTranReq = fexTranReqService.getByIdUnion(vo.getTranBillId());
            }
            pageVO.setFexTranReqPage(fexTranReq);
            List<FexCashFlowCustInfoVO> list = fexCashFlowCustInfoService.listCustInfoByBusId(vo.getId());
            if(EmptyUtils.isEmpty(list)){
                FexSettleFillRuleVO searchVO = new FexSettleFillRuleVO();
                searchVO.setCorpId(vo.getCorpId());
                searchVO.setCashFlowMethod(vo.getSettleType());
                searchVO.setCotpId(vo.getCotpId());
                searchVO.setTranType(vo.getTranType());
                searchVO.setAccId(vo.getCorpAccId());
                searchVO.setCountryId(fexTranReq.getCountryId());
                List<FexSettleFillRuleAdd> custFields = fexSettleFillRuleAddService.listRuleAddByRule(searchVO);
                pageVO.setCustFields(custFields);
            }else {
                pageVO.setCustFields(list);
            }
            pageVO.setCorp(corpService.get(vo.getCorpId()));
        }
        return success(pageVO);
    }

    @GetMapping("/choose")
    public AjaxJson choose(String id) {
        BisAccDtlDTO bisAccDtlDTO = new BisAccDtlDTO();
        if(StringUtils.isNotBlank(id)){
            bisAccDtlDTO = bisAccDtlService.getById(id);
            return success(bisAccDtlDTO);
        }
        return success();
    }
}
