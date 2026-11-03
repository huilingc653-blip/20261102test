package com.bytter.credit.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationAdjust;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.service.*;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.finance.constant.ResourceConstant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 综合授信最高额度调整
 *
 * @author luxy
 * Title: RatBkrationAdjustController
 */
@Controller
@RequestMapping("/cms/rat/ratbkrationadjust")
public class RatBkrationAdjustController extends BaseController {
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatBkrationDisService ratBkrationDisService;
    @Autowired
    private RatBkrationAdjustService bkrationAdjustService;
    @Autowired
    private RatBkrationAdjustDtlService adjustDtlService;
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatBkrationDisDetailService ratBkrationDisDetailService;

    @RequestMapping("/ratAdjust")
    @LogMessage(content = "综合授信最高额度调整列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String ratAdjustIndexTab(RatBkrationVO rationVO, Model model) {
        String url = "";
        if (StringUtils.isEmpty(rationVO.getType())) {
            url = "cms/rat/bkration/ratBkAdjustIndexTab";
        } else if ("input".equals(rationVO.getType())) {
            url = ratInputAdjust(rationVO, 1, 10, model);
        } else if ("dis".equals(rationVO.getType())) {
            url = ratDisAdjust(rationVO, 1, 10, model);
        }
        return url;
    }

