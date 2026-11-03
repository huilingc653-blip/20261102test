package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitAllotCheckService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotCheckVO;
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
import javax.servlet.http.HttpServletRequest;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 额度分配核准Controller
 * @Description: 额度分配核准核准控制器类
 * @date 2020-02-24
 */
@Controller
@RequestMapping("/billpool/limitAllotCheck")
public class AbPoolLimitAllotCheckController extends BaseController {
    @Autowired
    private AbPoolLimitAllotCheckService abPoolLimitAllotCheckService;
    @Autowired
    private AbLogService abLogService;
    /**
     * 额度分配核准列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolLimitAllotCheck";
    }
    /**
     * 额度分配核准 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度分配核准列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolLimitAllotCheckService.getDataTablesUnion(input);
    }
    /**
     * 跳转至额度分配核准审核界面
     *
     * @param ids        额度分配核准ids
     * @param applyMoney 合计分配额度
     * @param num        笔数
     * @param model
     * @return String
     */
    @GetMapping("/batchAudit")
    public String batchAudit(String ids, String applyMoney, String num, Model model) {
        model.addAttribute("ids", ids);
        model.addAttribute("applyMoney", applyMoney);
        model.addAttribute("num", num);
        return "billPool/addAbPoolLimitAllotCheck";
    }
    /**
     * 保存额度分配核准信息
     *
     * @param abPoolLimitAllotCheckVO
     * @return
     */
    @LogMessage(content = "保存额度分配核准信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolLimitAllotCheckVO abPoolLimitAllotCheckVO, HttpServletRequest request) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(abPoolLimitAllotCheckService.saveLimitAllotCheckInfo(abPoolLimitAllotCheckVO, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbLimitAllotCheckApprove";
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
        return abPoolLimitAllotCheckService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId 审批流水ID
     * @param taskId     流程ID
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolLimitAllotCheckVO checkVO = abPoolLimitAllotCheckService.getLimitAllotCheckVoByAid(businessId);
            model.addAttribute("abPoolLimitAllotCheckVO", checkVO);
        }
        model.addAttribute("isApprove", true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/approveLimitAllotCheck";
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
            AbPoolLimitAllotCheckVO checkVO = abPoolLimitAllotCheckService.getLimitAllotCheckVoByAid(businessId);
            model.addAttribute("abPoolLimitAllotCheckVO", checkVO);
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveLimitAllotCheck";
    }
    /**
     * 跳转额度分配核准详情界面
     *
     * @param id    额度分配核准ID
     * @param model
     * @return String
     */
    @GetMapping("/viewLimitAllotCheck/{id}")
    public String viewLimitAllot(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            AbPoolLimitAllotCheckVO checkVO = abPoolLimitAllotCheckService.getLimitAllotCheckVoByAid(id);
            model.addAttribute("abLogList", abLogService.listAbLog(null, id));
            model.addAttribute("abPoolLimitAllotCheckVO", checkVO);
            model.addAttribute("isApprove", false);
        }
        return "billPool/viewAbPoolLimitAllotCheck";
    }
}
