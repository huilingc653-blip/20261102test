package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.service.RatCorpService;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatItemCustomDTO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信额度录入表
 * @date 2017-07-21 14:35:43
 */
@Api(tags = "授信额度录入表", value = "/v1-credit/cms/rat/ratbkration")
@RestController("v1RatBkrationController")
@RequestMapping("/v1-credit/cms/rat/ratbkration")
public class RatBkrationController extends BaseController {
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatItemCustomService itemCustomService;
    @DubboReference
    private ICorpService iCorpService;
    @Autowired
    private RatCorpService ratCorpService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @Resource
    private IConService conCommonService;

    @ApiOperation("查询授信额度录入表列表")
    @LogMessage(content = "查询授信额度录入表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public AjaxJson<DataTablesOutput> listdata(Integer start, Integer len, RatBkrationVO ratBkrationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        this.setSearchParams(input, ratBkrationVO);
        List<String> corpIds = null;
        if (StringUtils.isNotEmpty(ratBkrationVO.getCorpId())) {
            corpIds = Arrays.asList(ratBkrationVO.getCorpId().split(","));
        }
        List<RatItemSumVO> ratItemSumList = new ArrayList<>();
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_RATION_QUERY,
                getUserId(), corpIds, ratItemSumList);
        ratBkrationService.checkBkrationInother(output);
        return AjaxJson.success(output);
    }

    private void setSearchParams(DataTablesInput input, RatBkrationVO ratBkrationVO) {
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        if (!StringUtils.isEmpty(ratBkrationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", ratBkrationVO.getContractNo());
        }
        if (null != ratBkrationVO.getRatDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);
        }
        if (null != ratBkrationVO.getBankName()) {
            input.addFilter(MatchTypeEnum.LIKE, "bankName", ratBkrationVO.getBankName(), String.class);
        }
        if (null != ratBkrationVO.getgMoney()) {
            input.getFormdata().put("gMoney", ratBkrationVO.getgMoney());
        }
        if (null != ratBkrationVO.getlMoney()) {
            input.getFormdata().put("lMoney", ratBkrationVO.getlMoney());
        }
        if (null != ratBkrationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(ratBkrationVO.getgBeginDate()));
        }
        if (null != ratBkrationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(ratBkrationVO.getlBeginDate()));
        }
        if (null != ratBkrationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(ratBkrationVO.getgEndDate()));
        }
        if (null != ratBkrationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(ratBkrationVO.getlEndDate()));
        }
        if (null != ratBkrationVO.getgGuaranteeBeginDate()) {
            input.getFormdata().put("ged_guaranteeBeginDate", DateUtils.formatDate(ratBkrationVO.getgGuaranteeBeginDate()));
        }
        if (null != ratBkrationVO.getlGuaranteeBeginDate()) {
            input.getFormdata().put("led_guaranteeBeginDate", DateUtils.formatDate(ratBkrationVO.getlGuaranteeBeginDate()));
        }
        if (null != ratBkrationVO.getgGuaranteeEndDate()) {
            input.getFormdata().put("ged_guaranteeEndDate", DateUtils.formatDate(ratBkrationVO.getgGuaranteeEndDate()));
        }
        if (null != ratBkrationVO.getlGuaranteeEndDate()) {
            input.getFormdata().put("led_guaranteeEndDate", DateUtils.formatDate(ratBkrationVO.getlGuaranteeEndDate()));
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratBkrationVO.getRatCode());
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getUserName())) {
            input.addFilter(MatchTypeEnum.LIKE, "userName", ratBkrationVO.getUserName());
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getItemFlag())) {
            input.getFormdata().put("itemFlag", ratBkrationVO.getItemFlag());
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getItemCode())) {
            input.getFormdata().put("itemCode", ratBkrationVO.getItemCode());
        }
        if (!EmptyUtils.isEmpty(ratBkrationVO.getStatus())) {
            input.addFilter(MatchTypeEnum.EQ, "status", ratBkrationVO.getStatus());
        }
    }

    @ApiOperation("查询授信额度录入表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput v1DataTablesInput) {
        DataTablesInput input = v1DataTablesInput.convertAndInitColumns();
        return AjaxJson.success(ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_RATION_QUERY,
                getUserId()));
    }

    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatBkrationAddInitVO> add(RatBkration ratBkration) {
        RatBkrationAddInitVO result = new RatBkrationAddInitVO();
        if (StringUtils.isNotEmpty(ratBkration.getId())) {
            ratBkration = ratBkrationService.get(ratBkration.getId());
            result.setRatBkrationPage(ratBkration);
        }
        ConfigDTO config = ratBkrationService.getIsOpenRat();//敞口
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            result.setIsOpen("1");
        }

        List<RatItemCustomDTO> list = itemCustomService.getAllItemCustom(RationConstant.RAT_TYPE_0);
        result.setCopmRatSize(list != null && !list.isEmpty() ? list.size() : 0);
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();
        result.setIsHangBail(bailConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();
        result.setIsGuarantee(guaConfig.getValue());
        result.setRelateCon(ratBkrationService.getRelateCon());
        result.setConType(ratBkrationService.getRatConType());
        ConfigDTO creditContractConfig = ratBkrationService.getCreditContractControl();
        result.setCreditContractControl(creditContractConfig.getValue());
        return AjaxJson.success(result);
    }

    @ApiOperation("校验授信合同号是否存在")
    @PostMapping("/checkContractNo")
    public AjaxJson<String> checkContractNo(@RequestParam String contractNo, @RequestParam String ratCode) {
        if (ratBkrationService.checkContractNo(contractNo, ratCode)) {
            return AjaxJson.success(RationConstant.RAT_COMMON_Y);
        }
        return AjaxJson.success(RationConstant.RAT_COMMON_N);
    }

    @ApiOperation("初始化银行授信录入明细")
    @PostMapping("/initRationDtl")
    public AjaxJson<List<RatBkRationDtlVm>> initRationDtl(String ratCode, String itemFlag) {
        List<RatBkRationDtlVm> rationDtls = new ArrayList<>();
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkRationVm bkrationListDTO = ratBkrationService.geBkrationListDTOByRatCode(ratCode, itemFlag);
            if (null != bkrationListDTO.getRationDtls()) {
                rationDtls = bkrationListDTO.getRationDtls();
            }
        }
        return AjaxJson.success(rationDtls);
    }

    @ApiOperation("设置额度明细敞口担保条件页面数据")
    @GetMapping("/setAssure")
    public AjaxJson<RatBkrationSetAssureVO> setAssure(RatBkRationDtlVm RatBkRationDtlVm, String rowIndex) {
        String assurecondition = RatBkRationDtlVm.getAssurecondition();
        RatBkRationDtlVm.setAssurecondition(assurecondition);
        RatBkrationSetAssureVO result = new RatBkrationSetAssureVO();
        result.setRatBkrationDtlPage(RatBkRationDtlVm);
        result.setRowIndex(rowIndex);
        return AjaxJson.success(result);
    }

    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationEditPageVO> updatePage(RatBkRationVm bkrationListDTO) {
        RatBkrationEditPageVO result = new RatBkrationEditPageVO();
        if (StringUtils.isNotEmpty(bkrationListDTO.getId())) {
            bkrationListDTO = ratBkrationService.geBkrationListDTOByRatCode(bkrationListDTO.getId(),
                    RationConstant.RAT_COMFLAG_1);
            result.setRatBkrationPage(bkrationListDTO);
            result.setSingItemSize(bkrationListDTO.getSingItemSize());
            result.setCompItemSize(bkrationListDTO.getCompItemSize());
            result.setItemSize(bkrationListDTO.getItemSize());
        }
        List<RatItemCustomDTO> list = itemCustomService.getAllItemCustom(RationConstant.RAT_TYPE_0);
        result.setCopmRatSize(list != null && !list.isEmpty() ? list.size() : 0);
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();
        if (bailConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            if (bkrationListDTO.getIsHookBond() != null
                    && bkrationListDTO.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y)) {
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(bkrationListDTO.getId(),
                        bkrationListDTO.getRatCode());
                result.setDto(dto);
            }
        }
        result.setIsHangBail(bailConfig.getValue());
        result.setRelateCon(ratBkrationService.getRelateCon());
        result.setConType(ratBkrationService.getRatConType());
        result.setConId(bkrationListDTO.getConId());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();
        result.setIsGuarantee(guaConfig.getValue());
        ConfigDTO creditContractConfig = ratBkrationService.getCreditContractControl();
        result.setCreditContractControl(creditContractConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            result.setIsHangGuarantee(bkrationListDTO.getIsHangGuarantee());
            if (null != bkrationListDTO.getIsHangGuarantee()
                    && bkrationListDTO.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {
                setGuaContractFields(result, bkrationListDTO);
            }
        }
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信额度录入表详情")
    @LogMessage(content = "查询授信额度录入表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatBkrationViewPageVO> view(@PathVariable("id") String id) {
        RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(id);
        if (StringUtils.isNotEmpty(ratBkration.getOldRatCode())) {
            RatBkration oldBkration = ratBkrationService.get(ratBkration.getOldRatCode(), true);
            ratBkration.setOldRatCode(oldBkration.getRatCode());
        }
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();
        RatBkrationViewPageVO result = new RatBkrationViewPageVO();
        result.setRatBkrationPage(ratBkration);
        result.setViewFlag("1");
        result.setIsHangBail(bailConfig.getValue());
        result.setIsGuarantee(guaConfig.getValue());
        result.setRelateCon(ratBkrationService.getRelateCon());
        result.setConType(ratBkrationService.getRatConType());
        result.setConId(ratBkration.getConId());
        return AjaxJson.success(result);
    }

    @ApiOperation("校验授信时间区间")
    @LogMessage(content = "校验授信时间区间", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/checkRation")
    public AjaxJson checkRation(@RequestBody RatBkRationVm rationtDTO) {
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        ratBkrationService.checkBkration(rationtDTO);
        return success();
    }

    @ApiOperation("添加授信额度录入表")
    @LogMessage(content = "添加授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkRationVm rationtDTO) {
        ratBkrationService.validata(rationtDTO);
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        rationtDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationService.saveRatBkration(rationtDTO, getCurrentUser());
        return success();
    }

    @ApiOperation("提交授信额度录入表")
    @LogMessage(content = "提交授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatBkRationVm rationVm) {
        ratBkrationService.validata(rationVm);
        ratBkrationService.setRatBkrationDtls(rationVm);
        rationVm.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationService.saveRatBkration(rationVm, getCurrentUser());
        return success();
    }

    @ApiOperation("修改暂存授信额度录入表")
    @LogMessage(content = "修改授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatBkRationVm rationtDTO) {
        ratBkrationService.validata(rationtDTO);
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        rationtDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationService.updateRatBkration(rationtDTO, getCurrentUser());
        return success();
    }

    @ApiOperation("修改提交授信额度录入表")
    @LogMessage(content = "修改授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatBkRationVm rationtDTO) {
        ratBkrationService.validata(rationtDTO);
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        rationtDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationService.updateRatBkration(rationtDTO, getCurrentUser());
        return success();
    }

    private void setRatBkrationDtls(RatBkRationVm rationtDTO) {
        List<RatBkRationDtlVm> dtls = new ArrayList<>();
        List<RatBkRationDtlVm> rationDtls = rationtDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<>();
        }
        if (EmptyUtils.isEmpty(rationtDTO.getIncludeBailmoney())) {
            rationtDTO.setIncludeBailmoney(RationConstant.RAT_COMMON_Y);
        }
        BigDecimal money = null;
        BigDecimal openMoney = null;
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO && !StringUtils.isEmpty(dtlDTO.getItemFlag())) {
                dtls.add(dtlDTO);
            }
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                if (null != dtlDTO.getMoney()) {
                    money = dtlDTO.getMoney();
                    dtlDTO.setCompSign(RationConstant.RAT_COMP_SIGN_1);
                }
                if (null != dtlDTO.getOpenMoney()) {
                    openMoney = dtlDTO.getOpenMoney();
                }
            }
            if (null == dtlDTO.getMoney()) {
                dtlDTO.setMoney(BigDecimal.ZERO);
            }
            if (null == dtlDTO.getOpenMoney()) {
                dtlDTO.setOpenMoney(dtlDTO.getMoney());
            }
        }
        Collections.sort(dtls, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        Integer index = 1;
        for (RatBkRationDtlVm dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
            if (StringUtils.isEmpty(dtlDTO.getCompSign())) {
                dtlDTO.setCompSign(RationConstant.RAT_COMP_SIGN_0);
            }
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
                if (null == dtlDTO.getOpenMoney()) {
                    dtlDTO.setOpenMoney(BigDecimal.ZERO);
                }
            }
            if (null == dtlDTO.getOpenMoney()) {
                dtlDTO.setOpenMoney(dtlDTO.getMoney());
            }
        }
        rationtDTO.setRationDtls(dtls);
    }

    @ApiOperation("修改授信额度录入表")
    @LogMessage(content = "修改授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatBkration ratBkration) {
        RatBkration t = ratBkrationService.get(ratBkration.getId());
        BeanUtils.copyNotNullProperties(ratBkration, t);
        ratBkrationService.saveOrUpdate(t);
        return success();
    }

    @ApiOperation("删除授信额度录入表")
    @LogMessage(content = "删除授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkration ratBkration = ratBkrationService.get(id);
        ratBkration.setStatus(RationConstant.STATUS_DELETE);
        ratBkrationService.updateEntity(ratBkration);
        if (ratBkration.getIsHookBond() != null) {
            if (ratBkration.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y)) {
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                        ratBkration.getRatCode());
                dto.setStatus(FcBondConstant.STATUE_RELEASE);
                dto.setUpdateBy(getUserLoginName());
                dto.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(dto);
            }
        }
        String relateCon = ratBkrationService.getRelateCon().getValue();
        if (RationConstant.RAT_COMMON_Y.equals(relateCon)) {
            if (StringUtils.isNotEmpty(ratBkration.getConId())) {
                conCommonService.unHookConOp(ratBkration.getConId(), ratBkration.getId(), getCurrentUser());
            }
        }
        return success();
    }

    @ApiOperation("批量删除授信额度录入表")
    @LogMessage(content = "批量删除授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        for (String id : ids.split(",")) {
            RatBkration ratBkration = ratBkrationService.get(id);
            ratBkration.setStatus(RationConstant.STATUS_DELETE);
            ratBkrationService.updateEntity(ratBkration);
        }
        return success();
    }

    @ApiOperation("单位授信主体设置页面数据")
    @LogMessage(content = "单位授信主体设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryCorp")
    public AjaxJson<RatBkrationQueryCorpVO> queryCorp(Integer type, CorpDTO corpDTO) {
        String code = corpDTO.getCode();
        List<CorpDTO> list = iCorpService.listEffectiveCorp();
        RatBkrationQueryCorpVO result = new RatBkrationQueryCorpVO();
        result.setCode(code);
        result.setCorpDTO(getCorpIdInfo(list, code));
        return AjaxJson.success(result);
    }

    public List<CorpDTO> getCorpIdInfo(List<CorpDTO> corpList, String code) {
        List<CorpDTO> list = new ArrayList<>();
        List<CorpDTO> corps = iCorpService.listCorpTree(corpList);
        for (CorpDTO dto : corps) {
            boolean b = corps.stream().filter(item -> dto.getId().equals(item.getParentId())).findAny().isPresent();
            if (b) {
                dto.setType(0);
            }
        }
        if (StringUtils.isEmpty(code)) {
            return corps;
        }
        for (CorpDTO dto : corps) {
            if (!StringUtils.isEmpty(dto.getCode())) {
                if (dto.getCode().contains(code)) {
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @ApiOperation("修改单位授信主体设置")
    @LogMessage(content = "修改单位授信主体设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveRatCorp")
    public AjaxJson saveRatCorp(@RequestBody Map<String, List<String>> body) {
        ratCorpService.updateRatGroup(body.get("corpIds"));
        return success();
    }

    @ApiOperation("获取有效单位列表")
    @GetMapping("/getlistEffectiveCorp")
    public AjaxJson getlistEffectiveCorp() {
        return AjaxJson.success(iCorpService.listEffectiveCorp());
    }

    @ApiOperation("查询授信录入发放审批列表")
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public AjaxJson<RatBkrationApproveInitVO> listApprove(String flowStatus, String cycleTime) {
        RatBkrationApproveInitVO result = new RatBkrationApproveInitVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信录入审批数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput v1Input) {
        DataTablesInput input = v1Input.convertAndInitColumns();
        Object beginDate = input.getFormdata().get("beginDate");
        if (!EmptyUtils.isEmpty(beginDate)) {
            input.addFilter(MatchTypeEnum.LE, "sDate", beginDate.toString(), Date.class);
            input.addFilter(MatchTypeEnum.GE, "eDate", beginDate.toString(), Date.class);
        }
        return AjaxJson.success(ratBkrationService.listApproveOutputData(input));
    }

    @ApiOperation("查询授信录入审批详情")
    @GetMapping("/viewBkrationApprove")
    public AjaxJson<RatBkrationApproveDetailVO> viewBkrationApprove(String businessId, String taskId) {
        RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(businessId);
        if (StringUtils.isNotEmpty(ratBkration.getOldRatCode())) {
            RatBkration oldBkration = ratBkrationService.get(ratBkration.getOldRatCode(), true);
            ratBkration.setOldRatCode(oldBkration.getRatCode());
        }
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();
        RatBkrationApproveDetailVO result = new RatBkrationApproveDetailVO();
        result.setRatBkrationPage(ratBkration);
        result.setTaskId(taskId);
        result.setIsHangBail(bailConfig.getValue());
        result.setIsGuarantee(guaConfig.getValue());
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(), ratBkration.getRatCode());
        if (dto != null) {
            List<FcBondApplyDTO> listFc = fcBondHookService.getFcBondApplyListByItemId(ratBkration.getId());
            BigDecimal addMoney = new BigDecimal(0);
            if (listFc != null) {
                for (FcBondApplyDTO dto1 : listFc) {
                    if (dto1.getApplyMoney() != null && dto1.getStatus().equals(FcBondConstant.STATUE_APPLY)
                            && !(dto1.getItemNo().equals(dto.getItemNo()))) {
                        addMoney = addMoney.add(dto1.getApplyMoney());
                    }
                }
            }
            result.setDto(dto);
            result.setAddMoney(addMoney);
        }
        result.setRelateCon(ratBkrationService.getRelateCon());
        result.setConType(ratBkrationService.getRatConType());
        result.setConId(ratBkration.getConId());
        return AjaxJson.success(result);
    }

    @ApiOperation("根据分配类型获取单位的直接下属单位")
    @GetMapping("/listCorp")
    public AjaxJson<List<CorpDTO>> listCorp(String ratModel) {
        List<CorpDTO> corpDTOs;
        if (RationConstant.RATMODE_1.equals(ratModel)) {
            corpDTOs = iCorpService.listUserCorp(RationConstant.CMS_RATION_RATION_APPLY, getUserId());
        } else {
            corpDTOs = iCorpService.getRatGroupCorpNet(RationConstant.CMS_RATION_RATION_APPLY, getUserId());
        }
        return AjaxJson.success(TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                if (o1.getSort().compareTo(o2.getSort()) > 0) {
                    return 1;
                } else if (o1.getSort().compareTo(o2.getSort()) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }));
    }

    @ApiOperation("授信录入手机审批详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatBkrationMobileApproveVO> mobileBkrationApprove(String businessId, String taskId) {
        RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(businessId);
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();
        RatBkrationMobileApproveVO result = new RatBkrationMobileApproveVO();
        result.setRatBkrationPage(ratBkration);
        result.setTaskId(taskId);
        result.setIsGuarantee(guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            if (ratBkration.getIsHangGuarantee() != null
                    && ratBkration.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {
                GuaContractUseDTO guaContractUseDTO = contractOccupServie.getGuaContractOccupyDtl(ratBkration.getId(),
                        ratBkration.getRatCode());
                result.setGuaContractUseDTO(guaContractUseDTO);
            }
        }
        return AjaxJson.success(result);
    }

    private void setGuaContractFields(RatBkrationEditPageVO result, RatBkRationVm bkrationListDTO) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(bkrationListDTO.getId());
        guaContractOccupDTO.setStatus(GuaranteeConstant.GUA_CONTRACT_TEMP);
        GuaContractOccupDTO guaContractOccup = contractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
        List<String> idList = guaContractOccup.getContractIds();
        String contractIds = "";
        for (String id : idList) {
            contractIds += id + ",";
        }
        result.setContractIds(contractIds);
        result.setIdList(idList);
        result.setContractCode(guaContractOccup.getContractCode());
        result.setGuaWayNames(guaContractOccup.getGuaWayName());
        result.setGuaWay(guaContractOccup.getGuaWay());
        result.setGuaranteeRmk(guaContractOccup.getGuaRmk());
    }

    @ApiOperation("获取银行授信录入审批通过的数据")
    @PostMapping("/ratBkData")
    public AjaxJson<DataTablesOutput> ratBkData(@RequestBody V1DataTablesInput v1Input) {
        DataTablesInput input = v1Input.convertAndInitColumns();
        return AjaxJson.success(ratBkrationService.ratBkData(input));
    }

    @ApiOperation("获取授信额度录入列表")
    @GetMapping("/json")
    public AjaxJson<List<RatBkRationVm>> json(RatBkRationVm uration) {
        return AjaxJson.success(ratBkrationService.queryRationAll(uration));
    }

    @ApiOperation("导入授信额度录入数据")
    @LogMessage(content = "导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<RatBkrationVO> list) {
        ratBkrationService.dealImportData(list, this.getCurrentUser());
        return success();
    }

    @ApiOperation("获取挂接担保合同变更的授信数据")
    @PostMapping("/getGuaContract")
    public AjaxJson<DataTablesOutput<GuaContractOccupVO>> getUseChangelist(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(ratBkrationService.getGuaForRat(input, getCurrentUser()));
    }

    @ApiOperation("银行授信额度录入导出")
    @LogMessage(content = "银行授信额度录入导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkration")
    public void exportListRatBkration(HttpServletRequest request, HttpServletResponse response,
                                      RatBkrationVO vo) throws Exception {
        String fileName = "银行授信额度录入表";
        // 统一URL编码，同时支持filename 和 filename*，适配你的前端正则
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());

        // =========修改重点1：删掉 response.setCharacterEncoding("utf-8"); 二进制xls不需要！=========
        // =========修改重点2：ContentType改成xls标准MIME，不要application/force-download=========
        response.setContentType("application/vnd.ms-excel");
        // 双header：filename给旧浏览器，filename*=UTF-8''给现代浏览器（正好匹配你前端正则）
        response.setHeader("content-disposition",
                "attachment;filename=" + encodedFileName + ".xls;filename*=UTF-8''" + encodedFileName + ".xls");

        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(vo.getExportType())) {
            if (null == vo.getStart()) {
                vo.setStart(1);
            }
            if (null == vo.getPageSize()) {
                vo.setPageSize(10);
            }
            input.setStart((vo.getStart() - 1) * vo.getPageSize());
            input.setLength(vo.getPageSize());
        } else {
            input.setStart(1);
            input.setLength(99999999);
        }
        this.setSearchParams(input, vo);
        List<String> corpIds = null;
        if (StringUtils.isNotBlank(vo.getCorpId())) {
            corpIds = Arrays.asList(vo.getCorpId().split(","));
        }
        List<RatItemSumVO> ratItemSumList = new ArrayList<>();
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_RATION_QUERY,
                getUserId(), corpIds, ratItemSumList);
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度录入表",
                CommonConstant.YES, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);

        // 输出流，header必须在write之前（现在顺序没问题）
        try (OutputStream ouputStream = response.getOutputStream()) {
            workbook.write(ouputStream);
            ouputStream.flush();
        } finally {
            workbook.close();
        }
    }

}
