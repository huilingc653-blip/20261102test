package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanDelService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseApproveListVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanAddVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanListVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanPayWayVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "/v1-preposebill/cms/lease/leasecapitalplan", tags = "租赁计划")
@RestController("v1LeaseCapitalPlanController")
@RequestMapping("/v1-preposebill/cms/lease/leasecapitalplan")
public class LeaseCapitalPlanController extends BaseController {
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;

    /**
     * 租金计划登记列表页面数据
     */
    @ApiOperation("租金计划登记列表页面数据")
    @LogMessage(content = "查询租金计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{isSaveSign}")
    public AjaxJson<LeaseCapitalPlanListVO> list(@PathVariable("isSaveSign") String isSaveSign) {
        LeaseCapitalPlanListVO result = new LeaseCapitalPlanListVO();
        String resource = "";
        if (CmsConstant.IS_TRUE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_LEASE_CMSLEASECAPITALPLAN_QUERY;
        }
        if (CmsConstant.IS_FALSE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_LEASE_UPDATELEASECAPITALPLAN_QUERY;
        }
        result.setIsSaveSign(isSaveSign);
        result.setResource(resource);
        return AjaxJson.success(result);
    }

    /**
     * 租金计划登记table列表
     */
    @ApiOperation("租金计划登记table列表")
    @LogMessage(content = "查询租金计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(String isSaveSign, @RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseCapitalPlanService.getDataTablesUnion(input, isSaveSign);
        return AjaxJson.success(output);
    }

