package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexForeignReq;
import com.bytter.biz.foreign.service.FexForeignReqService;
import com.bytter.biz.foreign.vo.FexForeignReqVO;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.BankConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BankTypeCustomDTO;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外币汇款申请表
 * @date 2019-07-15 15:20:33
 */
@Controller
@RequestMapping("/foreign/fexforeignreq")
public class FexForeignReqController extends BaseController {
    @Autowired
    private FexForeignReqService fexForeignReqService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

    /**
     * 外币汇款申请表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询外币汇款表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexForeignReq";
    }

    /**
     * 外币汇款申请表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexForeignReqService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 外币汇款查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询外币汇款申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "foreign/listFexForeignReqQuery";
    }

    /**
     * 外币汇款申请表新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXFOREIGNREQ_APPLY)
    @GetMapping("/add")
    public String add(FexForeignReq fexForeignReq, Model model) {
        if (StringUtils.isNotEmpty(fexForeignReq.getId())) {
            fexForeignReq = fexForeignReqService.get(fexForeignReq.getId());
            model.addAttribute("fexForeignReqPage", fexForeignReq);
        }
        //初始化参数
        this.initConfig(model);
        return "foreign/addFexForeignReq";
    }

    /**
     * 设置支付类型
     * @param fexForeignReq
     * @param model
     */
    public void setBankPayType(FexForeignReqVO fexForeignReq, Model model) {
        BankTypeDTO bankTypeDTO = new BankTypeDTO();
        // 支付类型
        BankTypeCustomDTO bankPayTypeDTO = new BankTypeCustomDTO();
        // 收款人类型
        BankTypeCustomDTO payeeTypeDTO = new BankTypeCustomDTO();
        BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexForeignReq.getPayerAcc());
        if (payerAcc != null) {
            bankTypeDTO = bankTypeService.getObjectById(payerAcc.getBankTypeId());
            // 支付类型
            if (StringUtils.isNotBlank(fexForeignReq.getBankPayType())) {
                bankPayTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexForeignReq.getBankPayType(), BankConstant.CUSTOM_TYPE_BANKPAYTYPE);
            }
            // 收款人类型
            if (StringUtils.isNotBlank(fexForeignReq.getPayeeType())) {
                payeeTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexForeignReq.getPayeeType(), BankConstant.CUSTOM_TYPE_PAYEETYPE);
            }
        }
        String countryId = BdlinkConstant.COUNTRY_CODE_CHINA;
        if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())) {
            countryId = payerAcc.getProv();
        }
        model.addAttribute("countryId", countryId);
        model.addAttribute("payerAcc", payerAcc);
        model.addAttribute("bankType", bankTypeDTO);
        model.addAttribute("bankPayType", bankPayTypeDTO);
        model.addAttribute("payeeType", payeeTypeDTO);
    }

    /**
     * 外币汇款申请表编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXFOREIGNREQ_APPLY)
    @GetMapping("/update")
    public String update(FexForeignReqVO fexForeignReq, Model model) {
        if (StringUtils.isNotEmpty(fexForeignReq.getId())) {
            fexForeignReq = fexForeignReqService.getByIdUnion(fexForeignReq.getId());
            //设置支付类型
            this.setBankPayType(fexForeignReq, model);
            model.addAttribute("fexForeignReqPage", fexForeignReq);
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(fexForeignReq.getBankTypeId());
            String remitMethod = "0,1";
            if (bankTypeDTO != null && StringUtils.isNotBlank(bankTypeDTO.getRemitMethod())) {
                remitMethod = remitMethod + CommonConstant.COMMA_SPLIT + bankTypeDTO.getRemitMethod();
            }
            model.addAttribute("remitMethod", remitMethod);
            //获取付款账户
            BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexForeignReq.getPayerAcc());
            if (payerAcc != null && CommonConstant.NO.equals(payerAcc.getIsDomesticBank())) {
                if (payerAcc != null && StringUtils.isNotBlank(payerAcc.getProv())) {
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(payerAcc.getProv());
                    if (sysCountryCustDTO != null) {
                        model.addAttribute("payerCountryId", sysCountryCustDTO.getId());
                        model.addAttribute("payerCountryNameZh", sysCountryCustDTO.getNameZh());
                        model.addAttribute("payerEnCode", sysCountryCustDTO.getEnCode());
                    }
                }
            }
        }
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_FOREIGN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        CorpDTO corp = corpService.get(fexForeignReq.getCorpId());
        //所用账号单位代码
        if (EmptyUtils.isBlank(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        model.addAttribute("accCorpId", corp.getUserAccountCode());
        return "foreign/updateFexForeignReq";
    }

    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BUDGETSEL).getValue());
        model.addAttribute("payItem", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_PAYITEM).getValue());
        model.addAttribute("externalCorp", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
    }

    /**
     * 获取汇款账号
     *
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayerAcc")
    @ResponseBody
    public List<BtBankAccDTO> getPayerAcc(String corpId, String bankTypeId) {
        List<BtBankAccDTO> listBankAcc = new ArrayList<>();
        if (!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(bankTypeId)) {
            //获取转账权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTOByMultiVal(this.getUserId(), corpId, null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
                HashMap<String, BisBifInitDTO> bifCodeMap = new HashMap<>();
                for (BtBankAccDTO dto : listBankAccTemp) {
                    //汇款账号必须账户类别为活期，且银行类别要跟汇款的银行类别一致
                    if (!BtBankAccConstant.ACCTYPE_02.equals(dto.getAccType()) && !BtBankAccConstant.ACCTYPE_06.equals(dto.getAccType())
                            && bankTypeId.equals(dto.getBankTypeId())) {
                        dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
                        if(StringUtils.isNotBlank(dto.getBifCode())) {
                            BisBifInitDTO bifInitDTO = bifCodeMap.get(dto.getBifCode());
                            if (bifInitDTO == null) {
                                bifInitDTO = bisBifInitService.getObjectById(dto.getBifCode());
                                bifCodeMap.put(dto.getBifCode(), bifInitDTO);
                            }
                            if(bifInitDTO != null){
                                dto.setBifCodeStr(bifInitDTO.getBifCode());
                            }
                        }
                        //境外银行获取国家地区名称
                        if (CommonConstant.NO.equals(dto.getIsDomesticBank()) && StringUtils.isNotBlank(dto.getProv())) {
                            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(dto.getProv());
                            if (sysCountryCustDTO != null) {
                                dto.setProvName(sysCountryCustDTO.getNameZh());
                                //简称
                                dto.setCountryEnCode(sysCountryCustDTO.getEnCode());
                            }
                        }
                        listBankAcc.add(dto);
                    }
                }
            }
        }
        return listBankAcc;
    }

    /**
     * 添加外币汇款表
     *
     * @param fexForeignReq
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXFOREIGNREQ_APPLY)
    @LogMessage(content = "添加外币汇款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexForeignReqVO fexForeignReq) {
        fexForeignReqService.saveFexForeignReq(fexForeignReq, this.getCurrentUser());
        return success();
    }

    /**
     * 修改外币汇款表
     *
     * @param fexForeignReq
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXFOREIGNREQ_APPLY)
    @LogMessage(content = "修改外币汇款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexForeignReqVO fexForeignReq) {
        fexForeignReqService.updateFexForeignReq(fexForeignReq, this.getCurrentUser());
        return success();
    }

    /**
     * 删除外币汇款表
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXFOREIGNREQ_APPLY)
    @LogMessage(content = "删除外币汇款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{chargeBackType}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("chargeBackType") String chargeBackType) {
        //状态校验
        FexForeignReq entity = fexForeignReqService.get(id);
        //暂存和编辑状态才可以提交
        if (entity != null && !ForeignConstant.STATUE_TEMP.equals(entity.getStatus()) && !ForeignConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        fexForeignReqService.delFexForeignReq(id, this.getCurrentUser(), false, chargeBackType);
        return success();
    }

    /**
     * 批量删除外币汇款表
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(com.bytter.facade.netbank.constant.ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_DELETE)
    @LogMessage(content = "批量删除外币汇款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids,@RequestParam(value = "chargeBackType",required = false)String chargeBackType) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            FexForeignReq entity = fexForeignReqService.get(id);
            //暂存和编辑状态才可以提交
            if (entity != null && !ForeignConstant.STATUE_TEMP.equals(entity.getStatus()) && !ForeignConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        fexForeignReqService.batchDelFexForeignReq(ids, this.getCurrentUser(), chargeBackType);
        return success();
    }

    /**
     * 批量提交外币汇款表
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(com.bytter.facade.netbank.constant.ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @LogMessage(content = "批量提交外币汇款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexForeignReqVO fexForeignReq = fexForeignReqService.getByIdUnion(id);
            fexForeignReq.setStatus(ForeignConstant.STATUE_WAIT_CHECK);
            fexForeignReqService.updateFexForeignReq(fexForeignReq, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 外币汇款表详情页
     *
     * @return
     */
    @LogMessage(content = "外币汇款表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        FexForeignReqVO fexForeignReq = fexForeignReqService.getByIdUnion(id);
        //设置支付类型
        this.setBankPayType(fexForeignReq, model);
        model.addAttribute("fexForeignReqPage", fexForeignReq);
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_FOREIGN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FOREIGN);
        return "foreign/viewFexForeignReq";
    }

    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/listFexForeignReqApprove";
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.fexForeignReqService.dataTableApprove(dataTablesInput);
    }

    /**
     * 根据审批列表的汇款单corpIds组装单位树
     *
     * @return
     */
    @GetMapping("/treeByCorpIds")
    @ResponseBody
    public Object treeByCorpIds() {
        return this.fexForeignReqService.treeByCorpIds(getCurrentUser());
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FexForeignReqVO fexForeignReqVO = this.fexForeignReqService.getByIdUnion(id);
            //设置支付类型
            this.setBankPayType(fexForeignReqVO, model);
            model.addAttribute("fexForeignReqPage", fexForeignReqVO);
            model.addAttribute("taskId", taskId);
            //初始化参数
            this.initConfig(model);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_FOREIGN);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FOREIGN);
        }
        return "foreign/approveFexForeignReq";
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            FexForeignReqVO fexForeignReqVO = this.fexForeignReqService.getByIdUnion(businessId);
            model.addAttribute("fexForeignReqPage", fexForeignReqVO);
            model.addAttribute("taskId", taskId);
            this.initConfig(model);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_FOREIGN);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FOREIGN);
        }
        return "foreign/viewApproveFexForeignReq";
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<FexForeignReqVO> list) {
        this.fexForeignReqService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
