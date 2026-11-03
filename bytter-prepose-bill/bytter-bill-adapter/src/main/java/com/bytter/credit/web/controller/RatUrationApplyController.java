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
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.domain.service.*;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatItemDisVO;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信占用申请表
 * @date 2017-08-19 11:36:28
 */
@Controller
@RequestMapping("/cms/rat/raturationapply")
public class RatUrationApplyController extends BaseController {
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatBkrationDisService ratDisService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private RatBkrationAddService ratAddService;
    @Autowired
    private RatItemCustomService ratItemCustomService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @Autowired
    private RatUrationService ratUrationService;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 授信占用申请表列表 页面跳转
     *
     * @return 授信占用申请表地址
     */
    @LogMessage(content = "查询授信占用申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
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
        // 查询参数设置
        this.setSearchParams(model, input, rationVO);

        DataTablesOutput output;
        if (!StringUtils.isEmpty(rationVO.getCorpId())) {
            output = ratAddService.queryApplyRationAddData(input, rationVO.getCorpId(), RationConstant.CMS_RATION_APPLY_QUERY, getUserId());
            model.addAttribute("corpId", rationVO.getCorpId());
        } else {
            output = new DataTablesOutput();
        }
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
        return "cms/rat/bkration/listRatUrationApply";
    }

