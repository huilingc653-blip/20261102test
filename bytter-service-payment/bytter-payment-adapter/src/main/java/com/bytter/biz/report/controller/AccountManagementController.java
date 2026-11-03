package com.bytter.biz.report.controller;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.report.service.AccountManagementService;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.biz.report.vo.NisCreateDate;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @author zhuop
 * @Description 资金归集、下拨情况汇总
 * @date 2018.6.1
 */
@Controller
@RequestMapping("/report/nisAllocationReq")
public class AccountManagementController extends BaseController{
	@Autowired
	private AccountManagementService accountManagementService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	 * nis_allocation_req 列表页面跳转
	 * start 第几页
	 * len 每页条数
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY)
	@LogMessage(content = "资金情况查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(NisAllocationReqVO vo,Model model,Integer start,Integer len){
		DataTablesInput input=new DataTablesInput();
		DataTablesOutput output=null;
		String option=vo.getOption();
		if(null == start){
			start=Integer.valueOf(1);
		}
		if(null !=len){
			input.setLength(len);//页面数据条数
		}
		input.setStart((start-1)*input.getLength());
		input.setDraw(start);
		if("query".equals(option)){
			if(StringUtils.isNotEmpty(vo.getGedCreateDate())){
				model.addAttribute("monthFirstDay", DateUtils.formatDate(vo.getGedCreateDate()));
			}else{
				model.addAttribute("monthFirstDay", "");
			}
			if(StringUtils.isNotEmpty(vo.getLedCreateDate())){
			   model.addAttribute("today", DateUtils.formatDate(vo.getLedCreateDate()));
			}else{
			   model.addAttribute("today", "");
			}
			queryParam(model, input, vo);
			output=accountManagementService.getNisAllocationReq(input,ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY , getCurrentUser());
		}else{
			String date=DateUtils.formatDate(DateUtils.getMonthStartTime());
			String today=DateUtils.formatDate(DateUtils.getDate());
			model.addAttribute("monthFirstDay", date);
			model.addAttribute("today", today);
			model.addAttribute("onload", "onload");
			output=new DataTablesOutput();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		long resultCount = output.getRecordsTotal();
		int pageSize = input.getLength();//每页多少条
		int pageCount = 0;
		if (resultCount % pageSize == 0) {
			//整除则直接取整相除
			pageCount = (int) (resultCount / pageSize); //总页数
		} else {
			//否则取整相除后加一
			pageCount = (int) ((resultCount / pageSize) + 1); //页数
		}
		result.put("nisAllocationReq", output.getData());
		result.put("total", output.getRecordsTotal());//总条数
		result.put("curr", start);//第几页
		result.put("len", input.getLength());// 每页条数
		model.addAttribute("model", result);
		//获取人民币，开户币种默认人民币
		model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
		return "report/listAccountManagement";
	}
	/**
	 * 查询参数
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY)
	public DataTablesInput queryParam(Model model,DataTablesInput input ,NisAllocationReqVO vo){
		if(StringUtils.isNotEmpty(vo.getCorpId())){
			input.addFilter(MatchTypeEnum.IN, "corpId", vo.getCorpId());
			model.addAttribute("corpId",vo.getCorpId());
		}
		if(StringUtils.isNotEmpty(vo.getNetId())){
			input.addFilter(MatchTypeEnum.IN, "netId", vo.getNetId());
			model.addAttribute("netId",vo.getNetId());
		}
		if(StringUtils.isNotEmpty(vo.getGedCreateDate())){
			input.addFilter(MatchTypeEnum.GE, "createDate", DateUtils.formatDate(vo.getGedCreateDate()),Date.class);
			model.addAttribute("gedCreateDate",DateUtils.date2Str(vo.getGedCreateDate(),DateUtils.date_sdf()));
		}
		if(StringUtils.isNotEmpty(vo.getLedCreateDate())){
			input.addFilter(MatchTypeEnum.LE, "createDate", DateUtils.formatDate(DateUtils.getIntegralEndTime(vo.getLedCreateDate()),"yyyy-MM-dd HH:mm:ss"), Date.class);
			model.addAttribute("ledCreateDate", DateUtils.date2Str(vo.getLedCreateDate(),DateUtils.date_sdf()));
		}
		if(StringUtils.isNotEmpty(vo.getChangeCur())){
			input.addFilter(MatchTypeEnum.EQ, "changeCur", vo.getChangeCur());
			model.addAttribute("changeCur",vo.getChangeCur());
		}else{
			model.addAttribute("changeCur", CommonConstant.NO);
		}
		if(StringUtils.isNotEmpty(vo.getRateType())){
			input.addFilter(MatchTypeEnum.EQ, "rateType", vo.getRateType());
			model.addAttribute("rateType",vo.getRateType());
		}
		if(StringUtils.isNotEmpty(vo.getReducedCurId())){
			input.addFilter(MatchTypeEnum.EQ, "reducedCurId", vo.getReducedCurId());
			model.addAttribute("reducedCurId",vo.getReducedCurId());
		}
		if(StringUtils.isNotEmpty(vo.getRateDate())){
			input.addFilter(MatchTypeEnum.LE, "rateDate", DateUtils.formatDate(DateUtils.getIntegralEndTime(vo.getRateDate()),"yyyy-MM-dd HH:mm:ss"), Date.class);
			model.addAttribute("rateDate", DateUtils.date2Str(vo.getRateDate(),DateUtils.date_sdf()));
		}else{
            model.addAttribute("rateDate", DateUtils.date2Str(DateUtils.getDateYMD(),DateUtils.date_sdf()));
		}
		return input;
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY)
	@GetMapping("/view")
	public String view(NisCreateDate nd,String curIds,String corpIds,String type,String status,Model model){
		model.addAttribute("curIds", curIds);
		model.addAttribute("corpIds",corpIds);
		model.addAttribute("type",type);
		model.addAttribute("status",status);
		model.addAttribute("createDate",nd.getCreateDate());
		model.addAttribute("gedCreateDate",nd.getGedCreateDate());
		model.addAttribute("ledCreateDate", nd.getLedCreateDate());
		return "report/viewDetail";
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return accountManagementService.getViewDetail(dataTablesInput);
	}
}
