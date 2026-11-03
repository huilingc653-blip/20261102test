package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.bytter.supply.domain.common.service.ScfInvoiceInfoService;
import com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfInvoiceInfoController
 * @Description: 应付/应收债券发票信息控制器
 * @since 2026/7/3
 */
@Controller
@RequestMapping("/v1-supply/invoice")
@Api(value = "/v1-supply/invoice",tags = "应付/应收债券发票信息控制器")
public class ScfInvoiceInfoController extends BaseController {

    @Resource
    private ScfInvoiceInfoService scfInvoiceInfoService;

    /**
     * 查询发票信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public AjaxJson<DataTablesOutput<ScfInvoiceInfoVO>> list(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(scfInvoiceInfoService.dataTable(input));
    }

    /**
     * 新增发票信息
     */
    @LogMessage(content = "新增发票信息", type = com.bytter.core.constant.Globals.Log_Type_INSERT, level = com.bytter.core.constant.Globals.Log_Leavel_INFO)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody @Valid ScfInvoiceInfoDTO invoiceInfoDTO) {
        scfInvoiceInfoService.saveInvoiceInfo(invoiceInfoDTO);
        return AjaxJson.success();
    }

    /**
     * 修改发票信息
     */
    @LogMessage(content = "修改发票信息", type = com.bytter.core.constant.Globals.Log_Type_UPDATE, level = com.bytter.core.constant.Globals.Log_Leavel_INFO)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody ScfInvoiceInfoDTO invoiceInfoDTO) {
        if (StringUtils.isEmpty(invoiceInfoDTO.getId())) {
            return AjaxJson.failed("主键ID不能为空");
        }
        ScfInvoiceInfo invoiceInfo = scfInvoiceInfoService.getInvoiceInfoById(invoiceInfoDTO.getId());
        if (invoiceInfo == null) {
            return AjaxJson.failed("记录不存在");
        }
        BeanUtils.copyNotNullProperties(invoiceInfoDTO, invoiceInfo);
        scfInvoiceInfoService.updateInvoiceInfo(invoiceInfo);
        return AjaxJson.success();
    }

    /**
     * 删除发票信息
     */
    @LogMessage(content = "删除发票信息", type = com.bytter.core.constant.Globals.Log_Type_DEL, level = com.bytter.core.constant.Globals.Log_Leavel_INFO)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        scfInvoiceInfoService.deleteInvoiceInfo(id);
        return AjaxJson.success();
    }

    /**
     * 根据主键查询详情
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson view(@PathVariable("id") String id) {
        ScfInvoiceInfoVO invoiceInfo = scfInvoiceInfoService.getInvoiceInfo(id);
        return AjaxJson.success(invoiceInfo);
    }
}
