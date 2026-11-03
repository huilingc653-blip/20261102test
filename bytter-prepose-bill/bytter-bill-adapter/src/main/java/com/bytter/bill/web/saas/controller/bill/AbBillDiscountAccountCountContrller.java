package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillDiscountAccountCountService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountCountVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description:贴现到账统计Controller
 * @date 2017-7-21 13:49:49
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilldiscountaccountCount", tags = "贴现到账统计")
@RestController("v1AbBillDiscountAccountCountContrller")
@RequestMapping("/v1-preposebill/bill/accebill/abbilldiscountaccountCount")
public class AbBillDiscountAccountCountContrller {
    @Autowired
    private AbBillDiscountAccountCountService abBillDiscountAccountCountService;

    /**
     * 贴现到账统计页面初始化
     */
    @LogMessage(content = "贴现到账页面统计跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    @ApiOperation("贴现到账统计页面初始化")
    public AjaxJson<Map<String, Object>> list(AbBillDiscountAccountCountVO vo) {
        ConcurrentModel model = new ConcurrentModel();
        if (vo.getIsQuery() != null && "1".equals(vo.getIsQuery())) {
            abBillDiscountAccountCountService.toListInit(model, vo);
        }
        return AjaxJson.success(model.asMap());
    }

    /**
     * 贴现到账统计列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("贴现到账统计分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String corpId) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        if (StringUtils.isNotBlank(corpId)) {
            input.getFormdata().put("ins_corpId", corpId);
        }
        DataTablesOutput output = abBillDiscountAccountCountService.listAbBillDiscountAccountCount(input);
        return AjaxJson.success(output);
    }
}