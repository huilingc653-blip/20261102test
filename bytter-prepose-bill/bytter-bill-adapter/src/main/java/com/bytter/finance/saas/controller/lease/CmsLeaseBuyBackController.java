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
import com.bytter.finance.domain.lease.entity.LeaseBuyBackInfo;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.CmsLeaseAfterBuyBackService;
import com.bytter.finance.domain.lease.vo.LeaseBuyBackApproveViewVO;
import com.bytter.finance.domain.lease.vo.LeaseBuyBackInitVO;
import com.bytter.finance.domain.lease.vo.LeaseBuyBackUpdatePageVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 融资租赁回购
 */
@Api(value = "/v1-preposebill/cms/afterLease/buyBack", tags = "融资租赁回购")
@RestController("v1CmsLeaseBuyBackController")
@RequestMapping("/v1-preposebill/cms/afterLease/buyBack")
public class CmsLeaseBuyBackController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private CmsLeaseAfterBuyBackService cmsLeaseAfterBuyBackService;

    /**
     * 融资租赁回购列表数据
     */
    @ApiOperation("融资租赁回购列表数据")
    @LogMessage(content = "查询融资租赁回购列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsLeaseAfterBuyBackService.getDataLeaseTables(input, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 融资租赁回购审核列表数据
     */
    @ApiOperation("融资租赁回购审核列表数据")
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsLeaseAfterBuyBackService.approveDataTable(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 回购信息保存（新增/暂存/提交）
     */
    @ApiOperation("回购信息保存")
    @LogMessage(content = "回购信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LeaseBuyBackInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterBuyBackService.saveLeaseBuyBackInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterBuyBackService.saveLeaseBuyBackInfo(entity, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 回购信息更新（暂存/提交）
     */
    @ApiOperation("回购信息更新")
    @LogMessage(content = "回购信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/repoUpdate")
    public AjaxJson update(@RequestBody LeaseBuyBackInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterBuyBackService.updateLeaseBuyBackInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterBuyBackService.updateLeaseBuyBackInfo(entity, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 获取审批详情信息
     */
    @ApiOperation("获取审批详情信息")
    @GetMapping("/viewApproveDate")
    public AjaxJson<LeaseBuyBackApproveViewVO> view(String id, String flag) {
        LeaseBuyBackApproveViewVO result = new LeaseBuyBackApproveViewVO();
        result.setFlag(flag);
        result.setCmsAfterLeaseInfoVO(cmsLeaseAfterBuyBackService.queryLeaseObjectInfo(id));
        return AjaxJson.success(result);
    }

    /**
     * 融资租赁回购新增页初始化
     * 旧接口：/cms/afterLease/buyBack/repoLease/{id}
     */
    @ApiOperation("融资租赁回购新增页初始化")
    @GetMapping("/repoLease/{id}")
    public AjaxJson<LeaseBuyBackInitVO> repoLease(@PathVariable("id") String id) {
        LeaseBuyBackInitVO result = new LeaseBuyBackInitVO();
        result.setLeaseId(id);
        LeaseBuyBackInfo leaseBuyBackInfo = cmsLeaseAfterBuyBackService.get(id);
        result.setLeaseInfo(cmsLeaseAfterBuyBackService.getLeaseById(leaseBuyBackInfo.getId()));
        return AjaxJson.success(result);
    }

    /**
     * 回购暂存/打回编辑页初始化
     * 旧接口：/cms/afterLease/buyBack/repoUpdate/{id}
     */
    @ApiOperation("回购暂存打回编辑页初始化")
    @GetMapping("/repoUpdate/{id}")
    public AjaxJson<LeaseBuyBackUpdatePageVO> repoUpdate(@PathVariable("id") String id) {
        LeaseBuyBackUpdatePageVO result = new LeaseBuyBackUpdatePageVO();
        result.setLeaseId(id);
        LeaseBuyBackInfo leaseBuyBackInfo = cmsLeaseAfterBuyBackService.get(id);
        result.setLeaseInfo(cmsLeaseAfterBuyBackService.getLeaseById(leaseBuyBackInfo.getId()));
        result.setUpdateVO(cmsLeaseAfterBuyBackService.getRepo(id));
        return AjaxJson.success(result);
    }
}
