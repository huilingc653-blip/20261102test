package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.bond.service.CmsBondSwapService;
import com.bytter.finance.domain.bond.vo.CmsBondSwapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 可交债Controller
 *
 * @author wangf
 */
@Controller
@RequestMapping("/cms/bond/swap")
public class CmsBondSwapController extends BaseController {
    @Autowired
    private CmsBondSwapService cmsBondSwapService;
    /**
     * 可交债登记列表主界面
     *
     * @return 页面路径
     */
    @GetMapping("/listSwapRegister")
    public String listTransferableRegisterInfo() {
        return "cms/bond/listBondSwapRegisterInfo";
    }
    /**
     * 查询可交债注册列表
     *
     * @param input
     *            前端传参
     * @return 债券注册信息列表数据
     */
    @LogMessage(content = "查询可交债注册列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableSwapRegisterInfo")
    @ResponseBody
    public DataTablesOutput dataTableForTransferableRegisterInfo(@RequestBody DataTablesInput input) {
        return cmsBondSwapService.getSwapRegisterDataTable(input, this.getCurrentUser());
    }
    /**
     * 查询可交债登记列表
     *
     * @return 页面路径
     */
    @LogMessage(content = "查询可交债登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{issueId}/{isHistroy}")
    public String list(@PathVariable String issueId, @PathVariable String isHistroy, Model model) {
        model.addAttribute("isHistroy", "1".equals(isHistroy) ? "1" : "0");
        model.addAttribute("issueId", issueId);
        return "cms/bond/listBondSwap";
    }
    /**
     * 可交债登记列表数据
     *
     * @param input
     *            前端传参
     * @return 可交债登记列表数据
     */
    @PostMapping("/datatable/{issueId}")
    @ResponseBody
    public DataTablesOutput dataTable(@PathVariable String issueId, @RequestBody DataTablesInput input) {
        return cmsBondSwapService.getDataTable(input, issueId);
    }
    /**
     * 可交债编辑页面
     *
     * @return 页面路径
     */
    @GetMapping("/edit")
    public String editpage(CmsBondSwapVO cmsBondSwapVO, Model model) {
        if (!EmptyUtils.isEmpty(cmsBondSwapVO) && EmptyUtils.isNotEmpty(cmsBondSwapVO.getId())) {
            cmsBondSwapVO = this.cmsBondSwapService.getCmsBondSwapById(cmsBondSwapVO.getId());
        }
        model.addAttribute("cmsBondSwapVO", cmsBondSwapVO);
        return "cms/bond/editBondSwap";
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CmsBondSwapVO> list) {
        this.cmsBondSwapService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * 新增或修改交债信息
     *
     * @param cmsBondSwapVO
     *            交债信息
     * @return 是否操作成功
     */
    // @LogMessage(content = "新增或修改交债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsBondSwapVO cmsBondSwapVO, String saveType) {
        this.cmsBondSwapService.save(cmsBondSwapVO, saveType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除交债信息
     *
     * @param id
     *            交债信息id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除交债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.cmsBondSwapService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * 查看某个交债信息
     *
     * @param id
     *            交债信息id
     * @return 交债信息
     */
    @LogMessage(content = "查看某个交债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsBondSwapVO cmsBondSwapVO = this.cmsBondSwapService.getCmsBondSwapById(id);
        model.addAttribute("cmsBondSwapVO", cmsBondSwapVO);
        return "cms/bond/viewBondSwap";
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
        return "cms/bond/approve/listBondSwapApprove";
    }
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return this.cmsBondSwapService.dataTableApprove(input);
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
        model.addAttribute("cmsBondSwapVO", this.cmsBondSwapService.getCmsBondSwapById(businessId));
        return "cms/bond/approve/viewBondSwapApprove";
    }
}
