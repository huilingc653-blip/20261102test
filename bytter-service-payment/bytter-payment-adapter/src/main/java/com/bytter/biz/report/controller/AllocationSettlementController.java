package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.AllocationSettlementService;
import com.bytter.biz.report.vo.AllocationSettlementDtlVO;
import com.bytter.biz.report.vo.AllocationSettlementVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
/**
 * @author zhoup
 * @Description 结算量统计表
 * @date 2018.4.18
 */
@Controller
@RequestMapping("/report/allocationsettlement")
public class AllocationSettlementController extends BaseController{
	@Autowired
	private AllocationSettlementService allocationSettlementService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY)
	@GetMapping("/corplist")
	public String corplist(Model model,HttpServletRequest request){
		String date=DateUtils.formatDate(DateUtils.getMonthStartTime());

		//近7天/近1月/近3月
		String dateType=request.getParameter("dateType");
		if(dateType != null && !dateType.equals("")){
			//获取开始日期
			Date endDate = DateUtils.getDateYMD();
			Date beginDate = DateUtils.addDay(endDate, -7);
			if("30".equals(dateType)){
				beginDate = DateUtils.addMonth(endDate, -1);
			} else if ("90".equals(dateType)) {
				beginDate = DateUtils.addMonth(endDate, -3);
			}
			date=DateUtils.formatDate(beginDate);
		}


		model.addAttribute("monthFirstDay", date);
		//获取人民币，开户币种默认人民币
		model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
		return "report/corplistCheckAllocation";
	}
	//////@RequiresPermissions(value = ResourceConstant.REPORT_ALLOCATIONSETTLEMENT_ACCLIST_QUERY)
	@GetMapping("/acclist")
	public String acclist(Model model){
		String date=DateUtils.formatDate(DateUtils.getMonthStartTime());
		model.addAttribute("monthFirstDay", date);
		//获取人民币，开户币种默认人民币
		model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
		return "report/acclistCheckAllocation";
	}
	//////@RequiresPermissions(value = ResourceConstant.REPORT_ALLOCATIONSETTLEMENT_ACCOUNTBANKLIST_QUERY)
	@GetMapping("/accountbanklist")
	public String accountbanklist(Model model){
		String date=DateUtils.formatDate(DateUtils.getMonthStartTime());
		model.addAttribute("monthFirstDay", date);
		//获取人民币，开户币种默认人民币
		model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
		return "report/banklistCheckAllocation";
	}
	//////@RequiresPermissions(value = ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY)
	@GetMapping("/banklist")
	public String banklist(Model model){
		String date=DateUtils.formatDate(DateUtils.getMonthStartTime());
		model.addAttribute("monthFirstDay", date);
		//获取人民币，开户币种默认人民币
		model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
		return "report/typelistCheckAllocation";
	}
	@SuppressWarnings("rawtypes")
	@PostMapping("/getData")
	@ResponseBody
	public DataTablesOutput getData(@RequestBody DataTablesInput input) throws ParseException{
		AllocationSettlementVO allocationSettlementVO=new AllocationSettlementVO();
		String type=(String)input.getFormdata().get("type");
	    String corpId=(String)input.getFormdata().get("corpId");
	    String bankAcc=(String)input.getFormdata().get("bankAcc");
	    String bankName=(String)input.getFormdata().get("bankName");
	    String bankType=(String)input.getFormdata().get("typeName");
	    String curId=(String)input.getFormdata().get("curId");
	    String sbeginDate=(String)input.getFormdata().get("beginDate");
	    String sendDate=(String)input.getFormdata().get("endDate");
	    String attributeId=(String)input.getFormdata().get("attributeId");
	    String natureId=(String)input.getFormdata().get("natureId");
	    String accType=(String)input.getFormdata().get("accType");
	    String orderPara=input.getOrderPara();
	    String orderByPara=input.getOrderByPara();
	    Integer length= input.getLength();
	    Integer start=input.getStart();
	    List<Order> list=input.getOrder();
	    Date beginDate= DateUtils.getIntegralStartTime(DateUtils.parse(sbeginDate, "yyyy-MM-dd"));
	    Date endDate= DateUtils.getIntegralEndTime(DateUtils.parse(sendDate, "yyyy-MM-dd"));
	    Integer draw= input.getDraw();
		allocationSettlementVO.setCorpId(corpId);
		allocationSettlementVO.setBankAcc(bankAcc);
		allocationSettlementVO.setCurId(curId);
		allocationSettlementVO.setBankName(bankName);
		allocationSettlementVO.setBankType(bankType);
		allocationSettlementVO.setBeginDate(beginDate);
		allocationSettlementVO.setEndDate(endDate);
		allocationSettlementVO.setDraw(draw);
		allocationSettlementVO.setType(type);
		allocationSettlementVO.setOrderPara(orderPara);
		allocationSettlementVO.setOrderByPara(orderByPara);
		allocationSettlementVO.setLength(length);
		allocationSettlementVO.setStart(start);
		allocationSettlementVO.setAttributeId(attributeId);
		allocationSettlementVO.setNatureId(natureId);
		allocationSettlementVO.setAccType(accType);
		String changeCur=(String)input.getFormdata().get("changeCur");
		String reducedCurId=(String)input.getFormdata().get("reducedCurId");
		String rateType=(String)input.getFormdata().get("rateType");
		Date rateDate= DateUtils.getIntegralEndTime(DateUtils.parse((String)input.getFormdata().get("rateDate"), "yyyy-MM-dd"));
		allocationSettlementVO.setChangeCur(changeCur);
		allocationSettlementVO.setReducedCurId(reducedCurId);
		allocationSettlementVO.setRateType(rateType);
		allocationSettlementVO.setRateDate(rateDate);
	    return allocationSettlementService.getData(allocationSettlementVO,this.getCurrentUser());
	}
	/**
	 * 收支明细详情页面
	 * @param vo
	 * @param model
	 * @return
	 */
	@GetMapping("/view")
	public String view(AllocationSettlementDtlVO vo, Model model) {
		model.addAttribute("beginDate", DateUtils.formatDate(vo.getBeginDate()));
		model.addAttribute("endDate", DateUtils.formatDate(vo.getEndDate()));
		model.addAttribute("dtlPage", vo);
		return "report/listAllocationSettlementDtl";
	}
	/**
	 * 收支明细列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return allocationSettlementService.getDataDtlByType(dataTablesInput, this.getCurrentUser());
	}
	/**
	 *币种转换校验
	 *@Param origCurId 原币种
	 *@Param convertCurId   转换币种
	 *@Param date 日期
	 *@Param isAllCur 是否查所有币种
	 *@return
	 */
	@PostMapping("/checkConvertCurId")
	@ResponseBody
	public AjaxJson checkConvertCurId(String origCurId, String convertCurId, String rateType, String rateDate){
		allocationSettlementService.checkConvertCurId(origCurId, convertCurId, rateType, rateDate);
		return success();
	}
}
