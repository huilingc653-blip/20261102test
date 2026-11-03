package com.bytter.finance.saas.controller.precative;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanDeferred;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanDeferredService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanRepayService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanDeferredVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.domain.provide.entity.ProvideLoanDeferred;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款展期申请表
 * @date 2017-08-03 10:48:22
 */


@Api(value = "/v1-cms/cms/precative/precativeloandeferred", tags = "委托贷款展期申请表")
@RestController("v1CmsPrecativeLoanDeferredController")
@RequestMapping("/v1-cms/cms/precative/precativeloandeferred")
public class CmsPrecativeLoanDeferredController extends BaseController {
    @Autowired
    private CmsPrecativeLoanDeferredService cmsPrecativeLoanDeferredService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private CmsPrecativeLoanRepayService cmsPrecativeLoanRepayService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 委托贷款展期申请跳转
     */
    @GetMapping("/listApply")
    public AjaxJson<Map<String, Object>> list() {
        Map<String, Object> model = new HashMap<>();
        model.put("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return AjaxJson.success(model);
    }

    /**
     * 委托贷款展期申请Table列表
     */
    @PostMapping("/dataTableApply")
    public AjaxJson<DataTablesOutput> dataTableApply(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanDeferredService.queryApply(input,
                ResourceConstant.CMS_PRECATIVE_DEFERRED_APPLY_QUERY, getUserId()));
    }

    /**
     * 委托贷款展期申请页面
     */
    @GetMapping("Apply")
    public AjaxJson<Map<String, Object>> Apply(String voucherNo, String sendNo) {
        Map<String, Object> model = new HashMap<>();
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(voucherNo);
        model.put("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(sendNo);
        model.put("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendNo);// 已还金额
        model.put("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendNo);// 在途金额
        model.put("passageMoney", passageMoney);
        model.put("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return AjaxJson.success(model);
    }

    /**
     * 委托贷款展期保存
     */
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsPrecativeLoanDeferred pld) {
        cmsPrecativeLoanDeferredService.save(pld, getCurrentUser());
        return success();
    }

    /**
     * 委托贷款展期修改/打回Table列表
     */
    @PostMapping("/dataTableUpdate")
    public AjaxJson<DataTablesOutput> dataTableUpdate(@RequestBody V1DataTablesInput dataTablesInput, String pageType) {
            DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanDeferredService.queryTable(input, pageType,
                ResourceConstant.CMS_PRECATIVE_DEFERRED_UPDATE_QUERY, getUserId()));
    }

    /**
     * 委托贷款展期修改/打回页面
     */
    @GetMapping("updatePage")
    public AjaxJson<Map<String, Object>> updatePage(PrecativeLoanDeferredVo pre) {
        Map<String, Object> model = new HashMap<>();
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getVoucherNo());
        model.put("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendNo());
        model.put("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(pre.getSendNo()); // 已还金额
        model.put("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(pre.getSendNo());// 在途金额
        model.put("passageMoney", passageMoney);
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(pre.getBillCode());
        model.put("deferred", deferred);
        model.put("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return AjaxJson.success(model);
    }

    /**
     * 委托贷款展期修改或保存
     */
    @PostMapping("/addUpdate")
    public AjaxJson addUpdate(@RequestBody CmsPrecativeLoanDeferred pld) {
        cmsPrecativeLoanDeferredService.addUpdate(pld, getCurrentUser());
        return success();
    }

    /**
     * 委托贷款展期补录Table表
     */
    @PostMapping("Additional")
    public AjaxJson<DataTablesOutput> Additional(@RequestBody V1DataTablesInput dataTablesInput, String pageType) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanDeferredService.queryTable(input, pageType,
                ResourceConstant.CMS_PRECATIVE_DEFERRED_ADDIT_QUERY, getUserId()));
    }

    /**
     * 委托贷款展期补录提交
     */
    @GetMapping("queryAdditional")
    public AjaxJson<Map<String, Object>> queryAdditional(PrecativeLoanDeferredVo pre) {
        Map<String, Object> model = new HashMap<>();
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getVoucherNo());
        model.put("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendNo());
        model.put("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(pre.getSendNo());// 已还金额
        model.put("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(pre.getSendNo());// 在途金额
        model.put("passageMoney", passageMoney);
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(pre.getBillCode());
        model.put("deferred", deferred);
        model.put("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return AjaxJson.success(model);
    }

    /**
     * 委托贷款展期详情页
     */
    // @LogMessage(content = "查询委托贷款展期申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> view(String sendBillCode, String billCode) {
        Map<String, Object> model = new HashMap<>();
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(sendBillCode);
        model.put("send", send);
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
        model.put("info", info);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendBillCode);// 已还
        model.put("repayMoney", repayMoney);
        if (!EmptyUtils.isEmpty(billCode)) {
            CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(billCode);
            model.put("deferred", deferred);
        }
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendBillCode);// 在途金额
        model.put("passageMoney", passageMoney);
        return AjaxJson.success(model);
    }

    /**
     * 委托贷款展期删除
     */
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsPrecativeLoanDeferredService.deleteDef(id, getUserId());
        return success();
    }

    /**
     * 委托贷款展期处理Table表
     */
    @PostMapping("queryHandle")
    public AjaxJson<DataTablesOutput> queryHandle(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanDeferredService.queryHandle(input, ResourceConstant.CMS_PRECATIVE_DEFERRED_HANDLE_QUERY,
                getUserId()));
    }

    /**
     * 委托贷款展期处理页面
     */
    @GetMapping("handle")
    public AjaxJson<Map<String, Object>> handle(PrecativeLoanDeferredVo pre) {
        Map<String, Object> model = new HashMap<>();
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getVoucherNo());
        model.put("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendNo());
        model.put("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(pre.getSendNo());// 已还金额
        model.put("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(pre.getSendNo());// 在途金额
        model.put("passageMoney", passageMoney);
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(pre.getBillCode());
        model.put("deferred", deferred);
        model.put("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return AjaxJson.success(model);
    }

    /**
     * 多次展期列表跳转
     */
    @GetMapping("/deferredInfolist")
    public AjaxJson<Map<String, Object>> deferredInfolist(ProvideLoanDeferred provideLoanDeferred) {
        Map<String, Object> model = new HashMap<>();
        model.put("liCode", provideLoanDeferred.getLiCode());
        return AjaxJson.success(model);
    }

    /**
     * 多次展期列表明细
     */
    @PostMapping("/deferredInfoItems")
    public AjaxJson<List<PrecativeLoanDeferredVo>> deferredInfoItems(String liCode) {
        return AjaxJson.success(cmsPrecativeLoanDeferredService.getPrecativeLoanSendListByBillCode(liCode));
    }

    /**
     * 委贷展期申请审核列表
     **/
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableDeferredApprove")
    public AjaxJson<DataTablesOutput> dataTableDeferredApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanDeferredService.dataTableApprove(input));
    }

    /**
     * 委贷展期补录审核列表
     **/
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableRepairApprove")
    public AjaxJson<DataTablesOutput> dataTableRepairApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanDeferredService.dataTableRepairApprove(input));
    }

