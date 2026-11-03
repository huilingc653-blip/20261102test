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
import com.bytter.credit.domain.entity.RatBkrationDelay;
import com.bytter.credit.domain.service.RatBkrationDelayService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vo.*;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
/**
 * @Title: Controller
 * @Description: 授信延期表
 * @author onlineGenerator
 * @date 2017-08-26 09:13:31
 * @version 1.0
 *
 */
@Api(tags = "授信延期表", value = "/v1-cms/cms/rat/ratbkrationdelay")
@RestController("v1RatBkrationDelayController")
@RequestMapping("/v1-cms/cms/rat/ratbkrationdelay")
public class RatBkrationDelayController extends BaseController {
    @Autowired
    private RatBkrationDelayService ratBkrationDelayService;
    @Autowired
    private RatBkrationService rationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    /**
     * 授信延期表列表
     *
     * @return
     */
    @ApiOperation("查询授信延期表列表")
    @LogMessage(content = "查询授信延期表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<RatBkrationDelayListVO> list(Integer start, Integer len, RatBkrationVO rationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        //查询参数设置
        this.setSearchParams(input, rationVO);
        DataTablesOutput output = ratBkrationDelayService.listDelayOutputData(input, RationConstant.CMS_RATION_DELAY_QUERY, getUserId());
        RatBkrationDelayListVO result = new RatBkrationDelayListVO();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }
        result.setRations(output.getData());
        result.setCurr(start);
        result.setPageCount(pageCount);
        result.setPageSize(pageSize);
        result.setResultCount(resultCount);
        result.setRatType(RationConstant.RAT_TYPE_0);
        result.setCorpId(rationVO.getCorpId());
        result.setContractNo(rationVO.getContractNo());
        result.setRatCode(rationVO.getRatCode());
        result.setBankCode(rationVO.getBankCode());
        result.setItemCode(rationVO.getItemCode());
        result.setRatMode(rationVO.getRatMode());
        result.setgBeginDate(rationVO.getgBeginDate() != null ? DateUtils.formatDate(rationVO.getgBeginDate()) : null);
        result.setlBeginDate(rationVO.getlBeginDate() != null ? DateUtils.formatDate(rationVO.getlBeginDate()) : null);
        result.setgEndDate(rationVO.getgEndDate() != null ? DateUtils.formatDate(rationVO.getgEndDate()) : null);
        result.setlEndDate(rationVO.getlEndDate() != null ? DateUtils.formatDate(rationVO.getlEndDate()) : null);
        return success(result);
    }

    /**
     * 授信延期导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     */
    @ApiOperation("授信延期导出")
    @LogMessage(content = "授信延期导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkrationDelay")
    public void exportListRatBkrationDelay(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO) throws Exception {
        // 导出的文件名
        String fileName = "授信延期表.xls";
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
        setSearchParams(input, rationVO);
        DataTablesOutput output = ratBkrationDelayService.listDelayOutputData(input, RationConstant.CMS_RATION_DELAY_QUERY, getUserId());
        //导出数据
        HSSFWorkbook workbook = rationService.exportListRatBkration(output, "授信延期表", CommonConstant.NO, CommonConstant.NO, CommonConstant.YES, CommonConstant.NO);
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
     * @param input    查询参数设置对象
     * @param rationVO 查询条件
     */
    private void setSearchParams(DataTablesInput input, RatBkrationVO rationVO) {
        //授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        // 授信单位
        String corpId = rationVO.getCorpId();
        if (!StringUtils.isEmpty(corpId)) {
            input.addFilter(MatchTypeEnum.IN, "corpId", corpId);
        }
        // 授信合同号
        if (!StringUtils.isEmpty(rationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", rationVO.getContractNo());
        }
        // 授信编号
        if (!StringUtils.isEmpty(rationVO.getRatCode()) && !"undefined".equals(rationVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", rationVO.getRatCode());
        }
        // 银行信息
        String bankCode = rationVO.getBankCode();
        if (!StringUtils.isEmpty(bankCode)) {
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCode);
        }
        // 授信品种
        String itemCode = rationVO.getItemCode();
        if (!StringUtils.isEmpty(itemCode)) {
            input.getFormdata().put("itemCode", itemCode);
        }
        // 授信模式
        String ratMode = rationVO.getRatMode();
        if (!StringUtils.isEmpty(ratMode)) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", ratMode);
        }
        // 授信开始时间段最大时间
        if (null != rationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
        }
        // 授信开始时间段最小时间
        if (null != rationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
        }
        // 授信结束时间段最大时间
        if (null != rationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(rationVO.getgEndDate()));
        }
        // 授信结束时间段最小时间
        if (null != rationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(rationVO.getlEndDate()));
        }
    }

    /**
     * 授信延期表 table列表
     *
     * @return
     */
    @ApiOperation("查询授信延期表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ratBkrationDelayService.getDataTables(input));
    }
    /**
     * 授信延期表新增页面数据
     *
     * @return
     */
    @ApiOperation("授信延期表新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatBkrationDelayAddVO> add(String ratCode) {
        RatBkrationDelayAddVO result = new RatBkrationDelayAddVO();
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(ratCode);// 授信录入明细
            result.setRatBkrationPage(pageVO);
            // 授信延期日期在结束日期的基础上加一天
            Date endDate = pageVO.getEndDate();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(endDate);
            rightNow.add(Calendar.DAY_OF_MONTH, 1);
            Date delaydate = rightNow.getTime();
            result.setDelayDate(delaydate);
        }
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        return success(result);
    }
    /**
     * 授信延期表编辑页面数据
     *
     * @return
     */
    @ApiOperation("授信延期表编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationDelayEditVO> updatePage(RatBkrationDelay ratBkrationDelay) {
        RatBkrationDelayEditVO result = new RatBkrationDelayEditVO();
        if (StringUtils.isNotEmpty(ratBkrationDelay.getId())) {
            RatBkrationDelay delay = ratBkrationDelayService.get(ratBkrationDelay.getId(), true);
            result.setDelay(delay);
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(delay.getRatCode());// 授信录入明细
            result.setRatBkrationPage(pageVO);
        }
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        return success(result);
    }
    /**
     * 授信延期表详情
     *
     * @return
     */
    @ApiOperation("查询授信延期表详情")
    @LogMessage(content = "查询授信延期表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatBkrationDelayViewVO> view(@PathVariable("id") String id) {
        RatBkrationDelayViewVO result = new RatBkrationDelayViewVO();
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationDelay delay = ratBkrationDelayService.getRationDelayByRatCode(id);
            if (null != delay) {
                result.setDelaySign("1");
                result.setDelayDate(delay.getDelayDate());
                result.setAttachmentId(delay.getAttachmentId());
            } else {
                result.setDelaySign("0");
            }
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(id);// 授信录入明细
            result.setRatBkrationPage(pageVO);
        }
        result.setViewFlag("1");
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        return success(result);
    }
    /**
     * 暂存添加授信延期表
     *
     * @param
     * @return
     */
    @ApiOperation("暂存添加授信延期表")
    @LogMessage(content = "暂存添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkrationDelayVO delayVO) {
        delayVO.setStatus(RationConstant.STATUS_TEMP);// 暂存
        ratBkrationDelayService.saveRationDelay(delayVO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 提交添加授信延期表
     *
     * @param
     * @return
     */
    @ApiOperation("提交添加授信延期表")
    @LogMessage(content = "提交添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatBkrationDelayVO delayVO) {
        delayVO.setStatus(RationConstant.STATUS_WAIT_CHECK);// 待审批
        ratBkrationDelayService.saveRationDelay(delayVO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 暂存添加授信延期表
     *
     * @param
     * @return
     */
    @ApiOperation("修改暂存授信延期表")
    @LogMessage(content = "暂存添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatBkrationDelayVO delayVO) {
        delayVO.setStatus(RationConstant.STATUS_TEMP);// 暂存
        ratBkrationDelayService.saveRationDelay(delayVO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 提交添加授信延期表
     *
     * @param
     * @return
     */
    @ApiOperation("修改提交授信延期表")
    @LogMessage(content = "提交添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatBkrationDelayVO delayVO) {
        delayVO.setStatus(RationConstant.STATUS_WAIT_CHECK);// 待审批
        ratBkrationDelayService.saveRationDelay(delayVO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 删除授信延期表
     *
     * @param delayVO
     * @return
     */
    @ApiOperation("删除授信延期表")
    @LogMessage(content = "删除授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson delete(@RequestBody RatBkrationDelayVO delayVO) {
        ratBkrationDelayService.deleteRationDelay(delayVO, getUserId());
        return success();
    }
    /**
     * 授信延期审批 - 列表页面数据
     */
    @ApiOperation("查询授信延期发放审批列表")
    @LogMessage(content = "查询授信延期发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDelayApprove")
    public AjaxJson<RatBkrationDelayApproveListVO> listDelayApprove(String flowStatus, String cycleTime) {
        RatBkrationDelayApproveListVO result = new RatBkrationDelayApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }
    /**
     * 授信延期审批 - 数据
     */
    @ApiOperation("查询授信延期发放审批数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2)) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return success(ratBkrationDelayService.getDataApplyApproveTables(input));
    }
    /**
     * 授信延期审批 - 详情
     */
    @ApiOperation("查询授信延期发放审批详情")
    @GetMapping("/viewDelayApprove")
    public AjaxJson<RatBkrationDelayApproveDetailVO> viewDelayApprove(String businessId, String taskId) {
        RatBkrationDelayApproveDetailVO result = new RatBkrationDelayApproveDetailVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            RatBkrationDelay delay = ratBkrationDelayService.get(businessId, true);
            result.setDelay(delay);
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(delay.getRatCode());// 授信录入明细
            result.setRatBkrationPage(pageVO);
        }
        result.setTaskId(taskId);// 必须要做此步骤
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        return success(result);
    }
    /**
     * 授信延期审批 - 手机详情
     */
    @ApiOperation("授信延期手机审批详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatBkrationDelayMobileApproveVO> mobileDelayApprove(String businessId, String taskId) {
        RatBkrationDelayMobileApproveVO result = new RatBkrationDelayMobileApproveVO();
        RatBkrationPageVO pageVO = null;
        if (!EmptyUtils.isEmpty(businessId)) {
            RatBkrationDelay delay = ratBkrationDelayService.get(businessId, true);
            result.setDelay(delay);
            pageVO = rationService.getBkRationBeanById(delay.getRatCode());// 授信录入明细
            result.setRatBkrationPage(pageVO);
        }
        result.setTaskId(taskId);// 必须要做此步骤
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {// 参数设置挂接担保
            if (pageVO != null && pageVO.getIsHangGuarantee() != null
                    && pageVO.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {// 授信挂接担保
                GuaContractUseDTO guaContractUseDTO = contractOccupServie.getGuaContractOccupyDtl(pageVO.getId(),
                        pageVO.getRatCode());
                result.setGuaContractUseDTO(guaContractUseDTO);
            }
        }
        return success(result);
    }
}
