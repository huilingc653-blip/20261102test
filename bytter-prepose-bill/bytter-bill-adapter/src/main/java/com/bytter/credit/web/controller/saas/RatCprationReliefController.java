package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationRelief;
import com.bytter.credit.domain.service.RatBkrationDetailService;
import com.bytter.credit.domain.service.RatBkrationDisService;
import com.bytter.credit.domain.service.RatCprationReliefService;
import com.bytter.credit.domain.service.RatCprationService;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 内部授信调剂
 */
@Api(tags = "内部授信调剂", value = "/v1-cms/cms/rat/ratcprationrelief")
@RestController("v1RatCprationReliefController")
@RequestMapping("/v1-cms/cms/rat/ratcprationrelief")
public class RatCprationReliefController extends BaseController {

    @Autowired
    private RatBkrationDisService ratDisService;
    @Autowired
    private RatBkrationDetailService ratDtlService;
    @Autowired
    private RatCprationReliefService ratCpRelieService;
    @Autowired
    private RatCprationService ratCpService;

    /**
     * 内部授信额度调剂表列表(取审批通过的内部授信分配数据)
     *
     * @param rationVO 查询条件(合同号/授信时间)
     * @param start    当前页
     * @param len      每页条数
     * @return 调剂表列表数据
     */
    @ApiOperation("查询内部授信额度调剂表列表")
    @LogMessage(content = "查询内部授信额度调剂表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<DataTablesOutput> list(RatBkrationVO rationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
        }
        if (null != rationVO.getBeginDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(rationVO.getBeginDate()), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(rationVO.getBeginDate()), Date.class);// 小于等于授信结束日期
        }
        DataTablesOutput output = ratCpRelieService.queryRationDisCheckData(input, RationConstant.CMS_CPRATION_RELIEF_QUERY,
                getUserId());
        return success(output);
    }

    /**
     * 授信额度调剂表表格数据
     *
     * @param dataTablesInput 表格入参
     * @param ratCode         授信编号
     * @return 调剂表表格数据
     */
    @ApiOperation("查询授信额度调剂表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String ratCode) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        return success(ratCpRelieService.queryRatBkRationReliefByRatCode(input, RationConstant.CMS_CPRATION_RELIEF_QUERY,
                getUserId()));
    }

    /**
     * 内部授信调剂信息列表查询
     *
     * @param rationVO 授信明细(ratCode)
     * @return 授信明细数据
     */
    @ApiOperation("内部授信调剂信息列表查询")
    @LogMessage(content = "内部授信调剂信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public AjaxJson<RatBkrationPageVO> addList(RatBkrationPageVO rationVO) {
        return success(ratCpService.getRatBkrationInfo(rationVO.getRatCode()));
    }

    /**
     * 内部授信调剂单位列表
     *
     * @param ratCode 授信编号
     * @param corpId  单位id
     * @param flag    调剂标志
     * @return 调剂单位列表
     */
    @ApiOperation("获取内部授信调剂单位列表")
    @GetMapping("/getRefCorpDTOs")
    public AjaxJson<List<CorpDTO>> getRefCorpDTOs(String ratCode, String corpId, String flag) {
        return success(ratCpRelieService.queryRatReliefCorp(flag, ratCode, corpId));
    }

    /**
     * 获取授信分配单位已分配的授信品种类型
     *
     * @param ratCode 授信编号
     * @param corpId  单位id
     * @return 授信品种类型列表
     */
    @ApiOperation("获取授信分配单位已分配的授信品种类型")
    @PostMapping("/getReliefItemFlag")
    public AjaxJson<List<RatItemDisVO>> getReliefItemFlag(String ratCode, String corpId) {
        return success(ratCpRelieService.getCorpItemFlags(ratCode, corpId));
    }

    /**
     * 获取授信分配单位已分配的授信品种
     *
     * @param ratCode  授信编号
     * @param corpId   单位id
     * @param itemFlag 品种类型
     * @return 授信品种列表
     */
    @ApiOperation("获取授信分配单位已分配的授信品种")
    @PostMapping("/getReliefItem")
    public AjaxJson<List<RatItemDisVO>> getReliefItem(String ratCode, String corpId, String itemFlag) {
        return success(ratCpRelieService.getItemCustomDTOs(ratCode, corpId, itemFlag));
    }

    /**
     * 根据授信品种获取授信品种额度
     *
     * @param ratCode  授信编号
     * @param itemCode 品种编号
     * @return 授信品种额度
     */
    @ApiOperation("根据授信品种获取授信品种额度")
    @PostMapping("/getItemAssure")
    public AjaxJson<RatItemDisVO> getItemAssure(String ratCode, String itemCode) {
        return success(ratDtlService.getItemAssure(ratCode, itemCode));
    }

    /**
     * 获取授信分配单位已分配授信品种的授信额度
     *
     * @param ratCode  授信编号
     * @param corpId   单位id
     * @param itemFlag 品种类型
     * @param itemCode 品种编号
     * @return 授信额度
     */
    @ApiOperation("获取授信分配单位已分配授信品种的授信额度")
    @PostMapping("/getDisItemMoney")
    public AjaxJson<BigDecimal> getDisItemMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
        return success(ratCpRelieService.getDisItemMoney(ratCode, corpId, itemFlag, itemCode));
    }

    /**
     * 授信额度调剂表新增页面数据
     *
     * @param ratCode 授信编号
     * @param corpId  单位id
     * @param flag    调剂标志
     * @return 新增页面数据
     */
    @ApiOperation("授信额度调剂表新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatCprationReliefFormVO> add(String ratCode, String corpId, String flag) {
        RatCprationReliefFormVO result = new RatCprationReliefFormVO();
        if (StringUtils.isNotEmpty(ratCode)) {
            result.setRatBkrationReliefPage(ratCpRelieService.initRationReliefPage(ratCode));
            result.setFlag(ratDisService.queryRationDisWay(ratCode));// 所有分配方式
        }
        return success(result);
    }

    /**
     * 授信额度调剂表编辑页面数据
     *
     * @param relief 调剂信息(id)
     * @return 编辑页面数据
     */
    @ApiOperation("授信额度调剂表编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatCprationReliefFormVO> update(RatBkrationRelief relief) {
        RatCprationReliefFormVO result = new RatCprationReliefFormVO();
        if (StringUtils.isNotEmpty(relief.getId())) {
            populateReliefResult(result, relief.getId());
        }
        return success(result);
    }

    /**
     * 查询授信额度调剂表详情
     *
     * @param id 主键id
     * @return 调剂表详情数据
     */
    @ApiOperation("查询授信额度调剂表详情")
    @LogMessage(content = "查询授信额度调剂表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatCprationReliefFormVO> view(@PathVariable("id") String id) {
        RatCprationReliefFormVO result = new RatCprationReliefFormVO();
        if (StringUtils.isNotEmpty(id)) {
            populateReliefResult(result, id);
        }
        result.setViewFlag("1");
        return success(result);
    }

    /**
     * 暂存授信额度调剂表
     *
     * @param refVO 调剂表数据
     * @return 操作结果
     */
    @ApiOperation("暂存授信额度调剂表")
    @LogMessage(content = "新增内部授信额度调剂", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratCpRelieService.saveRationRelief(refVO, getCurrentUser());
        return success();
    }

    /**
     * 提交授信额度调剂表
     *
     * @param refVO 调剂表数据
     * @return 操作结果
     */
    @ApiOperation("提交授信额度调剂表")
    @LogMessage(content = "提交内部授信额度调剂", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratCpRelieService.saveRationRelief(refVO, getCurrentUser());
        return success();
    }

    /**
     * 修改暂存授信额度调剂表
     *
     * @param refVO 调剂表数据
     * @return 操作结果
     */
    @ApiOperation("修改暂存授信额度调剂表")
    @LogMessage(content = "增加内部授信额度调剂", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratCpRelieService.updateRationRelief(refVO, getCurrentUser());
        return success();
    }

    /**
     * 修改提交授信额度调剂表
     *
     * @param refVO 调剂表数据
     * @return 操作结果
     */
    @ApiOperation("修改提交授信额度调剂表")
    @LogMessage(content = "提交内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratCpRelieService.updateRationRelief(refVO, getCurrentUser());
        return success();
    }

    /**
     * 修改授信额度调剂表
     *
     * @param ratBkrationRelief 调剂信息
     * @return 操作结果
     */
    @ApiOperation("修改授信额度调剂表")
    @LogMessage(content = "修改内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatBkrationRelief ratBkrationRelief) {
        RatBkrationRelief t = ratCpRelieService.get(ratBkrationRelief.getId());
        BeanUtils.copyNotNullProperties(ratBkrationRelief, t);
        ratCpRelieService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除授信额度调剂表
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除授信额度调剂表")
    @LogMessage(content = "删除内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratCpRelieService.updateRationRefStatus(id);
        return success();
    }

    /**
     * 批量删除授信额度调剂表
     *
     * @param body 主键id集合
     * @return 操作结果
     */
    @ApiOperation("批量删除授信额度调剂表")
    @LogMessage(content = "批量删除内部授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        for (String id : ids.split(",")) {
            ratCpRelieService.updateRationRefStatus(id);
        }
        return success();
    }

    /**
     * 授信调剂审批列表页面数据
     *
     * @param flowStatus 流程状态
     * @param cycleTime  周期时间
     * @return 审批列表页面数据
     */
    @ApiOperation("查询授信调剂审批列表页面数据")
    @LogMessage(content = "查询内部授信调剂审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public AjaxJson<ApproveListVO> listApprove(String flowStatus, String cycleTime) {
        ApproveListVO result = new ApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 授信调剂审批列表数据
     *
     * @param dataTablesInput 表格入参
     * @return 审批列表数据
     */
    @ApiOperation("查询授信调剂审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return success(ratCpRelieService.listApproveOutputData(input));
    }

    /**
     * 授信调剂审批详情页面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     * @return 审批详情数据
     */
    @ApiOperation("授信调剂审批详情页面数据")
    @GetMapping("/viewCprationApprove")
    public AjaxJson<RatCprationReliefFormVO> viewCprationApprove(String businessId, String taskId) {
        RatCprationReliefFormVO result = new RatCprationReliefFormVO();
        populateReliefResult(result, businessId);
        result.setTaskId(taskId);
        result.setViewPage("viewRatCprationRelief");
        return success(result);
    }

    /**
     * 授信调剂手机审批详情页面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     * @return 手机审批详情数据
     */
    @ApiOperation("授信调剂手机审批详情页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatCprationReliefFormVO> mobileApprove(String businessId, String taskId) {
        RatCprationReliefFormVO result = new RatCprationReliefFormVO();
        populateReliefResult(result, businessId);
        result.setTaskId(taskId);
        result.setViewPage("mobileRatCprationRelief");
        return success(result);
    }

    private void populateReliefResult(RatCprationReliefFormVO result, String reliefId) {
        Map<String, Object> serviceResult = ratCpRelieService.editRationReliefPage(reliefId);
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
        List<RatBkrationReliefDtlPageVO> dtls = new ArrayList<RatBkrationReliefDtlPageVO>();
        List<RatBkrationReliefDtlPageVO> reliefDtls = refVO.getReliefDtls();
        for (RatBkrationReliefDtlPageVO dtlPageVO : reliefDtls) {
            if (StringUtils.isEmpty(dtlPageVO.getReliefItemCode())
                    && StringUtils.isEmpty(dtlPageVO.getByReliefItemCode())) {
                continue;
            }
            dtls.add(dtlPageVO);
        }
        if (null == refVO.getReliefOpenMoney()) {
            refVO.setReliefOpenMoney(new BigDecimal(0));
        }
        refVO.setReliefDtls(dtls);
    }
}
