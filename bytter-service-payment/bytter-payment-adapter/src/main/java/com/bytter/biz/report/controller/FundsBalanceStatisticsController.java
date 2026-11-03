package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.FundsBalanceStatisticsService;
import com.bytter.biz.report.vo.FundsBalanceStatisticsVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author zuoy
 * @Description 资金余额统计表
 */
@Controller
@RequestMapping("/report/fundsbalancestatistics")
public class FundsBalanceStatisticsController extends BaseController {
	@Autowired
	private FundsBalanceStatisticsService fundsBalanceStatisticsService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IDictService dictService;
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_FUNDSBALANCESTATISTICS_QUERY)
	@GetMapping("/list")
	public String list(Model model) {
		//获取人民币，开户币种默认人民币
		model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
		//获取银行存款类型
		List<DictDTO> dictList = dictService.list("btBankAcc.accType");
		model.addAttribute("dictList",dictList);
		model.addAttribute("size", dictList.size()+1);
		return "report/listFundsBalanceStatistics";
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_FUNDSBALANCESTATISTICS_QUERY)
	@LogMessage(content = "资金余额统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/loadData")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		 return  fundsBalanceStatisticsService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**手机**/
	@PostMapping("/mobileBalanceList")
	public String mobileBalanceList(Model model,String corpId) {
		List<BtCurrencyDTO> curList =  fundsBalanceStatisticsService.getBtCurrency();
		model.addAttribute("corpId",corpId);
		model.addAttribute("curList", curList);
		return "report/mobileBalanceList";
	}
	/**余额汇总查询列表页面*/
	@PostMapping("/gatherPieList")
	@ResponseBody
	public Object gatherPieList(String curId, String corpId,String startDate,String endDate,String corpName,String curName,Model model){
		List<FundsBalanceStatisticsVO>  list = fundsBalanceStatisticsService.getData(curId, corpId, startDate,endDate,getUserId());
		return  fundsBalanceStatisticsService.getPieData(list,corpName,curName);
	}
	/**余额汇总查询列表页面*/
	@PostMapping("/gatherPieListBank")
	@ResponseBody
	public Object gatherPieListBank(String curId, String corpId,String startDate,String endDate,String corpName,Model model){
		List<FundsBalanceStatisticsVO> list = fundsBalanceStatisticsService.getData(curId, corpId, startDate,endDate,getUserId());
		return fundsBalanceStatisticsService.getPieDataBank(list,corpName);
	}
	/**余额汇总查询列表页面*/
	@PostMapping("/gatherBarList")
	@ResponseBody
	public Object gatherBarList(String curId, String corpId,String startDate,String endDate,String corpName,String curName,Model model){
		List<FundsBalanceStatisticsVO> list = fundsBalanceStatisticsService.getData(curId, corpId, startDate,endDate,getUserId());
		return fundsBalanceStatisticsService.getBarData(list,corpName,curName);
	}
	/**
	 * 资金余额表报表
	 * @param model
	 * @param netId
	 * @param corpId
	 * @return
	 * @throws
	 */
	@RequestMapping("/mobileBalance")
	public String mobileAllocation(Model model,String netId,String corpId) {
		String startDate = DateUtils.formatDate(DateUtils.addYear(DateUtils.getDate(),-1));
		String endDate = DateUtils.formatDate(DateUtils.getDate());
		String natualId = btCurrencyService.getNatualCurrency().getId();
		List<BtCurrencyDTO> curList = fundsBalanceStatisticsService.getBtCurrency();
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("natualId", natualId);
		model.addAttribute("curList", curList);
		model.addAttribute("netId", netId);
		model.addAttribute("corpId", corpId);
		Map<String, BigDecimal> subMap = fundsBalanceStatisticsService.subMap("", "", startDate, endDate,getUserId());
		model.addAttribute("subMap", subMap);
		Map<String,String> innerMap = new HashMap<>();
		Map<String,String> bankMap = new HashMap<>();
		Map<String,String> billMap = new HashMap<>();
		for(Map.Entry<String,BigDecimal> info : subMap.entrySet()){
			String[] key = info.getKey().split(";");
			if(!EmptyUtils.isEmpty(key)){
				if("inner".equals(key[0])){
					innerMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bank".equals(key[0])){
					bankMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bill".equals(key[0])){
					billMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
			}
		}
		model.addAttribute("innerMap", innerMap);
		model.addAttribute("bankMap", bankMap);
		model.addAttribute("billMap", billMap);
		model.addAttribute("startDateStr", DateUtils.formatDate(DateUtils.addYear(DateUtils.getDate(),-1),"yyyy.MM.dd"));
		model.addAttribute("endDateStr", DateUtils.formatDate(DateUtils.getDate(),"yyyy.MM.dd"));
		return "report/balanceMobile";
	}
	/**
	 * 资金余额表报表
	 * @param model
	 * @param netId
	 * @param corpId
	 * @return
	 * @throws
	 */
	@GetMapping("/mobileBalance1")
	public String mobileBalance1(Model model,String netId,String corpId) {
		String startDate = DateUtils.formatDate(DateUtils.addYear(DateUtils.getDate(),-1));
		String endDate = DateUtils.formatDate(DateUtils.getDate());
		String natualId = btCurrencyService.getNatualCurrency().getId();
		List<BtCurrencyDTO> curList = fundsBalanceStatisticsService.getBtCurrency();
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("natualId", natualId);
		model.addAttribute("curList", curList);
		model.addAttribute("netId", netId);
		model.addAttribute("corpId", corpId);
		Map<String, BigDecimal> subMap = fundsBalanceStatisticsService.subMap("", "", startDate, endDate,getUserId());
		model.addAttribute("subMap", subMap);
		Map<String,String> innerMap = new HashMap<>();
		Map<String,String> bankMap = new HashMap<>();
		Map<String,String> billMap = new HashMap<>();
		for(Map.Entry<String,BigDecimal> info : subMap.entrySet()){
			String[] key = info.getKey().split(";");
			if(!EmptyUtils.isEmpty(key)){
				if("inner".equals(key[0])){
					innerMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bank".equals(key[0])){
					bankMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bill".equals(key[0])){
					billMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
			}
		}
		model.addAttribute("innerMap", innerMap);
		model.addAttribute("bankMap", bankMap);
		model.addAttribute("billMap", billMap);
		model.addAttribute("startDateStr", DateUtils.formatDate(DateUtils.addYear(DateUtils.getDate(),-1),"yyyy.MM.dd"));
		model.addAttribute("endDateStr", DateUtils.formatDate(DateUtils.getDate(),"yyyy.MM.dd"));
		return "report/balanceMobile";
	}
	/**
	 * 资金余额表数据
	 */
	@PostMapping("/getInfoList")
	@ResponseBody
	public Object getInfoList(Model model, FundsBalanceStatisticsVO vo, String startDate, String endDate) {
		if(StringUtils.isNotBlank(startDate)){
			startDate = DateUtils.formatDate(DateUtils.addYear(DateUtils.getDate(),-1));
		}
		if(StringUtils.isNotBlank(endDate)){
			endDate = DateUtils.formatDate(DateUtils.getDate());
		}
		List<FundsBalanceStatisticsVO> list = fundsBalanceStatisticsService.getAppData(vo.getCurId(), vo.getCorpId(), startDate,endDate,this.getCurrentUser(),vo.getUnit());
		Map<String, BigDecimal> total = fundsBalanceStatisticsService.getTotal(list);
		Map<String,Object> returnObj = new HashMap<String,Object>();
		returnObj.put("list",list);
		returnObj.put("total",total);
		Map<String, BigDecimal> subMap = fundsBalanceStatisticsService.subMap("", vo.getCorpId(), startDate, endDate,getUserId());
		model.addAttribute("subMap", subMap);
		Map<String,String> innerMap = new HashMap<>();
		Map<String,String> bankMap = new HashMap<>();
		Map<String,String> billMap = new HashMap<>();
		for(Map.Entry<String,BigDecimal> info : subMap.entrySet()){
			String[] key = info.getKey().split(";");
			if(!EmptyUtils.isEmpty(key)){
				if("inner".equals(key[0])){
					innerMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bank".equals(key[0])){
					bankMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bill".equals(key[0])){
					billMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
			}
		}
		String unitName = "金额单位：元";
		if(vo.getUnit() == 10000){
			unitName = "金额单位：万元";
		}
		returnObj.put("innerMap",innerMap);
		returnObj.put("bankMap",bankMap);
		returnObj.put("billMap",billMap);
		returnObj.put("unitName",unitName);
		return returnObj;
	}
	/**
	 * 资金余额表数据
	 */
	@GetMapping("/getInfoList1")
	@ResponseBody
	public Object getInfoList1(Model model, FundsBalanceStatisticsVO vo, String startDate, String endDate) {
		if(StringUtils.isNotBlank(startDate)){
			startDate = DateUtils.formatDate(DateUtils.addYear(DateUtils.getDate(),-1));
		}
		if(StringUtils.isNotBlank(endDate)){
			endDate = DateUtils.formatDate(DateUtils.getDate());
		}
		List<FundsBalanceStatisticsVO> list = fundsBalanceStatisticsService.getAppData(vo.getCurId(), vo.getCorpId(), startDate,endDate,this.getCurrentUser(),vo.getUnit());
		Map<String, BigDecimal> total = fundsBalanceStatisticsService.getTotal(list);
		Map<String,Object> returnObj = new HashMap<String,Object>();
		returnObj.put("list",list);
		returnObj.put("total",total);
		Map<String, BigDecimal> subMap = fundsBalanceStatisticsService.subMap("", vo.getCorpId(), startDate, endDate,getUserId());
		model.addAttribute("subMap", subMap);
		Map<String,String> innerMap = new HashMap<>();
		Map<String,String> bankMap = new HashMap<>();
		Map<String,String> billMap = new HashMap<>();
		for(Map.Entry<String,BigDecimal> info : subMap.entrySet()){
			String[] key = info.getKey().split(";");
			if(!EmptyUtils.isEmpty(key)){
				if("inner".equals(key[0])){
					innerMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bank".equals(key[0])){
					bankMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
				if("bill".equals(key[0])){
					billMap.put(key[1], NumberUtil.formatNumeric(info.getValue()));
				}
			}
		}
		returnObj.put("innerMap",innerMap);
		returnObj.put("bankMap",bankMap);
		returnObj.put("billMap",billMap);
		return returnObj;
	}
}
