package com.bytter.report.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.increment.api.service.IBiRemainTermService;
import com.bytter.report.domain.cms.service.CmsExpireAnalysisService;
import com.bytter.report.domain.cms.vo.CmsExpireAnalysisVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-19 14:13
 * @Version 1.0
 */
@Controller
@RequestMapping("/mobile/report/cms/cmsExpireAnalysis")
public class CmsExpireAnalysisMobileExtController extends BaseController {

    @Autowired
    private CmsExpireAnalysisService cmsExpireAnalysisService;

    @Autowired
    private IBtCurrencyService currencyService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IBiRemainTermService biRemainTermService;

    @Autowired(required = false)
    private ICorpService corpService;


    /**
     * 【到期贷款分析】
     * @param model
     * @return
     */
    @RequestMapping("/mobileExpireAnalysi")
    public String viewMobileCmsExpireAnalysis(Model model){
        model.addAttribute("cmsExpireAnalysisVOS", cmsExpireAnalysisService.getMobileCmsLoanDataTable(null, getCurrentUser()));
        model.addAttribute("curList", currencyService.getBtCurrency());
        model.addAttribute("loanTypes",dictService.list("Loan.Sign"));
        model.addAttribute("biRemainTerms", biRemainTermService.getBiRemainTermDTOList());
        return "mobile/report/cmsLoan/mobileCmsExpireAnalysis";
    }

    /**
    *报表数据列表筛选
    *@Param cmsExpireAnalysisVO
    *@return
    */
    @PostMapping("/getMobileData")
    @ResponseBody
    public AjaxJson getMobileDataTable(CmsExpireAnalysisVO cmsExpireAnalysisVO) {
        AjaxJson ajaxJson = new AjaxJson();
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", cmsExpireAnalysisVO.getCorpId());//币种
        params.put("loanType", cmsExpireAnalysisVO.getLoanType());//融资类型
        params.put("expireAnalysis", cmsExpireAnalysisVO.getExpireAnalysis());//期限类型
        params.put("curId", cmsExpireAnalysisVO.getCurId());//币种
        params.put("isConvert", cmsExpireAnalysisVO.getIsConvert());//是否折合币种
        params.put("convertCurId", cmsExpireAnalysisVO.getConvertCurId());//折合币种
        params.put("isInclude", cmsExpireAnalysisVO.getIsInclude());//是否包含在途
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(cmsExpireAnalysisService.getMobileCmsLoanDataTable(params, getCurrentUser()));
        return ajaxJson;
    }

    /**
    *手机详情页跳转
    *@Param loanType
    *@Param corpId
    *@Param remainTermId
    *@Param curId
    *@Param model
    *@return
    */
    @PostMapping("/getMobileDtlData")
    public String getMobileDtlData(String loanType, String corpId, String remainTermId, String curId, Model model) {
        String page = "";
        if ("01".equals(loanType)) {//内部贷款
            model.addAttribute("dtlData", cmsExpireAnalysisService.getMobileLoanDtl(corpId, remainTermId, curId));
            page = "mobileLoanDtl";
        } else if ("02".equals(loanType)) {//银行借款
            model.addAttribute("dtlData", cmsExpireAnalysisService.getMobilePrivodeDtl(corpId, remainTermId, curId));
            page = "mobileProvideDtl";
        } else {//委托贷款
            model.addAttribute("dtlData", cmsExpireAnalysisService.getMobilePrecativeDtl(corpId, remainTermId, curId));
            page = "mobilePrecativeDtl";
        }
        return "mobile/report/cmsLoan/" + page;
    }

    /**
    *构造手机图表数据
    *@Param isInclude
    *@Param curId
    *@Param convertCurId
    *@Param moneyType
    *@Param corpIds
    *@Param isConvert
    *@return
    */
    @PostMapping("/getchartData")
    @ResponseBody
    public AjaxJson getchertData(@RequestParam(required = false) String isInclude, String curId,
                                     @RequestParam(required = false) String convertCurId,
                                     @RequestParam(required = false, defaultValue = "1")String moneyType,
                                     @RequestParam(required = false) String corpId,
                                     @RequestParam(required = false, defaultValue = "0") String isConvert){
        Map<String, Object> barData = new HashMap<>();
        barData.put("xData", cmsExpireAnalysisService.getBiRemainTerms());
        List<String> corpIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(corpId)) {
            corpIds.addAll(Arrays.asList(corpId.split(",")));
        } else {
            corpIds.addAll(corpService.getUserCorpIds(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY, getCurrentUser().getId()));
        }
        barData.put("mobileDate", cmsExpireAnalysisService.getMobileData(isInclude,curId,convertCurId,moneyType,corpIds,isConvert));
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(barData);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }


}
