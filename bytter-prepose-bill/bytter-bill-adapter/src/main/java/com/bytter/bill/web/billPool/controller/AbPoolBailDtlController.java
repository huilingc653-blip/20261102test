package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.service.AbPoolBailDtlService;
import com.bytter.bill.domain.billPool.vo.AbPoolBailDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: AbPoolBailDtlController
 * @Description: 保证金管理Controller
 * @author wangzy
 * @date 2020/1/10 18:09
 *
 */
@Controller
@RequestMapping("/billPool/bail")
public class AbPoolBailDtlController extends BaseController {
    @Autowired
    private AbPoolBailDtlService abPoolBailDtlService;
    /**
     * 保证金存入列表页面跳转
     * @param model
     * @return
     */
    @GetMapping("/inList")
    public String listAbPoolBailInVO(Model model) {
        model.addAttribute("appDateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("appDateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/listAbPoolBailDtlIn";
    }
    /**
     * 保证金提取列表页面跳转
     * @param model
     * @return
     */
    @GetMapping("/outList")
    public String listAbPoolBailOutVO(Model model) {
        model.addAttribute("appDateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("appDateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/listAbPoolBailDtlOut";
    }
    /**
     * 保证金存取 table列表
     * @param type 保证金存取类型 1存入 2提取
     * @param input 查询条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询存取保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable/{type}")
    @ResponseBody
    public DataTablesOutput dataTable(@PathVariable("type") String type, @RequestBody DataTablesInput input) {
        return abPoolBailDtlService.getDataTablesUnion(type, input);
    }
    /**
     * 保证金存入新增页面跳转
     * @param model
     * @return
     */
    @GetMapping("/depositAmount")
    public String addAbPoolBailInVO(Model model) {
        return "billPool/addAbPoolBailDtlIn";
    }
    /**
     * 保证金提取新增页面跳转
     * @param model
     * @return
     */
    @GetMapping("/extractAmount")
    public String addAbPoolBailOutVO(Model model) {
        return "billPool/addAbPoolBailDtlOut";
    }
    /**
     * 根据单位ID和票据池ID查询保证金信息
     * @param corpId 单位ID
     * @param bpId 票据池ID
     * @return 保证金信息
     */
    @PostMapping("/getBailInfo/{type}")
    @ResponseBody
    public AbPoolBailDtlVO getBailInfo(@PathVariable("type") String type, String corpId, String bpId) {
        return abPoolBailDtlService.getBailInfo(type, corpId, bpId);
    }
    /**
     * 保证金存取提交
     * @param  type 1为存入，2为取出
     * @param  infoVO 信息
     * @return 处理结果
     */
    @LogMessage(content = "保证金存取提交",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/submit/{type}")
    @ResponseBody
    public AjaxJson bailInfoSubmit(@PathVariable("type") String type, AbPoolBailDtlVO infoVO){
        return abPoolBailDtlService.bailInfoSubmit(infoVO, type, getCurrentUser());
    }
    /**
     * 保证金存取查看页面
     * @param id 保证金流水Id
     * @param model
     * @return 页面
     */
    @GetMapping("/viewBail/{type}")
    public String viewBail(@PathVariable("type") String type, String id,Model model) {
        model.addAttribute("abPoolBailDtlVO",abPoolBailDtlService.getAbPoolBailDtlInfo(id));
        model.addAttribute("abLogList", abPoolBailDtlService.listAbLogInfo(id));
        // type为1跳转存入详情界面
        if(AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN.equals(type)){
            return "billPool/viewAbPoolBailDtlIn";
        }
        return "billPool/viewAbPoolBailDtlOut";
    }
    /**
     * 保证金存入审批列表页面跳转
     * @param model
     * @return 审批列表
     */
    @GetMapping("/inApprove")
    public String listApproveAbPoolBailInVO(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolBailDtlInApprove";
    }
    /**
     * 保证金提取审批列表页面跳转
     * @param model
     * @return 审批列表
     */
    @GetMapping("/outApprove")
    public String listApproveAbPoolBailOutVO(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolBailDtlOutApprove";
    }
    /**
     * 保证金存取审批 table列表
     * @param  type 1为存入，2为取出
     * @param  input 信息
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询保证金存取审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable/{type}")
    @ResponseBody
    public DataTablesOutput listApproveDatatable(@PathVariable("type") String type, @RequestBody DataTablesInput input) {
        return abPoolBailDtlService.listApproveDatatable(input, type);
    }
    /**
     * 保证金提取审批详情页面
     * @param id 保证金流水Id
     * @param model
     * @return 页面
     */
    @GetMapping("/viewBailApprove/{type}")
    public String viewBailApprove(@PathVariable("type") String type, String taskId, String id,Model model) {
        model.addAttribute("abPoolBailDtlVO",abPoolBailDtlService.getAbPoolBailDtlInfo(id));
        model.addAttribute("isApprove", true);
        model.addAttribute("taskId", taskId);
        if(AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN.equals(type)){
            return "billPool/approve/viewAbPoolBailDtlInApprove";
        }
        return "billPool/approve/viewAbPoolBailDtlOutApprove";
    }
    /**
     * 跳转至保证金存入详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileInApprove")
    public String mobileInApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolBailDtlVO abPoolBailDtlVO = abPoolBailDtlService.getAbPoolBailDtlInfo(businessId);
            model.addAttribute("abPoolBailDtlVO", abPoolBailDtlVO);
            model.addAttribute("inBail",true);
        }
        return "billPool/mobile/approvePoolBailDtl";
    }
    /**
     * 跳转至保证金提取详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileOutApprove")
    public String mobileOutApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolBailDtlVO abPoolBailDtlVO = abPoolBailDtlService.getAbPoolBailDtlInfo(businessId);
            model.addAttribute("abPoolBailDtlVO", abPoolBailDtlVO);
        }
        return "billPool/mobile/approvePoolBailDtl";
    }
}
