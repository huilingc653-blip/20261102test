package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillTransferCollectionService;
import com.bytter.bill.domain.bill.vo.AbBillTransferCollectionVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据归集
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilltransfercollection", tags = "票据归集")
@RestController("v1AbBillTransferCollectionController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilltransfercollection")
public class AbBillTransferCollectionController extends BaseController {
    @Autowired
    private AbBillTransferCollectionService abBillTransferCollectionService;


    /**
     * 应收-票据划拨-票据归集 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询可票据归集的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("查询可票据归集的票据分页列表")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = abBillTransferCollectionService.getDataTablesUnion(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据归集
     */
    @LogMessage(content = "票据归集", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/transfercollection")
    @ApiOperation("执行票据归集操作")
    public AjaxJson transfercollection(AbBillTransferCollectionVo abBillTransferCollectionVo) {
        abBillTransferCollectionService.transfercollection(abBillTransferCollectionVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 根据网点id查询银行账号信息
     */
    @GetMapping("/getCorpIdByNetId")
    @ApiOperation("根据网点ID查询单位信息")
    public AjaxJson getCorpIdByNetId(String netId) {
        List<CorpDTO> list = abBillTransferCollectionService.getCorpIdByNetId(netId);
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setObj(list);
        return ajaxJson;
    }

    /**
     * 根据网点id查询直属下级单位的银行账号信息
     */
    @GetMapping("/getBankAccByNetId")
    @ApiOperation("根据网点ID查询下级单位银行账号")
    public AjaxJson getBankAccByNetId(String netId) {
        List<BtBankAccDTO> list = abBillTransferCollectionService.getBankAccByNetId(netId);
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setObj(list);
        return ajaxJson;
    }

    /**
     * 根据网点id查询网点的银行账号信息
     */
    @GetMapping("/getNetBankAccByNetId")
    @ApiOperation("根据网点ID查询网点自身银行账号")
    public AjaxJson getNetBankAccByNetId(String netId) {
        List<BtBankAccDTO> list = abBillTransferCollectionService.getNetBankAccByNetId(netId);
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setObj(list);
        return ajaxJson;
    }



    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("票据归集审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferCollectionService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据归集审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferCollectionVo vo = abBillTransferCollectionService.getAbBillTransferCollectionVoById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbbilltransfercollection");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("票据归集移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferCollectionVo vo = abBillTransferCollectionService.getAbBillTransferCollectionVoById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(dataMap);
    }
}