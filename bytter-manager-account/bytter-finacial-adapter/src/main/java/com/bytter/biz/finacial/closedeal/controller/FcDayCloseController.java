package com.bytter.biz.finacial.closedeal.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import com.bytter.biz.finacial.closedeal.bean.FcAccInitCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcAccbookCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcFreezeCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcOutAccbookCheckBean;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.biz.finacial.closedeal.service.FcDayCloseService;
import com.bytter.biz.finacial.closedeal.vo.FcDayCloseVO;
import com.bytter.biz.payment.api.dto.FcCorpPoolDTO;
//import com.bytter.biz.payment.api.service.IFcCorpPoolService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.dto.CmsLoanSendOutCheckDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendCheckDTO;
import com.bytter.finance.dto.CmsProvideLoanSendCheckDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: 期终处理日结  
 * @Description: FC_DAY_CLOSE
 * @author onlineGenerator
 * @date 2017-07-06 14:51:02
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/closedeal/fcdayclose")
@Slf4j
public class FcDayCloseController extends BaseController {
	@Autowired
	private FcDayCloseService fcDayCloseService;
	
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	/**
	* 跳转日结页面
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
    @LogMessage(content = "跳转FC_DAY_CLOSE处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toDayClose")
	public String toDayClose(FcDayClose fcDayClose, Model model) {
		//获取用户的网点,设置默认网点日结日期
    	List<String> corpDTOs = corpService.getUserNetIds(ResourceConstant.FCL_CLOSE_COMMON_APPLY, 
    			CorpConstant.CORP_TYPE_CODE_NET, getUserId());//查询所有网点
    	if(!EmptyUtils.isEmptyList(corpDTOs)){
    		//取第一个网点
    		String netCode = corpDTOs.get(0);
    		fcDayClose = fcDayCloseService.getDayCloseDate(netCode);//获取网点日结日期
    		
    		FcDayCloseVO closeVO = new FcDayCloseVO();
    		closeVO.setNetCode(netCode);
    		Date endDate = DateUtils.addDay(fcDayClose.getCloseDate(), 1);
    		closeVO.setLastCloseDate(fcDayClose.getCloseDate());//已日结日期
    		closeVO.setCloseDate(endDate);//本次日结日期即工作日日期
    		closeVO.setCloseDay(new Integer(1));
    		model.addAttribute("dayClose", closeVO);
    	}
		return "finacial/closedeal/dayclose/addFcDayClose";
	}
	/**
	 * 跳转日结页面
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_CLOSEDEAL_TOPOOLDAYCLOSE_APPLY)
	@LogMessage(content = "跳转FC_DAY_CLOSE处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toPoolDayClose")
	public String toPoolDayClose(FcDayClose fcDayClose, Model model) {
		//获取用户的网点,设置默认网点日结日期
		List<com.bytter.system.api.dto.FcCorpPoolDTO> corpDTOs = fcCorpPoolService.getUserCorpPools(this.getCurrentUser(),
				ResourceConstant.FCL_CLOSE_POOL_APPLY);//查询所有网点
		if(!EmptyUtils.isEmptyList(corpDTOs)){
			//取第一个网点
			String netCode = corpDTOs.get(0).getId();//资金池主体
			fcDayClose = fcDayCloseService.getPoolDayCloseDate(netCode);//获取网点日结日期
			FcDayCloseVO closeVO = new FcDayCloseVO();
			closeVO.setNetCode(netCode);
			Date endDate = DateUtils.addDay(fcDayClose.getCloseDate(), 1);
			closeVO.setLastCloseDate(fcDayClose.getCloseDate());//已日结日期
			closeVO.setCloseDate(endDate);//本次日结日期即工作日日期
			closeVO.setCloseDay(new Integer(1));
			model.addAttribute("dayClose", closeVO);
		}
		return "finacial/closedeal/dayclose/addFcDayClosePool";
	}
    
    /**
     * 功能介绍 : 查询网点已日结日期。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/toViewCloseDate")
	@ResponseBody
	public FcDayCloseVO toViewCloseDate(String netId, String poolSign) {
    	FcDayCloseVO closeVO = null;
    	if (!StringUtils.isEmpty(netId)) {
			FcDayCloseDTO dayCloseDTO = null;
    		if (EmptyUtils.isEmpty(poolSign)) {
    			dayCloseDTO = fcDayCloseService.getDayCloseFinalDate(netId);
			} else {
    			dayCloseDTO = fcDayCloseService.getPoolDayCloseFinalDate(netId);
			}
    		closeVO = new FcDayCloseVO();
    		closeVO.setNetCode(netId);
    		Date lastCloseDate = dayCloseDTO.getCloseDate();//已日结日期
    		Date endDate = DateUtils.addDay(lastCloseDate, 1);
    		closeVO.setLastCloseDate(lastCloseDate);//已日结日期
			log.info("lastcloseDate:" + closeVO.getLastCloseDate());
            closeVO.setLastCloseDateStr(DateUtils.date2Str(lastCloseDate, new SimpleDateFormat("yyyy-MM-dd")));//转换为年月日
			log.info("lastcloseDateStr:" + DateUtils.formatDate(lastCloseDate));
    		closeVO.setCloseDate(endDate);//本次日结日期即工作日日期
			log.info("closeDate:" + closeVO.getCloseDate());
            closeVO.setCloseDateStr(DateUtils.date2Str(endDate, new SimpleDateFormat("yyyy-MM-dd")));//转换为年月日
			log.info("closeDateStr:" + DateUtils.formatDate(endDate));
    		closeVO.setCloseDay(new Integer(1));
		}
		return closeVO;
	}
	/**
	 * 跳转反日结
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
	@LogMessage(content = "跳转FC_DAY_CLOSE处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toreDayClose")
	public String toreDayClose(FcDayClose fcDayClose, Model model) {
		//获取用户的网点,设置默认网点日结日期
		List<String> corpDTOs = corpService.getUserNetIds(ResourceConstant.FCL_CLOSE_COMMON_APPLY,
				CorpConstant.CORP_TYPE_CODE_NET, getUserId());//查询所有网点
		if(!EmptyUtils.isEmptyList(corpDTOs)){
			//取第一个网点
			String netCode = corpDTOs.get(0);
			fcDayClose = fcDayCloseService.getDayCloseDate(netCode);//获取网点日结日期
			FcDayCloseVO closeVO = new FcDayCloseVO();
			closeVO.setNetCode(netCode);
			closeVO.setCloseDate(fcDayClose.getCloseDate());//已日结日期
			closeVO.setLastCloseDate(fcDayClose.getCloseDate());//已日结日期
			model.addAttribute("dayClose", closeVO);
		}
		return "finacial/closedeal/dayclose/restoreFcDayClose";
	}
    /**
	* 跳转资金池反日结
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_CLOSEDEAL_TOREPOOLDAYCLOSE_APPLY)
    @LogMessage(content = "跳转FC_DAY_CLOSE处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/torePoolDayClose")
	public String torePoolDayClose(FcDayClose fcDayClose, Model model) {
    	//获取用户的网点,设置默认网点日结日期
		List<com.bytter.system.api.dto.FcCorpPoolDTO> corpDTOs = fcCorpPoolService.getUserCorpPools(this.getCurrentUser(),
				ResourceConstant.FCL_CLOSE_POOL_APPLY);//查询有权限的资金池
    	if(!EmptyUtils.isEmptyList(corpDTOs)){
    		//取第一个网点
    		com.bytter.system.api.dto.FcCorpPoolDTO poolDTO = corpDTOs.get(0);
			// id = daedff181b61460ba9bf35735d395a60
			// corpId = 9ceff224ab274cc4a6758a3d0aa64308
    		fcDayClose = fcDayCloseService.getPoolDayCloseDate(poolDTO.getId());//获取资金池日结日期
    		
    		FcDayCloseVO closeVO = new FcDayCloseVO();
    		closeVO.setNetCode(poolDTO.getId());
    		closeVO.setCloseDate(fcDayClose.getCloseDate());//已日结日期
    		closeVO.setLastCloseDate(fcDayClose.getCloseDate());//已日结日期
    		model.addAttribute("dayClose", closeVO);
    	}
		return "finacial/closedeal/dayclose/restoreFcDayClosePool";
	}
    /**
     * 功能介绍 : 查询网点已日结日期。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/toViewReCloseDate")
	@ResponseBody
	public FcDayCloseVO toViewReCloseDate(String netId, String poolSign) {
    	FcDayCloseVO closeVO = null;
    	if (!StringUtils.isEmpty(netId)) {
    		FcDayCloseDTO dayCloseDTO = null;
    		if (!EmptyUtils.isEmpty(poolSign)) {
				dayCloseDTO = fcDayCloseService.getPoolDayCloseFinalDate(netId);
			} else {
				dayCloseDTO = fcDayCloseService.getDayCloseFinalDate(netId);
			}
    		
    		closeVO = new FcDayCloseVO();
    		closeVO.setNetCode(netId);
    		closeVO.setLastCloseDate(dayCloseDTO.getCloseDate());//已日结日期
    		closeVO.setCloseDate(dayCloseDTO.getCloseDate());//本次日结日期即工作日日期
    		closeVO.setCloseDay(new Integer(1));
		}
		return closeVO;
	}
    
    /**
	* 跳转月结页面
	*
	* @return
	*/
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
    @LogMessage(content = "跳转FC_DAY_CLOSE处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toMonthClose")
	public String toMonthClose(FcMonthCloseDTO fcMonthClose, Model model) {
		//获取用户的网点,设置默认网点日结日期
    	List<String> corpDTOs = corpService.getUserNetIds(ResourceConstant.FCL_CLOSE_COMMON_APPLY, 
    			CorpConstant.CORP_TYPE_CODE_NET, getUserId());//查询所有网点
    	if(!EmptyUtils.isEmptyList(corpDTOs)){
    		//取第一个网点
    		String netCode = corpDTOs.get(0);
    		FcDayClose dayClose = fcDayCloseService.getMonthCloseDate(netCode);//获取网点日结日期
    		
    		FcMonthCloseDTO closeVO = new FcMonthCloseDTO();
    		closeVO.setNetCode(netCode);
    		Calendar cal = Calendar.getInstance();
            cal.setTime(dayClose.getCloseDate());
            //待月结月份
            cal.add(Calendar.MONTH, 1);//默认下一个月为月结日期
    		
            closeVO.setCloseYear(cal.get(Calendar.YEAR));
            closeVO.setCloseMonth(cal.get(Calendar.MONTH) + 1);
    		model.addAttribute("monthClose", closeVO);
    	}
		return "finacial/closedeal/dayclose/addFcMonthClose";
	}
    
