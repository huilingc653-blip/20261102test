package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.bond.entity.CmsBondRegisterInfo;
import com.bytter.finance.domain.bond.service.CmsBondRegisterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 债券注册Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/cms/bond/register")
public class CmsBondRegisterInfoController extends BaseController {
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    /**
     * 债券注册信息查询列表跳转
     *
     * @return 页面路径
     */
    @GetMapping("/list")
    public String list() {
        return "cms/bond/listBondRegisterInfo";
    }
    /**
     * 获取债券注册信息新增页路径
     *
     * @return 债券注册信息新增页路径
     */
    @GetMapping("/add")
    public String add() {
        return "cms/bond/addBondRegisterInfo";
    }
    /**
     * 获取债券注册信息编辑页路径
     *
     * @param id
     *            债券注册信息id
     * @param model
     *            模型
     * @return 债券注册信息编辑页路径
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("registerInfo", cmsBondRegisterInfoService.getInfoById(id));
        return "cms/bond/updateBondRegisterInfo";
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
        return "cms/bond/approve/listBondRegisterInfoApprove";
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     *            id
     * @param taskId
     *            审批任务id
     * @param model
     *            模型
     * @return 前端页面路径
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        model.addAttribute("taskId", taskId);
        model.addAttribute("registerInfo", cmsBondRegisterInfoService.getInfoById(businessId));
        return "cms/bond/approve/viewBondRegisterInfoApprove";
    }
    /**
     * 债劵注册信息表详情页
     *
     * @param id
     *            id
     * @param model
     *            model
     * @return 前端页面路径
     */
    @LogMessage(content = "查询债劵注册信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("registerInfo", cmsBondRegisterInfoService.getInfoById(id));
        return "cms/bond/viewBondRegisterInfo";
    }
    /**
     * 债券注册审批列表 - 数据
     *
     * @param input
     *            前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return cmsBondRegisterInfoService.dataTableApprove(input);
    }
    /**
     * 获取债券注册信息列表数据
     *
     * @param input
     *            前端传参
     * @return 债券注册信息列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return cmsBondRegisterInfoService.getDataTable(input, this.getCurrentUser());
    }
    /**
     * 新增债券注册信息
     *
     * @param cmsBondRegisterInfo
     *            债券注册信息
     * @return 是否新增成功
     */
    @LogMessage(content = "新增债券注册信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsBondRegisterInfo cmsBondRegisterInfo) {
        cmsBondRegisterInfoService.saveBondRegisterInfo(cmsBondRegisterInfo, this.getCurrentUser());
        return success();
    }
    /**
     * 修改债券注册信息
     *
     * @param cmsBondRegisterInfo
     *            债券注册信息
     * @return 是否修改成功
     */
    @LogMessage(content = "修改债券注册信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsBondRegisterInfo cmsBondRegisterInfo) {
        cmsBondRegisterInfoService.updateBondRegisterInfo(cmsBondRegisterInfo, this.getCurrentUser());
        return success();
    }
    /**
     * 删除债券注册信息
     *
     * @param id
     *            债券注册信息id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除债券注册信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsBondRegisterInfoService.deleteBondRegisterInfo(id, this.getCurrentUser());
        return success();
    }
}
