package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlViewPageVO;
import com.bytter.finance.domain.inl.vo.GuaContractCmsInlVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "内部拆借数据")
@RestController("v1InlInfoController")
@RequestMapping("/v1-preposebill/inl/info")
public class InlInfoController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private InlInfoService inlInfoService;
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
    private LoanRateMaintenceService loanRateMaintenceService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    /**
     * 获取挂接担保合同的内部拆借数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ApiOperation(value = "获取挂接担保合同的内部拆借数据")
    @PostMapping("/getGuaContract")
    public AjaxJson<DataTablesOutput<GuaContractCmsInlVO>> getUseChangelist(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlInfoService.getGuaForInl(tablesInput, getCurrentUser()));
    }


    @GetMapping("/view")
    public AjaxJson<CmsInlViewPageVO> viewInlInfo(String id, @RequestParam(required = false) String flag) {
        CmsInlViewPageVO pageVO = new CmsInlViewPageVO();
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(id);
        pageVO.setCorp(corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        setParams(configService, pageVO, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        pageVO.setInlInfo(cmsInlInfoVO);
        pageVO.setFlag(flag);
        pageVO.setBaseRate(iBaseRateService.getBaseRateById(cmsInlInfoVO.getReferenceRateType()));
        if (StringUtils.isNotEmpty(cmsInlInfoVO.getRepayCycle())) {
            pageVO.setCreditPeriod(iCreditPeriodService.getById(cmsInlInfoVO.getRepayCycle()));
        }
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        pageVO.setConfig(config);
        pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_INL_SEND);
        pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);
        return success(pageVO);
    }

    public static void setParams(IConfigService configService, CmsInlViewPageVO pageVO, String id, InlInfoService inlInfoService,
                                 IConService conService, IGuaContractOccupServie guaContractOccupServie) {
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        Map<String, Object> inlConfig = (Map<String, Object>) config.get(ConfigConstant.INL);
        if (null != config) {
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_HANG_GUA).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsHangGuarantee())) {
                    pageVO.setGuaInfo(guaContractOccupServie.getGuaContractOccupyDtl(cmsInlInfo.getId(),
                            cmsInlInfo.getGuaContract()));
                }
            }
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_CON).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsCon())) {
                    ConInfoDTO conInfoDTO = conService.getConInfo(cmsInlInfo.getConId(), cmsInlInfo.getId());
                    pageVO.setConInfo(conInfoDTO);
                    if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.CON_TYPE).toString())
                            && null != conInfoDTO) {
                        pageVO.setConTypeId(conInfoDTO.getTypeId());
                    }
                }
            }
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_HANG_CREDIT).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsHangCredit().toString())) {
                    pageVO.setCpURationDTO(inlInfoService.getCpratUrationDTO(cmsInlInfo.getId()));
                }
            }
        }
    }


    /**
     * 列表条转
     *
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> addInlInfo() {
        Map<String, Object> model = new HashMap<>();
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        Map<String, Object> inlConfig = (Map<String, Object>) config.get(ConfigConstant.INL);
        model.put("relateCon", inlConfig.get("is_con"));
        model.put("conType", inlConfig.get("con_type"));
        Map<String, Object> inl = (Map<String, Object>) config.getOrDefault("inl", Maps.newHashMap());
        model.put("relateRat", inl.get("is_hang_credit"));
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
    public AjaxJson<Map<String, Object>> editInlInfo(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        model.put("corp", corpService.get(cmsInlInfo.getBorrowInCorp()));
        Map<String, Object> inlConfig = (Map<String, Object>) configService.listByType(ConfigConstant.INL)
                .get(ConfigConstant.INL);
        model.put("relateCon", inlConfig.get("is_con"));
        model.put("conType", inlConfig.get("con_type"));
        model.put("conId", cmsInlInfo.getConId());
        InlUtil.setParams(configService, model, cmsInlInfo.getId(), inlInfoService, conCommonService, guaContractOccupServie);
        model.put("inlInfo", cmsInlInfo);
        model.put("repayPlan", inlInfoService.getRepayPlan(cmsInlInfo.getId()));
        model.put("baseRate", iBaseRateService.getBaseRateById(cmsInlInfo.getReferenceRateType()));
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(cmsInlInfo.getCurId(),
                cmsInlInfo.getReferenceRateType(), cmsInlInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.put("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(model);
    }


    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsInlInfoVO inlInfoVO) {
        inlInfoService.save(inlInfoVO, getCurrentUser());
        return success();
    }

    /**
     * @return
     */
    @PostMapping("/delete")
    public AjaxJson deleteById(String id) {
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        cmsInlInfo.setStatus(InlConstant.DELETE);
        inlInfoService.updateEntity(cmsInlInfo);
        /**
         * 业务挂接释放
         */
        inlInfoService.releaseBusiness(cmsInlInfo, getCurrentUser());
        return success();
    }

    /**
     * 列表查询
     *
     * @param dataTablesInput
     * @return
     */

    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlInfoService.datatale(input, getCurrentUser()));
    }

    /**
     * 列表查询
     *
     * @param dataTablesInput
     * @return
     */

    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlInfoService.approveDataTable(input, getCurrentUser()));
    }


    /**
     * 选择账户页面
     *
     * @return
     */
    @GetMapping("/selectAcc")
    public AjaxJson<Map<String, Object>> selectAcc(String borrowInCorp, String borrowOutCorp, String flag, String curId, String accId) {
        Map<String, Object> model = new HashMap<>();
        model.put("borrowInCorp", borrowInCorp);
        model.put("borrowOutCorp", borrowOutCorp);
        model.put("curId", curId);
        model.put("accId", accId);
        model.put("flag", flag);
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(accId);
        if (null != btBankAccDTO) {
            model.put("accType", "bankAcc");
        } else {
            model.put("accType", "fcAcc");
        }
        return AjaxJson.success(model);
    }


    /**
     * @return
     */

    @PostMapping("/listAccDataTable")
    public AjaxJson<DataTablesOutput> listAccDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlInfoService.listAcc(input));
    }

    /**
     * @return
     */

    @PostMapping("/listBankAccDataTable")
    public AjaxJson<DataTablesOutput> listBankAccDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlInfoService.listBankAcc(input));
    }

    /**
     * @return
     */

    @PostMapping("/listFcAccDataTable")
    public AjaxJson<DataTablesOutput> listFcAccDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlInfoService.listFcAcc(input));
    }
}
