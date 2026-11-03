package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillDiscountReleaseService;
import com.bytter.bill.domain.bill.service.AbRatUrationService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Title: Controller
 * @Description:贴现额度释放Controller
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abBillDiscountRelease", tags = "贴现额度释放")
@RestController("v1AbBillDiscountReleaseController")
@RequestMapping("/v1-preposebill/bill/accebill/abBillDiscountRelease")
public class AbBillDiscountReleaseController extends BaseController {
    @Autowired
    private AbBillDiscountReleaseService abBillDiscountReleaseService;

    @Autowired
    private AbRatUrationService abRatUrationService;

    /**
     * 贴现额度释放页面初始化
     */
    @LogMessage(content = "贴现额度释放", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    @ApiOperation("贴现额度释放列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        ConcurrentModel model = new ConcurrentModel();
        Integer isDisCountCredit = abRatUrationService.isDisCountCredit();
        Integer isCredit = abRatUrationService.isRelateRat() == false ? 0 : 1;
        model.addAttribute("isCredit", isCredit);
        model.addAttribute("isDisCountCredit", isDisCountCredit);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 贴现释放列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("贴现额度释放分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.getFormdata().put("checkboxHide", "1");
        DataTablesOutput output = abBillDiscountReleaseService.listAbBillDiscountAccount(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量确认释放额度
     */
    @LogMessage(content = "批量确认释放额度", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量确认释放额度")
    public AjaxJson batchSubmitAbBillDiscountAccount(@RequestBody AbBillDiscountAccountVO abBillDiscountAccountVO) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillDiscountReleaseService.batchSubmitAbBillDiscountAccount(abBillDiscountAccountVO,
                this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 单笔确认
     */
    @LogMessage(content = "单笔确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/sure")
    @ApiOperation("单笔确认释放额度")
    public AjaxJson sure(String vvid, String id) {
        AjaxJson ajaxJson = AjaxJson.success();
        boolean flag = abBillDiscountReleaseService.signRelease(vvid, id, this.getCurrentUser());
        ajaxJson.setSuccess(flag);
        return ajaxJson;
    }
}