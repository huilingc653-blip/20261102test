package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.billPool.dto.AbPoolIntoRepealOptDTO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolIntoRepealService;
import com.bytter.bill.domain.billPool.service.AbPoolPledgeeService;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoRepealFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoRepealVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolIntoRepealImpService;
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
 * @Description: 入池撤销（登记/审批/银行接口）
 */
@Api(value = "/v1-preposebill/billPool/intoRepeal", tags = "入池撤销")
@RestController("v1AbPoolIntoRepealController")
@RequestMapping("/v1-preposebill/billPool/intoRepeal")
public class AbPoolIntoRepealController extends BaseController {

    @Autowired
    private AbPoolIntoRepealService abPoolIntoRepealService;

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
    private BillPoolIntoRepealImpService billPoolIntoRepealImpService;

    /**
     * 入池撤销table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询入池撤销列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询入池撤销列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolIntoRepealService.getDataTablesUnion(input));
    }

    /**
     * 入池撤销登记页面初始化数据
     */
    /**
     * 保存入池撤销
     */
    @ApiOperation("保存入池撤销")
    @PostMapping("/saveRepealDtl")
    public AjaxJson saveRepealDtl(@RequestBody AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolIntoRepealService.saveRepealDtl(abPooIntoBeanVO, this.getCurrentUser());
    }

    /**
     * 加载入池撤销审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载入池撤销审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolIntoRepealService.dataTableApprove(input));
    }

    /**
     * 入池撤销审核详情界面初始化数据
     */
    @ApiOperation("入池撤销审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolIntoRepealFormVO> approve(String businessId, String taskId) {
        AbPoolIntoRepealFormVO result = new AbPoolIntoRepealFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            //获取审批中的数据
            AbPoolIntoRepealVO abPoolRepeaVo = abPoolIntoRepealService.queryAbPoolIntoRepealVoByApdId(businessId);
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abPoolRepeaVo.getAccId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(abPoolRepeaVo);
            AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(businessId);
            result.setAttachmentInfo(attVo);
            if (StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())) {
                result.setAttachmentInfoDiscount(abBillAttachmentService.viewAttachmentByDiscount(businessId));
            }
            result.setIsAttachmentShow(abBillAttachmentService.isAttachmentShow(attVo.getBriefId()));
            result.setBankTypeName(StringUtils.isNotEmpty(attVo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()) : null);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 查询入池撤销详情
     */
    @LogMessage(content = "查看入池撤销详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询入池撤销详情")
    @GetMapping("/view/{id}/{status}")
    public AjaxJson<AbPoolIntoRepealFormVO> view(@PathVariable("id") String id, @PathVariable("status") String status) {
        AbPoolIntoRepealFormVO result = new AbPoolIntoRepealFormVO();
        if (StringUtils.isNotEmpty(id)) {
            AbPoolIntoRepealVO abPoolRepeaVo;
            AbReceiveBillVO info = null;
            if ("0".equals(status)) {
                abPoolRepeaVo = abPoolIntoRepealService.queryAbPoolIntoRepealVoById(id);
                AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolRepeaVo.getViceId(), abPoolRepeaVo.getId());
                if (abAccrecords != null) {
                    info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
                } else {
                    info = abReceiveBillService.getAbReceiveBillVOById(abPoolRepeaVo.getAid());
                }
                //业务指令流转信息
                if (StringUtils.isNotEmpty(abPoolRepeaVo.getAid())) {
                    AbPoolDtl abPoolDtlLast = abPoolIntoRepealService.getAbPoolDtlByLastPdId(id);
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(abPoolRepeaVo.getAid(), abPoolDtlLast != null ? abPoolDtlLast.getId() : null);
                    result.setAccrecordsList(abAccrecordsList);
                }
            } else {
                abPoolRepeaVo = abPoolIntoRepealService.queryAbPoolIntoRepealVoByApdId(id);
                info = abReceiveBillService.getAbReceiveBillVOById(abPoolRepeaVo.getAccId());
                //业务指令流转信息
                if (StringUtils.isNotEmpty(abPoolRepeaVo.getAccId())) {
                    AbPoolDtl abPoolDtlLast = abPoolIntoRepealService.getAbPoolDtlByLastPdId(id);
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(abPoolRepeaVo.getAccId(), abPoolDtlLast != null ? abPoolDtlLast.getId() : null);
                    result.setAccrecordsList(abAccrecordsList);
                }
            }
            result.setAbReceiveBillPage(info);
            result.setPoolDTO(abPoolRepeaVo);
            if (StringUtils.isNotEmpty(abPoolRepeaVo.getViceId())) {
                List<AbLog> ablogList = abLogService.listAbLog(abPoolRepeaVo.getViceId(), null);
                result.setAbLogPage(ablogList);
            }
        }
        return success(result);
    }

    /**
     * 修改入池撤销指令状态
     */
    @ApiOperation("修改入池撤销指令状态")
    @PostMapping("/opt")
    public AjaxJson siginOpt(@RequestBody AbPoolIntoRepealOptDTO dto) {
        return abPoolIntoRepealService.updateOptStatus(dto.getId(), dto.getOpt(), this.getCurrentUser());
    }

    /**
     * 查询票据池内的质权人
     */
    @ApiOperation("查询票据池内的质权人")
    @GetMapping("/getPoolInfo")
    public AjaxJson<AbPoolPledgeeVO> getPoolInfo(String id) {
        return success(abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id));
    }

    /**
     * 手机端入池撤销审核详情界面初始化数据
     */
    @ApiOperation("手机端入池撤销审核详情初始化数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolIntoRepealFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolIntoRepealFormVO result = new AbPoolIntoRepealFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            //获取审批中的数据
            AbPoolIntoRepealVO abPoolDtl = abPoolIntoRepealService.queryAbPoolIntoRepealVoById(businessId);
            AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
            AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
            result.setAbReceiveBillPage(info);
            result.setIsApprove(true);
            result.setPoolDTO(abPoolDtl);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 对获取的可入池撤消数据进行入库（先根据票据号、交易日期判断是否存在数据再新增）
     */
    @ApiOperation("入池撤销数据入库")
    @PostMapping("/savePoolIntoRepealInfo")
    public AjaxJson savePoolIntoRepealInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolIntoRepealImpService.savePoolIntoRepealInfo(paramsVo);
        return success();
    }

    /**
     * 获取待发送入池撤销申请数据
     */
    @ApiOperation("获取待发送入池撤销申请数据")
    @GetMapping("/queryIntoRepealByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoRepealByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoRepealImpService.queryIntoRepealByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果入池撤销数据
     */
    @ApiOperation("获取等待结果入池撤销数据")
    @GetMapping("/queryIntoRepealResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryIntoRepealResultByBifCode(String bifCode, String reCode) {
        return success(billPoolIntoRepealImpService.queryIntoRepealResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改入池撤销的指令状态
     */
    @ApiOperation("修改入池撤销的指令状态")
    @PostMapping("/updatePoolIntoRepeal")
    public AjaxJson updatePoolIntoRepeal(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolIntoRepealImpService.updatePoolIntoRepeal(paramsVo);
        return success();
    }
}
