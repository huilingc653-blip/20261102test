package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.bond.service.CmsBondTransferService;
import com.bytter.finance.domain.bond.vo.CmsBondTransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 可转债Controller
 *
 * @author liuli
 */
@Controller
@RequestMapping("/cms/bond/transfer")
public class CmsBondTransferController extends BaseController {
    @Autowired
    private CmsBondTransferService cmsBondTransferService;
    /**
     * 可转债登记列表主界面
     *
     * @return 页面路径
     */
    @GetMapping("/listTransferRegisterInfo")
    public String listTransferableRegisterInfo() {
        return "cms/bond/listBondTransferableRegisterInfo";
    }
    /**
     * 查询可转债注册列表
     *
     * @param input
     *            前端传参
     * @return 债券注册信息列表数据
     */
    @LogMessage(content = "查询可转债注册列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableTransferRegisterInfo")
    @ResponseBody
    public DataTablesOutput dataTableForTransferableRegisterInfo(@RequestBody DataTablesInput input) {
        return cmsBondTransferService.getTransferableRegisterDataTable(input, this.getCurrentUser());
    }
    /**
     * 查询可转债登记列表
     *
     * @return 页面路径
     */
    @LogMessage(content = "查询可转债登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{issueId}/{isHistory}")
    public String list(@PathVariable String issueId, @PathVariable String isHistory, Model model) {
        model.addAttribute("isHistory", "1".equals(isHistory) ? "1" : "0");
        model.addAttribute("issueId", issueId);
        return "cms/bond/listBondTransfer";
    }
    /**
     * 可转债登记列表数据
     *
     * @param input
     *            前端传参
     * @return 可转债登记列表数据
     */
    @PostMapping("/datatable/{issueId}")
    @ResponseBody
    public DataTablesOutput dataTable(@PathVariable String issueId, @RequestBody DataTablesInput input) {
        return cmsBondTransferService.getDataTable(input, issueId);
    }
    /**
     * 可转债编辑页面
     *
     * @return 页面路径
     */
    @GetMapping("/edit")
    public String editpage(CmsBondTransferVO cmsBondTransferVO, Model model) {
        if (!EmptyUtils.isEmpty(cmsBondTransferVO) && EmptyUtils.isNotEmpty(cmsBondTransferVO.getId())) {
            cmsBondTransferVO = this.cmsBondTransferService.getCmsBondTransferById(cmsBondTransferVO.getId());
        }
        model.addAttribute("cmsBondTransferVO", cmsBondTransferVO);
        return "cms/bond/editTransfer";
    }
    /**
     * 新增或修改转债信息
     *
     * @param cmsBondTransferVO
     *            转债信息
     * @return 是否操作成功
     */
    @LogMessage(content = "新增或修改转债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsBondTransferVO cmsBondTransferVO, String saveType) {
        this.cmsBondTransferService.save(cmsBondTransferVO, saveType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除转债信息
     *
     * @param id
     *            转债信息id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除转债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.cmsBondTransferService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * 查看某个转债信息
     *
     * @param id
     *            转债信息id
     * @return 转债信息
     */
    @LogMessage(content = "查看某个转债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsBondTransferVO cmsBondTransferVO = this.cmsBondTransferService.getCmsBondTransferById(id);
        model.addAttribute("cmsBondTransferVO", cmsBondTransferVO);
        return "cms/bond/viewBondTransfer";
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CmsBondTransferVO> list) {
        this.cmsBondTransferService.saveImport(list, this.getCurrentUser());
        return success();
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
        return "cms/bond/approve/listBondTransferApprove";
    }
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return this.cmsBondTransferService.dataTableApprove(input);
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
        model.addAttribute("cmsBondTransferVO", this.cmsBondTransferService.getCmsBondTransferById(businessId));
        return "cms/bond/approve/viewBondTransferApprove";
    }
}
