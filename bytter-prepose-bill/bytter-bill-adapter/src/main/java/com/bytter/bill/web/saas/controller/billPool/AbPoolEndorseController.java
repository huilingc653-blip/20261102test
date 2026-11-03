package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billPool.dto.AbPoolEndorseOperateDTO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolEndorseService;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseApproveFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseViewVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseVO;
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
 * @Description: 背书申请
 */
@Api(value = "/v1-preposebill/billPool/endorse", tags = "背书申请")
@RestController("v1AbPoolEndorseController")
@RequestMapping("/v1-preposebill/billPool/endorse")
public class AbPoolEndorseController extends BaseController {

    @Autowired
    private AbPoolEndorseService abPoolEndorseService;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    /**
     * 查询背书转让TAB列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询背书转让TAB列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询背书转让TAB列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolEndorseService.getDataTablesUnion(input));
    }

    /**
     * 保存背书转让信息
     */
    @ApiOperation("保存背书转让信息")
    @LogMessage(content = "保存背书转让信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolEndorseVO abPoolEndorseVO) {
        abPoolEndorseService.saveAbPoolEndorseInfo(abPoolEndorseVO, getCurrentUser());
        return success();
    }

    /**
     * 背书申请操作
     */
    @ApiOperation("背书申请操作")
    @PostMapping("/operation")
    public AjaxJson operation(@RequestBody AbPoolEndorseOperateDTO dto) {
        abPoolEndorseService.operation(dto.getPdId(), dto.getOpt(), getCurrentUser(), false);
        return success();
    }

    /**
     * 加载背书申请审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载背书申请审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolEndorseService.dataTableApprove(input));
    }

    /**
     * 背书申请审核详情页面数据
     */
    @ApiOperation("背书申请审核详情页面数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolEndorseApproveFormVO> approve(String businessId, String taskId) {
        AbPoolEndorseApproveFormVO result = new AbPoolEndorseApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolEndorseVO abPoolEndorseVO = abPoolEndorseService.getAbPoolEndorseVOByPdId(businessId);
            result.setAbPoolEndorseVO(abPoolEndorseVO);
            if (StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
                result.setBillInfo(abPoolEndorseService.getAbBillEndorseApplyVOByVvid(abPoolEndorseVO.getVvId(), abPoolEndorseVO.getId()));
            }
        }
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 背书申请手机审核详情页面数据
     */
    @ApiOperation("背书申请手机审核详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolEndorseApproveFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolEndorseApproveFormVO result = new AbPoolEndorseApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolEndorseVO abPoolEndorseVO = abPoolEndorseService.getAbPoolEndorseVOByPdId(businessId);
            result.setAbPoolEndorseVO(abPoolEndorseVO);
            if (StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
                result.setBillInfo(abPoolEndorseService.getAbBillEndorseApplyVOByVvid(abPoolEndorseVO.getVvId(), abPoolEndorseVO.getId()));
            }
        }
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 背书申请详情数据
     */
    @ApiOperation("背书申请详情数据")
    @GetMapping("/view")
    public AjaxJson<AbPoolEndorseViewVO> view(String id) {
        AbPoolEndorseViewVO result = new AbPoolEndorseViewVO();
        if (StringUtils.isNotEmpty(id)) {
            AbPoolEndorseVO endorseVO = abPoolEndorseService.getAbPoolEndorseVOByPdId(id);
            result.setAbPoolEndorseVO(endorseVO);
            result.setAbLogList(abLogService.listAbLog(null, endorseVO.getLastId() != null ? endorseVO.getLastId() : endorseVO.getId()));
            if (StringUtils.isNotEmpty(endorseVO.getVvId())) {
                AbPoolEndorseVO billInfo = abPoolEndorseService.getAbBillEndorseApplyVOByVvid(endorseVO.getVvId(), endorseVO.getId());
                result.setBillInfo(billInfo);
                if (StringUtils.isNotEmpty(billInfo.getAccId())) {
                    AbPoolDtl abPoolDtl = abPoolEndorseService.getAbPoolDtlByLastPdId(id);
                    List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(billInfo.getAccId(), abPoolDtl != null ? abPoolDtl.getId() : null);
                    result.setAccrecordsList(abAccrecordsList);
                }
            }
        }
        result.setIsApprove(false);
        return success(result);
    }
}
