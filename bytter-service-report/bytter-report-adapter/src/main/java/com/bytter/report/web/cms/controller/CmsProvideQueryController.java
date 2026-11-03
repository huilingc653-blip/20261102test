package com.bytter.report.web.cms.controller;
import com.bytter.report.domain.cms.service.CmsProvideQueryService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 银行借款查询报表 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/10/23 0023 <br>
 */
@Controller
@RequestMapping("/bi/cms/report/provide")
public class CmsProvideQueryController extends BaseController {
    @Autowired
    private CmsProvideQueryService queryService;
    /**
     * <br>
     * <b>功能：</b> 查询借款融资情况页面跳转 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/6 0004 <br>
     */
        @LogMessage(content = "查询借款融资情况列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/capitalizeList")
    public String capitalizeList(Model model) {
        model.addAttribute("calcDate", DateUtils.getDate());
        return "cms/capitalizeList";
    }
    /**
     * <br>
     * <b>功能：</b> 查询查询借款融资数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/4 0004 <br>
     */
    @PostMapping("/capitalizeTable")
    @ResponseBody
    public DataTablesOutput capitalizeTable(@RequestBody DataTablesInput input) {
        String calcDateStr = (String) input.getFormdata().get("calcDate");
        String startRepayDateStr = (String) input.getFormdata().get("ged_repayDate");
        String endRepayDateStr = (String) input.getFormdata().get("led_repayDate");
        String startBorrowDateStr = (String) input.getFormdata().get("ged_borrowDate");
        String endBorrowDateStr = (String) input.getFormdata().get("led_borrowDate");
        if(EmptyUtils.isEmpty(calcDateStr)) {
            return new DataTablesOutput();
        }
        Date calcDate = DateUtils.parseDate(calcDateStr);
        Date startRepayDate = null, endRepayDate = null, startBorrowDate = null, endReBorrowDate = null;
        if (EmptyUtils.isNotEmpty(startRepayDateStr)) {
            startRepayDate = DateUtils.parseDate(startRepayDateStr);
        }
        if (EmptyUtils.isNotEmpty(endRepayDateStr)) {
            endRepayDate = DateUtils.parseDate(endRepayDateStr);
        }
        if (EmptyUtils.isNotEmpty(startBorrowDateStr)) {
            startBorrowDate = DateUtils.parseDate(startBorrowDateStr);
        }
        if (EmptyUtils.isNotEmpty(endBorrowDateStr)) {
            endReBorrowDate = DateUtils.parseDate(endBorrowDateStr);
        }
        String curId = (String) input.getFormdata().get("curId");
        String corps=  (String) input.getFormdata().get("corpId");//查询条件
        String bankTypeId = (String) input.getFormdata().get("bankTypeId");
        String plClass = (String) input.getFormdata().get("plClass");
        String plType = (String) input.getFormdata().get("guaWay");
        List<String> plTypeIds = null;
        if(EmptyUtils.isNotEmpty(plType)) {
            plTypeIds = StringUtils.splitToList(",", plType);
        }
        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(corps)) {
            corpIds = StringUtils.splitToList(",", corps);
        }
        if(null == calcDate || EmptyUtils.isEmpty(curId)) {
            return new DataTablesOutput();
        }
        return queryService.queryLoanCapitalize(curId, getCurrentUser(), calcDate, corpIds, startRepayDate, endRepayDate, startBorrowDate, endReBorrowDate, bankTypeId, plClass, plTypeIds);
    }
}
