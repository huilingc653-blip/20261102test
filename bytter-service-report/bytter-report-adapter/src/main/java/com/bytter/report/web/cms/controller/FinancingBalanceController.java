package com.bytter.report.web.cms.controller;
import com.bytter.report.domain.cms.service.FinancingBalanceService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
/**
 * 各融资品种融资余额报表
 *
 * @author zuoy 2020-12-07
 */
@Controller
@RequestMapping("/bi/cms/financingBalance")
public class FinancingBalanceController extends BaseController {
    @Autowired
    public FinancingBalanceService financingBalanceService;
    /**
     * 列表页面
     *
     * @param model
     * @return
     */
        @GetMapping("/list")
    public String list(Model model) {
        return "cms/listFinancingBalance";
    }
    /**
     * 查询
     */
        @PostMapping("/search")
    @ResponseBody
    public Object search(Date queryDate, String curId) {
        return financingBalanceService.search(queryDate, curId, getCurrentUser());
    }
    /**
     * 导出
     *
     * @param request
     * @param response
     * @param queryDate
     * @param curId
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, Date queryDate, String curId) throws Exception {
        financingBalanceService.export(request, response, queryDate, curId, getCurrentUser());
    }
}
