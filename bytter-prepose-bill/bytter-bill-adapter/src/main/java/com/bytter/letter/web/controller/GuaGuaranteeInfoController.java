package com.bytter.letter.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.service.GuaPerformRegService;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeInfoController
 * @Description:保函申请控制层
 * @date 2019年1月7日 上午9:37:01
 */
@Controller
@RequestMapping("/letter/guarantee/guaGuaranteeInfo")
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
    @Autowired
    private IFcBondHookService fcBondHookService;
    @Autowired
    private GuaPerformRegService performRegService;
    @LogMessage(content = "跳转到保函申请列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaGuaranteeInfo";
    }
    /**
     * 保函申请列表数据
     */
    @LogMessage(content = "查询保函申请列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/LgDatatable")
    @ResponseBody
    public DataTablesOutput LgDatatable(@RequestBody DataTablesInput input) {
        return guaLgInfoService.LgDatatable(input, this.getCurrentUser());
    }
    /**
     * 新增保函申请
     */
    @LogMessage(content = "跳转到新增保函申请页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add(Model model) {
        getParamsConfig(model);// 获取保函基础参数
        return "cms/guarantee/addGuaGuaranteeInfo";
    }
    /**
     * 保存保函申请
     */
    @LogMessage(content = "保存保函申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(GuaGuaranteeInfoVO LgInfoVo, RatUrationDTO urationDTO) {
        guaLgInfoService.tempOrSubmit(LgInfoVo, urationDTO, this.getCurrentUser());
        return success();
    }
    /**
     * 保函申请修改
     */
    @LogMessage(content = "跳转到保函申请修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty("id")) {
            // GuaGuaranteeInfoVO LgInfoVo=guaLgInfoService.getGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfoVo = guaLgInfoService.get(id);
            model.addAttribute("LgInfoVo", LgInfoVo);
            InputBankInfoDTO bankInfo = inputBankInfoService.getByBankCodeAndImport(LgInfoVo.getOpenCorp());// 查询银行
            model.addAttribute("bankName", bankInfo.getBankName());
            getParamsConfig(model);// 获取保函基础参数
            CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
            model.addAttribute("standCur", btCurrencyService.getObjectById(corpDTO.getCurId()));
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                setBondMsg(LgInfoVo, model);
            }
            if (LgInfoVo.getRatmodel() != null && RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {// 挂接授信
                setRatMsg(LgInfoVo, model);
            }
        }
        return "cms/guarantee/updateGuaGuaranteeInfo";
    }
    /**
     * 查看保函申请详情
     */
    @LogMessage(content = "跳转到保函申请查看页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty("id")) {
            // GuaGuaranteeInfoVO LgInfoVo=guaLgInfoService.getGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfo = guaLgInfoService.get(id);
            GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
            BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
            setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
            model.addAttribute("LgInfoVo", LgInfoVo);
            getParamsConfig(model);// 获取保函基础参数
            CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
            model.addAttribute("standCur", btCurrencyService.getObjectById(corpDTO.getCurId()));
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
                BtCurrencyDTO guaCurDTO = btCurrencyService.getObjectById(LgInfoVo.getCurId());
                model.addAttribute("naturalSign", guaCurDTO.getNaturalSign());// 本位币标识
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id);
                model.addAttribute("urationDTO", urationDTO);// 授信明细
                model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
            }
            //保证金相关
            if (StringUtils.isNotBlank(LgInfoVo.getIsHookBond()) && "1".equals(LgInfoVo.getIsHookBond())){
                FcBondTag fcBondTag = new FcBondTag();
                model.addAttribute("fcBondTag", fcBondTag);
            }
        }
        return "cms/guarantee/viewGuaGuaranteeInfo";
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
        if (StringUtils.isNotBlank(LgInfoVo.getGuaranteeedCorp())){
            CorpDTO guaranteeedCorp = corpService.get(LgInfoVo.getGuaranteeedCorp());
            LgInfoVo.setGuaranteeedCorpShow("("+guaranteeedCorp.getCode()+")"+guaranteeedCorp.getName());
        }
    }
    /**
     * 获取被担保单位
     */
    @PostMapping("/listGuaranteedCorp")
    @ResponseBody
    public List<CorpDTO> listCorp(String corps) {
        return listGuaranteedCorp(corps);
    }
    @GetMapping("/listGuaranteedCorp")
    @ResponseBody
    public List<CorpDTO> listGuaranteedCorp(String corps) {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        if (corps != null) {// 设置被担保单位
            String[] corpIds = corps.split(",");
            List<String> corpList = new ArrayList<String>(corpIds.length);
            for (String s : corpIds) {
                corpList.add(s);
            }
            corpDTOs = corpService.listCorpDTOByCorpIds2(corpList);
        }
        return TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }
    /**
     * 检查保函编号是否重复
     *
     * @return true-重复 false-不重复
     */
    @LogMessage(content = "检查保函编号是否重复", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/checkLgNo")
    @ResponseBody
    public boolean checkLgNo(String guaranteeNo) {
        List<GuaGuaranteeInfoVO> LgList = guaLgInfoService.queryLgByNo(guaranteeNo);
        if (LgList.size() > 0) {
            return true;
        }
        return false;
    }
    /**
     * 审批列表页面
     */
    @GetMapping("/listApprove")
    @LogMessage(content = "跳转到保函申请审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/guarantee/listGuaGuaranteeInfoApprove";
    }
    /**
     * 查询审批列表数据
     */
    @LogMessage(content = "查询保函申请待审批列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/approveDataList")
    @ResponseBody
    public DataTablesOutput approveDataList(@RequestBody DataTablesInput dataTablesInput) {
        return guaLgInfoService.getApproveDataTables(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 审批详情
     */
    @LogMessage(content = "跳转到保函申请待审批详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        GuaGuaranteeInfo LgInfo = guaLgInfoService.get(id);
        GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
        BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
        setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
        model.addAttribute("LgInfoVo", LgInfoVo);
        getParamsConfig(model);// 获取保函基础参数
        CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
        model.addAttribute("standCur", btCurrencyService.getObjectById(corpDTO.getCurId()));
        // 挂接的授信信息
        if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
            RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, LgInfoVo.getId());
            model.addAttribute("urationDTO", urationDTO);// 授信明细
            model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
        }

        //设置被担保单位
        if (StringUtils.isNotBlank(LgInfoVo.getGuaranteeedCorp())) {
            List<String> corpIdList = Arrays.asList(LgInfoVo.getGuaranteeedCorp().split(","));
            List<CorpDTO> corpDTOs = corpService.listCorpDTOByCorpIds2(corpIdList);
            if (EmptyUtils.isNotEmpty(corpDTOs)) {
                String guaranteeedCorp = corpDTOs.stream()
                        .map(item -> "(" + item.getCode() + ")" + item.getName())
                        .collect(Collectors.joining());
                LgInfoVo.setGuaranteeedCorpShow(guaranteeedCorp);
            }
        }

        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/viewGuaGuaranteeInfoApprove";
    }
    /**
     * 保函到期处理列表页面
     *
     * @return
     */
    @LogMessage(content = "跳转到保函到期处理列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/logoutList")
    public String logoutList() {
        return "cms/guarantee/listLogoutGuaGuaranteeInfo";
    }
    /**
     * 保函到期处理列表数据
     */
    @LogMessage(content = "查询保函到期处理列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/logoutDatatable")
    @ResponseBody
    public DataTablesOutput logoutDatatable(@RequestBody DataTablesInput input) {
        return guaLgInfoService.logoutDatatable(input, this.getCurrentUser());
    }
    /**
     * 保函到期处理页面
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "跳转到保函到期处理页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/logout/{id}")
    public String logoutGuaGuarantee(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeInfoVO info = guaLgInfoService.getGuaranteeInfoById(id);
            model.addAttribute("LgInfoVo", info);
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(info.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id);
                model.addAttribute("urationDTO", urationDTO);// 授信明细
                model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
            }
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
        }
        return "cms/guarantee/logoutGuaGuarantee";
    }
    /**
     * 保函注销处理
     */
    @LogMessage(content = "执行保函注销操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/logout")
    @ResponseBody
    public AjaxJson logout(GuaGuaranteeInfoVO LgInfoVo) {
        guaLgInfoService.logout(LgInfoVo, this.getCurrentUser());
        return success();
    }
    /**
     * 保函查询页面跳转
     *
     * @return
     */
    @LogMessage(content = "跳转到保函查询页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/listAll")
    public String listAllGuaGuarantee() {
        return "cms/guarantee/listGuaGuaranteeAllInfo";
    }
    /**
     * 保函查询列表数据
     */
    @LogMessage(content = "查询保函相关数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @SuppressWarnings("rawtypes")
    @PostMapping("/loadAllDatatable")
    @ResponseBody
    public DataTablesOutput loadAllDatatable(@RequestBody DataTablesInput input) {
        return guaLgInfoService.loadAllDatatable(input, this.getCurrentUser());
    }
    /**
     * 查看保函申请详情
     */
    @LogMessage(content = "查看保函申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/viewDtl/{id}")
    public String viewDtl(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty("id")) {
            // GuaGuaranteeInfoVO LgInfoVo=guaLgInfoService.getGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfo = guaLgInfoService.get(id);
            GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
            BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
            setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
            model.addAttribute("LgInfoVo", LgInfoVo);
            // 保函履约信息
            GuaPerformRegVO guaPerformRegVO = performRegService.getRegVOById(id);
            model.addAttribute("guaPerformRegVO", guaPerformRegVO);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
            ConfigDTO openRatConfig = guaLgInfoService.getIsStartOpenRat();// 参数设置是否启用敞口授信
            model.addAttribute("startOpenRat", openRatConfig.getValue());
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                        LgInfoVo.getId());
                model.addAttribute("urationDTO", urationDTO);// 授信明细
                model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
            }
        }
        return "cms/guarantee/viewDtlGuaGuaranteeInfo";
    }
    /**
     * 手机审批详情
     */
    @LogMessage(content = "跳转到保函申请待审批详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        GuaGuaranteeInfo LgInfo = guaLgInfoService.get(businessId);
        GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
        BeanUtils.copyNotNullProperties(LgInfo, LgInfoVo);
        setGuaLgInfo(LgInfoVo);// 设置保函申请其他信息
        model.addAttribute("LgInfoVo", LgInfoVo);
        // 挂接的授信信息
        if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, LgInfoVo.getId());
            String isRmbRat = "";// 是否本位币
            List<RatUrationDisDTO> rations = urationService.viewCorpDisRation(ratUrationDTO, isRmbRat);
            model.addAttribute("rations", rations);
        }
        // 挂接保证金信息
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
            model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
        }
        // 设置被担保单位
        List<CorpDTO> corpDTOs = this.listGuaranteedCorp(LgInfoVo.getGuaranteeedCorp());
        String guaranteeedCorp = "";
        if (corpDTOs != null && corpDTOs.size() > 0) {// 设置被担保单位
            for (CorpDTO corpDTO : corpDTOs) {
                guaranteeedCorp += corpDTO.getName() + ",";
            }
            model.addAttribute("guaranteeedCorp", guaranteeedCorp);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/mobileGuaGuaranteeInfo";
    }
    /**
     * 功能介绍 : 获取币别指定日期的最新汇率。<br>
     */
    @PostMapping("/getNearCurRat")
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
    public void setBondMsg(GuaGuaranteeInfo LgInfoVo, Model model) {
        if (StringUtils.isNotEmpty(LgInfoVo.getIsHookBond())
                && LgInfoVo.getIsHookBond().equals(LetterConstant.GUA_COMMON_Y)) {// 外部挂接保证金
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(LgInfoVo.getId(), LgInfoVo.getGuaranteeNo());
            if (dto != null) {
                model.addAttribute("isHookBond", LgInfoVo.getIsHookBond());
                model.addAttribute("bondBankAcc", dto.getBankAcc());
                model.addAttribute("bailmoney", dto.getApplyMoney());
                model.addAttribute("bondCurId", dto.getCurId());
                model.addAttribute("bailmoneyRate", dto.getBondPercent());
                model.addAttribute("bailmoneyContract", dto.getBondContract());
                model.addAttribute("bondId", dto.getBondId());
            }
        } else {
            model.addAttribute("isHookBond", LgInfoVo.getIsHookBond());
            model.addAttribute("bailmoney", LgInfoVo.getBondMoney());
            model.addAttribute("bondCurId", LgInfoVo.getBondCurId());
            model.addAttribute("bailmoneyRate", LgInfoVo.getBondPercent());
            model.addAttribute("bailmoneyContract", LgInfoVo.getBondContract());
        }
        BigDecimal curExrate = BigDecimal.ONE;
        if (!LgInfoVo.getCurId().equals(LgInfoVo.getBondCurId())) {
            CorpDTO corpDTO = corpService.get(LgInfoVo.getCorpId());
            if (null != corpDTO) {
                curExrate = rateAdjustService.getExchangeRateBy(LgInfoVo.getCurId(), corpDTO.getCurId(),
                        LgInfoVo.getStartDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            }
        }
        model.addAttribute("curExrate", curExrate);
        model.addAttribute("bondRate", LgInfoVo.getBondRate());
    }
    /**
     * 设置授信相关信息
     */
    public void setRatMsg(GuaGuaranteeInfo LgInfoVo, Model model) {
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
        model.addAttribute("ratItemCode", ratUrationDTO.getRatItemCode());
        model.addAttribute("isRmbRat", LgInfoVo.getIsRmbRat());// 是否折合本位币
        model.addAttribute("exchangerate", exchangerate);
        model.addAttribute("billName", ratUrationDTO.getBillName());
        model.addAttribute("urationDTO", ratUrationDTO);
        model.addAttribute("ratBankCode", ratUrationDTO.getBankCode());// 授信银行
    }
    /**
     * 获取保函基础参数设置
     */
    public void getParamsConfig(Model model) {
        ConfigDTO config = guaLgInfoService.getRatContains();
        model.addAttribute("ratContains", config.getValue());// 获取挂接授信参数
        ConfigDTO openRatConfig = guaLgInfoService.getIsStartOpenRat();// 参数设置是否启用敞口授信
        model.addAttribute("startOpenRat", openRatConfig.getValue());
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
        // BtCurrencyDTO natualCurrency=btCurrencyService.getNatualCurrency();//获取本位币
        // model.addAttribute("standCur", natualCurrency);
    }
    /**
     * 获取保证金可选币别信息
     *
     * @param billCurId
     *            业务币别
     * @return 币别集合
     */
    @GetMapping("/getBondCurList")
    @ResponseBody
    public List<BtCurrencyDTO> getBondCurList(String billCurId, String corpId) {
        List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.getBondCurList(billCurId, corpId);
        return btCurrencyDTOS;
    }
}
