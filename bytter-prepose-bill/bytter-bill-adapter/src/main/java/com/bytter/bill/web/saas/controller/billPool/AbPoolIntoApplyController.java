package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolIntoApplyImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billPool.dto.AbPoolIntoApplyOptDTO;
import com.bytter.bill.domain.billPool.service.AbPoolIntoApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolPledgeeService;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeVO;
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
 * @Description: 入池申请（入池申请/审批/银行接口）
 */
@Api(value = "/v1-preposebill/billPool/intoApply", tags = "入池申请")
@RestController("v1AbPoolIntoApplyController")
@RequestMapping("/v1-preposebill/billPool/intoApply")
public class AbPoolIntoApplyController extends BaseController {

    @Autowired
    private AbPoolIntoApplyService abPoolIntoApplyService;

    @Autowired
    private AbReceiveBillService abReceiveBillService;

    @Autowired
    private AbPoolPledgeeService abPoolPledgeeService;

    @Autowired
    private AbBillAttachmentService abBillAttachmentService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    @Autowired
    private BillPoolIntoApplyImpService billPoolIntoApplyImpService;

    /**
     * 入池申请table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询入池申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询入池申请列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolIntoApplyService.getDataTablesUnion(input));
    }

    /**
     * 入池申请登记页面数据
     *
     * @param ids         业务流水表ID
     * @param abCount     票据数量
     * @param billFlag    标识
     * @param abBillMoney 票据金额
     */
    /**
     * 保存入池新增
     */
    @ApiOperation("保存入池新增")
    @LogMessage(content = "保存入池新增", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveAppleDtl")
    public AjaxJson saveAppleDtl(@RequestBody AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolIntoApplyService.saveIntoApple(abPooIntoBeanVO, getCurrentUser());
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolIntoApplyService.dataTableApprove(input));
    }

    /**
     * 审核详情页面数据
     */
    @ApiOperation("审核详情页面数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolIntoApplyFormVO> approve(String businessId, String taskId) {
        AbPoolIntoApplyFormVO result = new AbPoolIntoApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolIntoApplyVO abPoolDtl = abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
            AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(abPoolDtl);
            fillAttachment(businessId, result);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 入池申请详情数据
     */
    @LogMessage(content = "查看出入池详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询入池申请详情")
    @GetMapping("/view/{id}")
    public AjaxJson<AbPoolIntoApplyFormVO> view(@PathVariable("id") String id, String billFlag) {
        AbPoolIntoApplyFormVO result = new AbPoolIntoApplyFormVO();
        if (StringUtils.isNotEmpty(id)) {
            if (billFlag.equals(AbPoolConstant.OPT_INI_Y)) {
                AbVoucherinfoVO info = abReceiveBillService.getAbBillInfo(id, AccebillConstant.BRIEF_STATUS_6001);
                result.setAbReceiveBillPage(info);
                result.setAbLogPage(abLogService.listAbLog(info.getVvid(), null));
            } else {
                AbPoolIntoApplyVO abPoolDtl = abPoolIntoApplyService.queryPoolIntoVObyId(id);
                AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
                AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
                result.setAbReceiveBillPage(info);
                result.setPoolDTO(abPoolDtl);
                if (StringUtils.isNotEmpty(abPoolDtl.getViceId())) {
                    result.setAbLogPage(abLogService.listAbLog(abPoolDtl.getViceId(), null));
                }
                if (StringUtils.isNotEmpty(abAccrecords.getId())) {
                    AbPoolDtl abPoolDtlLast = abPoolIntoApplyService.getAbPoolDtlByLastPdId(id);
                    result.setAccrecordsList(abAccrecordsService.getAbAccrecordsbyIdOrPdId(abAccrecords.getId(), abPoolDtlLast != null ? abPoolDtlLast.getId() : null));
                }
            }
        }
        return success(result);
    }

    /**
     * 修改入池申请指令状态
     */
    @ApiOperation("修改入池申请指令状态")
    @PostMapping("/opt")
    public AjaxJson opt(@RequestBody AbPoolIntoApplyOptDTO dto) {
        return abPoolIntoApplyService.updateOptStatus(dto.getId(), dto.getOpt(), getCurrentUser());
    }

    /**
     * 根据票据池ID查询质权人信息
     */
    @ApiOperation("根据票据池ID查询质权人信息")
    @GetMapping("/getPoolInfo")
    public AjaxJson<AbPoolPledgeeVO> getPoolInfo(String id) {
        AjaxJson<AbPoolPledgeeVO> reJson = success();
        AbPoolPledgeeVO vo = abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id);
        if (StringUtils.isNotEmpty(vo)) {
            reJson.setObj(vo);
        }
        return reJson;
    }

