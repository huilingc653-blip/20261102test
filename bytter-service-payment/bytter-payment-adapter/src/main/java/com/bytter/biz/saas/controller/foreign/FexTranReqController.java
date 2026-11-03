package com.bytter.biz.saas.controller.foreign;

import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.service.*;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.biz.foreign.vo.FexTranOptionDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.foreign.vo.v1.FexTranReqPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.*;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.NetbankMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Controller
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Api(value = "外汇交易", tags = "外汇交易")
@RestController("v1FexTranReqController")
@RequestMapping("/v1-payment/foreign/fextranreq")
public class FexTranReqController extends BaseController {
    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired(required=false)
    private IBtBankAccService btBankAccService;
    @Autowired(required=false)
    private IConfigService configService;
    @Autowired(required=false)
    private IUserService userService;
    @Autowired(required = false)
    private IAccountGrantService accountGrantService;
    @Autowired
    private FexCustFieldRuleService fexCustFieldRuleService;
    @Autowired
    private FexTranCustInfoService fexTranCustInfoService;
    @Autowired
    private FexTranDealService fexTranDealService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private FexProductService fexProductService;
    @DubboReference(check = false)
    private ICalendarService calendarService;
    @Autowired
    private FexTranOptionDealService fexTranOptionDealService;
    @Autowired(required=false)
    private IBisBifInitService bisBifInitService;
    @Autowired(required=false)
    private IBankTypeService bankTypeService;
    @Autowired(required=false)
    private IBtBankAccService bankAccService;
    @Autowired(required=false)
    private ISysCountryCustService sysCountryCustService;

    /**
     * 根据交易类型返回不同的跳转页面
     * @param tranType
     * @return
     */
    public String getForwardByTranType(String tranType){
        /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
        String forward = "";
        if(ForeignConstant.TRAN_TYPE_1.equals(tranType)){
            //外汇买卖交易
            forward = "Foreign";
        }else if(ForeignConstant.TRAN_TYPE_2.equals(tranType)){
            //外汇期权产品
            forward = "Option";
        }else if(ForeignConstant.TRAN_TYPE_3.equals(tranType)){
            //资金调拨交易
            forward = "Allocation";
        }else if(ForeignConstant.TRAN_TYPE_4.equals(tranType)){
            //资金收付交易
            forward = "Pay";
        }else{
            //交易组交易
            forward = "Trade";
        }
        return forward;
    }

    /**
     * 外汇交易表 详情页
     *
     * @return
     */
    @ApiOperation(value = "外汇交易表详情页")
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FexTranReqPageVO> view(@PathVariable("id") String id) {
        FexTranReqPageVO pageVO = new FexTranReqPageVO();
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(id);
        fexTranReqService.injectValue(fexTranReq);
        pageVO.setFexTranReqPage(fexTranReq);
        pageVO.setSysDate(DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        pageVO.setDealer(userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());

        //初始化参数
        pageVO.setBudgetSel(configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BUDGETSEL).getValue());
        pageVO.setPayItem(configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_PAYITEM).getValue());

        //资金计划
        pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_TRAN);
        pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_FEX);
        pageVO.setBusType(FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        pageVO.setCustFields(fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId()));
        pageVO.setCashFlowList(fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        //已违约状态，显示违约信息
        if(ForeignConstant.STATUE_DEFAULT.equals(fexTranReq.getStatus())){
            pageVO.setOpType(ForeignConstant.OP_TYPE_2);
            List<FexTranDealVO> tranDealList = fexTranDealService.listVOByTranReqId(ForeignConstant.OP_TYPE_2, fexTranReq.getId());
            FexTranDealVO vo = new FexTranDealVO();
            for(FexTranDealVO tranDeal:tranDealList){
                if(ForeignConstant.STATUE_CHECK.equals(tranDeal.getStatus())){
                    vo = tranDeal;
                    break;
                }
            }
            pageVO.setFexTranDealPage(vo);
        }
        return success(pageVO);
    }

}
