package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.billPool.service.AbPoolEmitFinaApplyService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @Title: AbPoolEmitFinaApplyController
 * @Description: 融资申请Controller
 * @author wangzy
 * @date 2020/2/10 18:09
 *
 */
@Controller
@RequestMapping("/billpool/emitFinaApply")
public class AbPoolEmitFinaApplyController extends BaseController {
    @Autowired
    private AbPoolEmitFinaApplyService abPoolEmitFinaApplyService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbLogService abLogService;
    /**
     * 融资申请列表页面跳转
     * @param model
     * @return/v1-preposebill/bill/accebill/abbillInstructionManage
     */
    @GetMapping("/list")
    public String listAbPoolEmitFinaApplyVO(Model model) {
        model.addAttribute("dateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("dateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/listAbPoolEmitFinaApply";
    }
    /**
     * 票据池融资申请 table列表
     * @param input 查询条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池融资申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolEmitFinaApplyService.getDataTablesUnion(input);
    }
    /**
     * 融资申请新增页面跳转
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addAbPoolEmitFinaApplyVO(Model model) {
        model.addAttribute("externalCorpNum",abVoucherinfoViceService.getExternalCorpNum());
        return "billPool/addAbPoolEmitFinaApply";
    }
    /**
     * 融资申请保存和提交
     * @param  operate save为保存，submit 为提交
     * @param  infoVO 信息
     * @return 处理结果
     */
    @LogMessage(content = "融资申请保存或提交",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operate}")
    @ResponseBody
    public AjaxJson abPoolEmitFinaApplySaveOrSubmit(@PathVariable("operate") String operate, AbPoolEmitFinaApplyVO infoVO){
        return abPoolEmitFinaApplyService.abPoolEmitFinaApplySaveOrSubmit(operate, infoVO, getCurrentUser());
    }
    /**0
     * 融资申请修改页面跳转
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String updateAbPoolEmitFinaApplyVO(Model model,String id) {
        model.addAttribute("externalCorpNum",abVoucherinfoViceService.getExternalCorpNum());
        model.addAttribute("abPoolEmitFinaApplyVO",abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByDtlId(id));
        return "billPool/addAbPoolEmitFinaApply";
    }
    /**
     * 查看融资申请详情页面
     *
     * @return
     */
    @LogMessage(content = "查询融资申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        if(StringUtils.isNotEmpty(id)){
            model.addAttribute("abPoolEmitFinaApplyVO",abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByDtlId(id));
            model.addAttribute("conInfoList",abPoolEmitFinaApplyService.listConInfo(id));
            List<AbLog> ablogList = abLogService.listAbLog(null, id);
            model.addAttribute("abLogList", ablogList);
        }
        return "billPool/viewAbPoolEmitFinaApply";
    }
    /**
     * 批量提交融资申请信息
     * @param ids 票据流水表ids
     * @return
     */
    @LogMessage(content = "批量提交融资申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(String ids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolEmitFinaApplyService.batchSubmit(ids, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 批量删除融资申请信息
     * @param ids 票据池明细表ids
     * @return
     */
    @LogMessage(content = "批量删除融资申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(String ids,String reason) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolEmitFinaApplyService.batchDelete(ids, reason, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 融资申请审批列表页面跳转
     * @param model
     * @return 审批列表
     */
    @GetMapping("/approve")
    public String listApproveAbPoolEmitFinaApplyVO(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolEmitFinaApplyApprove";
    }
    /**
     * 融资申请审批 table列表
     * @param  input 信息
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询融资申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable")
    @ResponseBody
    public DataTablesOutput listApproveDatatable( @RequestBody DataTablesInput input) {
        return abPoolEmitFinaApplyService.listApproveDatatable(input);
    }
    /**
     * 跳转至审批详情界面
     * @param taskId
     * @param dtlId
     * @param model
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String taskId,String dtlId, Model model) {
        model.addAttribute("abPoolEmitFinaApplyVO",abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByDtlId(dtlId));
        model.addAttribute("isApprove",true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/viewAbPoolEmitFinaApplyApprove";
    }
    /**
     * 重新生成合同
     * @param conId 合同Id
     * @param model
     * @return
     */
    @LogMessage(content = "重新生成合同",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/regenerateContract")
    @ResponseBody
    public AjaxJson regenerateContract(String conId, Model model) {
        return abPoolEmitFinaApplyService.regenerateContract(conId, getCurrentUser());
    }
    /**
     * 合同确认
     * @param conId 合同id
     * @return AjaxJson    返回类型
     */
    @LogMessage(content = "合同确认",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/contractConfirm")
    @ResponseBody
    public AjaxJson contractConfirm(String conId){
        return  abPoolEmitFinaApplyService.contractConfirm(conId, getCurrentUser());
    }
    /**
     * 票据池质权人信息
     * @param bpId 票据池id
     * @return AbPoolEmitFinaApplyVO   返回类型
     */
    @GetMapping("/selectPledgee")
    @ResponseBody
    public AbPoolEmitFinaApplyVO selectPledgee(String bpId){
        return  abPoolEmitFinaApplyService.selectPledgee(bpId);
    }
    /**
     * 查看下载合同文件
     * @param conId 合同id
     *
     */
    @GetMapping("/viewContract")
    public void viewContract(HttpServletRequest request, HttpServletResponse response, String conId) {
        try {
            abPoolEmitFinaApplyService.viewContract(request, response, conId);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO = abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByAid(businessId);
            model.addAttribute("abEmitFinaApplyVO", abPoolEmitFinaApplyVO);
        }
        return "billPool/mobile/approveEmitFinaApply";
    }
    /**
     * 出票融资申请导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<AbPoolEmitFinaApplyVO> list) {
        abPoolEmitFinaApplyService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