    /**
     * 手机端审核详情页面数据
     */
    @ApiOperation("手机端审核详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolIntoApplyFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolIntoApplyFormVO result = new AbPoolIntoApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolIntoApplyVO abPoolDtl = abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
            AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(abPoolDtl);
        }
        return success(result);
    }

    /**
     * 获取待发送票据入池申请数据
     */
    @ApiOperation("获取待发送票据入池申请数据")
    @GetMapping("/queryIntoApplyByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoApplyByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryIntoApplyByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果票据入池数据
     */
    @ApiOperation("获取等待结果票据入池数据")
    @GetMapping("/queryIntoApplyResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoApplyResultByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryIntoApplyResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改票据入池的指令状态
     */
    @ApiOperation("修改票据入池的指令状态")
    @PostMapping("/updatePoolIntoApply")
    public AjaxJson updatePoolIntoApply(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolIntoApplyImpService.updatePoolIntoApply(paramsVo);
        return success();
    }

    /**
     * 获取待发送票据托管入池数据
     */
    @ApiOperation("获取待发送票据托管入池数据")
    @GetMapping("/queryIntoManagedByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoManagedByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryIntoManagedByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果票据托管入池数据
     */
    @ApiOperation("获取等待结果票据托管入池数据")
    @GetMapping("/queryIntoManagedResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoManagedResultByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryIntoManagedResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改票据托管入池的指令状态
     */
    @ApiOperation("修改票据托管入池的指令状态")
    @PostMapping("/updatePoolIntoManaged")
    public AjaxJson updatePoolIntoManaged(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolIntoApplyImpService.updatePoolIntoManaged(paramsVo);
        return success();
    }

    /**
     * 获取待发送票据质押入池数据
     */
    @ApiOperation("获取待发送票据质押入池数据")
    @GetMapping("/queryIntoPledgeByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoPledgeByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryIntoPledgeByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果票据质押入池数据
     */
    @ApiOperation("获取等待结果票据质押入池数据")
    @GetMapping("/queryIntoPledgeResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoPledgeResultByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryIntoPledgeResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改票据质押入池的指令状态
     */
    @ApiOperation("修改票据质押入池的指令状态")
    @PostMapping("/updatePoolIntoPledge")
    public AjaxJson updatePoolIntoPledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolIntoApplyImpService.updatePoolIntoPledge(paramsVo);
        return success();
    }

    /**
     * 获取待发送删除票据数据
     */
    @ApiOperation("获取待发送删除票据数据")
    @GetMapping("/queryDeleteBillByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryDeleteBillByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryDeleteBillByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果删除票据数据
     */
    @ApiOperation("获取等待结果删除票据数据")
    @GetMapping("/queryDeleteBillResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryDeleteBillResultByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoApplyImpService.queryDeleteBillResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改删除票据的指令状态
     */
    @ApiOperation("修改删除票据的指令状态")
    @PostMapping("/updateDeleteBill")
    public AjaxJson updateDeleteBill(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolIntoApplyImpService.updateDeleteBill(paramsVo);
        return success();
    }

    /**
     * 填充附件展示信息
     */
    private void fillAttachment(String accId, AbPoolIntoApplyFormVO result) {
        AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(accId);
        result.setAttachmentInfo(attVo);
        if (StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())) {
            result.setAttachmentInfoDiscount(abBillAttachmentService.viewAttachmentByDiscount(accId));
        }
        result.setIsAttachmentShow(abBillAttachmentService.isAttachmentShow(attVo.getBriefId()));
        result.setBankTypeName(StringUtils.isNotEmpty(attVo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()) : null);
    }
}
