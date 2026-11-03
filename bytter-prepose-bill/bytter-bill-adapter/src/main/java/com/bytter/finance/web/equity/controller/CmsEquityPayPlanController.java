package com.bytter.finance.web.equity.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlanDetail;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanUpdateService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 缴付/过户计划登记
 */
@Controller
@RequestMapping("/cms/equity/payPlan")
public class CmsEquityPayPlanController extends BaseController {
    @Autowired
    private CmsEquityPayPlanService cmsEquityPayPlanService;
    @Autowired
    private CmsEquityPayPlanUpdateService cmsEquityPayPlanUpdateService;
    @Autowired
    private CmsEquityPayPlanDetailService cmsEquityPayPlanDetailService;
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    /**
     * 查询缴付/过户计划登记-页面跳转
     */
    @LogMessage(content = "查询缴付/过户计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/equity/listEquityPayPlan";
    }
    /**
     * 查询缴付/过户计划修改-页面跳转
     */
    @LogMessage(content = "查询缴付/过户计划修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/payPlanUpdatelist")
    public String payPlanUpdatelist(Model model) {
        return "cms/equity/listEquityPayPlanUpdate";
    }
    /**
     * 缴付/过户计划登记列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityPayPlanService.getDataEquityPayPlanTable(dataTablesInput, getCurrentUser());
    }
    /**
     * 缴付/过户计划修改列表 - 数据
     */
    @PostMapping("/updateDatatable")
    @ResponseBody
    public DataTablesOutput updateDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityPayPlanUpdateService.getDataEquityPayPlanUpdateTable(dataTablesInput,
                getCurrentUser());
    }
    /**
     * 缴付/过户计划登记页面跳转
     *
     * @return
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, Model model) {
        model.addAttribute("equityId", id);
        return "cms/equity/addEquityPayPlan";
    }
    /**
     * 修改-页面跳转
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("equityId", id);
        return "cms/equity/updateEquityPayPlan";
    }
    /**
     * 股权融资明细列表 - 数据
     */
    @PostMapping("/infoDetailDatatable")
    @ResponseBody
    public List<CmsEquityInfoDetailVO> infoDetailDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_0);// 审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_1);// 计划审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_5);// 计划审批中
        // bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);//计划审批通过
        // 根据融资主信息id查询融资明细数据
        return cmsEquityInfoService.getDetails(id, bizStatuss);
    }
    /**
     * 股权融资明细列表 - 数据
     */
    @PostMapping("/infoDetailUpdateDatatable")
    @ResponseBody
    public List<CmsEquityInfoDetailVO> infoDetailUpdateDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);// 计划登记审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_11);// 计划修改审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_15);// 计划修改审批中
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_20);// 计划审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);// 缴付中
        // 根据融资主信息id查询融资明细数据
        return cmsEquityInfoService.getDetails(id, bizStatuss);
    }
    /**
     * 计划登记页面跳转
     *
     * @return
     */
    @GetMapping("/addPayPlanDetail/{equityCode}")
    public String addPayPlanDetail(@PathVariable("equityCode") String equityCode, Model model) {
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        model.addAttribute("infoDetail", infoDetail);
        // 计划主表
        CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
        model.addAttribute("payPlanVO", payPlanVO);
        return "cms/equity/addEquityPayPlanDetail";
    }
    /**
     * 计划修改页面跳转
     *
     * @return
     */
    @GetMapping("/updatePayPlanDetail/{equityCode}")
    public String updatePayPlanDetail(@PathVariable("equityCode") String equityCode, Model model) {
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        model.addAttribute("infoDetail", infoDetail);
        // 计划主表
        CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
        model.addAttribute("payPlanVO", payPlanVO);
        return "cms/equity/updateEquityPayPlanDetail";
    }
    /**
     * 加载计划明细数据
     *
     * @param planVO
     * @return
     */
    @PostMapping("/getPlanDetailByEquityCode")
    @ResponseBody
    public List<CmsEquityPayPlanDetailVO> listPlanDetail(CmsEquityPayPlanVO planVO) {
        // 计划明细
        List<CmsEquityPayPlanDetailVO> planDetailList = cmsEquityPayPlanDetailService
                .listPayPlanDetailByEquityCodeOrPlanId(null, planVO.getEquityCode());
        return planDetailList;
    }
    /**
     * 缴付/过户计划登记保存
     */
    @LogMessage(content = "缴付/过户计划登记保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsEquityPayPlanVO vo) {
        AjaxJson json = success();
        cmsEquityPayPlanService.save(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 缴付/过户计划修改保存
     */
    @LogMessage(content = "缴付/过户计划修改保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/payPlanUpdate")
    @ResponseBody
    public AjaxJson payPlanUpdate(CmsEquityPayPlanVO vo) {
        AjaxJson json = success();
        cmsEquityPayPlanUpdateService.payPlanUpdate(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 查看-页面跳转
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(id, null);
            model.addAttribute("payPlanVO", payPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(payPlanVO.getEquityCode());
            model.addAttribute("infoDetail", infoDetail);
        }
        return "cms/equity/viewEquityPayPlanDetail";
    }
    /**
     * 根据融资编号查看-页面跳转
     */
    @GetMapping("/viewByEquityCode/{equityCode}")
    public String viewByEquityCode(@PathVariable("equityCode") String equityCode, Model model) {
        if (!EmptyUtils.isEmpty(equityCode)) {
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
            model.addAttribute("payPlanVO", payPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
            model.addAttribute("infoDetail", infoDetail);
            model.addAttribute("isApprove", false);
        }
        return "cms/equity/viewEquityPayPlanDetail";
    }
    /**
     * 保存明细
     */
    @LogMessage(content = "计划明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDetail")
    @ResponseBody
    public AjaxJson saveDetail(CmsEquityPayPlanDetail detail) {
        AjaxJson ajaxJson = cmsEquityPayPlanService.saveDetail(detail);
        return ajaxJson;
    }
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CmsEquityPayPlanDetailVO> list) {
        return cmsEquityPayPlanService.saveImport(list);
    }
    /******************** 审批 **********************/
    /**
     * 缴付/过户计划登记审批列表-页面跳转
     */
    @LogMessage(content = "查询缴付/过户计划登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/listApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityPayPlanApprove";
    }
    /**
     * 缴付/过户计划修改审批列表-页面跳转
     */
    @LogMessage(content = "查询缴付/过户计划修改审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/listUpdateApprove")
    public String listUpdateApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityPayPlanUpdateApprove";
    }
    /**
     * 缴付/过户计划登记审批列表 - 数据
     */
    @PostMapping("/datatableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityPayPlanService.getDatatableApprove(dataTablesInput);
    }
    /**
     * 缴付/过户计划修改审批列表 - 数据
     */
    @PostMapping("/datatableUpdateApprove")
    @ResponseBody
    public DataTablesOutput datatableUpdateApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityPayPlanUpdateService.datatableUpdateApprove(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(id, null);
            model.addAttribute("payPlanVO", payPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(payPlanVO.getEquityCode());
            model.addAttribute("infoDetail", infoDetail);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/equity/approve/viewEquityPayPlanDetailApprove";
    }
}
