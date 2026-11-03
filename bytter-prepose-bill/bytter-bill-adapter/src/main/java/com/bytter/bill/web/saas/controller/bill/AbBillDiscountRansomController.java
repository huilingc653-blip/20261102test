package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillDiscountRansomService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountRansomVo;
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
 * @Description: 票据-贴现赎回申请
 * @author luop
 * @date 2017-09-07
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilldiscountransom", tags = "票据-贴现赎回申请")
@RestController("v1AbBillDiscountRansomController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilldiscountransom")
public class AbBillDiscountRansomController extends BaseController {
    @Autowired
    private AbBillDiscountRansomService abBillDiscountRansomService;



    /**
     * 票据贴现赎回申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据贴现赎回申请分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountRansomService.listAbBillDiscountRansom(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量贴现赎回申请批量提交
     */
    @LogMessage(content = "贴现赎回申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量贴现赎回申请提交")
    public AjaxJson batchSubmit(@RequestBody AbBillDiscountRansomVo abBillDiscountRansomVo) {
        abBillDiscountRansomService.batchSubmit(abBillDiscountRansomVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 贴现赎回申请审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("贴现赎回申请审核列表页面初始化")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        ConcurrentModel model = new ConcurrentModel();
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("贴现赎回申请审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountRansomService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("贴现赎回申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountRansomVo info = abBillDiscountRansomService.getAbBillDiscountRansomVoById(businessId);
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
    @ApiOperation("贴现赎回申请手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountRansomVo info = abBillDiscountRansomService.getAbBillDiscountRansomVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }
}