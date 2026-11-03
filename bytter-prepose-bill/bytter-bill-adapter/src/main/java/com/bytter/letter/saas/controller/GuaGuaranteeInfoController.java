package com.bytter.letter.saas.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.velocity.tags.FcBondTag;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.dto.GuaGuaranteeChangeSubmitDTO;
import com.bytter.letter.domain.dto.GuaGuaranteeInfoSubmitDTO;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.service.GuaPerformRegService;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoViewVO;
import com.bytter.letter.domain.vo.GuaPerformRegVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeInfoController
 * @Description:保函申请控制层
 * @date 2019年1月7日 上午9:37:01
 */
@ApiOperation(value = "/v1-guarantee/letter/guarantee/guaGuaranteeInfo",tags = "保函申请控制层")
@RestController("v1GuaGuaranteeInfoController")
@RequestMapping("/v1-guarantee/letter/guarantee/guaGuaranteeInfo")
public class GuaGuaranteeInfoController extends BaseController {
    @Autowired
    private GuaGuaranteeInfoService guaLgInfoService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference
    private IInputBankInfoService inputBankInfoService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IBtCurrencyService btCurrencyService;
    @DubboReference
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference
    private IFcBondHookService fcBondHookService;
    @Autowired
    private GuaPerformRegService performRegService;

    /**
     * 保函申请列表数据
     */
    @LogMessage(content = "查询保函申请列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/LgDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> LgDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(guaLgInfoService.LgDatatable(input, this.getCurrentUser()));
    }

    /**
     * 新增保函申请
     */
    @LogMessage(content = "跳转到新增保函申请页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public AjaxJson<GuaGuaranteeInfoViewVO> add() {
        GuaGuaranteeInfoViewVO map = new GuaGuaranteeInfoViewVO();
        getParamsConfig(map);// 获取保函基础参数
        return AjaxJson.success(map);
    }

    /**
     * 保存保函申请
     */
    @LogMessage(content = "保存保函申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(@RequestBody GuaGuaranteeInfoSubmitDTO request) {
        GuaGuaranteeInfoVO LgInfoVo = request.getLgInfoVo();
        RatUrationDTO urationDTO = request.getUrationDTO();
        guaLgInfoService.tempOrSubmit(LgInfoVo, urationDTO, this.getCurrentUser());
        return success();
    }

