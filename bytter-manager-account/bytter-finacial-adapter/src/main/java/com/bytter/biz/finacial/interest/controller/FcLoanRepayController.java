package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.interest.bean.LoanRepayAddBean;
import com.bytter.biz.finacial.interest.dto.FcLoanRepayDto;
import com.bytter.biz.finacial.interest.entity.FcLoanRepay;
import com.bytter.biz.finacial.interest.service.FcInterestService;
import com.bytter.biz.finacial.interest.service.FcLoanRepayService;
import com.bytter.biz.finacial.interest.vo.FcLessPayBillVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: FC_LOAN_REPAY
 * @author onlineGenerator
 * @date 2017-08-31 10:08:43
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/interest/fcloanrepay")
public class FcLoanRepayController extends BaseController {
	@Autowired
	private FcLoanRepayService fcLoanRepayService;
	@Autowired
	private FcInterestService fcInterestService;
	@Resource
    private IMultiLangService multiLangServiceI;
	/**
	* FC_LOAN_REPAY 新增列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("finacial.interest:fcloanrepay:index")
	@LogMessage(content = "查询FC_LOAN_REPAY列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/interest/listFcLoanRepay";
	}
	
	/**
	* FC_LOAN_REPAY 复核列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询FC_LOAN_REPAY列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listCheck")
	public String listCheck(Model model) {
        return "finacial/interest/listCheckFcLoanRepay";
	}
    /**
    * FC_LOAN_REPAY table列表暂存
    *
    * @return
    */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcLoanRepayService.listFcLoanRepay(dataTablesInput,getUserId());
    }
    
    /**
     * FC_LOAN_REPAY table列表复核
     *
     * @return
     */
    @PostMapping("/Checkdatatable")
    @ResponseBody
    public DataTablesOutput Checkdatatable(@RequestBody  DataTablesInput dataTablesInput){
		return fcLoanRepayService.listCheckFcLoanRepay(dataTablesInput,getCurrentUser());
    }
    
    /**
     * <b>功能介绍：</b>根据单位查询放款账号<br>
     * <b>日期：</b>2017年9月8日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param corpId
     * @return
     */
    @GetMapping("/getLoanAccByCorpCode")
    @ResponseBody
    public List<Map<String,Object>> getLoanAccByCorpCode(String corpId){
    	return fcLoanRepayService.getLoanAccByCorpCode(corpId);
    }
    
    /**
     * <b>功能介绍：</b>根据贷款账号查询单位活期存款账户<br>
     * <b>日期：</b>2017年9月4日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param loanAcc
     * @return
     */
    @GetMapping("/getCorpCurAccInfo")
    @ResponseBody
    public List<Map<String,Object>> getCorpCurAccInfo(String loanAcc){
    	return fcLoanRepayService.getCorpCurAccInfo(loanAcc);
    }
    
    /**
     * <b>功能介绍：</b>获取账号余额<br>
     * <b>日期：</b>2017年9月8日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param account
     * @return
     */
    @RequestMapping(params = "getAccountBalance")
	@ResponseBody
	public AjaxJson getAccountBalance(String account){
		AjaxJson json = new AjaxJson();
		BigDecimal checkBalance = fcLoanRepayService.getAccountBalance(account);
		Map<String,Object> result =new HashMap<String,Object>();
		result.put("checkBalance", checkBalance);
		result.put("accName", fcLoanRepayService.getaccNameByaccount(account));
		json.setObj(result);
		json.setSuccess(true);
		return json;
	}
    
	/**
	* FC_LOAN_REPAY新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("finacial.interest:fcloanrepay:save")
	@GetMapping("/add")
	public String add(FcLoanRepay fcLoanRepay, Model model) {
		String initNet = fcInterestService.getInitNetId(FinacialResourceConstant.FINACIAL_INTEREST_FCLOANREPAY_HAND,getUserId());
		Date closeDate = DateUtils.addDay(fcInterestService.getDayOverDate(initNet), 1);
		model.addAttribute("netId", initNet);
		model.addAttribute("closeDate", DateUtils.date_sdf().format(closeDate));
		return "finacial/interest/addFcLoanRepay";
	}
	
	/**
	 * <b>功能介绍：</b>选择还款明细跳转<br>
	 * <b>日期：</b>2017年9月1日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	@GetMapping("/selectRepayDtl")
	public String selectRepayDtl(String acc,Model model){
		model.addAttribute("acc", acc);
		return "finacial/interest/listFcLoanRepayDtls";
	}
	
	/**
	 * <b>功能介绍：</b>查询待归还账户明细<br>
	 * <b>日期：</b>2017年9月5日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param acc
	 * @return
	 */
	@PostMapping("/queryRepayDetailByAcc")
    @ResponseBody
    public List<FcLessPayBillVO> queryRepayDetailByAcc(String acc){
		return fcLoanRepayService.queryRepayDetailByAcc(acc);
	}
	
	
	/**
	* FC_LOAN_REPAY编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(FcLoanRepay fcLoanRepay, Model model) {
		String repayId = fcLoanRepay.getId();
		if (StringUtils.isNotEmpty(repayId)) {
			FcLoanRepayDto fcLoanRepayDto = fcLoanRepayService.queryById(repayId);
			model.addAttribute("fcLoanRepayPage", fcLoanRepayDto);
			FcLessPayBillVO lessPayBill = fcLoanRepayService.getFcLessPayBillByFcLoanRepay(repayId);
			model.addAttribute("lessPayBill", lessPayBill);
			FcAccbookDTO book = fcLoanRepayService.findAccbookByOrderNo(fcLoanRepayDto.getInterestOrderNo());
			model.addAttribute("vourcherType", book.getVoucherType());
			String summaryContent = multiLangServiceI.getLang("loanRepay.accbook.content");//应收利息还款利息
			model.addAttribute("summaryContent", summaryContent);
			model.addAttribute("checkBalance", fcLoanRepayService.getAccountBalance(fcLoanRepayDto.getRepayAcc()));
			model.addAttribute("debitaccName", fcLoanRepayService.getaccNameByaccount(fcLoanRepayDto.getRepayAcc()));
			model.addAttribute("creditaccName", fcLoanRepayService.getaccNameByaccount(fcLoanRepayDto.getNeedInterestAcc()));
			Date closeDate = DateUtils.addDay(fcInterestService.getDayOverDate(fcLoanRepayDto.getNetId()), 1);
			model.addAttribute("closeDate", DateUtils.date_sdf().format(closeDate));
		}
		return "finacial/interest/updateFcLoanRepay";
	}
	
	/**
	* FC_LOAN_REPAY复核页面跳转
	*
	* @return
	*/
	@GetMapping("/check")
	public String check(FcLoanRepay fcLoanRepay, Model model) {
		String repayId = fcLoanRepay.getId();
		if (StringUtils.isNotEmpty(repayId)) {
			FcLoanRepayDto fcLoanRepayDto = fcLoanRepayService.queryById(repayId);
//			fcLoanRepay = fcLoanRepayService.get(repayId, true);
			model.addAttribute("loanRepayPage", fcLoanRepayDto);
			model.addAttribute("fcLoanRepayPage", fcLoanRepayService.getFcLoanRepayById(repayId));
			FcLessPayBillVO lessPayBill = fcLoanRepayService.getFcLessPayBillByFcLoanRepay(repayId);
			model.addAttribute("lessPayBill", lessPayBill);
			FcAccbookDTO book = fcLoanRepayService.findAccbookByOrderNo(fcLoanRepayDto.getInterestOrderNo());
			model.addAttribute("vourcherType", book.getVoucherType());
			String summaryContent = multiLangServiceI.getLang("loanRepay.accbook.content");//应收利息还款利息
			model.addAttribute("summaryContent", summaryContent);
			model.addAttribute("checkBalance", fcLoanRepayService.getAccountBalance(fcLoanRepayDto.getRepayAcc()));
			model.addAttribute("debitaccName", fcLoanRepayService.getaccNameByaccount(fcLoanRepayDto.getRepayAcc()));
			model.addAttribute("creditaccName", fcLoanRepayService.getaccNameByaccount(fcLoanRepayDto.getNeedInterestAcc()));
		}
		return "finacial/interest/checkFcLoanRepay";
	}
	
	/**
	* FC_LOAN_REPAY详情页
	*
	* @return
	*/
	@LogMessage(content = "查询FC_LOAN_REPAY详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcLoanRepay fcLoanRepay = fcLoanRepayService.get(id);
		model.addAttribute("fcLoanRepayPage", fcLoanRepay);
		return "finacial/interest/viewFcLoanRepay";
	}
	/**
	 * 添加FC_LOAN_REPAY
	 * @param loanRepayBean
	 * @return
	 */
	@LogMessage(content = "添加FC_LOAN_REPAY",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(LoanRepayAddBean loanRepayBean){
		fcLoanRepayService.saveFcLoanRepay(loanRepayBean,getCurrentUser());
		return success();
	}
	/**
	* 修改FC_LOAN_REPAY
	* @param fcLoanRepay
	* @return
	*/
	@LogMessage(content = "修改FC_LOAN_REPAY",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcLoanRepay fcLoanRepay,LoanRepayAddBean loanRepayBean){
		FcLoanRepay t = fcLoanRepayService.get(fcLoanRepay.getId());
		BeanUtils.copyNotNullProperties(fcLoanRepay, t);
		fcLoanRepayService.deleteFcLoanRepay(t,getUserId());//先删除
		fcLoanRepayService.saveFcLoanRepay(loanRepayBean,getCurrentUser());//再保存
		return  success();
	}
	
	/**
	* 复核FC_LOAN_REPAY
	* @param fcLoanRepay
	* @return
	*/
	@PostMapping("/checkBack")
	@ResponseBody
	public AjaxJson checkBack(FcLoanRepay fcLoanRepay){
		FcLoanRepay repay = fcLoanRepayService.get(fcLoanRepay.getId(),true);
		if(fcLoanRepay.getStatus() == 0){//打回处理
			repay.setStatus(CommonConstant.STATUE_BACK);
			repay.setRefuseWhy(fcLoanRepay.getRefuseWhy());
		}else{
			repay.setStatus(CommonConstant.STATUE_CHECK);
			repay.setCheckerCode(getUserLoginName());
		}
		fcLoanRepayService.checkFcLoanRepay(repay,getUserId());//复核数据
		return  success();
	}
	
	
	/**
	* 删除FC_LOAN_REPAY
	* @param id
	* @return
	*/
	////@RequiresPermissions("finacial.interest:fcloanrepay:delete")
	@LogMessage(content = "删除FC_LOAN_REPAY",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcLoanRepay fcLoanRepay = fcLoanRepayService.get(id,true);
		fcLoanRepayService.deleteFcLoanRepay(fcLoanRepay,getUserId());
		return success();
	}
	/**
	* 批量删除FC_LOAN_REPAY
	* @param ids
	* @return
	*/
	////@RequiresPermissions("finacial.interest:fcloanrepay:delete")
	@LogMessage(content = "批量删除FC_LOAN_REPAY",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcLoanRepay fcLoanRepay = fcLoanRepayService.get(id);
			fcLoanRepayService.deleteEntity(fcLoanRepay);
		}
		return success();
	}
	
	/**
	 * <b>功能介绍：</b>获取日结日期<br>
	 * <b>日期：</b>2017年12月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param netId
	 * @return
	 */
	@RequestMapping(params = "getDayCloseDate")
	@ResponseBody
	public AjaxJson getDayCloseDate(String netId){
		AjaxJson json=new AjaxJson();
		Date dayCloseDate = new Date();
        if (EmptyUtils.isNotEmpty(netId)) {
            dayCloseDate = fcLoanRepayService.getNetDayOverDate(netId);
        }
	    json.setObj(DateUtils.date_sdf().format(DateUtils.addDay(dayCloseDate, 1)));
		json.setSuccess(true);
		return json;
	}
}
