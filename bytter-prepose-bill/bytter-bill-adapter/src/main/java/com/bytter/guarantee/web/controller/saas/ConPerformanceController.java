package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.service.ConPerformanceService;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ConPerformanceVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同履约Controller
 * @Description: 合同履约控制器类
 * @date 2019-11-12
 */
@Api(value = "/v1-guarantee/contract/conperformance", tags = "合同履约")
@RestController("v1ConPerformanceController")
@RequestMapping("/v1-guarantee/contract/conperformance")
public class ConPerformanceController extends BaseController {
    @Autowired
    private ConPerformanceService conPerformanceService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;

    /**
     * 合同履约 table列表
     */
    @ApiOperation("查询合同履约列表")
    @LogMessage(content = "查询合同履约", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conPerformanceService.getDataTablesUnion(input, this.getCurrentUser().getId());
        return AjaxJson.success(output);
    }

    /**
     * 删除履约信息
     *
     * @param rid 流水id
     */
    @ApiOperation("删除合同履约信息")
    @LogMessage(content = "删除履约信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson batchDelete(String rid) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conPerformanceService.batchDelete(rid, this.getCurrentUser()));
        return ajaxJson;
    }

    /**
     * 暂存合同履约
     */
    @ApiOperation("新增合同履约")
    @LogMessage(content = "新增合同履约", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody  ConPerformanceVO performanceVO) {
        conPerformanceService.savePerformanceApply(performanceVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改合同履约
     */
    @ApiOperation("修改合同履约")
    @LogMessage(content = "修改合同履约", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    public AjaxJson update(@RequestBody ConPerformanceVO performanceVO) {
        conPerformanceService.updatePerformanceApply(performanceVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 提交合同履约
     */
    @ApiOperation("提交合同履约")
    @LogMessage(content = "提交合同履约", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody ConPerformanceVO performanceVO) {
        conPerformanceService.submitPerformanceApply(performanceVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @ApiOperation("查询合同履约审核列表")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conPerformanceService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 获取合同履约审核详情
     */
    @ApiOperation("获取合同履约审核详情")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConPerformanceVO performanceVO = conPerformanceService.getConPerformanceVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(performanceVO.getMid()));
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                performanceVO.setBusdtls(busdtlList);
            }
            resultMap.put("performanceVO", performanceVO);
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }

    /**
     * 获取手机端合同履约审核详情
     */
    @ApiOperation("获取手机端合同履约审核详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConInfoVO params = new ConInfoVO();
            params.setRecordId(businessId);
            ConInfoDTO conInfo = conCommonService.getConInfo(params);
            ConPerformanceVO performanceVO = conPerformanceService.getConPerformanceVOByRid(businessId);
            resultMap.put("conInfo", conInfo);
            resultMap.put("performanceVO", performanceVO);
        }
        resultMap.put("conBusiness", "ContractPerformance");
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }
}