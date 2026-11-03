package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.service.ConDraftService;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.service.ContractSignedService;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.vo.ConDraftVO;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Title: 合同签署Contorl.java
 * @Package com.bytter.biz.contract.controller
 * @Description: 合同签署
 * @author: Jinyong.cao
 * @date: 2019-11-9 上午11:53:44
 */
@Api(value = "/v1-guarantee/contract/signed", tags = "合同签署")
@RestController("v1ContractSignedController")
@RequestMapping("/v1-guarantee/contract/signed")
public class ContractSignedController extends BaseController {
    @Autowired
    private ContractSignedService contractSignedService;
    @Autowired
    private ConDraftService conDraftService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;

    /**
     * 查询合同签署列表数据
     */
    @LogMessage(content = "查询合同签署列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = contractSignedService.getDataTablesUnion(input, this.getCurrentUser().getId());
        return AjaxJson.success(output);
    }

    /**
     * 加载签署审核列表数据
     */
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = contractSignedService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量操作
     */
    @LogMessage(content = "合同签署批量操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchOpt")
    public AjaxJson batchOpt(String ids, String opt, String reason) {
        AjaxJson ajaxJson = new AjaxJson();
        try {
            contractSignedService.batchConSignedOptWith(ids, opt, reason, this.getCurrentUser());
            ajaxJson.setSuccess(true);
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(e.getMessage());
        }
        return ajaxJson;
    }

    /**
     * 单行操作
     */
    @LogMessage(content = "合同签署单条操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/sigOpt")
    public AjaxJson siginOpt(String id, String opt, String reason) {
        AjaxJson ajaxJson = new AjaxJson();
        try {
            contractSignedService.singleDispose(id, opt, this.getCurrentUser(), reason);
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("操作成功");
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败:" + e.getMessage());
        }
        return ajaxJson;
    }

    /**
     * 跳转至审核详情界面
     */
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConDraftVO conDraftVO = conDraftService.getConDraftVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conDraftVO.getMid()));
            if (null != conBusdtlList && conBusdtlList.size() > 0) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conDraftVO.setBusdtls(busdtlList);
            }
            model.put("conDraftVO", conDraftVO);
        }
        model.put("taskId", taskId);
        return AjaxJson.success(model);
    }

    /**
     * 手机端签署审核详情
     */
    @GetMapping("/mobile/approve")
    public AjaxJson<Map<String, Object>> mobileSignApprove(String businessId, String flowStatus) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConInfoVO params = new ConInfoVO();
            params.setRecordId(businessId);
            ConInfoDTO conInfo = conCommonService.getConInfo(params);
            model.put("conInfo", conInfo);
        }
        model.put("flowStatus", flowStatus);
        return AjaxJson.success(model);
    }
}