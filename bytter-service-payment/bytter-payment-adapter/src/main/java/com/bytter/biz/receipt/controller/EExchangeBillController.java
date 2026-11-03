package com.bytter.biz.receipt.controller;
import com.bytter.biz.receipt.entity.EExchangeBill;
import com.bytter.biz.receipt.service.EExchangeBillService;
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
 * @Description: 内部结售汇电子回单
 * @date 2017-07-04 15:30:33
 */
@Controller
@RequestMapping("/receipt/eexchangebill")
public class EExchangeBillController extends BaseController {
    @Autowired
    private EExchangeBillService eExchangeBillService;
    /**
     * 内部结售汇电子回单列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询内部结售汇电子回单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listEExchangeBill";
    }
    /**
     * 内部结售汇电子回单 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return eExchangeBillService.getDataTables(dataTablesInput);
    }
    /**
     * 内部结售汇电子回单新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(EExchangeBill eExchangeBill, Model model) {
        if (StringUtils.isNotEmpty(eExchangeBill.getId())) {
            eExchangeBill = eExchangeBillService.get(eExchangeBill.getId());
            model.addAttribute("eExchangeBillPage", eExchangeBill);
        }
        return "receipt/elecbill/addEExchangeBill";
    }
    /**
     * 内部结售汇电子回单编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(EExchangeBill eExchangeBill, Model model) {
        if (StringUtils.isNotEmpty(eExchangeBill.getId())) {
            eExchangeBill = eExchangeBillService.get(eExchangeBill.getId());
            model.addAttribute("eExchangeBillPage", eExchangeBill);
        }
        return "receipt/elecbill/updateEExchangeBill";
    }
    /**
     * 内部结售汇电子回单详情页
     *
     * @return
     */
    @LogMessage(content = "查询内部结售汇电子回单详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EExchangeBill eExchangeBill = eExchangeBillService.get(id);
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        model.addAttribute("eExchangeBillPage", eExchangeBill);
        return "receipt/elecbill/viewEExchangeBill";
    }
    /**
     * 添加内部结售汇电子回单
     *
     * @param eExchangeBill
     * @return
     */
    @LogMessage(content = "添加内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EExchangeBill eExchangeBill) {
        eExchangeBillService.save(eExchangeBill);
        return success();
    }
    /**
     * 修改内部结售汇电子回单
     *
     * @param eExchangeBill
     * @return
     */
    @LogMessage(content = "修改内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(EExchangeBill eExchangeBill) {
        EExchangeBill t = eExchangeBillService.get(eExchangeBill.getId());
        BeanUtils.copyNotNullProperties(eExchangeBill, t);
        eExchangeBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除内部结售汇电子回单
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EExchangeBill eExchangeBill = eExchangeBillService.get(id);
        eExchangeBillService.deleteEntity(eExchangeBill);
        return success();
    }
    /**
     * 批量删除内部结售汇电子回单
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EExchangeBill eExchangeBill = eExchangeBillService.get(id);
            eExchangeBillService.deleteEntity(eExchangeBill);
        }
        return success();
    }
}
