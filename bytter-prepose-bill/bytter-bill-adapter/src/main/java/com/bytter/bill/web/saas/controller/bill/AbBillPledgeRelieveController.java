package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillPledgeRelieveService;
import com.bytter.bill.domain.bill.vo.AbBillPledgeRelieveVo;
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
 * @Description: 票据-解除质押
 * @author zhenghj
 * @date 2017-07-14
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillpledgerelieve", tags = "票据-解除质押")
@RestController("v1AbBillPledgeRelieveController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillpledgerelieve")
public class AbBillPledgeRelieveController extends BaseController {
    @Autowired
    private AbBillPledgeRelieveService abBillPledgeRelieveService;


    /**
     * 票据解除质押 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据解除质押列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPledgeRelieveService.listAbBillPledgeRelieve(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量解除质押
     */
    @LogMessage(content = "解除质押", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchRelieve")
    @ApiOperation("批量解除质押")
    public AjaxJson batchRelieve(@RequestBody AbBillPledgeRelieveVo abBillPledgeRelieveVo) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillPledgeRelieveService.batchRelieve(abBillPledgeRelieveVo, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }



    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("解除质押审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPledgeRelieveService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("解除质押审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillPledgeRelieveVo info = abBillPledgeRelieveService.getAbBillPledgeRelieveVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/approve/approveAbBillPledgeRelieve");
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("手机端解除质押审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillPledgeRelieveVo info = abBillPledgeRelieveService.getAbBillPledgeRelieveVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(model.asMap());
    }
}