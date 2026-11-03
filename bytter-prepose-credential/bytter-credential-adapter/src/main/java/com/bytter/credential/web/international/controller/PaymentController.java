package com.bytter.credential.web.international.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.entity.LcPayment;
import com.bytter.credential.domain.international.service.BillArrivalService;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.credential.domain.international.service.PaymentService;
import com.bytter.credential.domain.international.vo.LcPaymentVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-19 9:39
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/payment")
public class PaymentController extends BaseController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CreatedCreditService creditService;
    @Autowired
    private BillArrivalService billArrivalService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private ICorpService corpService;
    /**
     * 到单登记列表页面
     */
    @LogMessage(content = "付汇申请页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listArrival")
    public String listArrival(Model model) {
        return "international/credit/payment/listPaymentApplay";
    }
    /**
     *  付汇申请列表
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "付汇申请数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryArrival")
    @ResponseBody
    public DataTablesOutput queryArrival(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return paymentService.listArrival(dataTablesInput, user);
    }
    /**
     *  付汇修改列表跳转
     * @param model
     * @return
     */
    @LogMessage(content = "付汇修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listPayment")
    public String listArrivalUpdate(Model model){
        return "international/credit/payment/listPaymentUpdate";
    }
    /**
     * 付汇修改数据查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "付汇申请数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryListPayment")
    @ResponseBody
    public DataTablesOutput listArrivalUpdate(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return paymentService.listPaymentUpdate(dataTablesInput, user);
    }
    /**
     * 付汇申请页面跳转
     */
    @LogMessage(content = "付汇申请页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addPayment")
    public String addPayment(@RequestParam String id ,Model model) {
        LcBillArrival lcBillArrival = billArrivalService.get(id);
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("curName", currencyService.getObjectById(lcBillArrival.getCurId()).getCurName());
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*付汇在途和已押汇金额*/
        params.put("flag", "2");
        model.addAttribute("paymentedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("paymentingMoney",paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/payment/addPayment";
    }
    /**
     * 付汇数据保存
     */
    @LogMessage(content = "付汇数据保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/savePayment")
    @ResponseBody
    public AjaxJson savePayment(LcPayment lcPayment, Model model) {
        ICurrentUser user = getCurrentUser();
        paymentService.saveOrTmpPayment(lcPayment, user);
        return success();
    }
    /**
     * 付汇编辑跳转
     */
    @LogMessage(content = "付汇编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editPayment")
    public String editPayment(@RequestParam String id ,Model model) {
        LcPayment lcPayment = paymentService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcPayment.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("lcPayment", lcPayment);
        model.addAttribute("curName", currencyService.getObjectById(lcBillArrival.getCurId()).getCurName());
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*付汇在途和已押汇金额*/
        params.put("flag", "2");
        model.addAttribute("paymentedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("paymentingMoney",paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/payment/editPayment";
    }
    /**
     *  付汇申请审批列表跳转
     * @param model
     * @return
     */
    @LogMessage(content = "付汇申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listPaymentApprove")
    public String listPaymentApprove(@RequestParam String flowStatus,Model model){
        model.addAttribute("flowStatus", flowStatus);
        return "international/credit/payment/approve/listPaymentApprove";
    }
    /**
     * 付汇申请数据查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "付汇申请数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryPaymentApprove")
    @ResponseBody
    public DataTablesOutput listPaymentApprove(@RequestBody DataTablesInput dataTablesInput){
        return paymentService.listPaymentApprove(dataTablesInput);
    }
    /**
     * 付汇审批页面跳转
     * @return
     */
    @LogMessage(content = "付汇审批页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPaymentApprove")
    public String viewPaymentApprove(@RequestParam String id, @RequestParam String taskId, Model model){
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "international/credit/payment/approve/viewPaymentApprove";
    }
    /**
     * 付汇详情显示页面
     * @return
     */
    @LogMessage(content = "付汇详情显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPayment")
    public String viewPayment(@RequestParam String id, Model model){
        LcPayment lcPayment = paymentService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcPayment.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        LcPaymentVO lcPaymentVO = new LcPaymentVO();
        BeanUtils.copyProperties(lcPayment,lcPaymentVO);
        
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("lcPayment", lcPaymentVO);
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*付汇在途和已押汇金额*/
        params.put("flag", "2");
        model.addAttribute("paymentedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("paymentingMoney",paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/payment/viewPayment";
    }
    /**
     * 取消付汇操作
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "取消付汇", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/cancelPayment")
    @ResponseBody
    public AjaxJson cancelPayment(@RequestParam String id, Model model) {
        ICurrentUser user = getCurrentUser();
        paymentService.cancelPayment(id, user);
        return success();
    }
    /**
     *付汇详情手机显示页面
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "付汇详情手机显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileViewPayment")
    public String MobileViewPayment(@RequestParam String id, @RequestParam String businessId, Model model){
        LcPayment lcPayment = paymentService.get(businessId);
        LcBillArrival lcBillArrival = billArrivalService.get(lcPayment.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        LcPaymentVO lcPaymentVO = new LcPaymentVO();
        BeanUtils.copyProperties(lcPayment,lcPaymentVO);
        
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("lcPayment", lcPaymentVO);
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*付汇在途和已押汇金额*/
        params.put("flag", "2");
        model.addAttribute("paymentedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("paymentingMoney",paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/payment/approve/mobileViewPayment";
    }
}
