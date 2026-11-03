package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.CmsBalnceGatherService;
import com.bytter.report.domain.cms.vo.CmsBalanceGatherVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 信贷融资余额汇总报表
 * @author lvwl
 */
@Controller
@RequestMapping("/cms/report/cmsbalancegather")
public class CmsBalanceGatherController extends BaseController {

	@Autowired
	private CmsBalnceGatherService cmsBalnceGatherService;
	@Autowired(required = false)
	@Qualifier("btCurrencyService")
	private IBtCurrencyService iBtCurrencyService;
	@Autowired(required = false)
	@Qualifier("corpService")
    private ICorpService iCorpService;

	/**余额汇总查询列表页面
	 * @throws ParseException */
	//@RequiresPermissions(ResourceConstant.CMS_REPORT_LOAN_BALANCEGATHER_QUERY)
	@RequestMapping("/gatherList")
	public String gatherList(CmsBalanceGatherVO vo,Model model,HttpSession httpSession) throws ParseException {
		List list = cmsBalnceGatherService.queryGatherList(vo,getUserId());
		model.addAttribute("balanceList", list);
		model.addAttribute("queryVO", vo);
		httpSession.setAttribute("balanceGatherList", list);//缓存查询集合
		return "cms/report/balanceGather/listBalanceGather";
	}

	/**
	 * 融资明细页面
	 */
	@RequestMapping("/detailList")
	public String detailList(CmsBalanceGatherVO vo,Model model){
		model.addAttribute("vo", vo);
		return "cms/report/balanceGather/listBalanceDetail";
	}

	/**
	 * 明细数据分页
	 * @return
	 */
	@PostMapping("/datatableUpdate")
    @ResponseBody
    public Object datatableUpdate(CmsBalanceGatherVO vo,@RequestBody DataTablesInput dataTablesInput){
		return cmsBalnceGatherService.queryDetailList(vo,dataTablesInput);
    }

	/**
	 * 获取缓存查询集合
	 * @return
	 */
	private List<Map<String, Object>> getQueryListBySession(CmsBalanceGatherVO vo,HttpSession httpSession){
		List<Map<String, Object>> list = (List<Map<String, Object>>) httpSession.getAttribute("balanceGatherList");//获取缓存查询集合
		if(EmptyUtils.isEmpty(list)){
			list = cmsBalnceGatherService.queryGatherList(vo,getUserId());
		}
		return list;
	}

	/**
	 * 饼状图查看页面
	 */
	@GetMapping("/gatherViewPir")
	public String gatherViewPir(CmsBalanceGatherVO vo,Model model,HttpSession httpSession) throws ParseException {
		List<Map<String, Object>> list = this.getQueryListBySession(vo,httpSession);//获取缓存查询集合
		model.addAttribute("vo", vo);
		model.addAttribute("list", cmsBalnceGatherService.getGatherLis(vo,list));
		return "cms/report/balanceGather/curreportDetail";
	}

	/**余额汇总查询列表页面*/
	@PostMapping("/gatherPieList")
	@ResponseBody
	public Object gatherPieList(CmsBalanceGatherVO vo, Model model, HttpSession httpSession) throws ParseException{

		List<Map<String, Object>> list = this.getQueryListBySession(vo,httpSession);//获取缓存查询集合;//获取缓存查询集合

		return cmsBalnceGatherService.createNetListPie(vo,list);
	}

	/**
	 * 首页饼状图查看页面
	 */
	@RequestMapping("/gatherViewPirPage")
	public String gatherViewPirPage(CmsBalanceGatherVO vo, Model model) throws ParseException {
		model.addAttribute("vo", vo);
		return "cms/report/balanceGather/curreportDetailPage";
	}


	@PostMapping("/gatherPieListPage")
	@ResponseBody
	public Object gatherPieListPage(CmsBalanceGatherVO vo) throws ParseException{
		List<Map<String, Object>> list = cmsBalnceGatherService.queryGatherList(new CmsBalanceGatherVO(),getUserId());//查询当月全部数据
		return cmsBalnceGatherService.createNetListPie(vo,list);//提取汇总折合人名币
	}



	/**余额汇总查询列表页面*/
	@PostMapping("/gatherLineList")
	@ResponseBody
	public Object gatherLineList(CmsBalanceGatherVO vo,Model model) throws ParseException{
		cmsBalnceGatherService.queryDetailList(vo,null);
		return cmsBalnceGatherService.createNetListLine(vo);
	}

