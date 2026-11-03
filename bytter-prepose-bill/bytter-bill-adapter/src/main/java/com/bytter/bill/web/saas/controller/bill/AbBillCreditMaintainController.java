package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbBillCreditInfoMaintain;
import com.bytter.bill.domain.bill.service.AbBillCreditMaintainService;
import com.bytter.bill.domain.bill.vo.AbBillCreditInfoMaintainVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行开票授信资料维护
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Api(value = "/v1-preposebill/bill/accebill/creditMaintain", tags = "银行开票授信资料维护")
@RestController("v1AbBillCreditMaintainController")
@RequestMapping("/v1-preposebill/bill/accebill/creditMaintain")
public class AbBillCreditMaintainController extends BaseController {

    @Autowired
    private AbBillCreditMaintainService abBillCreditMaintainService;



    /**
     * 银行开票授信资料维护列表 table列表
     */
    @PostMapping("/datatable")
    @ApiOperation("银行开票授信资料维护分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillCreditMaintainService.dataTable(input, this.getCurrentUser());
        return AjaxJson.success(output);
    }



    /**
     * 编辑页面初始化
     */
    @GetMapping("/update")
    @ApiOperation("银行开票授信资料编辑页面初始化")
    public AjaxJson<Map<String, Object>> update(AbBillCreditInfoMaintainVO vo) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(vo.getId())) {
            AbBillCreditInfoMaintain entity = abBillCreditMaintainService.get(vo.getId(), true);
            resultMap.put("creditInfoPage", entity);
        }
        return AjaxJson.success(resultMap);
    }

    /**
     * 保存银行开票授信资料提交
     */
    @LogMessage(content = "保存银行开票授信资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增银行开票授信资料")
    public AjaxJson save(AbBillCreditInfoMaintain form) {
        abBillCreditMaintainService.saveCreditInfo(form, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 银行开票授信资料删除
     */
    @LogMessage(content = "删除银行开票授信资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除银行开票授信资料")
    public AjaxJson delete(@PathVariable("id") String id) {
        abBillCreditMaintainService.delCreditInfo(id);
        return AjaxJson.success();
    }

    /**
     * 编辑银行开票授信资料提交
     */
    @LogMessage(content = "编辑银行开票授信资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改银行开票授信资料")
    public AjaxJson modify(AbBillCreditInfoMaintain form) {
        abBillCreditMaintainService.updateCreditInfo(form, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 银行开票授信资料导入
     */
    @PostMapping("/saveImport")
    @ApiOperation("导入银行开票授信资料")
    public AjaxJson saveImport(@RequestBody List<AbBillCreditInfoMaintain> list) {
        abBillCreditMaintainService.saveImport(list, getCurrentUser());
        return AjaxJson.success();
    }
}