package com.bytter.tally.web.controller;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.dto.FcAccPlusDTO;
import com.bytter.tally.service.CenterPaymentService;
import com.bytter.tally.vo.FcAccbookPageVO;
import com.bytter.tally.vo.FcAccbookVoucherVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller  中心申请付款记账
 * @Description: BisExc
 * @author
 * @date 2017-07-04 14:58:22
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/bankCollect/centerPayment")
public class CenterPaymentController extends BaseController {
	@Autowired
	private CenterPaymentService centerPaymentService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY)
	@LogMessage(content = "查询BisExcDTO列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{type}")
	public String list(@PathVariable("type") String type,Model model) {
		model.addAttribute("opType", type);
		return "finacial/bankcollect/centerpayment/listCenterPayment";
	}
	/**
	 * 构建记账分录
	 */
	@PostMapping("/queryBisExc")
	@ResponseBody
	public FcAccbookPageVO queryBisExcDTO(String voucherNo, String type){
		return centerPaymentService.processAccbook(voucherNo, type, getUserId());
	}
    @PostMapping("/datatable")
    @ResponseBody
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY)
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return centerPaymentService.getBisExcDataTables(dataTablesInput, getCurrentUser());
    }
	/**
	 * 提交FC_ACCBOOK
	 */
    //@RequiresPermissions(ResourceConstant.FCL_CENTERPAYMENT_COMMON_APPLY)
	@LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
		centerPaymentService.submit(fcAccbook, key, getCurrentUser()); //TODO
		return success();
	}
    //@RequiresPermissions(ResourceConstant.FCL_CENTERPAYMENT_COMMON_APPLY)
	@LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook,String key) {
		centerPaymentService.save(fcAccbook, key, getCurrentUser());
		return success();
	}
    //@RequiresPermissions(ResourceConstant.FCL_CENTERPAYMENT_COMMON_APPLY)
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
		centerPaymentService.backFcAccbook(fcAccbook, key, getCurrentUser());
		return success();
	}
	//@RequiresPermissions(ResourceConstant.FCL_CENTERPAYMENT_COMMON_APPLY)
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
   	@PostMapping("/pass")
   	@ResponseBody
   	public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook,String key) {
   		centerPaymentService.createVoucher(fcAccbook, key, getCurrentUser());
   		return success();
   	}
    /**
	 * 查询父账号下面子账号的内部帐号
	 */
	@PostMapping("/findFcAcc")
	@ResponseBody
	public List<FcAccPlusDTO> getFcAcc(HttpServletRequest request, String voucherNo) {
		return centerPaymentService.getFcAcc(voucherNo);
	}
	@GetMapping("/getVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> json(){
        return fcVoucherTypeService.getFcVoucherType();
    }
}
