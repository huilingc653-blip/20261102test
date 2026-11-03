package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billPool.dto.AbPoolDiscountOperateDTO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolDiscountService;
import com.bytter.bill.domain.billPool.vo.AbPoolDiscountApproveFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolDiscountViewVO;
import com.bytter.bill.domain.billPool.vo.AbPoolDiscountVO;
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
 * @Description: 贴现申请
 */
@Api(value = "/v1-preposebill/billPool/discount", tags = "贴现申请")
@RestController("v1AbPoolDiscountController")
@RequestMapping("/v1-preposebill/billPool/discount")
public class AbPoolDiscountController extends BaseController {

    @Autowired
    private AbPoolDiscountService abPoolDiscountService;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    /**
     * 查询贴现申请TAB列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询贴现申请TAB列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询贴现申请TAB列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolDiscountService.getDataTablesUnion(input));
    }

    /**
     * 保存贴现申请信息
     */
    @ApiOperation("保存贴现申请信息")
    @LogMessage(content = "保存贴现申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolDiscountVO abPoolDiscountVO) {
        abPoolDiscountService.saveAbPoolDiscountInfo(abPoolDiscountVO, getCurrentUser());
        return success();
    }

    /**
     * 贴现申请操作
     */
    @ApiOperation("贴现申请操作")
    @PostMapping("/operation")
    public AjaxJson operation(@RequestBody AbPoolDiscountOperateDTO dto) {
        abPoolDiscountService.operation(dto.getPdId(), dto.getOpt(), getCurrentUser());
        return success();
    }

    /**
     * 加载贴现申请审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载贴现申请审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolDiscountService.dataTableApprove(input));
    }

    /**
     * 贴现申请审核详情页面数据
     */
    @ApiOperation("贴现申请审核详情页面数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolDiscountApproveFormVO> approve(String businessId, String taskId) {
        AbPoolDiscountApproveFormVO result = new AbPoolDiscountApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolDiscountVO abPoolDiscountVO = abPoolDiscountService.getAbPoolDiscountVOByPdId(businessId);
            result.setAbPoolDiscountVO(abPoolDiscountVO);
            if (StringUtils.isNotEmpty(abPoolDiscountVO.getId())) {
                result.setBillInfo(abPoolDiscountService.getAbBillDiscountApplyVOByVvid(abPoolDiscountVO.getVvId(), abPoolDiscountVO.getId()));
            }
        }
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 贴现申请手机审核详情页面数据
     */
    @ApiOperation("贴现申请手机审核详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolDiscountApproveFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolDiscountApproveFormVO result = new AbPoolDiscountApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolDiscountVO abPoolDiscountVO = abPoolDiscountService.getAbPoolDiscountVOByPdId(businessId);
            result.setAbPoolDiscountVO(abPoolDiscountVO);
            if (StringUtils.isNotEmpty(abPoolDiscountVO.getId())) {
                result.setBillInfo(abPoolDiscountService.getAbBillDiscountApplyVOByVvid(abPoolDiscountVO.getVvId(), abPoolDiscountVO.getId()));
            }
        }
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 贴现申请详情数据
     */
    @ApiOperation("贴现申请详情数据")
    @GetMapping("/view")
    public AjaxJson<AbPoolDiscountViewVO> view(String id) {
        AbPoolDiscountViewVO result = new AbPoolDiscountViewVO();
        if (StringUtils.isNotEmpty(id)) {
            AbPoolDiscountVO discountVO = abPoolDiscountService.getAbPoolDiscountVOByPdId(id);
            result.setAbPoolDiscountVO(discountVO);
            result.setAbLogList(abLogService.listAbLog(null, discountVO.getLastId() != null ? discountVO.getLastId() : discountVO.getId()));
            if (StringUtils.isNotEmpty(discountVO.getVvId())) {
                AbPoolDiscountVO billInfo = abPoolDiscountService.getAbBillDiscountApplyVOByVvid(discountVO.getVvId(), discountVO.getId());
                result.setBillInfo(billInfo);
                if (StringUtils.isNotEmpty(billInfo.getAccId())) {
                    AbPoolDtl abPoolDtl = abPoolDiscountService.getAbPoolDtlByLastPdId(id);
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(billInfo.getAccId(), abPoolDtl != null ? abPoolDtl.getId() : null);
                    result.setAccrecordsList(abAccrecordsList);
                }
            }
        }
        result.setIsApprove(false);
        return success(result);
    }
}
