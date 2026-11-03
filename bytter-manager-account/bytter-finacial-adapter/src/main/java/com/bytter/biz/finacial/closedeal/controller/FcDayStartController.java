package com.bytter.biz.finacial.closedeal.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.biz.finacial.closedeal.entity.FcDayStart;
import com.bytter.biz.finacial.closedeal.service.FcDayCloseService;
import com.bytter.biz.finacial.closedeal.service.FcDayStartService;
import com.bytter.biz.finacial.closedeal.vo.FcDayStartVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.CorpConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**   
 * @Title: 期终处理日初
 * @Description: FC_DAY_START
 * @author onlineGenerator
 * @date 2019-11-12 14:51:02
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/closedeal/fcdaystart")
public class FcDayStartController extends BaseController {
	@Autowired
	private FcDayStartService fcDayStartService;
	@Autowired
	private FcDayCloseService fcDayCloseService;
	@DubboReference(check = false)
	private ICorpService corpService;
	
	/**
	* 跳转日初页面----------------------------
	*/
	//@RequiresPermissions(ResourceConstant.FCL_START_COMMON_APPLY)
    @LogMessage(content = "跳转fc_day_start处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toDayStart")
	public String toDayStart(FcDayStart fcDayStart, Model model) {
		//获取用户的网点,设置默认网点日结日期
    	List<String> corpDTOs = corpService.getUserNetIds(ResourceConstant.FCL_CLOSE_COMMON_APPLY,CorpConstant.CORP_TYPE_CODE_NET, getUserId());//查询所有网点
    	if(!EmptyUtils.isEmptyList(corpDTOs)){
    		//取第一个网点
    		String netCode = corpDTOs.get(0);
    		fcDayStart = fcDayStartService.getDayStartDate(netCode);//获取网点日初日期
			if(null == fcDayStart){
				FcDayStartVO start = fcDayStartService.getDayStartFinalDate(netCode);
				fcDayStart = new FcDayStart();
				BeanUtils.copyNotNullProperties(start,fcDayStart);
			}
			FcDayStartVO startVO = new FcDayStartVO();
			startVO.setNetCode(netCode);
    		Date endDate = DateUtils.addDay(fcDayStart.getStartDate(), 1);
			startVO.setLastStartDate(fcDayStart.getStartDate());//已日初日期
			startVO.setStartDate(endDate);//本次日初日期即工作日日期
			startVO.setStartDay(new Integer(1));
			model.addAttribute("dayStart", startVO);
    	}
		return "finacial/closedeal/daystart/addFcDayStart";
	}
	
    
    /**
     * 功能介绍 : 查询网点已日初日期。<br>-------------
     */
    @PostMapping("/toViewStartDate")
	@ResponseBody
	public FcDayStartVO toViewStartDate(String netId) {
    	FcDayStartVO startVO = null;
    	if (!StringUtils.isEmpty(netId)) {
			FcDayStartVO dayStartVO = fcDayStartService.getDayStartFinalDate(netId);
			startVO = new FcDayStartVO();
			startVO.setNetCode(netId);
    		Date lastCloseDate = dayStartVO.getStartDate();//已日初日期
    		Date endDate = DateUtils.addDay(lastCloseDate, 1);
			startVO.setLastStartDate(lastCloseDate);//已日结日期
			startVO.setLastStartDateStr(DateUtils.date2Str(lastCloseDate, new SimpleDateFormat("yyyy-MM-dd")));//转换为年月日
			startVO.setStartDate(endDate);//本次日结日期即工作日日期
			startVO.setStartDateStr(DateUtils.date2Str(endDate, new SimpleDateFormat("yyyy-MM-dd")));//转换为年月日
			startVO.setStartDay(new Integer(1));
		}
		return startVO;
	}
	
	/**
	 * 日初处理----------
	 */
    //@RequiresPermissions(ResourceConstant.FCL_START_COMMON_APPLY)
	@LogMessage(content = "添加FC_DAY_START",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doDayStart")
	@ResponseBody
	public AjaxJson doDayStart(FcDayStartVO startVO){
		FcDayClose fcDayClose = fcDayCloseService.getDayCloseDate(startVO.getNetCode());//获取网点日结日期
		int day = DateUtils.getOffsetDays(fcDayClose.getCloseDate(),startVO.getStartDate());
		if(day > 1){
			Date date = DateUtils.addDay(fcDayClose.getCloseDate(),1);
			String closeDate = DateUtils.formatDate(date);
			throw new ParameterException(CloseMessageEnum.FIN_START_NET_NOT_CLOSE,closeDate);
		}
    	if(StringUtils.isNotEmpty(startVO.getNetCode())){
		   fcDayStartService.doDayStart(startVO, getCurrentUser());
    	}else{
    		throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NOTGET,FinacialConstant.DATE_KNOT);
    	}
		return success();
	}
	/**
	 * 反日初处理-------------------
	 * @param startVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_START_COMMON_APPLY)
	@LogMessage(content = "添加FC_DAY_START",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doReverseDayStart")
	@ResponseBody
	public AjaxJson doReverseDayStart(FcDayStartVO startVO) {
		fcDayStartService.execReverseDay(startVO, getCurrentUser());
		return success();
	}
	
}
