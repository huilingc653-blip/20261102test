package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillDiscountCollectService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountCollectVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据-贴现收回
 * @author luop
 * @date 2017-09-05
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilldiscountcollect", tags = "票据-贴现收回")
@RestController("v1AbBillDiscountCollectController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilldiscountcollect")
public class AbBillDiscountCollectController extends BaseController {
    @Autowired
    private AbBillDiscountCollectService abBillDiscountCollectService;


    /**
     * 票据贴现收回 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据贴现收回分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountCollectService.listAbBillDiscountCollect(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量贴现收回
     */
    @LogMessage(content = "贴现收回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量贴现收回操作")
    public AjaxJson batchSubmit(AbBillDiscountCollectVo abBillDiscountCollectVo) {
        abBillDiscountCollectService.batchSubmit(abBillDiscountCollectVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("贴现收回审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountCollectService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("贴现收回审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountCollectVo info = abBillDiscountCollectService.getAbBillDiscountCollectVoById(businessId, true);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("贴现收回手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountCollectVo info = abBillDiscountCollectService.getAbBillDiscountCollectVoById(businessId, true);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }
}