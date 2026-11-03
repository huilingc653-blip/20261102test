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
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.service.RatBkrationDisService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.service.RatUrationService;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatUrationBean;
import com.bytter.credit.domain.vo.RatUrationQueryVO;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.credit.dto.RatUrationDtlDTO;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信占用结转台账表
 * @date 2017-08-19 11:36:37
 */
@Controller
@RequestMapping("/cms/rat/raturation")
public class RatUrationController extends BaseController {
    @Autowired
    private RatUrationService ratUrationService;
    @Autowired
    private RatBkrationService bkrationService;
    @Autowired
    private RatBkrationDisService disService;
    @DubboReference(check = false)
    private IBtCurrencyService curService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IInputBankInfoService bankInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 获取所有授信银行信息
     *
     * @return
     */
    @GetMapping("/getRationBank")
    @ResponseBody
    public List<BtBankDTO> json() {
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        return bankDTOs;
    }

    /**
     * 授信占用台账表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询授信占用台账表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/rat/bkration/listRatUration";
    }

    /**
     * 授信占用台账表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        Object beginDate = input.getFormdata().get("beginDate");
        if (!EmptyUtils.isEmpty(beginDate)) {
            input.addFilter(MatchTypeEnum.LE, "beginDate", beginDate.toString(), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "endDate", beginDate.toString(), Date.class);// 小于等于授信结束日期
        }
        return ratUrationService.queryRatURationList(input, RationConstant.CMS_RATION_CHANGE_APPLY, getUserId());
    }

    /**
     * 功能介绍 : 跳转授信额度单选择。<br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @LogMessage(content = "跳转授信额度单选择页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/chooseration")
    public String chooseration(String urationIds, String changeType, Model model) {
        List<RatUrationBean> urationBeans = ratUrationService.queryChangeRation(urationIds, changeType);
        model.addAttribute("urationBeans", urationBeans);
        model.addAttribute("changeType", changeType);
        boolean isShowBtn = false; // 默认不显示【确定】【取消】按钮
        if (!CollectionUtils.isEmpty(urationBeans)) {
            List<RatUrationBean> filterList = urationBeans.stream().filter(item -> !CollectionUtils.isEmpty(item.getRationDtls())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterList)) {
                isShowBtn = true;
            }
            model.addAttribute("isShowBtn", isShowBtn);
        }
        return "cms/rat/bkration/ratUrationChoose";
    }

    /**
     * 授信占用台账表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatUration ratUration, Model model) {
        if (StringUtils.isNotEmpty(ratUration.getId())) {
            ratUration = ratUrationService.get(ratUration.getId());
            model.addAttribute("ratUrationPage", ratUration);
        }
        return "cms/rat/bkration/addRatUration";
    }

    /**
     * 授信占用台账表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatUration ratUration, Model model) {
        if (StringUtils.isNotEmpty(ratUration.getId())) {
            ratUration = ratUrationService.get(ratUration.getId());
            model.addAttribute("ratUrationPage", ratUration);
        }
        return "cms/rat/bkration/updateRatUration";
    }

    /**
     * 授信占用台账表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信占用台账表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RatUration ratUration = ratUrationService.get(id);
        model.addAttribute("ratUrationPage", ratUration);
        return "cms/rat/bkration/viewRatUration";
    }

    /**
     * 授信跨年结转
     *
     * @return
     */
    @LogMessage(content = "授信跨年结转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/rationChange")
    @ResponseBody
    public AjaxJson rationChange(String urationIds, String changeType) {
        ratUrationService.changeRationCode(urationIds, changeType, getUserId());
        return success();
    }

    /**
     * 添加授信占用台账表
     *
     * @param ratUration
     * @return
     */
    //
    @LogMessage(content = "添加授信占用台账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatUration ratUration) {
        ratUrationService.save(ratUration);
        return success();
    }