    @LogMessage(content = "授信录入综合授信最高额度调整列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/ratInputAdjust")
    public String ratInputAdjust(RatBkrationVO rationVO, Integer start, Integer len, Model model) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());

        this.setSearchParams(model, input, rationVO);
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkInputCanAdjust(input, RationConstant.CMS_RATION_ADJUST_QUERY, getUserId());

        Map<String, Object> result = new HashMap<>();
        long resultCount = (int) outputTables.getRecordsTotal();//查询出的总数据量减掉只有单品种的数据量
        int pageSize = input.getLength();
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }
        result.put("rations", outputTables.getData());
        result.put("curr", start);
        result.put("pageCount", pageCount);
        result.put("pageSize", pageSize);
        result.put("resultCount", resultCount);

        model.addAttribute("model", result);
        return "cms/rat/bkration/ratBkInputAdjustTab";
    }

    /**
     * 最高额度调整授信录入导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "最高额度调整授信录入导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkInputAdjustTab")
    public void exportListRatBkInputAdjustTab(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "最高额度调整授信录入表.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(rationVO.getExportType())) {
            // 导出当前页
            if (null == rationVO.getStart()) {
                rationVO.setStart(1);
            }
            if (null == rationVO.getPageSize()) {
                rationVO.setPageSize(10);
            }
            input.setStart((rationVO.getStart() - 1) * rationVO.getPageSize());
            input.setLength(rationVO.getPageSize());
        } else {
            input.setStart(1);
            input.setLength(99999999);
        }
        //查询参数设置
        setSearchParams(model, input, rationVO);
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkInputCanAdjust(input, ResourceConstant.CMS_RATION_ADJUST_QUERY, getUserId());

        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(outputTables, "最高额度调整授信录入表", CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    @LogMessage(content = "授信分配综合授信最高额度调整列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/ratDisAdjust")
    public String ratDisAdjust(RatBkrationVO rationVO, Integer start, Integer len, Model model) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());

        this.setSearchParams(model, input, rationVO);
        // 取金额共享分配数据
        input.addFilter(MatchTypeEnum.IN, "distributeWay", RationConstant.RAT_DISWAY_3);
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkDisCanAdjust(input, RationConstant.CMS_RATION_ADJUST_QUERY, getUserId());

        Map<String, Object> result = new HashMap<>();
        long resultCount = (int) outputTables.getRecordsTotal(); // 查询出的总数据量减掉只有单品种的数据量
        int pageSize = input.getLength();
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }

        result.put("rations", outputTables.getData());
        result.put("curr", start);
        result.put("pageCount", pageCount);
        result.put("pageSize", pageSize);
        result.put("resultCount", resultCount);
        model.addAttribute("model", result);
        return "cms/rat/bkration/ratBkDisAdjustTab";
    }

    /**
     * 最高额度调整授信分配导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "最高额度调整授信分配导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkInputAdjustTabDis")
    public void exportListRatBkInputAdjustTabDis(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "最高额度调整授信分配表.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(rationVO.getExportType())) {
            // 导出当前页
            if (null == rationVO.getStart()) {
                rationVO.setStart(1);
            }
            if (null == rationVO.getPageSize()) {
                rationVO.setPageSize(10);
            }
            input.setStart((rationVO.getStart() - 1) * rationVO.getPageSize());
            input.setLength(rationVO.getPageSize());
        } else {
            input.setStart(1);
            input.setLength(99999999);
        }
        //查询参数设置
        setSearchParams(model, input, rationVO);
        input.addFilter(MatchTypeEnum.IN, "distributeWay", "3");//取金额共享分配数据
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkDisCanAdjust(input, ResourceConstant.CMS_RATION_ADJUST_QUERY, getUserId());

        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(outputTables, "最高额度调整授信分配表", CommonConstant.NO, CommonConstant.YES, CommonConstant.NO, CommonConstant.YES);
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
     * 设置查询参数
     *
     * @param model    视图
     * @param input    查询参数设置对象
     * @param rationVO 查询条件
     */
    private void setSearchParams(Model model, DataTablesInput input, RatBkrationVO rationVO) {
        //授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        model.addAttribute("ratType", ratType);

        // 授信单位
        String corpId = rationVO.getCorpId();
        if (!StringUtils.isEmpty(corpId)) {
            input.addFilter(MatchTypeEnum.IN, "corpId", corpId);
            model.addAttribute("corpId", corpId);
        }
        // 授信合同号
        if (!StringUtils.isEmpty(rationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", rationVO.getContractNo());
            model.addAttribute("contractNo", rationVO.getContractNo());
        }
        // 授信编号
        if (!StringUtils.isEmpty(rationVO.getRatCode()) && !"undefined".equals(rationVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", rationVO.getRatCode());
            model.addAttribute("ratCode", rationVO.getRatCode());
        }
        // 银行信息
        String bankCode = rationVO.getBankCode();
        if (!StringUtils.isEmpty(bankCode)) {
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCode);
            model.addAttribute("bankCode", bankCode);
        }
        // 授信品种
        String itemCode = rationVO.getItemCode();
        if (!StringUtils.isEmpty(itemCode)) {
            input.getFormdata().put("itemCode", itemCode);
            model.addAttribute("itemCode", itemCode);
        }
        // 授信模式
        String ratMode = rationVO.getRatMode();
        if (!StringUtils.isEmpty(ratMode)) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", ratMode);
            model.addAttribute("ratMode", ratMode);
        }
        // 授信开始时间段最大时间
        if (null != rationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
            model.addAttribute("gBeginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
        }
        // 授信开始时间段最小时间
        if (null != rationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
            model.addAttribute("lBeginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
        }
        // 授信结束时间段最大时间
        if (null != rationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(rationVO.getgEndDate()));
            model.addAttribute("gEndDate", DateUtils.formatDate(rationVO.getgEndDate()));
        }
        // 授信结束时间段最小时间
        if (null != rationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(rationVO.getlEndDate()));
            model.addAttribute("lEndDate", DateUtils.formatDate(rationVO.getlEndDate()));
        }
    }

    @GetMapping("addAdjust")
    @LogMessage(content = "综合授信最高额度调整页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String addAdjust(String businessId, String businessType, Model model) {
        DataTablesInput input = new DataTablesInput();
        input.addFilter(MatchTypeEnum.EQ, "businessId", businessId);
        input.addFilter(MatchTypeEnum.EQ, "businessType", businessType);
        input.addFilter(MatchTypeEnum.NE, "status", 95);
        DataTablesOutput onHandData = bkrationAdjustService.queryAdjustDataByParams(input);
        if (onHandData.getData().size() > 0) {
            List<RatBkrationAdjustVO> onHandAdjustVO = onHandData.getData();
            if (onHandAdjustVO.get(0).getStatus().equals(RationConstant.STATUS_TEMP)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_ADJUST_ONTEMP);
            } else if (onHandAdjustVO.get(0).getStatus().equals(RationConstant.STATUS_BACK)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_ADJUST_ONBACK);
            } else if (onHandAdjustVO.get(0).getStatus().equals(RationConstant.STATUS_WAIT_CHECK)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_ADJUST_ONCHECKED);
            }
        } else {
            if (businessType.equals(RationConstant.RAT_INPUT_ADJUST)) {
                RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(businessId);
                if (ratBkration.getRatMode().equals(RationConstant.RATMODE_0)) {// 集团授信
                    setRatBkrationDisDtl(ratBkration, model);
                    model.addAttribute("ratBkrationPage", ratBkration);
                } else if (ratBkration.getRatMode().equals(RationConstant.RATMODE_1)) {// 自主授信
                    RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(businessId, ratBkration.getCorpId());
                    bkrationAdjustService.setRatBkrationApplyDtl(pageVO, businessId);
                    model.addAttribute("ratBkrationPage", pageVO);
                }
            } else if (businessType.equals(RationConstant.RAT_DIS_ADJUST)) {
                RatBkrationDis ratBkrationDis = ratBkrationDisService.get(businessId);
                RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(ratBkrationDis.getRatCode(), ratBkrationDis.getCorpId());
                bkrationAdjustService.setRatBkrationApplyDtl(pageVO, ratBkrationDis.getRatCode());
                model.addAttribute("ratBkrationPage", pageVO);
            }
            model.addAttribute("businessType", businessType);
        }
        return "cms/rat/bkration/addRatBkrationAdjust";
    }

    @GetMapping("adjustList")
    @LogMessage(content = "授信最高额度调整列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String adjustList(String businessId, String businessType, Model model, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null != businessId) {
            input.addFilter(MatchTypeEnum.EQ, "businessId", businessId);
        }
        if (null != businessType) {
            input.addFilter(MatchTypeEnum.EQ, "businessType", businessType);
        }

        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        DataTablesOutput output = bkrationAdjustService.queryAdjustList(input, getCurrentUser());
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

        model.addAttribute("curr", start);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("resultCount", resultCount);
        model.addAttribute("businessId", businessId);
        model.addAttribute("businessType", businessType);
        model.addAttribute("adjustList", output.getData());
        return "cms/rat/bkration/listRatBkrationAdjust";
    }

    @GetMapping("viewAdjust/{id}")
    @LogMessage(content = "综合授信最高额度调整查看页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String viewAdjust(@PathVariable("id") String id, Model model) {
        RatBkrationAdjust ratBkrationAdjust = bkrationAdjustService.get(id);
        setRatBkration(ratBkrationAdjust, model);
        model.addAttribute("ratBkrationAdjust", ratBkrationAdjust);
        model.addAttribute("viewFlag", "1");
        return "cms/rat/bkration/viewRatBkAdjust";
    }

    @PostMapping("saveAdjust")
    @ResponseBody
    @LogMessage(content = "保存授信调整信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson saveAdjust(RatBkrationAdjustVO adjustVO) {
        handleData(adjustVO);
        bkrationAdjustService.saveData(adjustVO, getCurrentUser());
        return success();
    }

    @PostMapping("delete")
    @ResponseBody
    @LogMessage(content = "删除授信调整信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson deleteAdjust(String id) {
        bkrationAdjustService.deleteAdjust(id, getCurrentUser());
        return success();
    }

    /**
     * 去掉无效数据
     *
     * @param adjustVO 授信调整VO
     */
    public void handleData(RatBkrationAdjustVO adjustVO) {
        List<RatBkrationAdjustDtlVO> bkrationAdjustDtlVOS = adjustVO.getAdjustDtls();
        List<RatBkrationAdjustDtlVO> adjustDtlVOS = new ArrayList<>();
        for (RatBkrationAdjustDtlVO bkrationAdjustDtlVO : bkrationAdjustDtlVOS) {
            if (null == bkrationAdjustDtlVO.getItemCode()) {
                continue;
            }
            adjustDtlVOS.add(bkrationAdjustDtlVO);
        }
        adjustVO.setAdjustDtls(adjustDtlVOS);
    }

    /**
     * 设置授信录入或授信分配信息
     *
     * @param ratBkrationAdjust 授信调整对象
     * @param model
     */
    public void setRatBkration(RatBkrationAdjust ratBkrationAdjust, Model model) {
        if (ratBkrationAdjust.getBusinessType().equals(RationConstant.RAT_INPUT_ADJUST)) {
            // 授信录入最大金额调整明细
            List<RatBkrationAdjustDtlVO> adjustDtlList = adjustDtlService.queryInputAdjustDtl(ratBkrationAdjust.getId());

            RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(ratBkrationAdjust.getBusinessId());
            if (ratBkration.getRatMode().equals(RationConstant.RATMODE_0)) {//集团授信
                setRatBkrationDisDtl(ratBkration, model);//设置授信录入分配明细
                List<RatBkRationDtlVm> compRationDtls = ratBkration.getCompRationDtls();
                for (RatBkrationAdjustDtlVO dtlVO : adjustDtlList) {
                    for (RatBkRationDtlVm bkrationDtlDTO : compRationDtls) {
                        if (bkrationDtlDTO.getItemCode().equals(dtlVO.getItemCode())) {
                            bkrationDtlDTO.setAdjustedMoney(dtlVO.getMoney());
                        }
                    }
                }
                ratBkration.setCompRationDtls(compRationDtls);
                model.addAttribute("ratBkrationPage", ratBkration);
            } else if (ratBkration.getRatMode().equals(RationConstant.RATMODE_1)) {//自主授信
                RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(ratBkration.getId(), ratBkration.getCorpId());
                bkrationAdjustService.setRatBkrationApplyDtl(pageVO, ratBkration.getId());
                List<RatBkrationCpAddDtlDTO> compRationDtls = pageVO.getCompRationDtls();
                for (RatBkrationAdjustDtlVO dtlVO : adjustDtlList) {
                    for (RatBkrationCpAddDtlDTO bkrationDtlDTO : compRationDtls) {
                        if (bkrationDtlDTO.getItemCode().equals(dtlVO.getItemCode())) {
                            bkrationDtlDTO.setAdjustedMoney(dtlVO.getMoney());
                        }
                    }
                }
                pageVO.setCompRationDtls(compRationDtls);
                model.addAttribute("ratBkrationPage", pageVO);
            }
        } else if (ratBkrationAdjust.getBusinessType().equals(RationConstant.RAT_DIS_ADJUST)) {
            RatBkrationDis ratBkrationDis = ratBkrationDisService.get(ratBkrationAdjust.getBusinessId());
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(ratBkrationDis.getRatCode(), ratBkrationDis.getCorpId());
            bkrationAdjustService.setRatBkrationApplyDtl(pageVO, ratBkrationDis.getRatCode());
            List<RatBkrationCpAddDtlDTO> compRationDtls = pageVO.getCompRationDtls();

            // 授信分配最大金额调整明细
            List<RatBkrationAdjustDtlVO> adjustDtlList = adjustDtlService.queryDisAdjustDtl(ratBkrationAdjust.getId());
            for (RatBkrationAdjustDtlVO dtlVO : adjustDtlList) {
                for (RatBkrationCpAddDtlDTO bkrationDtlDTO : compRationDtls) {
                    if (bkrationDtlDTO.getItemCode().equals(dtlVO.getItemCode())) {
                        bkrationDtlDTO.setAdjustedMoney(dtlVO.getMoney());
                    }
                }
            }

            // 获取授信分配时每个授信品种的最大额度
            List<RatBkRationDtlVm> disDtlList = ratBkrationDisDetailService.getBkrationDtlByRatCode(ratBkrationDis.getRatCode());
            disDtlList = disDtlList.stream().filter(item -> RationConstant.RAT_COMFLAG_1.equals(item.getItemFlag())).collect(Collectors.toList());
            // 每个授信品种对应录入的最大额度
            Map<String, BigDecimal> maxMoneyMap = disDtlList
                    .stream()
                    .collect(Collectors.groupingBy(RatBkRationDtlVm::getItemCode))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0).getMaxMoney()));

            compRationDtls.forEach(comp -> {
                if (maxMoneyMap.containsKey(comp.getItemCode())) {
                    // 授信录入时设置的最高额度，分配的最高额度不得高于录入的最高额度
                    comp.setInputMaxMoney(maxMoneyMap.get(comp.getItemCode()));
                }
            });
            pageVO.setCompRationDtls(compRationDtls);
            model.addAttribute("ratBkrationPage", pageVO);
        }
    }

    @GetMapping("update")
    @LogMessage(content = "综合授信最高额度调整修改页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String updateAdjust(String id, Model model) {
        RatBkrationAdjust ratBkrationAdjust = bkrationAdjustService.get(id);
        setRatBkration(ratBkrationAdjust, model);
        model.addAttribute("ratBkrationAdjust", ratBkrationAdjust);
        return "cms/rat/bkration/updateRatBkrationAdjust";
    }

    @GetMapping("listApprove")
    @LogMessage(content = "授信最高额度调整列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model, Integer start) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("businessType", CommonConstant.NO);
        return "cms/rat/bkration/approve/listRatBkrationAdjustApprove";
    }

    /**
     * 授信额度调整审批 - 数据
     *
     * @param input 查询条件
     * @return 授信额度调整审批数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input) {
        DataTablesOutput output = bkrationAdjustService.queryApproveData(input);
        return output;
    }

    @GetMapping("viewApprove")
    @LogMessage(content = "跳转到审批详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String viewApprove(String businessId, String taskId, Model model) {
        RatBkrationAdjust ratBkrationAdjust = bkrationAdjustService.get(businessId);
        setRatBkration(ratBkrationAdjust, model);
        model.addAttribute("ratBkrationAdjust", ratBkrationAdjust);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/bkration/approve/viewRatBkAdjustApprove";
    }

    @GetMapping("mobileApprove")
    @LogMessage(content = "跳转到手机审批详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String mobileApprove(String businessId, String taskId, Model model) {
        RatBkrationAdjust ratBkrationAdjust = bkrationAdjustService.get(businessId);
        setRatBkration(ratBkrationAdjust, model);
        model.addAttribute("ratBkrationAdjust", ratBkrationAdjust);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/bkration/approve/mobileRatBkrationAdjust";
    }

    /**
     * 设置授信录入各品种分配总额
     *
     * @param ratBkration 授信调整对象
     * @param model
     */
    public void setRatBkrationDisDtl(RatBkrationPageVO ratBkration, Model model) {
        List<RatBkRationDtlVm> compDtls = ratBkration.getCompRationDtls();
        ratBkrationDisService.getRationLeftMoney(ratBkration);
        List<RatBkRationDtlVm> compDtlList = new ArrayList<>();
        List<RatBkrationDis> disHis = ratBkrationDisService.getRatBkDisWayByRatCode(ratBkration.getId(), "",
                RationConstant.RAT_DISWAY_2);
        List<RationDisBean> disDtls = null;
        if (disHis.size() == 0) {// 不存在总额共享分配数据
            disDtls = ratBkrationDisService.getDisDtl(ratBkration.getId(), "");
            if (null != disDtls && disDtls.size() > 0) {
                Map<String, RationDisBean> dtlMap = new HashMap<String, RationDisBean>();
                // 按授信品种分类
                for (RationDisBean detail : disDtls) {
                    dtlMap.put(detail.getItemCode(), detail);
                }
                for (RatBkRationDtlVm bkrationDtlDTO : compDtls) {
                    RationDisBean disBean = dtlMap.get(bkrationDtlDTO.getItemCode());
                    bkrationDtlDTO.setDisMoney(disBean.getMoney());
                    compDtlList.add(bkrationDtlDTO);
                }
            } else {
                compDtlList = compDtls;
            }
        } else {
            disDtls = ratBkrationDisService.getAmountShareMoney(ratBkration);
            if (null != disDtls && disDtls.size() > 0) {
                Map<String, RationDisBean> dtlMap = new HashMap<String, RationDisBean>();
                // 按授信品种分类
                for (RationDisBean detail : disDtls) {
                    dtlMap.put(detail.getItemCode(), detail);
                }
                for (RatBkRationDtlVm bkrationDtlDTO : compDtls) {
                    RationDisBean disBean = dtlMap.get(bkrationDtlDTO.getItemCode());
                    bkrationDtlDTO.setDisMoney(disBean.getMaxMoney());
                    compDtlList.add(bkrationDtlDTO);
                }
            } else {
                compDtlList = compDtls;
            }
        }
        ratBkration.setCompRationDtls(compDtlList);
    }
}
