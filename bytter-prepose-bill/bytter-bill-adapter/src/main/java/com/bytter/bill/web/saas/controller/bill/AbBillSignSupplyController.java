package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillSignSupplyService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillSignSupplyVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 签收补录Controller
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillSignSupply", tags = "签收补录")
@RestController("v1AbBillSignSupplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillSignSupply")
public class AbBillSignSupplyController extends BaseController {
    @Autowired
    private AbBillSignSupplyService abBillSignSupplyService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;



    /**
     * 签收补录信息 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("查询签收补录信息分页列表")
    @LogMessage(content = "查询签收补录信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillSignSupplyService.listAbBillSignSupplyDaoInfo(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 签收补录操作
     */
    @PostMapping("/signSupply")
    @ApiOperation("签收补录操作")
    @LogMessage(content = "签收补录操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson signSupply(@RequestBody AbBillSignSupplyVo abBillSignSupplyVo) {
        abBillSignSupplyService.signSupply(abBillSignSupplyVo, this.getCurrentUser());
        return AjaxJson.success();
    }


    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("签收补录审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillSignSupplyService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("签收补录审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(10);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillSignSupplyVo info = abBillSignSupplyService.getAbBillSignSupplyVoById(businessId);
            dataMap.put("info", info);
            dataMap.put("isApprove", true);
            String bankTypeName = StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null;
            dataMap.put("bankTypeName", bankTypeName);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_REC);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillReceivebiz");
        return AjaxJson.success(dataMap);
    }
}