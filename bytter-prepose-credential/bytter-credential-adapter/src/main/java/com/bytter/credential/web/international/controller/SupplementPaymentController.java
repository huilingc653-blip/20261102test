package com.bytter.credential.web.international.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.entity.LcPayment;
import com.bytter.credential.domain.international.service.BillArrivalService;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.credential.domain.international.service.PaymentService;
import com.bytter.credential.domain.international.service.SupplementPaymentService;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-20 11:10
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/supplementPayment")
public class SupplementPaymentController  extends BaseController {
    @Autowired
    private SupplementPaymentService supplementPaymentService;
    @Autowired
    private BillArrivalService billArrivalService;
    @Autowired
    private CreatedCreditService creditService;
    @Autowired
    private PaymentService paymentService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
    @DubboReference(check = false)
	private IRatUrationService urationService;
    @DubboReference(check = false)
	private ICorpService corpService;
    /**
     * 付汇补录列表页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "付汇补录列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listSupplementPayment")
    public String listArrival(Model model) {
        return "international/credit/payment/listSupplementPayment";
    }
    /**
     * 付汇补录显示页面
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "付汇补录显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editSupplementPayment")
    public String editSupplementPayment(@RequestParam String id, Model model) {
        LcPayment lcPayment = supplementPaymentService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcPayment.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        CorpDTO corpDTO = corpService.get(lcBillArrival.getCorpId());
        BigDecimal exchange = excrateAdjustService.getExchangeRateBy(lcEnrol.getIssuingCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        lcPayment.setDayRate(exchange);
        RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.LCS,lcEnrol.getId());
        if(ratUrationDTO!=null){
            model.addAttribute("ratCreditType", ratUrationDTO.getCreditType());//授信方式
        }
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("lcPayment", lcPayment);
        model.addAttribute("curName", currencyService.getObjectById(lcPayment.getCurId()).getCurName());
        return "international/credit/payment/editSupplementPayment";
    }
    /**
     * 付汇补录改查询列表
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "付汇补录数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/querySupplementPayment")
    @ResponseBody
    public DataTablesOutput listPaymentApproved(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return supplementPaymentService.listPaymentApproved(dataTablesInput,user);
    }
    /**
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "付汇费用记录表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/querySupplementPaymentCost")
    @ResponseBody
    public List<Object> getPaymentCost(@RequestParam String id, Model model) {
        Map<String, Object> params = new HashedMap();
        params.put("paymentId", id);
        return supplementPaymentService.listPaymentCost(params);
    }
    /**
     * 付汇补录保存
     * @param feeTypes
     * @param feeMoneys
     * @param id
     * @param settleRate
     * @param dayRate
     * @return
     */
    @LogMessage(content = "付汇补录保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveSupplementPayment")
    @ResponseBody
    public AjaxJson saveSupplementPayment(
            @RequestParam(value = "feeType", required = false) List<String> feeTypes,
            @RequestParam(value = "feeMoney", required = false) List<Double> feeMoneys,
            @RequestParam String id, @RequestParam Double settleRate, @RequestParam Double dayRate,
            @RequestParam(value = "isAllRelease", required = false, defaultValue = "0") String isAllRelease
            ) {
        ICurrentUser user = getCurrentUser();
        /*更新付汇表数据*/
        LcPayment lcPayment = paymentService.get(id);
        lcPayment.setSettleRate(BigDecimal.valueOf(settleRate));
        lcPayment.setDayRate(BigDecimal.valueOf(dayRate));
        lcPayment.setUpdateBy(user.getLoginName());
        lcPayment.setUpdateDate(new Date());
        lcPayment.setIsAllRelease(isAllRelease);
//      修改为只能进行一次补录操作，补录完成后则置为业务结束状态
        lcPayment.setStatus(InternationalConstant.BUSINESSFINISH);
        paymentService.updateEntity(lcPayment);
        supplementPaymentService.savePaymentCosts(feeTypes, feeMoneys, user, id);
        /*释放授信*/
        supplementPaymentService.handleUration(lcPayment);
//        释放保证金
        supplementPaymentService.handleBond(lcPayment);
        return success();
    }
}
