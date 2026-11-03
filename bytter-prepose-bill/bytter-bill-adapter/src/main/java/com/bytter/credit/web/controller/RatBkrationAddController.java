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
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.service.RatBkrationAddService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vo.RatBkrationAddPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.google.common.collect.Lists;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信追加信息表
 * @date 2017-07-31 16:00:15
 */
@Controller
@RequestMapping("/cms/rat/ratbkrationadd")
public class RatBkrationAddController extends BaseController {
    @Autowired
    private RatBkrationAddService ratBkrationAddService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    /**
     * 银行授信追加信息表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询银行授信追加信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
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
        //授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        model.addAttribute("ratType", ratType);
        //授信合同号
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.getFormdata().put("likesContractNo", contractNo);
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        //授信编号
        String ratCode = rationVO.getRatCode();
        if (StringUtils.isNotBlank(ratCode)) {
            input.getFormdata().put("likesRatCode", ratCode);
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
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);//授信审批通过
        //授信单位
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.IN, "corpId", rationVO.getCorpId());
            model.addAttribute("corpId", rationVO.getCorpId());
        }
        if (StringUtils.isNotBlank(rationVO.getBankCode())) {
            String bankCodes = rationVO.getBankCode().substring(0, rationVO.getBankCode().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCodes);
            model.addAttribute("bankCode", bankCodes);
        }
        //授信品种
        List<String> itemCodeArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getItemCode())) {
            String[] split = rationVO.getItemCode().split(",");
            itemCodeArr.addAll(Arrays.asList(split));
            String substring = rationVO.getItemCode().substring(0, rationVO.getItemCode().length() - 1);
            input.getFormdata().put("INs_itemCode", substring);
            model.addAttribute("itemCode", substring);
        }
        // 授信模式
        if (StringUtils.isNotBlank(rationVO.getRatMode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", rationVO.getRatMode());
            model.addAttribute("ratMode", rationVO.getRatMode().trim());
        }
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_ADD_QUERY, getUserId());

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
        return "cms/rat/bkration/listRatBkrationAdd";
    }

    /**
     * 银行授信额度追加导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "银行授信额度追加导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkRationAddBank")
    public void exportListRatBkRationAddBank(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "银行授信额度追加表.xls";
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
        //授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        if (!StringUtils.isEmpty(ratType)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
            model.addAttribute("ratType", ratType);
        }
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
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
        if (StringUtils.isNotBlank(rationVO.getCorpId())) {
            String corpIds = rationVO.getCorpId().substring(0, rationVO.getCorpId().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "corpId", corpIds);
            model.addAttribute("corpId", corpIds);
        }
        if (StringUtils.isNotBlank(rationVO.getBankCode())) {
            String bankCodes = rationVO.getBankCode().substring(0, rationVO.getBankCode().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCodes);
            model.addAttribute("bankCode", bankCodes);
        }
        List<String> itemCodeArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(rationVO.getItemCode())) {
            String[] split = rationVO.getItemCode().split(",");
            itemCodeArr.addAll(Arrays.asList(split));
            model.addAttribute("itemCode", rationVO.getItemCode().substring(0, rationVO.getItemCode().length() - 1));
        }
        if (StringUtils.isNotBlank(rationVO.getRatMode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", rationVO.getRatMode());
            model.addAttribute("ratMode", rationVO.getRatMode().trim());
        }
        //授信审批通过
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, ResourceConstant.CMS_RATION_ADD_QUERY, getUserId());
        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度追加表", CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
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
     * 集团授信追加信息表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询集团授信追加信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/cplist")
    public String cplist(Model model, Integer start, Integer len, RatBkrationVO rationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        //查询参数设置
        setSearchParams(model, input, rationVO);

        DataTablesOutput output = ratBkrationAddService.queryCpRationAddData(input, RationConstant.CMS_RATION_CPADD_QUERY, getUserId());

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
        return "cms/rat/bkration/listCpRatBkrationAdd";
    }

    /**
     * 集团授信额度追加导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "集团授信额度追加导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatBkRationAddCorp")
    public void exportListRatBkRationAddCorp(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "集团授信额度追加表.xls";
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
        DataTablesOutput output = ratBkrationAddService.queryCpRationAddData(input, ResourceConstant.CMS_RATION_CPADD_QUERY, getUserId());

        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "集团授信额度追加表", CommonConstant.NO, CommonConstant.YES, CommonConstant.NO, CommonConstant.NO);
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
        // 授信单位
        String corpId = rationVO.getCorpId();
        if (!StringUtils.isEmpty(corpId)) {
            input.addFilter(MatchTypeEnum.IN, "corpId", corpId);
            model.addAttribute("corpId", corpId);
        }
        // 额度使用单位
        String disCorpId = rationVO.getDisCorp();
        if (!StringUtils.isEmpty(disCorpId)) {
            input.addFilter(MatchTypeEnum.IN, "disCorp", disCorpId);
            model.addAttribute("disCorpId", disCorpId);
        }
        // 授信合同号
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        // 授信编号
        String ratCode = rationVO.getRatCode();
        if (!StringUtils.isEmpty(ratCode)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratCode);
            model.addAttribute("ratCode", ratCode);
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
            input.getFormdata().put("INs_itemCode", itemCode);
            model.addAttribute("itemCode", itemCode);
        }
        // 授信开始时间段最小时间
        if (null != rationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
            model.addAttribute("gBeginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
        }
        // 授信开始时间段最大时间
        if (null != rationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
            model.addAttribute("lBeginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
        }
        // 授信结束时间段最小时间
        if (null != rationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(rationVO.getgEndDate()));
            model.addAttribute("gEndDate", DateUtils.formatDate(rationVO.getgEndDate()));
        }
        // 授信结束时间段最大时间
        if (null != rationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(rationVO.getlEndDate()));
            model.addAttribute("lEndDate", DateUtils.formatDate(rationVO.getlEndDate()));
        }
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);//授信审批通过
    }

    /**
     * 授信追加信息表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ratBkrationAddService.getDataTables(dataTablesInput);
    }
    /**
     * 授信追加信息表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatBkrationAdd ratBkrationAdd, Model model) {
        if (StringUtils.isNotEmpty(ratBkrationAdd.getRatCode())) {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.getRatRationAddInfo(ratBkrationAdd.getRatCode());
            model.addAttribute("ratBkrationPage", ratBkration);
            // todo:挂接了保证金显示保证金信息
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            model.addAttribute("dto", dto);
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_BANK);// 银行授信额度追加
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "addRatBkrationAdd";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addRatBkrationAddOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 授信追加信息表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatBkrationAdd rationAdd, Model model) {
        if (StringUtils.isNotEmpty(rationAdd.getId())) {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(rationAdd.getId());
            model.addAttribute("ratBkrationPage", ratBkration);
            // todo:如果挂接保证金将信息显示在页面上
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            FcBondApplyDTO cdto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatAddCode());// 查询挂接的保证金占用信息
            if (dto != null) {
                if (cdto != null) {
                    dto.setApplyMoney(cdto.getApplyMoney());
                } else {
                    dto.setApplyMoney(new BigDecimal(0));
                }
                model.addAttribute("dto", dto);
            }
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_BANK);// 银行授信额度追加
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "updateRatBkrationAdd";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateRatBkrationAddOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 集团授信追加信息表新增页面跳转
     *
     * @return
     */
    @GetMapping("/cpadd")
    public String cpadd(RatBkrationAdd ratBkrationAdd, Model model) {
        if (StringUtils.isNotEmpty(ratBkrationAdd.getRatCode())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.initCpRationAddData(ratBkrationAdd.getRatCode(),
                    ratBkrationAdd.getCorpId());
            model.addAttribute("ratBkrationPage", ratBkration);
            // todo:挂接了保证金显示保证金信息
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            model.addAttribute("dto", dto);
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团授信额度追加
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "addCpRatBkrationAdd";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addCpRatBkrationAddOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 授信追加信息表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/cpupdate")
    public String cpupdate(RatBkrationAdd rationAdd, Model model) {
        if (StringUtils.isNotEmpty(rationAdd.getId())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            model.addAttribute("ratBkrationPage", ratBkration);
            model.addAttribute("addCode", rationAdd.getId());
            // todo:如果挂接保证金将信息显示在页面上
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatCode());
            FcBondApplyDTO cdto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    rationAdd.getAddCode());// 查询挂接的保证金占用信息
            if (cdto != null) {
                dto.setApplyMoney(cdto.getApplyMoney());
            }
            model.addAttribute("dto", dto);
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团授信额度追加
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "updateCpRatBkrationAdd";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateCpRatBkrationAddOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 银行授信追加信息列表查询
     *
     * @return
     */
    @LogMessage(content = "银行授信追加信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public String addList(RatBkrationAddListVO addVO, Model model, Integer start) {
        queryRationAddData(addVO, model, start, RationConstant.RAT_ADD_FLAG_BANK, RationConstant.CMS_RATION_ADD_QUERY);// 银行授信追加
        return "cms/rat/bkration/listRatBkrationAdded";
    }
    /**
     * 功能介绍 : 查询授信追加明细。<br>
     * <b>日期：</b>2017年8月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void queryRationAddData(RatBkrationAddListVO addVO, Model model, Integer start, String flag,
                                   String resource) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(addVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", addVO.getCorpId());// 单位
        }
        input.addFilter(MatchTypeEnum.EQ, "ratCode", addVO.getRatCode());// 授信录入主键编号
        input.addFilter(MatchTypeEnum.EQ, "flag", flag);// 银行授信追加
        DataTablesOutput output = ratBkrationAddService.queryRationAddData(input, resource, getUserId());
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
        result.put("total", pageCount);
        result.put("curr", start);
        model.addAttribute("addVO", addVO);
        model.addAttribute("model", result);
    }
    /**
     * 集团授信追加信息列表查询
     *
     * @return
     */
    @LogMessage(content = "集团授信追加信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/cpaddList")
    public String cpaddList(RatBkrationAddListVO addVO, Model model, Integer start) {
        queryRationAddData(addVO, model, start, RationConstant.RAT_ADD_FLAG_GROUP,
                RationConstant.CMS_RATION_CPADD_QUERY);// 银行授信追加
        return "cms/rat/bkration/listCpRatBkrationAdded";
    }
    /**
     * 授信追加信息表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信追加信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(id);
            model.addAttribute("ratBkrationPage", ratBkration);
            // todo:以下是挂接的保证信息
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(),
                    ratBkration.getRatAddCode());// 查询挂接的保证金占用信息
            model.addAttribute("dto", dto);
        }
        model.addAttribute("viewFlag", "1");
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationAdd";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationAddOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    /**
     * 集团授信追加信息表详情页
     *
     * @return
     */
    @LogMessage(content = "查询集团授信追加信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/cpview/{id}")
    public String cpview(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationAdd rationAdd = new RatBkrationAdd();
            rationAdd.setId(id);
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            model.addAttribute("ratBkrationPage", ratBkration);
            model.addAttribute("addCode", rationAdd.getId());
            // todo:以下是挂接的保证信息
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkration.getId(), rationAdd.getAddCode());// 查询挂接的保证金占用信息
            model.addAttribute("dto", dto);
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团授信额度追加
        model.addAttribute("viewFlag", "1");
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewCpRatBkrationAdd";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewCpRatBkrationAddOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }
    private void setRatBkrationAddDtls(RatBkRationAddVm listDTO) {
        List<RatBkRationAddDtlVm> dtls = new ArrayList<RatBkRationAddDtlVm>();
        List<RatBkRationAddDtlVm> rationDtls = listDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationAddDtlVm>();
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
                        compFlag = true;// 第一个综合项判断，表示已经录入过综合项，后续都全部录入
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
        // 按照单品种和综合品种排序
        Collections.sort(dtls, new Comparator<RatBkRationAddDtlVm>() {
            public int compare(RatBkRationAddDtlVm o1, RatBkRationAddDtlVm o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        for (RatBkRationAddDtlVm dtlDTO : dtls) {
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                // 所有的综合授信品种都设置同一个授信金额
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
    /**
     * 暂存银行授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "暂存银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationAddService.saveRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 提交银行授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "提交银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationAddService.saveRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 修改暂存银行授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "修改暂存银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationAddService.editRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 修改提交银行授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "修改提交银行授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatBkRationAddVm listDTO) {
        setRatBkrationAddDtls(listDTO);
        listDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationAddService.editRationAdd(listDTO, getUserId(), getUserLoginName());
        return success();
    }
    /**
     * 暂存集团授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "暂存集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpsave")
    @ResponseBody
    public AjaxJson cpsave(RatBkRationAddVm listDTO) {
        save(listDTO);
        return success();
    }
    /**
     * 提交集团授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "提交集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpsubmit")
    @ResponseBody
    public AjaxJson cpsubmit(RatBkRationAddVm listDTO) {
        submit(listDTO);
        return success();
    }
    /**
     * 修改暂存集团授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "修改暂存集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpeditSave")
    @ResponseBody
    public AjaxJson cpeditSave(RatBkRationAddVm listDTO) {
        editSave(listDTO);
        return success();
    }
    /**
     * 修改提交集团授信追加信息表
     *
     * @return
     */
    @LogMessage(content = "修改提交集团授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/cpeditSubmit")
    @ResponseBody
    public AjaxJson cpeditSubmit(RatBkRationAddVm listDTO) {
        editSubmit(listDTO);
        return success();
    }
    /**
     * 删除授信追加信息表
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationAdd ratBkrationAdd = ratBkrationAddService.get(id);
        ratBkrationAdd.setStatus(RationConstant.STATUS_DELETE);
        ratBkrationAddService.updateEntity(ratBkrationAdd);
        // todo:以下代码是更新占用的保证金信息为"已删除"状态
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ratBkrationAdd.getRatCode(),
                ratBkrationAdd.getAddCode());// 查询挂接的保证金占用信息
        if (dto != null) {// 不为空则说明挂接了保证金
            dto.setStatus(FcBondConstant.STATUE_DEL);
            fcBondHookService.updateFcBondApply(dto);// 更新占用的保证金信息为"已删除"状态
        }
        // 删除挂接保证金占用信息的代码结束
        return success();
    }
    /**
     * 批量删除授信追加信息表
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信追加信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatBkrationAdd ratBkrationAdd = ratBkrationAddService.get(id);
            ratBkrationAddService.deleteEntity(ratBkrationAdd);
        }
        return success();
    }
    /**
     * 授信追加审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_BANK);// 银行授信
        return "cms/rat/bkration/approve/listRatBkrationAddApprove";
    }
    /**
     * 授信追加审批 - 数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input, String flag) {
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        if (RationConstant.RAT_ADD_FLAG_BANK.equals(flag)) {// 银行授信
            return ratBkrationAddService.listApproveOutputData(input);
        } else {
            return ratBkrationAddService.listCpApproveOutputData(input);
        }
    }
    /**
     * 集团授信追加审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCpAddApprove")
    public String listCpAddApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团授信
        return "cms/rat/bkration/approve/listRatBkrationAddApprove";
    }
    /**
     * 授信追加审批 - 详情页面
     */
    @GetMapping("/viewBkrationAddApprove")
    public String viewBkrationAddApprove(String businessId, String taskId, Model model) {
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationAdd";
        RatBkrationAdd rationAdd = ratBkrationAddService.get(businessId, true);
        // 集团授信追加
        if (RationConstant.RAT_ADD_FLAG_GROUP.equals(rationAdd.getFlag())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            model.addAttribute("ratBkrationPage", ratBkration);
            model.addAttribute("addCode", rationAdd.getId());
            if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
                page = "viewCpRatBkrationAddOpen";// 敞口跳转
            } else {
                page = "viewCpRatBkrationAdd";
            }
        } else {// 银行授信追加
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(businessId);
            model.addAttribute("ratBkrationPage", ratBkration);
            if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
                page = "viewRatBkrationAddOpen";// 敞口跳转
            }
        }
        // todo:以下是挂接的保证信息
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(rationAdd.getRatCode(), rationAdd.getAddCode());// 查询挂接的保证金占用信息
        if (dto != null) {
            model.addAttribute("dto", dto);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/viewRatBkrationApprove";
    }
    /**
     * 授信追加手机审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileBkrationAddApprove(String businessId, String taskId, Model model) {
        // 获取授信参数
        // ConfigDTO config = ratBkrationService.getStartOpenRat();//敞口
        String page = "mobileRatBkrationAdd";
        RatBkrationAdd rationAdd = ratBkrationAddService.get(businessId, true);
        // 集团授信追加
        if (RationConstant.RAT_ADD_FLAG_GROUP.equals(rationAdd.getFlag())) {
            RatBkrationCpAddListDTO ratBkration = ratBkrationAddService.editCpRatRationAddInfo(rationAdd);
            model.addAttribute("ratBkrationPage", ratBkration);
            model.addAttribute("addCode", rationAdd.getId());
            // if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            // page = "mobileCpRatBkrationAddOpen";//敞口跳转
            // } else {
            // page = "mobileCpRatBkrationAdd";
            // }
        } else {// 银行授信追加
            RatBkrationAddPageVO ratBkration = ratBkrationAddService.editRatRationAddInfo(businessId);
            model.addAttribute("ratBkrationPage", ratBkration);
            // if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            // page = "viewRatBkrationAddOpen";//敞口跳转
            // }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/" + page;
    }
}