    /**
     * 功能介绍 : 查询网点已月结日期。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/toViewMonthCloseDate")
	@ResponseBody
	public FcMonthCloseDTO toViewMonthCloseDate(String netId) {
    	FcMonthCloseDTO dayCloseDTO = null;
    	if (StringUtils.isNotEmpty(netId)) {
    		dayCloseDTO = fcDayCloseService.getMonthCloseFinalDate(netId);
		}
		return dayCloseDTO;
	}
    
    /**
	* 跳转反月结
	*
	* @return
	*/
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
    @LogMessage(content = "跳转FC_DAY_CLOSE处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toreMonthClose")
	public String toreMonthClose(FcDayClose fcDayClose, Model model) {
    	//获取用户的网点,设置默认网点日结日期
    	List<String> corpDTOs = corpService.getUserNetIds(ResourceConstant.FCL_CLOSE_COMMON_APPLY, 
    			CorpConstant.CORP_TYPE_CODE_NET, getUserId());//查询所有网点
    	if(!EmptyUtils.isEmptyList(corpDTOs)){
    		//取第一个网点
    		String netCode = corpDTOs.get(0);
    		FcDayClose dayClose = fcDayCloseService.getMonthCloseDate(netCode);//获取网点日结日期
    		
    		FcMonthCloseDTO closeVO = new FcMonthCloseDTO();
    		closeVO.setNetCode(netCode);
    		Calendar cal = Calendar.getInstance();
            cal.setTime(dayClose.getCloseDate());
            closeVO.setCloseYear(cal.get(Calendar.YEAR));
            closeVO.setCloseMonth(cal.get(Calendar.MONTH) + 1);//已月结月份
    		model.addAttribute("monthClose", closeVO);
    	}
		return "finacial/closedeal/dayclose/restoreFcMonthClose";
	}
    