    /**
     * 保函申请修改
     */
    @LogMessage(content = "跳转到保函申请修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update/{id}")
    public AjaxJson<GuaGuaranteeInfoViewVO> update(@PathVariable("id") String id) {
        GuaGuaranteeInfoViewVO model = new GuaGuaranteeInfoViewVO();
        if (StringUtils.isNotEmpty("id")) {
            // GuaGuaranteeInfoVO LgInfoVo=guaLgInfoService.getGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfoVo = guaLgInfoService.get(id);
            model.setLgInfoVo(LgInfoVo);
            InputBankInfoDTO bankInfo = inputBankInfoService.getByBankCodeAndImport(LgInfoVo.getOpenCorp());// 查询银行
            model.setBankName(bankInfo.getBankName());
            getParamsConfig(model);// 获取保函基础参数
            CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
            model.setStandCur(btCurrencyService.getObjectById(corpDTO.getCurId()));
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                setBondMsg(LgInfoVo, model);
            }
            if (LgInfoVo.getRatmodel() != null && RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {// 挂接授信
                setRatMsg(LgInfoVo, model);
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 查看保函申请详情
     */
    @LogMessage(content = "跳转到保函申请查看页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<GuaGuaranteeInfoViewVO> view(@PathVariable("id") String id) {
        GuaGuaranteeInfoViewVO model = new GuaGuaranteeInfoViewVO();
        if (StringUtils.isNotEmpty("id")) {
            // GuaGuaranteeInfoVO LgInfoVo=guaLgInfoService.getGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfo = guaLgInfoService.get(id);
            GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
            BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
            setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
            model.setLgInfoVo(LgInfoVo);
            getParamsConfig(model);// 获取保函基础参数
            CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
            model.setStandCur(btCurrencyService.getObjectById(corpDTO.getCurId()));
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
                BtCurrencyDTO guaCurDTO = btCurrencyService.getObjectById(LgInfoVo.getCurId());
                model.setNaturalSign(guaCurDTO.getNaturalSign());// 本位币标识
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id);
                model.setUrationDTO(urationDTO);// 授信明细
                model.setRatBankCode(urationDTO.getBankCode());// 授信银行
            }
            //保证金相关
            if (StringUtils.isNotBlank(LgInfoVo.getIsHookBond()) && "1".equals(LgInfoVo.getIsHookBond())) {
                FcBondTag fcBondTag = new FcBondTag();
                model.setFcBondTag(fcBondTag);
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 删除保函申请
     */
    @LogMessage(content = "删除保函申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaLgInfoService.delete(id, this.getCurrentUser());
        return success();
    }

    /**
     * 设置保函申请其他信息
     *
     * @param LgInfoVo
     *
     */
    public void setGuaLgInfo(GuaGuaranteeInfoVO LgInfoVo) {
        CorpDTO corp = corpService.get(LgInfoVo.getCorpId());
        LgInfoVo.setCorpName(corp.getName());// 设置保函申请人名称
        BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(LgInfoVo.getCurId());// 设置币别
        LgInfoVo.setCurName(currencyDTO.getCurName());
        InputBankInfoDTO bankInfo = inputBankInfoService.getByBankCodeAndImport(LgInfoVo.getOpenCorp());// 查询银行
        if (bankInfo != null) {
            LgInfoVo.setOpenCorpName(bankInfo.getBankName());
        }
        if (StringUtils.isNotBlank(LgInfoVo.getGuaranteeedCorp())) {
            // 被担保单位可能为多个，以英文逗号分隔，需按ID集合查询，避免主键查询查不到而空指针
            List<CorpDTO> guaranteeedCorps = corpService
                    .listCorpDTOByCorpIds2(Arrays.asList(LgInfoVo.getGuaranteeedCorp().split(",")));
            if (EmptyUtils.isNotEmpty(guaranteeedCorps)) {
                LgInfoVo.setGuaranteeedCorpShow(guaranteeedCorps.stream()
                        .map(item -> "(" + item.getCode() + ")" + item.getName())
                        .collect(Collectors.joining()));
            }
        }
    }

    /**
     * 获取被担保单位
     */
    @PostMapping("/listGuaranteedCorp")
    @ResponseBody
    public AjaxJson<List<CorpDTO>> listCorp(String corps) {
        return listGuaranteedCorp(corps);
    }

    @GetMapping("/listGuaranteedCorp")
    @ResponseBody
    public AjaxJson<List<CorpDTO>> listGuaranteedCorp(String corps) {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        if (corps != null) {// 设置被担保单位
            String[] corpIds = corps.split(",");
            List<String> corpList = new ArrayList<String>(corpIds.length);
            for (String s : corpIds) {
                corpList.add(s);
            }
            corpDTOs = corpService.listCorpDTOByCorpIds2(corpList);
        }
        return AjaxJson.success(TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        }));
    }

    /**
     * 检查保函编号是否重复
     *
     * @return true-重复 false-不重复
     */
    @LogMessage(content = "检查保函编号是否重复", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/checkLgNo")
    @ResponseBody
    public AjaxJson<Boolean> checkLgNo(String guaranteeNo) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.success(true);
        List<GuaGuaranteeInfoVO> LgList = guaLgInfoService.queryLgByNo(guaranteeNo);
        if (LgList.size() > 0) {
            ajaxJson.setObj(true);
            return ajaxJson;
        }
        ajaxJson.setObj(false);
        return ajaxJson;
    }

    /**
     * 查询审批列表数据
     */
    @LogMessage(content = "查询保函申请待审批列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/approveDataList")
    @ResponseBody
    public AjaxJson<DataTablesOutput> approveDataList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(guaLgInfoService.getApproveDataTables(input, this.getCurrentUser()));
    }

    /**
     * 审批详情
     */
    @LogMessage(content = "跳转到保函申请待审批详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewApprove")
    public AjaxJson<GuaGuaranteeInfoViewVO> viewApprove(String id, String taskId) {
        GuaGuaranteeInfoViewVO model = new GuaGuaranteeInfoViewVO();
        GuaGuaranteeInfo LgInfo = guaLgInfoService.get(id);
        GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
        BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
        setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
        model.setLgInfoVo(LgInfoVo);
        getParamsConfig(model);// 获取保函基础参数
        CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
        model.setStandCur(btCurrencyService.getObjectById(corpDTO.getCurId()));
        // 挂接的授信信息
        if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
            RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, LgInfoVo.getId());
            model.setUrationDTO(urationDTO);// 授信明细
            model.setRatBankCode(urationDTO.getBankCode());// 授信银行
        }
        model.setTaskId(taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }


