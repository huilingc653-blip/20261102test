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
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.service.RatBkrationAddService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Api(tags = "授信追加信息表", value = "/v1-credit/cms/rat/ratbkrationadd")
@RestController("v1RatBkrationAddController")
@RequestMapping("/v1-credit/cms/rat/ratbkrationadd")
public class RatBkrationAddController extends BaseController {
    @Autowired
    private RatBkrationAddService ratBkrationAddService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;

    @ApiOperation("查询银行授信追加信息表列表")
    @LogMessage(content = "查询银行授信追加信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public AjaxJson<DataTablesOutput> list(Integer start, Integer len, RatBkrationVO rationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.getFormdata().put("likesContractNo", contractNo);
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
        }
        String ratCode = rationVO.getRatCode();
        if (StringUtils.isNotBlank(ratCode)) {
            input.getFormdata().put("likesRatCode", ratCode);
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
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.IN, "corpId", rationVO.getCorpId());
        }
        if (StringUtils.isNotBlank(rationVO.getBankCode())) {
            String bankCodes = rationVO.getBankCode().substring(0, rationVO.getBankCode().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCodes);
        }
        List<String> itemCodeArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getItemCode())) {
            String[] split = rationVO.getItemCode().split(",");
            itemCodeArr.addAll(Arrays.asList(split));
            String substring = rationVO.getItemCode().substring(0, rationVO.getItemCode().length() - 1);
            input.getFormdata().put("INs_itemCode", substring);
        }
        if (StringUtils.isNotBlank(rationVO.getRatMode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", rationVO.getRatMode());
        }
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_ADD_QUERY,
                getUserId());
        return AjaxJson.success(output);
    }

    @ApiOperation("银行授信额度追加导出")
    @LogMessage(content = "银行授信额度追加导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkRationAddBank")
    public void exportListRatBkRationAddBank(HttpServletRequest request, HttpServletResponse response,
                                              RatBkrationVO rationVO) throws Exception {
        String fileName = "银行授信额度追加表.xls";
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
        String ratType = RationConstant.RAT_TYPE_0;
        if (!StringUtils.isEmpty(ratType)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
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
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            String corpIds = rationVO.getCorpId().substring(0, rationVO.getCorpId().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "corpId", corpIds);
        }
        if (StringUtils.isNotBlank(rationVO.getBankCode())) {
            String bankCodes = rationVO.getBankCode().substring(0, rationVO.getBankCode().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCodes);
        }
        if (StringUtils.isNotBlank(rationVO.getItemCode())) {
            String[] split = rationVO.getItemCode().split(",");
            Arrays.asList(split);
        }
        if (StringUtils.isNotBlank(rationVO.getRatMode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", rationVO.getRatMode());
        }
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, ResourceConstant.CMS_RATION_ADD_QUERY,
                getUserId());
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度追加表",
                CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    @ApiOperation("查询集团授信追加信息表列表")
    @LogMessage(content = "查询集团授信追加信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/cplist")
    public AjaxJson<DataTablesOutput> cplist(Integer start, Integer len, RatBkrationVO rationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        setSearchParams(input, rationVO);
        DataTablesOutput output = ratBkrationAddService.queryCpRationAddData(input,
                RationConstant.CMS_RATION_CPADD_QUERY, getUserId());
        return AjaxJson.success(output);
    }

    @ApiOperation("集团授信额度追加导出")
    @LogMessage(content = "集团授信额度追加导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkRationAddCorp")
    public void exportListRatBkRationAddCorp(HttpServletRequest request, HttpServletResponse response,
                                              RatBkrationVO rationVO) throws Exception {
        String fileName = "集团授信额度追加表.xls";
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
        DataTablesOutput output = ratBkrationAddService.queryCpRationAddData(input,
                ResourceConstant.CMS_RATION_CPADD_QUERY, getUserId());
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "集团授信额度追加表",
                CommonConstant.NO, CommonConstant.YES, CommonConstant.NO, CommonConstant.NO);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    private void setSearchParams(DataTablesInput input, RatBkrationVO rationVO) {
        String corpId = rationVO.getCorpId();
        if (!StringUtils.isEmpty(corpId)) {
            input.addFilter(MatchTypeEnum.IN, "corpId", corpId);
        }
        String disCorpId = rationVO.getDisCorp();
        if (!StringUtils.isEmpty(disCorpId)) {
            input.addFilter(MatchTypeEnum.IN, "disCorp", disCorpId);
        }
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
        }
        String ratCode = rationVO.getRatCode();
        if (!StringUtils.isEmpty(ratCode)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratCode);
        }
        String bankCode = rationVO.getBankCode();
        if (!StringUtils.isEmpty(bankCode)) {
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCode);
        }
        String itemCode = rationVO.getItemCode();
        if (!StringUtils.isEmpty(itemCode)) {
            input.getFormdata().put("INs_itemCode", itemCode);
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
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
    }

    @ApiOperation("查询授信追加信息表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput v1DataTablesInput) {
        DataTablesInput input = v1DataTablesInput.convertAndInitColumns();
        return AjaxJson.success(ratBkrationAddService.getDataTables(input));
    }

    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatBkrationAddFormInitVO> add(RatBkrationAdd ratBkrationAdd) {
        RatBkrationAddFormInitVO result = new RatBkrationAddFormInitVO();
        if (StringUtils.isNotEmpty(ratBkrationAdd.getRatCode())) {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.getRatRationAddInfo(ratBkrationAdd.getRatCode());
            result.setRatBkrationPage(ratBkration);
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            result.setDto(dto);
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_BANK);
        return AjaxJson.success(result);
    }

    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationAddFormInitVO> update(RatBkrationAdd rationAdd) {
        RatBkrationAddFormInitVO result = new RatBkrationAddFormInitVO();
        if (StringUtils.isNotEmpty(rationAdd.getId())) {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(rationAdd.getId());
            result.setRatBkrationPage(ratBkration);
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            FcBondApplyDTO cdto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatAddCode());
            if (dto != null) {
                if (cdto != null) {
                    dto.setApplyMoney(cdto.getApplyMoney());
                } else {
                    dto.setApplyMoney(new BigDecimal(0));
                }
                result.setDto(dto);
            }
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_BANK);
        return AjaxJson.success(result);
    }

    @ApiOperation("集团授信追加新增页面数据")
    @GetMapping("/cpadd")
    public AjaxJson<RatBkrationAddCpDetailVO> cpadd(RatBkrationAdd ratBkrationAdd) {
        RatBkrationAddCpDetailVO result = new RatBkrationAddCpDetailVO();
        if (StringUtils.isNotEmpty(ratBkrationAdd.getRatCode())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.initCpRationAddData(
                    ratBkrationAdd.getRatCode(), ratBkrationAdd.getCorpId());
            result.setRatBkrationPage(ratBkration);
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            result.setDto(dto);
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_GROUP);
        return AjaxJson.success(result);
    }

    @ApiOperation("集团授信追加编辑页面数据")
    @GetMapping("/cpupdate")
    public AjaxJson<RatBkrationAddCpDetailVO> cpupdate(RatBkrationAdd rationAdd) {
        RatBkrationAddCpDetailVO result = new RatBkrationAddCpDetailVO();
        if (StringUtils.isNotEmpty(rationAdd.getId())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            result.setRatBkrationPage(ratBkration);
            result.setAddCode(rationAdd.getId());
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            FcBondApplyDTO cdto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    rationAdd.getAddCode());
            if (cdto != null) {
                dto.setApplyMoney(cdto.getApplyMoney());
            }
            result.setDto(dto);
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_GROUP);
        return AjaxJson.success(result);
    }

    @ApiOperation("银行授信追加信息列表查询")
    @LogMessage(content = "银行授信追加信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public AjaxJson<RatBkrationAddSubListVO> addList(RatBkrationAddListVO addVO, Integer start) {
        return AjaxJson.success(queryRationAddData(addVO, start, RationConstant.RAT_ADD_FLAG_BANK,
                RationConstant.CMS_RATION_ADD_QUERY));
    }

    private RatBkrationAddSubListVO queryRationAddData(RatBkrationAddListVO addVO, Integer start, String flag,
                                                        String resource) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(addVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", addVO.getCorpId());
        }
        input.addFilter(MatchTypeEnum.EQ, "ratCode", addVO.getRatCode());
        input.addFilter(MatchTypeEnum.EQ, "flag", flag);
        DataTablesOutput output = ratBkrationAddService.queryRationAddData(input, resource, getUserId());
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount = resultCount % pageSize == 0 ? (int) (resultCount / pageSize) : (int) ((resultCount / pageSize) + 1);
        RatBkrationAddSubListVO result = new RatBkrationAddSubListVO();
        result.setRations(output.getData());
        result.setTotal(pageCount);
        result.setCurr(start);
        result.setAddVO(addVO);
        return result;
    }

    @ApiOperation("集团授信追加信息列表查询")
    @LogMessage(content = "集团授信追加信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/cpaddList")
    public AjaxJson<RatBkrationAddSubListVO> cpaddList(RatBkrationAddListVO addVO, Integer start) {
        return AjaxJson.success(queryRationAddData(addVO, start, RationConstant.RAT_ADD_FLAG_GROUP,
                RationConstant.CMS_RATION_CPADD_QUERY));
    }

    @ApiOperation("查询授信追加信息表详情")
    @LogMessage(content = "查询授信追加信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatBkrationAddDetailVO> view(@PathVariable("id") String id) {
        RatBkrationAddDetailVO result = new RatBkrationAddDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(id);
            result.setRatBkrationPage(ratBkration);
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatAddCode());
            result.setDto(dto);
        }
        result.setViewFlag("1");
        return AjaxJson.success(result);
    }

    @ApiOperation("查询集团授信追加信息表详情")
    @LogMessage(content = "查询集团授信追加信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/cpview/{id}")
    public AjaxJson<RatBkrationAddCpDetailVO> cpview(@PathVariable("id") String id) {
        RatBkrationAddCpDetailVO result = new RatBkrationAddCpDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationAdd rationAdd = new RatBkrationAdd();
            rationAdd.setId(id);
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            result.setRatBkrationPage(ratBkration);
            result.setAddCode(rationAdd.getId());
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    rationAdd.getAddCode());
            result.setDto(dto);
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_GROUP);
        result.setViewFlag("1");
        return AjaxJson.success(result);
    }

    private void setRatBkrationAddDtls(RatBkRationAddVm listDTO) {
        List<RatBkRationAddDtlVm> dtls = new ArrayList<>();
        List<RatBkRationAddDtlVm> rationDtls = listDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<>();
        }
        boolean compFlag = false;
        for (RatBkRationAddDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (null == dtlDTO.getMoney()) {
                        dtlDTO.setMoney(BigDecimal.ZERO);
                    }
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                        dtls.add(dtlDTO);
                    }
                } else {
                    if (null != dtlDTO.getMoney() && dtlDTO.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                        compFlag = true;
                    }
                    if (compFlag) {
                        dtls.add(dtlDTO);
                    }
                }
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
                if (null == dtlDTO.getOpenMoney()) {
                    dtlDTO.setOpenMoney(dtlDTO.getMoney());
                }
            }
        }
        Collections.sort(dtls, new Comparator<RatBkRationAddDtlVm>() {
            public int compare(RatBkRationAddDtlVm o1, RatBkRationAddDtlVm o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        for (RatBkRationAddDtlVm dtlDTO : dtls) {
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
        listDTO.setRationDtls(dtls);
    }

    @ApiOperation("暂存银行授信追加信息表")
    @LogMessage(content = "暂存银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationAddService.saveRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }

    @ApiOperation("提交银行授信追加信息表")
    @LogMessage(content = "提交银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationAddService.saveRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }

    @ApiOperation("修改暂存银行授信追加信息表")
    @LogMessage(content = "修改暂存银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationAddService.editRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }

    @ApiOperation("修改提交银行授信追加信息表")
    @LogMessage(content = "修改提交银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationAddService.editRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }

    @ApiOperation("暂存集团授信追加信息表")
    @LogMessage(content = "暂存集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpsave")
    public AjaxJson cpsave(@RequestBody RatBkRationAddVm listDTO) {
        save(listDTO);
        return success();
    }

    @ApiOperation("提交集团授信追加信息表")
    @LogMessage(content = "提交集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpsubmit")
    public AjaxJson cpsubmit(@RequestBody RatBkRationAddVm listDTO) {
        submit(listDTO);
        return success();
    }

    @ApiOperation("修改暂存集团授信追加信息表")
    @LogMessage(content = "修改暂存集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpeditSave")
    public AjaxJson cpeditSave(@RequestBody RatBkRationAddVm listDTO) {
        editSave(listDTO);
        return success();
    }

    @ApiOperation("修改提交集团授信追加信息表")
    @LogMessage(content = "修改提交集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpeditSubmit")
    public AjaxJson cpeditSubmit(@RequestBody RatBkRationAddVm listDTO) {
        editSubmit(listDTO);
        return success();
    }

    @ApiOperation("删除授信追加信息表")
    @LogMessage(content = "删除授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationAdd ratBkrationAdd = ratBkrationAddService.get(id);
        ratBkrationAdd.setStatus(RationConstant.STATUS_DELETE);
        ratBkrationAddService.updateEntity(ratBkrationAdd);
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkrationAdd.getRatCode(),
                ratBkrationAdd.getAddCode());
        if (dto != null) {
            dto.setStatus(FcBondConstant.STATUE_DEL);
            fcBondHookService.updateFcBondApply(dto);
        }
        return success();
    }

    @ApiOperation("批量删除授信追加信息表")
    @LogMessage(content = "批量删除授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        for (String id : ids.split(",")) {
            RatBkrationAdd ratBkrationAdd = ratBkrationAddService.get(id);
            ratBkrationAddService.deleteEntity(ratBkrationAdd);
        }
        return success();
    }

    @ApiOperation("查询授信追加审批列表")
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public AjaxJson<RatBkrationApproveInitVO> listApprove(String flowStatus, String cycleTime) {
        RatBkrationApproveInitVO result = new RatBkrationApproveInitVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        result.setFlag(RationConstant.RAT_ADD_FLAG_BANK);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信追加审批数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput v1Input, String flag) {
        DataTablesInput input = v1Input.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);
        }
        if (RationConstant.RAT_ADD_FLAG_BANK.equals(flag)) {
            return AjaxJson.success(ratBkrationAddService.listApproveOutputData(input));
        } else {
            return AjaxJson.success(ratBkrationAddService.listCpApproveOutputData(input));
        }
    }

    @ApiOperation("查询集团授信追加审批列表")
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCpAddApprove")
    public AjaxJson<RatBkrationApproveInitVO> listCpAddApprove(String flowStatus, String cycleTime) {
        RatBkrationApproveInitVO result = new RatBkrationApproveInitVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        result.setFlag(RationConstant.RAT_ADD_FLAG_GROUP);
        return AjaxJson.success(result);
    }

    @ApiOperation("查询授信追加审批详情")
    @GetMapping("/viewBkrationAddApprove")
    public AjaxJson<RatBkrationAddApproveDetailVO> viewBkrationAddApprove(String businessId, String taskId) {
        ConfigDTO config = ratBkrationService.getStartOpenRat();
        String page;
        RatBkrationAddApproveDetailVO result = new RatBkrationAddApproveDetailVO();
        RatBkrationAdd rationAdd = ratBkrationAddService.get(businessId, true);
        if (RationConstant.RAT_ADD_FLAG_GROUP.equals(rationAdd.getFlag())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            result.setCpPage(ratBkration);
            result.setAddCode(rationAdd.getId());
            page = RationConstant.OPEN_SIGN_1.equals(config.getValue()) ? "viewCpRatBkrationAddOpen" : "viewCpRatBkrationAdd";
        } else {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(businessId);
            result.setBankPage(ratBkration);
            page = RationConstant.OPEN_SIGN_1.equals(config.getValue()) ? "viewRatBkrationAddOpen" : "viewRatBkrationAdd";
        }
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(rationAdd.getRatCode(), rationAdd.getAddCode());
        if (dto != null) {
            result.setDto(dto);
        }
        result.setTaskId(taskId);
        result.setViewPage(page);
        return AjaxJson.success(result);
    }

    @ApiOperation("授信追加手机审批详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatBkrationAddApproveDetailVO> mobileBkrationAddApprove(String businessId, String taskId) {
        String page = "mobileRatBkrationAdd";
        RatBkrationAddApproveDetailVO result = new RatBkrationAddApproveDetailVO();
        RatBkrationAdd rationAdd = ratBkrationAddService.get(businessId, true);
        if (RationConstant.RAT_ADD_FLAG_GROUP.equals(rationAdd.getFlag())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            result.setCpPage(ratBkration);
            result.setAddCode(rationAdd.getId());
        } else {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(businessId);
            result.setBankPage(ratBkration);
        }
        result.setTaskId(taskId);
        result.setViewPage(page);
        return AjaxJson.success(result);
    }
}
