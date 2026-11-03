package com.bytter.report.web.cms.controller;

import com.bytter.bill.api.bill.service.IAbBillPlanAmtMaintainService;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.cms.service.BankCreditBalanceService;
import com.bytter.report.domain.cms.vo.BankCreditBalanceForm;
import com.bytter.report.domain.cms.vo.BankCreditBalanceVO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 银行授信余额表控制器
 *
 * @author fangcf
 * @date 2025-07-01
 */
@Controller
@RequestMapping("/cms/report/creditBalance")
public class BankCreditBalanceController extends BaseController {

    @DubboReference(check = false)
    private IAbBillPlanAmtMaintainService abBillPlanAmtMaintainService;
    @Autowired
    private BankCreditBalanceService bankCreditBalanceService;

    /**
     * 银行授信余额表列表查询
     *
     * @param form  银行授信余额表表单对象
     * @param model model对象
     * @return 银行授信余额表跳转地址
     */
    @GetMapping("/list")
    public String list(BankCreditBalanceForm form, Model model) {
        List<BankCreditBalanceVO> resultList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(form.getQueryFlag()) && "query".equals(form.getQueryFlag())) {
            // 表单参数校验
            bankCreditBalanceService.formCheck(form.getWeekStartDay(), form.getWeekEndDay());
            // 查询银行授信余额数据
            resultList = bankCreditBalanceService.listBankCreditBalance(form.getCorpId(), form.getWeekStartDay(), form.getWeekEndDay(), this.getCurrentUser());
        }
        model.addAttribute("resultList", resultList);

        // 传入前端
        model.addAttribute("form", form);
        // “周开始日”必须是周一。获取2000年-2099年所有周一日期，然后日期字符串拼接
        String showAllMonday = abBillPlanAmtMaintainService.getMondays();
        model.addAttribute("showAllMonday", showAllMonday);
        return "cms/report/bankCreditBalance/listBankCreditBalance";
    }

    /**
     * 导出银行授信余额表
     *
     * @param form     查询参数
     * @param request  请求参数
     * @param response 相应参数
     * @param model    model对象
     * @throws Exception 异常
     */
    @PostMapping("/export")
    public void export(BankCreditBalanceForm form, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String filename = "银行授信余额表.xls";
        if (ServletUtils.isIE(request)) {// 判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        List<BankCreditBalanceVO> resultList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(form.getQueryFlag()) && "query".equals(form.getQueryFlag())) {
            // 表单参数校验
            bankCreditBalanceService.formCheck(form.getWeekStartDay(), form.getWeekEndDay());
            // 查询银行授信余额数据
            resultList = bankCreditBalanceService.listBankCreditBalance(form.getCorpId(), form.getWeekStartDay(), form.getWeekEndDay(), this.getCurrentUser());
        }
        //导出数据
        HSSFWorkbook workbook = bankCreditBalanceService.exportBankCreditBalance(resultList, form.getWeekStartDay(), form.getWeekEndDay());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
