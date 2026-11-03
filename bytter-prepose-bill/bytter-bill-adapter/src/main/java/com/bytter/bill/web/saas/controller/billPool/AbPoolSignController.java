package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billPool.dto.AbPoolSignOperateDTO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolSignService;
import com.bytter.bill.domain.billPool.vo.AbPoolSignApproveFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolSignVO;
import com.bytter.bill.domain.billPool.vo.AbPoolSignViewVO;
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
 * @Title: 电票签收Controller
 * @Description: 电票签收
 */
@Api(value = "/v1-preposebill/billPool/abSign", tags = "电票签收")
@RestController("v1AbPoolSignController")
@RequestMapping("/v1-preposebill/billPool/abSign")
public class AbPoolSignController extends BaseController {

    @Autowired
    private AbPoolSignService abPoolSignService;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    /**
     * 查询签收TAB列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询签收TAB列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询签收TAB列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolSignService.getDataTablesUnion(input));
    }

    /**
     * 保存签收信息
     */
    @ApiOperation("保存签收信息")
    @LogMessage(content = "保存签收信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolSignVO abPooalSignVO) {
        abPoolSignService.saveAbPoolSignInfo(abPooalSignVO, getCurrentUser());
        return success();
    }

    /**
     * 继续或中止操作
     */
    @ApiOperation("继续或中止签收操作")
    @PostMapping("/operation")
    public AjaxJson operation(@RequestBody AbPoolSignOperateDTO dto) {
        abPoolSignService.operation(dto.getPdId(), dto.getOpt(), getCurrentUser());
        return success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolSignService.dataTableApprove(input));
    }

    /**
     * 审核详情页面数据
     */
    @ApiOperation("审核详情页面数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolSignApproveFormVO> approve(String businessId, String taskId) {
        AbPoolSignApproveFormVO result = new AbPoolSignApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolSignVO abPoolSignVO = abPoolSignService.getAbPoolSignVOByPdId(businessId);
            result.setAbPoolSignVO(abPoolSignVO);
            if (StringUtils.isNotEmpty(abPoolSignVO.getId())) {
                result.setInfo(abPoolSignService.getAbBillReceivebizVOById(abPoolSignVO.getRecId()));
            }
        }
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 手机审核详情页面数据
     */
    @ApiOperation("手机审核详情页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolSignApproveFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolSignApproveFormVO result = new AbPoolSignApproveFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbPoolSignVO abPoolSignVO = abPoolSignService.getAbPoolSignVOByPdId(businessId);
            result.setAbPoolSignVO(abPoolSignVO);
            if (StringUtils.isNotEmpty(abPoolSignVO.getId())) {
                result.setInfo(abPoolSignService.getAbBillReceivebizVOById(abPoolSignVO.getRecId()));
            }
        }
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 电票签收详细信息数据
     */
    @ApiOperation("电票签收详细信息数据")
    @GetMapping("/view")
    public AjaxJson<AbPoolSignViewVO> view(AbPoolSignVO poolSignVO) {
        AbPoolSignViewVO result = new AbPoolSignViewVO();
        //票据池信息
        if (StringUtils.isNotEmpty(poolSignVO.getPdId()) && !"null".equals(poolSignVO.getPdId())) {
            AbPoolSignVO abPoolSignVO = abPoolSignService.getAbPoolSignVOByPdId(poolSignVO.getPdId());
            result.setAbPoolSignVO(abPoolSignVO);
            result.setAbLogList(abLogService.listAbLog(null, poolSignVO.getPdId()));
        }
        //签收信息
        if (StringUtils.isNotEmpty(poolSignVO.getRecId())) {
            result.setInfo(abPoolSignService.getAbBillReceivebizVOById(poolSignVO.getRecId()));
        }
        //业务流转信息
        if (StringUtils.isNotEmpty(poolSignVO.getAccId()) && !"null".equals(poolSignVO.getAccId())) {
            AbPoolDtl abPoolDtl = abPoolSignService.getAbPoolDtlByLastPdId(poolSignVO.getPdId());
            List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(poolSignVO.getAccId(), abPoolDtl != null ? abPoolDtl.getId() : null);
            result.setAccrecordsList(abAccrecordsList);
        }
        result.setIsApprove(false);
        return success(result);
    }
}
