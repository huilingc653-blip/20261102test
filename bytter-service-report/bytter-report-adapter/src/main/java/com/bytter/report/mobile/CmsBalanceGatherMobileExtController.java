package com.bytter.report.mobile;

import com.bytter.report.domain.cms.service.CmsBalnceGatherService;
import com.bytter.report.domain.cms.vo.CmsBalanceGatherVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/mobile/report/cms/cmsbalancegather")
public class CmsBalanceGatherMobileExtController extends BaseController {

	@Autowired
	private CmsBalnceGatherService cmsBalnceGatherService;
	@Autowired(required = false)
	private IBtCurrencyService iBtCurrencyService;

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
			for(int i=0;i<list.size()-1;i++){
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
		return "mobile/report/balanceGather/mobileBalanceGather";
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
			for(int i=0;i<list.size()-1;i++){
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
		return "mobile/report/balanceGather/mobileBalanceDetail";
	}
}
