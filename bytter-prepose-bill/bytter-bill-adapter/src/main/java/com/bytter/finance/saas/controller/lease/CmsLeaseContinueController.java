package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.lease.entity.LeaseContinueInfo;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.CmsLeaseAfterContinueService;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseContinueApproveViewVO;
import com.bytter.finance.domain.lease.vo.LeaseContinueInitVO;
import com.bytter.finance.domain.lease.vo.LeaseContinueUpdatePageVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 融资租赁续租
 */
@Api(value = "/v1-preposebill/cms/afterLease/continue", tags = "融资租赁续租")
@RestController("v1CmsLeaseContinueController")
@RequestMapping("/v1-preposebill/cms/afterLease/continue")
public class CmsLeaseContinueController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private CmsLeaseAfterContinueService cmsLeaseAfterContinueService;

    /**
     * 融资续租列表数据
     */
    @ApiOperation("融资续租列表数据")
    @LogMessage(content = "融资续租列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput out = cmsLeaseAfterContinueService.getDataLeaseTables(input, getCurrentUser());
        return AjaxJson.success(out);
    }

    /**
     * 融资续租审核列表数据
     */
    @ApiOperation("融资续租审核列表数据")
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        return AjaxJson.success(cmsLeaseAfterContinueService.approveDataTable(dataInput, getCurrentUser()));
    }

    /**
     * 续租信息保存
     */
    @ApiOperation("续租信息保存")
    @LogMessage(content = "续租信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LeaseContinueInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterContinueService.saveLeaseContinueInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterContinueService.saveLeaseContinueInfo(entity, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 续租信息修改
     */
    @ApiOperation("续租信息修改")
    @LogMessage(content = "续租信息修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/updateContinue")
    public AjaxJson update(@RequestBody LeaseContinueInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterContinueService.updateLeaseContinueInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterContinueService.updateLeaseContinueInfo(entity, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 续租审批详情
     */
    @ApiOperation("续租审批详情")
    @GetMapping("/viewApproveDate")
    public AjaxJson<LeaseContinueApproveViewVO> view(String id, String flag) {
        LeaseContinueApproveViewVO result = new LeaseContinueApproveViewVO();
        result.setFlag(flag);
        result.setCmsAfterLeaseInfoVO(cmsLeaseAfterContinueService.queryLeaseContinueInfo(id));
        return AjaxJson.success(result);
    }

    /**
     * 续租操作日志列表
     */
    @ApiOperation("续租操作日志列表")
    @PostMapping("/listMsgData")
    public AjaxJson<DataTablesOutput> listMsgData(@RequestBody V1DataTablesInput dataTablesInput, String leaseId) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsLeaseAfterContinueService.getDataListMsgData(input, leaseId);
        return AjaxJson.success(output);
    }

    /**
     * 融资租赁续租页面跳转
     *
     * @return
     */
    @GetMapping("/contLease/{id}")
    public AjaxJson<LeaseContinueInitVO> contLease(@PathVariable("id") String id) {
        LeaseContinueInitVO result = new LeaseContinueInitVO();
        result.setLeaseId(id);
        LeaseContinueInfo info = cmsLeaseAfterContinueService.get(id);
        result.setLeaseInfo(cmsLeaseAfterContinueService.getLeaseById(info.getId()));
        return AjaxJson.success(result);
    }

    /**
     * 续租暂存打回编辑页面
     *
     * @param id
     * @return
     */
    @GetMapping("/continueUpdate/{id}")
    public AjaxJson<LeaseContinueUpdatePageVO> refundUpdate(@PathVariable("id") String id) {
        LeaseContinueUpdatePageVO result = new LeaseContinueUpdatePageVO();
        result.setLeaseId(id);
        LeaseContinueInfo leaseContinueInfo = cmsLeaseAfterContinueService.get(id);
        result.setLeaseInfo(cmsLeaseAfterContinueService.getLeaseById(leaseContinueInfo.getId()));
        result.setUpdateVO(cmsLeaseAfterContinueService.getContinue(id));
        return AjaxJson.success(result);
    }
}