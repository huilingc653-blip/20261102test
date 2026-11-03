package com.bytter.biz.finacial.closedeal.controller;

import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.closedeal.entity.FcCarry;
import com.bytter.biz.finacial.closedeal.service.FcCarryService;
import com.bytter.biz.finacial.closedeal.vo.FcAccVoucherVO;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 结转记录表
 * @author onlineGenerator
 * @date 2017-06-27 09:55:51
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/closedeal/fccarry")
public class FcCarryController extends BaseController {
	@Autowired
	private FcCarryService fcCarryService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	/**
	* 结转记录表列表 页面跳转
	*/
	////@RequiresPermissions("fccarry:fccarry:index")
	@LogMessage(content = "查询结转记录表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/closedeal/fccarry/listFcCarry";
	}
    /**
        * 结转记录表 table列表 日结
    */
    ////@RequiresPermissions("fccarry:fccarry:index")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput input){
    	return fcCarryService.getFcCarry(input,FinacialResourceConstant.FINACIAL_FCCARRY_DAYREPORT_QUERY, getUserId());
    }
	/**
	* 结转记录表列表 页面跳转
	*/
	////@RequiresPermissions("fccarry:fccarry:index")
	@LogMessage(content = "查询结转记录表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listLog")
	public String listLog(Model model) {
        return "finacial/closedeal/fccarry/listFcCarryLog";
	}
	/**
    * 结转记录表 table列表
    */
    ////@RequiresPermissions("fccarry:fccarry:index")
    @PostMapping("/LogDatatable")
    @ResponseBody
	public DataTablesOutput LogDataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fcCarryService.doQueryDayLog(dataTablesInput, FinacialResourceConstant.FINACIAL_FCCARRY_LOG_QUERY,
				getUserId());
	}
    /**
     * 功能介绍 : 跳转损益转本年利润。<br>
     * <b>日期：</b>2018年1月5日<br>
     * @version 1.0
     * @author liyh
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_APPLY)
    @LogMessage(content = "调整本年利润结转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toProfitYear")
	public String toProfitYear(Model model, HttpServletRequest request) {
    	Map<String, Object> params = new HashMap<String, Object>();
        String netCode = request.getParameter("netId");
        String curCode = request.getParameter("curCode");
        String opDate = request.getParameter("opDate");
        if (EmptyUtils.isEmpty(opDate)) {
            opDate = DateUtils.getDate("yyyy-MM-dd");//结转日期默认当前日期
        }
        if (EmptyUtils.isEmpty(curCode)) {
        	BtCurrencyDTO curDTO = currencyService.getNatualCurrency();
        	curCode = curDTO.getId();//本位币
        }
        if (EmptyUtils.isEmpty(netCode)) {
        	//获取用户的网点,设置默认网点日结日期
        	List<String> corpDTOs = corpService.getUserNetIds(ResourceConstant.FCL_CLOSE_COMMON_APPLY,
        			CorpConstant.CORP_TYPE_CODE_NET, getUserId());//查询所有网点
        	if(!EmptyUtils.isEmptyList(corpDTOs)){
        		netCode = corpDTOs.get(0);
        	}
        }
        Integer busOriType = FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.get();//损益转本年利润
        params.put("netCode", netCode);
        params.put("curCode", curCode);
        params.put("busOriType", busOriType);
        params.put("opDate", opDate);
        FcCarry carry = fcCarryService.queryNoReverseFcCarries(netCode, CloseConstant.CarryType.CARRY_TYPE_INCOME.get());
        if (null == carry) {
        	params.put("startDateFlag", "0");
        } else {
            Date startDate = carry.getVoucherEndDate();
            params.put("startDateFlag", "1");
            params.put("startDate", DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        model.addAttribute("param", params);
        return "finacial/closedeal/fccarry/profitYearCharge";
	}
    /**
     * 功能介绍 : 查询损益类科目可结转的账号发生额明细。<br>
     * <b>日期：</b>2018年1月5日<br>
     * @version 1.0
     * @author liyh
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_APPLY)
    @LogMessage(content = "查询本年利润结转数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@RequestMapping("/findProfitYear")
    @ResponseBody
    public DataTablesOutput<FcAccVoucherVO> findProfitYear(@RequestBody DataTablesInput input, HttpServletRequest request) {
    	Map<String, Object> param = new HashMap<String, Object>();
        String netCode = input.getPara("netId");
        Integer busOriType = FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.get();//损益转本年利润
        String opDate = input.getPara("opDate");
        String startDateStr = input.getPara("startDate");
        param.put("netCode", netCode);
        param.put("busOriType", busOriType);
        param.put("opDateStr", opDate);
        Date endDate = DateUtils.str2Date(opDate, DateUtils.date_sdf);
        param.put("opDate", endDate);
    	if (!EmptyUtils.isEmpty(startDateStr)) {
            Date startDate = DateUtils.str2Date(startDateStr, DateUtils.date_sdf);//开始日期
            if (DateUtils.dayDiffs(startDate, endDate) >= 0) {
                return new DataTablesOutput(input.getDraw(), null, 0, 0);
                //throw new ParameterException(CloseMessageEnum.FIN_CLOSE_PROFITYEAR_STARTDATE, startDateStr);
            }
            param.put("startDate", startDate);
            param.put("startDateStr", startDateStr);
        }
        //查询最近一次结转的日期
        FcCarry carry = fcCarryService.queryNoReverseFcCarries(netCode, CloseConstant.CarryType.CARRY_TYPE_INCOME.get());
        if (carry != null) {
            param.put("carryDate", carry.getCarryTime());
            param.put("startDate", carry.getVoucherStartDate());
        }
    	return fcCarryService.findProfitYear(param, input);
    }
    /**
     * 功能介绍 : 查看损益转本年利润结转凭证。<br>
     * <b>日期：</b>2018年1月18日<br>
     * @version 1.0
     * @author liyh
     */
    @LogMessage(content = "记账凭证保存并提交",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/submitProfitYear")
    @ResponseBody
    public AjaxJson submitProfitYear(Model model,
                                     @RequestParam String eqd_opDate,
                                     @RequestParam String eqd_startDate,
                                     @RequestParam String eqs_netId,
                                     @RequestParam(value = "eqs_CurCode",required = false) String curCode) {
        if (StringUtils.isEmpty(curCode)) {
            curCode = currencyService.getNatualCurrency().getCurCode();
        }
        Map<String, Object> params = new HashedMap();
        params.put("busOriType",FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.getValue());
        params.put("userCode", getUserId());
        params.put("opDate", DateUtils.parse(eqd_opDate, "yyyy-MM-dd"));
        if (EmptyUtils.isNotEmpty(eqd_startDate)) {
            params.put("startDate", DateUtils.parse(eqd_startDate, "yyyy-MM-dd"));
        }
        params.put("netCode", eqs_netId);
        params.put("curCode", curCode);//币别
        //查询最近一次结转的日期
        FcCarry carry = fcCarryService.queryNoReverseFcCarries(eqs_netId, CloseConstant.CarryType.CARRY_TYPE_INCOME.get());
        if (carry != null) {
            if(eqd_opDate.equals(DateUtils.date2Str(carry.getVoucherStartDate(),DateUtils.date_sdf()))){
                throw new ParameterException(CloseMessageEnum.FIN_PROCES_INT_CARRY_PROANDLOSS_BOOKKEPT, eqd_opDate);
            }
            params.put("carryDate", carry.getCarryTime());
            params.put("startDate", carry.getCarryTime());
        }
//        提交操作
        fcCarryService.submitVoucher(params);
        return success();
    }
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_CHECK)
    @LogMessage(content = "损益转本年利润复核页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/CheckProfitYear")
    public String CheckProfitYear() {
        return "finacial/closedeal/fccarry/checkProfitYear";
    }
    /**
     * 查询待复核数据
     * @param model
     * @param netId
     * @param curCode
     * @param busOriType
     * @param opDate
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_CHECK)
    @LogMessage(content = "查询待复核数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getCheckProfitYear")
    public String getCheckProfitYear(Model model,
                                  @RequestParam(value = "eqs_netId", required = false) String netId,
                                  @RequestParam(value = "curCode", required = false) String curCode,
                                  @RequestParam(value = "busOriType", required = false) String busOriType,
                                  @RequestParam(value = "opDate", required = false) String opDate) {
        if (opDate == null) {
            opDate = DateUtils.format(new Date(), "yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(curCode)) {
            curCode = currencyService.getNatualCurrency().getCurCode();
        }
        if (StringUtils.isEmpty(netId)) {
//            netId = getCurrentUser().getCorpId();
            return "finacial/closedeal/fccarry/viewProfitYear";
        }
        Map<String, Object> params = new HashedMap();
        params.put("netId", netId);//网点
        params.put("curCode", curCode);//币别
        params.put("busOriType", FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.getValue());//业务类型来源
        params.put("userCode", getCurrentUser().getId());
        params.put("opDate", opDate);
        params.put("voucherState", FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.getValue());//待复核状态
        model.addAttribute("voucher", fcCarryService.getCheckProfitYear(params));
        return "finacial/closedeal/fccarry/viewProfitYear";
    }
    /**
     * 复核操作
     * @param model
     * @param netId
     * @param curCode
     * @param busOriType
     * @param opDate
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_CHECK)
    @LogMessage(content = "损益转本年利润复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/docheckProfitYear")
    @ResponseBody
    public AjaxJson docheckProfitYear(Model model,
                                    @RequestParam(value = "eqs_netId", required = false) String netId,
                                    @RequestParam(value = "curCode", required = false) String curCode,
                                    @RequestParam(value = "busOriType", required = false) String busOriType,
                                    @RequestParam(value = "opDate", required = false) String opDate,
                                    @RequestParam(value = "flag", required = true) String flag,
                                    @RequestParam(value = "backReason",required = false) String backReason) {
        if (opDate == null) {
            opDate = DateUtils.format(new Date(), "yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(curCode)) {
            curCode = currencyService.getNatualCurrency().getCurCode();
        }
        if (StringUtils.isEmpty(netId)) {
            netId = getCurrentUser().getCorpId();
        }
        Map<String, Object> params = new HashedMap();
        params.put("netId", netId);//网点
        params.put("curCode", curCode);//币别
        params.put("busOriType", FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.getValue());//业务类型来源
        params.put("userCode", getCurrentUser().getId());
        params.put("opDate", opDate);
        params.put("flag", flag);
        params.put("backReason",backReason);
        params.put("voucherState", FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.getValue());//待复核状态
        fcCarryService.checkProfitYear(params);
        return success();
    }
    /**
     * 查询本年已结转凭证或打回凭证
     * @param model
     * @param netId
     * @param curCode
     * @param busOriType
     * @param opDate
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_APPLY)
    @LogMessage(content = "查询已结转凭证或打回凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewProfitYear")
    public String viewCheckProfitYear(Model model,
                                     @RequestParam(value = "eqs_netId", required = false) String netId,
                                     @RequestParam(value = "curCode", required = false) String curCode,
                                     @RequestParam(value = "busOriType", required = false) String busOriType,
                                     @RequestParam(value = "opDate", required = false) String opDate,
                                      @RequestParam(value = "voucherState",required = true) String voucherState) {
        if (opDate == null) {
            opDate = DateUtils.format(new Date(), "yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(curCode)) {
            curCode = currencyService.getNatualCurrency().getCurCode();
        }
        if (StringUtils.isEmpty(netId)) {
//            netId = getCurrentUser().getCorpId();
            model.addAttribute("showNoDataBox", CommonConstant.YES);
            return "finacial/closedeal/fccarry/viewProfitYear";
        }
        Map<String, Object> params = new HashedMap();
        params.put("netId", netId);//网点
        params.put("curCode", curCode);//币别
        params.put("busOriType", FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.getValue());//业务类型来源
        params.put("userCode", getCurrentUser().getId());
        params.put("opDate", opDate);
        params.put("voucherState", voucherState);//查看结转或打回状态数据
        model.addAttribute("voucher", fcCarryService.getCheckProfitYear(params));
        model.addAttribute("showNoDataBox", CommonConstant.NO);
        return "finacial/closedeal/fccarry/viewProfitYear";
    }
    /**
     * 重新提交打回凭证
     * @param model
     * @param netId
     * @param curCode
     * @param busOriType
     * @param opDate
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_APPLY)
    @LogMessage(content = "重新提交打回凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/rebackCheckProfitYear")
    @ResponseBody
    public AjaxJson rebackCheckProfitYear(Model model,
                                      @RequestParam(value = "eqs_netId", required = false) String netId,
                                      @RequestParam(value = "curCode", required = false) String curCode,
                                      @RequestParam(value = "busOriType", required = false) String busOriType,
                                      @RequestParam(value = "opDate", required = false) String opDate,
                                      @RequestParam(value = "voucherState",required = true) String voucherState) {
        if (opDate == null) {
            opDate = DateUtils.format(new Date(), "yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(curCode)) {
            curCode = currencyService.getNatualCurrency().getCurCode();
        }
        if (StringUtils.isEmpty(netId)) {
            return success();
        }
        Map<String, Object> params = new HashedMap();
        params.put("netId", netId);//网点
        params.put("curCode", curCode);//币别
        params.put("busOriType", FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.getValue());//业务类型来源
        params.put("userCode", getCurrentUser().getId());
        params.put("opDate", opDate);
        params.put("flag", "1");
        params.put("voucherState", voucherState);
        fcCarryService.rebackCheckProfitYear(params);
        return success();
    }
    /**
     * 删除打回凭证
     * @param model
     * @param netId
     * @param curCode
     * @param busOriType
     * @param opDate
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_CLOSE_FCCARRY_APPLY)
    @LogMessage(content = "删除打回凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/deleteProfitYear")
    @ResponseBody
    public AjaxJson deleteProfitYear(Model model,
                                          @RequestParam(value = "eqs_netId", required = false) String netId,
                                          @RequestParam(value = "curCode", required = false) String curCode,
                                          @RequestParam(value = "busOriType", required = false) String busOriType,
                                          @RequestParam(value = "opDate", required = false) String opDate,
                                          @RequestParam(value = "voucherState",required = true) String voucherState) {
        if (opDate == null) {
            opDate = DateUtils.format(new Date(), "yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(curCode)) {
            curCode = currencyService.getNatualCurrency().getCurCode();
        }
        if (StringUtils.isEmpty(netId)) {
            return success();
        }
        Map<String, Object> params = new HashedMap();
        params.put("netId", netId);//网点
        params.put("curCode", curCode);//币别
        params.put("busOriType", FinaBookConstant.BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.getValue());//业务类型来源
        params.put("userCode", getCurrentUser().getId());
        params.put("opDate", opDate);
        params.put("flag", "1");
        params.put("voucherState", voucherState);
        fcCarryService.deleteProfitYear(params);
        return success();
    }
    /**
     * 记账前时间校验
     * @param eqs_netId
     * @param eqd_opDate
     * @return
     */
    @LogMessage(content = "获取日结日期，和最近一年结转日期", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getCheckDate")
    @ResponseBody
    public Map<String, Object> checkDate(String eqs_netId, String eqd_opDate, Model model) {
        Map<String, Object> params = new HashedMap();
        params.put("netId", eqs_netId);
        params.put("opDate", eqd_opDate);
        params.put("carryType", CloseConstant.CarryType.CARRY_TYPE_INCOME.getValue());
        Map<String, Object> resultMap = new HashedMap();
        Date dayCloseDate = dayCloseService.getDayCloseFinalDate(eqs_netId);//日结日期
        FcCarry fcCarry = fcCarryService.getMaxCarryTimeFcCarryByNetId(params);//最近一次本年结转日期
        resultMap.put("dayCloseDate", dayCloseDate);
        resultMap.put("fcCarryTime", null != fcCarry ? fcCarry.getCarryTime() : "");
        resultMap.put("success", true);
        return resultMap;
    }
    /**
     * 获取上一次结转信息
     *
     * @param netId
     * @return
     */
    @RequestMapping(params = "getCarryDate")
    @ResponseBody
    public AjaxJson getCarryDate(String netId) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        //查询最近一次结转的日期
        FcCarry carry = fcCarryService.queryNoReverseFcCarries(netId, CloseConstant.CarryType.CARRY_TYPE_INCOME.get());
        ajaxJson.setObj(carry);
        return ajaxJson;
    }
}
