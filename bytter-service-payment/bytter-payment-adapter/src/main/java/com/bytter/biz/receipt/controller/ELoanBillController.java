package com.bytter.biz.receipt.controller;
import com.bytter.biz.receipt.entity.ELoanBill;
import com.bytter.biz.receipt.service.ELoanBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.ELoanBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款电子回单表
 * @date 2017-07-04 15:30:01
 */
@Controller
@RequestMapping("/receipt/eloanbill")
public class ELoanBillController extends BaseController {
    @Autowired
    private ELoanBillService eLoanBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    /**
     * 贷款电子回单表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询贷款电子回单表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listELoanBill";
    }
    /**
     * 贷款电子回单表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return eLoanBillService.listForUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 贷款电子回单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(ELoanBill eLoanBill, Model model) {
        if (StringUtils.isNotEmpty(eLoanBill.getId())) {
            eLoanBill = eLoanBillService.get(eLoanBill.getId());
            model.addAttribute("eLoanBillPage", eLoanBill);
        }
        return "receipt/elecbill/addELoanBill";
    }
    /**
     * 贷款电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(ELoanBill eLoanBill, Model model) {
        if (StringUtils.isNotEmpty(eLoanBill.getId())) {
            eLoanBill = eLoanBillService.get(eLoanBill.getId());
            model.addAttribute("eLoanBillPage", eLoanBill);
        }
        return "receipt/elecbill/updateELoanBill";
    }
    /**
     * 贷款电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        ELoanBillVO eLoanBill = eLoanBillService.getVOByBillNo(null, id);
        eLoanBillService.setReturnMoney(eLoanBill);
        String netId = eLoanBill.getNetId();
        String billType = eLoanBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            model.addAttribute("eVisaTypePage", eVisaType);
        }
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        model.addAttribute("eLoanBillPage", eLoanBill);
        return "receipt/elecbill/viewELoanBill";
    }
    /**
     * 添加贷款电子回单表
     *
     * @param eLoanBill
     * @return
     */
    @LogMessage(content = "添加贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ELoanBill eLoanBill) {
        eLoanBillService.save(eLoanBill);
        return success();
    }
    /**
     * 修改贷款电子回单表
     *
     * @param eLoanBill
     * @return
     */
    @LogMessage(content = "修改贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(ELoanBill eLoanBill) {
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
