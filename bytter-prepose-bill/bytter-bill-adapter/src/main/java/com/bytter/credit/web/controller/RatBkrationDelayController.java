package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import com.bytter.credit.domain.vo.RatBkrationDelayVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.dto.ConfigDTO;
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
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Controller
 * @Description: 授信延期表
 * @author onlineGenerator
 * @date 2017-08-26 09:13:31
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/cms/rat/ratbkrationdelay")
public class RatBkrationDelayController extends BaseController {
    @Autowired
    private RatBkrationDelayService ratBkrationDelayService;
    @Autowired
    private RatBkrationService rationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    /**
     * 授信延期表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询授信延期表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String list(Model model, Integer start, Integer len, RatBkrationVO rationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        //查询参数设置
        this.setSearchParams(model, input, rationVO);
        DataTablesOutput output = ratBkrationDelayService.listDelayOutputData(input, RationConstant.CMS_RATION_DELAY_QUERY, getUserId());
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
        return "cms/rat/bkration/listRatBkrationDelay";
    }

    /**
     * 授信延期导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "授信延期导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkrationDelay")
    public void exportListRatBkrationDelay(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
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
        setSearchParams(model, input, rationVO);
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

    /**
     * 授信延期表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ratBkrationDelayService.getDataTables(dataTablesInput);
    }
    /**
     * 授信延期表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(String ratCode, Model model) {
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(ratCode);// 授信录入明细
            model.addAttribute("ratBkrationPage", pageVO);
            // 授信延期日期在结束日期的基础上加一天
            Date endDate = pageVO.getEndDate();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(endDate);
            rightNow.add(Calendar.DAY_OF_MONTH, 1);
            Date delaydate = rightNow.getTime();
            model.addAttribute("delayDate", delaydate);
        }
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        ConfigDTO config = rationService.getStartOpenRat();// 敞口
        String page = "addRatBkrationDelay";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addRatBkrationDelayOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 授信延期表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatBkrationDelay ratBkrationDelay, Model model) {
        if (StringUtils.isNotEmpty(ratBkrationDelay.getId())) {
            RatBkrationDelay delay = ratBkrationDelayService.get(ratBkrationDelay.getId(), true);
            model.addAttribute("delay", delay);
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(delay.getRatCode());// 授信录入明细
            model.addAttribute("ratBkrationPage", pageVO);
        }
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        ConfigDTO config = rationService.getStartOpenRat();// 敞口
        String page = "updateRatBkrationDelay";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateRatBkrationDelayOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 授信延期表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信延期表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationDelay delay = ratBkrationDelayService.getRationDelayByRatCode(id);
            if (null != delay) {
                model.addAttribute("delaySign", "1");
                model.addAttribute("delayDate", delay.getDelayDate());
                model.addAttribute("attachmentId", delay.getAttachmentId());
            } else {
                model.addAttribute("delaySign", "0");
            }
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(id);// 授信录入明细
            model.addAttribute("ratBkrationPage", pageVO);
        }
        model.addAttribute("viewFlag", "1");
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        ConfigDTO config = rationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationDelay";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationDelayOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 暂存添加授信延期表
     *
     * @param
     * @return
     */
    @LogMessage(content = "暂存添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkrationDelayVO delayVO) {
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
    @LogMessage(content = "提交添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatBkrationDelayVO delayVO) {
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
    @LogMessage(content = "暂存添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatBkrationDelayVO delayVO) {
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
    @LogMessage(content = "提交添加授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatBkrationDelayVO delayVO) {
        delayVO.setStatus(RationConstant.STATUS_WAIT_CHECK);// 待审批
        ratBkrationDelayService.saveRationDelay(delayVO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 删除授信延期表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(RatBkrationDelayVO delayVO) {
        ratBkrationDelayService.deleteRationDelay(delayVO, getUserId());
        return success();
    }
    /**
     * 授信延期审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信延期发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDelayApprove")
    public String listDelayApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/bkration/approve/listRatBkrationDelayApprove";
    }
    /**
     * 授信延期审批 - 数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input) {
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return ratBkrationDelayService.getDataApplyApproveTables(input);
    }
    /**
     * 授信延期审批 - 详情页面
     */
    @GetMapping("/viewDelayApprove")
    public String viewDelayApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            RatBkrationDelay delay = ratBkrationDelayService.get(businessId, true);
            model.addAttribute("delay", delay);
            RatBkrationPageVO pageVO = rationService.getBkRationBeanById(delay.getRatCode());// 授信录入明细
            model.addAttribute("ratBkrationPage", pageVO);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        // 获取授信参数
        ConfigDTO config = rationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationDelayApprove";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationDelayOpenApprove";// 敞口跳转
        }
        return "cms/rat/bkration/approve/" + page;
    }
    /**
     * 授信延期审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileDelayApprove(String businessId, String taskId, Model model) {
        RatBkrationPageVO pageVO = null;
        if (!EmptyUtils.isEmpty(businessId)) {
            RatBkrationDelay delay = ratBkrationDelayService.get(businessId, true);
            model.addAttribute("delay", delay);
            pageVO = rationService.getBkRationBeanById(delay.getRatCode());// 授信录入明细
            model.addAttribute("ratBkrationPage", pageVO);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        ConfigDTO guaConfig = rationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {// 参数设置挂接担保
            if (pageVO.getIsHangGuarantee() != null
                    && pageVO.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {// 授信挂接担保
                GuaContractUseDTO guaContractUseDTO = contractOccupServie.getGuaContractOccupyDtl(pageVO.getId(),
                        pageVO.getRatCode());
                model.addAttribute("guaContractUseDTO", guaContractUseDTO);
            }
        }
        return "cms/rat/bkration/approve/mobileRatBkrationDelay";
    }
}
