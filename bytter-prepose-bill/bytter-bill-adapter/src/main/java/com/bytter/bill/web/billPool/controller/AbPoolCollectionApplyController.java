package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolCollectionApplyService;
import com.bytter.bill.domain.billPool.vo.AbPoolCollectionApplyVO;
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
 * @Title: AbPoolEmitFinaApplyController
 * @Description: 提示付款Controller
 * @author wangzy
 * @date 2020/3/11 15:09
 *
 */
@Controller
@RequestMapping("/billpool/collection")
public class AbPoolCollectionApplyController extends BaseController {
    @Autowired
    private AbPoolCollectionApplyService abPoolCollectionApplyService;
    @Autowired
    private AbLogService abLogService;
    /**
     * 提示列表页面跳转
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listAbPoolCollectionApplyVO(Model model) {
        model.addAttribute("dateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("dateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/listAbPoolCollectionApply";
    }
    /**
     * 票据池提示付款 table列表
     * @param input 查询条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池提示付款列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolCollectionApplyService.getDataTablesUnion(input);
    }
    /**
     * 提示付款新增页面跳转
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addAbPoolCollectionApplyVO(AbPoolCollectionApplyVO abPoolCollectionApplyVO, Model model) {
        model.addAttribute("abPoolCollectionApplyVO",abPoolCollectionApplyVO);
        model.addAttribute("businessDate", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/addAbPoolCollectionApply";
    }
    /**
     * 提示付款提交
     * @param  abPoolCollectionApplyVO
     * @return 处理结果
     */
    @LogMessage(content = "提示付款提交",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("submit")
    @ResponseBody
    public AjaxJson abPoolCollectionApplySubmit(AbPoolCollectionApplyVO abPoolCollectionApplyVO){
        return abPoolCollectionApplyService.abPoolCollectionApplySubmit(abPoolCollectionApplyVO, getCurrentUser());
    }
    /**
     * 查看详情页面
     * @param  id 明细表id
     * @return
     */
    @LogMessage(content = "查询详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("abPoolCollectionApplyVO",abPoolCollectionApplyService.getAbPoolCollectionApplyVOByDtlId(id));
        if(StringUtils.isNotEmpty(id)){
            List<AbLog> ablogList = abLogService.listAbLog(null, id);
            model.addAttribute("abLogList", ablogList);
        }
        return "billPool/viewAbPoolCollectionApply";
    }
    /**
     * 审批列表页面跳转
     * @param model
     * @return 审批列表
     */
    @GetMapping("/approve")
    public String listAbPoolCollectionApplyApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolCollectionApplyApprove";
    }
    /**
     * 审批 table列表
     * @param  input 信息
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询提示付款审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable")
    @ResponseBody
    public DataTablesOutput listApproveDatatable( @RequestBody DataTablesInput input) {
        return abPoolCollectionApplyService.listApproveDatatable(input);
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
        model.addAttribute("abPoolCollectionApplyVO",abPoolCollectionApplyService.getAbPoolCollectionApplyVOByDtlId(dtlId));
        model.addAttribute("isApprove",true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/approveAbPoolCollectionApply";
    }
    /**
     * 中止
     * @param aid 流水Id
     * @param model
     * @return
     */
    @PostMapping("/abort")
    @ResponseBody
    public AjaxJson abortDtl(String aid, Model model) {
        return abPoolCollectionApplyService.abortDtl(aid, getCurrentUser());
    }
    /**
     * 继续
     * @param aid 流水id
     * @return AjaxJson    返回类型
     */
    @LogMessage(content = "继续",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/continueRemind")
    @ResponseBody
    public AjaxJson continueRemind(String aid){
        return abPoolCollectionApplyService.continueRemind(aid, getCurrentUser());
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
            AbPoolCollectionApplyVO abPoolCollectionApplyVO = abPoolCollectionApplyService.getAbPoolCollectionApplyVOByAid(businessId);
            model.addAttribute("abPoolCollectionApplyVO", abPoolCollectionApplyVO);
        }
        return "billPool/mobile/approveCollectionApply";
    }
}
