package com.bytter.bill.web.bill.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.domain.bill.entity.AbBriefStatus;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.util.ExportUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 票据查询入口
 * @date 2017-09-23 15:25:10
 */
@Controller
@RequestMapping("/bill/accebill/abbillQuery")
public class AbBillQueryController extends BaseController {
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbBillQueryService abBillQueryService;
    @Autowired
    private AbExcelExportService abExcelExportService;
    @Autowired
    private AbReceiveBillService abReceiveBillService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbBillReceivebizService abBillReceivebizService;

    /**
     * 银承占比及额度使用情况统计表 页面跳转
     *
     * @param form  前端传入参数
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listTargetProportionAndQuotaUsageStatistics")
    public String listAcUsageRatioStatistics(AbBillTargetProportionAndQuotaUsageVo form, Model model) {
        // 查询日期默认为上个月
        String ym = DateUtil.format(DateUtil.lastMonth(), "yyyy-MM");
        if (StringUtils.isEmpty(form.getYm())) {
            form.setYm(ym);
        }
        // 查询银行授信余额数据
        List<AbBillTargetProportionAndQuotaUsageVo> resultList = abBillQueryService.listAcUsageRatioStatistics(form.getYm(), this.getCurrentUser());
        // 传入前端
        model.addAttribute("resultList", resultList);
        model.addAttribute("form", form);
        model.addAttribute("defaultYm", ym);
        return "bill/accebill/listTargetProportionAndQuotaUsageStatistics";
    }

    /**
     * 导出银行授信余额表
     *
     * @param form     查询参数
     * @param request  请求参数
     * @param response 相应参数
     * @param model    model对象
     * @throws Exception 异常
     */
    @PostMapping("/exportAcUsageRatioStatistics")
    public void export(AbBillTargetProportionAndQuotaUsageVo form, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String filename = "银承占比及额度使用情况统计表.xls";
        if (ServletUtils.isIE(request)) {// 判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        // 查询日期默认为上个月
        if (StringUtils.isEmpty(form.getYm())) {
            form.setYm(DateUtil.format(DateUtil.lastMonth(), "yyyy-MM"));
        }
        // 查询银行授信余额数据
        List<AbBillTargetProportionAndQuotaUsageVo> resultList = abBillQueryService.listAcUsageRatioStatistics(form.getYm(), this.getCurrentUser());
        // 导出数据
        HSSFWorkbook workbook = abBillQueryService.exportAcUsageRatioStatistics(resultList, form.getYm());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 银承开票金额统计表 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listBankOpenBillAmtStatistics")
    public String listBankOpenBillAmtStatistics(AbBillBankOpenBillAmtStatisticsVo form, Model model) {
        // 默认日期区间
        Date defaultStartDate = DateUtil.beginOfMonth(DateUtil.date());
        Date defaultEndDate = DateUtil.endOfMonth(DateUtil.date());
        model.addAttribute("defaultStartDate", defaultStartDate);
        model.addAttribute("defaultEndDate", defaultEndDate);
        // 设置默认查询日期区间
        if (StringUtils.isEmpty(form.getStartQueryDate())) {
            form.setStartQueryDate(DateUtils.formatDate(defaultStartDate));
        }
        if (StringUtils.isEmpty(form.getEndQueryDate())) {
            form.setEndQueryDate(DateUtils.formatDate(defaultEndDate));
        }
        model.addAttribute("form", form);

        // 查询银行授信余额数据
        List<AbBillBankOpenBillAmtStatisticsVo> resultList = abBillQueryService.listBankOpenBillAmtStatistics(form, this.getCurrentUser());
        // 传入前端
        model.addAttribute("resultList", resultList);
        return "bill/accebill/listBankOpenBillAmtStatistics";
    }

    /**
     * 导出银行授信余额表
     *
     * @param form     查询参数
     * @param request  请求参数
     * @param response 相应参数
     * @param model    model对象
     * @throws Exception 异常
     */
    @PostMapping("/exportBankOpenBillAmtStatistics")
    public void exportBankOpenBillAmtStatistics(AbBillBankOpenBillAmtStatisticsVo form, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String filename = "银承开票金额统计表.xls";
        if (ServletUtils.isIE(request)) {// 判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        Date defaultStartDate = DateUtil.beginOfMonth(DateUtil.date());
        Date defaultEndDate = DateUtil.endOfMonth(DateUtil.date());
        // 设置默认查询日期区间
        if (StringUtils.isEmpty(form.getStartQueryDate())) {
            form.setStartQueryDate(DateUtils.formatDate(defaultStartDate));
        }
        if (StringUtils.isEmpty(form.getEndQueryDate())) {
            form.setEndQueryDate(DateUtils.formatDate(defaultEndDate));
        }
        // 查询银行授信余额数据
        List<AbBillBankOpenBillAmtStatisticsVo> resultList = abBillQueryService.listBankOpenBillAmtStatistics(form, this.getCurrentUser());
        // 导出数据
        HSSFWorkbook workbook = abBillQueryService.exportBankOpenBillAmtStatistics(resultList);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 票据解付与解付账户前一天余额对比表 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listBankAccBalCompare")
    public String listBankAccBalCompare(Model model) {
        return "bill/accebill/listBankAccBalCompare";
    }

    /**
     * 加载票据解付与解付账户前一天余额对比表数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryBankAccBalCompareData")
    @ResponseBody
    @LogMessage(content = "票据解付与解付账户前一天余额对比表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQueryBankAccBalCompareData(@RequestBody DataTablesInput dataTablesInput) {
        return abBillQueryService.dataQueryBankAccBalCompareData(dataTablesInput);
    }

    /**
     * 月度还银承表 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listAbBillBabByMonth")
    public String listAbBillBabByMonth(Model model) {
        model.addAttribute("beginDate", DateUtil.beginOfMonth(DateUtil.date()));
        model.addAttribute("endDate", DateUtil.endOfMonth(DateUtil.date()));
        return "bill/accebill/listAbBillBabByMonth";
    }

    /**
     * 加载月度还银承表数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryBillBabByMonthTable")
    @ResponseBody
    @LogMessage(content = "月度还银承表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQueryBillBabByMonthTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput output = abBillQueryService.dataQueryBillBabByMonthTable(dataTablesInput);
        //分类汇总
        Map<String, List<AbBillBabVo>> fldata = new LinkedHashMap<>();
        List<AbBillBabVo> list = output.getData();
        if (list == null || list.size() == 0) {
            return output;
        }
        if (CollectionUtil.isNotEmpty(list)) {
            for (AbBillBabVo vo : list) {
                String key = StringUtils.isBlank(vo.getParentName()) ? "空" : (vo.getParentName() + "汇总");
                if (fldata.get(key) == null) {
                    List<AbBillBabVo> mList = new ArrayList<>();
                    mList.add(vo);
                    fldata.put(key, mList);
                } else {
                    fldata.get(key).add(vo);
                }
            }
        }
        List<AbBillBabVo> newRetrunList = new ArrayList<>();
        for (String key : fldata.keySet()) {
            AbBillBabVo vo = new AbBillBabVo();
            vo.setParentName(key);
            BigDecimal billMoney = BigDecimal.ZERO;
            BigDecimal bailMoney = BigDecimal.ZERO;
            BigDecimal applyBackMoney = BigDecimal.ZERO;
            BigDecimal settleMoney = BigDecimal.ZERO;
            for (AbBillBabVo abBillBabVo : fldata.get(key)) {
                billMoney = billMoney.add(abBillBabVo.getBillMoney());
                bailMoney = bailMoney.add(abBillBabVo.getBailMoney());
                applyBackMoney = applyBackMoney.add(abBillBabVo.getApplyRepayMoney());
                settleMoney = settleMoney.add(abBillBabVo.getSettleMoney());
            }
            vo.setBailMoney(bailMoney);
            vo.setBillMoney(billMoney);
            vo.setSettleMoney(settleMoney);
            vo.setApplyRepayMoney(applyBackMoney);
            newRetrunList.add(vo);
            newRetrunList.addAll(fldata.get(key));
        }

        AbBillBabVo sum = new AbBillBabVo();
        sum.setParentName("合计");
        // 票面金额
        sum.setBillMoney(list.stream().filter(k -> CharSequenceUtil.isNotEmpty(k.getParentId()))
                .map(AbBillBabVo::getBillMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 保证金金额
        sum.setBailMoney(list.stream().filter(k -> CharSequenceUtil.isNotEmpty(k.getParentId()))
                .map(AbBillBabVo::getBailMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 申请还款金额
        sum.setApplyRepayMoney(list.stream().filter(k -> CharSequenceUtil.isNotEmpty(k.getParentId()))
                .map(AbBillBabVo::getApplyRepayMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 结算金额
        sum.setSettleMoney(list.stream().filter(k -> CharSequenceUtil.isNotEmpty(k.getParentId()))
                .map(AbBillBabVo::getSettleMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        newRetrunList.add(sum);
        output.setData(newRetrunList);
        return output;
    }

    /**
     * 票据明细信息 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listQueryDtl")
    public String listQueryDtl(Model model) {
        return "bill/accebill/listAbBillQueryDtl";
    }

    /**
     * 加载票据明细信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryDtltable")
    @ResponseBody
    @LogMessage(content = "查询票据明细信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQueryDtlTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillQueryService.listAbBillQueryDtlInfo(dataTablesInput);
    }
//	@PostMapping("/exportExcel")
//	public void exportExcel(DataTablesInput dataTablesInput,ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
//		ExportParams exportParams = new ExportParams("附件列表","附件信息", ExcelType.XSSF);
//		abExcelExportService.exportExcel(AbAccrecordsVO.class,"abBillQueryService",dataTablesInput,exportParams,modelMap,request,response);
//	}

    /**
     * 票据业务信息 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listQueryBus")
    public String listQueryBus(Model model) {
        return "bill/accebill/listAbBillQueryBus";
    }

    /**
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     * 加载票据业务信息数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryBusTable")
    @ResponseBody
    @LogMessage(content = "查询票业务细信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQueryBusTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput info = abBillQueryService.listAbBillQueryBusInfo(dataTablesInput);
        return info;
    }

    /**
     * 票据贴现信息 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listQueryDiscount")
    public String listAbBillDiscountQuery(Model model) {
        return "bill/accebill/listAbBillQueryDiscount";
    }

    /**
     * 加载票据贴现信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryDiscountTable")
    @ResponseBody
    @LogMessage(content = "查询票业务细信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQueryDiscountTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput info = abBillQueryService.listAbBillQueryDiscountInfo(dataTablesInput);
        return info;
    }

    /**
     * 查询所有业务类型
     *
     * @return
     */
    @PostMapping("/listBusType")
    @ResponseBody
    @LogMessage(content = "查询所有业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public List<AbBriefStatus> listBusType() {
        return abBillQueryService.listBusTypeInfo();
    }

    /**
     * 单位票据统计表 显示
     *
     * @param vo    查询条件
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/billUnitLstatistics")
    @LogMessage(content = "查询单位票据统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String billUnitLstatistics(AbAccrecordsVO vo, Model model) {
        if (vo.getFirstTime() == null || vo.getFirstTime()) {
            vo.setBeginDate(DateUtils.getDateYMD(DateUtils.addMonth(DateUtils.getDateYMD(), -1)));
            vo.setEndDate(DateUtils.getDateYMD());
            model.addAttribute("beginDate", DateUtils.formatDate(vo.getBeginDate(), "yyyy-MM-dd"));
            model.addAttribute("endDate", DateUtils.formatDate(vo.getEndDate(), "yyyy-MM-dd"));
        } else {
            if (vo.getBeginDate() != null) {
                model.addAttribute("beginDate", DateUtils.formatDate(vo.getBeginDate(), "yyyy-MM-dd"));
            }
            if (vo.getEndDate() != null) {
                model.addAttribute("endDate", DateUtils.formatDate(vo.getEndDate(), "yyyy-MM-dd"));
            }
        }
        model.addAttribute("firstTime", false);
        abBillQueryService.getBillUnitLstatisticsInfo(vo, model, this.getCurrentUser());
        return "bill/accebill/listBillUnitLstatistics";
    }

    /**
     * 单位票据统计表明细页面示
     *
     * @param vo    查询条件
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/billUnitLstatisticsDtl")
    @LogMessage(content = "查询单位票据统计表明细页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String toBillUnitLstatisticsDtl(AbAccrecordsVO vo, Model model) {
        model.addAttribute("infoParams", vo);
        return "bill/accebill/listBillUnitLstatisticsDtl";
    }

    /**
     * 加载单位票据统计表明细信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @PostMapping("/billUnitLstatisticsDtlData")
    @ResponseBody
    @LogMessage(content = "查询单位票据统计表明细信息数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput queryBillUnitLstatisticsDtlData(@RequestBody DataTablesInput dataTablesInput) {
        return abBillQueryService.queryBillUnitLstatisticsDtlData(dataTablesInput);
    }

    /**
     * 应收票据台账查询 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listBillReceAccount")
    public String listBillReceAccount(Model model) {
        String receDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        model.addAttribute("receDate", receDate);
        return "bill/accebill/listBillReceAccount";
    }

    /**
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     * 加载应收票据台账数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/billReceDataTable")
    @ResponseBody
    @LogMessage(content = "应收票据台账列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput billReceDataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput info = abBillQueryService.billReceDataTable(dataTablesInput);
        return info;
    }

    /**
     * 背书转让明细页面
     *
     * @param vo    查询条件
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/endorsementCountDtl")
    @LogMessage(content = "查询背书转让明细页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String endorsementCountDtl(AbAccrecordsVO vo, Model model) {
        if (StringUtils.isEmpty(vo.getRecid())) {
            AbVoucherinfoVO abBillInfo = abReceiveBillService.getAbBillInfoById(vo.getId());
            AbVoucherinfoVO abVoucherinfoVO = abVoucherinfoService.getAbVoucherinfoVOByAccId(vo.getId());
            if (abVoucherinfoVO != null) {
                if (StringUtils.isEmpty(abBillInfo.getBanendrsmtmkName())) {
                    abBillInfo.setBanendrsmtmkName(abVoucherinfoVO.getBanendrsmtmkName());
                }
                abBillInfo.setBillFlagName(abVoucherinfoVO.getBillFlag() == 10 ? "纸票" : "电票");
            }
            model.addAttribute("abReceiveBillPage", abBillInfo);
        } else {
            AbBillReceivebizVO receivebizVO = abBillReceivebizService.getAbBillReceivebizVOById(vo.getRecid());
            AbVoucherinfoVO abVoucherinfoVO = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(receivebizVO.getOrigNo(), receivebizVO.getSbbllRngStrtSn(), receivebizVO.getSbbllRngEndSn());
            if (abVoucherinfoVO != null) {
                receivebizVO.setBillFlagName(abVoucherinfoVO.getBillFlag() == 10 ? "纸票" : "电票");
            }
            model.addAttribute("abReceiveBillPage", receivebizVO);
        }
        return "bill/accebill/endorsementCountDtl";
    }

    /**
     * 加载背书转让详情列表数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/endorsementCountData")
    @ResponseBody
    @LogMessage(content = "背书转让详情列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput endorsementCountData(@RequestBody DataTablesInput dataTablesInput) {
        return abBillQueryService.endorsementCountData(dataTablesInput);
    }

    /**
     * 应付票据台账查询 页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listBillPayAccount")
    public String listBillPayAccount(String corpId, String bankAcc, Date endDate, BigDecimal bailPercent, Model model) {
        String startDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        model.addAttribute("startDate", startDate);
        if (StringUtils.isNotBlank(corpId)) {
            model.addAttribute("corpId", corpId);
        }
        if (StringUtils.isNotBlank(bankAcc)) {
            model.addAttribute("bankAcc", bankAcc);
        }
        if (endDate != null) {
            model.addAttribute("endDate", endDate);
        }
        if (bailPercent != null) {
            model.addAttribute("bailPercent", bailPercent);
        }
        return "bill/accebill/listBillPayAccount";
    }

    /**
     * 加载应付票据台账数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/billPayDataTable")
    @ResponseBody
    @LogMessage(content = "应付票据台账列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput billPayDataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput info = abBillQueryService.billPayDataTable(dataTablesInput, abVoucherinfoViceService.isEmitMake());
        return info;
    }

    /**
     * 单位信息统计表导出
     *
     * @param request  request
     * @param response response
     * @param vo       查询条件
     * @param model    model
     */
    @LogMessage(content = "导出单位信息统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportUnitCountExcel")
    public void exportUnitCountExcel(HttpServletRequest request, HttpServletResponse response, AbAccrecordsVO vo, Model model) throws Exception {
        // 导出的文件名
        String fileName = "单位票据统计表.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        //导出数据
        HSSFWorkbook workbook = abBillQueryService.createUnitCountExcelContent(vo, model, getCurrentUser());
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
     * 应付票据台账导出
     *
     * @param dataTablesInput 查询入参
     * @param modelMap
     * @param request
     * @param response
     * @return 返回列表数据信息
     */
    @PostMapping("/exportPayExcelByMap")
    public void exportPayExcelByMap(String params, String ids, DataTablesInput dataTablesInput, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 转换前台传来的数据为JSON
        params = URLDecoder.decode(params, "utf-8").replaceAll("&quot;", "\"");
        // 用于排除当出现多余的列
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        dataTablesInput = mapper.readValue(params, DataTablesInput.class);
        // 配置导出要素
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        if (StringUtils.isNotBlank(ids)) {
            dataTablesInput.getFormdata().put("ins_id", URLDecoder.decode(ids, "utf-8").replaceAll("&quot;", "\""));
        }
        if (StringUtils.isBlank(dataTablesInput.getTitle())) {
            dataTablesInput.setTitle("应付票据台账导出");
        }
        if (StringUtils.isBlank(dataTablesInput.getFilename())) {
            dataTablesInput.setFilename("应付票据台账导出");
        }
        if (modelMap.get("dataTablesInput") == null) {
            modelMap.put("dataTablesInput", dataTablesInput);
        }
        abBillQueryService.listPayData(dataTablesInput, abVoucherinfoViceService.isEmitMake(), modelMap, entityList, request, response);
    }

    /**
     * 应收票据台账导出
     *
     * @param modelMap
     * @param request
     * @param response
     * @return 返回列表数据信息
     */
    @PostMapping("/exportReceiveExcelByMap")
    public void exportReceiveExcelByMap(String params, DataTablesInput dataTablesInput, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 转换前台传来的数据为JSON
        params = URLDecoder.decode(params, "utf-8").replaceAll("&quot;", "\"");
        // 用于排除当出现多余的列
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        dataTablesInput = mapper.readValue(params, DataTablesInput.class);
        // 配置导出要素
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        abBillQueryService.listReceiveData(dataTablesInput, modelMap, entityList, request, response);
    }

    /**
     * 应付票据导出全部
     *
     * @return
     */
    @RequestMapping("/exportPayAll")
    public void exportPayAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                dataTablesInput.setMoneySum(false);
                // 统计列名
                dataTablesInput.setTotalColumn("applyCorpName");
                // 统计数据列名
                dataTablesInput.addTotalList("billMoney","bailMoney","payMoney");

                DataTablesOutput info = abBillQueryService.billPayDataTable(dataTablesInput, abVoucherinfoViceService.isEmitMake());
                List<AbBillPayAndReceVO> list = info.getData();
                BigDecimal billMoney = new BigDecimal(0);
                BigDecimal bailMoney = new BigDecimal(0);
                BigDecimal payMoney = new BigDecimal(0);
                if (ObjectUtil.isNotEmpty( list)){
                    billMoney = list.stream().map(AbBillPayAndReceVO::getBillMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    bailMoney = list.stream().map(AbBillPayAndReceVO::getBailMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    payMoney = list.stream().map(AbBillPayAndReceVO::getPayMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                }

                // 合计列
                AbBillPayAndReceVO vo = new AbBillPayAndReceVO();
                vo.setApplyCorpName("合计");
                vo.setBillMoney(billMoney);
                vo.setBailMoney(bailMoney);
                vo.setPayMoney(payMoney);
                list.add(vo);
                return list;
            }
        }, totalParam, fileNameParam,"应付票据台账", 5000, AbBillPayAndReceVO.class);
    }
}
