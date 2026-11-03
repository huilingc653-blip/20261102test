package com.bytter.bill.web.saas.controller.bill;

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
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 承兑汇票系统票据认领参数配置
 * @date 2022-03-01 15:25:10
 */
@Api(value = "/v1-preposebill/bill/accebillClaim/config", tags = "承兑汇票系统票据认领参数配置")
@RestController("v1AccebillClaimConfigController")
@RequestMapping("/v1-preposebill/bill/accebillClaim/config")
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
     * 查询承兑汇票系统日期参数配置
     */
    @GetMapping("/dateConfig")
    @ApiOperation("查询承兑汇票系统日期参数配置")
    public AjaxJson<Map<String, Object>> dateConfig() {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询承兑汇票系统金额参数配置
     */
    @GetMapping("/moneyConfig")
    @ApiOperation("查询承兑汇票系统金额参数配置")
    public AjaxJson<Map<String, Object>> moneyConfig() {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询认领参数配置
     */
    @GetMapping("/claimParam")
    @ApiOperation("查询认领参数配置")
    public AjaxJson<Map<String, Object>> claimParam() {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询承兑汇票系统账号规则配置
     */
    @GetMapping("/accountConfig")
    @ApiOperation("查询承兑汇票系统账号规则配置")
    public AjaxJson<Map<String, Object>> accountConfig() {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return AjaxJson.success(dataMap);
    }

    /**
     * 承兑汇票系统账号规则配置 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询账号规则配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/accountConfig/datatable")
    @ApiOperation("查询账号规则配置分页列表")
    public AjaxJson<DataTablesOutput> accountConfigDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimAccountService.listAbClaimAccountInfo(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询账号规则配置详情
     */
    @LogMessage(content = "查询账号规则配置详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/accountConfig/{id}")
    @ApiOperation("查询账号规则配置详情")
    public AjaxJson<AbClaimAccountVo> getAccountConfig(@PathVariable("id") String id) {
        AbClaimAccountVo abClaimAccountVo = abClaimAccountService.getByIdUnion(id);
        return AjaxJson.success(abClaimAccountVo);
    }

    /**
     * 添加承兑汇票系统账号规则配置
     */
    @LogMessage(content = "添加承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/accountConfig/save")
    @ApiOperation("添加承兑汇票系统账号规则配置")
    public AjaxJson saveAccountConfig(@RequestBody AbClaimAccountVo abClaimAccountVo) {
        if (EmptyUtils.isBlank(abClaimAccountVo.getCreateBy())) {
            abClaimAccountVo.setCreateBy(this.getCurrentUser().getLoginName());
            abClaimAccountVo.setCreateDate(DateUtils.getDate());
        }
        // 检验单位+账号+业务类型不允许重复
        boolean ischeck = abClaimAccountService.checkAddRepeat(abClaimAccountVo);
        if (!ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_REPEAT);
        }
        AbClaimAccount abClaimAccount = new AbClaimAccount();
        BeanUtils.copyNotNullProperties(abClaimAccountVo, abClaimAccount);
        abClaimAccount.setBankTypeName(abClaimAccountVo.getBankTypeName());
        abClaimAccount.setCorpName(abClaimAccountVo.getCorpName());
        abClaimAccountService.saveOrUpdate(abClaimAccount);
        return AjaxJson.success();
    }

    /**
     * 修改承兑汇票系统账号规则配置
     */
    @LogMessage(content = "修改承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/accountConfig/modify")
    @ApiOperation("修改承兑汇票系统账号规则配置")
    public AjaxJson updateAccountConfig(@RequestBody AbClaimAccountVo abClaimAccountVo) {
        // 检验单位+账号+业务类型不允许重复
        boolean ischeck = abClaimAccountService.checkAddRepeat(abClaimAccountVo);
        if (!ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_REPEAT);
        }
        AbClaimAccount t = abClaimAccountService.get(abClaimAccountVo.getId());
        BeanUtils.copyNotNullProperties(abClaimAccountVo, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        abClaimAccountService.saveOrUpdate(t);
        return AjaxJson.success();
    }

    /**
     * 删除承兑汇票系统账号规则配置
     */
    @LogMessage(content = "删除承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/accountConfig/delete/{id}")
    @ApiOperation("删除承兑汇票系统账号规则配置")
    public AjaxJson deleteAccountConfig(@PathVariable("id") String id) {
        abClaimAccountService.deleteAbClaimAccount(id);
        return AjaxJson.success();
    }

    /**
     * 批量删除承兑汇票系统账号规则
     */
    @LogMessage(content = "批量删除承兑汇票系统账号规则配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/accountConfig/batchDelete/{ids}")
    @ApiOperation("批量删除承兑汇票系统账号规则配置")
    public AjaxJson batchDeleteAccountConfig(@PathVariable("ids") String ids) {
        for (String id : ids.split(",")) {
            abClaimAccountService.deleteAbClaimAccount(id);
        }
        return AjaxJson.success();
    }

    /**
     * 票号规则 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票号规则列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/ruleBillNo/datatable")
    @ApiOperation("查询票号规则分页列表")
    public AjaxJson<DataTablesOutput> ruleBillNoDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillRuleBillNoService.getBillNOList(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询票号规则详情
     */
    @LogMessage(content = "查询票号规则详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/ruleBillNo/{id}")
    @ApiOperation("查询票号规则详情")
    public AjaxJson<AbBillRuleBillNoVO> getRuleBillNo(@PathVariable("id") String id) {
        AbBillRuleBillNo abBillRuleBillNo = abBillRuleBillNoService.get(id);
        AbBillRuleBillNoVO abBillRuleBillNoVO = new AbBillRuleBillNoVO();
        BeanUtil.copyProperties(abBillRuleBillNo, abBillRuleBillNoVO);
        if (StringUtils.isNotEmpty(abBillRuleBillNoVO.getBankTypeId())) {
            abBillRuleBillNoVO.setBankTypeName(iBankTypeService.getObjectById(abBillRuleBillNoVO.getBankTypeId()).getTypeName());
        }
        return AjaxJson.success(abBillRuleBillNoVO);
    }

    /**
     * 添加票号规则
     */
    @LogMessage(content = "添加票号规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/ruleBillNo/save")
    @ApiOperation("添加票号规则")
    public AjaxJson saveRuleBillNo(@RequestBody AbBillRuleBillNoVO abBillRuleBillNoVO) {
        boolean ischeck = abBillRuleBillNoService.checkAddBillNO(abBillRuleBillNoVO);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillNo abBillRuleBillNo = new AbBillRuleBillNo();
        BeanUtils.copyNotNullProperties(abBillRuleBillNoVO, abBillRuleBillNo);
        abBillRuleBillNoService.save(abBillRuleBillNo);
        return AjaxJson.success();
    }

    /**
     * 修改票号规则
     */
    @LogMessage(content = "修改票号规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/ruleBillNo/modify")
    @ApiOperation("修改票号规则")
    public AjaxJson updateRuleBillNo(@RequestBody AbBillRuleBillNoVO abBillRuleBillNoVO) {
        boolean ischeck = abBillRuleBillNoService.checkupdateBillNO(abBillRuleBillNoVO);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillNo abBillRuleBillNo = new AbBillRuleBillNo();
        BeanUtils.copyNotNullProperties(abBillRuleBillNoVO, abBillRuleBillNo);
        abBillRuleBillNoService.saveOrUpdate(abBillRuleBillNo);
        return AjaxJson.success();
    }

    /**
     * 删除票号信息
     */
    @LogMessage(content = "删除票号规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/ruleBillNo/delete/{id}")
    @ApiOperation("删除票号规则")
    public AjaxJson deleteRuleBillNo(@PathVariable("id") String id) {
        abBillRuleBillNoService.delete(id);
        return AjaxJson.success();
    }

    /**
     * 票据流水备注规则 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据流水备注规则列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/ruleBillDtlRmk/datatable")
    @ApiOperation("查询票据流水备注规则分页列表")
    public AjaxJson<DataTablesOutput> ruleBillDtlRmkDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillRuleBillDtlRmkService.getBillRuleList(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询票据流水备注规则详情
     */
    @LogMessage(content = "查询票据流水备注规则详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/ruleBillDtlRmk/{id}")
    @ApiOperation("查询票据流水备注规则详情")
    public AjaxJson<Map<String, Object>> getRuleBillDtlRmk(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>(4);
        AbBillRuleBillDtlRmk abBillRuleBillDtlRmk = abBillRuleBillDtlRmkService.get(id, true);
        List<AbBillRuleBillDtlRmkDtlVO> abBillRuleBillDtlRmkDtlVOList = abBillRuleBillDtlRmkService.getByByBusId(id);
        dataMap.put("rule", abBillRuleBillDtlRmk);
        dataMap.put("detailList", abBillRuleBillDtlRmkDtlVOList);
        dataMap.put("detailCount", abBillRuleBillDtlRmkDtlVOList.size());
        return AjaxJson.success(dataMap);
    }

    /**
     * 添加票据流水备注规则
     */
    @LogMessage(content = "添加票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/ruleBillDtlRmk/save")
    @ApiOperation("添加票据流水备注规则")
    public AjaxJson saveRuleBillDtlRmk(@RequestBody AbBillRuleBillDtlRmkVO abBillRuleBillDtlRmkVO) {
        boolean ischeck = abBillRuleBillDtlRmkService.checkAddBillDtlRmk(abBillRuleBillDtlRmkVO, true);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillDtlRmk abBillRuleBillDtlRmk = new AbBillRuleBillDtlRmk();
        BeanUtils.copyNotNullProperties(abBillRuleBillDtlRmkVO, abBillRuleBillDtlRmk);
        abBillRuleBillDtlRmk.setCreateBy(this.getCurrentUser().getLoginName());
        abBillRuleBillDtlRmk.setCreateDate(DateUtils.getDate());
        abBillRuleBillDtlRmkService.save(abBillRuleBillDtlRmk, abBillRuleBillDtlRmkVO.getRuleDtl());
        return AjaxJson.success();
    }

    /**
     * 修改票据流水备注规则
     */
    @LogMessage(content = "修改票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/ruleBillDtlRmk/modify")
    @ApiOperation("修改票据流水备注规则")
    public AjaxJson updateRuleBillDtlRmk(@RequestBody AbBillRuleBillDtlRmkVO abBillRuleBillDtlRmkVO) {
        boolean ischeck = abBillRuleBillDtlRmkService.checkAddBillDtlRmk(abBillRuleBillDtlRmkVO, false);
        if (ischeck) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO);
        }
        AbBillRuleBillDtlRmk abBillRuleBillDtlRmk = new AbBillRuleBillDtlRmk();
        BeanUtils.copyNotNullProperties(abBillRuleBillDtlRmkVO, abBillRuleBillDtlRmk);
        abBillRuleBillDtlRmk.setUpdateBy(this.getCurrentUser().getLoginName());
        abBillRuleBillDtlRmk.setUpdateDate(DateUtils.getDate());
        abBillRuleBillDtlRmkService.saveOrUpdate(abBillRuleBillDtlRmk, abBillRuleBillDtlRmkVO.getRuleDtl());
        return AjaxJson.success();
    }

    /**
     * 删除票据流水备注规则
     */
    @LogMessage(content = "删除票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/ruleBillDtlRmk/delete/{id}")
    @ApiOperation("删除票据流水备注规则")
    public AjaxJson deleteRuleBillDtlRmk(@PathVariable("id") String id) {
        abBillRuleBillDtlRmkService.deleteRuleBillDtlRmk(id);
        return AjaxJson.success();
    }

    /**
     * 批量删除票据流水备注规则
     */
    @LogMessage(content = "批量删除票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/ruleBillDtlRmk/batchDelete/{ids}")
    @ApiOperation("批量删除票据流水备注规则")
    public AjaxJson batchDeleteRuleBillDtlRmk(@PathVariable("ids") String ids) {
        for (String id : ids.split(",")) {
            abBillRuleBillDtlRmkService.deleteRuleBillDtlRmk(id);
        }
        return AjaxJson.success();
    }

    /**
     * 批量禁用票据流水备注规则
     */
    @LogMessage(content = "批量禁用票据流水备注规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/ruleBillDtlRmk/disable/{ids}")
    @ApiOperation("批量禁用票据流水备注规则")
    public AjaxJson disableRuleBillDtlRmk(@PathVariable("ids") String ids) {
        abBillRuleBillDtlRmkService.disable(ids, this.getCurrentUser());
        return AjaxJson.success();
    }

}
