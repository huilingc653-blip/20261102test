package com.bytter.biz.receipt.controller;
import com.bytter.biz.receipt.entity.EConsignBill;
import com.bytter.biz.receipt.service.EConsignBillService;
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
 * @Description: 委托收款电子回单表
 * @date 2017-07-04 15:29:50
 */
@Controller
@RequestMapping("/receipt/econsignbill")
public class EConsignBillController extends BaseController {
    @Autowired
    private EConsignBillService eConsignBillService;
    /**
     * 委托收款电子回单表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询委托收款电子回单表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listEConsignBill";
    }
    /**
     * 委托收款电子回单表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return eConsignBillService.getDataTables(dataTablesInput);
    }
    /**
     * 委托收款电子回单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(EConsignBill eConsignBill, Model model) {
        if (StringUtils.isNotEmpty(eConsignBill.getId())) {
            eConsignBill = eConsignBillService.get(eConsignBill.getId());
            model.addAttribute("eConsignBillPage", eConsignBill);
        }
        return "receipt/elecbill/addEConsignBill";
    }
    /**
     * 委托收款电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(EConsignBill eConsignBill, Model model) {
        if (StringUtils.isNotEmpty(eConsignBill.getId())) {
            eConsignBill = eConsignBillService.get(eConsignBill.getId());
            model.addAttribute("eConsignBillPage", eConsignBill);
        }
        return "receipt/elecbill/updateEConsignBill";
    }
    /**
     * 委托收款电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询委托收款电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EConsignBill eConsignBill = eConsignBillService.get(id);
        model.addAttribute("eConsignBillPage", eConsignBill);
        return "receipt/elecbill/viewEConsignBill";
    }
    /**
     * 添加委托收款电子回单表
     *
     * @param eConsignBill
     * @return
     */
    @LogMessage(content = "添加委托收款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EConsignBill eConsignBill) {
        eConsignBillService.save(eConsignBill);
        return success();
    }
    /**
     * 修改委托收款电子回单表
     *
     * @param eConsignBill
     * @return
     */
    @LogMessage(content = "修改委托收款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(EConsignBill eConsignBill) {
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
