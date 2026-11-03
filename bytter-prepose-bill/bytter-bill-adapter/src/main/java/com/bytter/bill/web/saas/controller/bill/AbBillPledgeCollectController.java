package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillPledgeCollectService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillPledgeCollectVo;
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
 * @Description: 票据-质押收回
 * @author zhenghj
 * @date 2017-07-17
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillpledgecollect", tags = "票据-质押收回")
@RestController("v1AbBillPledgeCollectController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillpledgecollect")
public class AbBillPledgeCollectController extends BaseController {
    @Autowired
    private AbBillPledgeCollectService abBillPledgeCollectService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;



    /**
     * 票据质押收回 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据质押收回列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPledgeCollectService.listAbBillPledgeCollect(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量质押收回
     */
    @LogMessage(content = "解除质押", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchCollect")
    @ApiOperation("批量质押收回")
    public AjaxJson batchCollect(@RequestBody AbBillPledgeCollectVo abBillPledgeCollectVo) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillPledgeCollectService.batchCollect(abBillPledgeCollectVo, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }



    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("质押收回审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPledgeCollectService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("质押收回审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillPledgeCollectVo info = abBillPledgeCollectService.getAbBillPledgeCollectVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);

            AbAccrecords accrecordsInfo = abAccrecordsService.get(businessId, true);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(accrecordsInfo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()) : null);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/approve/approveAbBillPledgeCollect");
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("手机端质押收回审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillPledgeCollectVo info = abBillPledgeCollectService.getAbBillPledgeCollectVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(model.asMap());
    }
}