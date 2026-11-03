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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信额度调剂表
 * @date 2017-07-31 16:10:53
 */
@Controller
@RequestMapping("/cms/rat/ratbkrationrelief")
public class RatBkrationReliefController extends BaseController {
    @Autowired
    private RatBkrationReliefService ratBkrationReliefService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatBkrationDisService ratDisService;
    @Autowired
    private RatBkrationDetailService ratDtlService;

    /**
     * 授信额度调剂表列表 页面跳转 取审批通过的授信分配数据
     *
     * @return
     */
    @LogMessage(content = "查询授信额度调剂表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String list(RatBkrationVO rationVO, Model model, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());

        //授信单位
        List<String> corpIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            String[] split = rationVO.getCorpId().split(",");
            corpIds.addAll(Arrays.asList(split));
            model.addAttribute("corpId", rationVO.getCorpId());
        }

        //查询参数设置
        this.setSearchParams(model, input, rationVO);

        DataTablesOutput output = ratDisService.queryRationDisCheckData(input, RationConstant.CMS_RATION_RELIEF_QUERY, getUserId(), corpIds, rationVO.getItemCode());
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
        return "cms/rat/bkration/listRatBkrationRelief";
    }

    /**
     * 银行授信额度调剂导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "银行授信额度调剂导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkRationRelief")
    public void exportListRatBkRationreLief(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "银行授信额度调剂表.xls";
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

        //授信单位
        List<String> corpIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            String[] split = rationVO.getCorpId().split(",");
            corpIds.addAll(Arrays.asList(split));
            model.addAttribute("corpId", rationVO.getCorpId());
        }
        //查询参数设置
        this.setSearchParams(model, input, rationVO);
        DataTablesOutput output = ratDisService.queryRationDisCheckData(input, ResourceConstant.CMS_RATION_RELIEF_QUERY, getUserId(), corpIds, rationVO.getItemCode());
        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度调剂表", CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
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
        String ratType = RationConstant.RAT_TYPE_0;//授信种类为银行授信
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        model.addAttribute("ratType", ratType);

        //授信模式
        if (StringUtils.isNotBlank(rationVO.getRatMode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", rationVO.getRatMode());
            model.addAttribute("ratMode", rationVO.getRatMode());
        }
        //授信合同号
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        //授信编号
        String ratCode = rationVO.getRatCode();
        if (!StringUtils.isEmpty(ratCode)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratCode);
            model.addAttribute("ratCode", ratCode);
        }
        //授信开始时间段最小时间
        if (null != rationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
            model.addAttribute("gBeginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
        }
        //授信开始时间段最大时间
        if (null != rationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
            model.addAttribute("lBeginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
        }
        //授信结束时间段最小时间
        if (null != rationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(rationVO.getgEndDate()));
            model.addAttribute("gEndDate", DateUtils.formatDate(rationVO.getgEndDate()));
        }
        //授信结束时间段最大时间
        if (null != rationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(rationVO.getlEndDate()));
            model.addAttribute("lEndDate", DateUtils.formatDate(rationVO.getlEndDate()));
        }
        // 银行信息
        if (StringUtils.isNotBlank(rationVO.getBankCode())) {
            String bankcodes = rationVO.getBankCode().substring(0, rationVO.getBankCode().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankcodes);
            model.addAttribute("bankCode", bankcodes);
        }
        // 授信品种
        if (StringUtils.isNotBlank(rationVO.getItemCode())) {
            String itemCodes = rationVO.getItemCode().substring(0, rationVO.getItemCode().length() - 1);
            input.getFormdata().put("INs_itemCode", itemCodes);
            input.addFilter(MatchTypeEnum.IN, "itemCode", itemCodes);
            model.addAttribute("itemCode", itemCodes);
        }
    }

    /**
     * 授信额度调剂表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String ratCode) {
        dataTablesInput.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        return ratBkrationReliefService.queryRatBkRationReliefByRatCode(dataTablesInput,
                RationConstant.CMS_RATION_RELIEF_QUERY, getUserId());
    }

    /**
     * 银行授信追加信息列表查询
     *
     * @return
     */
    @LogMessage(content = "银行授信追加信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public String addList(RatBkrationPageVO rationVO, Model model, Integer start) {
        rationVO = ratBkrationService.getRatBkrationInfo(rationVO.getRatCode());
        model.addAttribute("addVO", rationVO);
        return "cms/rat/bkration/listRatBkrationReliefed";
    }

    /**
     * 功能介绍 ：判断授信是否可以进行调剂。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/checkRatDis")
    public String checkRatDis(RatBkrationRelief ratBkrationRelief, Model model) {
        /*
         * 根据已分配明细的分配方式获取不同的调剂单位 分配方式分为：自用，金额共享，总额共享，金额共享+自用，金额共享+总额共享
         */
        if (StringUtils.isNotEmpty(ratBkrationRelief.getId())) {
            ratBkrationRelief = ratBkrationReliefService.get(ratBkrationRelief.getId());
            model.addAttribute("ratBkrationReliefPage", ratBkrationRelief);
        }
        return "cms/rat/bkration/addRatBkrationRelief";
    }

    /**
     * 授信额度调剂表新增页面跳转
     *
     * @return
     */
    //
    @GetMapping("/getRefCorpDTOs")
    @ResponseBody
    public List<CorpDTO> getRefCorpDTOs(String ratCode, String corpId, String flag) {
        /*
         * 根据已分配明细的分配方式获取不同的调剂单位 分配方式分为：自用，金额共享，总额共享，金额共享+自用，金额共享+总额共享
         */
        return ratBkrationReliefService.queryRatReliefCorp(flag, ratCode, corpId);
    }

    /**
     * 获取授信分配单位已分配的授信品种类型
     */
    @PostMapping("/getReliefItemFlag")
    @ResponseBody
    public List<RatItemDisVO> getReliefItemFlag(String ratCode, String corpId) {
        return ratBkrationReliefService.getCorpItemFlags(ratCode, corpId);
    }

    /**
     * 获取授信分配单位已分配的授信品种
     */
    @PostMapping("/getReliefItem")
    @ResponseBody
    public List<RatItemDisVO> getReliefItem(String ratCode, String corpId, String itemFlag) {
        return ratBkrationReliefService.getItemCustomDTOs(ratCode, corpId, itemFlag);
    }

    /**
     * 根据授信品种获取授信品种额度
     */
    @PostMapping("/getItemAssure")
    @ResponseBody
    public RatItemDisVO getItemAssure(String ratCode, String itemCode) {
        return ratDtlService.getItemAssure(ratCode, itemCode);
    }

    /**
     * 获取授信分配单位已分配授信品种的授信额度
     */
    @PostMapping("/getDisItemMoney")
    @ResponseBody
    public RatBkrationCpAddDtlDTO getDisItemMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
        return ratDisService.getRationDisItemMoney(ratCode, corpId, itemFlag, itemCode);
    }

    /**
     * 授信额度调剂表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(String ratCode, String corpId, String flag, Model model) {
        /*
         * 根据已分配明细的分配方式获取不同的调剂单位 分配方式分为：自用，金额共享，总额共享，金额共享+自用，金额共享+总额共享
         */
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationReliefPageVO refVO = ratBkrationReliefService.initRationReliefPage(ratCode);
            model.addAttribute("ratBkrationReliefPage", refVO);
            model.addAttribute("flag", ratDisService.queryRationDisWay(ratCode));// 所有分配方式
        }
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "addRatBkrationRelief";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addRatBkrationReliefOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信额度调剂表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatBkrationRelief relief, Model model) {
        if (StringUtils.isNotEmpty(relief.getId())) {
            Map<String, Object> result = ratBkrationReliefService.editRationReliefPage(relief.getId());
            model.addAttribute("result", result);
        }
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "updateRatBkrationRelief";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateRatBkrationReliefOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信额度调剂表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信额度调剂表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Map<String, Object> result = ratBkrationReliefService.editRationReliefPage(id);
            model.addAttribute("result", result);
        }
        model.addAttribute("viewFlag", "1");
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationRelief";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationReliefOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
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
            refVO.setReliefOpenMoney(refVO.getReliefMoney());
        }
        refVO.setReliefDtls(dtls);
    }

    /**
     * 暂存授信额度调剂表
     *
     * @param
     * @return
     */
    @LogMessage(content = "添加授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.saveRationRelief(refVO, getUserId());
        return success();
    }

    /**
     * 提交授信额度调剂表
     *
     * @param
     * @return
     */
    @LogMessage(content = "提交授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.saveRationRelief(refVO, getUserId());
        return success();
    }

    /**
     * 暂存授信额度调剂表
     *
     * @param
     * @return
     */
    @LogMessage(content = "添加授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_TEMP);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.updateRationRelief(refVO, getUserId());
        return success();
    }

    /**
     * 提交授信额度调剂表
     *
     * @param
     * @return
     */
    @LogMessage(content = "提交授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatBkrationReliefPageVO refVO) {
        refVO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        setRatBkrationRelief(refVO);
        ratBkrationReliefService.updateRationRelief(refVO, getUserId());
        return success();
    }

    /**
     * 修改授信额度调剂表
     *
     * @param ratBkrationRelief
     * @return
     */
    //
    @LogMessage(content = "修改授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatBkrationRelief ratBkrationRelief) {
        RatBkrationRelief t = ratBkrationReliefService.get(ratBkrationRelief.getId());
        BeanUtils.copyNotNullProperties(ratBkrationRelief, t);
        ratBkrationReliefService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除授信额度调剂表
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratBkrationReliefService.updateRationRefStatus(id);
        return success();
    }

    /**
     * 批量删除授信额度调剂表
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信额度调剂表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            ratBkrationReliefService.updateRationRefStatus(id);
        }
        return success();
    }

    /**
     * 授信调剂审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信调剂审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/bkration/approve/listRatBkrationReliefApprove";
    }

    /**
     * 授信调剂审批 - 数据
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
        return ratBkrationReliefService.listApproveOutputData(input);
    }

    /**
     * 授信调剂审批 - 详情页面
     */
    @GetMapping("/viewBkrationApprove")
    public String viewBkrationApprove(String businessId, String taskId, Model model) {
        Map<String, Object> result = ratBkrationReliefService.editRationReliefPage(businessId);
        model.addAttribute("result", result);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationRelief";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationReliefOpen";// 敞口跳转
        }
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/viewRatBkrationApprove";
    }

    /**
     * 授信调剂手机审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileBkrationApprove(String businessId, String taskId, Model model) {
        Map<String, Object> result = ratBkrationReliefService.editRationReliefPage(businessId);
        model.addAttribute("result", result);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 获取授信参数
        // ConfigDTO config = ratBkrationService.getStartOpenRat();//敞口
        String page = "mobileRatBkrationRelief";
        // if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
        // page = "mobileRatBkrationReliefOpen";//敞口跳转
        // }
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/" + page;
    }
}
