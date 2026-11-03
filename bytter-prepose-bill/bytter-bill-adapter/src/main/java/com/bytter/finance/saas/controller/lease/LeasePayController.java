package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.lease.entity.LeasePay;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanService;
import com.bytter.finance.domain.lease.service.LeasePayService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.domain.lease.vo.LeasePayEditVO;
import com.bytter.finance.domain.lease.vo.LeasePayVO;
import com.bytter.finance.domain.lease.vo.LeasePayViewVO;
import com.bytter.system.api.IBtBankAccService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁租金支付表
 * @date 2022-04-15 10:07:46
 */
@Api(value = "/v1-preposebill/cms/lease/leasepay", tags = "租赁租金支付")
@RestController("v1LeasePayController")
@RequestMapping("/v1-preposebill/cms/lease/leasepay")
public class LeasePayController extends BaseController {
    @Autowired
    private LeasePayService leasePayService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;

    /**
     * 租金支付table列表
     */
    @ApiOperation("租金支付列表数据")
    @LogMessage(content = "查询租金支付列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leasePayService.getDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 获取编辑页面初始化数据
     */
    @ApiOperation("租金还款编辑初始化数据")
    @GetMapping("/edit")
    public AjaxJson<LeasePayEditVO> edit(String tempOrSubmitSign, LeasePayVO leasePayVo, String downSign) {
        LeasePayEditVO result = new LeasePayEditVO();
        if (StringUtils.isNotEmpty(leasePayVo.getId())) {
            List<LeasePayVO> leasePayVOList = leasePayService.getByLeaseId(leasePayVo.getLeaseId());
            if (!ListUtils.isNullOrEmpty(leasePayVOList)) {
                leasePayVo = leasePayVOList.get(0);
            }
            leasePayVo.setCreateDate(new Date());
        } else {
            leasePayVo.setCreateDate(new Date());
        }
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leasePayVo.getLeaseId());
        LeaseCapitalPlanVO leaseCapitalPlanVO = leaseCapitalPlanService.getByLeaseId(leasePayVo.getLeaseId());
        leasePayVo.setLeasePlanId(leaseCapitalPlanVO.getId());
        BigDecimal sendBalance = leasePayService.completeSendBalance(leasePayVo.getLeaseId(), null);

        result.setOnlineSign(leasePayVo.getOnlineSign());
        result.setLeaseInfoVo(cmsFinancingLeaseInfoVO);
        result.setLeasePayVo(leasePayVo);
        result.setDownSign(downSign);
        result.setSendBalance(sendBalance);
        result.setTempOrSubmitSign(tempOrSubmitSign);
        return AjaxJson.success(result);
    }

    /**
     * 租金申请查询详情
     */
    @ApiOperation("租金申请查询详情数据")
    @LogMessage(content = "租金申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewLeasePay")
    public AjaxJson<LeasePayViewVO> viewRepay(String leaseId) {
        LeasePayViewVO result = new LeasePayViewVO();
        if (EmptyUtils.isNotEmpty(leaseId)) {
            result.setLeaseInfoVo(cmsFinancingLeaseInfoService.getLeaseById(leaseId));
            result.setLeasePayVOList(leasePayService.getByLeaseId(leaseId));
        }
        return AjaxJson.success(result);
    }

    /**
     * 编辑租金付款 暂存/提交
     */
    @ApiOperation("租金付款暂存/提交")
    @LogMessage(content = "编辑租金付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody LeasePayVO vo) {
        leasePayService.tempOrSubmit(vo, vo.getSaveType(), vo.getTempOrSubmitSign(), getCurrentUser(), vo.getSign());
        return AjaxJson.success();
    }

    /**
     * 租金支付审批列表数据
     */
    @ApiOperation("租金支付申请审批列表数据")
    @LogMessage(content = "租金支付申请审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/dataTableLeasePayApprove")
    public AjaxJson<DataTablesOutput> dataTableLeasePayApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leasePayService.dataTableLeasePayApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 租金支付审批详情
     */
    @ApiOperation("租金支付审批详情")
    @GetMapping("/viewLeasePayApprove")
    public AjaxJson<LeasePayViewVO> viewLeasePayApprove(String businessId, String taskId) {
        LeasePayViewVO result = new LeasePayViewVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            LeasePay leasePay = leasePayService.get(businessId);
            if (StringUtils.isNotEmpty(leasePay) && StringUtils.isNotEmpty(leasePay.getLeaseId())) {
                result.setLeaseInfoVo(cmsFinancingLeaseInfoService.getLeaseById(leasePay.getLeaseId()));
                List<LeasePayVO> leasePayVOList = new ArrayList<>();
                leasePayVOList.add(leasePayService.getById(leasePay.getId()));
                result.setLeasePayVOList(leasePayVOList);
            }
        }
        result.setTaskId(taskId);
        return AjaxJson.success(result);
    }
}
