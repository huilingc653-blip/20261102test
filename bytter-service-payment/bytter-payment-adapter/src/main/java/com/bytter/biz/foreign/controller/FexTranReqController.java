package com.bytter.biz.foreign.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCotpService;
import com.bytter.biz.foreign.service.FexCustFieldRuleService;
import com.bytter.biz.foreign.service.FexCustFieldRuleTenantService;
import com.bytter.biz.foreign.service.FexCustInfoTenantService;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.service.FexTranConfigTemplateService;
import com.bytter.biz.foreign.service.FexTranCustInfoService;
import com.bytter.biz.foreign.service.FexTranDealService;
import com.bytter.biz.foreign.service.FexTranOptionDealService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.utils.FexTranReqUtil;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.biz.foreign.vo.FexTranOptionDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.biz.limit.dto.PaymentLimitBatchDetailDto;
import com.bytter.biz.limit.exception.PaymentLimitBatchExceededException;
import com.bytter.biz.limit.exception.PaymentLimitExceededException;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisForeignExcService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ForeignMessageEnum;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.AccountGrantConstant;
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
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Title: Controller
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fextranreq")
public class FexTranReqController extends BaseController {

    @Autowired
    private FexTranReqService fexTranReqService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private IUserService userService;

    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;

    @Autowired
    private FexCustFieldRuleTenantService fexCustFieldRuleTenantService;

    @Autowired
    private FexCustInfoTenantService fexCustInfoTenantService;

    @Autowired
    private FexTranDealService fexTranDealService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Autowired
    private FexProductService fexProductService;

    @DubboReference(check = false)
    private ICalendarService calendarService;

    @Autowired
    private FexTranOptionDealService fexTranOptionDealService;

    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;

    @DubboReference(check = false)
    private IBisForeignExcService bisForeignExcService;
    @DubboReference(check = false)
    private IInputBankInfoService inputBankInfoService;
    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;
    @Autowired
    private FexCotpService fexCotpService;
    @DubboReference(check = false)
    private ICorpService iCorpService;

    @Autowired(required = false)
    private  FexTranConfigTemplateService fexTranConfigTemplateService;


    @Autowired
    private FexTranCustInfoService fexTranCustInfoService;



