package com.bytter.finance.web.equity.controller;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityPayThe;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityPayTheService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayTheVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 股权融资缴付到账
 */
@Controller
@RequestMapping("/cms/equity/payThe")
public class CmsEquityPayTheController extends BaseController {
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityPayTheService cmsEquityPayTheService;
    @Autowired
    private CmsEquityPayPlanDetailService cmsEquityPayPlanDetailService;
    @Autowired
    private CmsEquityPayPlanService cmsEquityPayPlanService;
    /**
     * 查询股权融资列表-页面跳转
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/equity/listEquityPayThe";
    }
    /**
     * 股权融资列表 - 数据
     */
    @PostMapping("/datatablePass")
    @ResponseBody
    public DataTablesOutput datatablePass(@RequestBody DataTablesInput dataTablesInput) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_20);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_25);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21);// 打回
        return cmsEquityInfoService.getDataEquityPassTable(dataTablesInput, getCurrentUser(),
                ResourceConstant.CMS_EQUITY_PAY_VIEW, bizStatus);
    }
    /**
     * 缴付明细列表-页面跳转
     */
    @GetMapping("/equityDetails/{id}")
    public String equityDetails(@PathVariable("id") String id, Model model) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_20);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_25);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21);// 打回
        List<CmsEquityInfoDetailVO> detailList = cmsEquityInfoService.getDetails(id, bizStatus);
        model.addAttribute("list", detailList);
        return "cms/equity/listPayEquityDetail";
    }
    /**
     * 缴付-页面跳转
     */
    @GetMapping("/payEquity")
    public String payEquity(String equityCode, String bizStatus, String type, Model model) {
        CmsEquityPayTheVO payTheVO = new CmsEquityPayTheVO();
        CmsEquityInfoDetailVO detailVO = cmsEquityInfoService.getDetail(equityCode);
        CmsEquityInfo info = cmsEquityInfoService.get(detailVO.getEquityId());
        model.addAttribute("corpId", info.getCorpId());
        model.addAttribute("curId", info.getCurId());
        if (bizStatus.equals(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22)
                || bizStatus.equals(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21)) {
            payTheVO = cmsEquityPayTheService.getByCode(equityCode, "temp").get(0);// 找暂存打回的缴付
        } else {
            cmsEquityPayTheService.createPayThe(payTheVO, detailVO);
            payTheVO.setPayCurId(info.getCurId());
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
            payTheVO.setPlanId(payPlanVO.getId());
        }
        model.addAttribute("payTheVO", payTheVO);
        if (type.equals("1")) {
            return "cms/equity/payEquity";
        } else {
            return "cms/equity/moveEquity";
        }
    }
    /**
     * 缴付明细列表-页面跳转
     */
    @GetMapping("/equityPlans")
    public String equityPlans(String equityCode, String planDetailId, Model model) {
        // 计划明细
        List<CmsEquityPayPlanDetailVO> planDetailList = cmsEquityPayPlanDetailService
                .listPayPlanDetailByEquityCodeOrPlanId(null, equityCode);
        model.addAttribute("planDetailList", planDetailList);
        model.addAttribute("detailPlanID", planDetailId);
        return "cms/equity/listEquityDetailPlans";
    }
    /**
     * 提交
     */
    @LogMessage(content = "缴付到账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/savePay")
    @ResponseBody
    public AjaxJson savePay(CmsEquityPayTheVO cmsEquityPayTheVO, String saveType) {
        cmsEquityPayTheService.savePay(cmsEquityPayTheVO, saveType, getCurrentUser());
        return success();
    }
    /**
     * 折合币别
     *
     * @param curId
     * @return
     */
    @PostMapping("/conver")
    @ResponseBody
    public BigDecimal getConverRate(String curId, String payCurId, String curPreTime) {
        return cmsEquityPayTheService.getConverRate(curId, payCurId, DateUtils.parse(curPreTime, "yyyy-MM-dd"));
    }
    /**
     * 查看-页面跳转
     */
    @GetMapping("/viewPay")
    public String viewPay(String equityCode, String invWay, Model model) {
        if (StringUtils.isEmpty(equityCode) || "undefined".equals(equityCode)) {
            return null;
        }
        CmsEquityPayTheVO payTheVO = cmsEquityPayTheService.getByCode(equityCode, "comm").get(0);// 找审批中的缴付
        model.addAttribute("payTheVO", payTheVO);
        if (invWay.equals("1")) {
            return "cms/equity/viewPayEquity";
        } else {
            return "cms/equity/viewMoveEquity";
        }
    }
    /******************** 审批 **********************/
    /**
     * 股权缴付过户审批列表-页面跳转
     */
    @LogMessage(content = "查询股权缴付过户审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityPayApprove";
    }
    /**
     * 股权缴付过户审批列表 - 数据
     */
    @PostMapping("/datatableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityPayTheService.getDatatableApprove(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/payApprove")
    public String payApprove(String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            CmsEquityPayThe payThe = cmsEquityPayTheService.get(id);
            CmsEquityPayTheVO payTheVO = cmsEquityPayTheService.getByCode(payThe.getEquityCode(), "comm").get(0);// 找审批中的缴付
            model.addAttribute("payThe", payTheVO);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/equity/approve/viewEquityPayApprove";
    }
}
