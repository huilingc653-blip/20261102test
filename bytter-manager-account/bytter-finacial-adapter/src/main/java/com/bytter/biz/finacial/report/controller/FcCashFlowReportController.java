package com.bytter.biz.finacial.report.controller;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.report.service.FcCashFlowReportService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/finacial/report/cashFlowReport")
public class FcCashFlowReportController extends BaseController {
    @Autowired
    private FcCashFlowReportService cashFlowReportService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    //@RequiresPermissions(ResourceConstant.FINACIAL_CASHFLOW_QUERY)
    @GetMapping("/list")
    public String list(Model model){
        return "finacial/report/ListCashFlowReport";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_CASHFLOW_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input, Model model){
        return cashFlowReportService.datatable(input, getCurrentUser());
    }
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String filename = "现金流量表.xls";
        if (isIE(request)) {//判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        Map<String, Object> params = new HashMap<>();
        getParams(request, params);
        HSSFWorkbook workbook = cashFlowReportService.exportExcel(params);
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
    private void getParams(HttpServletRequest request, Map<String,Object> params){
        String netCode = request.getParameter("netCode");
        String curId = request.getParameter("curId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String isConvert = request.getParameter("isConvert");
        String convertCurId = request.getParameter("convertCurId");
        String rateType = request.getParameter("rateType");
        String convertDate = request.getParameter("convertDate");
        if (StringUtils.isNotEmpty(netCode)) {
            params.put("netCode", netCode);
        }
        if (StringUtils.isNotEmpty(curId)) {
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(startDate)) {
            params.put("startDate", startDate + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(endDate)) {
            params.put("endDate", endDate + " 23:59:59");
        }
        if (StringUtils.isNotEmpty(isConvert)) {
            params.put("isConvert", isConvert);
        }
        if (StringUtils.isNotEmpty(convertCurId)) {
            params.put("convertCurId", convertCurId);
        }
        if (StringUtils.isNotEmpty(rateType)) {
            params.put("rateType", rateType);
        }
        if (StringUtils.isNotEmpty(convertDate)) {
            params.put("convertDate", convertDate);
        }
    }
    /**
     * 判断是否是IE
     * @param request
     * @return
     */
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
    @PostMapping("/getMonthCloseDate")
    @ResponseBody
    public AjaxJson getMonthCloseDate(String netId){
        Calendar monthCloseDate = fcDayCloseService.getMonthCloseDate(netId);
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(DateUtils.formatDate(monthCloseDate,"yyyy-MM-dd"));
        return ajaxJson;
    }
}
