package com.bytter.biz.ttpay.controller;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.service.TpBisdtlClaimService;
import com.bytter.biz.ttpay.vo.TPpBisdtlclaimVO;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author zhupw
 * @version 1.0
 * @Title: Controller
 * @Description: 第三方支付流水认领
 * @date 2022-5-16
 */
@Controller
@RequestMapping("/ttpay/bisdtlclaim")
public class TpBisdtlClaimController extends BaseController {
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired(required = false)
    private TpBisdtlClaimService tpBisdtlClaimService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_BISDTLCLAIM_OP)
    @LogMessage(content = "查询第三方流水明细表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_BISACCDTL_CLAIM);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
         //是否认领到业务类别
        model.addAttribute("isClaimBusType", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_PAY_TYPE).getValue());
        return "ttpay/listBisDtlBudgetRule";
    }
    /**
     * 商户信息流水查询列表数据获取
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP)
    @LogMessage(content = "查询商户信息流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
        return tpBisdtlClaimService.getDataTablesUnion(input);
    }
    @RequestMapping(params = "getMchId")
    @ResponseBody
    public List<TpMerchantVO> getMchId(String cdSign, String corpId) {
        return tpBisdtlClaimService.getMchId(corpId, cdSign);
    }
    /**
     * 确认
     *
     * @param tPpBisdtlclaimVO
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP)
    @LogMessage(content = "第三方支付流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/sure")
    @ResponseBody
    public AjaxJson sure(TPpBisdtlclaimVO tPpBisdtlclaimVO) {
        tpBisdtlClaimService.sure(tPpBisdtlclaimVO, this.getCurrentUser());
        return success();
    }
    /**
     * 处理逻辑
     *
     * @param id
     * @param type H 恢复 1还原 2删除
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "第三方支付流水认领处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/business/{id}/{type}")
    @ResponseBody
    public AjaxJson business(@PathVariable("id") String id, @PathVariable("type") String type) {
        // 逻辑变化
        tpBisdtlClaimService.business(id, this.getCurrentUser(), type);
        return success();
    }
    /**
     * 查询资金计划详情
     *
     * @param id
     * @param model
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @LogMessage(content = "查询资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        TPpBisdtlclaimVO tPpBisdtlclaimVO = tpBisdtlClaimService.getByUnion(id);
        model.addAttribute("tPpBisdtlclaimVO", tPpBisdtlclaimVO);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_BISACCDTL_CLAIM);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
        return "ttpay/viewBisDtlBudgetRule";
    }
    /**
     * 查询银行流水详情
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @LogMessage(content = "查询资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDtl/{id}")
    public String viewDtl(@PathVariable("id") String id, Model model) {
        TPpBisdtlclaimVO tPpBisdtlclaimVO = tpBisdtlClaimService.getByUnion(id);
        model.addAttribute("tPpBisdtlclaimVO", tPpBisdtlclaimVO);
        //默认人民币
        BtCurrencyDTO rmbCur = btCurrencyService.getRMBCurrency();
        tPpBisdtlclaimVO.setCurId(rmbCur.getId());
        tPpBisdtlclaimVO.setCurName(rmbCur.getCurName());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_BISACCDTL_CLAIM);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
        return "ttpay/viewBisDtlBudgetRuleDtl";
    }
    /**
     * 批量确认计划
     *
     * @param ids
     * @param type        Y 确认 D 删除
     * @param itemCode    科目代码
     * @param itemName    科目名称
     * @param processDate 执行时间
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "第三方支付流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batch")
    @ResponseBody
    public AjaxJson batchBusiness(@RequestParam("ids") String ids, @RequestParam("type") String type, @RequestParam("itemCode") String itemCode,
                                  @RequestParam("itemName") String itemName, @RequestParam("processDate") String processDate, @RequestParam("batchBusType") String batchBusType) {
        tpBisdtlClaimService.batchBusiness(ids, itemCode, itemName, processDate, this.getCurrentUser(), type, batchBusType);
        return success();
    }
}
