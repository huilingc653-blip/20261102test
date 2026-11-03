package com.bytter.finance.web.lease.controller;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanDelService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁计划
 * @date 2022-04-13 10:07:46
 */
@Controller
@RequestMapping("/cms/lease/leasecapitalplan")
public class LeaseCapitalPlanController extends BaseController {
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    /**
     * 租金计划登记 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询租金计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{isSaveSign}")
    public String list(@PathVariable("isSaveSign") String isSaveSign, Model model) {
        String resource = "";
        if (CmsConstant.IS_TRUE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_LEASE_CMSLEASECAPITALPLAN_QUERY;
        }
        if (CmsConstant.IS_FALSE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_LEASE_UPDATELEASECAPITALPLAN_QUERY;
        }
        model.addAttribute("isSaveSign", isSaveSign);
        model.addAttribute("resource", resource);
        return "cms/lease/leasecapitalplan/listLeaseCapitalPlan";
    }
    /**
     * 租金计划登记 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(String isSaveSign, @RequestBody DataTablesInput dataTablesInput) {
        return leaseCapitalPlanService.getDataTablesUnion(dataTablesInput, isSaveSign);
    }
    /**
     * 租金计划登记表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(String leaseId, Model model) {
        if (StringUtils.isNotEmpty(leaseId)) {
            CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
            model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
            if (!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO)) {
                LeaseCapitalPlanVO leaseCapitalPlanVo = leaseCapitalPlanService.getByLeaseId(leaseId);
                if (!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    leaseCapitalPlanVo.setRepayType(CmsConstant.REPAY_TYPE_07);
                    model.addAttribute("leaseCapitalPlanVo", leaseCapitalPlanVo);
                }
            }
        }
        return "cms/lease/leasecapitalplan/addLeaseCapitalPlan";
    }
    /**
     * 新增/修改-保存租金计划修改数据
     *
     * @param leaseCapitalPlanVO
     * @return
     */
    @LogMessage(content = "新增/修改-保存LeaseCapitalPlan", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdate(@PathVariable("operator") String operator, LeaseCapitalPlanVO leaseCapitalPlanVO)
            throws ParseException {
        AjaxJson json = success();
        json.setObj(leaseCapitalPlanService.saveOrUpdate(operator, leaseCapitalPlanVO, getCurrentUser()));
        return json;
    }
    /**
     * 租金计划登记表详情页
     *
     * @return
     */
    @LogMessage(content = "租金计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{leaseId}")
    public String view(@PathVariable("leaseId") String leaseId, Model model) {
        List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<LeaseCapitalPlanDelVO>();
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = new CmsFinancingLeaseInfoVO();
        if (StringUtils.isNotEmpty(leaseId)) {
            leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(leaseId);
            cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
        }
        model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
        model.addAttribute("leaseCapitalPlanDelVoList", leaseCapitalPlanDelVoList);
        return "cms/lease/leasecapitalplan/viewLeaseCapitalPlan";
    }
    /**
     * 租金计划模块加载还款计划
     *
     * @param leasePlanId
     * @return
     */
    @PostMapping("/listLeaseCapitalPlanDel")
    @ResponseBody
    public List<LeaseCapitalPlanDelVO> listLeaseCapitalPlanDel(String leasePlanId) {
        List<LeaseCapitalPlanDelVO> list = leaseCapitalPlanDelService.getByLeasePlanId(leasePlanId);
        return list;
    }
    /**
     * 预计利息明细
     *
     * @param leaseId
     * @param showSign
     *            显示标识 0查看.1带选择
     * @return
     */
    @GetMapping("/getPlanDelList")
    public String list(Model model, String leasePlanDelId, String leaseId, String showSign) {
        model.addAttribute("showSign", showSign);
        model.addAttribute("interestID", leasePlanDelId);
        List<LeaseCapitalPlanDelVO> list = leaseCapitalPlanDelService.getInfoByLeaseId(leaseId);
        model.addAttribute("list", list);
        return "cms/lease/leasepay/cmsLeasePayWay";
    }
    /**
     * 导出租金计划模板
     *
     * @param request
     * @param response
     * @param leaseCapitalPlanVO
     * @throws Exception
     */
    @GetMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response,
            LeaseCapitalPlanVO leaseCapitalPlanVO) throws Exception {
        leaseCapitalPlanService.exportBudget(request, response, leaseCapitalPlanVO);
    }
    /**
     * 融资租赁租金查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询融资租赁租金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listQuery")
    public String listQuery(Model model) {
        return "cms/lease/leasecapitalplan/listLeaseCapitalPlanQuery";
    }
    /**
     * 融资租赁租金查询 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableQuery")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody DataTablesInput dataTablesInput) {
        return leaseCapitalPlanDelService.getDataTablesQuery(dataTablesInput);
    }
    /**
     * 租金计划登记表详情页
     *
     * @return
     */
    @LogMessage(content = "租金计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewQuery/{id}")
    public String viewQuery(@PathVariable("id") String id, Model model) {
        List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<LeaseCapitalPlanDelVO>();
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = new CmsFinancingLeaseInfoVO();
        LeaseCapitalPlanDelVO leaseCapitalPlanDelVO = leaseCapitalPlanDelService.getInfoById(id);
        if (StringUtils.isNotEmpty(leaseCapitalPlanDelVO)) {
            String leaseId = leaseCapitalPlanDelVO.getLeaseId();
            cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
            leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(leaseId);
        }
        model.addAttribute("leaseCapitalPlanDelId", id);
        model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
        model.addAttribute("leaseCapitalPlanDelVoList", leaseCapitalPlanDelVoList);
        return "cms/lease/leasecapitalplan/viewLeaseCapitalPlanQuery";
    }
    /******************** 审批 **********************/
    /**
     * 申请审批列表-页面跳转
     */
    @LogMessage(content = "查询租金计划审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInfoApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/approve/listLeasecapitalplanApprove";
    }
    /**
     * 申请审批列表 - 数据
     */
    @PostMapping("/datatableInfoApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput dataTablesInput) {
        return leaseCapitalPlanService.getDataInfoApproveTables(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            LeaseCapitalPlan info = leaseCapitalPlanService.get(businessId);
            if (StringUtils.isNotEmpty(info) && StringUtils.isNotEmpty(info.getLeaseId())) {
                CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService
                        .getLeaseById(info.getLeaseId());
                model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
            }
            List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<LeaseCapitalPlanDelVO>();
            if (StringUtils.isNotEmpty(info.getLeaseId())) {
                leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(info.getLeaseId());
            }
            model.addAttribute("leaseCapitalPlanDelVoList", leaseCapitalPlanDelVoList);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/lease/approve/viewLeasecapitalplanApprove";
    }
}
