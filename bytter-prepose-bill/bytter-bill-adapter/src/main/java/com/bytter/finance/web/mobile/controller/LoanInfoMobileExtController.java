package com.bytter.finance.web.mobile.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BaseDataConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.CmsLoanBiBusinessVo;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanQueryVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 借款信息表
 * @author onlineGenerator
 * @date 2017-07-20 10:45:09
 * @version 1.0
 */
@Controller
@RequestMapping("/mobile/cms/provide/loaninfo")
public class LoanInfoMobileExtController extends BaseController {

	@Autowired
	private LoanInfoService loanInfoService;
	@Autowired
	private CmsProvideLoanRepayService cmsProvideLoanRepayService;
	@Autowired
	private CmsProvideLoanSendService cmsProvideLoanSendService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;


	/**
	 * 初始化资金计划系统参数
	 * @param model
	 */
	private void initConfig(Model model){
		//0为不挂接资金计划  现在需求要求不挂接
		model.addAttribute("budgetSel", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
		model.addAttribute("sendItem", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
	}

	/**
	 * 新增的相关银行APP借款查询的报表
	* 借款查询-app报表页面跳转
	*/
	@LogMessage(content = "查询手机借款信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@RequestMapping("/loanMobileQuery")
	public String mobileQuery(Model model) {
		List<BtCurrencyDTO> list= btCurrencyService.getBtCurrency();
		model.addAttribute("curList", list);
        return "mobile/cms/provide/mobileCmsLoanQuery";
	}


	/**
	 * 新增的相关银行APP借款查询的报表
	* 借款查询-app报表页面跳转
	*/
	@LogMessage(content = "查询手机借款信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/gcMoblile")
	public String gcMoblile(Model model) {
		List<BtCurrencyDTO> list= btCurrencyService.getBtCurrency();
		model.addAttribute("curList", list);
        return "mobile/cms/provide/mobileCmsLoanQuery";
	}


	/**
	 * 新增的相关银行APP借款查询的报表
	* 借款查询-app报表页面跳转
	*/
	@PostMapping("/getAppData")
	@ResponseBody
    public Object getAppData(CmsLoanBiBusinessVo searchParams){
	    DataTablesInput dataTablesInput = new DataTablesInput();
	    //无分页的情况下设置数据
	    dataTablesInput.setLength(10000);
	    //查询条件
	    Map<String,Object> queryParms = new LinkedHashMap<String,Object>();
		if (null != searchParams) {
			if (!StringUtils.isEmpty(searchParams.getCorpIds())) {
				queryParms.put("ins_corpId", searchParams.getCorpIds());
			}
			if (!StringUtils.isBlank(searchParams.getCurId())) {
				queryParms.put("eqs_curId", searchParams.getCurId());
			}

			if (!StringUtils.isBlank(searchParams.getSendCode())) {
				queryParms.put("likes_infoBillCode", searchParams.getSendCode());
			}
			if (StringUtils.isNotEmpty(searchParams.getSendBeginDate())) {
				queryParms.put("ged_sendDate", DateUtils.formatDate(searchParams.getSendBeginDate()));
			}

			if (StringUtils.isNotEmpty(searchParams.getSendEndDate())) {
				queryParms.put("led_sendDate", DateUtils.formatDate(searchParams.getSendEndDate()));
			}

			if (StringUtils.isNotEmpty(searchParams.getOverBeginDate())) {
				queryParms.put("ged_repayDate", DateUtils.formatDate(searchParams.getOverBeginDate()));
			}
			if (StringUtils.isNotEmpty(searchParams.getOverEndDate())) {
				queryParms.put("led_repayDate", DateUtils.formatDate(searchParams.getOverEndDate()));
			}
		}
	    dataTablesInput.setFormdata(queryParms);
		@SuppressWarnings("rawtypes")
		DataTablesOutput out = loanInfoService.queryLoanForMobile(dataTablesInput,this.getCurrentUser());
		@SuppressWarnings("unchecked")
		Object jsonObj = loanInfoService.appJsonObject(out.getData());
        return jsonObj;
    }

	/**
	 * 借款详情页面
	 */
	@RequestMapping("/loanMobileDetail")
	public String loanMobileDetail(String businessId,String sendCode,Model model) {
		if (!EmptyUtils.isEmpty(businessId)) {
			LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(businessId);
			model.addAttribute("vo", infoVO);
			BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendCode, "1");//在途金额
			BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendCode, "2");//已还金额
			model.addAttribute("repayPassage", repayPassage);
			model.addAttribute("repayFinish", repayFinish);
			LoanSendVO sendVO = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendCode);
			model.addAttribute("sendVO", sendVO);
			DataTablesInput dataTablesInput = new DataTablesInput();
			  //无分页的情况下设置数据
		    dataTablesInput.setLength(1);
			Map<String,Object> queryParms = new LinkedHashMap<String,Object>();
		    queryParms.put("likes_infoBillCode",businessId);
			dataTablesInput.getFormdata().put("likes_infoBillCode", businessId);
			@SuppressWarnings("rawtypes")
			DataTablesOutput out = loanInfoService.queryLoanForMobile(dataTablesInput,this.getCurrentUser());
			@SuppressWarnings("unchecked")
			List<LoanQueryVO> list = out.getData();
			model.addAttribute("queryVo", ListUtils.isNotNullOrEmpty(list)?(LoanQueryVO)list.get(0):null);
			model.addAttribute("loanCode", businessId);
			model.addAttribute("sendCode", sendCode);
		}
        return "mobile/cms/provide/mobileCmsLoanQueryInfo";
	}

	/**
	 * 借款利息明细
	 */
	@RequestMapping("/loanMobileRatInfo")
	public String loanMobileRatInfo(String businessId,String sendCode,Model model) {
		if (!EmptyUtils.isEmpty(businessId)) {
			loanInfoService.loadRepayWithSendBillCode(sendCode, model);
		}
        return "mobile/cms/provide/mobileCmsLoanQueryRatInfo";
	}

}
