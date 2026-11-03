package com.bytter.credential.web.international.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.ArrivalLetterService;
import com.bytter.credential.domain.international.service.ReceiveLetterService;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICommonUtilService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/international/credit/arrivalletter")
public class ArrivalLetterController extends BaseController {
    @Autowired
    private ArrivalLetterService arrivalLetterService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @Autowired
    private ReceiveLetterService receiveLetterService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService excrateAdjustService;
    @DubboReference(check = false)
    private ICommonUtilService commonUtilService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;


    /**
     * 交单登记列表
     */
    @GetMapping("/listArrival")
    public String listArrival(Model model) {
        return "international/credit/receive/listRegisterArrival";
    }

    /**
     * 功能介绍 :交单登记列表<br>
     *
     * @author zhengxn
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return arrivalLetterService.queryAll(dataTablesInput, getCurrentUser());
    }

    /**
     * 登记-页面跳转
     *
     * @param id    主键id
     * @param model
     * @return
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, Model model) {
        LcEnrol lcEnrol = receiveLetterService.get(id);
        String newCreditNo = arrivalLetterService.getNextArrivalNo(lcEnrol.getCreditNo());
        BtCurrencyDTO dto = iBtCurrencyService.getObjectById(lcEnrol.getIssuingCurId());
        model.addAttribute("newDate", DateUtils.formatDate(new Date()));
        model.addAttribute("creditNo", lcEnrol.getCreditNo());
        model.addAttribute("newCreditNo", newCreditNo);
        model.addAttribute("expiryDate", DateUtils.formatDate(lcEnrol.getExpiryDate()));
        model.addAttribute("curName", dto.getCurName());
        model.addAttribute("curId", lcEnrol.getIssuingCurId());
        model.addAttribute("lcId", lcEnrol.getId());
        model.addAttribute("corpId", lcEnrol.getCorpId());
        model.addAttribute("corp", corpService.get(lcEnrol.getCorpId()));
        model.addAttribute("lcType", lcEnrol.getLcInter());
        if (StringUtils.isEmpty(lcEnrol.getConvertCurId())) {
            lcEnrol.setConvertCurId(iBtCurrencyService.getRMBCurrency().getId());
        }
        BigDecimal exchange = excrateAdjustService.getExchangeRateBy(lcEnrol.getIssuingCurId(), lcEnrol.getConvertCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        if (null == exchange) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
        model.addAttribute("exchangeRate", exchange);
        model.addAttribute("beneficiary", lcEnrol.getBeneficiary());
        model.addAttribute("lcEnrol", lcEnrol);
        return "international/credit/receive/addReceiveArrival";
    }

    /**
     * 交单登记修改列表
     *
     * @param model
     * @return
     */
    @GetMapping("/listArrivalUpdate")
    public String listArrivalUpdate(Model model) {
        return "international/credit/receive/listRegisterArrivalUpdate";
    }

    /**
     * 修改-页面跳转
     *
     * @param id    主键id
     * @param model
     * @return
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        LcBillArrival arrival = arrivalLetterService.get(id);
        LcEnrol lcEnrol = receiveLetterService.get(arrival.getLcId());
        BtCurrencyDTO dto = iBtCurrencyService.getObjectById(arrival.getCurId());
        model.addAttribute("newDate", DateUtils.formatDate(new Date()));
        model.addAttribute("arrival", arrival);
        model.addAttribute("creditNo", lcEnrol.getCreditNo());
        model.addAttribute("curName", dto.getCurName());
        model.addAttribute("id", id);
        model.addAttribute("lcInter", lcEnrol.getLcInter());
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("corp", corpService.get(lcEnrol.getCorpId()));
        return "international/credit/receive/updateReceiveArrival";
    }

    /**
     * 功能介绍 :交单登记列表
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/updateDataTable")
    @ResponseBody
    public DataTablesOutput updateDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return arrivalLetterService.queryUpdate(dataTablesInput, getCurrentUser());
    }

    /**
     * 登记-入库 提交
     *
     * @param entity 实体
     * @return
     */
    @LogMessage(content = "添加收证登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(LcBillArrival entity) {
        arrivalLetterService.tempOrSubmit(entity, getCurrentUser());
        return success();
    }

    /**
     * 根据id删除
     *
     * @param id 主键
     * @return
     */
    @LogMessage(content = "删除收到信用证登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        arrivalLetterService.delLcEnrol(id, getCurrentUser());
        return success();
    }

    /**
     * 清零收到信用证登记
     *
     * @param id 主键id
     * @return
     */
    @LogMessage(content = "清零收到信用证登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/reset/{id}")
    @ResponseBody
    public AjaxJson reset(@PathVariable("id") String id) {
        arrivalLetterService.reset(id);
        return success();
    }

    /**
     * 加载收到信用证的INFO信息
     *
     * @param id    主键id
     * @param flag  是否显示关闭按钮标志
     * @param model
     * @return
     */
    @LogMessage(content = "查询交单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewArrovalInfo")
    public String viewArrovalInfo(String id, String flag, Model model) {
        if (EmptyUtils.isNotEmpty(id) && !"null".equals(id)) {
            LcBillArrival arrival = arrivalLetterService.get(id);

            LcBillArrivalVO lcBillArrivalVO = new LcBillArrivalVO();
            BeanUtils.copyProperties(arrival, lcBillArrivalVO);
            Map<String, String> mapCur = commonUtilService.mapListCurrency();
            lcBillArrivalVO.setCurName(mapCur.get(lcBillArrivalVO.getCurId()) != null ? (String) mapCur.get(lcBillArrivalVO.getCurId()) : "");
            LcEnrol lcEnrol = receiveLetterService.get(arrival.getLcId());
            model.addAttribute("arrival", lcBillArrivalVO);
            model.addAttribute("creditNo", lcEnrol.getCreditNo());
            model.addAttribute("lcInter", lcEnrol.getLcInter());
            model.addAttribute("flag", flag);
            model.addAttribute("lcEnrol", lcEnrol);
        }
        return "international/credit/receive/viewReceiveArrival";
    }

