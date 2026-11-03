package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.entity.ELoanBill;
import com.bytter.biz.receipt.service.ELoanBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.ELoanBillEntityVO;
import com.bytter.biz.receipt.vo.ELoanBillPageVO;
import com.bytter.biz.receipt.vo.ELoanBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款电子回单表
 * @date 2017-07-04 15:30:01
 */
@Api(value = "/v1-payment/receipt/eloanbill", tags = "贷款电子回单表数据")
@RestController("v1ELoanBillController")
@RequestMapping("/v1-payment/receipt/eloanbill")
public class ELoanBillController extends BaseController {
    @Autowired
    private ELoanBillService eLoanBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;

    /**
     * 贷款电子回单表 table列表
     *
     * @return
     */
    @ApiOperation(value = "查询贷款电子回单表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eLoanBillService.listForUnion(input, this.getCurrentUser()));
    }

    /**
     * 贷款电子回单表编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "贷款电子回单表编辑页面数据查询")
    @GetMapping("/update")
    public AjaxJson<ELoanBillEntityVO> update(String id) {
        ELoanBillEntityVO billPage = new ELoanBillEntityVO();
        if (StringUtils.isNotEmpty(id)) {
            ELoanBill eLoanBill = eLoanBillService.get(id);
            billPage.setELoanBillPage(eLoanBill);
            return success(billPage);
        }
        return success();
    }
    /**
     * 贷款电子回单表详情页
     *
     * @return
     */
    @ApiOperation(value = "查询贷款电子回单表详情")
    @LogMessage(content = "查询贷款电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<ELoanBillPageVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType) {
        ELoanBillPageVO pageVO = new ELoanBillPageVO();
        ELoanBillVO eLoanBill = eLoanBillService.getVOByBillNo(null, id);
        eLoanBillService.setReturnMoney(eLoanBill);
        String netId = eLoanBill.getNetId();
        String billType = eLoanBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            pageVO.setEVisaTypePage(eVisaType);
        }
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        pageVO.setELoanBillPage(eLoanBill);
        return success(pageVO);
    }
    /**
     * 添加贷款电子回单表
     *
     * @param eLoanBill
     * @return
     */
    @ApiOperation(value = "添加贷款电子回单表")
    @LogMessage(content = "添加贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ELoanBill eLoanBill) {
        eLoanBillService.save(eLoanBill);
        return success();
    }
    /**
     * 修改贷款电子回单表
     *
     * @param eLoanBill
     * @return
     */
    @ApiOperation(value = "修改贷款电子回单表")
    @LogMessage(content = "修改贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ELoanBill eLoanBill) {
        ELoanBill t = eLoanBillService.get(eLoanBill.getId());
        BeanUtils.copyNotNullProperties(eLoanBill, t);
        eLoanBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除贷款电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除贷款电子回单表")
    @LogMessage(content = "删除贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ELoanBill eLoanBill = eLoanBillService.get(id);
        eLoanBillService.deleteEntity(eLoanBill);
        return success();
    }
    /**
     * 批量删除贷款电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除贷款电子回单表")
    @LogMessage(content = "批量删除贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ELoanBill eLoanBill = eLoanBillService.get(id);
            eLoanBillService.deleteEntity(eLoanBill);
        }
        return success();
    }
}
