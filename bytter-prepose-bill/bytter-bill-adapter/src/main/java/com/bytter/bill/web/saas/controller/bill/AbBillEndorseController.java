package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillEndorseService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据背书
 * @author onlineGenerator
 * @date 2017-07-05 17:53:54
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillendorse", tags = "票据背书")
@RestController("v1AbBillEndorseController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillendorse")
public class AbBillEndorseController extends BaseController {
    @Autowired
    private AbBillEndorseService abBillEndorseService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;

    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 票据背书列表 页面初始化
     */
    @GetMapping("/list")
    @ApiOperation("票据背书列表页面初始化")
    public AjaxJson<Map<String, Object>> list(HttpServletRequest request) {
        ConcurrentModel model = new ConcurrentModel();
        // 系统参数 是否挂资金计划
        String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET)
                .getValue();
        model.addAttribute("budgetSel", budgetSel);
        //默认挑票模式为否
        String isSelectBill = request.getParameter("isSelectBill");
        if (StringUtils.isEmpty(isSelectBill)) {
            model.addAttribute("isSelectBill", "0");
        } else {
            model.addAttribute("isSelectBill", isSelectBill);
        }
        model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
        model.addAttribute("isCounterpartEndorse", abVoucherinfoViceService.isCounterpartEndorse());
        model.addAttribute("isRepeatEndorse", abVoucherinfoViceService.isRepeatEndorse());
        if ("1".equals(budgetSel)) {
            // 系统参数 背书挂接资金计划科目
            model.addAttribute("endorseItem", abConfigService
                    .getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
        }
        return AjaxJson.success(model.asMap());
    }

    /**
     * 票据背书 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据背书分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillEndorseService.listabBillEndorse(input);
        return AjaxJson.success(output);
    }

    @LogMessage(content = "背书申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/EndorsementApplication")
    @ApiOperation("发起背书申请")
    public AjaxJson EndorsementApplication(AbBillEndorseVo eo) {
        abBillEndorseService.EndorsementApplication(eo, AccebillConstant.BILL_OPERATE_SUBMIT, this.getCurrentUser());
        return AjaxJson.success();
    }


    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("票据背书审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillEndorseService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据背书审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillEndorseService.getAbBillEndorseVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null);
        }
        model.addAttribute("taskId", taskId);
        // 背书挂接资金计划
        model.addAttribute("endorseItem", abConfigService
                .getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("票据背书手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillEndorseService.getAbBillEndorseVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        model.addAttribute("endorseItem", abConfigService
                .getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 保存导入数据
     */
    @PostMapping("/saveImport")
    @ApiOperation("背书导入保存")
    public AjaxJson saveImport(@RequestBody List<AbBillEndorseVo> list) {
        this.abBillEndorseService.saveImport(list, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 背书验证票据重复背书方法
     */
    @PostMapping("/checkRepeatEndorse")
    @ApiOperation("校验票据是否重复背书")
    public Object checkRepeatEndorse(String ids, String origNos,
                                     String endorseeName, String endorseeBankNo, String repartMsg) {
        Object jsonObj = this.abBillEndorseService.checkRepeatEndorse(ids, origNos, endorseeName, endorseeBankNo, repartMsg);
        return jsonObj;
    }

    @PostMapping("/getBankTypeId")
    @ApiOperation("获取银行类型ID")
    public Object getBankTypeId(String bankCode, Integer interiorApply, String pledgeeAcc) {
        Object jsonObj = this.abBillEndorseService.getBankTypeId(bankCode, interiorApply, pledgeeAcc);
        return jsonObj;
    }
}