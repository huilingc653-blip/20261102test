package com.bytter.biz.foreign.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranTemplateConfig;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCotpService;
import com.bytter.biz.foreign.service.FexCustFieldRuleTenantService;
import com.bytter.biz.foreign.service.FexCustInfoTenantService;
import com.bytter.biz.foreign.service.FexTranConfigTemplateService;
import com.bytter.biz.foreign.service.FexTranCustInfoService;
import com.bytter.biz.foreign.service.FexTranDealService;
import com.bytter.biz.foreign.service.FexTranReqConfigService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.utils.FexTranReqUtil;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.BankConstant;
import com.bytter.system.api.dto.BankTypeCustomDTO;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BankTypeOtherCustomDTO;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 自定义交易管理表
 * @author wuxc
 * @date 2026-05-18
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fextranreqCust")
public class FexTranReqCustController extends BaseController {

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexCustFieldRuleTenantService fexCustFieldRuleTenantService;

    @Autowired
    private FexCustInfoTenantService fexCustInfoTenantService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private IUserService userService;

    @Autowired
    private FexTranCustInfoService fexTranCustInfoService;

    @Autowired
    private FexTranDealService fexTranDealService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;

    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;
    @Autowired
    private FexCotpService fexCotpService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    
    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;

    @Autowired
    private FexTranReqConfigService fexTranReqConfigService;

    /**
     * 交易对手关联客商是否境外：返回 "0" 境外 / "1" 境内 / "" 未关联到客商（此时页面按"国家是否中国"判定）
     * <p>境外客商主数据里 PROVINCE/CITY 存的是国家、地区（sys_country_cust），只有境内客商才是省/市（sys_region），
     * 故页面"省/市"与"国家+地区"的显隐必须跟随客商属性，不能只看国家是否中国。</p>
     */
    private String getCotpIsNative(FexTranReqVO fexTranReq) {
        if (fexTranReq == null || StringUtils.isBlank(fexTranReq.getCotpId())) {
            return "";
        }
        FexCotpVO fexCotp = fexCotpService.getFexcotpById(fexTranReq.getCotpId());
        if (fexCotp == null || StringUtils.isBlank(fexCotp.getOppId())) {
            return "";
        }
        ExternalCorpDTO externalCorp = externalCorpService.getById(fexCotp.getOppId());
        if (externalCorp == null || externalCorp.getIsNative() == null) {
            return "";
        }
        return String.valueOf(externalCorp.getIsNative());
    }

    /**
     * 设置"交易对手所在国家&地区"的展示口径（编辑页/复制页用）
     * <p>cotpUseRegion=true = 页面展示省/市（境内客商 + 国家为中国）；false = 页面展示国家 + 地区。</p>
     */
    private void setCotpRegionFlag(Model model, FexTranReqVO fexTranReq, String chinaId) {
        String cotpIsNative = this.getCotpIsNative(fexTranReq);
        boolean cotpUseRegion = !"0".equals(cotpIsNative)
                && StringUtils.isNotBlank(chinaId)
                && StringUtils.equals(chinaId, fexTranReq.getCotpCountry());
        model.addAttribute("cotpIsNative", cotpIsNative);
        model.addAttribute("cotpUseRegion", cotpUseRegion);
    }

    /**
     *
     * 收付交易（动态配置）
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addPay(Model model) {
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", "4");
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //初始化参数
        this.initConfig(model);
        //获取国家地区-中国
        SysCountryCustDTO china = sysCountryCustService.getByNameZh("中国");
        model.addAttribute("chinaId", china == null ? "":china.getId());

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());

        return "foreign/foreignPayReq/addFexTranReqPayCust";
    }

    /**
     * 资金收付交易自定义信息新增片段。
     *
     * @param tranType 交易类型
     * @param model 页面模型
     * @return 自定义信息新增片段
     */
    @GetMapping("/addCustInfo/{tranType}")
    public String addCustInfo(@PathVariable("tranType") String tranType, Model model) {
        model.addAttribute("custFields", fexCustFieldRuleTenantService.listByAppRange(CommonConstant.YES, tranType));
        model.addAttribute("hideCustInfoButtons", true);
        return "foreign/addFexTranCustInfo";
    }



