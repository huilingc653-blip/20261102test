package com.bytter.credit.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credit.domain.service.YearBankCreditPlanService;
import com.bytter.credit.domain.vo.YearBankCreditPlanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;

/**
 * 银行授信年度计划
 *
 * @author loveCamille
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/cms/bank/credit/plan")
public class YearBankCreditPlanController extends BaseController {

    private final YearBankCreditPlanService yearBankCreditPlanService;

    @GetMapping("/list")
    public String list() {
        return "cms/bank/credit/listYearBankCreditPlan";
    }

    @GetMapping("/addEdit")
    public String addEdit(String id, Model model) {
        if (CharSequenceUtil.isNotBlank(id)) {
            YearBankCreditPlanVO info = yearBankCreditPlanService.getById(id);
            model.addAttribute("info", info);
        } else {
            model.addAttribute("currentYear", DateUtil.thisYear());
        }
        return "cms/bank/credit/addEditYearBankCreditPlan";
    }

    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput<YearBankCreditPlanVO> dataTable(@RequestBody DataTablesInput input) {
        return yearBankCreditPlanService.dataTableQuery(input, getCurrentUser());
    }


    @ResponseBody
    @PostMapping("/deleteByIds")
    @LogMessage(content = "删除银行授信年度计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    public AjaxJson deleteByIds(String ids) {
        if (CharSequenceUtil.isBlank(ids)) return success();
        yearBankCreditPlanService.deleteByIds(Arrays.asList(ids.split(",")));
        return success();
    }

    @ResponseBody
    @PostMapping("/saveOrUpdate")
    public AjaxJson saveOrUpdate(YearBankCreditPlanVO detail) {
        if (CharSequenceUtil.isEmpty(detail.getCorpId())) {
            throw new BusinessException("单位为空");
        }
        if (Objects.isNull(detail.getYear())) {
            throw new BusinessException("年份为空");
        }
        if (CharSequenceUtil.hasBlank(detail.getBankId(), detail.getBankCode(), detail.getBankName())) {
            throw new BusinessException("银行信息为空");
        }
        if (CharSequenceUtil.isBlank(detail.getCreditType())) {
            throw new BusinessException("授信类型为空");
        }
        if (CharSequenceUtil.isBlank(detail.getCurId())) {
            throw new BusinessException("币别为空");
        }
        if (Objects.isNull(detail.getCreditAmount())) {
            throw new BusinessException("金额为空");
        }
        yearBankCreditPlanService.saveOrUpdate(detail, getCurrentUser());
        return success();
    }

}
