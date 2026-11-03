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
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.entity.RatCreditType;
import com.bytter.credit.domain.service.*;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.finance.constant.ResourceConstant;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Api(tags = "授信分配信息表", value = "/v1-credit/cms/rat/ratbkrationdis")
@RestController("v1RatBkrationDisController")
@RequestMapping("/v1-credit/cms/rat/ratbkrationdis")
public class RatBkrationDisController extends BaseController {
    @Autowired
    private RatBkrationDisService ratBkrationDisService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatCreditTypeService creditTypeService;
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatBkrationAdjustService bkrationAdjustService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @ApiOperation("查询授信分配信息表列表")
    @LogMessage(content = "查询授信分配信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public AjaxJson<DataTablesOutput> list(Integer start, Integer len, RatBkrationDisQueryVO queryVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        this.setSearchParams(input, queryVO);
        List<String> corpIdArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(queryVO.getCorpIds())) {
            corpIdArr = Arrays.asList(queryVO.getCorpIds().split(","));
        }
        DataTablesOutput output = ratBkrationDisService.listDisOutputData(input, RationConstant.CMS_RATION_DIS_QUERY,
                getUserId(), corpIdArr);
        return AjaxJson.success(output);
    }

    @ApiOperation("银行授信额度分配导出")
    @LogMessage(content = "银行授信额度分配导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListBkrationDis")
    public void exportListRatBkration(HttpServletRequest request, HttpServletResponse response,
                                       RatBkrationDisQueryVO vo) throws Exception {
        String fileName = "银行授信额度分配表.xls";
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
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
        List<String> corpIdArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(vo.getCorpIds())) {
            corpIdArr = Arrays.asList(vo.getCorpIds().split(","));
        }
        DataTablesOutput output = ratBkrationDisService.listDisOutputData(input, ResourceConstant.CMS_RATION_DIS_QUERY,
                getUserId(), corpIdArr);
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度分配表",
                CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    private void setSearchParams(DataTablesInput input, RatBkrationDisQueryVO vo) {
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        if (!StringUtils.isEmpty(vo.getBankCodes())) {
            String bankCode = vo.getBankCodes().substring(0, vo.getBankCodes().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCode);
        }
        if (!StringUtils.isEmpty(vo.getItemCodes())) {
            String itemCode = vo.getItemCodes().substring(0, vo.getItemCodes().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "item_code", itemCode);
        }
        if (!StringUtils.isEmpty(vo.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", vo.getRatCode());
        }
        if (!StringUtils.isEmpty(vo.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", vo.getContractNo());
        }
        if (null != vo.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(vo.getgBeginDate()));
        }
        if (null != vo.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(vo.getlBeginDate()));
        }
        if (null != vo.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(vo.getgEndDate()));
        }
        if (null != vo.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(vo.getlEndDate()));
        }
        input.addFilter(MatchTypeEnum.EQ, "ratMode", RationConstant.RATMODE_0);
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        input.addOrder("ratCode", false);
    }

    @ApiOperation("查询授信分配信息表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput v1DataTablesInput) {
        DataTablesInput input = v1DataTablesInput.convertAndInitColumns();
        return AjaxJson.success(ratBkrationDisService.getDataTables(input));
    }

    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatBkrationDisAddInitVO> add(String ratCode, String disWay) {
        RatBkrationDisAddInitVO result = new RatBkrationDisAddInitVO();
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationPageVO ratBkration = ratBkrationDisService.getRatRationDisInfo(ratCode, disWay);
            result.setRatBkrationPage(ratBkration);
        }
        List<RatCreditTypeVO> creditTypes = creditTypeService.getAllCreditType();
        if (creditTypes.size() > 0) {
            result.setCreditType(creditTypes.get(0).getId());
        }
        return AjaxJson.success(result);
    }

    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationDisAddInitVO> updatePage(RatBkrationDis rationDis) {
        RatBkrationDisAddInitVO result = new RatBkrationDisAddInitVO();
        if (StringUtils.isNotEmpty(rationDis.getId())) {
            RatBkrationPageVO ratBkration = ratBkrationDisService.editRatRationDisInfo(rationDis.getId());
            result.setRatBkrationPage(ratBkration);
        }
        return AjaxJson.success(result);
    }

    @ApiOperation("授信分配信息列表查询")
    @LogMessage(content = "授信分配信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/disList")
    public AjaxJson<RatBkrationDisSubListVO> disList(RatBkrationDisVO disVO, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(disVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", disVO.getCorpId());
        }
        if (!StringUtils.isEmpty(disVO.getDistributeWay())) {
            input.addFilter(MatchTypeEnum.EQ, "distributeWay", disVO.getDistributeWay());
        }
        input.addFilter(MatchTypeEnum.EQ, "ratCode", disVO.getRatCode());
        DataTablesOutput output = ratBkrationDisService.queryRationDisData(input, RationConstant.CMS_RATION_DIS_QUERY,
                getUserId());
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount = resultCount % pageSize == 0 ? (int) (resultCount / pageSize) : (int) ((resultCount / pageSize) + 1);
        RatBkrationDisSubListVO result = new RatBkrationDisSubListVO();
        result.setRations(output.getData());
        result.setTotal(pageCount);
        result.setCurr(start);
        result.setDisVO(disVO);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信分配信息表详情")
    @LogMessage(content = "查询授信分配信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatBkrationDisDetailVO> view(@PathVariable("id") String id) {
        RatBkrationDisDetailVO result = new RatBkrationDisDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(id);
            CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
            dto.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());
            if (StringUtils.isNotEmpty(dto.getType())) {
                RatCreditType creditType = creditTypeService.get(dto.getType());
                dto.setCreditTypeName(creditType.getName());
            }
            result.setRatBkrationPage(dto);
        }
        result.setViewFlag("1");
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信分配信息表详情(新)")
    @LogMessage(content = "查询授信分配信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDis/{id}")
    public AjaxJson<RatBkrationDisDetailVO> viewDis(@PathVariable("id") String id) {
        RatBkrationDisDetailVO result = new RatBkrationDisDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(id);
            CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
            dto.setDisCorpName(corpDTO.getName());
            if (null != dto.getType()) {
                RatCreditType creditType = creditTypeService.get(dto.getType());
                dto.setCreditTypeName(creditType.getName());
            }
            result.setRatBkrationPage(dto);
        }
        result.setViewFlag("1");
        return AjaxJson.success(result);
    }

    private void setRatBkrationDisDtls(RatBkrationDisListDTO ratBkrationDis) {
        List<RatBkrationDisDtlDTO> dtls = new ArrayList<>();
        List<RatBkrationDisDtlDTO> rationDtls = ratBkrationDis.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<>();
        }
        boolean compFlag = false;
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
                if (null == dtlDTO.getOpenMoney()) {
                    dtlDTO.setOpenMoney(dtlDTO.getMoney());
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                        dtls.add(dtlDTO);
                    }
                } else {
                    if (null != dtlDTO.getMoney() && dtlDTO.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                        compFlag = true;
                    }
                    if (compFlag) {
                        dtls.add(dtlDTO);
                    }
                }
            }
        }
        Collections.sort(dtls, new Comparator<RatBkrationDisDtlDTO>() {
            public int compare(RatBkrationDisDtlDTO o1, RatBkrationDisDtlDTO o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        Integer index = 1;
        for (RatBkrationDisDtlDTO dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
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
        ratBkrationDis.setRationDtls(dtls);
        if (null == ratBkrationDis.getOpenMoneysum()) {
            ratBkrationDis.setOpenMoneysum(ratBkrationDis.getMoneysum());
        }
    }

    @ApiOperation("暂存授信分配信息表")
    @LogMessage(content = "暂存授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationDisService.saveRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    @ApiOperation("提交授信分配信息表")
    @LogMessage(content = "提交授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationDisService.saveRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    @ApiOperation("修改暂存授信分配信息表")
    @LogMessage(content = "修改暂存授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationDisService.editRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    @ApiOperation("修改提交授信分配信息表")
    @LogMessage(content = "修改提交授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationDisService.editRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    @ApiOperation("修改授信分配信息表")
    @LogMessage(content = "修改授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatBkrationDis ratBkrationDis) {
        RatBkrationDis t = ratBkrationDisService.get(ratBkrationDis.getId());
        BeanUtils.copyNotNullProperties(ratBkrationDis, t);
        ratBkrationDisService.saveOrUpdate(t);
        return success();
    }

    @ApiOperation("删除授信分配信息表")
    @LogMessage(content = "删除授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationDis ratBkrationDis = ratBkrationDisService.get(id);
        ratBkrationDis.setStatus(RationConstant.STATUS_DELETE);
        ratBkrationDisService.updateEntity(ratBkrationDis);
        return success();
    }

    @ApiOperation("批量删除授信分配信息表")
    @LogMessage(content = "批量删除授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        for (String id : ids.split(",")) {
            RatBkrationDis ratBkrationDis = ratBkrationDisService.get(id);
            ratBkrationDisService.deleteEntity(ratBkrationDis);
        }
        return success();
    }

    @ApiOperation("根据分配类型获取单位的直接下属单位(Post)")
    @PostMapping("/listNetCorp")
    public AjaxJson<List<CorpDTO>> listMultNetCorp(String netId, String disWay) {
        return AjaxJson.success(getNetCorpList(netId, disWay));
    }

    @ApiOperation("根据分配类型获取单位的直接下属单位")
    @GetMapping("/listNetCorp")
    public AjaxJson<List<CorpDTO>> listNetCorp(String netId, String disWay) {
        return AjaxJson.success(getNetCorpList(netId, disWay));
    }

    private List<CorpDTO> getNetCorpList(String netId, String disWay) {
        List<CorpDTO> corpDTOs = new ArrayList<>();
        CorpDTO netCorp = corpService.get(netId);
        if (RationConstant.DIS_TYPE_OWN.equals(disWay)) {
            netCorp.setName("(" + netCorp.getCode() + ")" + netCorp.getName());
        }
        netCorp.setParentId(null);
        corpDTOs.add(netCorp);
        if (RationConstant.DIS_TYPE_OWN.equals(disWay)) {
            return corpDTOs;
        }
        ConfigDTO config = ratBkrationDisService.getRatctrModel();
        if (RationConstant.RATCTRMODEL_RESOURCE.equals(config.getValue())) {
            corpDTOs = corpService.listUserCorp(RationConstant.CMS_RATION_DIS_APPLY, getUserId());
        } else {
            corpDTOs.addAll(getChildCorp(netId, null));
        }
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

    private List<CorpDTO> getChildCorp(String parentId, Map<String, List<CorpDTO>> corpMap) {
        List<CorpDTO> result = new ArrayList<>();
        List<CorpDTO> corpDTOs = corpService.listUnderCorpByParentCorpId(parentId);
        if (EmptyUtils.isEmptyList(corpDTOs)) {
            return result;
        }
        for (CorpDTO corpDTO : corpDTOs) {
            List<CorpDTO> corps = getChildCorp(corpDTO.getId(), corpMap);
            result.add(corpDTO);
            result.addAll(corps);
        }
        return result;
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
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);
        }
        return AjaxJson.success(ratBkrationDisService.listApproveOutputData(input));
    }

    @ApiOperation("查询授信录入审批详情")
    @GetMapping("/viewBkrationDisApprove")
    public AjaxJson<RatBkrationDisApproveDetailVO> viewBkrationDisApprove(String businessId, String taskId) {
        RatBkrationDisApproveDetailVO result = new RatBkrationDisApproveDetailVO();
        RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(businessId);
        CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
        dto.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());
        if (null != dto.getType()) {
            RatCreditType creditType = creditTypeService.get(dto.getType());
            if (null != creditType) {
                dto.setCreditTypeName(creditType.getName());
            }
        }
        result.setRatBkrationPage(dto);
        result.setTaskId(taskId);
        ConfigDTO config = ratBkrationService.getStartOpenRat();
        String page = "viewRatBkrationDis";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationDisOpen";
        }
        result.setViewPage(page);
        return AjaxJson.success(result);
    }

    @ApiOperation("授信分配手机审批详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatBkrationDisApproveDetailVO> mobileBkrationDisApprove(String businessId, String taskId) {
        RatBkrationDisApproveDetailVO result = new RatBkrationDisApproveDetailVO();
        RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(businessId);
        CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
        dto.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());
        if (null != dto.getType()) {
            RatCreditType creditType = creditTypeService.get(dto.getType());
            if (null != creditType) {
                dto.setCreditTypeName(creditType.getName());
            }
        }
        result.setRatBkrationPage(dto);
        result.setTaskId(taskId);
        result.setViewPage("mobileRatBkrationDis");
        return AjaxJson.success(result);
    }

    @ApiOperation("总额共享方式已分配的单位查看")
    @LogMessage(content = "总额共享方式已分配的单位查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/distributeWayView/{ratCode}/{ratCorpId}")
    public AjaxJson<RatBkrationDisDistributeWayVO> distributeWayView(@PathVariable("ratCode") String ratCode,
                                                                       @PathVariable("ratCorpId") String ratCorpId) {
        RatBkrationDisDistributeWayVO result = new RatBkrationDisDistributeWayVO();
        List<CorpDTO> corpList = ratBkrationDisService.distributeWayView(ratCode, ratCorpId);
        result.setCorpList(corpList);
        return AjaxJson.success(result);
    }
}
