package com.bytter.system.web.controller;
import com.bytter.basic.entity.BtBank;
import com.bytter.basic.service.BtBankService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK
 * @date 2017-07-20 14:28:41
 */
@Controller
@RequestMapping("/basic/business/btbank")
public class BtBankController extends BaseController {
    @Autowired
    private BtBankService btBankService;
    /**
     * BT_BANK列表 页面跳转
     *
     * @return
     */
    //
    @LogMessage(content = "查询BT_BANK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtBank";
    }
    /**
     * BT_BANK table列表
     *
     * @return
     */
    //
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankService.getDataTables(dataTablesInput);
    }
    /**
     * BT_BANK新增页面跳转
     *
     * @return
     */
    //
    @GetMapping("/add")
    public String add(BtBank btBank, Model model) {
        if (StringUtils.isNotEmpty(btBank.getId())) {
            btBank = btBankService.get(btBank.getId());
            model.addAttribute("btBankPage", btBank);
        }
        return "basic/business/addBtBank";
    }
    /**
     * BT_BANK编辑页面跳转
     *
     * @return
     */
    //
    @GetMapping("/update")
    public String update(BtBank btBank, Model model) {
        if (StringUtils.isNotEmpty(btBank.getId())) {
            btBank = btBankService.get(btBank.getId());
            model.addAttribute("btBankPage", btBank);
        }
        return "basic/business/updateBtBank";
    }
    /**
     * BT_BANK详情页
     *
     * @return
     */
    //
    @LogMessage(content = "查询BT_BANK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtBank btBank = btBankService.get(id);
        model.addAttribute("btBankPage", btBank);
        return "basic/business/viewBtBank";
    }
    /**
     * 添加BT_BANK
     *
     * @param btBank
     * @return
     */
    //
    @LogMessage(content = "添加BT_BANK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtBank btBank) {
        btBankService.save(btBank);
        return success();
    }
    /**
     * 修改BT_BANK
     *
     * @param btBank
     * @return
     */
    //
    @LogMessage(content = "修改BT_BANK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtBank btBank) {
        BtBank t = btBankService.get(btBank.getId());
        BeanUtils.copyNotNullProperties(btBank, t);
        btBankService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除BT_BANK
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除BT_BANK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtBank btBank = btBankService.get(id);
        btBankService.deleteEntity(btBank);
        return success();
    }
    /**
     * 批量删除BT_BANK
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除BT_BANK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtBank btBank = btBankService.get(id);
            btBankService.deleteEntity(btBank);
        }
        return success();
    }
}