    /**
     * 银行额度系统占用导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "银行额度系统占用导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatUrationApply")
    public void exportListRatUrationApply(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "银行额度系统占用表.xls";
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
        DataTablesOutput output;
        if (!StringUtils.isEmpty(rationVO.getCorpId())) {
            output = ratAddService.queryApplyRationAddData(input, rationVO.getCorpId(), RationConstant.CMS_RATION_APPLY_QUERY, getUserId());
            model.addAttribute("corpId", rationVO.getCorpId());
        } else {
            output = new DataTablesOutput();
        }
        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行额度系统占用表", CommonConstant.NO, CommonConstant.YES, CommonConstant.NO, CommonConstant.NO);
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
     * 查询参数设置
     *
     * @param model    model对象
     * @param input    input输入参数
     * @param rationVO 对象
     */
    private void setSearchParams(Model model, DataTablesInput input, RatBkrationVO rationVO) {
        // 授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
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
        String bankName = rationVO.getBankName();
        if (!StringUtils.isEmpty(bankName)) {
            input.addFilter(MatchTypeEnum.LIKE, "bankName", bankName);
            model.addAttribute("bankName", bankName);
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
    }

    /**
     * 授信占用申请表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String ratCode, String corpId) {
        dataTablesInput.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        dataTablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
        dataTablesInput.addColumn("createDate", false, true, null);
        dataTablesInput.addOrder("createDate", false);
        String ratType = RationConstant.RAT_TYPE_0;// 授信种类为银行授信
        if (!StringUtils.isEmpty(ratType)) {
            dataTablesInput.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        }
        return ratUrationApplyService.queryURationApplyData(dataTablesInput, RationConstant.CMS_RATION_APPLY_QUERY,
                getUserId());
    }

    /**
     * 银行授信单据已占用列表跳转
     *
     * @return
     */
    @LogMessage(content = "银行授信单据已占用列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public String addList(RatBkrationPageVO rationVO, Model model, Integer start) {
        RatBkrationDisListDTO listDTO = ratDisService.geBkrationDisListDTOById(rationVO.getRatCode(),
                rationVO.getCorpId());
        model.addAttribute("addVO", listDTO);
        return "cms/rat/bkration/listRatUrationApplyed";
    }

    /**
     * 获取授信分配信息系统授信品种获取可占用的品种
     */
    @PostMapping("/getRationDisSysItem")
    @ResponseBody
    public List<RatSysItemDTO> getRationDisSysItem(String disCode) {
        return disDetailService.getRationDisSysItem(disCode);
    }

    /**
     * 根据编号获取系统授信品种
     */
    @PostMapping("/getRatSysItem")
    @ResponseBody
    public List<RatSysItemDTO> getReliefItemFlag(String sysItemCode) {
        return ratItemCustomService.getRatSysItemByCode(sysItemCode);
    }

    /**
     * 获取授信分配信息系统授信品种获取可占用的品种
     */
    @PostMapping("/getUrationItem")
    @ResponseBody
    public List<RatItemDisVO> getReliefItemFlag(String disCode, String sysItemCode) {
        return ratDisService.getRationDisItemCode(disCode, sysItemCode);
    }

    /**
     * 授信占用申请表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatUrationApply ratApply, Model model) {
        if (StringUtils.isNotEmpty(ratApply.getRatCode())) {
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(ratApply.getRatCode(),
                    ratApply.getCorpId());
            model.addAttribute("ratBkrationPage", pageVO);
        }
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "addRatUrationApply";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addRatUrationApplyOpen";// 敞口跳转
        }
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        model.addAttribute("isOpenRat", ratConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            RatBkration ratBkration = ratBkrationService.get(ratApply.getRatCode());
            model.addAttribute("isHangGuarantee", ratBkration.getIsHangGuarantee());
            if (ratBkration.getIsHangGuarantee() != null
                    && ratBkration.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {
                this.setGuaCotractMsg(ratApply, model);
            }
        }

        String view = "cms/rat/bkration/" + page;
        return view;
    }

    /**
     * 授信占用申请表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatUrationApply ratApply, Model model) {
        if (StringUtils.isNotEmpty(ratApply.getRatCode())) {
            RatUrationApplyListDTO applyDTO = ratUrationApplyService.editRatUrationInfo(ratApply.getId());
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(applyDTO.getOriRatCode(),
                    applyDTO.getCorpId());//授信分配信息
            model.addAttribute("ratBkrationPage", pageVO);
            model.addAttribute("applyDTO", applyDTO);

            RatBkration ratBkration = ratBkrationService.get(ratApply.getRatCode(), true);
            // 获取授信开始时间
            Date beginDate = ratBkration.getBegindate();
            model.addAttribute("beginDate", DateUtils.getDateStr(beginDate, "yyyy-MM-dd"));
            // 授信币种与业务币种不同，则需要通过汇率转换
            if (!applyDTO.getCurCode().equals(applyDTO.getInitCurCode())) {
                BigDecimal exchangeMoneyRate = rateAdjustService.getExchangeRateBy(applyDTO.getInitCurCode(), applyDTO.getCurCode(), beginDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//汇率类型为折算汇率
                model.addAttribute("exchangeMoneyRate", exchangeMoneyRate);
            } else {
                model.addAttribute("exchangeMoneyRate", 1);
            }

            // 业务币种与保证金币种不同，则需要通过汇率转换
            if (!applyDTO.getInitCurCode().equals(applyDTO.getBondCurId())) {
                BigDecimal exchangeBondMoneyRate = rateAdjustService.getExchangeRateBy(applyDTO.getInitCurCode(), applyDTO.getBondCurId(), DateUtils.getDateYMD(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//汇率类型为折算汇率
                model.addAttribute("exchangeBondMoneyRate", exchangeBondMoneyRate);
            } else {
                model.addAttribute("exchangeBondMoneyRate", 1);
            }
        }

        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "updateRatUrationApply";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateRatUrationApplyOpen";// 敞口跳转
        }
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        model.addAttribute("isOpenRat", ratConfig.getValue());

        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();//参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            RatBkration ratBkration = new RatBkration();
            ratBkration = ratBkrationService.get(ratApply.getRatCode());
            model.addAttribute("isHangGuarantee", ratBkration.getIsHangGuarantee());
            if (ratBkration.getIsHangGuarantee() != null && ratBkration.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {
                setGuaCotractMsg(ratApply, model);
            }
        }
        String view = "cms/rat/bkration/" + page;
        return view;
    }

    /**
     * 授信占用申请表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信占用申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, String ratCode, Model model) {
        RatUrationApplyListDTO applyDTO = ratUrationApplyService.editRatUrationInfo(id);
        RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(applyDTO.getOriRatCode(),
                applyDTO.getCorpId());// 授信分配信息
        model.addAttribute("ratBkrationPage", pageVO);
        model.addAttribute("applyDTO", applyDTO);
        model.addAttribute("viewFlag", "1");
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatUrationApply";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatUrationApplyOpen";// 敞口跳转
        }
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        model.addAttribute("isOpenRat", ratConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            if (StringUtils.isNotEmpty(applyDTO.getGuaWay())) {
                String guaWayNames = guaWayServie.getGuaWayNameByCodes(applyDTO.getGuaWay());
                model.addAttribute("guaWayNames", guaWayNames);
            }
        }
        return "cms/rat/bkration/" + page;
    }

    private void setUrationApply(RatUrationApplyDTO applyDTO) {
        // 去掉无效数据
        List<RatUrationApplyListDTO> applyDtls = applyDTO.getApplyDtls();
        if (EmptyUtils.isEmptyList(applyDtls)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_ITEM_NULL);
        }
        List<RatUrationApplyListDTO> dtls = new ArrayList<RatUrationApplyListDTO>();
        for (RatUrationApplyListDTO listDTO : applyDtls) {
            if (StringUtils.isEmpty(listDTO.getItemCode())) {
                continue;
            }
            setUrationApplyList(listDTO);
            listDTO.setRatType(applyDTO.getRatType());
            listDTO.setRatCode(applyDTO.getRatCode());
            listDTO.setDisCode(applyDTO.getDisCode());
            listDTO.setCorpId(applyDTO.getCorpId());
            listDTO.setStatus(applyDTO.getStatus());
            listDTO.setCurCode(applyDTO.getCurCode());
            if (StringUtils.isNotEmpty(applyDTO.getGuaWay())) {
                listDTO.setGuaWay(applyDTO.getGuaWay());
            }
            dtls.add(listDTO);
        }
        applyDTO.setApplyDtls(dtls);
    }

    /**
     * 暂存授信占用申请表
     *
     * @param applyDTO
     * @return
     */
    @LogMessage(content = "添加授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatUrationApplyDTO applyDTO, HttpServletRequest request) {
        applyDTO.setStatus(CommonConstant.STATUE_TEMP);
        setUrationApply(applyDTO);
        ratUrationApplyService.saveRatUrationApply(applyDTO, getUserId());
        return success();
    }

    /**
     * 功能介绍 : 提交授信占用申请。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @LogMessage(content = "提交授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatUrationApplyDTO applyDTO, HttpServletRequest request) {
        applyDTO.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        setUrationApply(applyDTO);
        ratUrationApplyService.saveRatUrationApply(applyDTO, getUserId());
        return success();
    }

    private void setUrationApplyList(RatUrationApplyListDTO applyDTO) {
        // 去掉无效数据
        List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
        List<RatUrationApplyDtlDTO> dtls = new ArrayList<RatUrationApplyDtlDTO>();
        if (!EmptyUtils.isEmptyList(applyDtls)) {
            for (RatUrationApplyDtlDTO listDTO : applyDtls) {
                if (StringUtils.isEmpty(listDTO.getItemCode())) {
                    continue;
                }
                if (null == listDTO.getMaxMoney()) {
                    listDTO.setMaxMoney(BigDecimal.ZERO);
                }
                dtls.add(listDTO);
            }
            applyDTO.setApplyDtls(dtls);
        }
        if (null == applyDTO.getOpenMoney()) {
            applyDTO.setOpenMoney(applyDTO.getMoney());// 默认敞口额度=占用额度
        }
    }

    /**
     * 修改暂存授信占用申请表
     *
     * @param applyDTO
     * @return
     */
    @LogMessage(content = "添加授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatUrationApplyListDTO applyDTO, HttpServletRequest request) {
        applyDTO.setStatus(CommonConstant.STATUE_TEMP);
        setUrationApplyList(applyDTO);
        ratUrationApplyService.updateRatUrationApply(applyDTO, getUserId());
        return success();
    }

    /**
     * 修改提交授信占用申请表
     *
     * @param applyDTO
     * @return
     */
    @LogMessage(content = "修改提交授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatUrationApplyListDTO applyDTO, HttpServletRequest request) {
        applyDTO.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        setUrationApplyList(applyDTO);
        ratUrationApplyService.updateRatUrationApply(applyDTO, getUserId());
        return success();
    }

    /**
     * 释放授信占用申请
     *
     * @return
     */
    @LogMessage(content = "释放授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/release")
    @ResponseBody
    public AjaxJson release(RatUrationApplyListDTO applyDTO, HttpServletRequest request) {
        ratUrationApplyService.updateUrationRelease(applyDTO.getId(), getUserId());
        return success();
    }

    /**
     * 删除授信占用申请表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratUrationApplyService.updateUrationDel(id, getUserId());
        return success();
    }

    /**
     * 授信占用审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信占用发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listSendApprove")
    public String listSendApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/bkration/approve/listRatUrationApplyApprove";
    }

    /**
     * 授信占用审批 - 数据
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
        return ratUrationApplyService.getDataApplyApproveTables(input);
    }

    /**
     * 授信占用审批 - 详情页面
     */
    @GetMapping("/viewApplyApprove")
    public String viewApplyApprove(String businessId, String taskId, Model model) {
        RatUrationApplyListDTO applyDTO = null;
        if (!EmptyUtils.isEmpty(businessId)) {
            applyDTO = ratUrationApplyService.editRatUrationInfo(businessId);
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(applyDTO.getOriRatCode(),
                    applyDTO.getCorpId());// 授信分配信息
            model.addAttribute("ratBkrationPage", pageVO);
            model.addAttribute("applyDTO", applyDTO);
            // 设置担保方式
            String guaWayNames = "";
            if (StringUtils.isNotEmpty(applyDTO.getGuaWay())) {
                guaWayNames = ratUrationApplyService.getGuaWaysName(applyDTO.getGuaWay());
            }
            model.addAttribute("guaWayNames", guaWayNames);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatUrationApplyApprove";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatUrationApplyOpenApprove";// 敞口跳转
        }
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        model.addAttribute("isOpenRat", ratConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            if (StringUtils.isNotEmpty(applyDTO.getGuaWay())) {
                String guaWayNames = guaWayServie.getGuaWayNameByCodes(applyDTO.getGuaWay());
                model.addAttribute("guaWayNames", guaWayNames);
            }
        }
        return "cms/rat/bkration/approve/" + page;
    }

    /**
     * 授信占用审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        RatUrationApplyListDTO applyDTO = null;
        if (!EmptyUtils.isEmpty(businessId)) {
            applyDTO = ratUrationApplyService.editRatUrationInfo(businessId);
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(applyDTO.getOriRatCode(),
                    applyDTO.getCorpId());// 授信分配信息
            model.addAttribute("ratBkrationPage", pageVO);
            model.addAttribute("applyDTO", applyDTO);
            // 设置担保方式
            String guaWayNames = guaWayServie.getGuaWayNameByCodes(applyDTO.getGuaWay());
            model.addAttribute("guaWayNames", guaWayNames);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        model.addAttribute("isOpenRat", ratConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.addAttribute("isGuarantee", guaConfig.getValue());
        // 获取授信参数
        // ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "mobileRatBkrationApply";
        // if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
        // page = "mobileRatBkrationApplyOpen";// 敞口跳转
        // }
        return "cms/rat/bkration/approve/" + page;
    }

    /**
     * 根据授信币别获取可选的业务币别
     */
    @GetMapping("/getBillCurList")
    @ResponseBody
    public List<BtCurrencyDTO> getBillCurList(String ratCurId) {
        List<BtCurrencyDTO> btCurrencyDTOS = new ArrayList<BtCurrencyDTO>();
        BtCurrencyDTO RMBBtCurrencyDTO = iBtCurrencyService.getRMBCurrency();
        if (ratCurId != null) {
            if (!ratCurId.equals(RMBBtCurrencyDTO.getId())) {// 当授信币种为外币时，业务币别只能为相应的外币
                BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(ratCurId);
                btCurrencyDTOS.add(btCurrencyDTO);
            } else if (ratCurId.equals(RMBBtCurrencyDTO.getId())) {// 授信币种为人民币，业务币种为所有的币种
                btCurrencyDTOS = iBtCurrencyService.getBtCurrency();
            }
        }
        return btCurrencyDTOS;
    }

    /**
     * 根据业务币别获取可选的保证金币别
     */
    @GetMapping("/getBondCurList")
    @ResponseBody
    public List<BtCurrencyDTO> getBondCurList(String billCurId, String corpId) {
        List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.getBondCurList(billCurId, corpId);
        return btCurrencyDTOS;
    }

    /**
     * 根据根据业务金额、保证金金额以及是否启用授信敞口计算授信占用金额
     */
    @PostMapping("/getRatUrationMoney")
    @ResponseBody
    public BigDecimal getRatUrationMoney(RatUrationApplyListDTO applyDTO) {
        RatUrationDTO ratUrationDTO = new RatUrationDTO();
        BeanUtils.copyNotNullProperties(applyDTO, ratUrationDTO);
        RatUration ratUration = new RatUration();
        ratUration.setCurCode(ratUrationDTO.getCurCode());// 设置授信币种
        ratUration.setInitCurCode(ratUrationDTO.getInitCurCode());// 设置业务币种
        return ratUrationService.getRatUrationMoney(ratUrationDTO, ratUration).getMoney();
    }

    /**
     * 根据业务币别和业务金额计算折合人民币的金额
     */
    @PostMapping("/getExchangeMoney")
    @ResponseBody
    public BigDecimal getExchangeMoney(String billCurId, String billMoney, String corpId) {
        return ratUrationService.getExchangeMoney(billCurId, billMoney, corpId);
    }

    /**
     * 获取所有担保方式
     *
     * @return
     */
    public String getAllGuaWayCode() {
        String guaWayCodes = "";
        List<GuaWayDTO> guaWayDTOS = guaWayServie.getAllGuaWay();
        if (guaWayDTOS != null || guaWayDTOS.size() > 0) {
            guaWayCodes = guaWayDTOS.stream().map(GuaWayDTO::getCode).collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
        }
        return guaWayCodes;
    }

    /**
     * 设置担保合同相关信息
     */
    public void setGuaCotractMsg(RatUrationApply ratApply, Model model) {
        String allGuaWayCode = this.getAllGuaWayCode();
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(ratApply.getRatCode());
        GuaContractOccupDTO guaContractDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
        model.addAttribute("guaWay", guaContractDTO.getGuaWay());
        model.addAttribute("guaWayName", guaContractDTO.getGuaWayName());
        model.addAttribute("allGuaWayCode", allGuaWayCode);
    }

    @PostMapping("/getNearCurRat")
    @ResponseBody
    public Object getNearCurRat(String curId, String convertCurId, Date date) {
        // TODO 调用货币汇率调整接口，获取折合本位币汇率
        return rateAdjustService.getExchangeRateBy(curId, convertCurId, date,
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT);// 汇率类型为折算汇率
    }

    /**
     * 更新授信分配信息占用额度
     *
     * @param id
     * @return
     */
    @LogMessage(content = "更新授信占用额度", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateRatDis")
    @ResponseBody
    public AjaxJson updateRatDis(String disId, String ratCode) {
        ratUrationApplyService.updateRatUrationApply(disId, ratCode, getUserId());
        return success();
    }
}
