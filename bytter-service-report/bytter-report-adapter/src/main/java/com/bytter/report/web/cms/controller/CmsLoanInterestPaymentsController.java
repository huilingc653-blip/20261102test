package com.bytter.report.web.cms.controller;


import com.bytter.report.domain.cms.service.CmsLoanInterestPaymentsService;
import com.bytter.report.domain.cms.vo.CmsInterestPayVO;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.constant.ResourceConstant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/cms/report/cmsloaninterestpayments")
public class CmsLoanInterestPaymentsController {

    @Autowired CmsLoanInterestPaymentsService paymentsService;

    /**内部存款利息收支综合情况表**/
    //@RequiresPermissions(ResourceConstant.CMS_INTEREST_PAYMENT_QUERY)
   @RequestMapping("/list")
    public String List( Model model,CmsInterestPayVO vo){
       List<CmsInterestPayVO> list = null;
       if(vo.getSearchStartDate() != null && vo.getSearchEndDate() != null){
           list=  paymentsService.getList(vo);
       }

       model.addAttribute("list",list);
       model.addAttribute("search",vo);
       if(vo.getSearchStartDate()!=null) {
           model.addAttribute("startDate", DateUtils.formatDate(vo.getSearchStartDate()));
           model.addAttribute("endDate", DateUtils.formatDate(vo.getSearchEndDate()));
       }

       return "cms/report/cmsLoanInterest/listLoanInterestPayments";
    }

    //@RequiresPermissions(ResourceConstant.CMS_INTEREST_PAYMENT_QUERY)
    @GetMapping("/getDetails")
    public String getDetails(String sendBillCode,String isRepay,String cmsType,String startDate,String endDate,Model model){
        List<CmsInterestPayVO> list =  paymentsService.getDetails(sendBillCode,isRepay,cmsType,startDate,endDate);
        model.addAttribute("list",list);
        model.addAttribute("isRepay",isRepay);
        model.addAttribute("cmsType",cmsType);
        return "cms/report/cmsLoanInterest/LoanInterestPaymentsDetails";
    }

    @PostMapping("/exportExcel")
    public void exportExcel(CmsInterestPayVO vo, HttpServletResponse response,HttpServletRequest request) throws IOException {
        String filename = "融资利息支出.xls";
        if (isIE(request)) {//判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        HSSFWorkbook workbook = paymentsService.exportExcel(vo);
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }


    /**
     * 判断是否是IE
     * @param request
     * @return
     */
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
