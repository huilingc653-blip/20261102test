package com.bytter.bill.web.bill.controller;

import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmk;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillNo;
import com.bytter.bill.domain.bill.entity.AbClaimAccount;
import com.bytter.bill.domain.bill.service.AbBillRuleBillDtlRmkService;
import com.bytter.bill.domain.bill.service.AbBillRuleBillNoService;
import com.bytter.bill.domain.bill.service.AbClaimAccountService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkVO;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillNoVO;
import com.bytter.bill.domain.bill.vo.AbClaimAccountVo;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 承兑汇票系统票据认领参数配置
 * @date 2022-03-01 15:25:10
 */
@Controller
@RequestMapping("/bill/accebillClaim/config")
public class AccebillClaimConfigController extends BaseController {
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillRuleBillNoService abBillRuleBillNoService;
    @Autowired
    private AbClaimAccountService abClaimAccountService;
    @Autowired
    private AbBillRuleBillDtlRmkService abBillRuleBillDtlRmkService;
    @DubboReference(check = false)
    public IBankTypeService iBankTypeService;
    /**
     * 承兑汇票系统日期参数配置
     *
     * @param model
     * @return
     */
    @GetMapping("/dateConfig")
    public String dateConfig(Model model) {
        model.addAttribute("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return "bill/accebill/accebillDateConfig";
    }
    /**
     * 承兑汇票系统金额参数配置
     *
     * @param model
     * @return
     */
    @GetMapping("/moneyConfig")
    public String moneyConfig(Model model) {
        model.addAttribute("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return "bill/accebill/accebillMoneyConfig";
    }
    /**
     * 认领参数controller
     *
     * @param model
     * @return
     */
    @GetMapping("/claimParam")
    public String fcsConfig(Model model) {
        model.addAttribute("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return "bill/accebill/claimParam";
    }
    /**
     * 承兑汇票系统账号规则配置
     *
     * @param model
     * @return
     */
    @GetMapping("/accountConfig")
    public String accountConfig(Model model) {
        model.addAttribute("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return "bill/accebill/accebillAccountConfig";
    }
    /**
     * 承兑汇票系统账号规则配置 table列表
     * /
     * /**
     * 承兑汇票系统账号规则配置 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/accountConfigDatatable")
    @ResponseBody
    public DataTablesOutput accountConfigDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimAccountService.listAbClaimAccountInfo(dataTablesInput);
    }
    /**
     * 承兑汇票系统账号规则配置维护新增页面跳转
     *
     * @return
     */
    @GetMapping("/accountConfigAdd")
    public String add(AbClaimAccount abClaimAccount, Model model) {
        if (StringUtils.isNotEmpty(abClaimAccount.getId())) {
            abClaimAccount = abClaimAccountService.get(abClaimAccount.getId());
            model.addAttribute("abClaimAccountVo", abClaimAccount);
        }
        return "bill/accebill/addAbClaimAccount";
    }
    /**
     * 承兑汇票系统账号规则配置维护编辑页面跳转
     *
     * @return
     */
    @GetMapping("/accountConfigUpdate")
    public String update(AbClaimAccount abClaimAccount, Model model) {
        if (StringUtils.isNotEmpty(abClaimAccount.getId())) {
            AbClaimAccountVo abClaimAccountVo = abClaimAccountService.getByIdUnion(abClaimAccount.getId());
            model.addAttribute("abClaimAccountVo", abClaimAccountVo);
        }
        return "bill/accebill/updateAbClaimAccount";
    }
    /**
     * 承兑汇票系统账号规则配置维护详情页
     *
     * @return
     */
    @LogMessage(content = "承兑汇票系统账号规则配置维护详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/accountConfigView/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        AbClaimAccountVo abClaimAccountVo = abClaimAccountService.getByIdUnion(id);
        model.addAttribute("abClaimAccountVo", abClaimAccountVo);
        return "bill/accebill/viewAbClaimAccount";
    }
    /**
     * 添加承兑汇票系统账号规则配置
     *
     * @param abClaimAccountVo
     * @return
     */
    @LogMessage(content = "添加承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/accountConfigSave")
    @ResponseBody
    public AjaxJson save(AbClaimAccountVo abClaimAccountVo) {
        // 创建人信息
        if (EmptyUtils.isBlank(abClaimAccountVo.getCreateBy())) {
            abClaimAccountVo.setCreateBy(this.getCurrentUser().getLoginName());
            abClaimAccountVo.setCreateDate(DateUtils.getDate());
        }
        // 检验单位+账号+业务类型不允许重复，当重复时系统弹出提示对话框，提示文字为“单位+账号+业务类型不允许重复”
        boolean ischeck = abClaimAccountService.checkAddRepeat(abClaimAccountVo);
        if (!ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_REPEAT);
        }
        AbClaimAccount abClaimAccount = new AbClaimAccount();
        BeanUtils.copyNotNullProperties(abClaimAccountVo, abClaimAccount);
        abClaimAccount.setBankTypeName(abClaimAccountVo.getBankTypeName());
        abClaimAccount.setCorpName(abClaimAccountVo.getCorpName());
        abClaimAccountService.saveOrUpdate(abClaimAccount);
        return success();
    }
    /**
     * 修改承兑汇票系统账号规则配置
     *
     * @param abClaimAccountVo
     * @return
     */
    @LogMessage(content = "修改承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/accountConfigModify")
    @ResponseBody
    public AjaxJson update(AbClaimAccountVo abClaimAccountVo) {
        // 检验单位+账号+业务类型不允许重复，当重复时系统弹出提示对话框，提示文字为“单位+账号+业务类型不允许重复”
        boolean ischeck = abClaimAccountService.checkAddRepeat(abClaimAccountVo);
        if (!ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_REPEAT);
        }
        AbClaimAccount t = abClaimAccountService.get(abClaimAccountVo.getId());
        BeanUtils.copyNotNullProperties(abClaimAccountVo, t);
        // 修改人信息
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        abClaimAccountService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除承兑汇票系统账号规则配置
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/accountConfigDelete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        abClaimAccountService.deleteAbClaimAccount(id);
        return success();
    }
    /**
     * 批量删除承兑汇票系统账号规则
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/accountConfigBatchDelete/{ids}")
    @ResponseBody
    public AjaxJson deleteDelete(@PathVariable("ids") String ids) {
        for (String id : ids.split(",")) {
            abClaimAccountService.deleteAbClaimAccount(id);
        }
        return success();
    }
    /**
     * 票号规则controller
     *
     * @param model
     * @return
     */
    @GetMapping("/ruleBillNo")
    public String list(Model model) {
        return "bill/accebill/ruleBillNo";
    }
    /**
     * 票号规则 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票号规则列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/ruleBillNo/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillRuleBillNoService.getBillNOList(dataTablesInput);
    }
    /**
     * 票号规则新增页面跳转
     *
     * @return
     */
    @GetMapping("/ruleBillNo/add")
    public String add() {
        return "bill/accebill/addBillRule";
    }
    /**
     * 删除票号信息
     *
     * @return
     */
    @PostMapping("/ruleBillNo/delete/{id}")
    @ResponseBody
    public AjaxJson deleteRuleNO(@PathVariable("id") String id) {
        abBillRuleBillNoService.delete(id);
        return success();
    }
    /**
     * 票号规则编辑页面跳转
     *
     * @return
     */
    @GetMapping("/ruleBillNo/update")
    public String update(AbBillRuleBillNo abBillRuleBillNo, Model model) {
        if (StringUtils.isNotEmpty(abBillRuleBillNo.getId())) {
            abBillRuleBillNo = abBillRuleBillNoService.get(abBillRuleBillNo.getId());
            model.addAttribute("abBillNoPage", abBillRuleBillNo);
        }
        return "bill/accebill/updateBillRule";
    }
    /**
     * 票号规则查看跳转
     *
     * @return
     */
    @GetMapping("/ruleBillNo/view")
    public String view(AbBillRuleBillNo abBillRuleBillNo, Model model) {
        if (StringUtils.isNotEmpty(abBillRuleBillNo.getId())) {
            abBillRuleBillNo = abBillRuleBillNoService.get(abBillRuleBillNo.getId());
            AbBillRuleBillNoVO abBillRuleBillNoVO = new AbBillRuleBillNoVO();
            BeanUtil.copyProperties(abBillRuleBillNo, abBillRuleBillNoVO);
            if (StringUtils.isNotEmpty(abBillRuleBillNoVO.getBankTypeId())) {
                abBillRuleBillNoVO.setBankTypeName(iBankTypeService.getObjectById(abBillRuleBillNoVO.getBankTypeId()).getTypeName());
            }
            model.addAttribute("abBillNoPage", abBillRuleBillNoVO);
        }
        return "bill/accebill/viewBillRule";
    }
    /**
     * 添加票号规则
     *
     * @return
     */
    @PostMapping("/ruleBillNo/save")
    @ResponseBody
    public AjaxJson save(AbBillRuleBillNoVO abBillRuleBillNoVO) {
        boolean ischeck = abBillRuleBillNoService.checkAddBillNO(abBillRuleBillNoVO);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillNo abBillRuleBillNo = new AbBillRuleBillNo();
        BeanUtils.copyNotNullProperties(abBillRuleBillNoVO, abBillRuleBillNo);
        // 保存的时候要把银行类型名称存储下来  ,已在前端增加提交字段，后端无需处理
        abBillRuleBillNoService.save(abBillRuleBillNo);
        return success();
    }
    /**
     * 修改票号规则
     *
     * @return
     */
    @PostMapping("/ruleBillNo/modify")
    @ResponseBody
    public AjaxJson update(AbBillRuleBillNoVO abBillRuleBillNoVO) {
        boolean ischeck = abBillRuleBillNoService.checkupdateBillNO(abBillRuleBillNoVO);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillNo abBillRuleBillNo = new AbBillRuleBillNo();
        BeanUtils.copyNotNullProperties(abBillRuleBillNoVO, abBillRuleBillNo);
        abBillRuleBillNoService.saveOrUpdate(abBillRuleBillNo);
        return success();
    }

    /**
     * 票据流水备注规则controller
     *
     * @param model
     * @return
     */
    @GetMapping("/ruleBillDtlRmk")
    public String listRuleBillDtlRmk(Model model) {
        return "bill/accebill/ruleBillDtlRmk";
    }

    /**
     * 票据流水备注规则 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/ruleBillDtlRmk/datatable")
    @ResponseBody
    public DataTablesOutput ruleBillDtlRmkDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillRuleBillDtlRmkService.getBillRuleList(dataTablesInput);
    }

    /**
     * 票据流水备注规则新增页面跳转
     *
     * @return
     */
    @GetMapping("/ruleBillDtlRmk/add")
    public String addBillDtlRmkRule() {
        return "bill/accebill/addBillDtlRmk";
    }

    /**
     * 添加票据流水备注规则
     *
     * @return
     */
    @LogMessage(content = "保存 票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/ruleBillDtlRmk/save")
    @ResponseBody
    public AjaxJson save(AbBillRuleBillDtlRmkVO abBillRuleBillDtlRmkVO, HttpServletRequest request) {
        boolean ischeck = abBillRuleBillDtlRmkService.checkAddBillDtlRmk(abBillRuleBillDtlRmkVO,true);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillDtlRmk abBillRuleBillDtlRmk = new AbBillRuleBillDtlRmk();
        BeanUtils.copyNotNullProperties(abBillRuleBillDtlRmkVO, abBillRuleBillDtlRmk);
        abBillRuleBillDtlRmk.setCreateBy(this.getCurrentUser().getLoginName());
        abBillRuleBillDtlRmk.setCreateDate(DateUtils.getDate());
        // 保存的时候要把银行类型名称存储下来  ,已在前端增加提交字段，后端无需处理
        abBillRuleBillDtlRmkService.save(abBillRuleBillDtlRmk,request);
        return success();
    }

    @LogMessage(content = "删除 票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/ruleBillDtlRmk/delete/{id}")
    @ResponseBody
    public AjaxJson deleteRuleBillDtlRmk(@PathVariable("id") String id) {
        abBillRuleBillDtlRmkService.deleteRuleBillDtlRmk(id);
        return success();
    }

    @LogMessage(content = "批量删除 票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/ruleBillDtlRmk/batchDelete/{ids}")
    @ResponseBody
    public AjaxJson batchDeleteRuleBillDtlRmk(@PathVariable("ids")String ids) {
        for (String id : ids.split(",")) {
            abBillRuleBillDtlRmkService.deleteRuleBillDtlRmk(id);
        }
        return success();
    }

    /**
     * 票据流水备注规则 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/ruleBillDtlRmk/update")
    public String ruleBillDtlRmkUpdate(AbBillRuleBillDtlRmk abBillRuleBillDtlRmk, Model model) {
        if (StringUtils.isNotEmpty(abBillRuleBillDtlRmk.getId())) {
            abBillRuleBillDtlRmk = abBillRuleBillDtlRmkService.get(abBillRuleBillDtlRmk.getId(),true);
            List<AbBillRuleBillDtlRmkDtlVO> abBillRuleBillDtlRmkDtlVOList =  abBillRuleBillDtlRmkService.getByByBusId(abBillRuleBillDtlRmk.getId());
            int length = abBillRuleBillDtlRmkDtlVOList.size();
            model.addAttribute("abBillRuleBillDtlRmkDtlVOList", abBillRuleBillDtlRmkDtlVOList);
            model.addAttribute("abBillRuleBillDtlRmkPage", abBillRuleBillDtlRmk);
            model.addAttribute("length", length);
        }
        return "bill/accebill/updateBillDtlRmk";
    }

    /**
     * 修改票据流水备注规则
     *
     * @return
     */
    @PostMapping("/ruleBillDtlRmk/modify")
    @ResponseBody
    public AjaxJson updateRuleBillDtlRmk(AbBillRuleBillDtlRmkVO abBillRuleBillDtlRmkVO, HttpServletRequest request) {
        boolean ischeck = abBillRuleBillDtlRmkService.checkAddBillDtlRmk(abBillRuleBillDtlRmkVO,false);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillDtlRmk abBillRuleBillDtlRmk = new AbBillRuleBillDtlRmk();
        BeanUtils.copyNotNullProperties(abBillRuleBillDtlRmkVO, abBillRuleBillDtlRmk);
        abBillRuleBillDtlRmk.setUpdateBy(this.getCurrentUser().getLoginName());
        abBillRuleBillDtlRmk.setUpdateDate(DateUtils.getDate());
        abBillRuleBillDtlRmkService.saveOrUpdate(abBillRuleBillDtlRmk,request);
        return success();
    }

    @LogMessage(content = "批量禁用 票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/ruleBillDtlRmk/disable/{ids}")
    @ResponseBody
    public AjaxJson disable(@PathVariable("ids")String ids) {
        abBillRuleBillDtlRmkService.disable(ids,this.getCurrentUser());
        return success();
    }

}
