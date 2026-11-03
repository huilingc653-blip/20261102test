package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.service.CmsBondIssueInfoService;
import com.bytter.finance.domain.bond.service.CmsBondPayService;
import com.bytter.finance.domain.bond.service.CmsBondRegisterInfoService;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondPayVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券还款
 * @date 2022-06-07 10:07:46
 */
@Controller
@RequestMapping("/cms/bond/cmsbondpay")
public class CmsBondPayController extends BaseController {
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    /**
     * 债券还款处理 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询债券还款处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "cms/bond/bondPay/listCmsBondPay";
    }
    /**
     * 债券还款计划登记 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsBondPayService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 债券还款新增页面跳转
     */
    @GetMapping("/edit")
    public String edit(String tempOrSubmitSign, CmsBondPayVo cmsBondPayVo, Model model) {
        if (StringUtils.isNotEmpty(cmsBondPayVo.getId())) {
            List<CmsBondPayVo> leasePayVOList = cmsBondPayService.getByRegisterId(cmsBondPayVo.getId(),
                    CmsConstant.IS_FALSE);
            if (!ListUtils.isNullOrEmpty(leasePayVOList)) {
                cmsBondPayVo = leasePayVOList.get(0);
            }
            cmsBondPayVo.setCreateDate(new Date());
        } else {
            cmsBondPayVo.setCreateDate(new Date());
        }
        CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService
                .getRegisterById(cmsBondPayVo.getRegisterId());
        String onlineSign = cmsBondPayVo.getOnlineSign();
        model.addAttribute("onlineSign", onlineSign);
        model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
        model.addAttribute("cmsBondPayVo", cmsBondPayVo);
        model.addAttribute("tempOrSubmitSign", tempOrSubmitSign);
        return "cms/bond/bondPay/editCmsBondPay";
    }
    /**
     * 债券还款申请查询- 公用页面
     */
    @LogMessage(content = "债券还款申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{registerId}")
    public String view(@PathVariable("registerId") String registerId, Model model) {
        CmsBondRegisterInfoVo cmsBondRegisterInfoVo = new CmsBondRegisterInfoVo();
        CmsBondIssueInfoVo cmsBondIssueInfoVo = new CmsBondIssueInfoVo();
        if (StringUtils.isNotEmpty(registerId)) {
            cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
        }
        model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
        model.addAttribute("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
        return "cms/bond/bondPay/viewCmsBondPay";
    }
    /**
     * 根据债券主表id查询债券还款详情
     *
     * @param registerId
     *            债劵注册id
     * @return
     */
    @PostMapping("/listBondPayByRegisterId")
    @ResponseBody
    public List<CmsBondPayVo> listBondPayByRegisterId(String registerId) {
        List<CmsBondPayVo> list = cmsBondPayService.getBondPayByRegisterId(registerId);
        return list;
    }
    /**
     * 编辑债券还款
     *
     * @return
     */
    @LogMessage(content = "编辑债券还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(CmsBondPayVo vo, String saveType, String tempOrSubmitSign, String sign) {
        // 校验该发行是否还有在审批中的数据
        cmsBondPayService.checkCmsBondPayStatus(vo.getIssueId());
        cmsBondPayService.tempOrSubmit(vo, saveType, tempOrSubmitSign, getCurrentUser(), sign);
        return success();
    }
    /******************** 审批BEGIN **********************/
    /**
     * 债券还款申请审批列表页面
     */
    @LogMessage(content = "债券还款申请审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/bond/approve/listBondPayApprove";
    }
    /**
     * 债券还款申请审批列表 - 数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsBondPayService.dataTableApprove(dataTablesInput);
    }
    /**
     * 债券还款申请 详情页面
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsBondPay cmsBondPay = cmsBondPayService.get(businessId);
            if (StringUtils.isNotEmpty(cmsBondPay) && StringUtils.isNotEmpty(cmsBondPay.getId())) {
                CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService
                        .getRegisterById(cmsBondPay.getRegisterId());
                CmsBondPayVo cmsBondPayVo = cmsBondPayService.getById(cmsBondPay.getId());
                cmsBondPayVo.setPurpose(cmsBondPay.getPurpose());
                model.addAttribute("cmsBondPayVo", cmsBondPayVo);
                model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/bond/approve/viewBondPayApprove";
    }
}
