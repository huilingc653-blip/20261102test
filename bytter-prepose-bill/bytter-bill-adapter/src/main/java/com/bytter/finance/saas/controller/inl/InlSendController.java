package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Api(tags = "内部拆借放款")
@RestController("v1InlSendController")
@RequestMapping("/v1-preposebill/inl/send")
public class InlSendController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private RepayTypeService repayTypeService;

    /**
     * 查询申请审批通过数据
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/approvedDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApproved(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlSendService.listInlInfoApproved(input, getCurrentUser()));
    }

    /**
     * 查询暂存打回数据
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/listSendUpdateDataTable")
    public AjaxJson<DataTablesOutput> listSendUpdateDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlSendService.listSendUpdateDataTable(input, getCurrentUser()));
    }

    /**
     * 查询待审批数据
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlsendApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlSendService.approveDataTable(input, getCurrentUser()));
    }



    /**
     * 查询待确认数据
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/listSendConfirmDataTable")
    public AjaxJson<DataTablesOutput> listSendConfirmDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlSendService.listSendConfirmDataTable(input, getCurrentUser()));
    }

    @ResponseBody
    @PostMapping("/sendConfirm")
    public AjaxJson sendConfirm(String id, Date realSendDate) {
        inlSendService.sendConfirm(id, realSendDate);
        return success();
    }

    /**
     * 保存数据
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody CmsInlSendVO cmsInlSendVO) {
        inlSendService.save(cmsInlSendVO, getCurrentUser());
        return success();
    }

    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(id);
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.put("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.put("inlInfo", cmsInlInfoVO);
        model.put("baseRate", iBaseRateService.getBaseRateById(cmsInlInfoVO.getReferenceRateType()));
        model.put("creditPeriod", iCreditPeriodService.getById(cmsInlInfoVO.getRepayCycle()));
        model.put("repayPlan", inlSendService.getRepayPlan(cmsInlInfoVO.getId()));
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(model);
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> edit(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(cmsInlSendVO.getInlInfoId());
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.put("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlSend", cmsInlSendVO);
        model.put("baseRate", iBaseRateService.getBaseRateById(cmsInlSendVO.getReferenceRateType()));
        if (StringUtils.isNotEmpty(cmsInlSendVO.getRepayCycle())) {
            model.put("creditPeriod", iCreditPeriodService.getById(cmsInlSendVO.getRepayCycle()));
        }
        model.put("repayPlan", inlSendService.getRepayPlan(cmsInlSendVO.getId()));
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(model);
    }

    /**
     * 查看页面
     *
     * @param id
     * @return
     */
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> view(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(cmsInlSendVO.getInlInfoId());
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.put("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlSend", cmsInlSendVO);
        if (StringUtils.isNotEmpty(cmsInlSendVO.getReferenceRateType())) {
            model.put("baseRate", iBaseRateService.getBaseRateById(cmsInlSendVO.getReferenceRateType()));
        }
        if (StringUtils.isNotEmpty(cmsInlSendVO.getRepayCycle())) {
            model.put("creditPeriod", iCreditPeriodService.getById(cmsInlSendVO.getRepayCycle()));
        }
        if (StringUtils.isNotEmpty(cmsInlSendVO.getRepayType())) {
            RepayTypeVO repayType = repayTypeService.getRepayTypeInfoById(cmsInlSendVO.getRepayType());
            if (!EmptyUtils.isEmpty(repayType)) {
                cmsInlSendVO.setRepayTypeName(repayType.getRepayName());
            }
        }
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(model);
    }

    /**
     * 查看页面
     *
     * @param id
     * @return
     */
    @GetMapping("/viewAllInlSend")
    public AjaxJson<Map<String, Object>> viewAllInlSend(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(id);
        model.put("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlSends", inlSendService.getSendsByInfoId(cmsInlInfoVO.getId()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        return AjaxJson.success(model);
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson removeById(String id) {
        inlSendService.removeById(id);
        return success();
    }
}