    /**
     * 租金计划登记新增页面数据
     */
    @ApiOperation("租金计划登记新增页面数据")
    @GetMapping("/add")
    public AjaxJson<LeaseCapitalPlanAddVO> add(String leaseId) {
        LeaseCapitalPlanAddVO result = new LeaseCapitalPlanAddVO();
        if (StringUtils.isNotEmpty(leaseId)) {
            CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
            result.setLeaseInfoVo(cmsFinancingLeaseInfoVO);
            if (!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO)) {
                LeaseCapitalPlanVO leaseCapitalPlanVo = leaseCapitalPlanService.getByLeaseId(leaseId);
                if (!EmptyUtils.isEmpty(leaseCapitalPlanVo)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    leaseCapitalPlanVo.setRepayType(CmsConstant.REPAY_TYPE_07);
                    result.setLeaseCapitalPlanVo(leaseCapitalPlanVo);
                }
            }
        }
        return AjaxJson.success(result);
    }

    /**
     * 新增/修改-保存租金计划修改数据
     */
    @ApiOperation("新增/修改-保存租金计划")
    @LogMessage(content = "新增/修改-保存LeaseCapitalPlan", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson saveOrUpdate(@RequestBody LeaseCapitalPlanVO leaseCapitalPlanVO) throws ParseException {
        AjaxJson json = AjaxJson.success();
        json.setObj(leaseCapitalPlanService.saveOrUpdate(leaseCapitalPlanVO.getOperator(), leaseCapitalPlanVO, getCurrentUser()));
        return json;
    }

    /**
     * 租金计划模块加载还款计划明细
     */
    @ApiOperation("加载租金计划还款明细")
    @PostMapping("/listLeaseCapitalPlanDel")
    public AjaxJson<List<LeaseCapitalPlanDelVO>> listLeaseCapitalPlanDel(String leasePlanId) {
        List<LeaseCapitalPlanDelVO> list = leaseCapitalPlanDelService.getByLeasePlanId(leasePlanId);
        return AjaxJson.success(list);
    }

    /**
     * 租金计划登记表详情页数据
     */
    @ApiOperation("租金计划登记表详情页数据")
    @LogMessage(content = "租金计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{leaseId}")
    public AjaxJson<LeaseCapitalPlanViewVO> view(@PathVariable("leaseId") String leaseId) {
        LeaseCapitalPlanViewVO result = new LeaseCapitalPlanViewVO();
        List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<>();
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = new CmsFinancingLeaseInfoVO();
        if (StringUtils.isNotEmpty(leaseId)) {
            leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(leaseId);
            cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
        }
        result.setLeaseInfoVo(cmsFinancingLeaseInfoVO);
        result.setLeaseCapitalPlanDelVoList(leaseCapitalPlanDelVoList);
        return AjaxJson.success(result);
    }

    /**
     * 预计利息明细
     *
     * @param leasePlanDelId 明细id
     * @param leaseId 租赁id
     * @param showSign 显示标识 0查看.1带选择
     */
    @ApiOperation("预计利息明细")
    @GetMapping("/getPlanDelList")
    public AjaxJson<LeaseCapitalPlanPayWayVO> getPlanDelList(String leasePlanDelId, String leaseId, String showSign) {
        LeaseCapitalPlanPayWayVO result = new LeaseCapitalPlanPayWayVO();
        result.setShowSign(showSign);
        result.setInterestID(leasePlanDelId);
        result.setList(leaseCapitalPlanDelService.getInfoByLeaseId(leaseId));
        return AjaxJson.success(result);
    }

    /**
     * 导出租金计划模板
     */
    @ApiOperation("导出租金计划模板")
    @GetMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response, LeaseCapitalPlanVO leaseCapitalPlanVO) throws Exception {
        leaseCapitalPlanService.exportBudget(request, response, leaseCapitalPlanVO);
    }

    /**
     * 融资租赁租金查询table列表
     */
    @ApiOperation("融资租赁租金查询列表")
    @PostMapping("/datatableQuery")
    public AjaxJson<DataTablesOutput> dataTableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseCapitalPlanDelService.getDataTablesQuery(input);
        return AjaxJson.success(output);
    }

    /**
     * 融资租赁租金查询详情页数据
     */
    @ApiOperation("融资租赁租金查询详情页数据")
    @LogMessage(content = "租金计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewQuery/{id}")
    public AjaxJson<LeaseCapitalPlanViewVO> viewQuery(@PathVariable("id") String id) {
        LeaseCapitalPlanViewVO result = new LeaseCapitalPlanViewVO();
        List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<>();
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = new CmsFinancingLeaseInfoVO();
        LeaseCapitalPlanDelVO leaseCapitalPlanDelVO = leaseCapitalPlanDelService.getInfoById(id);
        if (StringUtils.isNotEmpty(leaseCapitalPlanDelVO)) {
            String leaseId = leaseCapitalPlanDelVO.getLeaseId();
            cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
            leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(leaseId);
        }
        result.setLeaseCapitalPlanDelId(id);
        result.setLeaseInfoVo(cmsFinancingLeaseInfoVO);
        result.setLeaseCapitalPlanDelVoList(leaseCapitalPlanDelVoList);
        return AjaxJson.success(result);
    }

    /**
     * 申请审批列表页面数据
     */
    @ApiOperation("租金计划审批列表页面数据")
    @LogMessage(content = "查询租金计划审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInfoApprove")
    public AjaxJson<CmsLeaseApproveListVO> listInfoApprove(String flowStatus, String cycleTime) {
        CmsLeaseApproveListVO result = new CmsLeaseApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return AjaxJson.success(result);
    }

    /**
     * 租金计划审批列表数据
     */
    @ApiOperation("租金计划审批列表数据")
    @LogMessage(content = "查询租金计划审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableInfoApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseCapitalPlanService.getDataInfoApproveTables(input);
        return AjaxJson.success(output);
    }

    /**
     * 租金计划审批详情
     */
    @ApiOperation("租金计划审批详情")
    @GetMapping("/infoApprove")
    public AjaxJson<LeaseCapitalPlanViewVO> infoApprove(String businessId, String taskId) {
        LeaseCapitalPlanViewVO result = new LeaseCapitalPlanViewVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            LeaseCapitalPlan info = leaseCapitalPlanService.get(businessId);
            if (StringUtils.isNotEmpty(info) && StringUtils.isNotEmpty(info.getLeaseId())) {
                result.setLeaseInfoVo(cmsFinancingLeaseInfoService.getLeaseById(info.getLeaseId()));
            }
            if (StringUtils.isNotEmpty(info.getLeaseId())) {
                result.setLeaseCapitalPlanDelVoList(leaseCapitalPlanDelService.getInfoByLeaseId(info.getLeaseId()));
            }
        }
        result.setTaskId(taskId);
        return AjaxJson.success(result);
    }
}