    /**
     * 跳转至审核详情界面
     */
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId, String repairType, String billCode) {
        Map<String, Object> model = new HashMap<>();
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(businessId);
        model.put("send", send);
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
        model.put("info", info);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(businessId);// 已还
        model.put("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(businessId);// 在途金额
        model.put("passageMoney", passageMoney);
        if (!EmptyUtils.isEmpty(billCode)) {
            CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(billCode);
            model.put("deferred", deferred);
        }
        model.put("type", "type");
        model.put("repairType", repairType);
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     */
    @GetMapping("/mobileDeferredApprove")
    public AjaxJson<Map<String, Object>> mobileDeferredApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.get(businessId);
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(deferred.getLiCode());
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(sendVo.getFatherCode());
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendVo.getBillCode()); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendVo.getBillCode()); // 已还款金额
        BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
        model.put("deferred", deferred);
        model.put("precativeSendPage", preVo);
        model.put("precativePage", preVo);
        model.put("PassageMoney", PassageMoney);
        model.put("repayMoney", repayMoney);
        model.put("loanMoney", loanMoney);
        model.put("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
        model.put("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.put("taskId", taskId);
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     */
    @GetMapping("/mobileDeferredRepairApprove")
    public AjaxJson<Map<String, Object>> mobileDeferredRepairApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.get(businessId);
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(deferred.getLiCode());
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendVo.getBillCode()); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendVo.getBillCode()); // 已还款金额
        BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
        model.put("deferred", deferred);
        model.put("precativeSendPage", preVo);
        model.put("precativePage", preVo);
        model.put("PassageMoney", PassageMoney);
        model.put("repayMoney", repayMoney);
        model.put("loanMoney", loanMoney);
        model.put("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
        model.put("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.put("taskId", taskId);
        model.put("pageType", "repair");
        return AjaxJson.success(model);
    }
}