    /**
     *
     * 模板、支付方式和银行账号修改联动刷新页面
     *
     * @param fexTranReq
     * @return
     */
    @LogMessage(content = "模板、支付方式和银行账号修改联动刷新页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @GetMapping("/change")
    public String change(FexTranReqVO fexTranReq, String oldTemplateId, String oldCorpBankAcc, String oldBankPayType, Model model){
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", "4");
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());

        // 模板变更
        if (StringUtils.isNotEmpty(fexTranReq.getTemplateId()) 
                && StringUtils.isNotEmpty(oldTemplateId) 
                &&  !oldTemplateId.equals(fexTranReq.getTemplateId())){
            FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(fexTranReq.getTemplateId());
            FexTranTemplateConfig oldFexTranTemplateConfig = fexTranConfigTemplateService.get(fexTranReq.getTemplateId());
            // 主体银行变更
            if (fexTranTemplateConfig.getBankTypeId().equals(oldFexTranTemplateConfig.getBankTypeId())){
                fexTranReq.setCorpAccId(null);
                fexTranReq.setCorpAccName(null);
                fexTranReq.setCorpAccCurId(null);
            }
        }

        // 主体单位银行账户变更
        if (StringUtils.isNotEmpty(fexTranReq.getCorpBankAcc())
                && StringUtils.isNotEmpty(oldCorpBankAcc)
                && StringUtils.isNotEmpty(fexTranReq.getBankPayType())
                && !oldCorpBankAcc.equals(fexTranReq.getCorpBankAcc())){
            FexTranTemplateConfigVO templateByBankAcc = fexTranConfigTemplateService.getTemplateByBankAcc(fexTranReq.getCorpBankAcc(), fexTranReq.getBankPayType());
            if (null != templateByBankAcc){
                fexTranReq.setTemplateId(templateByBankAcc.getId());
            } else {
                fexTranReq.setTemplateId(null);
            }
        }

        // 支付类型变更
        if (StringUtils.isNotEmpty(fexTranReq.getBankPayType())
                && StringUtils.isNotEmpty(oldBankPayType)
                && StringUtils.isNotEmpty(fexTranReq.getCorpBankAcc())
                && !oldBankPayType.equals(fexTranReq.getBankPayType())){
            FexTranTemplateConfigVO templateByBankAcc = fexTranConfigTemplateService.getTemplateByBankAcc(fexTranReq.getCorpBankAcc(), fexTranReq.getBankPayType());
            if (null != templateByBankAcc){
                fexTranReq.setTemplateId(templateByBankAcc.getId());
            } else {
                fexTranReq.setTemplateId(null);
            }
        }

        model.addAttribute("fexTranReq", fexTranReq);
        //初始化参数
        this.initConfig(model);
        return "foreign/foreignPayReq/addFexTranReqPayCust";
    }

