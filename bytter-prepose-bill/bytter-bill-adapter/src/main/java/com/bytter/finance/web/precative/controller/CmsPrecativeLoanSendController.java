package com.bytter.finance.web.precative.controller;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款发放表
 * @date 2017-07-29 10:45:29
 */
@Controller
@RequestMapping("/cms/precative/loansend")
public class CmsPrecativeLoanSendController extends BaseController {
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    /**
     * 委托贷款发放表列表 页面跳转
     */
    // @LogMessage(content = "查询委托贷款发放表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/precative/loansend/listLoanSend";
    }
    /**
     * 委托贷款发放表 table列表
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanSendService.getLoanSend(dataTablesInput, getUserId());
    }
    /**
     * 委托贷款发放表新增页面跳转
     */
    @GetMapping("/add")
    public String add(CmsPrecativeLoanSend cmsPrecativeLoanSend, Model model) {
        if (StringUtils.isNotEmpty(cmsPrecativeLoanSend.getId())) {
            cmsPrecativeLoanSend = cmsPrecativeLoanSendService.get(cmsPrecativeLoanSend.getId());
            model.addAttribute("cmsPrecativeLoanSendPage", cmsPrecativeLoanSend);
        }
        return "listloansend/addCmsPrecativeLoanSend";
    }
    /**
     * 委托贷款发放表详情页
     */
    // @LogMessage(content = "查询委托贷款发放表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String fatherCode, Model model) {
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendService.getLoanSendVoByFatherCode(fatherCode);
        model.addAttribute("sendList", list);
        model.addAttribute("billCode", fatherCode);
        model.addAttribute("show", "1");
        return "cms/precative/loansend/viewLoanSend";
    }
    @GetMapping("/viewbybillcode")
    public String viewByBillCode(String billCode, Model model) {
        PrecativeLoanSendVo vo = cmsPrecativeLoanSendService.getSendByBillCode(billCode);
        List<PrecativeLoanSendVo> list = new ArrayList<PrecativeLoanSendVo>();
        list.add(vo);
        model.addAttribute("sendList", list);
        model.addAttribute("billCode", vo.getFatherCode());
        model.addAttribute("show", "0");
        return "cms/precative/loansend/viewLoanSend";
    }
    /**
     * 添加委托贷款发放表
     */
    // @LogMessage(content = "添加委托贷款发放表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsPrecativeLoanSend cmsPrecativeLoanSend) {
        cmsPrecativeLoanSendService.save(cmsPrecativeLoanSend);
        return success();
    }
    /**
     * 修改委托贷款发放表
     */
    // @LogMessage(content = "修改委托贷款发放表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsPrecativeLoanSend cmsPrecativeLoanSend) {
        CmsPrecativeLoanSend t = cmsPrecativeLoanSendService.get(cmsPrecativeLoanSend.getId());
        BeanUtils.copyNotNullProperties(cmsPrecativeLoanSend, t);
        cmsPrecativeLoanSendService.saveOrUpdate(t);
        return success();
    }
    /****
     * 已还记录查询
     */
    @GetMapping("/sendList")
    public String repayList(String fatherCode, Model model) {
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendService.getLoanSendVoByFatherCode(fatherCode);
        model.addAttribute("sendList", list);
        return "cms/precative/listSend";
    }
    /**
     * 删除委托贷款发放表
     */
    // @LogMessage(content = "删除委托贷款发放表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsPrecativeLoanSend cmsPrecativeLoanSend = cmsPrecativeLoanSendService.get(id);
        cmsPrecativeLoanSendService.deleteEntity(cmsPrecativeLoanSend);
        return success();
    }
    /**
     * 发放申请页面跳转
     */
    @GetMapping("/send")
    public String send(PrecativeLoanSendVo pre, String type, Model model) {
        if (StringUtils.isNotEmpty(pre.getInfoBillCode())) {
            CmsPrecativeLoanInfoVo preVoInfo = cmsPrecativeLoanInfoService.queryInfo(pre.getInfoBillCode());
            preVoInfo.setMoney(new BigDecimal(preVoInfo.getMoney().doubleValue()));
            PrecativeLoanSendVo preVoSend = cmsPrecativeLoanSendService.getLoanSend(preVoInfo.getBillCode());
            int sendCount = cmsPrecativeLoanSendService.sendCount(preVoInfo.getBillCode());
            // 判断是否本位币
            boolean isNatualCurrency = cmsPrecativeLoanSendService.isNatualCurrency(preVoInfo.getCurId(),
                    preVoInfo.getCorpId());
            /*
             * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); String opdate = sdf.format(new Date());
             */
            if (!EmptyUtils.isEmpty(preVoSend.getId())) {
                CmsRepaymentSchedule repayPlan = repaymentScheduleService
                        .getCmsRepaymentScheduleByBillCode(preVoSend.getBillCode());
                if (!EmptyUtils.isEmpty(repayPlan)) {
                    preVoSend.setPlanId(repayPlan.getId());
                }
            }
            model.addAttribute("type", type);
            model.addAttribute("isNatualCurrency", isNatualCurrency);
            model.addAttribute("precativeInfoPage", preVoInfo);
            model.addAttribute("opDate", preVoInfo.getCreateDate());
            model.addAttribute("precativeSendPage", preVoSend);
            model.addAttribute("sendCount", sendCount);
        }
        return "cms/precative/loansend/addLoanSend";
    }
    /**
     * 发放打回
     */
    @PostMapping("/back/{infoId}")
    @ResponseBody
    public AjaxJson back(@PathVariable("infoId") String infoId) {
        cmsPrecativeLoanSendService.back(infoId, getCurrentUser());
        return success();
    }
    /**
     * 发放提交暂存
     */
    @PostMapping("/submitOrTemp")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, PrecativeLoanSendVo pre, String saveType) {
        cmsPrecativeLoanSendService.submitOrTemp(pre, saveType, getCurrentUser(), false);
        return success();
    }
    /**
     * 跳转至审核列表
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/precative/loansend/listSendApprove";
    }
    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanSendService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(businessId);
        CmsPrecativeLoanInfoVo preVoInfo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
        model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.addAttribute("precativeSendPage", sendVo);
        model.addAttribute("precativeInfoPage", preVoInfo);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/precative/loansend/viewSendApprove";
    }
    /**
     * 跳转至手机审核列表
     */
    @GetMapping("/mobileSendApprove")
    public String mobileSendApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendById(businessId);
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
            model.addAttribute("precativeSendPage", sendVo);
            model.addAttribute("precativePage", preVo);
            model.addAttribute("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
            model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
            model.addAttribute("taskId", taskId);
        }
        return "cms/precative/loansend/mobileSendApprove";
    }
    /**
     * 引入贷款发放
     */
    @PostMapping("/pullLoan")
    public String pullLoan(HttpServletRequest request, Model model) {
        String billCode = request.getParameter("billCode");
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendService.getLoanSendVoByFatherCode(billCode);
        model.addAttribute("sendList", list);
        model.addAttribute("billCode", billCode);
        return "cms/precative/loansend/viewLoan";
    }
}