    /**
     * 外汇交易表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询交易管理列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{tranType}")
    public String list(@PathVariable("tranType") String tranType, Model model) {
        model.addAttribute("tranType", tranType);
        return "foreign/listFexTranReq" + FexTranReqUtil.getForwardByTranType(tranType);
    }

    /**
     * 外汇交易表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexTranReqService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 外汇交易表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询交易管理列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/query/{tranType}")
    public String query(@PathVariable("tranType") String tranType, Model model) {
        model.addAttribute("tranType", tranType);
        return "foreign/listFexTranReq" + FexTranReqUtil.getForwardByTranType(tranType) + "Query";
    }

    /**
     * 外汇交易表 新增页面跳转
     *
     * @return
     */
    @GetMapping("/add/{tranType}")
    public String add(@PathVariable("tranType") String tranType, FexTranReq fexTranReq, Model model) {
        if (StringUtils.isNotEmpty(fexTranReq.getId())) {
            fexTranReq = fexTranReqService.get(fexTranReq.getId());
            model.addAttribute("fexTranReqPage", fexTranReq);
        }
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", tranType);
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //初始化参数
        this.initConfig(model);
        //自定义字段
        this.setCustFields(model, tranType);
        //获取国家地区-中国
        SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        model.addAttribute("chinaId", china == null ? "":china.getId());

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());
        return "foreign/addFexTranReq" + FexTranReqUtil.getForwardByTranType(tranType);
    }

    /**
     * 资金收付交易 香港FPS产品 新增页面
     *
     * @return
     */
    @GetMapping("/addHK")
    public String addHK(Model model) {
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", "4");
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //初始化参数
        this.initConfig(model);
        //自定义字段
        this.setCustFields(model, "4");
        return "foreign/addFexTranReqPayHKProduct";
    }

    /**
     * 资金收付交易 香港FPS产品 新增页面
     *
     * @return
     */
    @GetMapping("/addPaypal")
    public String addPaypal(Model model) {
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", "4");
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //初始化参数
        this.initConfig(model);
        //自定义字段
        this.setCustFields(model, "4");
        return "foreign/addFexTranReqPayPaypalProduct";
    }

    /**
     *
     * 收付交易（动态配置）
     *
     * @param model
     * @return
     */
    @GetMapping("/addPayCust")
    public String addPay(Model model) {
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", "4");
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //初始化参数
        this.initConfig(model);
        return "foreign/foreignPayReq/addFexTranReqPayCust";
    }

    /**
     * 外汇交易表-自定义信息 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addCustInfo/{tranType}")
    public String addCustInfo(@PathVariable("tranType") String tranType, Model model) {
        //自定义字段
        this.setCustFields(model, tranType);
        return "foreign/addFexTranCustInfo";
    }

    private void setCustFields(Model model, String tranType) {
        //model.addAttribute("custFields", fexCustFieldRuleService.listByAppRange(CommonConstant.YES, tranType));
        model.addAttribute("custFields", fexCustFieldRuleTenantService.listByAppRange(CommonConstant.YES, tranType));
    }

    /**
     * 外汇交易表-自定义信息 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addTranReqForeign/{swapType}")
    public String addTranReqForeign(@PathVariable("swapType") String swapType, Model model) {
        /**近/远端  1-近端，2-远端*/
        model.addAttribute("swapType", swapType);
        model.addAttribute("tranType", ForeignConstant.TRAN_TYPE_1);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());
        return "foreign/addFexTranReqForeignCommon";
    }

    /**
     * 设置支付类型
     * @param fexTranReq
     * @param model
     */
    public void setBankPayType(FexTranReqVO fexTranReq, Model model) {
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
        //model.addAttribute("subMode", subModeDTO);
        model.addAttribute("otherCustom", otherCustomDTO);
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
            model.addAttribute("fexTranReqPage", fexTranReq);
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
        }
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        getCustFields(fexTranReq.getId(), model);
        model.addAttribute("custInfoBusId", fexTranReq.getId());
        //获取国家地区-中国
        SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
        model.addAttribute("chinaId", china == null ? "":china.getId());

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());

        //如果产品选择香港FPS产品，则跳到新页面
        if(!EmptyUtils.isEmpty(fexTranReq) && ForeignConstant.FEX_PRODUCT_FPS.equals(fexTranReq.getProId())){
            return "foreign/updateFexTranReqPayHKProduct";
        }
        //如果产品选择Paypal产品，则跳到新页面
        if(!EmptyUtils.isEmpty(fexTranReq) && ForeignConstant.FEX_PRODUCT_PAYPAL.equals(fexTranReq.getProId())){
            return "foreign/updateFexTranReqPayPaypalProduct";
        }
        return "foreign/updateFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
    }

    /**
     * 外汇交易表-自定义信息 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/updateCustInfo/{id}")
    public String updateCustInfo(@PathVariable("id") String id, Model model) {
        getCustFields(id, model);
        return "foreign/updateFexTranCustInfo";
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
     * 外汇交易表 详情页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getNewByIdUnion(id);


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

        // 自定义模板交易详情页
        if (StringUtils.isNotEmpty(fexTranReq.getTemplateId())){
            model.addAttribute("fexTranReqPage", fexTranReq);

            // 自定义信息
            getCustFields(fexTranReq.getId(), model);
            return "foreign/foreignPayReq/viewFexTranReqPayCust";
        } else if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())
                && (StringUtils.isNotEmpty(fexTranReq.getTranAppDtlId()) || StringUtils.isNotEmpty(fexTranReq.getVersionNo()))){
            model.addAttribute("fexTranReqPage", fexTranReq);
            // 无模板，默认显示页面
            return "foreign/foreignPayReq/viewDefFexTranReqPayCust";
        }

        //如果产品选择香港FPS产品，则跳到新页面
        if(!EmptyUtils.isEmpty(fexTranReq) && ForeignConstant.FEX_PRODUCT_FPS.equals(fexTranReq.getProId())){
            return "foreign/viewFexTranReqPayHKProduct";
        }
        //如果产品选择Paypal产品，则跳到新页面
        if(!EmptyUtils.isEmpty(fexTranReq) && ForeignConstant.FEX_PRODUCT_PAYPAL.equals(fexTranReq.getProId())){
            return "foreign/viewFexTranReqPayPaypalProduct";
        }
        return "foreign/viewFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
    }

    /**
     * 外汇交易表-自定义信息查看 详情页
     *
     * @return
     */
    @GetMapping("/viewCustInfo/{id}")
    public String viewCustInfo(@PathVariable("id") String id, Model model) {
        getCustFields(id, model);
        return "foreign/viewFexTranCustInfo";
    }

    /**
     * 获取保证金账号
     *
     * @param corpId
     * @param curId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBoodBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBoodBankAcc(String corpId, String curId, String bankTypeId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();

        if (!StringUtils.isEmpty(corpId)) {
            BtBankAccDTO bankAcc = new BtBankAccDTO();
            if (StringUtils.isNotBlank(curId)) {
                bankAcc.setCurId(curId);
            }
            bankAcc.setCorpId(corpId);
            bankAcc.setStatus(BtBankAccConstant.STATUE_CHECK);
            bankAcc.setBankTypeId(bankTypeId);
            listAcc = btBankAccService.getBankAccListAll(bankAcc);
        }
        // 境内账户获取SwiftCode
        if (EmptyUtils.isNotEmpty(listAcc)) {
            List<String> bankCodes = new ArrayList<>();
            for (BtBankAccDTO bankAcc:listAcc) {
                if (CommonConstant.YES.equals(bankAcc.getIsDomesticBank()) && StringUtils.isNotBlank(bankAcc.getBankCode())) {
                    bankCodes.add(bankAcc.getBankCode());
                }
            }
            if (EmptyUtils.isNotEmpty(bankCodes)) {
                Map<String, InputBankInfoDTO> bankInfoMap = inputBankInfoService.getBankCodeInfoByBankCodes(bankCodes);
                for (BtBankAccDTO bankAcc:listAcc) {
                    if (CommonConstant.YES.equals(bankAcc.getIsDomesticBank())) {
                        InputBankInfoDTO bankInfoDTO = bankInfoMap.get(bankAcc.getBankCode());
                        bankAcc.setSwiftCode(bankInfoDTO == null ? "":bankInfoDTO.getSwiftCode());
                    } else {
                        // 境外账户的swiftCode同开户行行号
                        bankAcc.setSwiftCode(bankAcc.getBankCode());
                    }
                }
            }
        }

        return listAcc;
    }

    /**
     * 获取源账户信息
     *
     * @param curId
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getSrcBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getSrcBankAcc(String curId, String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        //V20080015&V20080014-华强股份-不限制币种
        //if (!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(curId)) {
        if (!StringUtils.isEmpty(corpId)){
            List<String> curList = new ArrayList<>();
            //curList.add(curId);
            //获取转账权限的银行账号
            listAcc = accountGrantService.listDTOByMultiVal(this.getUserId(), corpId,
                    null, curList, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
        }
        return listAcc;
    }

    /**
     * 获取目标账户信息
     *
     * @param curId
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getTargetBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getTargetBankAcc(String curId, String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        //V20080015&V20080014-华强股份-不限制币种
        //if (!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(curId)) {
        if (!StringUtils.isEmpty(corpId)){
            BtBankAccDTO bankAcc = new BtBankAccDTO();
            //bankAcc.setCurId(curId);
            bankAcc.setCorpId(corpId);
            bankAcc.setStatus(BtBankAccConstant.STATUE_CHECK);
            listAcc = btBankAccService.getBankAccListAll(bankAcc);
        }
        return listAcc;
    }

    /**
     * 获取主体单位银行账号
     *
     * @param curId
     * @param bankType
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getCorpBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getCorpBankAcc(String corpId, String curId, String bankType, String countryId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        if (!StringUtils.isEmpty(corpId)) {
            //获取转账权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTOByMultiVal(this.getUserId(), corpId,
                    null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);

            HashMap<String, BisBifInitDTO> bifCodeMap = new HashMap<>();
            if(StringUtils.isNotBlank(bankType) && !EmptyUtils.isEmptyList(listBankAccTemp)){
                for(BtBankAccDTO bankAcc:listBankAccTemp){
                    if(StringUtils.isNotBlank(bankAcc.getBifCode())) {
                        BisBifInitDTO bifInitDTO = bifCodeMap.get(bankAcc.getBifCode());
                        if (bifInitDTO == null) {
                            bifInitDTO = bisBifInitService.getObjectById(bankAcc.getBifCode());
                            bifCodeMap.put(bankAcc.getBifCode(), bifInitDTO);
                        }
                        if(bifInitDTO != null){
                            bankAcc.setBifCodeStr(bifInitDTO.getBifCode());
                        }
                    }
                    //根据银行类别过滤
                    if(bankType.equals(bankAcc.getBankTypeId())){
                        // 境内
                        if (CommonConstant.YES.equals(bankAcc.getIsDomesticBank())){
                            SysCountryCustDTO countryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
                            bankAcc.setCountryId(countryCustDTO.getId());
                        }
                        //境外银行获取国家地区名称
                        if (CommonConstant.NO.equals(bankAcc.getIsDomesticBank()) && StringUtils.isNotBlank(bankAcc.getProv())) {
                            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(bankAcc.getProv());
                            if (sysCountryCustDTO != null) {
                                bankAcc.setProvName(sysCountryCustDTO.getNameZh());
                                //简称
                                bankAcc.setCountryEnCode(sysCountryCustDTO.getEnCode());
                                bankAcc.setCountryId(sysCountryCustDTO.getId());
                            }
                        }
                        //境外银行地址
                        if (CommonConstant.NO.equals(bankAcc.getIsDomesticBank())&& StringUtils.isNotBlank(bankAcc.getBankCode())) {
                            BtSwiftInfoDTO  btSwiftInfoDTO = btSwiftInfoService.getByCode(bankAcc.getBankCode());
                            if (btSwiftInfoDTO != null) {
                                bankAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                                if (StringUtils.isNotEmpty(btSwiftInfoDTO.getCountryCode())){
                                    bankAcc.setCountryId(btSwiftInfoDTO.getCountryCode());
                                }
                            }
                        }
                        listAcc.add(bankAcc);
                    }
                }
            }else{
                //先选账号情况下没有银行国家代码
                for(BtBankAccDTO bankAcc:listBankAccTemp){
                    if(StringUtils.isNotBlank(bankAcc.getBifCode())) {
                        BisBifInitDTO bifInitDTO = bifCodeMap.get(bankAcc.getBifCode());
                        if (bifInitDTO == null) {
                            bifInitDTO = bisBifInitService.getObjectById(bankAcc.getBifCode());
                            bifCodeMap.put(bankAcc.getBifCode(), bifInitDTO);
                        }
                        if(bifInitDTO != null){
                            bankAcc.setBifCodeStr(bifInitDTO.getBifCode());
                        }
                    }
                    // 境内
                    if (CommonConstant.YES.equals(bankAcc.getIsDomesticBank())){
                        SysCountryCustDTO countryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
                        bankAcc.setCountryId(countryCustDTO.getId());
                    }
                    //境外银行获取国家地区名称
                    if (CommonConstant.NO.equals(bankAcc.getIsDomesticBank()) && StringUtils.isNotBlank(bankAcc.getProv())) {
                        SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(bankAcc.getProv());
                        if (sysCountryCustDTO != null) {
                            bankAcc.setProvName(sysCountryCustDTO.getNameZh());
                            //简称
                            bankAcc.setCountryEnCode(sysCountryCustDTO.getEnCode());
                            bankAcc.setCountryId(sysCountryCustDTO.getId());
                        }
                    }
                    //境外银行地址
                    if (CommonConstant.NO.equals(bankAcc.getIsDomesticBank())&& StringUtils.isNotBlank(bankAcc.getBankCode())) {
                        BtSwiftInfoDTO  btSwiftInfoDTO = btSwiftInfoService.getByCode(bankAcc.getBankCode());
                        if (btSwiftInfoDTO != null) {
                            bankAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                            if (StringUtils.isNotEmpty(btSwiftInfoDTO.getCountryCode())){
                                bankAcc.setCountryId(btSwiftInfoDTO.getCountryCode());
                            }
                        }
                    }
                }
                listAcc = BeanUtils.copyListProperties(listBankAccTemp, BtBankAccDTO.class);
            }
        }

        // 过滤国家
        List<BtBankAccDTO> corpAccList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(listAcc) && StringUtils.isNotEmpty(countryId)){
            corpAccList = listAcc.stream()
                    .filter(Objects::nonNull)
                    //.filter(dto -> Objects.equals(countryId, dto.getCountryId()))
                    .collect(Collectors.toList());
        } else {
            corpAccList = listAcc;
        }
        return corpAccList;
    }

    /**
     * 获取费用支出账户信息
     *
     * @param bankType
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getCostAcc")
    @ResponseBody
    public List<BtBankAccDTO> getCostAcc(String bankType, String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        if (!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(bankType)) {
            BtBankAccDTO bankAccSearchVO = new BtBankAccDTO();
            bankAccSearchVO.setCorpId(corpId);
            bankAccSearchVO.setStatus(BtBankAccConstant.STATUE_CHECK);
            List<BtBankAccDTO> listBankAccTemp = btBankAccService.getBankAccListAll(bankAccSearchVO);
            if(StringUtils.isNotBlank(bankType)){
                for(BtBankAccDTO bankAcc:listBankAccTemp){
                    //根据银行类别过滤
                    if(bankType.equals(bankAcc.getBankTypeId())){
                        listAcc.add(bankAcc);
                    }
                }
            }else{
                listAcc = BeanUtils.copyListProperties(listBankAccTemp, BtBankAccDTO.class);
            }
        }
        return listAcc;
    }



    /**
     * 添加交易信息
     * @param fexTranReq
     * @return
     */
    @LogMessage(content = "添加交易表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexTranReqVO fexTranReq){
        setTemplateName(fexTranReq);
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            if(this.checkWorkDate(fexTranReq.getSettleDate1(), fexTranReq.getBuyCurId1(), fexTranReq.getTranAddrId1(),fexTranReq.getProId1())
                    && this.checkWorkDate(fexTranReq.getSettleDate1(), fexTranReq.getSellCurId1(), fexTranReq.getTranAddrId1(),fexTranReq.getProId1())) {
                if(CommonConstant.YES.equals(fexTranReq.getIsSwapTran())){
                    //掉期交易还需校验远端交易
                    if(this.checkWorkDate(fexTranReq.getSettleDate2(), fexTranReq.getBuyCurId2(), fexTranReq.getTranAddrId2(),fexTranReq.getProId2())
                            && this.checkWorkDate(fexTranReq.getSettleDate2(), fexTranReq.getSellCurId2(), fexTranReq.getTranAddrId2(),fexTranReq.getProId2())) {
                        fexTranReqService.saveFexTranReqForeign(fexTranReq, this.getCurrentUser());
                    }
                }else {
                    fexTranReqService.saveFexTranReqForeign(fexTranReq, this.getCurrentUser());
                }
            }
        } else {
            if(this.checkWorkDate(fexTranReq.getSettleDate(), fexTranReq.getCurId(), fexTranReq.getTranAddrId(),fexTranReq.getProId())) {
                fexTranReqService.saveFexTranReq(fexTranReq, this.getCurrentUser());
            }
        }
        return success();
    }
    /**
     * 批量提交交易
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量提交交易", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        List<FexTranReqVO> fexTranReqList = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            fexTranReqList.add(fexTranReqService.getNewByIdUnion(id));
        }
        // 支付限额按交易号码升序逐笔校验并占用，避免页面勾选顺序影响批量提交结果。
        fexTranReqList.sort(Comparator.comparing(FexTranReqVO::getBillNo,
                Comparator.nullsLast(String::compareTo)));
        List<LimitHitDetailDto> limitHitDetails = new ArrayList<>();
        for (FexTranReqVO fexTranReq : fexTranReqList) {
            try {
                if (this.checkWorkDate(fexTranReq.getSettleDate(), fexTranReq.getCurId(), fexTranReq.getTranAddrId(), fexTranReq.getProId())
                        && this.checkData(fexTranReq)) {
                    fexTranReq.setStatus(CommonConstant.STATUE_WAIT_CHECK);
                    fexTranReqService.updateFexTranReqSubmit(fexTranReq, this.getCurrentUser());
                }
            } catch (PaymentLimitExceededException exception) {
                if (exception.getDetail() != null) {
                    limitHitDetails.add(PaymentLimitBatchDetailDto.of(
                            "table.fexTranReq.billNo", fexTranReq.getBillNo(), exception.getDetail()));
                }
            }
        }
        if (!limitHitDetails.isEmpty()) {
            throw new PaymentLimitBatchExceededException(limitHitDetails);
        }
        return success();
    }


    /**
     * 修改交易表
     * @param fexTranReq
     * @return
     */
    @LogMessage(content = "修改交易表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexTranReqVO fexTranReq){
        setTemplateName(fexTranReq);
        //复制
        if(CommonConstant.YES.equals(fexTranReq.getCloneFlag()) && StringUtils.isBlank(fexTranReq.getId())){
            if(this.checkWorkDate(fexTranReq.getSettleDate(), fexTranReq.getCurId(), fexTranReq.getTranAddrId(),fexTranReq.getProId())) {
                fexTranReqService.saveFexTranReq(fexTranReq, this.getCurrentUser());
            }
        }else {
            if (this.checkWorkDate(fexTranReq.getSettleDate(), fexTranReq.getCurId(), fexTranReq.getTranAddrId(), fexTranReq.getProId())) {
                fexTranReqService.updateFexTranReq(fexTranReq, this.getCurrentUser());
            }
        }
        return  success();
    }

    /**
     * 删除交易表
     * @param id
     * @return
     */
    @LogMessage(content = "删除交易表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        //状态校验
        FexTranReq entity = fexTranReqService.get(id);
        if (entity != null && !CommonConstant.STATUE_TEMP.equals(entity.getStatus()) && !CommonConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        fexTranReqService.delFexTranReq(id, null, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除交易表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            FexTranReq entity = fexTranReqService.get(id, true);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        fexTranReqService.batchDelFexTranReq(ids, this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "删除交易表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/deleteContianMsg")
    @ResponseBody
    public AjaxJson deleteContianMsg(@RequestParam("id") String id, @RequestParam("deleteMsg") String deleteMsg) {
        //状态校验
        FexTranReq entity = fexTranReqService.get(id);
        if (entity != null && !CommonConstant.STATUE_TEMP.equals(entity.getStatus()) && !CommonConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        fexTranReqService.delFexTranReq(id, deleteMsg, this.getCurrentUser());
        return success();
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
        return "foreign/listFexTranReq" + FexTranReqUtil.getForwardByTranType(tranType) + "Approve";
    }

    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput){
        return this.fexTranReqService.dataTableApprove(dataTablesInput);
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
            getCustFields(vo.getId(), model);
        }
        return "foreign/approveFexTranReq";
    }


    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, Model model) {
        FexTranReqVO vo = this.fexTranReqService.getByIdUnion(businessId);
        model.addAttribute("fexTranReqPage", vo);
        model.addAttribute("dealer", userService.getByLoginName(vo.getCreateBy()).getUsername());
        return "foreign/mobileViewFexTranReq" + FexTranReqUtil.getForwardByTranType(vo.getTranType());
    }

    /**
     * 清空交易组信息
     * @param id
     * @return
     */
    @LogMessage(content = "取消交易组信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/updateTranGroup/{id}")
    @ResponseBody
    public AjaxJson updateTranGroup(@PathVariable("id") String id){
        FexTranReq fexTranReq = this.fexTranReqService.get(id, true);
        //清空交易组信息
        fexTranReq.setTranGroup(null);
        fexTranReq.setUpdateBy(this.getCurrentUser().getLoginName());
        fexTranReq.setUpdateDate(DateUtils.getDate());
        fexTranReqService.saveOrUpdate(fexTranReq);
        return success();
    }

    /**
     * 外汇交易处理表 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addFexTranDeal/{opType}/{tranReqId}")
    public String addFexTranDeal(@PathVariable("opType") String opType, @PathVariable("tranReqId") String tranReqId, Model model) {
        List<FexTranDealVO> list = fexTranDealService.listVOByTranReqId(opType, tranReqId);
        //获取交易信息
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(tranReqId);
        //获取产品信息
        FexProductVO fexProduct = fexProductService.getByIdUnion(fexTranReq.getProId());
        if(ForeignConstant.OP_TYPE_1.equals(opType)){
            //暂存/打回的结算金额
            BigDecimal tempSettleMoney = BigDecimal.ZERO;
            Date oldSettleDate = fexTranReq.getSettleDate();
            FexTranDealVO fexTranDeal = null;
            if(EmptyUtils.isEmpty(list)){
                fexTranDeal = new FexTranDealVO();
                fexTranDeal.setTranReqId(tranReqId);
                fexTranDeal.setCorpId(fexTranReq.getCorpId());
                fexTranDeal.setOpType(opType);
                fexTranDeal.setStatus(CommonConstant.STATUE_TEMP);
                fexTranDeal.setOldSettleDate(fexTranReq.getSettleDate());
                //交割方向为固定金额
                fexTranDeal.setDeliverySign(fexTranReq.getFixedMoney());
                //固定金额  1-买入，2-卖出
                if(CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
                    fexTranDeal.setBal(fexTranReq.getBuyMoney());
                }else{
                    fexTranDeal.setBal(fexTranReq.getSellMoney());
                }
                list.add(fexTranDeal);
            }else{
                boolean isAllApprove = true;
                for(FexTranDealVO vo:list){
                    //固定金额和交割方向不一致，跳过
                    if(!fexTranReq.getFixedMoney().equals(vo.getDeliverySign())){
                        continue;
                    }
                    //固定金额  1-买入，2-卖出
                    if(CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
                        fexTranReq.setBuyMoney(fexTranReq.getBuyMoney().subtract(vo.getSettleMoney()));
                    }else{
                        fexTranReq.setSellMoney(fexTranReq.getSellMoney().subtract(vo.getSettleMoney()));
                    }
                    if(vo.getStatus()> CommonConstant.STATUE_TEMP) {
                        oldSettleDate = vo.getNewSettleDate();
                    }
                    //判断产品是否允许多次交割
                    if(CommonConstant.YES.equals(fexProduct.getIsAllowedDelivery())){
                        //判断是否有未审批通过的数据
                        if(vo.getStatus()< CommonConstant.STATUE_CHECK){
                            isAllApprove = false;
                            tempSettleMoney = tempSettleMoney.add(vo.getSettleMoney());
                        }
                    }else{
                        isAllApprove = false;
                    }
                }
                //全都审批完毕
                if(isAllApprove){
                    //初始化一笔新的交割信息
                    fexTranDeal = new FexTranDealVO();
                    fexTranDeal.setTranReqId(tranReqId);
                    fexTranDeal.setCorpId(fexTranReq.getCorpId());
                    fexTranDeal.setOpType(opType);
                    fexTranDeal.setStatus(CommonConstant.STATUE_TEMP);
                    //交割方向为固定金额
                    fexTranDeal.setDeliverySign(fexTranReq.getFixedMoney());
                    list.add(fexTranDeal);
                }else{
                    //获取最后一笔数据
                    fexTranDeal = list.get(list.size()-1);
                }
            }
            //固定金额  1-买入，2-卖出
            if(CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
                fexTranDeal.setBal(fexTranReq.getBuyMoney());
            }else{
                fexTranDeal.setBal(fexTranReq.getSellMoney());
            }
            fexTranDeal.setOldSettleDate(oldSettleDate);
            model.addAttribute("tranBal", fexTranDeal.getBal().add(tempSettleMoney));
            model.addAttribute("fexTranDealPage", fexTranDeal);
            model.addAttribute("list", list);
            model.addAttribute("opType", opType);
            model.addAttribute("dataSize", list.size());
        }else{
            FexTranDealVO fexTranDeal = new FexTranDealVO();
            if(!EmptyUtils.isEmpty(list)){
                fexTranDeal = list.get(0);
                //交易员
                model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
            }else{
                fexTranDeal.setTranReqId(tranReqId);
                fexTranDeal.setCorpId(fexTranReq.getCorpId());
                if(ForeignConstant.OP_TYPE_2.equals(opType)){
                    fexTranDeal.setCurId(fexTranReq.getSellCurId());
                    fexTranDeal.setEnglishCode(fexTranReq.getSellEnglishCode());
                    fexTranDeal.setCurName(fexTranReq.getSellCurName());
                }else if(ForeignConstant.OP_TYPE_3.equals(opType)){
                    //无本金交割取交易的交割币种
                    fexTranDeal.setCurId(fexTranReq.getDeliveryCurId());
                    fexTranDeal.setEnglishCode(fexTranReq.getDeliveryEnglishCode());
                }else if(ForeignConstant.OP_TYPE_4.equals(opType)){
                    if(ForeignConstant.FIXED_MONEY_1.equals(fexTranReq.getFixedMoney())){
                        fexTranDeal.setDeliverySign(ForeignConstant.DELIVERY_SIGN_2);
                        fexTranDeal.setCurId(fexTranReq.getSellCurId());
                    }else if(ForeignConstant.FIXED_MONEY_2.equals(fexTranReq.getFixedMoney())){
                        fexTranDeal.setDeliverySign(ForeignConstant.DELIVERY_SIGN_1);
                        fexTranDeal.setCurId(fexTranReq.getBuyCurId());
                    }
                    fexTranDeal.setNewSettleDate(fexTranReq.getSettleDate());
                }
                fexTranDeal.setOpType(opType);
                fexTranDeal.setStatus(CommonConstant.STATUE_TEMP);
                fexTranDeal.setTranAddrId(fexTranReq.getTranAddrId());
                fexTranDeal.setTranAddr(fexTranReq.getTranAddr());
                model.addAttribute("fexTranDealPage", fexTranDeal);
                //交易员
                model.addAttribute("dealer", this.getCurrentUser().getUsername());
            }
            model.addAttribute("fexTranDealPage", fexTranDeal);
        }
        if(ForeignConstant.OP_TYPE_4.equals(opType)) {
            //初始化参数
            this.initConfig(model);
            //外汇买卖交易：即期、购汇、本金交割，固定金额为1-买入金额，非掉期交易
            if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                    && ForeignConstant.FEX_BUS_TYPE_2.equals(fexTranReq.getFexBusType())
                    && CommonConstant.NO.equals(fexTranReq.getDeliveryType())
                    && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                List<FexCashFlowVO> listFexCashFlow = fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId());
                BisForeignExcDTO bisForeignExcDTO = null;
                for (FexCashFlowVO vo:listFexCashFlow) {
                    bisForeignExcDTO = bisForeignExcService.getByCbsBillId(vo.getId());
                    if (bisForeignExcDTO != null && StringUtils.isNotBlank(bisForeignExcDTO.getVoucherNo())) {
                        break;
                    }
                }
                if (bisForeignExcDTO != null) {
                    FexTranDealVO fexTranDeal = new FexTranDealVO();
                    fexTranDeal.setStatus(CommonConstant.STATUE_TEMP);
                    fexTranDeal.setDeliveryRate(bisForeignExcDTO.getActualTransRate());
                    fexTranDeal.setSettleMoney(bisForeignExcDTO.getActualTransMoney());
                    model.addAttribute("fexTranDealPage", fexTranDeal);
                }
            }
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        }
        return "foreign/addFexTranDeal" + this.getForwardByOpType(opType);
    }

    /**
     * 根据操作类型返回不同的跳转页面
     * @param opType
     * @return
     */
    public String getForwardByOpType(String opType){
        /**操作类型 1-提前交割或展期、2-违约、3-NDF交割*/
        String forward = "";
        if(ForeignConstant.OP_TYPE_1.equals(opType)){
            forward = "Delivery";
        }else if(ForeignConstant.OP_TYPE_2.equals(opType)){
            forward = "Default";
        }else if(ForeignConstant.OP_TYPE_3.equals(opType)){
            forward = "NdfDelivery";
        }else if(ForeignConstant.OP_TYPE_4.equals(opType)){
            forward = "RateConfirm";
        }
        return forward;
    }

    /**
     * 添加交易处理信息
     * @param fexTranDeal
     * @return
     */
    @LogMessage(content = "添加交易处理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDeal")
    @ResponseBody
    public AjaxJson saveDeal(FexTranDealVO fexTranDeal){
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(fexTranDeal.getTranReqId());
        if(StringUtils.isBlank(fexTranDeal.getCurId())){
            if(ForeignConstant.OP_TYPE_1.equals(fexTranDeal.getOpType())){
                if(ForeignConstant.DELIVERY_SIGN_1.equals(fexTranDeal.getDeliverySign())){
                    fexTranDeal.setCurId(fexTranReq.getBuyCurId());
                }else{
                    fexTranDeal.setCurId(fexTranReq.getSellCurId());
                }
            }
        }
        if(StringUtils.isBlank(fexTranDeal.getTranAddr())){
            fexTranDeal.setTranAddr(fexTranReq.getTranAddr());
        }
        //结算日期交易(汇率确认无需校验结算日期)
        if(ForeignConstant.OP_TYPE_4.equals(fexTranDeal.getOpType())
                || this.checkWorkDate(fexTranDeal.getNewSettleDate(), fexTranDeal.getCurId(), fexTranDeal.getTranAddr(), fexTranReq.getProId())){
            if(StringUtils.isBlank(fexTranDeal.getId())){
                fexTranDealService.saveFexTranDeal(fexTranDeal, this.getCurrentUser());
            }else {
                fexTranDealService.updateFexTranDeal(fexTranDeal, this.getCurrentUser());
            }
        }
        return success();
    }

    /**
     * 跳转至审核列表
     * @param opType	操作类型
     * @return
     */
    @GetMapping("/listTranDealApprove/{opType}")
    public String listTranDealApprove(@PathVariable String opType, String flowStatus, Model model){
        model.addAttribute("opType", opType);
        model.addAttribute("flowStatus", flowStatus);
        return "foreign/listFexTranDeal" + this.getForwardByOpType(opType) + "Approve";
    }

    /**
     * 外汇交易表 table列表
     *
     * @return
     */
    @PostMapping("/dataTableTranDealApprove")
    @ResponseBody
    public DataTablesOutput dataTableTranDealApprove(@RequestBody DataTablesInput dataTablesInput){
        return fexTranDealService.dataTableApprove(dataTablesInput);
    }

    /**
     * 跳转至审核详情界面
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approveTranDeal")
    public String approveTranDeal(String id, String taskId, Model model) {
        FexTranDealVO vo = new FexTranDealVO();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.fexTranDealService.getByIdUnion(id);
            FexTranReqVO fexTranReq = this.fexTranReqService.getByIdUnion(vo.getTranReqId());
            model.addAttribute("fexTranDealPage", vo);
            model.addAttribute("fexTranReqPage", fexTranReq);
            model.addAttribute("opType", vo.getOpType());
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
            model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            //交易员
            model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
            //初始化参数
            this.initConfig(model);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
            getCustFields(fexTranReq.getId(), model);
            model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        }
        return "foreign/approveFexTranDeal";
    }

    /**
     * 跳转至手机审核详情界面
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileTranDealApprove/{opType}")
    public String mobileTranDealApprove(@PathVariable String opType, String businessId, String taskId, Model model) {
        FexTranDealVO vo = this.fexTranDealService.getByIdUnion(businessId);
        FexTranReqVO fexTranReq = this.fexTranReqService.getByIdUnion(vo.getTranReqId());
        model.addAttribute("fexTranDealPage", vo);
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("opType", opType);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        return "foreign/mobileViewFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
    }

    /**
     * 删除交易交割表
     * @param id
     * @return
     */
    @LogMessage(content = "删除交易交割表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/deleteTranDeal/{id}")
    @ResponseBody
    public AjaxJson deleteTranDeal(@PathVariable("id") String id){
        fexTranDealService.delFexTranDeal(id, this.getCurrentUser());
        return success();
    }

    /**
     * 即期/远期交易损益统计
     * @param fexTranType	外汇交易类型 0-即期、1-远期
     * @return
     */
    @GetMapping("/queryForeignReport/{fexTranType}")
    public String queryForeignReport(@PathVariable String fexTranType, String flowStatus, Model model){
        model.addAttribute("fexTranType", fexTranType);
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        if(natualCurrency!=null){
            model.addAttribute("cur", btCurrencyService.getNatualCurrency().getId());
        }else{
            model.addAttribute("cur", null);
        }
        return "foreign/listFexTranReqForeignReport";
    }

    /**
     * 即期/远期交易损益统计
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableForeignReport")
    @ResponseBody
    public DataTablesOutput dataTableForeignReport(@RequestBody DataTablesInput dataTablesInput) {
        //性能优化，首次进入报表页面不加载列表
        if (fexTranReqService.checkConditions(dataTablesInput)){
            return new DataTablesOutput<>();
        }
        return fexTranReqService.getDataTablesForeignReport(dataTablesInput,  this.getCurrentUser());
    }

    /**
     * 交易是否是工作日
     * @param settleDate
     * @param curId
     * @param tranAddrId
     * @return
     */
    private boolean checkWorkDate(Date settleDate,String curId,String tranAddrId, String proId){
        //交易地点为空时，无需校验是否是工作日
        if (StringUtils.isNotBlank(tranAddrId)) {
            /**交易产品：非工作日是否允许结算 1-是 0-否*/
            FexProductVO fexProduct = fexProductService.getByIdUnion(proId);
            if (fexProduct != null && CommonConstant.NO.equals(fexProduct.getIsAllowedSettle())) {
                //校验工作日
                if (!calendarService.checkWorkDay(settleDate, curId, tranAddrId)) {
                    throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_SETTLEDATE_NOT_WORKDATE, DateUtils.formatDate(settleDate, "yyyy-MM-dd"));
                }
            }
        }
        return true;
    }

    /**
     * 提交前校验
     * @param fexTranReq
     * @return
     */
    private boolean checkData(FexTranReqVO fexTranReq) {
        if (StringUtils.isNotBlank(fexTranReq.getId())) {
            if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
                //资金收付交易
                if (!ForeignConstant.REMIT_TYPE_2.equals(fexTranReq.getRemitType())) {
                    // 校验交易编码2
                    if (StringUtils.isBlank(fexTranReq.getTranCode2()) && (fexTranReq.getTranAmt2() != null || StringUtils.isNotBlank(fexTranReq.getTranAddWord2()))) {
                        // 相应金额2	或者 交易附言2 不为空，交易编码2必填
                        throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANCODE2_NULL, fexTranReq.getBillNo());
                    }
                    // 校验相应金额2
                    if (fexTranReq.getTranAmt2() == null && (StringUtils.isNotBlank(fexTranReq.getTranCode2()) || StringUtils.isNotBlank(fexTranReq.getTranAddWord2()))) {
                        // 交易附言2	或者 交易编码2 不为空，相应金额2必填
                        throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANAMT2_NULL, fexTranReq.getBillNo());
                    }
                    // 校验交易附言2
                    if (StringUtils.isBlank(fexTranReq.getTranAddWord2()) && (fexTranReq.getTranAmt2() != null || StringUtils.isNotBlank(fexTranReq.getTranCode2()))) {
                        // 相应金额2	或者 交易编码2 不为空，交易附言2必填
                        throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANADDWORD2_NULL, fexTranReq.getBillNo());
                    }
                    if (StringUtils.isBlank(fexTranReq.getTranCurId2()) && (fexTranReq.getTranAmt2() != null || StringUtils.isNotBlank(fexTranReq.getTranCode2()) || StringUtils.isNotBlank(fexTranReq.getTranAddWord2()))) {
                        // 相应金额2	或者 交易编码2 或者 交易附言2 不为空，交易币种2默认交易币种1
                        fexTranReq.setTranCurId2(fexTranReq.getTranCurId1());
                    }
                }
            }
        }
        return true;
    }

    /**
     * 外汇期权交易处理表 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addFexTranOptionDeal/{opType}/{tranReqId}")
    public String addFexTranOptionDeal(@PathVariable("opType") String opType, @PathVariable("tranReqId") String tranReqId, Model model) {
        //根据操作类型和交易ID获取对应的处理信息
        FexTranOptionDealVO fexTranOptionDeal = fexTranOptionDealService.getByTranReqIdUnion(opType, tranReqId);
        //获取交易信息
        FexTranReqVO fexTranReq = fexTranReqService.getNewByIdUnion(tranReqId);
        if(fexTranOptionDeal == null) {
            fexTranOptionDeal = new FexTranOptionDealVO();
            fexTranOptionDeal.setId("");
            fexTranOptionDeal.setTranReqId(tranReqId);
            fexTranOptionDeal.setCorpId(fexTranReq.getCorpId());
            fexTranOptionDeal.setCurId(fexTranReq.getCurId());
            fexTranOptionDeal.setOpType(opType);
            fexTranOptionDeal.setTranAddrId(fexTranReq.getTranAddrId());
            fexTranOptionDeal.setTranAddr(fexTranReq.getTranAddr());
            fexTranOptionDeal.setTranSettleMethod(fexTranReq.getTranSettleMethod());
            //敲出
            if(ForeignConstant.OPTION_OP_TYPE_4.equals(opType)) {
                if(CommonConstant.YES.equals(fexTranReq.getIsRefund())) {
                    //退款支付时间 0-到期日、1-敲出日
                    if (CommonConstant.NO.equals(fexTranReq.getRefundTime())) {
                        fexTranOptionDeal.setRefundDate(fexTranReq.getExpiresDate());
                    }
                    //退款金额的币种为期权费结算币种
                    fexTranOptionDeal.setCurId(fexTranReq.getPremiumCur());
                }
            }
        }
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRANEXERCISE);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("fexTranOptionDealPage", fexTranOptionDeal);
        model.addAttribute("fexTranReqPage", fexTranReq);
        //初始化参数
        initConfig(model);
        return "foreign/addFexTranOptionDeal" + this.getOptionForwardByOpType(opType);
    }

    /**
     * 根据操作类型返回不同的跳转页面
     * @param opType
     * @return
     */
    public String getOptionForwardByOpType(String opType){
        /**操作类型 1-失效，2-已确认均价，3-行权，4-敲出，5-敲入*/
        String forward = "";
        if(ForeignConstant.OPTION_OP_TYPE_1.equals(opType)){
            forward = "Invalid";
        }else if(ForeignConstant.OPTION_OP_TYPE_2.equals(opType)){
            forward = "EnterAvg";
        }else if(ForeignConstant.OPTION_OP_TYPE_3.equals(opType)){
            forward = "Exercise";
        }else if(ForeignConstant.OPTION_OP_TYPE_4.equals(opType)){
            forward = "KnockOut";
        }else if(ForeignConstant.OPTION_OP_TYPE_5.equals(opType)){
            forward = "KnockIn";
        }
        return forward;
    }

    /**
     * 添加交易处理信息
     * @param fexTranOptionDeal
     * @return
     */
    @LogMessage(content = "添加交易处理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOptionDeal")
    @ResponseBody
    public AjaxJson saveOptionDeal(FexTranOptionDealVO fexTranOptionDeal){
        fexTranOptionDealService.saveFexTranOptionDeal(fexTranOptionDeal, this.getCurrentUser());
        return success();
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
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        getCustFields(fexTranReq.getId(), model);
        model.addAttribute("custInfoBusId", fexTranReq.getId());
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

        if(StringUtils.isNotBlank(fexTranReq.getMergeId()) || StringUtils.isNotBlank(fexTranReq.getSplitId())){
            model.addAttribute("editPayMoneyFlag", "0");
        }else{
            model.addAttribute("editPayMoneyFlag", "1");
        }

        //查询人民币
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbId", rmbCurrency == null ? "":rmbCurrency.getId());
        //获取国家地区-中国
        SysCountryCustDTO china = sysCountryCustService.getByNameZh("中国");
        model.addAttribute("chinaId", china == null ? "":china.getId());

        return "foreign/updateFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
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
        getCustFields(fexTranReq.getId(), model);
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        String showTranAmt = CommonConstant.NO;
        if (!ForeignConstant.REMIT_TYPE_2.equals(fexTranReq.getRemitType())
                && (fexTranReq.getTranAmt1() != null || fexTranReq.getTranAmt2() != null)) {
            showTranAmt = CommonConstant.YES;
        }
        model.addAttribute("showTranAmt", showTranAmt);
        return "foreign/splitFexTranReqPay";
    }

    /**
     * 拆分保存
     *
     * @param fexTranReq
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_PAY_SPLIT)
    @LogMessage(content = "拆分交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitSave")
    @ResponseBody
    public AjaxJson splitSave(FexTranReqVO fexTranReq,@RequestParam("payMoneys[]") List<BigDecimal> payMoneys,@RequestParam("budgets[]") List<String> budgets) {
        fexTranReq = fexTranReqService.getByIdUnion(fexTranReq.getId());
        //暂存和打回状态才可以进行拆分
        if (fexTranReq != null && !NetbankConstant.STATUE_TEMP.equals(fexTranReq.getStatus()) && !NetbankConstant.STATUE_BACK.equals(fexTranReq.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, fexTranReq.getBillNo());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal payMoney : payMoneys) {
            total = total.add(payMoney);
        }
        //拆分金额总和必须等于拆分前的付款金额
        if (total.compareTo(fexTranReq.getPayMoney()) != 0) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_VALID);
        }
        fexTranReqService.splitSave(fexTranReq, payMoneys, budgets, null, null, this.getCurrentUser());
        return success();
    }

    /**
     * 拆分保存
     *
     * @param fexTranReq
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_PAY_SPLIT)
    @LogMessage(content = "拆分交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitAmtSave")
    @ResponseBody
    public AjaxJson splitAmtSave(FexTranReqVO fexTranReq,@RequestParam("payMoneys[]") List<BigDecimal> payMoneys,@RequestParam("budgets[]") List<String> budgets
            ,@RequestParam("tranAmt1s[]") List<BigDecimal> tranAmt1s,@RequestParam("tranAmt2s[]") List<BigDecimal> tranAmt2s) {
        fexTranReq = fexTranReqService.getByIdUnion(fexTranReq.getId());
        //暂存和打回状态才可以进行拆分
        if (fexTranReq != null && !NetbankConstant.STATUE_TEMP.equals(fexTranReq.getStatus()) && !NetbankConstant.STATUE_BACK.equals(fexTranReq.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, fexTranReq.getBillNo());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal payMoney : payMoneys) {
            total = total.add(payMoney);
        }
        //拆分金额总和必须等于拆分前的付款金额
        if (total.compareTo(fexTranReq.getPayMoney()) != 0) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_VALID);
        }
        fexTranReqService.splitSave(fexTranReq, payMoneys, budgets, tranAmt1s, tranAmt2s,this.getCurrentUser());
        return success();
    }

    /**
     * 是否可以拆分
     *
     * @param id
     * @return
     */
    @RequestMapping("/isMergeBill")
    @ResponseBody
    public AjaxJson isMergeBill(String id) {
        AjaxJson json = new AjaxJson();
        boolean flag = this.fexTranReqService.isMergeBill(id);
        json.setSuccess(flag);
        if (flag) {
            json.setMessageEnum(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_SPLIT);
        }
        return json;
    }

    /**
     * 撤销拆分
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_PAY_SPLIT)
    @LogMessage(content = "撤销拆分交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitCancel/{id}")
    @ResponseBody
    public AjaxJson splitCancel(@PathVariable("id") String id) {
        fexTranReqService.splitCancel(id, this.getCurrentUser());
        return success();
    }

    /**
     * 合并
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_PAY_MERGE)
    @LogMessage(content = "修改交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/merge")
    @ResponseBody
    public AjaxJson merge(@RequestParam("ids") String ids) {
        fexTranReqService.merge(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 撤销合并
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_PAY_MERGE)
    @LogMessage(content = "撤销交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestParam("ids") String ids) {
        fexTranReqService.mergeCancel(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 批量选择付款账号
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXTRANREQ_PAY_BATCHSETPAYERACC)
    @GetMapping("/toSetPayerAcc/{corpId}")
    public String toSetPayerAcc(@PathVariable("corpId") String corpId, String tranType, Model model) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("tranType", tranType);
        return "foreign/batchSetPayerAcc";
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
        fexTranReqService.batchSetPayerAcc(fexTranReqVO, this.getCurrentUser(), false);
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


        // 自定义模板交易详情页
        if (StringUtils.isNotEmpty(fexTranReq.getTemplateId())){
            getCustFields(fexTranReq.getId(), model);
            return "foreign/foreignPayReq/viewFexTranReqPayCust";
        } else if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())
                && (StringUtils.isNotEmpty(fexTranReq.getTranAppDtlId()) || StringUtils.isNotEmpty(fexTranReq.getVersionNo()))){
            // 无模板，默认显示页面
            return "foreign/foreignPayReq/viewDefFexTranReqPayCust";
        }

        return "foreign/viewFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
    }

    private void setTemplateName(FexTranReqVO fexTranReq){
        if (!EmptyUtils.isEmpty(fexTranReq.getTemplateId())) {
            FexTranTemplateConfigVO info = fexTranConfigTemplateService.getTemplateConfigById(fexTranReq.getTemplateId());
            if (info != null) {
                String name=  String.format(FexTranConfigTemplateService.childTemplateNameFormat,info.getTemplateNameParent(),
                        info.getBankTypeName(),info.getBifName(),info.getCountryZhName(),info.getBankPayType(),info.getPayTypeName()  );
                fexTranReq.setTemplateName(name);
            }
        }
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
