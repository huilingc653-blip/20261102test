package com.bytter.report.web.financial.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.report.domain.cms.service.CapitalInterestService;
import com.bytter.report.domain.cms.vo.CapitalInterestVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/bi/cms/capitalinterest")
public class CapitalInterestController extends BaseController {
    @Autowired
    private CapitalInterestService capitalInterestService;
    /**
     * 授信额度录入表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "信贷本息统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String listdata(Model model, Integer start, Integer len, CapitalInterestVO capitalInterestVO){
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        DataTablesOutput output= capitalInterestService.listOutputData(input, getCurrentUser().getId(),capitalInterestVO);
        Date beginDate;
        Date endDate;
        List<String> months=new ArrayList<>();
        if(EmptyUtils.isEmpty(capitalInterestVO.getRbeginDate())){
            beginDate=DateUtils.getFirstMonthDate(new Date());
            capitalInterestVO.setRbeginDate(DateUtils.format(beginDate,"yyyy-MM"));
        }else {
            beginDate=DateUtils.parse(capitalInterestVO.getRbeginDate()+"-01","yyyy-MM-dd");
        }
        if(EmptyUtils.isEmpty(capitalInterestVO.getRendDate())){
           endDate=DateUtils.getLastMonthDate(DateUtils.addMonth(beginDate,2));
           capitalInterestVO.setRendDate(DateUtils.format(endDate,"yyyy-MM"));
        }else {
            endDate=DateUtils.parse(capitalInterestVO.getRendDate(),"yyyy-MM");
            endDate=DateUtils.getLastMonthDate(endDate);
        }
        //未还本机利息录入处理
        capitalInterestService.interestMake(beginDate,endDate,months,output);
        Map<String, Object> result = new HashMap<String, Object>();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            //整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            //否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }

        model.addAttribute("months",months);
        result.put("list", output.getData());
        result.put("total", pageCount);
        result.put("resultCount", resultCount);
        result.put("curr", start);
        result.put("pageSize", pageSize);
        model.addAttribute("vo",capitalInterestVO);
        model.addAttribute("model", result);
        return "cms/listCapitalInterest";
    }

    /**
     * 授信延期导出
     *
     * @param request  请求
     * @param response 响应
     * @param interestVO 查询条件
     * @param model    model
     */
    @LogMessage(content = "信贷本息统计表导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, CapitalInterestVO interestVO, Model model) throws Exception {
        // 导出的文件名
        String fileName = "信贷本息统计表.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(interestVO.getExportType())) {
            // 导出当前页
            if (null == interestVO.getStart()) {
                interestVO.setStart(1);
            }
            if (null == interestVO.getPageSize()) {
                interestVO.setPageSize(10);
            }
            input.setStart((interestVO.getStart() - 1) * interestVO.getPageSize());
            input.setLength(interestVO.getPageSize());
        } else {
            input.setStart(1);
            input.setLength(99999999);
        }
        DataTablesOutput output = capitalInterestService.listOutputData(input, getCurrentUser().getId(), interestVO);
        Date beginDate;
        Date endDate;
        List<String> months=new ArrayList<>();
        if(EmptyUtils.isEmpty(interestVO.getRbeginDate())){
            beginDate=DateUtils.getFirstMonthDate(new Date());
            interestVO.setRbeginDate(DateUtils.format(beginDate,"yyyy-MM"));
        }else {
            beginDate=DateUtils.parse(interestVO.getRbeginDate()+"-01","yyyy-MM-dd");
        }
        if(EmptyUtils.isEmpty(interestVO.getRendDate())){
            endDate=DateUtils.getLastMonthDate(DateUtils.addMonth(beginDate,2));
            interestVO.setRendDate(DateUtils.format(endDate,"yyyy-MM"));
        }else {
            endDate=DateUtils.parse(interestVO.getRendDate(),"yyyy-MM");
            endDate=DateUtils.getLastMonthDate(endDate);
        }
        //未还本机利息录入处理
        capitalInterestService.interestMake(beginDate,endDate,months,output);
        //导出数据
        HSSFWorkbook workbook = capitalInterestService.export(months, output);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
