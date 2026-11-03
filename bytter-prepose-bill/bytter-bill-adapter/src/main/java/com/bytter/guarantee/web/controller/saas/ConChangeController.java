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
import com.bytter.guarantee.domain.service.ConChangeService;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.vo.ConChangeVO;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
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
 * @Title: 合同变更Controller
 * @Description: 合同变更控制器类
 * @date 2019-11-12
 */
@Api(value = "/v1-guarantee/contract/conchange", tags = "合同变更")
@RestController("v1ConChangeController")
@RequestMapping("/v1-guarantee/contract/conchange")
public class ConChangeController extends BaseController {
    @Autowired
    private ConChangeService conChangeService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;

    /**
     * 合同变更 table列表
     */
    @ApiOperation("查询合同变更列表")
    @LogMessage(content = "查询合同变更列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conChangeService.getDataTablesUnion(input, this.getCurrentUser().getId());
        return AjaxJson.success(output);
    }

    /**
     * 删除变更信息
     *
     * @param rid 流水id
     */
    @ApiOperation("删除合同变更信息")
    @LogMessage(content = "删除合同变更信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson batchDelete(String rid) {
        boolean result = conChangeService.batchDelete(rid, this.getCurrentUser());
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(result);
        return ajaxJson;
    }

    /**
     * 暂存/新增合同变更
     */
    @ApiOperation("新增合同变更")
    @LogMessage(content = "新增合同变更", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody  ConChangeVO changeVO) {
        conChangeService.saveChangeApply(changeVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改合同变更
     */
    @ApiOperation("修改合同变更")
    @LogMessage(content = "修改合同变更", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    public AjaxJson update(@RequestBody  ConChangeVO changeVO) {
        conChangeService.updateChangeApply(changeVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 提交合同变更
     */
    @ApiOperation("提交合同变更")
    @LogMessage(content = "提交合同变更", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody  ConChangeVO changeVO) {
        conChangeService.submitChangeApply(changeVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @ApiOperation("查询合同变更审核列表")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conChangeService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面（存在业务查询逻辑）
     */
    @ApiOperation("获取合同变更审核详情")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConChangeVO conChangeVO = conChangeService.getConChangeVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conChangeVO.getMid()));
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conChangeVO.setBusdtls(busdtlList);
            }
            model.put("conChangeVO", conChangeVO);
        }
        model.put("taskId", taskId);
        return AjaxJson.success(model);
    }

    /**
     * 手机端审核详情（存在业务查询逻辑）
     */
    @ApiOperation("获取手机端合同变更审核详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConInfoVO params = new ConInfoVO();
            params.setRecordId(businessId);
            ConInfoDTO conInfo = conCommonService.getConInfo(params);
            ConChangeVO conChangeVO = conChangeService.getConChangeVOByRid(businessId);

            model.put("conInfo", conInfo);
            model.put("conBusiness", "ConChange");
            model.put("conChangeVO", conChangeVO);
            model.put("isApprove", true);
        }
        model.put("taskId", taskId);
        return AjaxJson.success(model);
    }
}