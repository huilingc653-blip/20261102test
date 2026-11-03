package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.billPool.service.AbPoolEmitApplyService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitApplyVO;
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
import java.util.List;
/**
 * @Title: AbPoolEmitApplyController
 * @Description: 出票申请Controller
 * @author wangzy
 * @date 2020/2/25 18:09
 *
 */
@Controller
@RequestMapping("/billpool/emitApply")
public class AbPoolEmitApplyController  extends BaseController {
    @Autowired
    private AbPoolEmitApplyService abPoolEmitApplyService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbLogService abLogService;
    /**
     * 出票申请列表页面跳转
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listAbPoolEmitApplyVO(Model model) {
        model.addAttribute("dateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("dateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/listAbPoolEmitApply";
    }
    /**
     * 票据池出票申请 table列表
     * @param input 查询条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池出票申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolEmitApplyService.getDataTablesUnion(input);
    }
    /**
     * 出票申请新增页面跳转
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addAbPoolEmitApplyVO(Model model) {
        model.addAttribute("externalCorpNum",abVoucherinfoViceService.getExternalCorpNum());
        return "billPool/addAbPoolEmitApply";
    }
    /**
     * 出票申请修改页面跳转
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String updateAbPoolEmitApplyVO(Model model,String id) {
        model.addAttribute("externalCorpNum",abVoucherinfoViceService.getExternalCorpNum());
        model.addAttribute("abPoolEmitApplyVO",abPoolEmitApplyService.getAbPoolEmitApplyVOByDtlId(id));
        return "billPool/addAbPoolEmitApply";
    }
    /**
     * 出票申请保存和提交
     * @param  operate add为保存，submit 为提交
     * @param  infoVO 信息
     * @return 处理结果
     */
    @LogMessage(content = "出票申请保存或提交",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operate}")
    @ResponseBody
    public AjaxJson abPoolEmitApplySaveOrSubmit(@PathVariable("operate") String operate, AbPoolEmitApplyVO infoVO){
        return abPoolEmitApplyService.abPoolEmitApplySaveOrSubmit(operate, infoVO, getCurrentUser());
    }
    /**
     * 查看出票申请详情页面
     *
     * @return
     */
    @LogMessage(content = "查询出票申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("abPoolEmitApplyVO",abPoolEmitApplyService.getAbPoolEmitApplyVOByDtlId(id));
        if(StringUtils.isNotEmpty(id)){
            List<AbLog> ablogList = abLogService.listAbLog(null, id);
            model.addAttribute("abLogList", ablogList);
        }
        return "billPool/viewAbPoolEmitApply";
    }
    /**
     * 批量提交出票申请信息
     * @param ids 票据流水表ids
     * @return
     */
    @LogMessage(content = "批量提交出票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(String ids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolEmitApplyService.batchSubmit(ids, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 批量删除出票申请信息
     * @param ids 票据池明细表ids
     * @return
     */
    @LogMessage(content = "批量删除出票申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(String ids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolEmitApplyService.batchDelete(ids, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 出票申请审批列表页面跳转
     * @param model
     * @return 审批列表
     */
    @GetMapping("/approve")
    public String listApproveAbPoolEmitApplyVO(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolEmitApplyApprove";
    }
    /**
     * 出票申请审批 table列表
     * @param  input 信息
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询出票申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable")
    @ResponseBody
    public DataTablesOutput listApproveDatatable( @RequestBody DataTablesInput input) {
        return abPoolEmitApplyService.listApproveDatatable(input);
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
        model.addAttribute("abPoolEmitApplyVO",abPoolEmitApplyService.getAbPoolEmitApplyVOByDtlId(dtlId));
        model.addAttribute("isApprove",true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/viewAbPoolEmitApplyApprove";
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
            AbPoolEmitApplyVO abPoolEmitApplyVO = abPoolEmitApplyService.getAbPoolEmitApplyVOByAid(businessId);
            model.addAttribute("abPoolEmitApplyVO", abPoolEmitApplyVO);
        }
        return "billPool/mobile/approveEmitApply";
    }
}