	/**余额汇总APP
	 * @throws ParseException */
	@RequestMapping("/gatherApp")
	public String gatherApp(CmsBalanceGatherVO vo,Model model,HttpSession httpSession) throws ParseException {
		vo.setCorpId("");
	    if(vo == null){
	        /*CorpDTO corp = iCorpService.get(getCurrentUser().getCorpId());
	        if(null != corp){
                model.addAttribute("corpName",corp.getName());*/
                vo.setStartDate(DateUtils.getFirstYearDate(new Date()));
                vo.setEndDate(new Date());
            /*}*/
        }
		List<BtCurrencyDTO> curList = iBtCurrencyService.getBtCurrency();
		List list = cmsBalnceGatherService.queryGatherList(vo,getUserId());
		List<CmsBalanceGatherVO> tt = null;
		String curName = "人民币";
		String curId = null;
		if(!EmptyUtils.isEmptyList(list)){
			Map<String,Object> total = (Map<String,Object>)list.get(0);
			List s =  (List)total.get("curList");
			Map<String,Object> ss = (Map<String,Object>)s.get(s.size()-1);//折合人民币
			curName = (String)ss.get("curName");
			curId = (String)ss.get("curId");
			if(null == ss){
				ss = (Map<String,Object>)s.get(0);//人民币
			}else {
				tt = (List<CmsBalanceGatherVO>)ss.get("dataList");//汇总
			}
		}
		BigDecimal yesterBalance = BigDecimal.ZERO;
		BigDecimal earningMoney = BigDecimal.ZERO;//收入
		BigDecimal defrayMoney = BigDecimal.ZERO;//支出
		BigDecimal balance = BigDecimal.ZERO;
		if(!EmptyUtils.isEmptyList(tt)){
			for (CmsBalanceGatherVO v: tt) {
				v.setCurId(curId);
				yesterBalance = yesterBalance.add(v.getBeformMoney());
				earningMoney = earningMoney.add(v.getEarningMoney());
				defrayMoney = defrayMoney.add(v.getDefrayMoney());
				balance = balance.add(v.getBeformMoney().add(v.getEarningMoney()).subtract(v.getDefrayMoney()));
			}
		}

		List<CmsBalanceGatherVO> provideList = new ArrayList<>();
		List<CmsBalanceGatherVO> precativeList = new ArrayList<>();
		List<CmsBalanceGatherVO> loanList = new ArrayList<>();
		CmsBalanceGatherVO res = null;
		if(!EmptyUtils.isEmptyList(list)){
			for(int i=1;i<list.size();i++){
				Map<String,Object> item = (Map<String,Object>)list.get(i);
				List s =  (List)item.get("curList");
				//Map<String,Object> dtl = (Map<String,Object>)s.get(0);
				Map<String,Object> dtl = (Map<String,Object>)s.get(s.size()-1);
				List<CmsBalanceGatherVO> dtls = (List<CmsBalanceGatherVO>)dtl.get("dataList");
				for(CmsBalanceGatherVO v : dtls){
					v.setCurId(curId);
					BigDecimal bal = v.getBeformMoney().add(v.getEarningMoney()).subtract(v.getDefrayMoney());
					v.setPlMoney(bal.setScale(2, BigDecimal.ROUND_HALF_DOWN));
					if("02".equals(v.getLoanSign())){
						provideList.add(v);
					}
					if("01".equals(v.getLoanSign())){
						loanList.add(v);
					}
					if("03".equals(v.getLoanSign())){
						precativeList.add(v);
					}
				}
			}
		}
		model.addAttribute("yesterBalance", yesterBalance.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		model.addAttribute("earningMoney", earningMoney.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		model.addAttribute("defrayMoney", defrayMoney.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		model.addAttribute("balance", balance.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		model.addAttribute("searchDate",DateUtils.format(vo.getStartDate(),"yyyy-MM-dd")+"-"+DateUtils.format(vo.getEndDate(),"yyyy-MM-dd"));
		model.addAttribute("provideList", provideList);
		model.addAttribute("precativeList", precativeList);
		model.addAttribute("loanList", loanList);
		model.addAttribute("curList", curList);
		model.addAttribute("curName",curName);
		model.addAttribute("startDate",DateUtils.format(vo.getStartDate(),"yyyy-MM-dd"));
		model.addAttribute("endDate",DateUtils.format(vo.getEndDate(),"yyyy-MM-dd"));
		model.addAttribute("corpId",vo.getCorpId());
		return "cms/report/balanceGather/mobileBalanceGather";
	}

	@PostMapping("/getAppList")
	@ResponseBody
	public Object getAppList(CmsBalanceGatherVO vo,Model model) throws ParseException{
		if(vo == null){
		/*	CorpDTO corp = iCorpService.get(getCurrentUser().getCorpId());
			if(null != corp){
				model.addAttribute("corpName",corp.getName());
				vo.setCorpId(getCurrentUser().getCorpId());*/
				vo.setStartDate(DateUtils.getFirstYearDate(new Date()));
				vo.setEndDate(new Date());
			/*}*/
		}
		List<BtCurrencyDTO> curList = iBtCurrencyService.getBtCurrency();
		List list = cmsBalnceGatherService.queryGatherList(vo,getUserId());
		List<CmsBalanceGatherVO> tt = null;
		String curName = "人民币";
		String curId = null;
		if(!EmptyUtils.isEmptyList(list)){
			Map<String,Object> total = (Map<String,Object>)list.get(0);
			List s =  (List)total.get("curList");
			Map<String,Object> ss = (Map<String,Object>)s.get(s.size()-1);//折合人民币
			curName = (String)ss.get("curName");
			curId = (String)ss.get("curId");
			if(null == ss){
				ss = (Map<String,Object>)s.get(0);//人民币
			}else {
				tt = (List<CmsBalanceGatherVO>)ss.get("dataList");//汇总
			}
		}
		BigDecimal yesterBalance = BigDecimal.ZERO;
		BigDecimal earningMoney = BigDecimal.ZERO;//收入
		BigDecimal defrayMoney = BigDecimal.ZERO;//支出
		BigDecimal balance = BigDecimal.ZERO;
		if(!EmptyUtils.isEmptyList(tt)){
			for (CmsBalanceGatherVO v: tt) {
				yesterBalance = yesterBalance.add(v.getBeformMoney());
				earningMoney = earningMoney.add(v.getEarningMoney());
				defrayMoney = defrayMoney.add(v.getDefrayMoney());
				balance = balance.add(v.getBeformMoney().add(v.getEarningMoney()).subtract(v.getDefrayMoney()));
			}
		}

		List<CmsBalanceGatherVO> provideList = new ArrayList<>();
		List<CmsBalanceGatherVO> precativeList = new ArrayList<>();
		List<CmsBalanceGatherVO> loanList = new ArrayList<>();
		CmsBalanceGatherVO res = null;
		if(!EmptyUtils.isEmptyList(list)){
			for(int i=1;i<list.size();i++){
				Map<String,Object> item = (Map<String,Object>)list.get(i);
				List s =  (List)item.get("curList");
				//Map<String,Object> dtl = (Map<String,Object>)s.get(0);
				Map<String,Object> dtl = (Map<String,Object>)s.get(s.size()-1);
				List<CmsBalanceGatherVO> dtls = (List<CmsBalanceGatherVO>)dtl.get("dataList");
				for(CmsBalanceGatherVO v : dtls){
					v.setCurId(curId);
					BigDecimal bal = v.getBeformMoney().add(v.getEarningMoney()).subtract(v.getDefrayMoney());
					v.setPlMoney(bal.setScale(2, BigDecimal.ROUND_HALF_DOWN));
					if("02".equals(v.getLoanSign())){
						provideList.add(v);
					}
					if("01".equals(v.getLoanSign())){
						loanList.add(v);
					}
					if("03".equals(v.getLoanSign())){
						precativeList.add(v);
					}
				}
			}
		}
		CmsBalanceGatherVO rest = new CmsBalanceGatherVO();
		rest.setBeformMoney(yesterBalance.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		rest.setEarningMoney(earningMoney.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		rest.setDefrayMoney(defrayMoney.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		rest.setBalance(balance.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		rest.setSearchDate(DateUtils.format(vo.getStartDate(),"yyyy-MM-dd")+"-"+DateUtils.format(vo.getEndDate(),"yyyy-MM-dd"));
		rest.setProvideList(provideList);
		rest.setPrecativeList(precativeList);
		rest.setLoanList(loanList);
		rest.setCurName(curName);
		rest.setStartDate(vo.getStartDate());
		rest.setEndDate(vo.getEndDate());
		rest.setLoanSign(vo.getLoanSign());
		return rest;
	}

	/**
	 * 明细数据分页
	 * @return
	 */
	@RequestMapping("/detailListApp")
	public String detailListApp(CmsBalanceGatherVO vo,Model model){
		List<CmsBalanceGatherVO> dtls = (List<CmsBalanceGatherVO>)cmsBalnceGatherService.queryDetailList(vo,null);
		model.addAttribute("list",dtls);
		model.addAttribute("loanSign",vo.getLoanSign());
		return "cms/report/balanceGather/mobileBalanceDetail";
	}
}