    /**
     * 功能介绍 : 查询网点已月结日期。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/toViewReMonthDate")
	@ResponseBody
	public FcMonthCloseDTO toViewReMonthDate(String netId) {
    	FcMonthCloseDTO closeDTO = null;
    	if (!StringUtils.isEmpty(netId)) {
    		FcDayClose dayClose = fcDayCloseService.getMonthCloseDate(netId);
    		if (null == dayClose) {
    			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NO_INIT_NET);
    		}
    		
    		closeDTO = new FcMonthCloseDTO();
    		closeDTO.setNetCode(netId);
    		Calendar cal = Calendar.getInstance();
            cal.setTime(dayClose.getCloseDate());
            closeDTO.setCloseYear(cal.get(Calendar.YEAR));
            closeDTO.setCloseMonth(cal.get(Calendar.MONTH) + 1);//已月结月份
		}
		return closeDTO;
	}
    
	/**
	* 日结检查信息查看
	*
	* @return
	*/
	////@RequiresPermissions("closedeal:fcdayclose:view")
	@GetMapping("/toView")
	public String toView(FcDayCloseDTO fcDayClose, String checkType, Model model) {
		model.addAttribute("fcDayClose", fcDayClose);
		model.addAttribute("checkType", checkType);
		return "finacial/closedeal/dayclose/view" + checkType;
	}
	/**
	* FC_DAY_CLOSE详情页
	*
	* @return
	*/
	////@RequiresPermissions("closedeal:fcdayclose:view")
	@LogMessage(content = "查询FC_DAY_CLOSE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcDayClose fcDayClose = fcDayCloseService.get(id);
		model.addAttribute("fcDayClosePage", fcDayClose);
		return "closedeal/viewFcDayClose";
	}
	