    /**
     * 保函到期处理列表数据
     */
    @LogMessage(content = "查询保函到期处理列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/logoutDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> logoutDatatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput tablesInput = input.convertAndInitColumns();
        return success(guaLgInfoService.logoutDatatable(tablesInput, this.getCurrentUser()));
    }

    /**
     * 保函到期处理页面
     *
     * @param id
     * @return
     */
    @LogMessage(content = "跳转到保函到期处理页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/logout/{id}")
    public AjaxJson<GuaGuaranteeInfoViewVO> logoutGuaGuarantee(@PathVariable("id") String id) {
        GuaGuaranteeInfoViewVO model = new GuaGuaranteeInfoViewVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeInfoVO info = guaLgInfoService.getGuaranteeInfoById(id);
            model.setLgInfoVo(info);
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(info.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id);
                model.setUrationDTO(urationDTO);// 授信明细
                model.setRatBankCode(urationDTO.getBankCode());// 授信银行
            }
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.setIsHangBail(isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 保函注销处理
     */
    @LogMessage(content = "执行保函注销操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/logout")
    @ResponseBody
    public AjaxJson logout(@RequestBody GuaGuaranteeInfoVO LgInfoVo) {
        guaLgInfoService.logout(LgInfoVo, this.getCurrentUser());
        return success();
    }


    /**
     * 保函查询列表数据
     */
    @LogMessage(content = "查询保函相关数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @SuppressWarnings("rawtypes")
    @PostMapping("/loadAllDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> loadAllDatatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput tablesInput = input.convertAndInitColumns();
        return success(guaLgInfoService.loadAllDatatable(tablesInput, this.getCurrentUser()));

    }

    /**
     * 查看保函申请详情
     */
    @LogMessage(content = "查看保函申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/viewDtl/{id}")
    public AjaxJson<GuaGuaranteeInfoViewVO> viewDtl(@PathVariable("id") String id) {
        GuaGuaranteeInfoViewVO model = new GuaGuaranteeInfoViewVO();
        if (StringUtils.isNotEmpty("id")) {
            // GuaGuaranteeInfoVO LgInfoVo=guaLgInfoService.getGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfo = guaLgInfoService.get(id);
            GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
            BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
            setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
            model.setLgInfoVo(LgInfoVo);
            // 保函履约信息
            GuaPerformRegVO guaPerformRegVO = performRegService.getRegVOById(id);
            model.setGuaPerformRegVO(guaPerformRegVO);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.setIsHangBail(isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
            ConfigDTO openRatConfig = guaLgInfoService.getIsStartOpenRat();// 参数设置是否启用敞口授信
            model.setStartOpenRat(openRatConfig.getValue());
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                        LgInfoVo.getId());
                model.setUrationDTO(urationDTO);// 授信明细
                model.setRatBankCode(urationDTO.getBankCode());// 授信银行
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 手机审批详情
     */
    @LogMessage(content = "跳转到保函申请待审批详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileApprove")
    public AjaxJson<GuaGuaranteeInfoViewVO> mobileApprove(String businessId, String taskId) {
        GuaGuaranteeInfoViewVO model = new GuaGuaranteeInfoViewVO();
        GuaGuaranteeInfo LgInfo = guaLgInfoService.get(businessId);
        GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
        BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
        setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
        model.setLgInfoVo(LgInfoVo);
        // 挂接的授信信息
        if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, LgInfoVo.getId());
            String isRmbRat = "";// 是否本位币
            List<RatUrationDisDTO> rations = urationService.viewCorpDisRation(ratUrationDTO, isRmbRat);
            model.setRations(rations);
        }
        // 挂接保证金信息
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
            model.setIsHangBail(isHangBailConfig.getValue());// 参数设置是否挂接保证金
        }
        // 设置被担保单位
        AjaxJson<List<CorpDTO>> corpDTOs1 = this.listGuaranteedCorp(LgInfoVo.getGuaranteeedCorp());
        List<CorpDTO> corpDTOs = corpDTOs1.getObj();
        String guaranteeedCorp = "";
        if (corpDTOs != null && corpDTOs.size() > 0) {// 设置被担保单位
            for (CorpDTO corpDTO : corpDTOs) {
                guaranteeedCorp += corpDTO.getName() + ",";
            }
            model.setGuaranteeedCorp(guaranteeedCorp);
        }
        model.setTaskId(taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }

    /**
     * 功能介绍 : 获取币别指定日期的最新汇率。<br>
     */
    @GetMapping("/getNearCurRat")
    @ResponseBody
    public AjaxJson getNearCurRat(String curId, String convertCurId, Date guaDate) {
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        if (EmptyUtils.isEmpty(guaDate)) {
            guaDate = DateUtils.getDate();
        }
        json.setObj(rateAdjustService.getExchangeRateBy(curId, convertCurId, guaDate,
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT));
        return json;
    }

    /**
     * 设置保证金相关信息
     */
    public void setBondMsg(GuaGuaranteeInfo LgInfoVo, GuaGuaranteeInfoViewVO model) {
        if (StringUtils.isNotEmpty(LgInfoVo.getIsHookBond())
                && LgInfoVo.getIsHookBond().equals(LetterConstant.GUA_COMMON_Y)) {// 外部挂接保证金
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(LgInfoVo.getId(), LgInfoVo.getGuaranteeNo());
            if (dto != null) {
                model.setIsHookBond(LgInfoVo.getIsHookBond());
                model.setBondBankAcc(dto.getBankAcc());
                model.setBailmoney(dto.getApplyMoney());
                model.setBondCurId(dto.getCurId());
                model.setBailmoneyRate(dto.getBondPercent());
                model.setBailmoneyContract(dto.getBondContract());
                model.setBondId(dto.getBondId());
            }
        } else {
            model.setIsHookBond(LgInfoVo.getIsHookBond());
            model.setBailmoney(LgInfoVo.getBondMoney());
            model.setBondCurId(LgInfoVo.getBondCurId());
            model.setBailmoneyRate(LgInfoVo.getBondPercent());
            model.setBailmoneyContract(LgInfoVo.getBondContract());
        }
        BigDecimal curExrate = BigDecimal.ONE;
        if (!LgInfoVo.getCurId().equals(LgInfoVo.getBondCurId())) {
            CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
            if (null != corpDTO) {
                curExrate = rateAdjustService.getExchangeRateBy(LgInfoVo.getCurId(), corpDTO.getCurId(),
                        LgInfoVo.getStartDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            }
        }
        model.setCurExrate(curExrate);
        model.setBondRate(LgInfoVo.getBondRate());
    }

    /**
     * 设置授信相关信息
     */
    public void setRatMsg(GuaGuaranteeInfo LgInfoVo, GuaGuaranteeInfoViewVO model) {
        RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, LgInfoVo.getId());// 授信明细
        CorpDTO corpDTO = corpService.get(ratUrationDTO.getCorpId());
        Date startDate;
        if (null != LgInfoVo.getStartDate()) {
            startDate = DateUtils.parse(DateUtils.formatDate(LgInfoVo.getStartDate()) + " 23:59:59");
        } else {
            startDate = new Date();
        }
        BigDecimal exchangerate = rateAdjustService.getExchangeRateBy(LgInfoVo.getCurId(), corpDTO.getCurId(),
                startDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        model.setRatItemCode(ratUrationDTO.getRatItemCode());
        model.setIsRmbRat(LgInfoVo.getIsRmbRat());// 是否折合本位币
        model.setExchangerate(exchangerate);
        model.setBillName(ratUrationDTO.getBillName());
        model.setUrationDTO(ratUrationDTO);
        model.setRatBankCode(ratUrationDTO.getBankCode());// 授信银行
    }

    /**
     * 获取保函基础参数设置
     */

    public void getParamsConfig(GuaGuaranteeInfoViewVO model) {
        ConfigDTO config = guaLgInfoService.getRatContains();
        model.setRatContains(config.getValue());// 获取挂接授信参数
        ConfigDTO openRatConfig = guaLgInfoService.getIsStartOpenRat();// 参数设置是否启用敞口授信
        model.setStartOpenRat(openRatConfig.getValue());
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        model.setIsHangBail(isHangBailConfig.getValue());// 参数设置是否挂接保证金
        // BtCurrencyDTO natualCurrency=btCurrencyService.getNatualCurrency();//获取本位币
        // model.addAttribute("standCur", natualCurrency);
    }

    /**
     * 获取保证金可选币别信息
     *
     * @param billCurId 业务币别
     * @return 币别集合
     */
    @GetMapping("/getBondCurList")
    @ResponseBody
    public AjaxJson<List<BtCurrencyDTO>> getBondCurList(String billCurId, String corpId) {
        List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.getBondCurList(billCurId, corpId);
        return AjaxJson.success(btCurrencyDTOS);
    }
}
