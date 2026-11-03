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
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanDelService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanService;
import com.bytter.finance.domain.lease.service.UpdateLeaseCapitalPlanService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanAddVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁计划修改
 * @date 2022-04-02 10:07:46
 */
@Api(value = "/v1-preposebill/cms/lease/updateLeaseCapitalPlan", tags = "租赁计划修改")
@RestController("v1UpdateLeaseCapitalPlanController")
@RequestMapping("/v1-preposebill/cms/lease/updateLeaseCapitalPlan")
public class UpdateLeaseCapitalPlanController extends BaseController {
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private UpdateLeaseCapitalPlanService updateLeaseCapitalPlanService;

    /**
     * 租金计划修改页面初始化数据
     */
    @ApiOperation("租金计划修改初始化数据")
    @GetMapping("/update")
    public AjaxJson<LeaseCapitalPlanAddVO> update(String leaseId) {
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
     * 导出租金计划模板
     */
    @ApiOperation("导出租金计划模板")
    @GetMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response,
                             LeaseCapitalPlanVO leaseCapitalPlanVO) throws Exception {
        leaseCapitalPlanService.exportBudget(request, response, leaseCapitalPlanVO);
    }

    /**
     * 租赁计划修改审批列表数据
     */
    @ApiOperation("租赁计划修改审批列表数据")
    @LogMessage(content = "查询借款申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableInfoApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = updateLeaseCapitalPlanService.getDataInfoApproveTables(input);
        return AjaxJson.success(output);
    }

    /**
     * 租赁计划修改审批详情
     */
    @ApiOperation("租赁计划修改审批详情")
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