	/**
     * 功能介绍 : 日结前检查控制项。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/dayClosePreCheck")
	@ResponseBody
	public Map<String, Object> dayClosePreCheck(FcDayCloseDTO closeVO) {
    	//日结前检查业务
    	Map<String, Object> returnMap =null;
    	if (EmptyUtils.isEmpty(closeVO.getPoolSign())) {//资金池标识为空
			if(StringUtils.isNotEmpty(closeVO.getNetCode())){
				returnMap= fcDayCloseService.dayCloseCheck(closeVO);
			}else{
				throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NOTGET,FinacialConstant.DATE_KNOT);
			}
		} else {
			if(StringUtils.isNotEmpty(closeVO.getNetCode())){
				returnMap= fcDayCloseService.dayPoolCloseCheck(closeVO);
			}else{
				throw new ParameterException(CloseMessageEnum.FIN_CLOSE_POOLNET_NOTGET,FinacialConstant.DATE_KNOT);
			}
		}
		return returnMap;
	}
	/**
	 * 日结处理
	 * @param closeVO
	 * @return
	 */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
	@LogMessage(content = "添加FC_DAY_CLOSE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doDayClose")
	@ResponseBody
	public AjaxJson doDayClose(FcDayCloseVO closeVO){
    	if(StringUtils.isNotEmpty(closeVO.getNetCode())){
		   fcDayCloseService.doDayClose(closeVO, getCurrentUser());
    	}else{
    		throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NOTGET,FinacialConstant.DATE_KNOT);
    	}
		return success();
	}
	/**
	 * 反日结处理
	 * @param closeVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
	@LogMessage(content = "添加FC_DAY_CLOSE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doReverseDayClose")
	@ResponseBody
	public AjaxJson doReverseDayClose(FcDayCloseDTO closeVO) {
		fcDayCloseService.execReverseDay(closeVO, getCurrentUser(),false);
		return success();
	}
	/**
	 * 日结处理
	 * @param closeVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_CLOSE_POOL_APPLY)
	@LogMessage(content = "添加FC_DAY_CLOSE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doPoolDayClose")
	@ResponseBody
	public AjaxJson doPoolDayClose(FcDayCloseVO closeVO){
		if(StringUtils.isNotEmpty(closeVO.getNetCode())){
			fcDayCloseService.doPoolDayClose(closeVO, getCurrentUser());
		}else{
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_POOLNET_NOTGET,FinacialConstant.DATE_KNOT);
		}
		return success();
	}
	
	/**
	 * 反日结处理
	 * @param closeVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_CLOSE_POOL_APPLY)
	@LogMessage(content = "添加FC_DAY_CLOSE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doReversePoolDayClose")
	@ResponseBody
	public AjaxJson doReversePoolDayClose(FcDayCloseDTO closeVO) {
		fcDayCloseService.execPoolReverseDay(closeVO, getCurrentUser(),true);
		return success();
	}
	
    /**
     * 功能介绍 : 日结前检查控制项。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/viewCheckAccount")
	@ResponseBody
	public List<FcAccDTO> viewCheckAccount(FcDayCloseDTO closeVO) {
    	//日结前检查业务
    	List<FcAccDTO> output = fcDayCloseService.findNoCheckVFcAcc(closeVO);
		return output;
	}
    
    @PostMapping("/viewCheckAccountInit")
    @ResponseBody
    public List<FcAccInitCheckBean> viewCheckAccountInit(FcDayCloseDTO closeVO) {
    	//日结前检查业务
    	List<FcAccInitCheckBean> list = fcDayCloseService.findNoCheckAccInit(closeVO);
    	return list;
    }
    
    /**
     * 功能介绍 : 日结前检查控制项。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/viewCheckCounterOuterAccounts")
	@ResponseBody
	public List<FcOutAccbookCheckBean> viewCheckCounterOuterAccounts(FcDayCloseDTO closeVO) {
    	//日结前检查业务
    	List<FcOutAccbookCheckBean> output = fcDayCloseService.getNoCheckOutBookDtl(closeVO);
		return output;
	}
    
    @PostMapping("/viewCheckCounterAccounts")
	@ResponseBody
	public List<FcAccbookCheckBean> viewCheckCounterAccounts(FcDayCloseDTO closeVO) {
    	//日结前检查业务
    	List<FcAccbookCheckBean> output = fcDayCloseService.getNoCheckBookDtl(closeVO);
		return output;
	}
	
    @PostMapping("/viewCheckFreeze")
    @ResponseBody
    public List<FcFreezeCheckBean> viewCheckFreeze(FcDayCloseDTO closeVO) {
    	//日结前检查业务
    	List<FcFreezeCheckBean> list = fcDayCloseService.findNoCheckFreeze(closeVO);
    	return list;
    }
    
    /**
     * 功能介绍 : 日结前检查不控制项(内部贷款)。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/viewCheckEnterpriseLoan")
    @ResponseBody
    public List<CmsLoanSendOutCheckDTO> viewCheckEnterpriseLoan(FcDayCloseDTO dayClose) {
    	//日结前检查业务
    	List<CmsLoanSendOutCheckDTO> output = fcDayCloseService.findLoanSendOutInfo(dayClose);
    	return output;
    }
    
    /**
     * 功能介绍 : 日结前检查控制项(银行借款)。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/viewCheckBankLend")
	@ResponseBody
	public List<CmsProvideLoanSendCheckDTO> viewCheckBankLend(FcDayCloseDTO dayClose) {
    	//日结前检查业务
    	List<CmsProvideLoanSendCheckDTO> output = fcDayCloseService.findBankLoadSendInfo(dayClose);
    	return output;
	}
    
    
    /**
     * 功能介绍 : 日结前检查控制项(委托贷款)。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/viewCheckEntrustLoan")
	@ResponseBody
	public List<CmsPrecativeLoanSendCheckDTO> viewCheckEntrustLoan(FcDayCloseDTO dayClose) {
    	//日结前检查业务
    	List<CmsPrecativeLoanSendCheckDTO> output = fcDayCloseService.findPrecativeLoanInfo(dayClose);
    	return output;
	}
    
    /**
     * 功能介绍 : 月结前检查控制项。<br>
     * <b>日期：</b>2017年7月12日<br>
     * @version 1.0
     * @author liyh 
     */
    @PostMapping("/monthClosePreCheck")
	@ResponseBody
	public Map<String, Object> monthClosePreCheck(FcMonthCloseDTO closeDTO) {
    	Map<String, Object> returnMap=null;
    	if(StringUtils.isNotEmpty(closeDTO.getNetCode())){
    	//月结前检查业务
    	 returnMap = fcDayCloseService.monthCloseCheck(closeDTO);
    	}else{
    		throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NOTGET,FinacialConstant.MONTH_KNOT);
    	}
		return returnMap;
	}
	/**
	 * 月结处理
	 * @param closeDTO
	 * @return
	 */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
	@LogMessage(content = "添加FC_DAY_CLOSE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doMonthClose")
	@ResponseBody
	public AjaxJson doMonthClose(FcMonthCloseDTO closeDTO){
    	if(StringUtils.isNotEmpty(closeDTO.getNetCode())){
    		fcDayCloseService.doMonthClose(closeDTO, getCurrentUser());
    	}else{
    		throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NOTGET,FinacialConstant.MONTH_KNOT);
    	}
		return success();
	}
	
	
	/**
	 * 反月结处理
	 * @param closeDTO
	 * @return
	 */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_COMMON_APPLY)
	@LogMessage(content = "添加FC_DAY_CLOSE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doReverseMonthClose")
	@ResponseBody
	public AjaxJson doReverseMonthClose(FcMonthCloseDTO closeDTO) {
		fcDayCloseService.execReverseMonth(closeDTO, getCurrentUser());
		return success();
	}
	/**
	 * 获取资金池
	 * @return
	 */
	@RequestMapping(params = "getUserCorpPools")
	@ResponseBody
	public List<com.bytter.system.api.dto.FcCorpPoolDTO> getUserCorpPools() {
		String resource = ResourceConstant.FCL_CLOSE_POOL_APPLY;
		return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
	}
    
}
