package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.BillAccKeepAmt;
import com.bytter.bill.domain.bill.service.BillAccKeepAmtService;
import com.bytter.bill.domain.bill.vo.BillAccKeepAmtVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 银承账户留底金额配置
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/3/26
 */
@Api(value = "/v1-preposebill/bill/accebill/accKeepAmt", tags = "银承账户留底金额配置")
@RestController("v1BillAccKeepAmtController")
@RequestMapping("/v1-preposebill/bill/accebill/accKeepAmt")
public class BillAccKeepAmtController extends BaseController {

    @Autowired
    private BillAccKeepAmtService billAccKeepAmtService;

    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;

    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 银承账户留底金额配置 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询银承账户留底金额配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询银承账户留底金额配置分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = billAccKeepAmtService.getDataTables(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 保存银承账户留底金额配置
     */
    @LogMessage(content = "添加银承账户留底金额配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存银承账户留底金额配置")
    public AjaxJson save(@RequestBody BillAccKeepAmtVO accKeepAmtVO) {
        billAccKeepAmtService.save(accKeepAmtVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改银承账户留底金额配置
     */
    @LogMessage(content = "修改银承账户留底金额配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改银承账户留底金额配置")
    public AjaxJson modify(@RequestBody BillAccKeepAmtVO accKeepAmtVO) {
        billAccKeepAmtService.modify(accKeepAmtVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除银承账户留底金额配置
     */
    @LogMessage(content = "删除银承账户留底金额配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除银承账户留底金额配置")
    public AjaxJson delete(@PathVariable("id") String id) {
        BillAccKeepAmt policyAgreement = billAccKeepAmtService.get(id);
        billAccKeepAmtService.deleteEntity(policyAgreement);
        return AjaxJson.success();
    }

    /**
     * 批量禁用银承账户留底金额配置
     */
    @LogMessage(content = "批量禁用银承账户留底金额配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchForbidden")
    @ApiOperation("批量禁用银承账户留底金额配置")
    public AjaxJson batchForbidden(@RequestBody BillAccKeepAmtVO vo) {
        billAccKeepAmtService.batchForbidden(vo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 导入银承账户留底金额配置
     */
    @LogMessage(content = "导入银承账户留底金额配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ApiOperation("导入银承账户留底金额配置")
    public AjaxJson saveImport(@RequestBody List<BillAccKeepAmtVO> list) {
        billAccKeepAmtService.saveImport(list, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 获取付款账户查询列表
     */
    @PostMapping("/getPayerAccQuery")
    @ApiOperation("获取付款账户查询列表")
    public AjaxJson<List<BtBankAccDTO>> getPayerAccQuery(@RequestBody Map<String, String> params) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        String resource = params.get("resource");
        String corpId = params.get("corpId");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("accType", BtBankAccConstant.ACCTYPE_10);// 活期票据专户
        // 获取转账权限的银行账号
        List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(
                this.getUserId(), corpId, resource, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT, queryParams);
        for (BtBankAccDTO dto : listBankAccTemp) {
            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
            listAcc.add(dto);
        }
        return AjaxJson.success(listAcc);
    }
}