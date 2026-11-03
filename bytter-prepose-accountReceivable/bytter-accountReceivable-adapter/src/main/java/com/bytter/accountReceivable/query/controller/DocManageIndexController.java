package com.bytter.accountReceivable.query.controller;

import com.bytter.accountReceivable.query.service.DocBiService;
import com.bytter.accountReceivable.query.vo.*;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 首页应收账款分析
 */
@Controller
@RequestMapping("/bi/receivables")
public class DocManageIndexController extends BaseController {

    @Autowired
    private DocBiService docBiService;

    //跳转
    @GetMapping("/index")
    public String list(Model model) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年M月");
        String beginDate = LocalDate.now().minusMonths(6).format(df);
        String endDate = LocalDate.now().minusMonths(1).format(df);
        model.addAttribute("beginDate",beginDate);
        model.addAttribute("endDate",endDate);
        return "query/docManageIndex";
    }

    //应收账款分析
    @PostMapping("/queryArAnalysis")
    @ResponseBody
    public ArAnalysisVo queryArAnalysis(ReceivableIndexQueryVo queryVo){
        return docBiService.queryArAnalysis(queryVo, getCurrentUser());
    }

    //应收款周转分析
    @PostMapping("/queryArTurnoverRate")
    @ResponseBody
    public ArTurnoverVo queryArTurnoverRate(ReceivableIndexQueryVo queryVo){
        return docBiService.queryArTurnoverAnalysis(queryVo, getCurrentUser());
    }

    //应收趋势分析
    @PostMapping("/queryArTrendAnalysis")
    @ResponseBody
    public List<ArTrendVo> queryArTrendAnalysis(ReceivableIndexQueryVo queryVo){
        return docBiService.queryArTrendAnalysis(queryVo, getCurrentUser());
    }

    //应收款占比
    @PostMapping("/queryArRatio")
    @ResponseBody
    public List<ArRateVo> queryArRatio(ReceivableIndexQueryVo queryVo){
        return docBiService.queryArRatio(queryVo, getCurrentUser());
    }

    //应收款单据类型分布
    @PostMapping("/queryArDocTypeDist")
    @ResponseBody
    public List<ArDocTypeDistVo> queryArDocTypeDist(ReceivableIndexQueryVo queryVo){
        return docBiService.queryArDocTypeDist(queryVo, getCurrentUser());
    }

    //账龄分布图
    @PostMapping("/queryArAgingDist")
    @ResponseBody
    public List<ArAgingDistVo> queryArAgingDist(ReceivableIndexQueryVo queryVo){
        return docBiService.queryArAgingDist(queryVo, getCurrentUser());
    }
}
