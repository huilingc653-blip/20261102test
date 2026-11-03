package com.bytter.biz.finacial.accbook.controller;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.service.FcAccbookInvalidService;
import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookPageVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
/**
 * @Title: 作废控制器
 * @Description: FC_ACCBOOK
 * @author onlineGenerator
 * @date 2017-06-19 11:28:14
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/accbook/fcaccbookinvalid")
public class FcAccbookInvalidController extends BaseController {
	@Autowired
	private FcAccbookService fcAccbookService;
	@Autowired
	private FcAccbookInvalidService invalidService;
	/**
	 * FC_ACCBOOK列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "finacial/accbook/invalid/listFcAccbookInvalid";
	}
	
	/**
	 * FC_ACCBOOK作废修改列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/editlist")
	public String editlist(Model model) {
		return "finacial/accbook/invalid/listEditFcAccbookInvalid";
	}
	
	/**
	 * FC_ACCBOOK 复核列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/checklist")
	public String checklist(Model model) {
		return "finacial/accbook/invalid/listCheckFcAccbookInvalid";
	}
	/**
	 * FC_ACCBOOK table列表
	 *  待作废账务列表数据查询
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return invalidService.getCanInvalidFcAccbookVoList(dataTablesInput, ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
	}
	
	/**
	 * 功能介绍 : 作废修改账务列表数据查询。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@PostMapping("/editdataTable")
    @ResponseBody
    public DataTablesOutput editdataTable(@RequestBody  DataTablesInput dataTablesInput){
		return invalidService.getEditInvalidFcAccbookVoList(dataTablesInput, ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
    }
	/**
	 * 功能介绍 : 作废复核账务列表数据查询。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@PostMapping("/checkdataTable")
    @ResponseBody
    public DataTablesOutput checkdataTable(@RequestBody  DataTablesInput dataTablesInput){
		return invalidService.getCheckInvalidFcAccbookVoList(dataTablesInput, ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
    }
	
	/**
	 * 功能介绍 : 账务作废前判断。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@RequestMapping("/invalidBefore")
	@ResponseBody
    public String invalidBefore(String bookId){
		return invalidService.invalidBefore(bookId);
    }
	
	/**
	 * 作废申请
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@GetMapping("/add")
	public String add(String id, String opType, Model model) {
		if (StringUtils.isNotEmpty(id)) {
            FcAccbookPageVO voucher = invalidService.getFcAccbookPageVOById(id);
			BigDecimal debitTot = BigDecimal.ZERO;
			for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
				if (null == bookDtl.getDebitMoney()) {
					bookDtl.setDebitMoney(BigDecimal.ZERO);
				}
				if (null == bookDtl.getCreditMoney()) {
					bookDtl.setCreditMoney(BigDecimal.ZERO);
				}
				debitTot = debitTot.add(bookDtl.getDebitMoney());
			}
			model.addAttribute("voucher", voucher);
			model.addAttribute("debitTot", debitTot);
			model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,###.00"));
			model.addAttribute("busGlobalId", voucher.getAccbook().getBusGlobalId());
		}
		return "finacial/accbook/invalid/addFcAccbookInvalid";
	}
	
	/**
	 * FC_ACCBOOK编辑页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@GetMapping("/update")
	public String update(String id, String opType, Model model) {
		if (StringUtils.isNotEmpty(id)) {
            FcAccbookPageVO voucher = invalidService.getFcAccbookPageVOById(id);
			BigDecimal debitTot = BigDecimal.ZERO;
			for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
				if (null == bookDtl.getDebitMoney()) {
					bookDtl.setDebitMoney(BigDecimal.ZERO);
				}
				if (null == bookDtl.getCreditMoney()) {
					bookDtl.setCreditMoney(BigDecimal.ZERO);
				}
				debitTot = debitTot.add(bookDtl.getDebitMoney());
			}
			model.addAttribute("voucher", voucher);
			model.addAttribute("debitTot", debitTot);
			model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,###.00"));
			model.addAttribute("busGlobalId", voucher.getAccbook().getBusGlobalId());
		}
		model.addAttribute("opType", StringUtils.isEmpty(opType));
		String page = "finacial/accbook/invalid/updateFcAccbookInvalid";
		if (FinacialConstant.PAGE_CHECK.equals(opType)) {
			page = "finacial/accbook/invalid/checkFcAccbookInvalid";
		}
		
		return page;
	}
	/**
	 * FC_ACCBOOK详情页
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcAccbookPageVO voucher = invalidService.getFcAccbookPageVOById(id);
			BigDecimal debitTot = BigDecimal.ZERO;
			for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
				if (null == bookDtl.getDebitMoney()) {
					bookDtl.setDebitMoney(BigDecimal.ZERO);
				}
				if (null == bookDtl.getCreditMoney()) {
					bookDtl.setCreditMoney(BigDecimal.ZERO);
				}
				debitTot = debitTot.add(bookDtl.getDebitMoney());
			}
			model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
					.equals(voucher.getAccbook().getVoucherState()));
			model.addAttribute("voucher", voucher);
			model.addAttribute("debitTot", debitTot);
			model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot.floatValue(), "#,###.00"));
		}
		return "finacial/accbook/invalid/viewFcAccbookInvalid";
	}
	
	/**
	 * 作废修改暂存FC_ACCBOOK
	 * 
	 * @param fcAccbookBlned
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "添加FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(HttpServletRequest request, FcAccbookVoucherVO voucherVO) {
		validVoucher(request);
		processInvalidVoucher(request, "save",getCurrentUser());
		return success();
	}
	
	/**
	 * 作废提交FC_ACCBOOK
	 * 
	 * @param fcAccbookBlned
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "添加FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherVO voucherVO) {
		validVoucher(request);
		processInvalidVoucher(request, "submit",getCurrentUser());
		return success();
	}
	
	/**
	 * 作废暂存FC_ACCBOOK
	 * 
	 * @param fcAccbookBlned
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "添加FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherVO voucherVO) {
		validVoucher(request);
		processInvalidVoucher(request, "save",getCurrentUser());
		return success();
	}
	private void validVoucher(HttpServletRequest request) {
		String pageType = request.getParameter("pageType");//页面类型
		String busGlobalId = request.getParameter("busGlobalId");//业务ID
		invalidService.validVoucher(pageType, busGlobalId);
	}
	
	/**
	 * 功能介绍 : 设置凭证作废数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void processInvalidVoucher(HttpServletRequest request,
									   String opType, ICurrentUser user) {
		String invalidReason = request.getParameter("invalidReason");//作废凭证的作废原因
		String backReason = request.getParameter("backReason");//打回原因
		String busGlobalId = request.getParameter("busGlobalId");
		
		FcAccbookExt bookExt = new FcAccbookExt();
		bookExt.setInvalidReason(invalidReason);
		bookExt.setBackReason(backReason);
		
		FcAccbookVoucherVO invalidVoucher = new FcAccbookVoucherVO();
		invalidVoucher.setBookExt(bookExt);
		
		VoucherWrapper voucher = setVoucherWrapper(invalidVoucher);
		voucher.setBusGlobalId(busGlobalId);
		voucher.setUserCode(getUserVo().getId());//设置操作用户
		voucher.setUser(user);
		if (FinacialConstant.METHOD_SAVE.equals(opType)) {
			invalidService.processInvalidVoucherSave(voucher);
		} else if (FinacialConstant.METHOD_SUBMIT.equals(opType)) {
			invalidService.processInvalidVoucherSubmit(voucher);
		} else if (FinacialConstant.METHOD_CHECK.equals(opType)) {
			invalidService.processInvalidVoucherCheck(voucher);
		} else if (FinacialConstant.METHOD_BACK.equals(opType)) {
			invalidService.processInvalidVoucherBack(voucher);
		} else if (FinacialConstant.METHOD_DELETE.equals(opType)) {
			invalidService.processInvalidVoucherDelete(voucher);
		}
		
	}
	
	private VoucherWrapper setVoucherWrapper(FcAccbookVoucherVO fcAccbook) {
		VoucherWrapper voucher = new VoucherWrapper();
		if (null != fcAccbook) {
			voucher.addTarget(fcAccbook);
		}
		voucher.setProcessType(ProcessType.PROCESS_INVALID);
		return voucher;
	}
	
	/**
	 * 作废复核通过FC_ACCBOOK
	 * 
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "修改FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(HttpServletRequest request) {
		validVoucher(request);
		processInvalidVoucher(request, "check",getCurrentUser());
		return success();
	}
	
	/**
	 * 打回作废凭证FC_ACCBOOK
	 * 
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "修改FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(HttpServletRequest request) {
		validVoucher(request);
		processInvalidVoucher(request, "back", getCurrentUser());
		return success();
	}
	
	/**
	 * 作废抹账FC_ACCBOOK
	 * 
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "修改FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		validVoucher(request);
		processInvalidVoucher(request, "delete", getCurrentUser());
		return success();
	}


	/**
	 * 批量作废提交
	 *
	 * @param ids
	 * @param backMsg
	 * @return
	 */
	@LogMessage(content = "批量作废提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchInvalid")
	@ResponseBody
	public AjaxJson batchInvalid(@RequestParam("ids") String ids, @RequestParam("backMsg") String backMsg) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			invalidService.validVoucher("add", id);
			batchProcessInvalidVoucher(backMsg,"", id, "submit",getCurrentUser());
		}
		return success();
	}

	/**
	 * 批量作废复核通过
	 *
	 * @param ids
	 * @return
	 */
	@LogMessage(content = "批量作废复核通过", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchCheck")
	@ResponseBody
	public AjaxJson batchCheck(@RequestParam("ids") String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			invalidService.validVoucher("check", id);
			batchProcessInvalidVoucher(null,"",id,"check",getCurrentUser());
		}
		return success();
	}

	/**
	 * 批量作废打回
	 *
	 * @param ids
	 * @return
	 */
	@LogMessage(content = "批量作废打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson batchBack(@RequestParam("ids") String ids,@RequestParam("backMsg") String backMsg) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			invalidService.validVoucher("check", id);
			batchProcessInvalidVoucher(null,backMsg,id,"back",getCurrentUser());
		}
		return success();
	}

	/**
	 * 凭证处理
	 *
	 * @param invalidReason
	 * @param backReason
	 * @param busGlobalId
	 * @param opType
	 * @param user
	 * @return
	 */
	private void batchProcessInvalidVoucher(String invalidReason,String backReason, String busGlobalId, String opType, ICurrentUser user) {
		FcAccbookExt bookExt = new FcAccbookExt();
		bookExt.setInvalidReason(invalidReason);
		bookExt.setBackReason(backReason);

		FcAccbookVoucherVO invalidVoucher = new FcAccbookVoucherVO();
		invalidVoucher.setBookExt(bookExt);

		VoucherWrapper voucher = setVoucherWrapper(invalidVoucher);
		voucher.setBusGlobalId(busGlobalId);
		voucher.setUserCode(getUserVo().getId());//设置操作用户
		voucher.setUser(user);
		if (FinacialConstant.METHOD_SAVE.equals(opType)) {
			invalidService.processInvalidVoucherSave(voucher);
		} else if (FinacialConstant.METHOD_SUBMIT.equals(opType)) {
			invalidService.processInvalidVoucherSubmit(voucher);
		} else if (FinacialConstant.METHOD_CHECK.equals(opType)) {
			invalidService.processInvalidVoucherCheck(voucher);
		} else if (FinacialConstant.METHOD_BACK.equals(opType)) {
			invalidService.processInvalidVoucherBack(voucher);
		} else if (FinacialConstant.METHOD_DELETE.equals(opType)) {
			invalidService.processInvalidVoucherDelete(voucher);
		}

	}
	
}
