package com.bytter.credit.web.controller.saas;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(tags = "综合授信最高额度调整", value = "/v1-credit/cms/rat/ratbkrationadjust")
@RestController("v1RatBkrationAdjustController")
@RequestMapping("/v1-credit/cms/rat/ratbkrationadjust")
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

    @ApiOperation("查询授信录入最高额度调整列表")
    @LogMessage(content = "授信录入综合授信最高额度调整列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/ratInputAdjust")
    public AjaxJson<DataTablesOutput> ratInputAdjust(RatBkrationVO rationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        this.setSearchParams(input, rationVO);
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkInputCanAdjust(input,
                RationConstant.CMS_RATION_ADJUST_QUERY, getUserId());
        return AjaxJson.success(outputTables);
    }

    @ApiOperation("最高额度调整授信录入导出")
    @LogMessage(content = "最高额度调整授信录入导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkInputAdjustTab")
    public void exportListRatBkInputAdjustTab(HttpServletRequest request, HttpServletResponse response,
                                               RatBkrationVO rationVO) throws Exception {
        String fileName = "最高额度调整授信录入表.xls";
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(rationVO.getExportType())) {
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
        setSearchParams(input, rationVO);
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkInputCanAdjust(input,
                ResourceConstant.CMS_RATION_ADJUST_QUERY, getUserId());
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(outputTables, "最高额度调整授信录入表",
                CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    @ApiOperation("查询授信分配最高额度调整列表")
    @LogMessage(content = "授信分配综合授信最高额度调整列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/ratDisAdjust")
    public AjaxJson<DataTablesOutput> ratDisAdjust(RatBkrationVO rationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        this.setSearchParams(input, rationVO);
        input.addFilter(MatchTypeEnum.IN, "distributeWay", RationConstant.RAT_DISWAY_3);
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkDisCanAdjust(input,
                RationConstant.CMS_RATION_ADJUST_QUERY, getUserId());
        return AjaxJson.success(outputTables);
    }

    @ApiOperation("最高额度调整授信分配导出")
    @LogMessage(content = "最高额度调整授信分配导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkInputAdjustTabDis")
    public void exportListRatBkInputAdjustTabDis(HttpServletRequest request, HttpServletResponse response,
                                                  RatBkrationVO rationVO) throws Exception {
        String fileName = "最高额度调整授信分配表.xls";
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(rationVO.getExportType())) {
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
        setSearchParams(input, rationVO);
        input.addFilter(MatchTypeEnum.IN, "distributeWay", "3");
        DataTablesOutput outputTables = bkrationAdjustService.queryRatBkDisCanAdjust(input,
                ResourceConstant.CMS_RATION_ADJUST_QUERY, getUserId());
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(outputTables, "最高额度调整授信分配表",
                CommonConstant.NO, CommonConstant.YES, CommonConstant.NO, CommonConstant.YES);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    private void setSearchParams(DataTablesInput input, RatBkrationVO rationVO) {
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        String corpId = rationVO.getCorpId();
        if (!StringUtils.isEmpty(corpId)) {
            input.addFilter(MatchTypeEnum.IN, "corpId", corpId);
        }
        if (!StringUtils.isEmpty(rationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", rationVO.getContractNo());
        }
        if (!StringUtils.isEmpty(rationVO.getRatCode()) && !"undefined".equals(rationVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", rationVO.getRatCode());
        }
        String bankCode = rationVO.getBankCode();
        if (!StringUtils.isEmpty(bankCode)) {
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCode);
        }
        String itemCode = rationVO.getItemCode();
        if (!StringUtils.isEmpty(itemCode)) {
            input.getFormdata().put("itemCode", itemCode);
        }
        String ratMode = rationVO.getRatMode();
        if (!StringUtils.isEmpty(ratMode)) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", ratMode);
        }
        if (null != rationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
        }
        if (null != rationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
        }
        if (null != rationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(rationVO.getgEndDate()));
        }
        if (null != rationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(rationVO.getlEndDate()));
        }
    }

    @ApiOperation("新增额度调整页面数据")
    @GetMapping("/addAdjust")
    public AjaxJson<RatBkrationAdjustDetailVO> addAdjust(String businessId, String businessType) {
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
            RatBkrationAdjustDetailVO result = new RatBkrationAdjustDetailVO();
            if (businessType.equals(RationConstant.RAT_INPUT_ADJUST)) {
                RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(businessId);
                if (ratBkration.getRatMode().equals(RationConstant.RATMODE_0)) {
                    setRatBkrationDisDtl(ratBkration);
                    result.setRatBkrationPageVO(ratBkration);
                } else if (ratBkration.getRatMode().equals(RationConstant.RATMODE_1)) {
                    RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(businessId,
                            ratBkration.getCorpId());
                    bkrationAdjustService.setRatBkrationApplyDtl(pageVO, businessId);
                    result.setRatBkrationCpPage(pageVO);
                }
            } else if (businessType.equals(RationConstant.RAT_DIS_ADJUST)) {
                RatBkrationDis ratBkrationDis = ratBkrationDisService.get(businessId);
                RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(
                        ratBkrationDis.getRatCode(), ratBkrationDis.getCorpId());
                bkrationAdjustService.setRatBkrationApplyDtl(pageVO, ratBkrationDis.getRatCode());
                result.setRatBkrationCpPage(pageVO);
            }
            result.setBusinessType(businessType);
            return AjaxJson.success(result);
        }
        return AjaxJson.success(new RatBkrationAdjustDetailVO());
    }

    @ApiOperation("查询额度调整列表")
    @GetMapping("/adjustList")
    public AjaxJson<RatBkrationAdjustSubListVO> adjustList(String businessId, String businessType,
                                                            Integer start, Integer len) {
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
        int pageCount = resultCount % pageSize == 0 ? (int) (resultCount / pageSize) : (int) ((resultCount / pageSize) + 1);
        RatBkrationAdjustSubListVO result = new RatBkrationAdjustSubListVO();
        result.setAdjustList(output.getData());
        result.setTotal(pageCount);
        result.setCurr(start);
        result.setBusinessId(businessId);
        result.setBusinessType(businessType);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询额度调整详情")
    @GetMapping("/viewAdjust/{id}")
    public AjaxJson<RatBkrationAdjustDetailVO> viewAdjust(@PathVariable("id") String id) {
        RatBkrationAdjust ratBkrationAdjust = bkrationAdjustService.get(id);
        RatBkrationAdjustDetailVO result = new RatBkrationAdjustDetailVO();
        setRatBkration(ratBkrationAdjust, result);
        result.setRatBkrationAdjust(ratBkrationAdjust);
        result.setViewFlag("1");
        return AjaxJson.success(result);
    }

    @ApiOperation("保存授信调整信息")
    @LogMessage(content = "保存授信调整信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveAdjust")
    public AjaxJson saveAdjust(@RequestBody RatBkrationAdjustVO adjustVO) {
        handleData(adjustVO);
        bkrationAdjustService.saveData(adjustVO, getCurrentUser());
        return success();
    }

    @ApiOperation("删除授信调整信息")
    @LogMessage(content = "删除授信调整信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete")
    public AjaxJson deleteAdjust(String id) {
        bkrationAdjustService.deleteAdjust(id, getCurrentUser());
        return success();
    }

    @ApiOperation("编辑额度调整页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationAdjustDetailVO> updateAdjust(String id) {
        RatBkrationAdjust ratBkrationAdjust = bkrationAdjustService.get(id);
        RatBkrationAdjustDetailVO result = new RatBkrationAdjustDetailVO();
        setRatBkration(ratBkrationAdjust, result);
        result.setRatBkrationAdjust(ratBkrationAdjust);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询额度调整审批列表")
    @GetMapping("/listApprove")
    public AjaxJson<RatBkrationApproveInitVO> listApprove(String flowStatus, String cycleTime) {
        RatBkrationApproveInitVO result = new RatBkrationApproveInitVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        result.setFlag(CommonConstant.NO);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询额度调整审批数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody DataTablesInput input) {
        return AjaxJson.success(bkrationAdjustService.queryApproveData(input));
    }



    private void handleData(RatBkrationAdjustVO adjustVO) {
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

    private void setRatBkration(RatBkrationAdjust ratBkrationAdjust, RatBkrationAdjustDetailVO result) {
        if (ratBkrationAdjust.getBusinessType().equals(RationConstant.RAT_INPUT_ADJUST)) {
            List<RatBkrationAdjustDtlVO> adjustDtlList = adjustDtlService.queryInputAdjustDtl(
                    ratBkrationAdjust.getId());
            RatBkrationPageVO ratBkration = ratBkrationService.getBkRationBeanById(
                    ratBkrationAdjust.getBusinessId());
            if (ratBkration.getRatMode().equals(RationConstant.RATMODE_0)) {
                setRatBkrationDisDtl(ratBkration);
                List<RatBkRationDtlVm> compRationDtls = ratBkration.getCompRationDtls();
                for (RatBkrationAdjustDtlVO dtlVO : adjustDtlList) {
                    for (RatBkRationDtlVm bkrationDtlDTO : compRationDtls) {
                        if (bkrationDtlDTO.getItemCode().equals(dtlVO.getItemCode())) {
                            bkrationDtlDTO.setAdjustedMoney(dtlVO.getMoney());
                        }
                    }
                }
                ratBkration.setCompRationDtls(compRationDtls);
                result.setRatBkrationPageVO(ratBkration);
            } else if (ratBkration.getRatMode().equals(RationConstant.RATMODE_1)) {
                RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(
                        ratBkration.getId(), ratBkration.getCorpId());
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
                result.setRatBkrationCpPage(pageVO);
            }
        } else if (ratBkrationAdjust.getBusinessType().equals(RationConstant.RAT_DIS_ADJUST)) {
            RatBkrationDis ratBkrationDis = ratBkrationDisService.get(ratBkrationAdjust.getBusinessId());
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(
                    ratBkrationDis.getRatCode(), ratBkrationDis.getCorpId());
            bkrationAdjustService.setRatBkrationApplyDtl(pageVO, ratBkrationDis.getRatCode());
            List<RatBkrationCpAddDtlDTO> compRationDtls = pageVO.getCompRationDtls();
            List<RatBkrationAdjustDtlVO> adjustDtlList = adjustDtlService.queryDisAdjustDtl(
                    ratBkrationAdjust.getId());
            for (RatBkrationAdjustDtlVO dtlVO : adjustDtlList) {
                for (RatBkrationCpAddDtlDTO bkrationDtlDTO : compRationDtls) {
                    if (bkrationDtlDTO.getItemCode().equals(dtlVO.getItemCode())) {
                        bkrationDtlDTO.setAdjustedMoney(dtlVO.getMoney());
                    }
                }
            }
            List<RatBkRationDtlVm> disDtlList = ratBkrationDisDetailService.getBkrationDtlByRatCode(
                    ratBkrationDis.getRatCode());
            disDtlList = disDtlList.stream()
                    .filter(item -> RationConstant.RAT_COMFLAG_1.equals(item.getItemFlag()))
                    .collect(Collectors.toList());
            Map<String, BigDecimal> maxMoneyMap = disDtlList.stream()
                    .collect(Collectors.groupingBy(RatBkRationDtlVm::getItemCode))
                    .entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0).getMaxMoney()));
            compRationDtls.forEach(comp -> {
                if (maxMoneyMap.containsKey(comp.getItemCode())) {
                    comp.setInputMaxMoney(maxMoneyMap.get(comp.getItemCode()));
                }
            });
            pageVO.setCompRationDtls(compRationDtls);
            result.setRatBkrationCpPage(pageVO);
        }
    }

    private void setRatBkrationDisDtl(RatBkrationPageVO ratBkration) {
        List<RatBkRationDtlVm> compDtls = ratBkration.getCompRationDtls();
        ratBkrationDisService.getRationLeftMoney(ratBkration);
        List<RatBkRationDtlVm> compDtlList = new ArrayList<>();
        List<RatBkrationDis> disHis = ratBkrationDisService.getRatBkDisWayByRatCode(ratBkration.getId(), "",
                RationConstant.RAT_DISWAY_2);
        List<RationDisBean> disDtls;
        if (disHis.isEmpty()) {
            disDtls = ratBkrationDisService.getDisDtl(ratBkration.getId(), "");
            if (null != disDtls && !disDtls.isEmpty()) {
                Map<String, RationDisBean> dtlMap = new HashMap<>();
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
            if (null != disDtls && !disDtls.isEmpty()) {
                Map<String, RationDisBean> dtlMap = new HashMap<>();
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
