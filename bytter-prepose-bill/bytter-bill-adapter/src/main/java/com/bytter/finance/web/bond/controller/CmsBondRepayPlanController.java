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
import com.bytter.finance.domain.bond.service.*;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券还款计划登记
 * @date 2022-06-06 10:07:46
 */
@Controller
@RequestMapping("/cms/bond/cmsbondrepayplan")
public class CmsBondRepayPlanController extends BaseController {
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    /**
     * 债券还款计划登记 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询债券还款计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{isSaveSign}")
    public String list(@PathVariable("isSaveSign") String isSaveSign, Model model) {
        model.addAttribute("isSaveSign", isSaveSign);
        return "cms/bond/bondRepayPlan/listCmsBondRepayPlan";
    }
    /**
     * 债券还款计划登记 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(String isSaveSign, @RequestBody DataTablesInput dataTablesInput) {
        return cmsBondRepayPlanService.getDataTablesUnion(dataTablesInput, isSaveSign);
    }
    /**
     * 债券还款计划登记表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(String registerId, Model model) {
        if (StringUtils.isNotEmpty(registerId)) {
            CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                // 债券发行信息
                CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
                model.addAttribute("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
                // 债券计划主表信息
                CmsBondRepayPlanVo planVO = cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfoVo.getId());
                if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    planVO.setRepayType(CmsConstant.REPAY_TYPE_07);
                    model.addAttribute("cmsBondRepayPlanVo", planVO);
                }
            }
        }
        return "cms/bond/bondRepayPlan/addCmsBondRepayPlan";
    }
    /**
     * 新增/修改-保存债券还款计划修改数据
     *
     * @param cmsBondRepayPlanVo
     * @return
     */
    @LogMessage(content = "新增/修改-保存LeaseCapitalPlan", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdate(@PathVariable("operator") String operator, CmsBondRepayPlanVo cmsBondRepayPlanVo)
            throws ParseException {
        AjaxJson json = success();
        json.setObj(cmsBondRepayPlanService.saveOrUpdate(operator, cmsBondRepayPlanVo, getCurrentUser()));
        return json;
    }
    /**
     * 债券还款计划登记表详情页
     *
     * @return
     */
    @LogMessage(content = "还款计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
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
        return "cms/bond/bondRepayPlan/viewCmsBondRepayPlan";
    }
    /**
     * 债券还款计划模块加载还款计划
     *
     * @param registerId
     *            债劵注册id
     * @return
     */
    @PostMapping("/listPlanDtlByRegisterId")
    @ResponseBody
    public List<CmsBondRepayPlanDtlVo> listPlanDtlByRegisterId(String registerId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.getByRegisterId(registerId);
        return list;
    }
    /**
     * 债券还款计划模块加载还款计划
     *
     * @param issueId
     *            债券发行Id
     * @return
     */
    @PostMapping("/listPlanDtlByIssueId")
    @ResponseBody
    public List<CmsBondRepayPlanDtlVo> listPlanDtlByIssueId(String issueId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.listPlanDtlByIssueId(issueId);
        return list;
    }
    /**
     * 还款计划明细
     *
     * @param registerId
     *            债劵注册id
     * @param showSign
     *            显示标识 0查看.1带选择
     * @return
     */
    @GetMapping("/getPlanDtlList")
    public String list(Model model, String registerId, String planDtlId, String showSign) {
        model.addAttribute("showSign", showSign);
        model.addAttribute("interestID", planDtlId);
        model.addAttribute("registerId", registerId);
        List<CmsBondIssueInfoVo> cmsBondIssueInfoVos = cmsBondIssueInfoService.getRegisterById(registerId);
        String issueId = "";
        String bondIssueNum = "";
        if (!ListUtils.isNullOrEmpty(cmsBondIssueInfoVos)) {
            CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoVos.get(0);
            issueId = cmsBondIssueInfoVo.getId();
            bondIssueNum = cmsBondIssueInfoVo.getBondIssueNum();
        }
        model.addAttribute("issueId", issueId);
        model.addAttribute("bondIssueNum", bondIssueNum);
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.getByRegisterId(registerId);
        for (CmsBondRepayPlanDtlVo vo : list) {
            // 最大输入金额控制
            BigDecimal sendBalance = cmsBondPayService.completeSendBalance(vo.getIssueId(), null);
            vo.setUnpaidMoney(sendBalance);
        }
        model.addAttribute("list", list);
        return "cms/bond/bondPay/cmsBondPayWay";
    }
    /**
     * 导出债券还款计划模板
     *
     * @param request
     * @param response
     * @param planVO
     * @throws Exception
     */
    @GetMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response, CmsBondRepayPlanVo planVO)
            throws Exception {
        cmsBondRepayPlanService.exportBudget(request, response, planVO);
    }
    /**
     * 债券还款计划登记表详情页
     *
     * @return
     */
    @LogMessage(content = "还款计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewQuery/{id}")
    public String viewQuery(@PathVariable("id") String id, Model model) {
        List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = new ArrayList<CmsBondRepayPlanDtlVo>();
        CmsBondRegisterInfoVo cmsBondRegisterInfoVo = new CmsBondRegisterInfoVo();
        CmsBondRepayPlanVo cmsBondRepayPlanVo = cmsBondRepayPlanService.getInfoById(id);
        if (StringUtils.isNotEmpty(cmsBondRepayPlanVo)) {
            String registerId = cmsBondRepayPlanVo.getId();
            cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            cmsBondRepayPlanDtlVoList = cmsBondRepayPlanDtlService.getByRegisterId(registerId);
        }
        model.addAttribute("leaseCapitalPlanDtlId", id);
        model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
        model.addAttribute("leaseCapitalPlanDelVoList", cmsBondRepayPlanDtlVoList);
        return "cms/bond/leasecapitalplan/viewLeaseCapitalPlanQuery";
    }
    // 审批功能取消
    // /********************审批**********************/
    // /**
    // * 申请审批列表-页面跳转
    // */
    // @LogMessage(content = "查询还款计划审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    // @GetMapping("/listApprove")
    // public String listApprove(String flowStatus, String cycleTime, Model model) {
    // model.addAttribute("flowStatus", flowStatus);
    // model.addAttribute("cycleTime", cycleTime);
    // return "cms/bond/approve/listBondRepayPlanApprove";
    // }
    //
    // /**
    // * 申请审批列表 - 数据
    // */
    // @PostMapping("/dataTableApprove")
    // @ResponseBody
    // public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
    // return cmsBondRepayPlanService.dataTableApprove(dataTablesInput);
    // }
    //
    // /**
    // * 申请审批 详情页面
    // */
    // @GetMapping("/viewApprove")
    // public String viewApprove(String businessId, String taskId, Model model) {
    // if (!EmptyUtils.isEmpty(businessId)) {
    // CmsBondRepayPlan info = cmsBondRepayPlanService.get(businessId);
    // if (StringUtils.isNotEmpty(info) && StringUtils.isNotEmpty(info.getRegisterId())) {
    // CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(info.getRegisterId());
    // model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
    // }
    //
    // List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = new ArrayList<CmsBondRepayPlanDtlVo>();
    // if (StringUtils.isNotEmpty(info.getRegisterId())) {
    // cmsBondRepayPlanDtlVoList = cmsBondRepayPlanDtlService.getByRegisterId(info.getRegisterId());
    // }
    // model.addAttribute("cmsBondRepayPlanDtlVoList", cmsBondRepayPlanDtlVoList);
    // }
    // model.addAttribute("taskId", taskId);//必须要做此步骤
    // return "cms/bond/approve/viewBondRepayPlanApprove";
    // }
}
