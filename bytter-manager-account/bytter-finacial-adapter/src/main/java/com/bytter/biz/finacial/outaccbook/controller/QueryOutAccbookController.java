package com.bytter.biz.finacial.outaccbook.controller;
import com.bytter.biz.finacial.outaccbook.service.QueryOutAccbookService;
import com.bytter.biz.finacial.outaccbook.vo.FcOutAccbookVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterMeasureUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
/**
 * @author zhengxn
 * @version 1.0
 * @Title: 表外财务--报表系列
 * @Description: 表外流水账主档表
 * @date 2017-07-05 15:40:08
 */
@Controller
@RequestMapping("/finacial/outaccbook/queryoutaccbook")
public class QueryOutAccbookController extends BaseController {
    @Autowired
    private QueryOutAccbookService fcOutAccbookService;
    /**
     * 表外流水账列表 页面跳转
     **/
    @LogMessage(content = "查询表外流水账主档表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/outaccbook/listFcOutAccbook";
    }
    /**
     * 表外流水账 table列表
     **/
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcOutAccbookService.listAll(dataTablesInput, FinacialResourceConstant.FINACIAL_OUTACCBOOK_QUERY, getUserId());
    }
    /**
     * 表外流水账主档表详情页
     **/
    @LogMessage(content = "查询表外流水账主档表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcOutAccbookVo FcOutAccbookVo = fcOutAccbookService.view(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        model.addAttribute("date", date);
        model.addAttribute("FcOutAccbookVo", FcOutAccbookVo);
        return "finacial/outaccbook/FcOutAccbookCheck";
    }
    /***记帐凭证查询**/
    @GetMapping("/voucherlist")
    public String voucherList(Model model) {
        return "finacial/outaccbook/listVoucher";
    }
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_VOUCHERLIST_QUERY)
    @PostMapping("/vdataTable")
    @ResponseBody
    public DataTablesOutput vdataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcOutAccbookService.listAll(dataTablesInput, FinacialResourceConstant.FINACIAL_OUTACCBOOK_VOUCHERLIST_QUERY, getUserId());
    }
    /**
     * 分户账查询页面跳转
     **/
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_DIVIDEDACCOUNTLIST_QUERY)
    @GetMapping("/dividedAccountList")
    public String dividedAccountList(Model model, Integer pageNum, FcOutAccbookVo search, String ss) {
        Map<String, Object> result = fcOutAccbookService.divided(pageNum, search, FinacialResourceConstant.FINACIAL_OUTACCBOOK_DIVIDEDACCOUNTLIST_QUERY, getUserId());
        model.addAttribute("accObj", result.get("accObj"));//根据页码显示的账号信息，包含开始时间结束时间
        model.addAttribute("accSize", result.get("accSize"));//总页数
        model.addAttribute("res", result.get("res"));//table集合
        model.addAttribute("dataCount", result.get("dataCount"));
        model.addAttribute("qcbalance", result.get("qcbalance"));//期初余额
        model.addAttribute("search", search);
        model.addAttribute("pageNum", pageNum);//当前页
        return "finacial/outaccbook/listDividedAccount ";
    }
    /***总账查询**/
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_LEDGERQUERYLIST_QUERY)
    @GetMapping("/ledgerlist")
    public String ledgerQueryList(Model model, FcOutAccbookVo vo, Integer pageNum) {
        model.addAttribute("condition", vo);//查询参数
        Map<String, Object> res = null;
        if (vo.getStartDate() == null && vo.getEndDate() == null) {
            //为空设置默认的最大和最小日期
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            vo.setStartDate(cale.getTime());//当前月的第一天
            // 获取前月的最后一天
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            vo.setEndDate(cale.getTime());//当前月的最后一天
            res = fcOutAccbookService.querySumAccount(pageNum, vo, FinacialResourceConstant.FINACIAL_OUTACCBOOK_LEDGERQUERYLIST_QUERY, getUserId());
        } else {
            res = fcOutAccbookService.querySumAccount(pageNum, vo, FinacialResourceConstant.FINACIAL_OUTACCBOOK_LEDGERQUERYLIST_QUERY, getUserId());
        }
        model.addAttribute("condition", vo);//条件
        model.addAttribute("search", res.get("search"));//科目名称
        model.addAttribute("size", res.get("size"));//总页数
        model.addAttribute("qcCount", res.get("qcCount"));//期初笔数
        model.addAttribute("qcBalance", res.get("qcBalance"));//期初余额
        model.addAttribute("table", res.get("table"));//列表
        model.addAttribute("pageNum", pageNum);//当前页
        return "finacial/outaccbook/ledgerQuery";
    }
    /***余额查询**/
    @GetMapping("/balancelist")
    public String balanceList(Model model) {
        return "finacial/outaccbook/balanceQuery";
    }
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_BALANCELIST_QUERY)
    @PostMapping("/balancetable")
    @ResponseBody
    public DataTablesOutput balanceTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcOutAccbookService.queryBalance(dataTablesInput, FinacialResourceConstant.FINACIAL_OUTACCBOOK_BALANCELIST_QUERY, getUserId());
    }
    /**
     * 日计表查询
     ***/
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_DAILYACCOUNTLIST_QUERY)
    @GetMapping("/dailyaccountlist")
    public String dailyAccountList(Model model, FcOutAccbookVo vo, Integer pageNum) {
        Date date = new Date();//当前日期
        Map<String, Object> res;
        if (vo.getStartDate() == null) {
            vo.setStartDate(date);
            res = fcOutAccbookService.queryOuterCharge(vo, true, pageNum, FinacialResourceConstant.FINACIAL_OUTACCBOOK_DAILYACCOUNTLIST_QUERY, getUserId());
        } else {
            res = fcOutAccbookService.queryOuterCharge(vo, true, pageNum, FinacialResourceConstant.FINACIAL_OUTACCBOOK_DAILYACCOUNTLIST_QUERY, getUserId());
        }
        if (EmptyUtils.isNotEmpty(vo.getIsShowChild()) && !" ".equals(vo.getIsShowChild())) {
            res.put("measureUnit", OuterMeasureUnit.UNIT_MONEY.get());//计量单位
        } else {
            res.put("measureUnit", vo.getMeasureUnit());
        }
        model.addAttribute("search", vo);
        model.addAttribute("list", res.get("list"));
        model.addAttribute("head", res.get("head"));//表头
        model.addAttribute("optDate", res.get("startDate"));
        model.addAttribute("pageNum", pageNum);//当前页
        model.addAttribute("size", res.get("size"));
        return "finacial/outaccbook/dailyAccount";
    }
    /**
     * 科目余额汇总表
     ***/
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_SUBJECTBALANCELIST_QUERY)
    @GetMapping("/subjectbalancelist")
    public String subjectBalanceList(Model model, FcOutAccbookVo vo, Integer pageNum) {
        Map<String, Object> res;
        if (vo.getStartDate() != null) {
            res = fcOutAccbookService.querySubjectBalance(pageNum, vo, FinacialResourceConstant.FINACIAL_OUTACCBOOK_SUBJECTBALANCELIST_QUERY, getUserId());
        } else {
            Date date = new Date();
            vo.setStartDate(date);
            res = fcOutAccbookService.querySubjectBalance(pageNum, vo, FinacialResourceConstant.FINACIAL_OUTACCBOOK_SUBJECTBALANCELIST_QUERY, getUserId());
        }
        model.addAttribute("head", res.get("head"));
        model.addAttribute("list", res.get("lst"));//列表
        model.addAttribute("total", res.get("total"));//合计
        model.addAttribute("search", vo);
        model.addAttribute("pageNum", pageNum);//当前页
        model.addAttribute("size", res.get("size"));
        return "finacial/outaccbook/subjectBalance";
    }
    /**
     * 月计表查询
     **/
    @GetMapping("/monthlybalancelist")
    public String monthlyBalanceList(Model model) {
        return "finacial/outaccbook/monthlyBalance";
    }
    @PostMapping("/monthlybalancetable")
    @ResponseBody
    public DataTablesOutput monthlyBalanceTable(@RequestBody DataTablesInput dataTablesInput) {
        return null;
    }
    /***流水账汇总***/
    @GetMapping("/summarylist")
    public String accountSummaryList(Model model) {
        return "finacial/outaccbook/accountSummary";
    }
    @PostMapping("/accountSummarytable")
    @ResponseBody
    public DataTablesOutput accountSummaryTable(@RequestBody DataTablesInput dataTablesInput) {
        return null;
    }
}
