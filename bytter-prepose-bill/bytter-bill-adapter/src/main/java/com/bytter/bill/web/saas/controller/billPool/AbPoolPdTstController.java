package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billPool.dto.AbPoolIntoApplyOptDTO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolIntoApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolOutApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolPdTstService;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPdTstApproveFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPdTstVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPdTstViewVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 质押托管转换
 */
@Api(value = "/v1-preposebill/billPool/pdtst", tags = "质押托管转换")
@RestController("v1AbPoolPdTstController")
@RequestMapping("/v1-preposebill/billPool/pdtst")
public class AbPoolPdTstController extends BaseController {

    @Autowired
    private AbPoolPdTstService abPoolPdTstService;

    @Autowired
    private AbReceiveBillService abReceiveBillService;

    @Autowired
    private AbPoolIntoApplyService abPoolIntoApplyService;

    @Autowired
    private AbPoolOutApplyService abPoolOutApplyService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 质押托管转换table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolPdTstService.getDataTablesUnion(input));
    }

    /**
     * 保存质押托管转换
     */
    @ApiOperation("保存质押托管转换")
    @PostMapping("/saveApplyDtl")
    public AjaxJson saveAppleDtl(@RequestBody AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolPdTstService.savePdTstApply(abPooIntoBeanVO, getCurrentUser());
    }

    /**
     * 加载质押托管转换审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载质押托管转换审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolPdTstService.dataTableApprove(input));
    }

    /**
     * 质押托管转换审核详情页面数据
     */
    @ApiOperation("质押托管转换审核详情页面数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolPdTstApproveFormVO> approve(String businessId, String taskId) {
        AbPoolPdTstApproveFormVO result = new AbPoolPdTstApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolIntoApplyVO abPoolDtl = abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
            AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), businessId);
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(abPoolDtl);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 指令处理修改 包括继续和重发
     */
    @ApiOperation("指令处理修改")
    @PostMapping("/opt")
    public AjaxJson siginOpt(@RequestBody AbPoolIntoApplyOptDTO dto) {
        return abPoolPdTstService.updateOptStatus(dto.getId(), dto.getOpt(), getCurrentUser());
    }

    /**
     * 质押托管转换详情数据
     */
    @LogMessage(content = "查看池池转换详细详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查看质押托管转换详情")
    @GetMapping("/view/{id}")
    public AjaxJson<AbPoolPdTstViewVO> view(@PathVariable("id") String id) {
        AbPoolPdTstViewVO result = new AbPoolPdTstViewVO();
        if (StringUtils.isNotEmpty(id)) {
            AbPoolOutApplyVO abPoolDtl = abPoolOutApplyService.queryPoolOutVObyId(id);
            AbVoucherinfoVO info = abReceiveBillService.getAbBillInfo(abPoolDtl.getAccId(), AccebillConstant.BRIEF_STATUS_6001);
            List<AbPoolPdTstVO> tstList = abPoolPdTstService.queryTstInfoList(abPoolDtl.getViceId(), abPoolDtl.getId());
            result.setAbReceiveBillPage(info);
            result.setPoolDTO(abPoolDtl);
            if (StringUtils.isNotEmpty(abPoolDtl.getApdId())) {
                List<AbLog> ablogList = abLogService.listAbLog(abPoolDtl.getViceId(), null);
                result.setAbLogPage(ablogList);
            }
            if (ListUtils.isNotNullOrEmpty(tstList)) {
                result.setTstList(tstList);
            } else {
                AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(abPoolDtl.getViceId(), id);
                if (accrecords != null && StringUtils.isNotEmpty(accrecords.getId())) {
                    AbPoolDtl abPoolDtlLast = abPoolIntoApplyService.getAbPoolDtlByLastPdId(id);
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(accrecords.getId(), abPoolDtlLast != null ? abPoolDtlLast.getId() : null);
                    result.setAccrecordsList(abAccrecordsList);
                }
            }
        }
        return success(result);
    }

    /**
     * 质押托管转换手机审批详情数据
     */
    @ApiOperation("质押托管转换手机审批详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolPdTstApproveFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolPdTstApproveFormVO result = new AbPoolPdTstApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolIntoApplyVO abPoolDtl = abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
            AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), businessId);
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(abPoolDtl);
            List<AbPoolPdTstVO> tstList = abPoolPdTstService.queryTstInfoList(abPoolDtl.getViceId(), abPoolDtl.getBpId());
            if (ListUtils.isNotNullOrEmpty(tstList)) {
                result.setTstList(tstList);
            }
        }
        result.setTaskId(taskId);
        return success(result);
    }
}
