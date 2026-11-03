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
import com.bytter.guarantee.domain.service.ConRegisterService;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ConRegisterVO;
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
 * @Title: 合同登记Controller
 * @Description: 合同登记控制器类
 * @date 2019-11-11
 */
@Api(value = "/v1-guarantee/contract/conregister", tags = "合同登记")
@RestController("v1ConRegisterController")
@RequestMapping("/v1-guarantee/contract/conregister")
public class ConRegisterController extends BaseController {
    @Autowired
    private ConRegisterService conRegisterService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;

    /**
     * 合同登记 table列表
     */
    @ApiOperation("查询合同登记列表")
    @LogMessage(content = "查询合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conRegisterService.getDataTablesUnion(input, this.getCurrentUser().getId());
        return AjaxJson.success(output);
    }

    /**
     * 删除登记信息
     *
     * @param rid 流水id
     */
    @ApiOperation("删除合同登记信息")
    @LogMessage(content = "删除登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson batchDelete(String rid) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conRegisterService.batchDelete(rid, this.getCurrentUser()));
        return ajaxJson;
    }

    /**
     * 暂存合同登记
     */
    @ApiOperation("新增合同登记")
    @LogMessage(content = "新增合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ConRegisterVO registerVO) {
        conRegisterService.saveRegisterApply(registerVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改合同登记
     */
    @ApiOperation("修改合同登记")
    @LogMessage(content = "修改合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    public AjaxJson update(@RequestBody ConRegisterVO registerVO) {
        conRegisterService.updateRegisterApply(registerVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 提交合同登记
     */
    @ApiOperation("提交合同登记")
    @LogMessage(content = "提交合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody ConRegisterVO registerVO) {
        conRegisterService.submitRegisterApply(registerVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @ApiOperation("查询合同登记审核列表")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conRegisterService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 获取合同登记审核详情
     */
    @ApiOperation("获取合同登记审核详情")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConRegisterVO conRegisterVO = conRegisterService.getConRegisterVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conRegisterVO.getMid()));
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conRegisterVO.setBusdtls(busdtlList);
            }
            resultMap.put("conRegisterVO", conRegisterVO);
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }

    /**
     * 获取手机端合同登记审核详情
     */
    @ApiOperation("获取手机端合同登记审核详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConInfoVO params = new ConInfoVO();
            params.setRecordId(businessId);
            ConInfoDTO conInfo = conCommonService.getConInfo(params);
            resultMap.put("conInfo", conInfo);
            resultMap.put("isApprove", true);
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }
}