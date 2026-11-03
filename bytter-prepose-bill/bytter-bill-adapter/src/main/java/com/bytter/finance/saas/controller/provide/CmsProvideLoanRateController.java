package com.bytter.finance.saas.controller.provide;

import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanRate;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRateService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.CmsProvideLoanRateVO;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: CMS_PROVIDE_LOAN_RATE
 * @date 2017-08-29 10:36:26
 */
@Api(value = "/v1-preposebill/cms/provide/cmsprovideloanrate", tags = "利率调整")
@RestController("v1CmsProvideLoanRateController")
@RequestMapping("/v1-preposebill/cms/provide/cmsprovideloanrate")
@Slf4j
public class CmsProvideLoanRateController extends BaseController {

    @Autowired
    private CmsProvideLoanRateService cmsProvideLoanRateService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private LoanInfoService loanInfoService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;

    /**
     * 查询利率调整详情
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_RATE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getInfo")
    public AjaxJson<Map<String, Object>> getInfo(String id, String billCode) {
        Map<String, Object> model = new HashMap<>();
        CmsProvideLoanRateVO cmsProvideLoanRateVO = cmsProvideLoanRateService.getCmsProvideLoanRateVO(id, billCode);
        LoanInfoVO loanInfo = loanInfoService.getLoanInfoVOByBillCode(billCode);
        List<TbBaseRateDTO> tbBaseRates = iBaseRateService.getBaseRateListById(loanInfo.getReferenceRateType());
        boolean isBankBaseRate = false;
        if (EmptyUtils.isNotEmpty(tbBaseRates) && tbBaseRates.size() == 1
                && BaseDataConstant.IS_BANKBASERATE.equals(tbBaseRates.get(0).getIsBankBaseRate())) {
            isBankBaseRate = true;
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintenceByDate(
                loanInfo.getCurId(), loanInfo.getReferenceRateType(), loanInfo.getRepayCycle(),
                loanInfo.getRateQuotedPriceDate());
        if (null != tbLoanRateMaintence) {
            model.put("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.put("cmsProvideLoanRateVO", cmsProvideLoanRateVO);
        log.debug("getInfo isBankBaseRate=" + isBankBaseRate + ",loanInfo =" + JsonUtils.toJSONString(loanInfo));
        model.put("loanInfo", loanInfo);
        model.put("isBankBaseRate", isBankBaseRate);
        return AjaxJson.success(model);
    }

    /**
     * CMS_PROVIDE_LOAN_RATE table列表
     */
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<CmsProvideLoanRateVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String billCode) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRateService.getDataTables(input, billCode));
    }

    /**
     * 保存利率调整数据
     */
    @LogMessage(content = "添加CmsProvideLoanRate", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsProvideLoanRate cmsProvideLoanRate) {
        cmsProvideLoanRateService.save(cmsProvideLoanRate, getCurrentUser());
        return AjaxJson.success();
    }
}