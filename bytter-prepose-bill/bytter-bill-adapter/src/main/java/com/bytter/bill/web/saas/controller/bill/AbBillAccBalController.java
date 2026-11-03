package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAccBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据账户余额
 * @author zhenghj
 * @date 2017-10-10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillaccbal", tags = "票据账户余额")
@RestController("v1AbBillAccBalController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillaccbal")
public class AbBillAccBalController extends BaseController {
    @Autowired
    private AbBillAccBalService abBillAccBalService;

    /**
     * 票据账户余额 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("票据账户余额分页列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : input.getFormdata().get("ins_corpId").toString();
        String balDate = (input.getFormdata().get("ged_balDate") == null || "".equals(input.getFormdata().get("ged_balDate")))
                ? "2010-01-01" : input.getFormdata().get("ged_balDate").toString();
        //不通过余额表查数据
        //abBillAccBalService.resetAbBillAccBal(corpIds,"2010-01-01",this.getCurrentUser(),false);
        DataTablesOutput out = abBillAccBalService.searchBalance(corpIds, this.getCurrentUser(), input, AccebillConstant.AB_BILL_ACCBAL_QUERY_CORPOP);
        //DataTablesOutput out1 = abBillAccBalService.listAbBillAccBalDao(dataTablesInput);
        return AjaxJson.success(out);
    }

    /**
     * 票据账户余额重新生成页面初始化
     */
    @LogMessage(content = "跳转票据账户余额重新生成页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("票据账户余额重新生成页面初始化数据")
    @GetMapping("/balReset")
    public AjaxJson<Map<String, Object>> toAbBillAccBalReset() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("dataToday", DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return AjaxJson.success(resultMap);
    }

    /**
     * 票据账户余额重新生成操作
     *
     * @param corpIds   需要重新生成的单位
     * @param beginDate 重新生成余额的开始日期
     */
    @ApiOperation("票据账户余额重新生成")
    @PostMapping("/resetAbBillAccBal")
    public AjaxJson resetAbBillAccBal(String corpIds, String beginDate) {
        abBillAccBalService.resetAbBillAccBal(corpIds, beginDate, this.getCurrentUser(), false);
        return AjaxJson.success();
    }

    /**
     * 票据账户余额指定日期重新生成操作
     *
     * @param corpIds   需要重新生成的单位
     * @param beginDate 重新生成余额的开始日期
     */
    @ApiOperation("票据账户余额按指定日期重新生成")
    @PostMapping("/resetAbBillAccBalByBalDate")
    public AjaxJson resetAbBillAccBalByBalDate(String corpIds, String beginDate) {
        abBillAccBalService.resetAbBillAccBal(corpIds, beginDate, this.getCurrentUser(), true);
        return AjaxJson.success();
    }
}