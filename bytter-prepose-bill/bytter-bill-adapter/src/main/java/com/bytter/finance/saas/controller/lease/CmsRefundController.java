package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.lease.entity.LeaseRefundInfo;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.CmsLeaseAfterRefundService;
import com.bytter.finance.domain.lease.vo.LeaseRefundApproveViewVO;
import com.bytter.finance.domain.lease.vo.LeaseRefundInitVO;
import com.bytter.finance.domain.lease.vo.LeaseRefundUpdatePageVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 融资租赁退租
 */
@Api(value = "/v1-preposebill/cms/afterLease/refund", tags = "融资租赁退租")
@RestController("v1CmsRefundController")
@RequestMapping("/v1-preposebill/cms/afterLease/refund")
public class CmsRefundController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private CmsLeaseAfterRefundService cmsLeaseAfterRefundService;

    /**
     * 融资退租列表数据
     */
    @ApiOperation("融资退租列表数据")
    @LogMessage(content = "融资租赁退租列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsLeaseAfterRefundService.getDataLeaseTables(input, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 退租信息保存
     */
    @ApiOperation("退租信息保存")
    @LogMessage(content = "退租信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LeaseRefundInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterRefundService.saveLeaseRefundInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterRefundService.saveLeaseRefundInfo(entity, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 退租信息编辑更新
     */
    @ApiOperation("退租信息编辑更新")
    @LogMessage(content = "退租信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/refundEdit")
    public AjaxJson update(@RequestBody LeaseRefundInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterRefundService.updateLeaseRefundInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterRefundService.updateLeaseRefundInfo(entity, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 删除退租单据
     */
    @ApiOperation("删除退租单据")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String leaseId) {
        LeaseRefundInfo leaseRefundInfo = cmsLeaseAfterRefundService.get(leaseId);
        leaseRefundInfo.setStatus(CmsConstant.DELETED);
        cmsLeaseAfterRefundService.updateEntity(leaseRefundInfo);
        return AjaxJson.success();
    }

    /**
     * 退租审核列表数据
     */
    @ApiOperation("退租审核列表数据")
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        return AjaxJson.success(cmsLeaseAfterRefundService.approveDataTable(dataInput, getCurrentUser()));
    }

    /**
     * 退租审批详情
     */
    @ApiOperation("退租审批详情")
    @GetMapping("/viewApproveDate")
    public AjaxJson<LeaseRefundApproveViewVO> view(String id, String flag) {
        LeaseRefundApproveViewVO result = new LeaseRefundApproveViewVO();
        result.setFlag(flag);
        result.setCmsAfterLeaseInfoVO(cmsLeaseAfterRefundService.queryLeaseObjectInfo(id));
        return AjaxJson.success(result);
    }

    /**
     * 融资租赁退租页面初始化
     */
    @ApiOperation("融资租赁退租页面初始化")
    @GetMapping("/refundEdit/{id}")
    public AjaxJson<LeaseRefundInitVO> refund(@PathVariable("id") String id) {
        LeaseRefundInitVO result = new LeaseRefundInitVO();
        result.setLeaseId(id);
        LeaseRefundInfo leaseRefundInfo = cmsLeaseAfterRefundService.get(id);
        result.setLeaseInfo(cmsLeaseAfterRefundService.getLeaseById(leaseRefundInfo.getId()));
        return AjaxJson.success(result);
    }

    /**
     * 退租暂存打回编辑页初始化
     * 旧接口：/cms/afterLease/refund/refundUpdate/{id}
     */
    @ApiOperation("退租暂存打回编辑页初始化")
    @GetMapping("/refundUpdate/{id}")
    public AjaxJson<LeaseRefundUpdatePageVO> refundUpdate(@PathVariable("id") String id) {
        LeaseRefundUpdatePageVO result = new LeaseRefundUpdatePageVO();
        result.setLeaseId(id);
        LeaseRefundInfo leaseRefundInfo = cmsLeaseAfterRefundService.get(id);
        result.setLeaseInfo(cmsLeaseAfterRefundService.getLeaseById(leaseRefundInfo.getId()));
        result.setUpdateVO(cmsLeaseAfterRefundService.getRefund(id));
        return AjaxJson.success(result);
    }
}
