package com.bytter.credential.web.international.controller;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.entity.LcIncoming;
import com.bytter.credential.domain.international.service.ArrivalLetterService;
import com.bytter.credential.domain.international.service.IncomingLetterService;
import com.bytter.credential.domain.international.service.ReceiveLetterService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/***到期收款**/
@Controller
@RequestMapping("/international/credit/incomingletter")
public class IncomingLetterController extends BaseController {
	@Autowired 
	private ArrivalLetterService arrivalLetterService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@Autowired
	private IncomingLetterService incomingLetterService;
	@Autowired
	private ReceiveLetterService receiveLetterService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
	/**
	 * 到期收款列表
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.CMS_PROVIDE_INFO_EDIT)
	@GetMapping("/listIncoming")
	public String listIncoming(Model model) {
		return "international/credit/receive/listRegisterIncoming";
	}
	/**
	 * 选择待到期收款数据
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.CMS_PROVIDE_INFO_EDIT)
	@GetMapping("/selectIncoming")
	public String selectIncoming(String row,String id,Model model) {
		if (null != id && StringUtils.isNotEmpty(id.toString())) {
			BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
			model.addAttribute("corpId", bisAccDtlDTO.getCorpId());
			model.addAttribute("curId", bisAccDtlDTO.getCurId());
			model.addAttribute("transTime",bisAccDtlDTO.getTransTime());
		}
		model.addAttribute("row",row);
		return "international/credit/receive/selectRegisterIncoming";
	}
	/**
	 * 功能介绍 :到期收款列表<br>
	 * @author zhengxn
	 */
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return incomingLetterService.queryIncoming(dataTablesInput, getCurrentUser());
	}
	@PostMapping("/selectDatatable")
	@ResponseBody
	public DataTablesOutput selectDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return incomingLetterService.selectDatatable(dataTablesInput, getCurrentUser());
	}
	/**
	 * 申请-页面跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/add/{id}")
	public String add(@PathVariable("id") String id,Model model){
		LcBillArrival arrival = arrivalLetterService.get(id);
		model.addAttribute("arrival",arrival);
		LcEnrol lcEnrol = receiveLetterService.get(arrival.getLcId());
		model.addAttribute("exchangeRate", excrateAdjustService.getExchangeRateBy(lcEnrol.getIssuingCurId(), lcEnrol.getConvertCurId(), lcEnrol.getExpiryDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT));
		model.addAttribute("lcInter",lcEnrol.getLcInter());
		return "international/credit/receive/addReceiveIncoming";
	}
	/**
	 * 登记-入库 提交
	 * @param entity 实体
	 * @return
	 */
	@LogMessage(content = "添加到期收款",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/tempOrSubmit")
	@ResponseBody
	public AjaxJson tempOrSubmit(LcIncoming entity){
		incomingLetterService.tempOrSubmit(entity,getCurrentUser());
		return success();
	}
	
}
