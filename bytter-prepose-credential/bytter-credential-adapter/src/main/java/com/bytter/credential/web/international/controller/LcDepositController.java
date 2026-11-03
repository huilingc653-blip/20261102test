package com.bytter.credential.web.international.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcDeposit;
import com.bytter.credential.domain.international.entity.LcDepositRepay;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.BillArrivalService;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.credential.domain.international.service.LcDepositService;
import com.bytter.credential.domain.international.service.PaymentService;
import com.bytter.credential.domain.international.vo.LcDepositVO;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
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
 * @Date 2018-12-21 10:11
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/lcDeposit")
public class LcDepositController extends BaseController {
    @Autowired
    private LcDepositService lcDepositService;
    @Autowired
    private CreatedCreditService creditService;
    @Autowired
    private PaymentService paymentService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private BillArrivalService billArrivalService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@DubboReference(check = false)
	private IRatUrationService urationService;
    /**
     * 押汇申请页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "押汇申请页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listArrival")
    public String listArrival(Model model) {
        return "international/credit/lcDeposit/listDepostApplay";
    }
    /**
     *  押汇申请列表
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "押汇申请数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryArrival")
    @ResponseBody
    public DataTablesOutput queryArrival(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositService.listArrival(dataTablesInput, user);
    }
    /**
     * 押汇申请页面跳转
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "押汇申请页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addDeposit")
    public String addDeposit(@RequestParam String id ,Model model) {
        LcBillArrival lcBillArrival = billArrivalService.get(id);
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("curName", currencyService.getObjectById(lcBillArrival.getCurId()).getCurName());
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*押汇在途和已押汇金额*/
        params.put("flag", "1");
        model.addAttribute("depositedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("depositingMoney", paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/lcDeposit/addDeposit";
    }
    /**
     *  押汇提交或暂存
     * @param lcDeposit
     * @param model
     * @return
     */
    @LogMessage(content = "押汇暂存或提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveOrTmpDeposit")
    @ResponseBody
    public AjaxJson saveOrTmp(LcDeposit lcDeposit, Model model){
        ICurrentUser user = getCurrentUser();
        lcDepositService.saveOrTmpDeposit(lcDeposit, user);
        return success();
    }
    /**
     * 押汇补录暂存列表
     * @param model
     * @return
     */
    @LogMessage(content = "押汇补录暂存列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listSupplementDeposit")
    public String listDeposit(Model model) {
        return "international/credit/lcDeposit/listDeposit";
    }
    /**
     * 押汇补录暂存数据查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "押汇补录暂存数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryDeposit")
    @ResponseBody
    public DataTablesOutput querySupplementDeposit(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositService.queryDeposit(dataTablesInput, user);
    }
    /**
     * 押汇编辑页面跳转
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "押汇编辑页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editDeposit")
    public String editDeposit(@RequestParam String id ,Model model) {
        LcDeposit lcDeposit = lcDepositService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("curName", currencyService.getObjectById(lcBillArrival.getCurId()).getCurName());
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*押汇在途和已押汇金额*/
        params.put("flag", "1");
        model.addAttribute("depositedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("depositingMoney", paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/lcDeposit/editDeposit";
    }
    /**
     * 押汇审批列表跳转
     * @param flowStatus
     * @param model
     * @return
     */
    @LogMessage(content = "押汇审批列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDepositApprove")
    public String listDepositApprove(@RequestParam String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "international/credit/lcDeposit/approve/listDepositApprove";
    }
    /**
     * 押汇审批列表显示
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "押汇审批列表显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryDepositApprove")
    @ResponseBody
    public DataTablesOutput queryDepositApprove(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositService.listDepositApprove(dataTablesInput);
    }
    /**
     * 押汇审批显示页面
     * @param id 主键id
     * @param taskId
     * @param model
     * @return
     */
    @LogMessage(content = "押汇审批页面显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDepositApprove")
    public String viewDepositApprove(@RequestParam String id, @RequestParam String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "international/credit/lcDeposit/approve/viewDepositApprove";
    }
    /**
     * 押汇信息显示页面
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "押汇信息显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDeposit")
    public String viewDeposit(@RequestParam String id ,Model model) {
        LcDeposit lcDeposit = lcDepositService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        LcDepositVO lcDepositVO = new LcDepositVO();
        BeanUtils.copyProperties(lcDeposit,lcDepositVO);
        
        model.addAttribute("lcDeposit", lcDepositVO);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*押汇在途和已押汇金额*/
        params.put("flag", "1");
        model.addAttribute("depositedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("depositingMoney", paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/lcDeposit/viewDeposit";
    }
    /**
     * 押汇补录
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "押汇补录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editSupplementDeposit")
    public String editSupplementDeposit(@RequestParam String id ,Model model) {
        LcDeposit lcDeposit = lcDepositService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("curName", currencyService.getObjectById(lcBillArrival.getCurId()).getCurName());
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*押汇在途和已押汇金额*/
        params.put("flag", "1");
        model.addAttribute("depositedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("depositingMoney", paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/lcDeposit/editSupplementDeposit";
    }
    /**
     * 取消押汇
     * @return
     */
    @LogMessage(content = "取消押汇", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/cancelDeposit")
    @ResponseBody
    public AjaxJson cancelDeposit(@RequestParam String id, Model model) {
        ICurrentUser user = getCurrentUser();
        lcDepositService.cancelDeposit(id, user);
        return success();
    }
    /**
     * 还押汇列表页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "还押汇列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listLcDepositRepay")
    public String listLcDepositRepay(Model model) {
        return "international/credit/lcDeposit/listLcDepositRepay";
    }
    /**
     * 还押汇数据列表查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "还押汇数据列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLcDepositRepay")
    @ResponseBody
    public DataTablesOutput querylcDepositRepay(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositService.querylcDepositRepay(dataTablesInput, user);
    }
    /**
     * 还押汇页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "还押汇列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addLcDepositRepay")
    public String addLcDepositRepay(@RequestParam String id ,Model model) {
        LcDeposit lcDeposit = lcDepositService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.LCS,lcEnrol.getId());
        if(ratUrationDTO!=null){
            model.addAttribute("ratCreditType", ratUrationDTO.getCreditType());//授信方式
        }
        model.addAttribute("lcDepositDelay", lcDepositService.getLcDepositDelayByDepositId(lcDeposit.getId()));
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("curName", currencyService.getObjectById(lcBillArrival.getCurId()).getCurName());
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        return "international/credit/lcDeposit/lcDepositRepay";
    }
    /**
     * 还押汇列操作
     * @param model
     * @return
     */
    @LogMessage(content = "还押汇", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveLcDepositRepay")
    @ResponseBody
    public AjaxJson saveLcDepositRepay(LcDepositRepay lcDepositRepay, Model model) {
        ICurrentUser user = getCurrentUser();
        lcDepositService.saveLcDepositRepay(lcDepositRepay, user);
        return success();
    }
    /**
     * 押汇信息手机显示页面
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "押汇信息手机显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileViewDeposit")
    public String MobileViewDeposit(@RequestParam String id ,@RequestParam String businessId, Model model) {
        LcDeposit lcDeposit = lcDepositService.get(businessId);
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("arrivalId", lcBillArrival.getId());
        LcDepositVO lcDepositVO = new LcDepositVO();
        BeanUtils.copyProperties(lcBillArrival,lcDepositVO);
        
        model.addAttribute("lcDeposit", lcDepositVO);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("applicantName", corpService.get(lcEnrol.getCorpId()).getName());
        /*押汇在途和已押汇金额*/
        params.put("flag", "1");
        model.addAttribute("depositedMoney",paymentService.getSumDepositedAndPaymented(params));
        model.addAttribute("depositingMoney", paymentService.getSumDepositingAndPaymenting(params));
        return "international/credit/lcDeposit/approve/mobileViewDeposit";
    }
    /**
     * 进口押汇查询跳转
     * @return
     */
    @GetMapping("/listReportDeposit")
    public String listReportDeposit(){
        return "international/report/listReportDeposit";
    }
    /**
     *进口押汇数据查询
     * @return
     */
    @PostMapping("/listReportDepositDataTable")
    @ResponseBody
    public DataTablesOutput listReportDepositDataTable(@RequestBody DataTablesInput input){
        return lcDepositService.listReportDepositDataTable(input, getCurrentUser());
    }
}
