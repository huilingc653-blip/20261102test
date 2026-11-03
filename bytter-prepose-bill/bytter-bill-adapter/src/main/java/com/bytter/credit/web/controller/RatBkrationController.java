package com.bytter.credit.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import com.bytter.credit.domain.vo.GuaContractOccupVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatItemSumVO;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信额度录入表
 * @date 2017-07-21 14:35:43
 */
@Controller
@RequestMapping("/cms/rat/ratbkration")
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


    /**
     * 授信额度录入表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询授信额度录入表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String listdata(Model model, Integer start, Integer len, RatBkrationVO ratBkrationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        // 查询参数设置
        this.setSearchParams(model, input, ratBkrationVO);
        List<String> corpIds = null;
        if (StringUtils.isNotEmpty(ratBkrationVO.getCorpId())) {
            corpIds = Arrays.asList(ratBkrationVO.getCorpId().split(","));
        }
        List<RatItemSumVO> ratItemSumList = new ArrayList<>();
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_RATION_QUERY, getUserId(), corpIds, ratItemSumList);
        ratBkrationService.checkBkrationInother(output);
        Map<String, Object> result = new HashMap<String, Object>();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }
        result.put("rations", output.getData());
        result.put("curr", start);
        result.put("pageCount", pageCount);
        result.put("pageSize", pageSize);
        result.put("resultCount", resultCount);
        model.addAttribute("model", result);
      //  model.addAttribute("ratItemSumList", ratItemSumList);
        return "cms/rat/bkration/listRatBkration";
    }

    /**
     * 查询参数设置
     *
     * @param model
     * @param input
     * @param ratBkrationVO
     */
    private void setSearchParams(Model model, DataTablesInput input, RatBkrationVO ratBkrationVO) {
        // 授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        model.addAttribute("ratType", ratType);
        /**
         * 授信合同号
         */
        if (!StringUtils.isEmpty(ratBkrationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", ratBkrationVO.getContractNo());
            model.addAttribute("contractNo", ratBkrationVO.getContractNo());
        }
        /**
         * 估计授信时间
         */
        if (null != ratBkrationVO.getRatDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);// 小于等于授信结束日期
            model.addAttribute("ratDate", DateUtils.formatDate(ratBkrationVO.getRatDate()));
        }
        /**
         * 银行信息
         */
        if (null != ratBkrationVO.getBankName()) {
            input.addFilter(MatchTypeEnum.LIKE, "bankName", ratBkrationVO.getBankName(), String.class);
            model.addAttribute("bankName", ratBkrationVO.getBankName());
        }
        /**
         * 授信金额
         */
        if (null != ratBkrationVO.getgMoney()) {
            // input.addFilter(MatchTypeEnum.GE, "money", ratBkrationVO.getgMoney(), BigDecimal.class);
            input.getFormdata().put("gMoney", ratBkrationVO.getgMoney());
            model.addAttribute("gMoney", ratBkrationVO.getgMoney());
        }
        /**
         * 授信金额
         */
        if (null != ratBkrationVO.getlMoney()) {
            // input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney(), BigDecimal.class);
            input.getFormdata().put("lMoney", ratBkrationVO.getlMoney());
            model.addAttribute("lMoney", ratBkrationVO.getlMoney());
        }
        /**
         * 授信开始时间段最小时间
         */
        if (null != ratBkrationVO.getgBeginDate()) {
            // input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney(), BigDecimal.class);
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(ratBkrationVO.getgBeginDate()));
            model.addAttribute("gBeginDate", DateUtils.formatDate(ratBkrationVO.getgBeginDate()));
        }
        /**
         * 授信开始时间段最大时间
         */
        if (null != ratBkrationVO.getlBeginDate()) {
            // input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney(), BigDecimal.class);
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(ratBkrationVO.getlBeginDate()));
            model.addAttribute("lBeginDate", DateUtils.formatDate(ratBkrationVO.getlBeginDate()));
        }
        /**
         * 授信结束时间段最小时间
         */
        if (null != ratBkrationVO.getgEndDate()) {
            // input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney(), BigDecimal.class);
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(ratBkrationVO.getgEndDate()));
            model.addAttribute("gEndDate", DateUtils.formatDate(ratBkrationVO.getgEndDate()));
        }
        /**
         * 授信结束时间段最大时间
         */
        if (null != ratBkrationVO.getlEndDate()) {
            // input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney(), BigDecimal.class);
            input.getFormdata().put("led_endDate", DateUtils.formatDate(ratBkrationVO.getlEndDate()));
            model.addAttribute("lEndDate", DateUtils.formatDate(ratBkrationVO.getlEndDate()));
        }
        /**
         * 担保开始时间段最小时间
         */
        if (null != ratBkrationVO.getgGuaranteeBeginDate()) {
            input.getFormdata().put("ged_guaranteeBeginDate", DateUtils.formatDate(ratBkrationVO.getgGuaranteeBeginDate()));
            model.addAttribute("gGuaranteeBeginDate", DateUtils.formatDate(ratBkrationVO.getgGuaranteeBeginDate()));
        }
        /**
         * 担保开始时间段最大时间
         */
        if (null != ratBkrationVO.getlGuaranteeBeginDate()) {
            input.getFormdata().put("led_guaranteeBeginDate", DateUtils.formatDate(ratBkrationVO.getlGuaranteeBeginDate()));
            model.addAttribute("lGuaranteeBeginDate", DateUtils.formatDate(ratBkrationVO.getlGuaranteeBeginDate()));
        }
        /**
         * 担保结束时间段最小时间
         */
        if (null != ratBkrationVO.getgGuaranteeEndDate()) {
            input.getFormdata().put("ged_guaranteeEndDate", DateUtils.formatDate(ratBkrationVO.getgGuaranteeEndDate()));
            model.addAttribute("gGuaranteeEndDate", DateUtils.formatDate(ratBkrationVO.getgGuaranteeEndDate()));
        }
        /**
         * 担保结束时间段最大时间
         */
        if (null != ratBkrationVO.getlGuaranteeEndDate()) {
            input.getFormdata().put("led_guaranteeEndDate", DateUtils.formatDate(ratBkrationVO.getlGuaranteeEndDate()));
            model.addAttribute("lGuaranteeEndDate", DateUtils.formatDate(ratBkrationVO.getlGuaranteeEndDate()));
        }
        /**
         * 授信编号
         */
        if (!StringUtils.isEmpty(ratBkrationVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratBkrationVO.getRatCode());
            model.addAttribute("ratCode", ratBkrationVO.getRatCode());
        }
        /**
         * 创建人
         */
        if (!StringUtils.isEmpty(ratBkrationVO.getUserName())) {
            input.addFilter(MatchTypeEnum.LIKE, "userName", ratBkrationVO.getUserName());
            model.addAttribute("userName", ratBkrationVO.getUserName());
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getCorpId())) {
            model.addAttribute("corpId", ratBkrationVO.getCorpId());
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getItemFlag())) {
            model.addAttribute("itemFlag", ratBkrationVO.getItemFlag());
            input.getFormdata().put("itemFlag", ratBkrationVO.getItemFlag());
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getItemCode())) {
            model.addAttribute("itemCode", ratBkrationVO.getItemCode());
            input.getFormdata().put("itemCode", ratBkrationVO.getItemCode());
        }
        // 状态
        if (!EmptyUtils.isEmpty(ratBkrationVO.getStatus())) {
            input.addFilter(MatchTypeEnum.EQ, "status", ratBkrationVO.getStatus());
            model.addAttribute("status", ratBkrationVO.getStatus());
        }
    }

    /**
     * 授信额度录入表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ratBkrationService.queryRatBkrationData(dataTablesInput, RationConstant.CMS_RATION_RATION_QUERY,
                getUserId());
    }

    /**
     * 授信额度录入表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatBkration ratBkration, Model model) {
        if (StringUtils.isNotEmpty(ratBkration.getId())) {
            ratBkration = ratBkrationService.get(ratBkration.getId());
            model.addAttribute("ratBkrationPage", ratBkration);
        }
        // 获取授信参数
        List<RatItemCustomDTO> list = itemCustomService.getAllItemCustom(RationConstant.RAT_TYPE_0);
        if (null != list && !list.isEmpty()) {
            model.addAttribute("copmRatSize", list.size());
        } else {
            model.addAttribute("copmRatSize", 0);
        }
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "addRatBkration";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addRatBkrationOpen";// 敞口跳转
        }
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();// 是否挂接保证金
        model.addAttribute("isHangBail", bailConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        model.addAttribute("relateCon", ratBkrationService.getRelateCon());
        model.addAttribute("conType", ratBkrationService.getRatConType());
        ConfigDTO creditContractConfig = ratBkrationService.getCreditContractControl();// 授信合同必须控制
        model.addAttribute("creditContractControl", creditContractConfig.getValue());
       // cms/rat/bkration/addRatBkration
        return "cms/rat/bkration/" + page;
    }

    /**
     * 功能介绍 : 查询授信合同号是否存在。<br>
     * <b>日期：</b>2017年9月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @PostMapping("/checkContractNo")
    @ResponseBody
    public String checkContractNo(String contractNo, String ratCode) {
        if (ratBkrationService.checkContractNo(contractNo, ratCode)) {
            return RationConstant.RAT_COMMON_Y;// 不存在
        }
        return RationConstant.RAT_COMMON_N;
    }

    /**
     * 功能介绍 : 初始化银行授信录入明细。<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @PostMapping("/initRationDtl")
    @ResponseBody
    public List<RatBkRationDtlVm> initRationDtl(String ratCode, String itemFlag) {
        List<RatBkRationDtlVm> rationDtls = new ArrayList<RatBkRationDtlVm>();
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkRationVm bkrationListDTO = ratBkrationService.geBkrationListDTOByRatCode(ratCode, itemFlag);
            if (null != bkrationListDTO.getRationDtls()) {
                rationDtls = bkrationListDTO.getRationDtls();// 单品种明细
            }
        } else {
            // 初始化几条空明细FIXME
            // rationDtls.add(new RatBkRationDtlVm());
            // rationDtls.add(new RatBkRationDtlVm());
        }
        return rationDtls;
    }

    /**
     * 功能介绍 : 跳转设置额度明细敞口担保条件。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/setAssure")
    public String setAssure(RatBkRationDtlVm RatBkRationDtlVm, HttpServletRequest request, String rowIndex,
                            Model model) {
        String assurecondition = RatBkRationDtlVm.getAssurecondition();
        RatBkRationDtlVm.setAssurecondition(assurecondition);
        model.addAttribute("ratBkrationDtlPage", RatBkRationDtlVm);
        model.addAttribute("rowIndex", rowIndex);
        return "cms/rat/bkration/setAssure";
    }

    /**
     * 授信额度录入表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatBkRationVm bkrationListDTO, Model model) {
        if (StringUtils.isNotEmpty(bkrationListDTO.getId())) {
            bkrationListDTO = ratBkrationService.geBkrationListDTOByRatCode(bkrationListDTO.getId(),
                    RationConstant.RAT_COMFLAG_1);
            model.addAttribute("ratBkrationPage", bkrationListDTO);
            model.addAttribute("singItemSize", bkrationListDTO.getSingItemSize());// 单品种Size
            model.addAttribute("compItemSize", bkrationListDTO.getCompItemSize());// 综合品种Size
            model.addAttribute("itemSize", bkrationListDTO.getItemSize());// 所有品种Size
        }
        // 获取授信参数
        List<RatItemCustomDTO> list = itemCustomService.getAllItemCustom(RationConstant.RAT_TYPE_0);
        if (null != list && !list.isEmpty()) {
            model.addAttribute("copmRatSize", list.size());
        } else {
            model.addAttribute("copmRatSize", 0);
        }
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "updateRatBkration";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateRatBkrationOpen";// 敞口跳转
        }
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();// 参数设置是否挂接保证金
        if (bailConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            // 判断是否挂接了保证金
            if (bkrationListDTO.getIsHookBond() != null) {
                if (bkrationListDTO.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y)) {
                    FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(bkrationListDTO.getId(),
                            bkrationListDTO.getRatCode());
                    model.addAttribute("dto", dto);
                }
            }
        }
        model.addAttribute("isHangBail", bailConfig.getValue());
        model.addAttribute("relateCon", ratBkrationService.getRelateCon());
        model.addAttribute("conType", ratBkrationService.getRatConType());
        model.addAttribute("conId", bkrationListDTO.getConId());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        ConfigDTO creditContractConfig = ratBkrationService.getCreditContractControl();// 授信合同必须控制
        model.addAttribute("creditContractControl", creditContractConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            model.addAttribute("isHangGuarantee", bkrationListDTO.getIsHangGuarantee());
            if (null != bkrationListDTO.getIsHangGuarantee()
                    && bkrationListDTO.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {// 授信挂接担保
                setGuaContractMsg(bkrationListDTO, model);// 设置担保合同相关信息
            }
        }
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信额度录入表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信额度录入表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(id);
        if (StringUtils.isNotEmpty(ratBkration.getOldRatCode())) {
            RatBkration oldBkration = ratBkrationService.get(ratBkration.getOldRatCode(), true);
            ratBkration.setOldRatCode(oldBkration.getRatCode());
        }
        model.addAttribute("ratBkrationPage", ratBkration);
        model.addAttribute("viewFlag", "1");
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkration";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationOpen";// 敞口跳转
        }
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();// 参数设置是否挂接保证金
        model.addAttribute("isHangBail", bailConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        model.addAttribute("relateCon", ratBkrationService.getRelateCon());
        model.addAttribute("conType", ratBkrationService.getRatConType());
        model.addAttribute("conId", ratBkration.getConId());
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信额度录入保存前校验，是否存在同时间的额度明细
     *
     * @return
     */
    //
    @LogMessage(content = "校验授信时间区间", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/checkRation")
    @ResponseBody
    public AjaxJson checkRation(RatBkRationVm rationtDTO) {
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        ratBkrationService.checkBkration(rationtDTO);
        return success();
    }

    /**
     * 授信额度录入暂存
     *
     * @return
     */
    @LogMessage(content = "添加授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkRationVm rationtDTO, HttpServletRequest request) {
        ratBkrationService.validata(rationtDTO);
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        rationtDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationService.saveRatBkration(rationtDTO, getCurrentUser());
        return success();
    }

    /**
     * 授信额度录入提交
     *
     * @return
     */
    @LogMessage(content = "提交授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatBkRationVm rationVm) {
        ratBkrationService.validata(rationVm);
        ratBkrationService.setRatBkrationDtls(rationVm);
        rationVm.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationService.saveRatBkration(rationVm, getCurrentUser());
        return success();
    }

    /**
     * 授信额度录入修改暂存
     *
     * @return
     */
    @LogMessage(content = "修改授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatBkRationVm rationtDTO) {
        ratBkrationService.validata(rationtDTO);
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        rationtDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationService.updateRatBkration(rationtDTO, getCurrentUser());
        return success();
    }

    /**
     * 授信额度录入修改提交
     *
     * @return
     */
    @LogMessage(content = "修改授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatBkRationVm rationtDTO) {
        ratBkrationService.validata(rationtDTO);
        ratBkrationService.setRatBkrationDtls(rationtDTO);
        rationtDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationService.updateRatBkration(rationtDTO, getCurrentUser());
        return success();
    }

    private void setRatBkrationDtls(RatBkRationVm rationtDTO) {
        List<RatBkRationDtlVm> dtls = new ArrayList<RatBkRationDtlVm>();
        List<RatBkRationDtlVm> rationDtls = rationtDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationDtlVm>();
        }
        if (EmptyUtils.isEmpty(rationtDTO.getIncludeBailmoney())) {
            rationtDTO.setIncludeBailmoney(RationConstant.RAT_COMMON_Y);// 包含保证金
        }
        BigDecimal money = null;
        BigDecimal openMoney = null;
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO && !StringUtils.isEmpty(dtlDTO.getItemFlag())) {
                dtls.add(dtlDTO);
            }
            // 取综合授信品种的授信金额
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                if (null != dtlDTO.getMoney()) {
                    money = dtlDTO.getMoney();
                    dtlDTO.setCompSign(RationConstant.RAT_COMP_SIGN_1);// 综合授信使用标识
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
        // 按照单品种和综合品种排序
        Collections.sort(dtls, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        // 重设itemorder
        Integer index = Integer.valueOf(1);
        for (RatBkRationDtlVm dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
            if (StringUtils.isEmpty(dtlDTO.getCompSign())) {
                dtlDTO.setCompSign(RationConstant.RAT_COMP_SIGN_0);// 默认都是0
            }
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                // 所有的综合授信品种都设置同一个授信金额
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

    /**
     * 修改授信额度录入表
     *
     * @param ratBkration
     * @return
     */
    //
    @LogMessage(content = "修改授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatBkration ratBkration) {
        RatBkration t = ratBkrationService.get(ratBkration.getId());
        BeanUtils.copyNotNullProperties(ratBkration, t);
        ratBkrationService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除授信额度录入表
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkration ratBkration = ratBkrationService.get(id);
        ratBkration.setStatus(RationConstant.STATUS_DELETE);
        ratBkrationService.updateEntity(ratBkration);
        // 判断是否挂接了保证金
        if (ratBkration.getIsHookBond() != null) {
            if (ratBkration.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y)) {
                // 根据项目id查找保证金占用信息
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                        ratBkration.getRatCode());
                dto.setStatus(FcBondConstant.STATUE_RELEASE);// 修改状态为释放
                dto.setUpdateBy(getUserLoginName());
                dto.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(dto);
            }
        }
        /**
         * 删除时判断是否挂接合同
         */
        String relateCon = ratBkrationService.getRelateCon().getValue();
        if (RationConstant.RAT_COMMON_Y.equals(relateCon)) {
            if (StringUtils.isNotEmpty(ratBkration.getConId())) {
                conCommonService.unHookConOp(ratBkration.getConId(), ratBkration.getId(), getCurrentUser());
            }
        }
        return success();
    }

    /**
     * 批量删除授信额度录入表
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信额度录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatBkration ratBkration = ratBkrationService.get(id);
            ratBkration.setStatus(RationConstant.STATUS_DELETE);
            ratBkrationService.updateEntity(ratBkration);
        }
        return success();
    }

    /**
     * 功能介绍 : 单位授信主体设置。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @LogMessage(content = "单位授信主体设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryCorp")
    public String queryCorp(Integer type, CorpDTO corpDTO, Model model) {
        String code = corpDTO.getCode();
        List<CorpDTO> list = iCorpService.listEffectiveCorp();
        model.addAttribute("code", code);
        model.addAttribute("corpDTO", getCorpIdInfo(list, code));
        return "cms/rat/corpRation";
    }

    public List<CorpDTO> getCorpIdInfo(List<CorpDTO> corpList, String code) {
        List<CorpDTO> list = new ArrayList<CorpDTO>();
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

    /**
     * 修改单位授信主体设置
     *
     * @return
     */
    //
    @LogMessage(content = "修改单位授信主体设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveRatCorp")
    @ResponseBody
    public AjaxJson saveRatCorp(@RequestParam("corpIds[]") List<String> corpIds) {
        ratCorpService.updateRatGroup(corpIds);
        return success();
    }

    /**
     * 功能介绍 : 获取有效单位。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @RequestMapping(params = "getlistEffectiveCorp")
    @ResponseBody
    public AjaxJson getlistEffectiveCorp() {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(iCorpService.listEffectiveCorp());
        return j;
    }

    /**
     * 授信录入审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/bkration/approve/listRatBkrationApprove";
    }

    /**
     * 授信录入审批 - 数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input) {
        Object beginDate = input.getFormdata().get("beginDate");
        if (!EmptyUtils.isEmpty(beginDate)) {
            input.addFilter(MatchTypeEnum.LE, "sDate", beginDate.toString(), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", beginDate.toString(), Date.class);// 小于等于授信结束日期
        }
        return ratBkrationService.listApproveOutputData(input);
    }

    /**
     * 授信录入审批 - 详情页面
     */
    @GetMapping("/viewBkrationApprove")
    public String viewBkrationApprove(String businessId, String taskId, Model model) {
        RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(businessId);
        model.addAttribute("ratBkrationPage", ratBkration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        if (StringUtils.isNotEmpty(ratBkration.getOldRatCode())) {
            RatBkration oldBkration = ratBkrationService.get(ratBkration.getOldRatCode(), true);
            ratBkration.setOldRatCode(oldBkration.getRatCode());
        }
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkration";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationOpen";// 敞口跳转
        }
        ConfigDTO bailConfig = ratBkrationService.getIsHangBail();// 参数设置是否挂接保证金
        model.addAttribute("isHangBail", bailConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        // 如果挂接了保证金显示占用的保证金信息
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
            model.addAttribute("dto", dto);
            model.addAttribute("addMoney", addMoney);
        }
        model.addAttribute("relateCon", ratBkrationService.getRelateCon());
        model.addAttribute("conType", ratBkrationService.getRatConType());
        model.addAttribute("conId", ratBkration.getConId());
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/viewRatBkrationApprove";
    }

    /**
     * 功能介绍 : 根据分配类型获取单位的直接下属单位。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/listCorp")
    @ResponseBody
    public List<CorpDTO> listCorp(String ratModel) {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        if (RationConstant.RATMODE_1.equals(ratModel)) {// 自主授信,有权限的单位
            corpDTOs = iCorpService.listUserCorp(RationConstant.CMS_RATION_RATION_APPLY, getUserId());
        } else {// 集团统一授信
            corpDTOs = iCorpService.getRatGroupCorpNet(RationConstant.CMS_RATION_RATION_APPLY,getUserId());
        }
        // 组装树，并排序
        // 组装树，并排序
        return TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
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
        });
    }

    /**
     * 授信录入审批 - 手机审批详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileBkrationApprove(String businessId, String taskId, Model model) {
        RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(businessId);
        model.addAttribute("ratBkrationPage", ratBkration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        String page = "mobileRatBkration";
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {// 参数设置挂接担保
            if (ratBkration.getIsHangGuarantee() != null
                    && ratBkration.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {// 授信挂接担保
                GuaContractUseDTO guaContractUseDTO = contractOccupServie.getGuaContractOccupyDtl(ratBkration.getId(),
                        ratBkration.getRatCode());
                model.addAttribute("guaContractUseDTO", guaContractUseDTO);
            }
        }
        return "cms/rat/bkration/approve/" + page;
    }

    public void setGuaContractMsg(RatBkRationVm bkrationListDTO, Model model) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(bkrationListDTO.getId());
        guaContractOccupDTO.setStatus(GuaranteeConstant.GUA_CONTRACT_TEMP);// 暂存状态
        GuaContractOccupDTO guaContractOccup = contractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
        List<String> idList = guaContractOccup.getContractIds();
        String contractIds = "";
        for (String id : idList) {
            contractIds += id + ",";
        }
        model.addAttribute("contractIds", contractIds);// 担保方式id组合，逗号隔开
        model.addAttribute("idList", idList);// 担保合同id集合
        model.addAttribute("contractCode", guaContractOccup.getContractCode());// 担保合同编号组合，逗号隔开
        model.addAttribute("guaWayNames", guaContractOccup.getGuaWayName());// 担保方式名称组合，逗号隔开
        model.addAttribute("guaWay", guaContractOccup.getGuaWay());// 担保方式code组合，逗号隔开
        model.addAttribute("guaranteeRmk", guaContractOccup.getGuaRmk());// 担保备注
    }

    /**
     * 获取银行授信录入审批通过的数据
     *
     * @author luxy
     */
    @PostMapping("/ratBkData")
    @ResponseBody
    public DataTablesOutput ratBkData(@RequestBody DataTablesInput input) {
        return ratBkrationService.ratBkData(input);
    }

    @GetMapping("/json")
    @ResponseBody
    public List<RatBkRationVm> json(RatBkRationVm uration) {
        return ratBkrationService.queryRationAll(uration);
    }

    /**
     * 导入
     *
     * @param
     * @return
     */
    @LogMessage(content = "导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<RatBkrationVO> list) {
        ratBkrationService.dealImportData(list, this.getCurrentUser());
        return success();
    }

    /**
     * 获取挂接担保合同变更的授信数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/getGuaContract")
    @ResponseBody
    public DataTablesOutput<GuaContractOccupVO> getUseChangelist(@RequestBody DataTablesInput dataTablesInput) {
        return ratBkrationService.getGuaForRat(dataTablesInput, getCurrentUser());
    }

    /**
     * 银行授信额度录入导出
     *
     * @param request  request
     * @param response response
     * @param vo       查询条件
     * @param model    model
     */
    @LogMessage(content = "银行授信额度录入导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkration")
    public void exportListRatBkration(HttpServletRequest request, HttpServletResponse response, RatBkrationVO vo, Model model) throws Exception {
        // 导出的文件名
        String fileName = "银行授信额度录入表.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(vo.getExportType())) {
            // 导出当前页
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
//		查询参数设置
        this.setSearchParams(model, input, vo);
        List<String> corpIds = null;
        if (StringUtils.isNotBlank(vo.getCorpId())) {
            corpIds = Arrays.asList(vo.getCorpId().split(","));
        }
        List<String> itemCodes = Lists.newArrayList();
        if (StringUtils.isNotBlank(vo.getItemCode())) {
            itemCodes = Arrays.asList(vo.getItemCode().split(","));
        }
        List<RatItemSumVO> ratItemSumList = new ArrayList<>();
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input,  RationConstant.CMS_RATION_RATION_QUERY, getUserId(), corpIds, ratItemSumList);
        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output,"银行授信额度录入表", CommonConstant.YES, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

}
