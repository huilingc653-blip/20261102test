package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.service.ContractSuspendService;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ContractSuspendVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @version V1.0
 * @Title: 合同终止Controll
 * @Package com.bytter.biz.contract.controller
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-12 下午09:17:48
 */
@Controller
@RequestMapping("/contract/suspend")
public class ContractSuspendController extends BaseController {
    @Autowired
    private ContractSuspendService contractSuspendService;
    @Autowired
    private ConCommonService conCommonService;
    @GetMapping("/list")
    public String list(Model model) {
        return "contract/listContractSuspend";
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
        return contractSuspendService.dataTableApprove(dataTablesInput);
    }
    /**
     * @param @param
     *            dataTablesInput
     * @param @return
     *            参数
     * @return DataTablesOutput 返回类型
     * @throws @Title:
     *             dataTable
     * @Description: TODO(查询数据)
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return contractSuspendService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/suspendListApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "contract/approve/listSuspendApproveList";
    }
    /**
     * 列表按钮操作
     *
     * @param id
     * @return
     */
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/shutOpt/{operate}")
    @ResponseBody
    public AjaxJson siginOpt(ContractSuspendVO contractSuspendVO, @PathVariable("operate") String operate) {
        AjaxJson ajaxJson = success();
        try {
            contractSuspendService.singleDispose(contractSuspendVO, operate, this.getCurrentUser());
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("操作成功");
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败");
        }
        return ajaxJson;
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     *            审批流水ID
     * @param taskId
     *            流程ID
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            model.addAttribute("conDraftVO", contractSuspendService.getContractSuspend(businessId));
        }
        model.addAttribute("taskId", taskId);
        return "contract/approve/approveSuspend";
    }
    /**
     * @param @param
     *            model
     * @param @return
     *            参数
     * @return String 返回类型
     * @throws @Title:
     *             add
     * @Description: TODO(Jump新增合同类型)
     */
    @GetMapping("/stopJump")
    public String add(String id, String opt, Model model) {
        model.addAttribute("conInfo", contractSuspendService.getContractSuspend(id));
        return "contract/stopSoftManage";
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delCon")
    @ResponseBody
    public AjaxJson delCon(String id, String lastId) {
        AjaxJson ajaxJson = success();
        try {
            contractSuspendService.delConSuSpend(id, lastId, this.getCurrentUser());
            ajaxJson.setSuccess(true);
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
    /**
     * @param @param
     *            model
     * @param @return
     *            参数
     * @return String 返回类型
     * @throws @Title:
     *             queryList
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    @GetMapping("/queryList")
    public String queryList(Model model) {
        return "contract/listConQuery";
    }
    /**
     * @param @param
     *            dataTablesInput
     * @param @return
     *            参数
     * @return DataTablesOutput 返回类型
     * @throws @Title:
     *             dataTable
     * @Description: TODO(查询数据)
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/queryDatatable")
    @ResponseBody
    public DataTablesOutput queryDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return contractSuspendService.getQueryDataTablesUnion(dataTablesInput);
    }
    /**
     * @param @param
     *            model
     * @param @return
     *            参数
     * @return String 返回类型
     * @throws @Title:
     *             queryList
     * @Description: TODO(单个业务详细)
     */
    @GetMapping("/conNumView/{operate}")
    public String conNumView(@PathVariable("operate") String operate, ConInfoVO params, Model model) {
        ConInfoDTO conInfo = conCommonService.getConInfo(params);
        model.addAttribute("conInfo", conInfo);
        model.addAttribute("opt", operate);
        return "contract/conNumView";
    }
    @GetMapping("/mobile/approve")
    public String mobileSignApprove(String businessId, String flowStatus, Model model) {
        model.addAttribute("conInfo", contractSuspendService.getContractSuspend(businessId));
        model.addAttribute("flowStatus", flowStatus);
        return "contract/mobile/appContractBasic";
    }
}
