package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.CmsExpireAnalysisService;
import com.bytter.report.domain.cms.vo.CmsExpireAnalysisVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.increment.api.service.IBiRemainTermService;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-19 14:13
 * @Version 1.0
 */
@Controller
@RequestMapping("/cms/report/cmsExpireAnalysis")
public class CmsExpireAnalysisController extends BaseController {

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


    //@RequiresPermissions(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY)
    @GetMapping("/showExpireAnalysi")
    public String viewCmsExpireAnalysis(Model model){
        return "cms/report/cmsLoan/cmsExpireAnalysis";
    }


    @RequestMapping("/mobileExpireAnalysi")
    public String viewMobileCmsExpireAnalysis(Model model){
        model.addAttribute("cmsExpireAnalysisVOS", cmsExpireAnalysisService.getMobileCmsLoanDataTable(null, getCurrentUser()));
        model.addAttribute("curList", currencyService.getBtCurrency());
        model.addAttribute("loanTypes",dictService.list("Loan.Sign"));
        model.addAttribute("biRemainTerms", biRemainTermService.getBiRemainTermDTOList());
        return "cms/report/cmsLoan/MobileCmsExpireAnalysis";
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

    //@RequiresPermissions(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY)
    @PostMapping("/getDataTables")
    @ResponseBody
    public DataTablesOutput getDataTables(@RequestBody DataTablesInput input){
        return cmsExpireAnalysisService.getCmsLoanDataTable(input, getCurrentUser());
    }

    /**
     *获取剩余期限
     * @return
     */
    @GetMapping("/getBiRemainTerm")
    @ResponseBody
    public List<com.bytter.report.api.dto.BiRemainTermDTO> getBiRemainTerm(){
        return cmsExpireAnalysisService.getBiRemainTerm();
    }

    /**
     * 查看明细
     * @return
     */
    @GetMapping("/viewCmsLoanDtl")
    public String viewCmsLoanDtl(String corpId, String loanType, String remainTermId, String curId, Model model) {
        String page = "";
        if ("01".equals(loanType)) {//内部贷款
            page = "cmsLoanDtl";
        } else if ("02".equals(loanType)) {//银行借款
            page = "cmsProvideDtl";
        } else {//委托贷款
            page = "cmsPrecativeDtl";
        }
        model.addAttribute("corpId", corpId);
        model.addAttribute("loanType", loanType);
        model.addAttribute("remainTermId", remainTermId);
        model.addAttribute("curId", curId);
        return "cms/report/cmsLoan/" + page;
    }

    /**
     * 获取明细
     * @param input
     * @return
     */
    @PostMapping("/getCmsLoanDtlData")
    @ResponseBody
    public DataTablesOutput getCmsLoanDtlData(@RequestBody DataTablesInput input) {
        DataTablesOutput output = null;
        Map<String, Object> params = input.getFormdata();
        String loanType = null;
        String corpId = null;
        String remainTermId = null;
        String curId = null;
        boolean flag = false;
        if (null != params.get("loanType")) {
            loanType = params.get("loanType").toString();
        }else{
            flag = true;
        }
        if (null != params.get("corpId")) {
            corpId = params.get("corpId").toString();
        }else{
            flag = true;
        }
        if (null != params.get("remainTermId")) {
            remainTermId = params.get("remainTermId").toString();
        }else{
            flag = true;
        }
        if (null != params.get("curId")) {
            curId = params.get("curId").toString();
        }else{
            flag = true;
        }
        if (flag) {
            output = new DataTablesOutput();
        }else {
            if ("01".equals(loanType)) {//内部贷款
                output = cmsExpireAnalysisService.getCmsLoanDtl(corpId, remainTermId, curId);
            } else if ("02".equals(loanType)) {//银行借款
                output = cmsExpireAnalysisService.getCmsPrivodeDtl(corpId, remainTermId, curId);
            } else {//委托贷款
                output = cmsExpireAnalysisService.getCmsPrecativeDtl(corpId, remainTermId, curId);
            }
        }
        return output;
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
    @RequestMapping("/getMobileDtlData")
    public String getMobileDtlData(String loanType, String corpId, String remainTermId, String curId, Model model) {
        String page = "";
        if ("01".equals(loanType)) {//内部贷款
            model.addAttribute("dtlData", cmsExpireAnalysisService.getMobileLoanDtl(corpId, remainTermId, curId));
            page = "MobileLoanDtl";
        } else if ("02".equals(loanType)) {//银行借款
            model.addAttribute("dtlData", cmsExpireAnalysisService.getMobilePrivodeDtl(corpId, remainTermId, curId));
            page = "MobileProvideDtl";
        } else {//委托贷款
            model.addAttribute("dtlData", cmsExpireAnalysisService.getMobilePrecativeDtl(corpId, remainTermId, curId));
            page = "MobilePrecativeDtl";
        }
        return "cms/report/cmsLoan/" + page;
    }

    /**
     * 获取图标option
     *
     * @param isInclude
     * @param curId
     * @param moneyType
     * @param corpIds
     * @return
     */
    @PostMapping("/getchartsOption")
    @ResponseBody
    public Object getchartsOption(String isInclude, String curId, String convertCurId, String moneyType, String isConvert,
                                  @RequestParam(name = "corpIds", required = false, defaultValue = "") String corpIds) {
        List<String> corpArr = new ArrayList<>();
        if (StringUtils.isNotEmpty(corpIds) && corpIds.contains(",")) {
            corpArr.addAll(Arrays.asList(corpIds.split(",")));
        } else {
            corpArr.addAll(corpService.getUserCorpIds(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY, getCurrentUser().getId()));
        }
        String barOption = cmsExpireAnalysisService.getBarOption(isInclude, curId, convertCurId, moneyType, corpArr, isConvert);
        String pieOption = cmsExpireAnalysisService.getPieOption(isInclude, curId, convertCurId, moneyType, corpArr, isConvert);
        Map<String, Object> options = new HashMap<>();
        options.put("barOption", barOption);
        options.put("pieOption", pieOption);
        return options;
    }

    /**
     * 跳转到统计图页面表格数据
     *
     * @param isInclude
     * @param curId
     * @param moneyType
     * @param model
     * @return
     */
    @GetMapping("/listChartsData")
    public String listChartsData(String isInclude, String curId,String convertCurId, String moneyType, String isConvert, Model model) {
        model.addAttribute("isInclude", isInclude);
        model.addAttribute("curId", curId);
        model.addAttribute("moneyType", moneyType);
        model.addAttribute("isConvert", isConvert);
        model.addAttribute("convertCurId", convertCurId);
        return "cms/report/cmsLoan/cmsChartsDataTable";
    }

    /**
     * 统计图下方表格数据
     * @param input
     * @return
     */
    @PostMapping("/getchartsDataTable")
    @ResponseBody
    public DataTablesOutput getchartsDataTable(@RequestBody DataTablesInput input) {
        return cmsExpireAnalysisService.getchartsDataTable(input, getCurrentUser());
    }

    /**
    *币种转换校验
    *@Param origCurId
    *@Param convertCurId
    *@return
    */
    @PostMapping("/checkConvertCurId")
    @ResponseBody
    public AjaxJson checkConvertCurId(String origCurId, String convertCurId){
        cmsExpireAnalysisService.checkConvertCurId(origCurId, convertCurId);
        return success();
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
