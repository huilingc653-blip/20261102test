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
import com.bytter.credit.domain.entity.RatBkrationRelief;
import com.bytter.credit.domain.service.RatBkrationDetailService;
import com.bytter.credit.domain.service.RatBkrationDisService;
import com.bytter.credit.domain.service.RatBkrationReliefService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Api(tags = "授信额度调剂表", value = "/v1-credit/cms/rat/ratbkrationrelief")
@RestController("v1RatBkrationReliefController")
@RequestMapping("/v1-credit/cms/rat/ratbkrationrelief")
public class RatBkrationReliefController extends BaseController {
    @Autowired
    private RatBkrationReliefService ratBkrationReliefService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatBkrationDisService ratDisService;
    @Autowired
    private RatBkrationDetailService ratDtlService;

    @ApiOperation("查询授信额度调剂表列表")
    @LogMessage(content = "查询授信额度调剂表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public AjaxJson<DataTablesOutput> list(RatBkrationVO rationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        List<String> corpIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            String[] split = rationVO.getCorpId().split(",");
            corpIds.addAll(Arrays.asList(split));
        }
        this.setSearchParams(input, rationVO);
        DataTablesOutput output = ratDisService.queryRationDisCheckData(input, RationConstant.CMS_RATION_RELIEF_QUERY,
                getUserId(), corpIds, rationVO.getItemCode());
        return AjaxJson.success(output);
    }

    @ApiOperation("银行授信额度调剂导出")
    @LogMessage(content = "银行授信额度调剂导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkRationRelief")
    public void exportListRatBkRationreLief(HttpServletRequest request, HttpServletResponse response,
                                             RatBkrationVO rationVO) throws Exception {
        String fileName = "银行授信额度调剂表.xls";
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
        List<String> corpIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            String[] split = rationVO.getCorpId().split(",");
            corpIds.addAll(Arrays.asList(split));
        }
        this.setSearchParams(input, rationVO);
        DataTablesOutput output = ratDisService.queryRationDisCheckData(input, ResourceConstant.CMS_RATION_RELIEF_QUERY,
                getUserId(), corpIds, rationVO.getItemCode());
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度调剂表",
                CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
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
        if (StringUtils.isNotBlank(rationVO.getRatMode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", rationVO.getRatMode());
        }
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
        }
        String ratCode = rationVO.getRatCode();
        if (!StringUtils.isEmpty(ratCode)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratCode);
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
        if (StringUtils.isNotBlank(rationVO.getBankCode())) {
            String bankcodes = rationVO.getBankCode().substring(0, rationVO.getBankCode().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankcodes);
        }
        if (StringUtils.isNotBlank(rationVO.getItemCode())) {
            String itemCodes = rationVO.getItemCode().substring(0, rationVO.getItemCode().length() - 1);
            input.getFormdata().put("INs_itemCode", itemCodes);
            input.addFilter(MatchTypeEnum.IN, "itemCode", itemCodes);
        }
    }

    @ApiOperation("查询授信额度调剂表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput v1Input, String ratCode) {
        DataTablesInput input = v1Input.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        return AjaxJson.success(ratBkrationReliefService.queryRatBkRationReliefByRatCode(input,
                RationConstant.CMS_RATION_RELIEF_QUERY, getUserId()));
    }

    @ApiOperation("银行授信追加信息列表查询")
    @LogMessage(content = "银行授信追加信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public AjaxJson<RatBkrationPageVO> addList(RatBkrationPageVO rationVO) {
        RatBkrationPageVO result = ratBkrationService.getRatBkrationInfo(rationVO.getRatCode());
        return AjaxJson.success(result);
    }

    @ApiOperation("判断授信是否可以进行调剂")
    @GetMapping("/checkRatDis")
    public AjaxJson<RatBkrationReliefCheckVO> checkRatDis(RatBkrationRelief ratBkrationRelief) {
        RatBkrationReliefCheckVO result = new RatBkrationReliefCheckVO();
        if (StringUtils.isNotEmpty(ratBkrationRelief.getId())) {
            ratBkrationRelief = ratBkrationReliefService.get(ratBkrationRelief.getId());
            result.setRatBkrationReliefPage(ratBkrationRelief);
        }
        return AjaxJson.success(result);
    }

    @ApiOperation("获取授信额度调剂单位列表")
    @GetMapping("/getRefCorpDTOs")
    public AjaxJson<List<CorpDTO>> getRefCorpDTOs(String ratCode, String corpId, String flag) {
        return AjaxJson.success(ratBkrationReliefService.queryRatReliefCorp(flag, ratCode, corpId));
    }

    @ApiOperation("获取授信分配单位已分配的授信品种类型")
    @PostMapping("/getReliefItemFlag")
    public AjaxJson<List<RatItemDisVO>> getReliefItemFlag(String ratCode, String corpId) {
        return AjaxJson.success(ratBkrationReliefService.getCorpItemFlags(ratCode, corpId));
    }

    @ApiOperation("获取授信分配单位已分配的授信品种")
    @PostMapping("/getReliefItem")
    public AjaxJson<List<RatItemDisVO>> getReliefItem(String ratCode, String corpId, String itemFlag) {
        return AjaxJson.success(ratBkrationReliefService.getItemCustomDTOs(ratCode, corpId, itemFlag));
    }

    @ApiOperation("根据授信品种获取授信品种额度")
    @PostMapping("/getItemAssure")
    public AjaxJson<RatItemDisVO> getItemAssure(String ratCode, String itemCode) {
        return AjaxJson.success(ratDtlService.getItemAssure(ratCode, itemCode));
    }

    @ApiOperation("获取授信分配单位已分配授信品种的授信额度")
    @PostMapping("/getDisItemMoney")
    public AjaxJson<RatBkrationCpAddDtlDTO> getDisItemMoney(String ratCode, String corpId, String itemFlag,
                                                             String itemCode) {
        return AjaxJson.success(ratDisService.getRationDisItemMoney(ratCode, corpId, itemFlag, itemCode));
    }

    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatBkrationReliefAddInitVO> add(String ratCode, String corpId, String flag) {
        RatBkrationReliefAddInitVO result = new RatBkrationReliefAddInitVO();
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationReliefPageVO refVO = ratBkrationReliefService.initRationReliefPage(ratCode);
            result.setRatBkrationReliefPage(refVO);
            result.setFlag(ratDisService.queryRationDisWay(ratCode));
        }
        return AjaxJson.success(result);
    }

    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationReliefDetailVO> update(RatBkrationRelief relief) {
        RatBkrationReliefDetailVO result = new RatBkrationReliefDetailVO();
        if (StringUtils.isNotEmpty(relief.getId())) {
            populateReliefResult(result, relief.getId());
        }
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信额度调剂表详情")
    @LogMessage(content = "查询授信额度调剂表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatBkrationReliefDetailVO> view(@PathVariable("id") String id) {
        RatBkrationReliefDetailVO result = new RatBkrationReliefDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            populateReliefResult(result, id);
        }
        result.setViewFlag("1");
        return AjaxJson.success(result);
    }

    private void populateReliefResult(RatBkrationReliefDetailVO result, String reliefId) {
        Map<String, Object> serviceResult = ratBkrationReliefService.editRationReliefPage(reliefId);
        result.setRatBkrationReliefPage((RatBkrationReliefPageVO) serviceResult.get("ratBkrationReliefPage"));
        result.setRatBkrationPage((RatBkrationPageVO) serviceResult.get("ratBkrationPage"));
        result.setReliefItemFlags((List<RatItemDisVO>) serviceResult.get("reliefItemFlags"));
        result.setReliefItems((List<RatItemDisVO>) serviceResult.get("reliefItems"));
        result.setDisMoneyDTO((RatBkrationCpAddDtlDTO) serviceResult.get("disMoneyDTO"));
        result.setByReliefItemFlags((List<RatItemDisVO>) serviceResult.get("byReliefItemFlags"));
        result.setByReliefItems((List<RatItemDisVO>) serviceResult.get("byReliefItems"));
        result.setByItemAssure((RatItemDisVO) serviceResult.get("byItemAssure"));
        result.setFlag((String) serviceResult.get("flag"));
    }

    private void setRatBkrationRelief(RatBkrationReliefPageVO refVO) {
        List<RatBkrationReliefDtlPageVO> dtls = new ArrayList<>();
        List<RatBkrationReliefDtlPageVO> reliefDtls = refVO.getReliefDtls();
        for (RatBkrationReliefDtlPageVO dtlPageVO : reliefDtls) {
            if (StringUtils.isEmpty(dtlPageVO.getReliefItemCode())
                    && StringUtils.isEmpty(dtlPageVO.getByReliefItemCode())) {
                continue;
            }
            dtls.add(dtlPageVO);
        }
        if (null == refVO.getReliefOpenMoney()) {
            refVO.setReliefOpenMoney(refVO.getReliefMoney());
        }
        refVO.setReliefDtls(dtls);
    }

    @ApiOperation("暂存授信额度调剂表")
    @LogMessage(content = "添加授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.saveRationRelief(refVO, getUserId());
        return success();
    }

    @ApiOperation("提交授信额度调剂表")
    @LogMessage(content = "提交授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.saveRationRelief(refVO, getUserId());
        return success();
    }

    @ApiOperation("暂存授信额度调剂表")
    @LogMessage(content = "添加授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.updateRationRelief(refVO, getUserId());
        return success();
    }

    @ApiOperation("提交授信额度调剂表")
    @LogMessage(content = "提交授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.updateRationRelief(refVO, getUserId());
        return success();
    }

    @ApiOperation("修改授信额度调剂表")
    @LogMessage(content = "修改授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatBkrationRelief ratBkrationRelief) {
        RatBkrationRelief t = ratBkrationReliefService.get(ratBkrationRelief.getId());
        BeanUtils.copyNotNullProperties(ratBkrationRelief, t);
        ratBkrationReliefService.saveOrUpdate(t);
        return success();
    }

    @ApiOperation("删除授信额度调剂表")
    @LogMessage(content = "删除授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratBkrationReliefService.updateRationRefStatus(id);
        return success();
    }

    @ApiOperation("批量删除授信额度调剂表")
    @LogMessage(content = "批量删除授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        for (String id : ids.split(",")) {
            ratBkrationReliefService.updateRationRefStatus(id);
        }
        return success();
    }

    @ApiOperation("查询授信调剂审批列表")
    @LogMessage(content = "查询授信调剂审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public AjaxJson<RatBkrationApproveInitVO> listApprove(String flowStatus, String cycleTime) {
        RatBkrationApproveInitVO result = new RatBkrationApproveInitVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信调剂审批数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput v1Input) {
        DataTablesInput input = v1Input.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);
        }
        return AjaxJson.success(ratBkrationReliefService.listApproveOutputData(input));
    }

    @ApiOperation("查询授信调剂审批详情")
    @GetMapping("/viewBkrationApprove")
    public AjaxJson<RatBkrationReliefDetailVO> viewBkrationApprove(String businessId, String taskId) {
        RatBkrationReliefDetailVO result = new RatBkrationReliefDetailVO();
        populateReliefResult(result, businessId);
        result.setTaskId(taskId);
        ConfigDTO config = ratBkrationService.getStartOpenRat();
        String page = "viewRatBkrationRelief";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationReliefOpen";
        }
        result.setViewPage(page);
        return AjaxJson.success(result);
    }

    @ApiOperation("授信调剂手机审批详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatBkrationReliefDetailVO> mobileBkrationApprove(String businessId, String taskId) {
        RatBkrationReliefDetailVO result = new RatBkrationReliefDetailVO();
        populateReliefResult(result, businessId);
        result.setTaskId(taskId);
        result.setViewPage("mobileRatBkrationRelief");
        return AjaxJson.success(result);
    }
}
