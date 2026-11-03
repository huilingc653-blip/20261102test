package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.service.FcCompareCenterAutoService;
import com.bytter.check.service.FcCompareCenterService;
import com.bytter.check.vo.FcBankCheckSearchVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
/**   
 * @Title: Controller  
 * @Description: FC_COMPARE_ACC 中心对账(一键对账、一键结转）
 * @author onlineGenerator
 * @date 2017-05-31 14:43:17
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fccompareAutoCenter")
public class FcCompareCenterAutoController extends BaseController {
	@Autowired
	private FcCompareCenterService fcCompareCenterService;
	@Autowired
	private FcCompareCenterAutoService fcCompareCenterAutoService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
	/**
	* 中心对账列表 页面跳转
	*
	* @return
	*/
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
	@LogMessage(content = "查询FC_COMPARE_ACC列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
    	String initNetId = fcCompareCenterService.getInitNetId(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId());
    	model.addAttribute("opType","init");
    	model.addAttribute("initNetId",initNetId);//初始化网点
    	model.addAttribute("notAuthNets",fcCompareCenterService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId()));//初始化网点
    	model.addAttribute("initNetOverDate",fcCompareCenterService.getDayOverByNetId(initNetId));//初始化网点日结日期
        return "check/fccomparecenter/listFcCompareCenter";
	}
	/**
	 * 一键结转列表，查询数据
	 * @param model
	 * @param fca
	 * @return
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
	@LogMessage(content = "一键结转列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/adjustList")
	public String adjustList(Model model, FcBankCheckSearchVo fca) {
		String initNetId = "";
		String account = fca.getAccount();
		if (EmptyUtils.isNotEmpty(account)) {
		    String[] accs = account.split(",");
		    if (null != accs && accs.length > 0) {
                FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(accs[0]);
                initNetId = fcAccDTO.getNetId();
            }
        }
		if (EmptyUtils.isEmpty(initNetId)) {
            initNetId = fcCompareCenterService.getInitNetId(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId());
        }
		model.addAttribute("fcBankCheck",fca);
		model.addAttribute("opType","init");
		model.addAttribute("initNetId",initNetId);//初始化网点
		model.addAttribute("notAuthNets",fcCompareCenterService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId()));//初始化网点
		model.addAttribute("initNetOverDate",fcCompareCenterService.getDayOverByNetId(initNetId));//初始化网点日结日期
		return "check/fccomparecenter/listFcCompareAutoAdjust";
	}
	/**
	 * <br>
	 * <b>功能：</b> 查询批量结转列表 <br>
	 * <b>作者：</b>chenf<br>
	 * <b>日期：</b> 2022/06/15 0015 <br>
	 */
	@PostMapping("/autoAdjustDataTable")
	@ResponseBody
	public DataTablesOutput autoAdjustDataTable(@RequestBody DataTablesInput input) {
		return fcCompareCenterAutoService.queryAdjustList(input, getCurrentUser(), null, null);
	}
	/**
	 * <br>
	 * <b>功能：</b> 查询批量对账列表 <br>
	 * <b>作者：</b>chenf<br>
	 * <b>日期：</b> 2022/06/15 0015 <br>
	 */
	@PostMapping("/autoCheckDataTable")
	@ResponseBody
	public DataTablesOutput autoCheckDataTable(@RequestBody DataTablesInput input) {
		String calcDateStr = (String) input.getFormdata().get("transTime");
		String compareSign = (String) input.getFormdata().get("eqs_compareSign");
		Date calcDate = null;
		if (EmptyUtils.isEmpty(calcDateStr)) {
			calcDate = DateUtils.getDate();
		} else {
			calcDate = DateUtils.parseDate(calcDateStr);
		}
		return fcCompareCenterAutoService.queryCheckList(input, getCurrentUser(), calcDate, compareSign);
	}
	/**
	 * 一键对账
	 * @param account 内部账号
	 * @param bankAcc 银行账号
	 * @param checkType 核对方式
	 * @param curId 币别id
	 * @param paramType 对账类型（0中心对账 1erp对账） 默认中心对账
	 * @param transTime 结束日期
	 * @param checkDate 对账日期
	 * @return
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
	@LogMessage(content = "一键对账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/autoCompare")
	@ResponseBody
	public AjaxJson autoCompare(String account,String bankAcc,String checkType,String curId,String paramType,Date transTime,String checkDate,String endDate) {
		AjaxJson json=new AjaxJson();
		FcBankCheckSearchVo fca =null;
		String accounts[] =account.split(CommonConstant.COMMA_SPLIT);
		String bankAccs[] =bankAcc.split(CommonConstant.COMMA_SPLIT);
		String curIds[] =curId.split(CommonConstant.COMMA_SPLIT);
		String checkDates[]  = checkDate.split(CommonConstant.COMMA_SPLIT);
		String endDates[]  = endDate.split(CommonConstant.COMMA_SPLIT);
		for (int i=0;i<accounts.length;i++) {
			fca = new FcBankCheckSearchVo();
			fca.setAccount(accounts[i]);
			fca.setBankAcc(bankAccs[i]);
			fca.setCurId(curIds[i]);
			fca.setCheckType(checkType);
			fca.setTransTime(transTime);
			fca.setParamType(paramType);
			try {
				Date lastOverDate = null;
				if(!EmptyUtils.isEmpty(endDates[i]) && !"null".equals(endDates[i])){
					lastOverDate = DateUtils.str2Date(endDates[i], DateUtils.date_sdf());
				}else{
					lastOverDate = DateUtils.str2Date(checkDates[i], DateUtils.date_sdf());
				}
				if (transTime.before(DateUtils.addDay(lastOverDate, 1))) {//结束日期应大于上次结转日期
					//logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
					throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE, DateUtils.date2Str(transTime, DateUtils.date_sdf()));
				}
				fcCompareCenterService.doCheck(fca,lastOverDate,getCurrentUser());
				json.setSuccess(true);
			} catch (Exception e) {
				if(e instanceof ParameterException){
					json.setMessageEnum(((ParameterException) e).getIMessageEnum());
					json.setParams(((ParameterException) e).getParams());
				}
				json.setSuccess(false);
			}
		}
		return json;
	}
	/**
	 * 一键结转
	 * @param account 内部账号
	 * @param bankAcc 银行账号
	 * @param checkType 核对方式
	 * @param curId 币别id
	 * @param paramType 对账类型（0中心对账 1erp对账） 默认中心对账
	 * @param transTime 对账日期
	 * @param lastEndDate 上次结转日期
	 * @param blnBal  企业账面余额
	 * @param bankBal 银行账面余额
	 * @return
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
	@LogMessage(content = "一键结转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/autoAdjust")
	@ResponseBody
	public AjaxJson autoAdjust(String account,String bankAcc,String checkType,String curId,String paramType,Date transTime,String lastEndDate,String checkDate,String  blnBal,String bankBal) {
		AjaxJson json=new AjaxJson();
		FcBankCheckSearchVo fca =null;
		//内部账户
		String accounts[] =account.split(CommonConstant.COMMA_SPLIT);
		//银行账户
		String bankAccs[] =bankAcc.split(CommonConstant.COMMA_SPLIT);
		//币别
		String curIds[] =curId.split(CommonConstant.COMMA_SPLIT);
		//企业账面余额
		String blnBalance[] =blnBal.split(CommonConstant.COMMA_SPLIT);
		//银行账面余额
		String bankBalance[] =bankBal.split(CommonConstant.COMMA_SPLIT);
		//上次结转日期
		String lastEndDates[] =lastEndDate.split(CommonConstant.COMMA_SPLIT);
		String checkDates[] = checkDate.split(CommonConstant.COMMA_SPLIT);
		Boolean success = true;
		for (int i=0;i<accounts.length;i++) {
			fca = new FcBankCheckSearchVo();
			fca.setAccount(accounts[i]);
			fca.setBankAcc(bankAccs[i]);
			fca.setCurId(curIds[i]);
			fca.setCheckType(checkType);
			fca.setTransTime(transTime);
			fca.setParamType(paramType);
			fca.setBlnBalance(NumberUtil.objToBigDecimal(blnBalance[i]));
			fca.setBankBalance(NumberUtil.objToBigDecimal(bankBalance[i]));
			try {
				Date lastOverDate = null;
				if(!EmptyUtils.isEmpty(lastEndDates[i]) && !"null".equals(lastEndDates[i])){
					lastOverDate = DateUtils.str2Date(lastEndDates[i], DateUtils.date_sdf());
				}else{
					lastOverDate = DateUtils.str2Date(checkDates[i], DateUtils.date_sdf());
				}
				if (transTime.before(DateUtils.addDay(lastOverDate, 1))) {//对账结束日期应大于上次结转日期
					//logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
					throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE,DateUtils.date2Str(transTime, DateUtils.date_sdf()));
				}
				fcCompareCenterService.doAdjustOverOther(fca,lastOverDate,getCurrentUser(),null);
			} catch (Exception e) {
				//logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
				if(e instanceof ParameterException){
					json.setMessageEnum(((ParameterException) e).getIMessageEnum());
					json.setParams(((ParameterException) e).getParams());
				}
				success = false;
			}
		}
		json.setSuccess(success);
		return json;
	}
	/**
	 * 内部账户列表业务组件
	 * @param netId
	 * @param model
	 * @return
	 */
	@GetMapping("/interAccInfo")
	public String interAccInfo(String netId,String curId,String resource,String paramType,Model model){
		model.addAttribute("netId", netId);
		model.addAttribute("curId", curId);
		model.addAttribute("paramType", paramType);
		model.addAttribute("resource", resource);
		return "check/fccomparecenter/interAccInfo";
	}
	/**
	 * 账号列表页面
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/listInterAccInfoDetail")
	@ResponseBody
	public DataTablesOutput listInterAccInfoDetail(@RequestBody DataTablesInput dataTablesInput) {
		return fcCompareCenterAutoService.listInterAccInfoDetail(dataTablesInput);
	}
	
	/**
	 * <b>功能介绍：</b>根据网点获取日结日期<br>
	 * <b>日期：</b>2017年9月26日<br>
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
            dayCloseDate = fcCompareCenterService.getDayOverByNetId(netId);
        }
	    json.setObj(dayCloseDate);
		json.setSuccess(true);
		return json;
	}
}