    /**
     * 修改授信占用台账表
     *
     * @param ratUration
     * @return
     */
    //
    @LogMessage(content = "修改授信占用台账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatUration ratUration) {
        RatUration t = ratUrationService.get(ratUration.getId());
        BeanUtils.copyNotNullProperties(ratUration, t);
        ratUrationService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除授信占用台账表
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信占用台账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatUration ratUration = ratUrationService.get(id);
        ratUrationService.deleteEntity(ratUration);
        return success();
    }

    /**
     * 授信选择页面
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toRationDtl")
    public String toRationDtl(UrationParamDTO paramDTO, RatUrationDTO urationDTO, Model model) {
        paramDTO.setRatCode(null);
        paramDTO.setItemCode(urationDTO.getRatItemCode());
        paramDTO.setStatus(RationConstant.STATUS_CHECK.toString());// 选择授信时只能选择审批通过状态的数据
        String bankCtlType = configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_BANK_CONTROLTYPE).getValue();// 授信银行控制
        paramDTO.setBankCtlType(bankCtlType);
        if (EmptyUtils.isNotEmpty(bankCtlType) && bankCtlType.equals(RationConstant.RAT_BANKTYPE_SAME)) {
            if (EmptyUtils.isNotEmpty(paramDTO.getBankCode())) {
                InputBankInfoDTO bankInfoDTO = bankInfoService.getByBankCode(paramDTO.getBankCode());// 根据联行号查询银行信息
                if (null != bankInfoDTO) {
                    // String bankPrefix=bankInfoDTO.getBankPrefix();
                    paramDTO.setBankPrefix(bankInfoDTO.getBankTypeId());
                }
            }
        }
        List<RatUrationDisDTO> rations = ratUrationService.queryCorpDisRation(paramDTO, urationDTO);
        if (!CollectionUtils.isEmpty(rations)) {
            for (RatUrationDisDTO dto : rations) {
                InputBankInfoDTO bankInfoDTO1 = bankInfoService.getByBankCode(dto.getBankCode());// 根据联行号查询银行信息
                if (bankInfoDTO1 != null) {
                    dto.setBankTypeName(bankInfoDTO1.getBankTypeName());
                }
            }
        }
        model.addAttribute("rations", rations);
        if (EmptyUtils.isEmptyList(rations)) {
            model.addAttribute("rationDtls", "0");// 无符合条件的授信合同
        } else {
            model.addAttribute("rationDtls", "1");
        }
        return "cms/rat/bkration/ratUrationSelect";
    }

    /**
     * 查看已选授信单
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRationDtl")
    public String viewRationDtl(RatUrationDTO urationDTO, String isRmbRat, Model model) {
        if (CommonConstant.YES.equals(urationDTO.getItemFlag())) {
            List<RatUrationDtlDTO> applyDtls = new ArrayList<>();
            RatUrationDtlDTO ratUrationDtlDTO = new RatUrationDtlDTO();
            ratUrationDtlDTO.setItemCode(urationDTO.getItemCode());
            ratUrationDtlDTO.setUseMaxMoney(urationDTO.getUseMaxMoney());
            applyDtls.add(ratUrationDtlDTO);
            urationDTO.setApplyDtls(applyDtls);
        }
        List<RatUrationDisDTO> rations = ratUrationService.viewCorpDisRation(urationDTO, isRmbRat);
        model.addAttribute("rations", rations);
        return "cms/rat/bkration/ratUrationDetail";
    }

    /**
     * 银行授信额度明细查询
     *
     * @return
     */
    @LogMessage(content = "银行授信额度明细查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/rationQuery")
    public String rationQuery(UrationParamDTO paramDTO, Model model) {
        // 显示已分配的单位、银行
        String corpSql = "";
        List<CorpDTO> corpDTOs = disService.queryRationCorp(corpSql);
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        if (EmptyUtils.isEmpty(paramDTO.getMoneyType())) {
            paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        }
        BtCurrencyDTO currencyDTO = curService.getNatualCurrency();// 本位币
        if (EmptyUtils.isEmpty(paramDTO.getCurCode())) {
            paramDTO.setCurCode(currencyDTO.getId());
        }
        List<RatUrationUseVO> rations = null;
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        // 显示单位
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATIONQUERY_QUERY,
                this.getCurrentUser().getId());
        String corpId = paramDTO.getCorpId();// 保存每次查询的单位
        if (StringUtils.isEmpty(paramDTO.getCorpId())) {// 未选择查询的单位则默认查询当前用户下所有单位的
            String tempStr = StringUtils.listTtoString(corpAuth);
            paramDTO.setCorpId(tempStr);
        }
        rations = ratUrationService.queryRationUseDtl(paramDTO);
        String bankSign = RationConstant.RAT_COMMON_N;
        if (!EmptyUtils.isEmptyList(bankDTOs)) {
            bankSign = RationConstant.RAT_COMMON_Y;
        }
        model.addAttribute("corpId", corpId);
        model.addAttribute("currencyDTO", rations);
        model.addAttribute("rations", rations);
        model.addAttribute("corpDTOs", corpDTOs);
        model.addAttribute("bankDTOs", bankDTOs);
        model.addAttribute("bankSign", bankSign);
        model.addAttribute("paramDTO", paramDTO);
        model.addAttribute("naturalCur", currencyDTO.getId());
        model.addAttribute("relateCon", bkrationService.getRelateCon());
        return "cms/rat/bkration/listRationQuery";
    }

    /**
     * 查看授信占用情况
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRatioUse")
    public String viewRatioUse(RatUrationQueryVO queryVO, Model model) {
        List<RatUrationUseBeanVO> totalrations = ratUrationService.queryCorpRationUseDtl(queryVO);
        List<RatUrationUseBeanVO> rations = new ArrayList<RatUrationUseBeanVO>();
        // 在占用明细页面,隐藏掉占用金额为0的数据
        for (RatUrationUseBeanVO rat : totalrations) {
            if ((!EmptyUtils.isEmpty(rat.getConvertPreMoney())
                    && rat.getConvertPreMoney().compareTo(BigDecimal.ZERO) > 0)
                    || (!EmptyUtils.isEmpty(rat.getBillMoney()) && rat.getBillMoney().compareTo(BigDecimal.ZERO) > 0)) {
                rations.add(rat);
            }
        }
        model.addAttribute("rations", rations);
        model.addAttribute("queryVO", queryVO);
        return "cms/rat/bkration/rationQueryDetail";
    }

    /**
     * 银行授信额度明细查询页上传合同按钮页面跳转
     *
     * @param ratCode 授信录入主键id
     * @param model   model对象
     * @return 跳转页面
     */
    @LogMessage(content = "授信额度录入上传合同", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/uploadAttachFile/{ratCode}")
    public String uploadAttachFile(@PathVariable("ratCode") String ratCode, Model model) {
        RatBkrationPageVO ratBkration = bkrationService.getBkRationBeanById(ratCode);
        if (StringUtils.isNotEmpty(ratBkration.getOldRatCode())) {
            RatBkration oldBkration = bkrationService.get(ratBkration.getOldRatCode(), true);
            ratBkration.setOldRatCode(oldBkration.getRatCode());
        }
        model.addAttribute("ratBkrationPage", ratBkration);

        ConfigDTO bailConfig = bkrationService.getIsHangBail();// 参数设置是否挂接保证金
        model.addAttribute("isHangBail", bailConfig.getValue());
        ConfigDTO guaConfig = bkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        model.addAttribute("relateCon", bkrationService.getRelateCon());
        model.addAttribute("conType", bkrationService.getRatConType());
        model.addAttribute("conId", ratBkration.getConId());

        return "cms/rat/bkration/uploadAttachFile";
    }

    /**
     * 保存上传合同附件
     *
     * @param rationtDTO 入参对象
     * @return 出参对象
     */
    @LogMessage(content = "保存上传合同附件", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveAttachFile")
    @ResponseBody
    public AjaxJson saveAttachFile(RatBkRationVm rationtDTO) {
        RatBkration ratBkration = bkrationService.get(rationtDTO.getId(), true);
        if (!ObjectUtils.isEmpty(ratBkration)) {
            // 保存上传合同附件
            ratBkration.setContractAttachfilepath(rationtDTO.getContractAttachfilepath());
            ratBkration.setUpdateBy(getUserId());
            ratBkration.setUpdateDate(DateUtils.getDate());
            bkrationService.saveOrUpdate(ratBkration);
        }
        return success();
    }

    /**
     * 页面跳转
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/RaturationUsedItemDetailQuery")
    public String ViewRaturationUsedItemDetail() {
        return "cms/rat/bkration/listRatUrationItemUseDetail";
    }

    /**
     * 银行授信业务品种明细数据明细查询
     *
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "银行授信业务品种明细数据明细查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getRaturationUsedItemDetail")
    @ResponseBody
    public DataTablesOutput getRaturationUsedItemDetail(@RequestBody DataTablesInput dataTablesInput) {
        return ratUrationService.queryRaturationUsedItemDetail(dataTablesInput, getCurrentUser());
    }

    /**
     * @return
     */
    @PostMapping("/getBanks")
    @ResponseBody
    public List<BtBankDTO> getBanks() {
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        return bankDTOs;
    }

    @PostMapping("/exportExcel")
    public void doExportInterestBill(UrationParamDTO paramDTO, HttpServletRequest request, HttpServletResponse response,
                                     Model model) throws Exception {
        String filename = "银行授信额度明细.xls";
        if (isIE(request)) {// 判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        // 显示已分配的单位、银行
        if (EmptyUtils.isEmpty(paramDTO.getMoneyType())) {
            paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        }
        Date beginDate = paramDTO.getBeginDate();
        if (null == beginDate) {
            beginDate = DateUtils.getDate();
            paramDTO.setBeginDate(beginDate);
        }
        Date endDate = paramDTO.getEndDate();
        if (null == endDate) {
            endDate = DateUtils.getDate();
            paramDTO.setEndDate(endDate);
        }
        BtCurrencyDTO currencyDTO = curService.getNatualCurrency();// 本位币
        if (EmptyUtils.isEmpty(paramDTO.getCurCode())) {
            paramDTO.setCurCode(currencyDTO.getId());
        }
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        HSSFWorkbook workbook = ratUrationService.exportExcel(paramDTO, getCurrentUser());
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 银行授信额度占用明细导出
     *
     * @param queryVO  查询条件
     * @param request  request
     * @param response response
     */
    @PostMapping("/exportOccupyDtl")
    public void exportOccupyDtl(RatUrationQueryVO queryVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 导出的文件名
        String fileName = "银行授信额度占用明细.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        //导出数据
        List<RatUrationUseBeanVO> totalrations = ratUrationService.queryCorpRationUseDtl(queryVO);
        List<RatUrationUseBeanVO> rations = new ArrayList<RatUrationUseBeanVO>();
        // 隐藏掉占用金额为0的数据
        for (RatUrationUseBeanVO rat : totalrations) {
            if ((!EmptyUtils.isEmpty(rat.getConvertPreMoney())
                    && rat.getConvertPreMoney().compareTo(BigDecimal.ZERO) > 0)
                    || (!EmptyUtils.isEmpty(rat.getBillMoney()) && rat.getBillMoney().compareTo(BigDecimal.ZERO) > 0)) {
                rations.add(rat);
            }
        }
        HSSFWorkbook workbook = ratUrationService.exportOccupyDtl(rations);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 判断是否是IE
     *
     * @param request
     * @return
     */
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0
                || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
