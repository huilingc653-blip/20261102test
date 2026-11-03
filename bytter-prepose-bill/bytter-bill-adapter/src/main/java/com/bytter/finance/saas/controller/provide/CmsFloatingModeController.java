package com.bytter.finance.saas.controller.provide;

import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.provide.service.CmsFloatingModeService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.vo.CmsFloatingModeVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: 浮动方式修改
 * @date 2017-08-09 16:40:48
 */
@Api(value = "/v1-preposebill/cms/provide/cmsFloatingMode", tags = "浮动方式修改")
@RestController("v1CmsFloatingModeController")
@RequestMapping("/v1-preposebill/cms/provide/cmsFloatingMode")
public class CmsFloatingModeController extends BaseController {

    @Autowired
    private CmsFloatingModeService cmsFloatingModeService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;

    /**
     * 根据id、billCode查询浮动方式详情
     */
    @LogMessage(content = "查询浮动方式详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getInfo")
    public AjaxJson<Map<String, Object>> getInfo(String billCode, String id) {
        Map<String, Object> model = new HashMap<>();
        CmsFloatingModeVO cmsFloatingModeVO = cmsFloatingModeService.getCmsFloatingMode(id, billCode);
        LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(billCode);
        TbRateMaintenceDTO tbLoanRateMaintence = null;
        if (loanSendVo != null) {
            tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(loanSendVo.getCurCode(),
                    loanSendVo.getReferenceRateType(), loanSendVo.getRepayCycle());
        }
        model.put("cmsFloatingModeVO", cmsFloatingModeVO);
        model.put("loanSend", loanSendVo);
        model.put("tbLoanRateMaintence", tbLoanRateMaintence);
        return AjaxJson.success(model);
    }

    /**
     * 浮动方式 table分页查询
     */
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<CmsFloatingModeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String id) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsFloatingModeService.listcmsFloatingMode(input, id));
    }

    /**
     * 保存浮动方式
     */
    @LogMessage(content = "添加CmsFloatingMode", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsFloatingModeVO cmsFloatingModeVO) {
        cmsFloatingModeService.save(cmsFloatingModeVO, getCurrentUser());
        return AjaxJson.success();
    }
}