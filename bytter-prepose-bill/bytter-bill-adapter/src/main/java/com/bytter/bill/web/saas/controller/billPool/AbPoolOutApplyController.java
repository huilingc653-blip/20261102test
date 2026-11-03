package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.billPool.dto.AbPoolOutApplyOptDTO;
import com.bytter.bill.domain.billPool.service.AbPoolOutApplyService;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolOutApplyImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 出池申请（出池管理/审批/银行接口）
 */
@Api(value = "/v1-preposebill/billPool/outApply", tags = "出池申请")
@RestController("v1AbPoolOutApplyController")
@RequestMapping("/v1-preposebill/billPool/outApply")
public class AbPoolOutApplyController extends BaseController {

    @Autowired
    private AbPoolOutApplyService abPoolOutApplyService;

    @Autowired
    private AbReceiveBillService abReceiveBillService;

    @Autowired
    private BillPoolOutApplyImpService billPoolOutApplyImpService;

    /**
     * 出池申请查询列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询出池申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询出池申请列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolOutApplyService.getDataTablesUnion(input));
    }

    /**
     * 加载出池审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载出池审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolOutApplyService.dataTableApprove(input));
    }

    /**
     * 出池申请页面初始化数据
     */
    /**
     * 出池申请保存提交
     */
    @ApiOperation("出池申请保存提交")
    @PostMapping("/saveApplyDtl")
    public AjaxJson saveAppleDtl(@RequestBody AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolOutApplyService.saveOutApply(abPooIntoBeanVO, this.getCurrentUser());
    }

    /**
     * 出池审核详情页面初始化数据
     */
    @ApiOperation("出池审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolOutApplyFormVO> approve(String businessId, String taskId) {
        AbPoolOutApplyFormVO result = new AbPoolOutApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolOutApplyVO abPoolOutApplyVO = abPoolOutApplyService.queryPoolOutVObyId(businessId);
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abPoolOutApplyVO.getAccId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(StringUtils.isBlank(info.getPdId())
                    ? null : abPoolOutApplyService.queryPoolOutVObyId(info.getPdId()));
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 出池申请指令处理修改
     */
    @ApiOperation("出池申请指令处理修改")
    @PostMapping("/opt")
    public AjaxJson siginOpt(@RequestBody AbPoolOutApplyOptDTO dto) {
        return abPoolOutApplyService.updateOptStatus(dto.getId(), dto.getOpt(), this.getCurrentUser());
    }

    /**
     * 手机端出池审核详情页面初始化数据
     */
    @ApiOperation("手机端出池审核详情页面初始化数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolOutApplyFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolOutApplyFormVO result = new AbPoolOutApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolOutApplyVO abPoolOutApplyVO = abPoolOutApplyService.queryPoolOutVObyId(businessId);
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abPoolOutApplyVO.getAccId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(StringUtils.isBlank(info.getPdId())
                    ? null : abPoolOutApplyService.queryPoolOutVObyId(info.getPdId()));
        }
        return success(result);
    }

    /**
     * 获取待发送票据出池申请数据
     */
    @ApiOperation("获取待发送票据出池申请数据")
    @GetMapping("/queryOutApplyByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryOutApplyByBifCode(String bifCode, String reCode) {
        return success(billPoolOutApplyImpService.queryOutApplyByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果票据出池数据
     */
    @ApiOperation("获取等待结果票据出池数据")
    @GetMapping("/queryOutApplyResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryOutApplyResultByBifCode(String bifCode, String reCode) {
        return success(billPoolOutApplyImpService.queryOutApplyResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改票据出池的指令状态
     */
    @ApiOperation("修改票据出池的指令状态")
    @PostMapping("/updatePoolOutApply")
    public AjaxJson updatePoolOutApply(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolOutApplyImpService.updatePoolOutApply(paramsVo);
        return success();
    }

    /**
     * 获取待发送票据质押解除出池数据
     */
    @ApiOperation("获取待发送票据质押解除出池数据")
    @GetMapping("/queryRemovePledgeByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryRemovePledgeByBifCode(String bifCode, String reCode) {
        return success(billPoolOutApplyImpService.queryRemovePledgeByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果票据质押解除出池数据
     */
    @ApiOperation("获取等待结果票据质押解除出池数据")
    @GetMapping("/queryRemovePledgeResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryRemovePledgeResultByBifCode(String bifCode, String reCode) {
        return success(billPoolOutApplyImpService.queryRemovePledgeResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改票据质押解除出池的指令状态
     */
    @ApiOperation("修改票据质押解除出池的指令状态")
    @PostMapping("/updatePoolRemovePledge")
    public AjaxJson updatePoolRemovePledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolOutApplyImpService.updatePoolRemovePledge(paramsVo);
        return success();
    }
}
