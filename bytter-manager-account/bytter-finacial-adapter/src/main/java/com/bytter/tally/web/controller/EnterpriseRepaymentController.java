package com.bytter.tally.web.controller;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.tally.entity.TallyCmsLoanRepay;
import com.bytter.tally.service.EnterpriseRepaymentService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * <br>
 * <b>类功能：</b>企业贷款还款手工记账<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2018年3月26日 <br>
 * <b>版权所有：<b>版权所有(C) 2018，拜特科技<br>
 */
@Controller
@RequestMapping("/finacial/enterpriseRepayment")
public class EnterpriseRepaymentController extends BaseController {
	
	@Autowired
	private EnterpriseRepaymentService enterpriseRepaymentService;
	
	@DubboReference(check = false)
	private IFcBusRaceService iFcBusRaceService;
	
	@PostMapping("/getAccbook")
	@ResponseBody
	public AjaxJson getAccbook(TallyCmsLoanRepay repay, String opType){
		AjaxJson json = new AjaxJson();
		FcAccbookVoucherDTO voucher = enterpriseRepaymentService.createFcAccbook(repay, opType, getCurrentUser());
		Map<String, Object> strs = new HashMap<String, Object>();
		strs.put("dtlsHtml", enterpriseRepaymentService.getDtlHtml(voucher.getBookDtls()));
		json.setObj(voucher);
		json.setAttributes(strs);
		json.setSuccess(true);
		return json;
	}
	
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook) {
		String opState = request.getParameter("opState");
		String pageType = request.getParameter("pageType");
		String repayId = request.getParameter("repayId");
		if(NbconsoleConstant.OpType.EDIT.get().equals(pageType)&&"2".equals(opState)){//改账打回
			enterpriseRepaymentService.editBackAccbook(fcAccbook, getCurrentUser());
		}else{//暂存/提交
			enterpriseRepaymentService.saveAccbook(fcAccbook,repayId,opState,pageType,getCurrentUser());
		}
		return success();
	}
	
	
	
	@PostMapping("/getFcAccbookTempInfo")
	@ResponseBody
	public AjaxJson getFcAccbookTempInfo(String billCode){
		AjaxJson json = new AjaxJson(true);
		VoucherWrapperDTO wrapper = enterpriseRepaymentService.getFcAccbookTempInfo(billCode);
		FcAccbookVoucherDTO voucher = wrapper.getBookVoucher();
		Map<String, Object> strs = new HashMap<String, Object>();
		strs.put("dtlsHtml", enterpriseRepaymentService.getDtlHtml(voucher.getBookDtls()));
		json.setObj(voucher);
		json.setAttributes(strs);
		return json;
	}
	
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(HttpServletRequest request) {
		String opState = request.getParameter("opState");
		String busGlobalId = request.getParameter("busGlobalId");
		enterpriseRepaymentService.checkAccbook(opState, busGlobalId,getCurrentUser());
		return success();
	}
	
	
	@PostMapping("/getRaceId")
	@ResponseBody
	public AjaxJson getRaceId(String netId){
		AjaxJson json = new AjaxJson();
		String raceId = iFcBusRaceService.getNetBusRace(SysRaceCode.NB_ENTERPRISE_REPAYMENT.get(), netId, getUserId());
		FcBusRaceDTO race = new FcBusRaceDTO();
		race.setId(raceId);
		race.setRaceName(SysRaceCode.NB_ENTERPRISE_REPAYMENT.getName());
		json.setObj(race);
		json.setSuccess(true);
		return json;
	}
	
}