    @LogMessage(content = "模板、支付方式和银行账号修改联动刷新接口",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @ResponseBody
    @GetMapping("/getTemplate")
    public AjaxJson getTemplate(FexTranReqVO fexTranReq, String oldTemplateId, String oldCorpBankAcc, String oldBankPayType){
        Map<String, Object> resMap = new HashMap<>();
        FexTranTemplateConfigVO fexTranTemplateConfig = fexTranConfigTemplateService.getTemplateConfigById(fexTranReq.getTemplateId());
        fexTranReq.setBankPayTypeId(fexTranTemplateConfig.getPayTypeId());
        fexTranReq.setCorpBank(fexTranTemplateConfig.getBankTypeId());
        fexTranReq.setBankPayTypeId(fexTranTemplateConfig.getPayTypeId());
        fexTranReq.setBankPayType(fexTranTemplateConfig.getBankPayType());
        fexTranReq.setBankPayTypeName(fexTranTemplateConfig.getPayTypeName());
        // 模板变更
        if (StringUtils.isNotEmpty(fexTranReq.getTemplateId())
                && StringUtils.isNotEmpty(oldTemplateId)
                &&  !oldTemplateId.equals(fexTranReq.getTemplateId())){
            FexTranTemplateConfigVO oldFexTranTemplateConfig = fexTranConfigTemplateService.getTemplateConfigById(oldTemplateId);
            // 主体银行变更
            if (!fexTranTemplateConfig.getBankTypeId().equals(oldFexTranTemplateConfig.getBankTypeId())){
                fexTranReq.setCorpAccId(null);
                fexTranReq.setCorpBankAcc(null);
                fexTranReq.setCorpAccName(null);
                fexTranReq.setCorpAccCurId(null);
            }
        }

        // 主体单位银行账户变更
        if (StringUtils.isNotEmpty(fexTranReq.getCorpBankAcc())
                && StringUtils.isNotEmpty(oldCorpBankAcc)
                && StringUtils.isNotEmpty(fexTranReq.getBankPayType())
                && !oldCorpBankAcc.equals(fexTranReq.getCorpBankAcc())){
            FexTranTemplateConfigVO templateByBankAcc = fexTranConfigTemplateService.getTemplateByBankAcc(fexTranReq.getCorpBankAcc(), fexTranReq.getBankPayType());
            if (null != templateByBankAcc){
                fexTranReq.setTemplateId(templateByBankAcc.getId());
            } else {
                fexTranReq.setTemplateId(null);
            }
        }

        // 支付类型变更
        if (StringUtils.isNotEmpty(fexTranReq.getBankPayType())
                && StringUtils.isNotEmpty(oldBankPayType)
                && StringUtils.isNotEmpty(fexTranReq.getCorpBankAcc())
                && !oldBankPayType.equals(fexTranReq.getBankPayType())){
            FexTranTemplateConfigVO templateByBankAcc = fexTranConfigTemplateService.getTemplateByBankAcc(fexTranReq.getCorpBankAcc(), fexTranReq.getBankPayType());
            if (null != templateByBankAcc){
                fexTranReq.setTemplateId(templateByBankAcc.getId());
            } else {
                fexTranReq.setTemplateId(null);
            }
        }

        BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(fexTranTemplateConfig.getBankTypeId());
        resMap.put("bankType", bankTypeDTO);
        resMap.put("fexTranReq", fexTranReq);
        return success(resMap);
    }


    /**
     * 外汇交易表 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(FexTranReqVO fexTranReq, Model model) {
        if (StringUtils.isNotEmpty(fexTranReq.getId())) {
            fexTranReq = fexTranReqService.getNewByIdUnion(fexTranReq.getId());
            //复制标志
            fexTranReq.setCloneFlag(CommonConstant.NO);
            //设置支付类型
            this.setBankPayType(fexTranReq, model);

            model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            //交易员
            model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
            //资金收付交易
            if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
                BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(fexTranReq.getCorpBank());
                String remitMethod = "0,1";
                if (bankTypeDTO != null && StringUtils.isNotBlank(bankTypeDTO.getRemitMethod())) {
                    remitMethod = remitMethod + CommonConstant.COMMA_SPLIT + bankTypeDTO.getRemitMethod();
                }
                model.addAttribute("remitMethod", remitMethod);
                //获取付款账户
                BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());

                if(payerAcc != null && StringUtils.isNotBlank(payerAcc.getBifCode()) && StringUtils.isBlank(payerAcc.getBifCodeStr())) {
                    BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(payerAcc.getBifCode());
                    payerAcc.setBifCodeStr(bifInitDTO == null?"":bifInitDTO.getBifCode());
                }
                model.addAttribute("payerAcc", payerAcc);
                if (payerAcc != null && CommonConstant.NO.equals(payerAcc.getIsDomesticBank()) && StringUtils.isNotBlank(payerAcc.getProv())) {
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(payerAcc.getProv());
                    model.addAttribute("payLocalClearNo", payerAcc.getLocalClearNo());
                    model.addAttribute("payBankSwiftCode", payerAcc.getBankCode());
                    if (sysCountryCustDTO != null) {
                        model.addAttribute("payerCountryId", sysCountryCustDTO.getId());
                        model.addAttribute("payerCountryNameZh", sysCountryCustDTO.getNameZh());
                        model.addAttribute("payerEnCode", sysCountryCustDTO.getEnCode());
                    }
                    //境外银行地址
                    if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())&& StringUtils.isNotBlank(payerAcc.getBankCode())) {
                        BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(payerAcc.getBankCode());
                        if (btSwiftInfoDTO != null) {
                            payerAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                            model.addAttribute("bankAdd", payerAcc.getBankAdd());
                        }
                    }
                }
                if(EmptyUtils.isNotEmpty(fexTranReq.getCotpId())){
                    FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranReq.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
                }
            }

            if(StringUtils.isNotBlank(fexTranReq.getMergeId()) || StringUtils.isNotBlank(fexTranReq.getSplitId())){
                model.addAttribute("editPayMoneyFlag", "0");
            }else{
                model.addAttribute("editPayMoneyFlag", "1");
            }
            CorpDTO corpDTO = iCorpService.get(fexTranReq.getCorpId());
            model.addAttribute("corpDTO", corpDTO);

            if (StringUtils.isEmpty(fexTranReq.getCorpAddrEn())){
                fexTranReq.setCorpAddrEn(corpDTO.getAddressEn());
            }

            if (StringUtils.isEmpty(fexTranReq.getCorpAddr())){
                fexTranReq.setCorpAddr(corpDTO.getAddress());
            }

            if (StringUtils.isEmpty(fexTranReq.getCorpNameEn())){
                fexTranReq.setCorpNameEn(corpDTO.getNameEn());
            }
        }
        model.addAttribute("fexTranReqPage", fexTranReq);
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);

        // 自定义模板字段
        getCustFields(fexTranReq.getId(), model);

        //获取国家地区-中国
        SysCountryCustDTO china = sysCountryCustService.getByNameZh("中国");
        model.addAttribute("chinaId", china == null ? "":china.getId());
        //交易对手"省/市"与"国家+地区"的显隐口径（跟随交易对手关联客商的境内/境外属性）
        this.setCotpRegionFlag(model, fexTranReq, china == null ? "":china.getId());

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());

        return "foreign/foreignPayReq/updateFexTranReqPayCust";
    }

    /**
     * 资金收付交易自定义信息修改片段。
     *
     * @param id 资金收付交易ID
     * @param model 页面模型
     * @return 自定义信息修改片段
     */
    @GetMapping("/updateCustInfo/{id}")
    public String updateCustInfo(@PathVariable("id") String id, Model model) {
        FexTranReq fexTranReq = fexTranReqService.get(id);
        String tranType = fexTranReq == null ? null : fexTranReq.getTranType();
        model.addAttribute("custFields", fexCustInfoTenantService.listCustInfoByBusIdAndAppRange(id, CommonConstant.YES, tranType));
        model.addAttribute("hideCustInfoButtons", true);
        return "foreign/updateFexTranCustInfo";
    }

    /**
     * 资金收付交易自定义信息查看片段。
     *
     * @param id 资金收付交易ID
     * @param model 页面模型
     * @return 自定义信息查看片段
     */
    @GetMapping("/viewCustInfo/{id}")
    public String viewCustInfo(@PathVariable("id") String id, Model model) {
        getCustFields(id, model);
        return "foreign/viewFexTranCustInfo";
    }


    /**
     * 外汇交易表 详情页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id,  Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getNewByIdUnion(id);
        getCustFields(id, model);
        model.addAttribute("fexTranReqPage", fexTranReq);
        return "foreign/foreignPayReq/viewFexTranReqPayCust";
    }


    /**
     * 外汇交易表详情数据
     *
     * @return
     */
    @PostMapping("/viewJson/{id}")
    @ResponseBody
    public AjaxJson viewJson(@PathVariable("id") String id){
        FexTranReqVO fexTranReq = fexTranReqService.getNewByIdUnion(id);
        Model model = new ExtendedModelMap();
        //设置支付类型
        this.setBankPayType(fexTranReq, model);
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        //单位本位币
        CorpDTO corpDTO = iCorpService.get(fexTranReq.getCorpId());
        model.addAttribute("corpDTO", corpDTO);
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));


