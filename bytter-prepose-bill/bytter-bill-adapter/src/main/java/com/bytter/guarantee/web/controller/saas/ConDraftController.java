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
import com.bytter.guarantee.domain.service.ConDraftService;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
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
 * @author zhenghj
 * @version 1.0
 * @Title: 合同起草Controller
 * @Description: 合同起草控制器类
 * @date 2019-11-04
 */
@Api(value = "/v1-guarantee/contract/condraft", tags = "合同起草")
@RestController("v1ConDraftController")
@RequestMapping("/v1-guarantee/contract/condraft")
public class ConDraftController extends BaseController {
    @Autowired
    private ConDraftService conDraftService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;


    /**
     * 合同起草 table列表
     */
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conDraftService.getDataTablesUnion(input, this.getCurrentUser().getId());
        return AjaxJson.success(output);
    }

    /**
     * 删除起草信息
     *
     * @param rid 流水id
     * @return
     */
    @LogMessage(content = "删除起草信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson batchDelete(String rid) {
        conDraftService.batchDelete(rid, this.getCurrentUser());
        return success();
    }



    /**
     * 暂存合同起草
     */
    @LogMessage(content = "新增合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ConDraftVO draftVo) {
        conDraftService.saveDraftApply(draftVo, this.getCurrentUser());
        return success();
    }

    /**
     * 合同起草编辑页面跳转
     */
    @GetMapping("/edit/{rid}")
    public AjaxJson<Map<String, Object>> edit(@PathVariable("rid") String rid) {
        Map<String, Object> model = new HashMap<>();
        ConDraftVO conDraftVO = conDraftService.getConDraftVOByRid(rid);
        model.put("conDraftVO", conDraftVO);
        return AjaxJson.success(model);
    }

    /**
     * 修改合同起草
     */
    @LogMessage(content = "修改合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    public AjaxJson update(@RequestBody ConDraftVO draftVo) {
        conDraftService.updateDraftApply(draftVo, this.getCurrentUser());
        return success();
    }

    /**
     * 提交合同起草
     */
    @LogMessage(content = "提交合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody ConDraftVO draftVo) {
        conDraftService.submitDraftApply(draftVo, this.getCurrentUser());
        return success();
    }

    /**
     * 跳转至审核列表
     */
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus) {
        Map<String, Object> model = new HashMap<>();
        model.put("flowStatus", flowStatus);
        return AjaxJson.success(model);
    }

    /**
     * 加载审核列表数据
     */
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conDraftService.dataTableApprove(input);
        return AjaxJson.success(output);
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
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conDraftVO.setBusdtls(busdtlList);
            }
            model.put("conDraftVO", conDraftVO);
        }
        model.put("taskId", taskId);
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            ConInfoVO params = new ConInfoVO();
            params.setRecordId(businessId);
            ConInfoDTO conInfo = conCommonService.getConInfo(params);
            model.put("conInfo", conInfo);
            model.put("isApprove", true);
        }
        model.put("taskId", taskId);
        return AjaxJson.success(model);
    }
}