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
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseExtend;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseExtendService;
import com.bytter.finance.domain.lease.vo.LeaseExtendApproveViewVO;
import com.bytter.finance.domain.lease.vo.LeaseExtendUpdatePageVO;
import com.bytter.finance.domain.lease.vo.LeaseExtendVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title
 * @Description 融资租赁展期
 */
@Api(value = "/v1-preposebill/cms/LeaseExtend", tags = "融资租赁展期")
@RestController("v1LeaseExtendedController")
@RequestMapping("/v1-preposebill/cms/LeaseExtend")
public class LeaseExtendedController extends BaseController {
    @Autowired
    private LeaseExtendService leaseExtendService;
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;

    /**
     * 融资展期申请列表数据
     */
    @ApiOperation("融资展期申请列表数据")
    @LogMessage(content = "跳转融资租赁展期页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseExtendService.getDataLeaseTables(input, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 融资租赁展期新增页初始化
     * 旧接口：/cms/LeaseExtend/update/{id}
     */
    @ApiOperation("融资租赁展期新增页初始化")
    @GetMapping("/update/{id}")
    public AjaxJson<LeaseExtendUpdatePageVO> update(@PathVariable("id") String id) {
        LeaseExtendUpdatePageVO result = new LeaseExtendUpdatePageVO();
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id, true);
        TbRateMaintenceDTO tbLoanRateMaintence = leaseExtendService.getLoanRateMaintence(cmsFinancingLeaseInfo);
        result.setLeaseId(id);
        result.setLeaseInfo(cmsFinancingLeaseInfo);
        result.setTbLoanRateMaintence(tbLoanRateMaintence);
        return AjaxJson.success(result);
    }

    /**
     * 融资租赁展期保存/提交
     */
    @ApiOperation("融资租赁展期保存/提交")
    @LogMessage(content = "融资租赁展期", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody LeaseExtendVO leaseExtendVO) {
        if (leaseExtendVO.getStatus() == 0) {// 暂存
            leaseExtendVO.setStatus(CmsConstant.TEMP);
            leaseExtendService.modifyLeaseExtend(leaseExtendVO, getCurrentUser());
        } else if (leaseExtendVO.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.LEASE_EXTENDY_CHECK);
            } else {
                leaseExtendVO.setStatus(CmsConstant.COMMITAPPROVE);
                leaseExtendService.modifyLeaseExtend(leaseExtendVO, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 融资租赁展期编辑页初始化
     * 旧接口：/cms/LeaseExtend/extendUpdate/{id}
     */
    @ApiOperation("融资租赁展期编辑页初始化")
    @GetMapping("/extendUpdate/{id}")
    public AjaxJson<LeaseExtendUpdatePageVO> extendUpdate(@PathVariable("id") String id) {
        LeaseExtendUpdatePageVO result = new LeaseExtendUpdatePageVO();
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id, true);
        TbRateMaintenceDTO tbLoanRateMaintence = leaseExtendService.getLoanRateMaintence(cmsFinancingLeaseInfo);
        result.setLeaseId(id);
        result.setLeaseInfo(cmsFinancingLeaseInfo);
        result.setTbLoanRateMaintence(tbLoanRateMaintence);
        result.setLeaseExtendVO(leaseExtendService.getExtend(id));
        return AjaxJson.success(result);
    }

    /**
     * 融资租赁展期编辑保存/提交
     */
    @ApiOperation("融资租赁展期编辑保存/提交")
    @LogMessage(content = "融资租赁展期编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/extendEdit")
    public AjaxJson extendEdit(@RequestBody LeaseExtendVO leaseExtendVO) {
        if (leaseExtendVO.getStatus() == 0) {// 暂存
            leaseExtendVO.setStatus(CmsConstant.TEMP);
            leaseExtendService.editLeaseExtend(leaseExtendVO, getCurrentUser());
        } else if (leaseExtendVO.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.LEASE_EXTENDY_CHECK);
            } else {
                leaseExtendVO.setStatus(CmsConstant.COMMITAPPROVE);
                leaseExtendService.editLeaseExtend(leaseExtendVO, getCurrentUser());
            }
        }
        return AjaxJson.success();
    }

    /**
     * 删除展期单据
     */
    @ApiOperation("删除展期单据")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        LeaseExtend leaseExtend = leaseExtendService.get(id);
        leaseExtend.setStatus(CmsConstant.DELETED);
        leaseExtendService.updateEntity(leaseExtend);
        return AjaxJson.success();
    }

    /**
     * 操作日志列表
     */
    @ApiOperation("展期操作日志列表")
    @PostMapping("/listMsgData")
    public AjaxJson<DataTablesOutput> listMsgData(@RequestBody V1DataTablesInput dataTablesInput, String leaseId) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseExtendService.getDataListMsgData(input, leaseId);
        return AjaxJson.success(output);
    }

    /**
     * 展期审批列表数据
     */
    @ApiOperation("展期审批列表数据")
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = leaseExtendService.approveDataTable(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 展期审批详情
     */
    @ApiOperation("展期审批详情")
    @GetMapping("/viewApproveDate")
    public AjaxJson<LeaseExtendApproveViewVO> view(String id, String flag) {
        LeaseExtendApproveViewVO result = new LeaseExtendApproveViewVO();
        result.setFlag(flag);
        result.setLeaseExtendVO(leaseExtendService.queryLeaseObjectInfo(id));
        return AjaxJson.success(result);
    }
}