        // 自定义模板字段
        getCustFields(fexTranReq.getId(), model);

        //已违约状态，显示违约信息
        if(ForeignConstant.STATUE_DEFAULT.equals(fexTranReq.getStatus())){
            model.addAttribute("opType", ForeignConstant.OP_TYPE_2);
            List<FexTranDealVO> tranDealList = fexTranDealService.listVOByTranReqId(ForeignConstant.OP_TYPE_2, fexTranReq.getId());
            FexTranDealVO vo = new FexTranDealVO();
            for(FexTranDealVO tranDeal:tranDealList){
                if(ForeignConstant.STATUE_CHECK.equals(tranDeal.getStatus())){
                    vo = tranDeal;
                    break;
                }
            }
            model.addAttribute("fexTranDealPage", vo);
        }
        //合并明细
        List<FexTranReqVO> listMerge = fexTranReqService.listByMergeId(id);
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(fexTranReq.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(fexTranReqService.getByIdUnion(fexTranReq.getSplitId()));
            }
        }
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);
        //资金收付交易
        if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            if(EmptyUtils.isNotEmpty(fexTranReq.getCotpId())){
                FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranReq.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
            }
            //获取付款账户
            BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());


            if(payerAcc != null && StringUtils.isNotBlank(payerAcc.getBifCode()) && StringUtils.isBlank(payerAcc.getBifCodeStr())) {
                BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(payerAcc.getBifCode());
                payerAcc.setBifCodeStr(bifInitDTO == null?"":bifInitDTO.getBifCode());
            }
            model.addAttribute("payerAcc", payerAcc);

            if (payerAcc != null && CommonConstant.NO.equals(payerAcc.getIsDomesticBank()) && StringUtils.isNotBlank(payerAcc.getProv())) {
                SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(payerAcc.getProv());
                model.addAttribute("payLocalClearNo", payerAcc.getLocalClearNo());
                model.addAttribute("payBankSwiftCode", payerAcc.getBankCode());
                if (sysCountryCustDTO != null) {
                    model.addAttribute("payerCountryId", sysCountryCustDTO.getId());
                    model.addAttribute("payerCountryNameZh", sysCountryCustDTO.getNameZh());
                    model.addAttribute("payerEnCode", sysCountryCustDTO.getEnCode());
                }
                //境外银行地址
                if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())&& StringUtils.isNotBlank(payerAcc.getBankCode())) {
                    BtSwiftInfoDTO  btSwiftInfoDTO = btSwiftInfoService.getByCode(payerAcc.getBankCode());
                    if (btSwiftInfoDTO != null) {
                        payerAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                        model.addAttribute("bankAdd", payerAcc.getBankAdd());
                    }
                }
            }
            if(ForeignConstant.FEX_PRODUCT_FPS.equals(fexTranReq.getProId()) ||
                    ForeignConstant.FEX_PRODUCT_PAYPAL.equals(fexTranReq.getProId())){
                if(!EmptyUtils.isEmpty(fexTranReq.getCotpCountry())) {
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(fexTranReq.getCotpCountry());
                    model.addAttribute("nameZh", sysCountryCustDTO.getNameEn());
                }
            }
        }

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());

        Map<String, Object> fexTranReqMap = model.asMap();
        return success(fexTranReqMap);
    }


    /**
     * 外汇交易表 复制页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/clone/{id}")
    public String clone(@PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getNewByIdUnion(id);
        model.addAttribute("cloneId", fexTranReq.getId());
        //设置支付类型
        this.setBankPayType(fexTranReq, model);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        //资金收付交易
        if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(fexTranReq.getCorpBank());
            String remitMethod = "0,1";
            if (bankTypeDTO != null && StringUtils.isNotBlank(bankTypeDTO.getRemitMethod())) {
                remitMethod = remitMethod + CommonConstant.COMMA_SPLIT + bankTypeDTO.getRemitMethod();
            }
            model.addAttribute("remitMethod", remitMethod);
            //获取付款账户
            BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());

            if(payerAcc != null && StringUtils.isNotBlank(payerAcc.getBifCode()) && StringUtils.isBlank(payerAcc.getBifCodeStr())) {
                BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(payerAcc.getBifCode());
                payerAcc.setBifCodeStr(bifInitDTO == null?"":bifInitDTO.getBifCode());
            }
            model.addAttribute("payerAcc", payerAcc);

            if (payerAcc != null && CommonConstant.NO.equals(payerAcc.getIsDomesticBank()) && StringUtils.isNotBlank(payerAcc.getProv())) {
                SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(payerAcc.getProv());
                model.addAttribute("payLocalClearNo", payerAcc.getLocalClearNo());
                model.addAttribute("payBankSwiftCode", payerAcc.getBankCode());
                if (sysCountryCustDTO != null) {
                    model.addAttribute("payerCountryId", sysCountryCustDTO.getId());
                    model.addAttribute("payerCountryNameZh", sysCountryCustDTO.getNameZh());
                    model.addAttribute("payerEnCode", sysCountryCustDTO.getEnCode());
                }
                //境外银行地址
                if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())&& StringUtils.isNotBlank(payerAcc.getBankCode())) {
                    BtSwiftInfoDTO  btSwiftInfoDTO = btSwiftInfoService.getByCode(payerAcc.getBankCode());
                    if (btSwiftInfoDTO != null) {
                        payerAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                        model.addAttribute("bankAdd", payerAcc.getBankAdd());
                    }
                }
            }
            if(EmptyUtils.isNotEmpty(fexTranReq.getCotpId())){
                FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranReq.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
            }
        }

        if(StringUtils.isNotBlank(fexTranReq.getMergeId()) || StringUtils.isNotBlank(fexTranReq.getSplitId())){
            model.addAttribute("editPayMoneyFlag", "0");
        }else{
            model.addAttribute("editPayMoneyFlag", "1");
        }


        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        // 自定义模板字段
        getCustFields(fexTranReq.getId(), model);
        CorpDTO corpDTO = iCorpService.get(fexTranReq.getCorpId());
        model.addAttribute("corpDTO", corpDTO);
        //复制标志
        fexTranReq.setCloneFlag(CommonConstant.YES);
        fexTranReq.setId("");
        fexTranReq.setBillNo("");
        fexTranReq.setTranAppDtlId(null);
        fexTranReq.setTranAppDtlNo(null);
        fexTranReq.setAttachmentId("");
        fexTranReq.setBillFrom(ForeignConstant.BILL_FROM_0);
        model.addAttribute("fexTranReqPage", fexTranReq);


        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());


        //获取国家地区-中国
        SysCountryCustDTO china = sysCountryCustService.getByNameZh("中国");
        model.addAttribute("chinaId", china == null ? "":china.getId());
        //交易对手"省/市"与"国家+地区"的显隐口径（跟随交易对手关联客商的境内/境外属性）
        this.setCotpRegionFlag(model, fexTranReq, china == null ? "":china.getId());
        return "foreign/foreignPayReq/updateFexTranReqPayCust";
    }



    /**
     * 批量选择付款账号
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_PAY_BATCHSETPAYERACC)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetPayerAcc")
    @ResponseBody
    public AjaxJson batchSetPayerAcc(FexTranReqVO fexTranReqVO) {
        fexTranReqService.batchSetPayerAcc(fexTranReqVO, this.getCurrentUser(), true);
        return success();
    }

    /**
     * 外汇交易表 详情页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewBillNo/{billNo}")
    public String viewBillNo(@PathVariable("billNo") String billNo, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByBillNoUnion(billNo);
        //设置支付类型
        this.setBankPayType(fexTranReq, model);
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        // 自定义模板字段
        getCustFields(fexTranReq.getId(), model);
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        //已违约状态，显示违约信息
        if(ForeignConstant.STATUE_DEFAULT.equals(fexTranReq.getStatus())){
            model.addAttribute("opType", ForeignConstant.OP_TYPE_2);
            List<FexTranDealVO> tranDealList = fexTranDealService.listVOByTranReqId(ForeignConstant.OP_TYPE_2, fexTranReq.getId());
            FexTranDealVO vo = new FexTranDealVO();
            for(FexTranDealVO tranDeal:tranDealList){
                if(ForeignConstant.STATUE_CHECK.equals(tranDeal.getStatus())){
                    vo = tranDeal;
                    break;
                }
            }
            model.addAttribute("fexTranDealPage", vo);
        }
        //合并明细
        List<FexTranReqVO> listMerge = fexTranReqService.listByMergeId(fexTranReq.getId());
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(fexTranReq.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(fexTranReqService.getByIdUnion(fexTranReq.getSplitId()));
            }
        }
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);



        getCustFields(fexTranReq.getId(), model);
        return "foreign/foreignPayReq/viewFexTranReqPayCust";


        //return "foreign/viewFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
    }

    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BUDGETSEL).getValue());
        model.addAttribute("payItem", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_PAYITEM).getValue());
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        String rateTypeId = "";
        if(profitAndLossConfig!=null && !StringUtils.isBlank(profitAndLossConfig.getValue())){
            rateTypeId = profitAndLossConfig.getValue();
        }
        model.addAttribute("rateTypeId", rateTypeId);
    }


    /**
     * 设置支付类型
     * @param fexTranReq
     * @param model
     */
    private void setBankPayType(FexTranReqVO fexTranReq, Model model) {
        if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            return;
        }
        BankTypeDTO bankTypeDTO = new BankTypeDTO();
        // 支付类型
        BankTypeCustomDTO bankPayTypeDTO = new BankTypeCustomDTO();
        // 收款人类型
        BankTypeCustomDTO payeeTypeDTO = new BankTypeCustomDTO();
        // 受益人关联关系
        BankTypeCustomDTO bnfRelateDTO = new BankTypeCustomDTO();
        // 业务模式编号
        //BankTypeCustomDTO subModeDTO = new BankTypeCustomDTO();
        //其他信息模块
        BankTypeOtherCustomDTO otherCustomDTO = new BankTypeOtherCustomDTO();
        String bankAcc = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType()) || ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            bankAcc = fexTranReq.getCorpBankAcc();
        } else if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
            bankAcc = fexTranReq.getSrcBankAcc();
        } else {
            return;
        }
        BtBankAccDTO payerAcc = btBankAccService.getBankAccInfo(bankAcc);
        String countryId = BdlinkConstant.COUNTRY_CODE_CHINA;
        if (payerAcc != null) {
            bankTypeDTO = bankTypeService.getObjectById(payerAcc.getBankTypeId());
            // 支付类型
            if (StringUtils.isNotBlank(fexTranReq.getBankPayType())) {
                bankPayTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getBankPayType(), BankConstant.CUSTOM_TYPE_BANKPAYTYPE);
            }
            // 收款人类型
            if (StringUtils.isNotBlank(fexTranReq.getPayeeType())) {
                payeeTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getPayeeType(), BankConstant.CUSTOM_TYPE_PAYEETYPE);
            }
            // 受益人关联关系
            if (StringUtils.isNotBlank(fexTranReq.getBnfRelate())) {
                bnfRelateDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getBnfRelate(), BankConstant.CUSTOM_TYPE_BNFRELATE);
            }
            // 业务模式编号
            /*if (StringUtils.isNotBlank(fexTranReq.getSubMode())) {
                subModeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getBnfRelate(), BankConstant.CUSTOM_TYPE_SUBMODE);
            }*/
            //适用页面 1-外汇买卖交易 2-资金调拨交易 3-资金收付交易 4-外币汇款申请 5-付款办理
            String applayScope = "";
            if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
                applayScope = BankConstant.APPLAY_SCOPE_1;
            } else if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
                applayScope = BankConstant.APPLAY_SCOPE_2;
            } else if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
                applayScope = BankConstant.APPLAY_SCOPE_3;
            }
            if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())) {
                countryId = payerAcc.getProv();
            }
            //其他信息模块
            otherCustomDTO = bankTypeService.getByBankTypeOtherCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), applayScope, countryId, fexTranReq.getRemitType());
        }
        model.addAttribute("countryId", countryId);
        model.addAttribute("payerAcc", payerAcc);
        model.addAttribute("bankType", bankTypeDTO);
        model.addAttribute("bankPayType", bankPayTypeDTO);
        model.addAttribute("payeeType", payeeTypeDTO);
        model.addAttribute("bnfRelate", bnfRelateDTO);
        model.addAttribute("otherCustom", otherCustomDTO);

        if(null != fexTranReq && StringUtils.isNotEmpty(fexTranReq.getTemplateId())){
            FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(fexTranReq.getTemplateId());
            model.addAttribute("fexTranTemplateConfig", fexTranTemplateConfig);
        }

    }

    /**
     * 跳转至审核列表
     * @param tranType	交易类型
     * @return
     */
    @GetMapping("/listApprove/{tranType}")
    public String listApprove(@PathVariable String tranType, String flowStatus, String cycleTime, Model model){
        model.addAttribute("tranType", tranType);
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/foreignPayReq/listFexTranReqPayApprove";
    }

    /**
     * 跳转至审核详情界面
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        FexTranReqVO vo = new FexTranReqVO();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.fexTranReqService.getByIdUnion(id);
            //设置支付类型
            this.setBankPayType(vo, model);
            model.addAttribute("fexTranReqPage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
            model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            //交易员
            model.addAttribute("dealer", userService.getByLoginName(vo.getCreateBy()).getUsername());
            //初始化参数
            this.initConfig(model);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
            // 自定义模板字段
            getCustFields(vo.getId(), model);
        }
        return "foreign/foreignPayReq/approveFexTranReq";
    }

    /**
     * 付款通知表拆分页面跳转
     *
     * @return
     */
    ////RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT)
    @GetMapping("/split/{id}")
    public String split(@PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(id);
        //暂存和打回状态才可以进行拆分
        if (fexTranReq != null && !NetbankConstant.STATUE_TEMP.equals(fexTranReq.getStatus()) && !NetbankConstant.STATUE_BACK.equals(fexTranReq.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, fexTranReq.getBillNo());
        }
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        // 自定义模板字段
        getCustFields(fexTranReq.getId(), model);
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        String showTranAmt = CommonConstant.NO;
        if (!ForeignConstant.REMIT_TYPE_2.equals(fexTranReq.getRemitType())
                && (fexTranReq.getTranAmt1() != null || fexTranReq.getTranAmt2() != null)) {
            showTranAmt = CommonConstant.YES;
        }
        model.addAttribute("showTranAmt", showTranAmt);
        return "foreign/foreignPayReq/splitFexTranReqPay";
    }

    /**
     *
     * 获取自定义信息
     *
     * @param reqId
     * @param model
     */
    private void getCustFields(String reqId, Model model){
        List<FexTranCustInfoVO> fexTranCustInfoVOS = fexTranCustInfoService.listCustInfoByBusId(reqId);
        if (CollectionUtil.isNotEmpty(fexTranCustInfoVOS)){
            Map<String, String> custFields = new HashMap<>();
            for (FexTranCustInfoVO fexTranCustInfoVO : fexTranCustInfoVOS){
                custFields.put(fexTranCustInfoVO.getFieldCode(), fexTranCustInfoVO.getFieldVal());
            }
            model.addAttribute("templateCustFields", JSONUtil.toJsonStr(custFields));
        }

        // 自定义信息
        model.addAttribute("custFields", fexCustInfoTenantService.listCustInfoByBusId(reqId));
    }
}
