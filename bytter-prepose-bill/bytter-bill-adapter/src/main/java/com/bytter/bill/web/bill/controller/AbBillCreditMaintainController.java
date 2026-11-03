package com.bytter.bill.web.bill.controller;

import com.bytter.bill.domain.bill.entity.AbBillCreditInfoMaintain;
import com.bytter.bill.domain.bill.service.AbBillCreditMaintainService;
import com.bytter.bill.domain.bill.vo.AbBillCreditInfoMaintainVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 银行开票授信资料维护
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Controller
@RequestMapping("/bill/accebill/creditMaintain")
public class AbBillCreditMaintainController extends BaseController {

    @Autowired
    private AbBillCreditMaintainService abBillCreditMaintainService;

    /**
     * 银行开票授信资料维护列表 页面跳转
     *
     * @return 银行开票授信资料维护列表跳转地址
     */
    @LogMessage(content = "银行开票授信资料维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillCreditMaintain";
    }

    /**
     * 银行开票授信资料维护列表 table列表
     *
     * @return 出参
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillCreditMaintainService.dataTable(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 银行开票授信资料维护列表新增页面跳转
     *
     * @return 银行开票授信资料维护列表跳转地址
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "bill/accebill/addAbBillCreditMaintain";
    }

    /**
     * 银行开票授信资料维护列表编辑页面跳转
     *
     * @return 银行开票授信资料维护列表跳转地址
     */
    @GetMapping("/update")
    public String update(AbBillCreditInfoMaintainVO vo, Model model) {
        if (StringUtils.isNotEmpty(vo.getId())) {
            AbBillCreditInfoMaintain entity = abBillCreditMaintainService.get(vo.getId(), true);
            model.addAttribute("creditInfoPage", entity);
        }
        return "bill/accebill/updateAbBillCreditMaintain";
    }

    /**
     * 保存银行开票授信资料提交
     *
     * @param form 产品赎回表单
     * @return 操作结果
     */
    @LogMessage(content = "保存银行开票授信资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbBillCreditInfoMaintain form) {
        abBillCreditMaintainService.saveCreditInfo(form, this.getCurrentUser());
        return success();
    }

    /**
     * 银行开票授信资料删除
     *
     * @param id 产品赎回主键id
     * @return 操作结果
     */
    @LogMessage(content = "删除银行开票授信资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        abBillCreditMaintainService.delCreditInfo(id);
        return success();
    }

    /**
     * 编辑银行开票授信资料提交
     *
     * @param form 产品赎回表单
     * @return 操作结果
     */
    @LogMessage(content = "编辑银行开票授信资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(AbBillCreditInfoMaintain form) {
        abBillCreditMaintainService.updateCreditInfo(form, this.getCurrentUser());
        return success();
    }

    /**
     * 银行开票授信资料导入
     *
     * @param list 银行开票授信资料集合
     * @return 操作结果
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<AbBillCreditInfoMaintain> list) {
        abBillCreditMaintainService.saveImport(list, getCurrentUser());
        return success();
    }
}
