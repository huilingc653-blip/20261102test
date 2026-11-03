package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolSignService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolSignVO;
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
import java.util.List;
/**
 * 
　 * @Title: 电票签收Controller
　 * @author jy.cao
  * @Description:AbPoolSignController.java
　 * @date 2020年2月26日
　 * @version 1.0
 */
@Controller
@RequestMapping("/billPool/abSign")
public class AbPoolSignController extends BaseController{
	
    @Autowired
    private AbPoolSignService abPoolSignService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
	 /**
     * 跳转到电票签收页面
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolSign";
    }
    /**
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询签收TAB列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolSignService.getDataTablesUnion(input);
    }
    /**
     * 跳转至签收界面
     * @param abPoolSignVO
     * @param model
     * @return String
     */
    @GetMapping("/batchSign")
    public String batchAudit(AbPoolSignVO abPoolSignVO, Model model) {
        model.addAttribute("abPoolSignVO",abPoolSignVO);
        return "billPool/addAbPoolSign";
    }
    /**
     * 保存签收信息
     *
     * @param abPooalSignVO
     * @return
     */
    @LogMessage(content = "保存签收信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolSignVO abPooalSignVO, HttpServletRequest request) {
        abPoolSignService.saveAbPoolSignInfo(abPooalSignVO, this.getCurrentUser());
        return success();
    }
    @PostMapping("/operation")
    @ResponseBody
    public AjaxJson operation(@RequestParam("pdId") String pdId, @RequestParam("opt") String opt) {
        abPoolSignService.operation(pdId,opt,this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "billPool/approve/listAbPoolSignApprove";
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
        return abPoolSignService.dataTableApprove(dataTablesInput);
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
            AbPoolSignVO abPoolSignVO = abPoolSignService.getAbPoolSignVOByPdId(businessId);
            model.addAttribute("abPoolSignVO", abPoolSignVO);
            if (StringUtils.isNotEmpty(abPoolSignVO.getId())) {
                AbBillReceivebizVO info = abPoolSignService.getAbBillReceivebizVOById(abPoolSignVO.getRecId());
                model.addAttribute("info", info);
            }
        }
        model.addAttribute("isApprove", true);
        model.addAttribute("taskId", taskId);
        return "billPool/approve/approveSignApply";
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
            AbPoolSignVO abPoolSignVO = abPoolSignService.getAbPoolSignVOByPdId(businessId);
            model.addAttribute("abPoolSignVO", abPoolSignVO);
            if (StringUtils.isNotEmpty(abPoolSignVO.getId())) {
                AbBillReceivebizVO info = abPoolSignService.getAbBillReceivebizVOById(abPoolSignVO.getRecId());
                model.addAttribute("info", info);
            }
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveSignApply";
    }
    /**
     * 电票签收详细信息页面跳转
     *
     * @return
     */
    @GetMapping("/view")
    public String view(AbPoolSignVO poolSignVO, Model model) {
        //票据池信息
        if (StringUtils.isNotEmpty(poolSignVO.getPdId()) && !"null".equals(poolSignVO.getPdId())) {
            AbPoolSignVO abPoolSignVO = abPoolSignService.getAbPoolSignVOByPdId(poolSignVO.getPdId());
            model.addAttribute("abPoolSignVO", abPoolSignVO);
            model.addAttribute("abLogList", abLogService.listAbLog(null, poolSignVO.getPdId()));
        }
        //签收信息
        if (StringUtils.isNotEmpty(poolSignVO.getRecId())) {
            AbBillReceivebizVO info = abPoolSignService.getAbBillReceivebizVOById(poolSignVO.getRecId());
            model.addAttribute("info", info);
        }
        //业务流转信息
        if (StringUtils.isNotEmpty(poolSignVO.getAccId()) && !"null".equals(poolSignVO.getAccId())) {
            AbPoolDtl abPoolDtl = abPoolSignService.getAbPoolDtlByLastPdId(poolSignVO.getPdId());
            List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(poolSignVO.getAccId(),abPoolDtl!=null?abPoolDtl.getId():null);
            model.addAttribute("accrecordsList", abAccrecordsList);
        }
        model.addAttribute("isApprove", false);
        return "billPool/viewAbPoolSignApply";
    }
}