    /**
     * 功能介绍 :收证修改/打回
     *
     * @param lcBillArrival 到单实体
     * @return
     */
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(LcBillArrival lcBillArrival) {
        arrivalLetterService.saveOrUpdate(lcBillArrival, getCurrentUser());
        return success();
    }

    /**
     * 交单报表查询
     *
     * @param model
     * @return
     */
    @GetMapping("/listReportArrival")
    public String listReportArrival(Model model) {
        return "international/report/listReportArrival";
    }

    /**
     * 功能介绍 :交单报表查询<br>
     *
     * @author zhengxn
     */
    @PostMapping("/reportDatatable")
    @ResponseBody
    public DataTablesOutput reportDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return arrivalLetterService.reportDatatable(dataTablesInput, getCurrentUser());
    }

    /**
     * 交单审批页面
     *
     * @param flowStatus 流程状态
     * @param model
     * @return
     */
    @GetMapping("/arrivalApproveList")
    public String arrivalApproveList(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "international/credit/receive/approve/listArrivalApprove";
    }

    /**
     * 功能介绍 :交单审批页面table表<br>
     *
     * @author zhengxn
     */
    //@RequiresPermissions(ResourceConstant.CMS_PROVIDE_DEFERRED_APPLY_QUERY)
    @PostMapping("/getDataInfoApproveTables")
    @ResponseBody
    public DataTablesOutput getDataInfoApproveTables(@RequestBody DataTablesInput dataTablesInput) {
        return arrivalLetterService.getDataInfoApproveTables(dataTablesInput);
    }

    /**
     * 申请审批 详情页面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            LcBillArrival info = arrivalLetterService.get(businessId);
            model.addAttribute("info", info);
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "international/credit/receive/approve/viewArrivalApprove";
    }

    /**
     * 手机审批
     *
     * @param id
     * @param flag
     * @param model
     * @return
     */
    @LogMessage(content = "手机审批", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileViewReceiveArrival")
    public String MobileViewArrovalInfo(String id, String flag, String businessId, Model model) {
        if (EmptyUtils.isNotEmpty(businessId) && !"null".equals(businessId)) {
            LcBillArrival arrival = arrivalLetterService.get(businessId);
            LcEnrol lcEnrol = receiveLetterService.get(arrival.getLcId());
            LcBillArrivalVO lcBillArrivalVO = new LcBillArrivalVO();
            BeanUtils.copyProperties(arrival, lcBillArrivalVO);

            model.addAttribute("arrival", lcBillArrivalVO);
            model.addAttribute("creditNo", lcEnrol.getCreditNo());
            model.addAttribute("lcInter", lcEnrol.getLcInter());
            model.addAttribute("flag", flag);
        }
        return "international/credit/receive/approve/mobileViewReceiveArrival";
    }

    /**
     * 查询合计交单金额
     *
     * @return
     * @Param id
     */
    @PostMapping("/getTotalMoney")
    @ResponseBody
    public AjaxJson getTotalMoney(String id) {
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        json.setObj(arrivalLetterService.getTotalMoney(id));
        return json;
    }

    /**
     * 导出交单查询数据
     *
     * @param queryParam 查询参数
     * @param request    请求参数
     * @param response   相应参数
     * @param model      model对象
     * @throws Exception 异常
     */
    @PostMapping("/exportArrival")
    public void exportArrival(LcBillArrivalVO queryParam, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String filename = "交单查询.xls";
        if (ServletUtils.isIE(request)) {// 判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        input.setStart(1);
        input.setLength(99999999);
        // 设置查询参数
        this.setSearchParams(model, input, queryParam);
        // 查询月度贷款明细表
        DataTablesOutput output = arrivalLetterService.reportDatatable(input, getCurrentUser());
        //导出数据
        HSSFWorkbook workbook = arrivalLetterService.exportArrival(output.getData());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 查询参数设置
     *
     * @param model
     * @param input
     * @param queryParam
     */
    private void setSearchParams(Model model, DataTablesInput input, LcBillArrivalVO queryParam) {
        model.addAttribute("queryParam", queryParam);
        // 单位
        if (!StringUtils.isEmpty(queryParam.getCorpId())) {
            input.addFilter(MatchTypeEnum.IN, "corpId", queryParam.getCorpId());
        }
        // 信用证类型
        if (!StringUtils.isEmpty(queryParam.getLcInter())) {
            input.addFilter(MatchTypeEnum.EQ, "lcInter", queryParam.getLcInter());
        }
        // 信用证号
        if (!StringUtils.isEmpty(queryParam.getLcId())) {
            input.addFilter(MatchTypeEnum.LIKE, "creditNo", queryParam.getLcId());
        }
        // 币别
        if (!StringUtils.isEmpty(queryParam.getCurId())) {
            input.addFilter(MatchTypeEnum.EQ, "curId", queryParam.getCurId());
        }
        // 交单日期-开始日期
        if (null != queryParam.getPaymentStartDate()) {
            input.getFormdata().put("ged_paymentDate", DateUtils.formatDate(queryParam.getPaymentStartDate()));
        }
        // 交单日期-结束日期
        if (null != queryParam.getPaymentEndDate()) {
            input.getFormdata().put("led_paymentDate", DateUtils.formatDate(queryParam.getPaymentEndDate()));
        }
    }
}
