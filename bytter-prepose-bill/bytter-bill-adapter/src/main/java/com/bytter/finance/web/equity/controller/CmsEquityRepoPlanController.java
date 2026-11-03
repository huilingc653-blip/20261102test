package com.bytter.finance.web.equity.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlanDetail;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanUpdateService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 股权融资回购计划
 */
@Controller
@RequestMapping("/cms/equity/repoPlan")
public class CmsEquityRepoPlanController extends BaseController {
    @Autowired
    private CmsEquityRepoPlanService cmsEquityRepoPlanService;
    @Autowired
    private CmsEquityRepoPlanUpdateService cmsEquityRepoPlanUpdateService;
    @Autowired
    private CmsEquityRepoPlanDetailService cmsEquityRepoPlanDetailService;
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    /**
     * 查询股权融资回购计划登记-页面跳转
     */
    @LogMessage(content = "查询股权融资回购计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/equity/listEquityRepoPlan";
    }
    /**
     * 查询股权融资回购计划修改-页面跳转
     */
    @LogMessage(content = "查询股权融资回购计划修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/updateList")
    public String updateList(Model model) {
        return "cms/equity/listEquityRepoPlanUpdate";
    }
    /**
     * 股权融资回购计划登记列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityRepoPlanService.getDataEquityRepoPlanTable(dataTablesInput, getCurrentUser());
    }
    /**
     * 股权融资回购计划修改列表 - 数据
     */
    @PostMapping("/updateDatatable")
    @ResponseBody
    public DataTablesOutput updateDatatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsEquityRepoPlanUpdateService.getDataEquityRepoPlanUpdateTable(dataTablesInput,
                getCurrentUser());
        return out;
    }
    /**
     * 股权融资回购计划登记页面跳转
     *
     * @return
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, Model model) {
        model.addAttribute("equityId", id);
        return "cms/equity/addEquityRepoPlan";
    }
    /**
     * 修改-页面跳转
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("equityId", id);
        return "cms/equity/updateEquityRepoPlan";
    }
    /**
     * 股权融资明细列表 - 数据
     */
    @PostMapping("/infoDetailDatatable")
    @ResponseBody
    public List<CmsEquityInfoDetailVO> infoDetailDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_30);// 缴付/过户完成
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_31);// 回购计划审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_35);// 回购计划审批中
        // bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);//回购计划审批通过
        // 根据融资主信息id查询融资明细数据
        List<CmsEquityInfoDetailVO> list = cmsEquityInfoService.getDetails(id, bizStatuss);
        return list;
    }
    /**
     * 股权融资明细列表 - 数据
     */
    @PostMapping("/infoDetailUpdateDatatable")
    @ResponseBody
    public List<CmsEquityInfoDetailVO> infoDetailUpdateDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);// 回购计划登记审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_41);// 回购计划修改审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_45);// 回购计划修改审批中
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);// 回购计划审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);// 回购中
        // 根据融资主信息id查询融资明细数据
        List<CmsEquityInfoDetailVO> list = cmsEquityInfoService.getDetails(id, bizStatuss);
        return list;
    }
    /**
     * 回购计划登记页面跳转
     *
     * @return
     */
    @GetMapping("/addRepoPlanDetail/{equityCode}")
    public String addRepoPlanDetail(@PathVariable("equityCode") String equityCode, Model model) {
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        model.addAttribute("infoDetail", infoDetail);
        // 回购计划主表
        CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
        model.addAttribute("repoPlanVO", repoPlanVO);
        return "cms/equity/addEquityRepoPlanDetail";
    }
    /**
     * 回购计划修改页面跳转
     *
     * @return
     */
    @GetMapping("/updateRepoPlanDetail/{equityCode}")
    public String updateRepoPlanDetail(@PathVariable("equityCode") String equityCode, Model model) {
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        model.addAttribute("infoDetail", infoDetail);
        // 回购计划主表
        CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
        model.addAttribute("repoPlanVO", repoPlanVO);
        return "cms/equity/updateEquityRepoPlanDetail";
    }
    /**
     * 加载回购计划明细数据
     *
     * @param planVO
     * @return
     */
    @PostMapping("/getRepoDetailByEquityCode")
    @ResponseBody
    public List<CmsEquityRepoPlanDetailVO> listPlanDetail(CmsEquityRepoPlanVO planVO) {
        // 回购计划明细
        List<CmsEquityRepoPlanDetailVO> planDetailList = cmsEquityRepoPlanDetailService
                .listRepoPlanDetailByEquityCodeOrPlanId(null, planVO.getEquityCode());
        return planDetailList;
    }
    /**
     * 股权融资回购计划登记保存
     */
    @LogMessage(content = "股权融资回购计划登记保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsEquityRepoPlanVO vo) {
        AjaxJson json = success();
        cmsEquityRepoPlanService.save(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 股权融资回购计划修改保存
     */
    @LogMessage(content = "股权融资回购计划修改保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/repoPlanUpdate")
    @ResponseBody
    public AjaxJson repoPlanUpdate(CmsEquityRepoPlanVO vo) {
        AjaxJson json = success();
        cmsEquityRepoPlanUpdateService.repoPlanUpdate(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 查看-页面跳转
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            // 回购计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(id, null);
            model.addAttribute("repoPlanVO", repoPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(repoPlanVO.getEquityCode());
            model.addAttribute("infoDetail", infoDetail);
        }
        return "cms/equity/viewEquityRepoPlanDetail";
    }
    /**
     * 根据融资编号查看-页面跳转
     */
    @GetMapping("/viewByEquityCode/{equityCode}")
    public String viewByEquityCode(@PathVariable("equityCode") String equityCode, Model model) {
        if (!EmptyUtils.isEmpty(equityCode)) {
            // 回购计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
            model.addAttribute("repoPlanVO", repoPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
            model.addAttribute("infoDetail", infoDetail);
            model.addAttribute("isApprove", false);
        }
        return "cms/equity/viewEquityRepoPlanDetail";
    }
    /**
     * 保存明细
     */
    @LogMessage(content = "回购计划明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDetail")
    @ResponseBody
    public AjaxJson saveDetail(CmsEquityRepoPlanDetail detail) {
        AjaxJson ajaxJson = cmsEquityRepoPlanService.saveDetail(detail);
        return ajaxJson;
    }
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CmsEquityRepoPlanDetailVO> list) {
        return cmsEquityRepoPlanService.saveImport(list);
    }
    /******************** 审批 **********************/
    /**
     * 股权融资回购计划登记审批列表-页面跳转
     */
    @LogMessage(content = "查询股权融资回购计划登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/listApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityRepoPlanApprove";
    }
    /**
     * 股权融资回购计划修改审批列表-页面跳转
     */
    @LogMessage(content = "查询股权融资回购计划修改审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/listUpdateApprove")
    public String listUpdateApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityRepoPlanUpdateApprove";
    }
    /**
     * 股权融资回购计划登记审批列表 - 数据
     */
    @PostMapping("/datatableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityRepoPlanService.getDatatableApprove(dataTablesInput);
    }
    /**
     * 股权融资回购计划修改审批列表 - 数据
     */
    @PostMapping("/datatableUpdateApprove")
    @ResponseBody
    public DataTablesOutput datatableUpdateApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityRepoPlanUpdateService.datatableUpdateApprove(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            // 回购计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(id, null);
            model.addAttribute("repoPlanVO", repoPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(repoPlanVO.getEquityCode());
            model.addAttribute("infoDetail", infoDetail);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/equity/approve/viewEquityRepoPlanDetailApprove";
    }
}
