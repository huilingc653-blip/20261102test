package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.entity.EConsignBill;
import com.bytter.biz.receipt.service.EConsignBillService;
import com.bytter.biz.receipt.vo.EConsignBillPage;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托收款电子回单表
 * @date 2017-07-04 15:29:50
 */
@Api(value = "/v1-payment/receipt/econsignbill",tags = "委托收款电子回单表")
@RestController("v1EConsignBillController")
@RequestMapping("/v1-payment/receipt/econsignbill")
public class EConsignBillController extends BaseController {
    @Autowired
    private EConsignBillService eConsignBillService;

    /**
     * 委托收款电子回单表 table列表
     *
     * @return
     */
    @ApiOperation(value = "委托收款电子回单表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eConsignBillService.getDataTables(input));
    }

    /**
     * 委托收款电子回单表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("编辑页面跳转前页面获取")
    @GetMapping("/update")
    public AjaxJson<EConsignBillPage> update(String id) {
        EConsignBillPage billPage = new EConsignBillPage();
        if (StringUtils.isNotEmpty(id)) {
            EConsignBill eConsignBill = eConsignBillService.get(id);
            billPage.setEConsignBillPage(eConsignBill);
        }
        return success(billPage);
    }
    /**
     * 委托收款电子回单表详情页
     *
     * @return
     */
    @ApiOperation("查询委托收款电子回单表详情")
    @LogMessage(content = "查询委托收款电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<EConsignBillPage> view(@PathVariable("id") String id, @PathVariable("opType") String opType) {
        EConsignBillPage billPage = new EConsignBillPage();
        EConsignBill eConsignBill = eConsignBillService.get(id);
        billPage.setEConsignBillPage(eConsignBill);
        return success(billPage);
    }
    /**
     * 添加委托收款电子回单表
     *
     * @param eConsignBill
     * @return
     */
    @ApiOperation("添加委托收款电子回单表")
    @LogMessage(content = "添加委托收款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody EConsignBill eConsignBill) {
        eConsignBillService.save(eConsignBill);
        return success();
    }
    /**
     * 修改委托收款电子回单表
     *
     * @param eConsignBill
     * @return
     */
    @ApiOperation("修改委托收款电子回单表")
    @LogMessage(content = "修改委托收款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody EConsignBill eConsignBill) {
        EConsignBill t = eConsignBillService.get(eConsignBill.getId());
        BeanUtils.copyNotNullProperties(eConsignBill, t);
        eConsignBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除委托收款电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除委托收款电子回单表")
    @LogMessage(content = "删除委托收款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EConsignBill eConsignBill = eConsignBillService.get(id);
        eConsignBillService.deleteEntity(eConsignBill);
        return success();
    }
    /**
     * 批量删除委托收款电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除委托收款电子回单表")
    @LogMessage(content = "批量删除委托收款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EConsignBill eConsignBill = eConsignBillService.get(id);
            eConsignBillService.deleteEntity(eConsignBill);
        }
        return success();
    }
}
