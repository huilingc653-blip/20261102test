package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitAllotService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitVO;
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
 * @Title: 额度分配Controller
 * @Description: 额度分配控制器类
 * @date 2020-01-13
 */
@Controller
@RequestMapping("/billpool/limitAllot")
public class AbPoolLimitAllotController extends BaseController {
    @Autowired
    private AbPoolLimitAllotService abPoolLimitAllotService;
    @Autowired
    private AbPoolLimitService abPoolLimitService;
    @Autowired
    private AbLogService abLogService;
    /**
     * 额度分配列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolLimitAllot";
    }
    /**
     * 额度分配 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度分配列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolLimitAllotService.getDataTablesUnion(input);
    }
    /**
     * 额度分配新增页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addAbPoolLimitAllot(Model model) {
        return "billPool/addAbPoolLimitAllot";
    }
    /**
     * 根据单位ID和票据池ID查询额度信息
     *
     * @param corpId 单位ID
     * @param bpId   票据池ID
     * @return 额度信息
     */
    @PostMapping("/getLimitInfo")
    @ResponseBody
    public AbPoolLimitVO getLimitInfo(String corpId, String bpId) {
        return abPoolLimitService.getLimitInfo(corpId, bpId);
    }
    /**
     * 保存额度分配信息
     *
     * @param abPoolLimitAllotVO
     * @return
     */
    @LogMessage(content = "保存额度分配信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolLimitAllotVO abPoolLimitAllotVO, HttpServletRequest request) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(abPoolLimitAllotService.saveLimitAllotInfo(abPoolLimitAllotVO, this.getCurrentUser()));
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
        return "billPool/approve/listAbLimitAllotApprove";
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
        return abPoolLimitAllotService.dataTableApprove(dataTablesInput);
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
            AbPoolLimitAllotVO abPoolLimitAllotVO = abPoolLimitAllotService.getLimitAllotVoById(businessId);
            model.addAttribute("abPoolLimitAllotVO", abPoolLimitAllotVO);
        }
        model.addAttribute("isApprove", true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/approveLimitAllot";
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
            AbPoolLimitAllotVO abPoolLimitAllotVO = abPoolLimitAllotService.getLimitAllotVoById(businessId);
            model.addAttribute("abPoolLimitAllotVO", abPoolLimitAllotVO);
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveLimitAllot";
    }
    /**
     * 跳转额度分配详情界面
     *
     * @param id    额度分配ID
     * @param model
     * @return String
     */
    @GetMapping("/viewLimitAllot/{id}")
    public String viewLimitAllot(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            AbPoolLimitAllotVO abPoolLimitAllotVO = abPoolLimitAllotService.getLimitAllotVoById(id);
            model.addAttribute("abLogList", abLogService.listAbLog(null, id));
            model.addAttribute("abPoolLimitAllotVO", abPoolLimitAllotVO);
            model.addAttribute("isApprove", false);
        }
        return "billPool/viewAbPoolLimitAllot";
    }
    /**
     * 额度调剂查询表 页面跳转
     *
     * @return
     */
    @GetMapping("/queryList")
    public String queryList(Model model) {
        return "billPool/listAbPoolLimitAllotQuery";
    }
    /**
     * 额度调剂查询table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度调剂列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/queryDatatable")
    @ResponseBody
    public DataTablesOutput queryDataTable(@RequestBody DataTablesInput input) {
        return abPoolLimitAllotService.getQueryDataTablesUnion(input);
    }
}
