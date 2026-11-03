package com.bytter.biz.bankacc.controller;

import com.bytter.biz.inneraccount.entity.BtBankAccFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC_FREEZE
 * @date 2017-06-14 14:26:46
 */
@Controller
@RequestMapping("/bankacc/btbankaccfreeze")
public class BtBankAccFreezeController extends BaseController {
    @Autowired
    private BtBankAccFreezeService btBankAccFreezeService;
    /**
     * BT_BANK_ACC_FREEZE列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_QUERY)
    @LogMessage(content = "查询BT_BANK_ACC_FREEZE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bankacc/listBtBankAccFreeze";
    }
    /**
     * BT_BANK_ACC_FREEZE table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccFreezeService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * BT_BANK_ACC_FREEZE新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @GetMapping("/add")
    public String add(BtBankAccFreezeVO vo, Model model) {
        btBankAccFreezeService.toAddFreeze(vo, model);
        return "bankacc/addBtBankAccFreeze";
    }
    /**
     * BT_BANK_ACC_FREEZE编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @GetMapping("/update")
    public String update(BtBankAccFreezeVO vo, Model model) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(vo.getId());
        BeanUtils.copyNotNullProperties(dto, vo);
        btBankAccFreezeService.toUpdateFreeze(vo, model);
        return "bankacc/updateBtBankAccFreeze";
    }
    /**
     * BT_BANK_ACC_FREEZE详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_QUERY)
    @LogMessage(content = "查询BT_BANK_ACC_FREEZE详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id, true);
        BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
        BeanUtils.copyNotNullProperties(dto, vo);
        btBankAccFreezeService.toAddFreeze(vo, model);
        model.addAttribute("freezeMoney", dto.getFreezeMoney());
        model.addAttribute("freezeRession", dto.getFreezeRession());
        return "bankacc/viewBtBankAccFreeze";
    }
    /**
     * 添加BT_BANK_ACC_FREEZE
     *
     * @param btBankAccFreeze
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @LogMessage(content = "添加BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/freezeSaveTemp")
    @ResponseBody
    public AjaxJson freezeSaveTemp(BtBankAccFreeze btBankAccFreeze) {
        btBankAccFreezeService.freezeSaveTemp(btBankAccFreeze, this.getCurrentUser());
        return success();
    }
    /**
     * 冻结提交
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @LogMessage(content = "添加BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/freezeSave")
    @ResponseBody
    public AjaxJson freezeSave(String ids) {
        btBankAccFreezeService.toSubmit(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 修改BT_BANK_ACC_FREEZE
     *
     * @param btBankAccFreeze
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @LogMessage(content = "修改BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtBankAccFreeze btBankAccFreeze) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(btBankAccFreeze.getId());
        dto.setUpdateBy(this.getCurrentUser().getLoginName());
        dto.setUpdateDate(DateUtils.getDate());
        dto.setFreezeMoney(btBankAccFreeze.getFreezeMoney());
        dto.setFreezeRession(btBankAccFreeze.getFreezeRession());
        btBankAccFreezeService.saveOrUpdate(dto);
        return success();
    }
    /**
     * 删除BT_BANK_ACC_FREEZE
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @LogMessage(content = "删除BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id);
        dto.setStatus(BtBankAccConstant.STATUE_DEL);
        btBankAccFreezeService.saveOrUpdate(dto);
        return success();
    }
    /**
     * 批量删除BT_BANK_ACC_FREEZE
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_APPLY)
    @LogMessage(content = "批量删除BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id);
            btBankAccFreezeService.deleteEntity(dto);
        }
        return success();
    }
    @PostMapping("/getTempFreeze")
    @ResponseBody
    public List<BtBankAccFreezeVO> getPrentBankAccList(String bankAccId, String curId) {
        return btBankAccFreezeService.getTempFreeze(bankAccId, curId);
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bankacc/listBtBankAccFreezeApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.btBankAccFreezeService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            BtBankAccFreezeDTO dto = this.btBankAccFreezeService.get(id, true);
            BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
            BeanUtils.copyNotNullProperties(dto, vo);
            btBankAccFreezeService.toAddFreeze(vo, model);
            model.addAttribute("taskId", taskId);
            model.addAttribute("freezeMoney", dto.getFreezeMoney());
            model.addAttribute("freezeRession", dto.getFreezeRession());
        }
        return "bankacc/approveBtBankAccFreeze";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            BtBankAccFreezeDTO dto = this.btBankAccFreezeService.get(businessId, true);
            BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
            BeanUtils.copyNotNullProperties(dto, vo);
            btBankAccFreezeService.toAddFreeze(vo, model);
            model.addAttribute("taskId", taskId);
            model.addAttribute("freezeMoney", dto.getFreezeMoney());
            model.addAttribute("freezeRession", dto.getFreezeRession());
            model.addAttribute("freeze", dto);
        }
        return "bankacc/viewApproveBtBankAccFreeze";
    }
    /**
     * BT_BANK_ACC_FREEZE列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_QUERY)
    @LogMessage(content = "查询BT_BANK_ACC_FREEZE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "bankacc/queryBtBankAccFreeze";
    }
    /**
     * BT_BANK_ACC_FREEZE table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_REPORTQUERY)
    @PostMapping("/dataTableQuery")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccFreezeService.getDataTablesQuery(dataTablesInput, this.getCurrentUser());
    }
}
