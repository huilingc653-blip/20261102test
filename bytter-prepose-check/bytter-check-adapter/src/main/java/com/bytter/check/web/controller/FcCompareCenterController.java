package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.entity.FcCheckInit;
import com.bytter.check.service.FcBankBillService;
import com.bytter.check.service.FcCheckInitService;
import com.bytter.check.service.FcCompareCenterService;
import com.bytter.check.vo.FcBankCheckSearchVo;
import com.bytter.check.vo.FcBankErpBalInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
/**   
 * @Title: Controller  
 * @Description: FC_COMPARE_ACC 中心对账
 * @author onlineGenerator
 * @date 2017-05-31 14:43:17
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fccomparecenter")
@Slf4j
public class FcCompareCenterController extends BaseController {
	@Autowired
	private FcCompareCenterService fcCompareCenterService;
	@Autowired
	private FcBankBillService fcBankBillService;
	@Autowired
	private FcCheckInitService fcCheckInitService;
	@DubboReference(check = false)
	private IBtBankAccService bankAccService;
	/**
	* FC_COMPARE_ACC列表 页面跳转
	*
	* @return
	*/
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
	@LogMessage(content = "查询FC_COMPARE_ACC列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(FcBankCheckSearchVo checkVO, Model model) {
		if (EmptyUtils.isNotEmpty(checkVO.getBankAcc())) {
			FcCheckInit fcCheckInit = fcCheckInitService.getCheckInitByAccount(checkVO.getBankAcc(), checkVO.getAccount(), FinacialConstant.CHECK_TYPE_CEN, checkVO.getCurId());
			Date carryDate = fcCheckInit.getCarryDate();
			if (EmptyUtils.isEmpty(carryDate)) {
				carryDate = fcCheckInit.getCheckDate();
			}
			BtBankAccDTO bankAccDTO = bankAccService.getBankAccInfo(checkVO.getBankAcc());
			checkVO.setCorpName(bankAccDTO.getCorpName());
			checkVO.setBankName(bankAccDTO.getBankName());
			model.addAttribute("checkVO", checkVO);//初始化网点
			model.addAttribute("carryDate", DateUtils.format(DateUtils.addDay(carryDate, 1), "yyyy-MM-dd"));//最小对账结束日期
			model.addAttribute("initNetOverDate",fcCompareCenterService.getDayOverByNetId(checkVO.getNetId()));//初始化网点日结日期
			model.addAttribute("notAuthNets",fcCompareCenterService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId()));//初始化网点
			return "check/fccomparecenter/addFcCompareAcc";
		} else {
			String initNetId = fcCompareCenterService.getInitNetId(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId());
			model.addAttribute("opType","init");
			model.addAttribute("initNetId",initNetId);//初始化网点
			model.addAttribute("notAuthNets",fcCompareCenterService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId()));//初始化网点
			model.addAttribute("initNetOverDate",fcCompareCenterService.getDayOverByNetId(initNetId));//初始化网点日结日期
			return "check/fccomparecenter/listFcCompareAcc";
		}
	}
    /**
    * FC_COMPARE_ACC table列表
    *
    * @return
    */
    ////@RequiresPermissions("finacial.check:fccompareacc:index")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcCompareCenterService.getDataTables(dataTablesInput);
    }
    
    /**
     * 
     * <b>功能介绍:获取未指定格式的银行帐号</b><br>
     * <b>日期：</b>2017年6月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @PostMapping("/getBankAccByOpType")
    @ResponseBody
    public List<Map<String,Object>> getBankAccByOpType(String bankAcc,String netCode,String natureCode){
    	return fcCompareCenterService.getBankAccByOpType(bankAcc,netCode,natureCode);
    }
    
    /**
     * 功能介绍:获取银行对账数据<br>
     * <b>日期：</b>2017年6月1日<br>
     * @version 1.0
     * @author caozx
     */
    public List<Map<String, Object>> queryBankCheckDatas(String bankAcc,String account,Date transDate,String[] compareSigns,BigDecimal beginMoney,BigDecimal endMoney) {
    	return fcBankBillService.queryBankCheckDatas(bankAcc,account,transDate,compareSigns,beginMoney,endMoney);
    }
    
    /**
     * 功能介绍:获取中心对账数据<br>
     * <b>日期：</b>2017年6月6日<br>
     * @version 1.0
     * @author caozx
     */
    public List<Map<String, Object>> queryCntrCheckDatas(String bankAcc,String account,Date transDate,String[] compareSigns,BigDecimal beginMoney,BigDecimal endMoney) {
    	return fcCompareCenterService.queryCntrCheckDatas(bankAcc,account,transDate,compareSigns,beginMoney,endMoney);
    }
    
    /**
     * 
     * <b>功能介绍:获取对账数据</b><br>
     * <b>日期：</b>2017年6月13日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/toLoadCheckDatas")
    @ResponseBody
    public List<Map<String,Object>> toLoadCheckDatas(String queryType,String bankAcc,String account,String transDate,String noHandRadios,String stayRadios,String handRadios,String checkRadios,String checkFlag,String beginMoney,String endMoney){
    	if(EmptyUtils.isEmpty(bankAcc)){
    		return new ArrayList();
    	}
    	if(EmptyUtils.isEmpty(account)){
    		return new ArrayList();
    	}
    	if(EmptyUtils.isEmpty(checkFlag)||"0".equals(checkFlag)){
    		//return new ArrayList();
    	}
    	
    	String[] compareSigns = new String[4];
    	if (FinacialConstant.VALID_TRUE.equals(noHandRadios)) {//未勾兑
            compareSigns[0] = FinacialConstant.COMPARE_SIGN_NO;
        }
        if (FinacialConstant.VALID_TRUE.equals(stayRadios)) {//未达
            compareSigns[1] = FinacialConstant.COMPARE_SIGN_STAY;
        }
        if (FinacialConstant.VALID_TRUE.equals(handRadios)) {//手动勾兑
            compareSigns[2] = FinacialConstant.COMPARE_SIGN_HAND;
        }
        if (FinacialConstant.VALID_TRUE.equals(checkRadios)) {//已对账
            compareSigns[3] = FinacialConstant.COMPARE_SIGN_YES;
        }
		log.info("transDate= "+transDate+" ;");
        Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		BigDecimal beginM = new BigDecimal(0);
		BigDecimal endM = new BigDecimal(0);
        if(StringUtils.isNotEmpty(beginMoney) && StringUtils.isNotEmpty(endMoney)){
			beginM =NumberUtil.objToBigDecimal(beginMoney);
			endM =NumberUtil.objToBigDecimal(endMoney);
		}
        if("0".equals(queryType)){//中心对账数据
        	return queryCntrCheckDatas(bankAcc,account,transDate_s,compareSigns,beginM,endM);
        }else{//银行对账数据
        	return queryBankCheckDatas(bankAcc,account,transDate_s,compareSigns,beginM,endM);
        }
    }
    
    /**
     * <b>功能介绍:</b>获取调节平衡余额<br>
     * <b>日期：</b>2017年6月8日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @RequestMapping(params = "toLoadAdjustBalance")
	@ResponseBody
    public AjaxJson toLoadAdjustBalance(String account,String bankAcc,String checkDate,String transDate,String paramType,String blnBalance,String bankBalance){
    	AjaxJson json=new AjaxJson();
    	Map<String, Object> model =  new HashMap<String, Object>();
    	if(StringUtils.isBlank(bankAcc)){
    		 model.put("paramType", paramType);
    		 json.setObj(model);
    		 return json;
    	}
        Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
        
        
        BigDecimal blnBalance_s = BigDecimal.valueOf(Double.valueOf(blnBalance.trim()));//中心账面余额
        BigDecimal bankBalance_s = BigDecimal.valueOf(Double.valueOf(bankBalance.trim()));//银行账面余额
        
        if(FinacialConstant.BOOLEAN_TRUE.equals(paramType)){//ERP对账
        }else if(FinacialConstant.BOOLEAN_FALSE.equals(paramType)){//中心对账
            //获取银行账号信息
            if (StringUtils.isBlank(account)) {
            	json.setObj(model);
                return json;
            }
            model = fcCompareCenterService.loadAdjustBalance(blnBalance_s, bankBalance_s, bankAcc, account, transDate_s);
        }
        model.put("transDateStr", transDate);
        model.put("paramType", paramType);
        json.setObj(model);
        json.setSuccess(true);
		return json;
    }
    
    /**
     * 功能介绍:获取账面余额<br>
     * <b>日期：</b>2017年6月8日<br>
     * @version 1.0
     * @author caozx
     */
    @RequestMapping(params = "getAccBalance")
	@ResponseBody
	public AjaxJson getAccBalance(String account, String bankAcc,String transDate,String paramType,String accDisplayType, String curId){
		AjaxJson json=new AjaxJson();
		Map<String, Object> model = new HashMap<String, Object>();
		FcBankErpBalInfoVO balInfo;
		try {
			balInfo = fcCompareCenterService.getAccBalInfo(account, bankAcc, transDate, paramType, accDisplayType, curId);
			model.put("blnBalance", balInfo.getAccBal());
			model.put("bankBalance", balInfo.getBankBal());
			model.put("beginDate", DateUtils.date2Str(DateUtils.addDay(balInfo.getLastOverDate(), 1), DateUtils.date_sdf()));
			model.put("lastOverDate", DateUtils.date2Str(balInfo.getLastOverDate(),DateUtils.date_sdf()));
			json.setObj(model);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof ParameterException){
				json.setMessageEnum(((ParameterException) e).getIMessageEnum());
				json.setParams(((ParameterException) e).getParams());
			}
			json.setSuccess(false);
		}
		return json;
	}
    
    /**
     * 功能介绍:对账操作处理<br>
     * <b>日期：</b>2017年6月8日<br>
     * @version 1.0
     * @author caozx
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
	@PostMapping("/docheck")
	@ResponseBody
	public AjaxJson docheck(FcBankCheckSearchVo fca,Date checkDate){
		AjaxJson json=new AjaxJson();
		try {
			fcCompareCenterService.doCheck(fca,checkDate,getCurrentUser());
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 
	 * <b>功能介绍:手工勾兑操作</b><br>
	 * <b>日期：</b>2017年6月12日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
    //只能做前端权限控制
	@RequestMapping(params = "doHandOver")
	@ResponseBody
	public AjaxJson doHandOver(String account,String bankAcc,String paramType,String checkDate,String transDate,
			String opType,String ctrPk,String bankPk){
		AjaxJson json=new AjaxJson();
		Date chekDate_s = DateUtils.str2Date(checkDate, DateUtils.date_sdf());
		Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		ctrPk = ctrPk==null?"":ctrPk;
		bankPk = bankPk==null?"":bankPk;
		if(FinacialConstant.BOOLEAN_FALSE.equals(paramType)){//银行-中心对账
			fcCompareCenterService.doHandOver(account,bankAcc,ctrPk,bankPk,chekDate_s,transDate_s,opType,paramType,getCurrentUser());
		}
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 
	 * <b>功能介绍:结转操作</b><br>
	 * <b>日期：</b>2017年6月14日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @throws Exception 
	 */
	@RequestMapping(params = "doAdjustOverOther")
	@ResponseBody
	public AjaxJson doAdjustOverOther(FcBankCheckSearchVo fca,String checkDate) throws Exception{
		AjaxJson json=new AjaxJson();
		String adjustId = fcCompareCenterService.doAdjustOverOther(fca,DateUtils.str2Date(checkDate, DateUtils.date_sdf()),getCurrentUser(),"1");
		json.setObj(adjustId);
		json.setSuccess(true);
		return json;
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
	/**
	 * 导出全部
	 *
	 * @return
	 */
	@RequestMapping("/export")
	public void export(HttpServletRequest request, HttpServletResponse response, String queryType,String bankAcc,String account,
						  String transDate,String noHandRadios,String stayRadios,String handRadios,String checkRadios,String checkFlag,String beginMoney,String endMoney) throws Exception {
		List<Map<String,Object>> list;
		String[] compareSigns = new String[4];
		if (FinacialConstant.VALID_TRUE.equals(noHandRadios)) {//未勾兑
			compareSigns[0] = FinacialConstant.COMPARE_SIGN_NO;
		}
		if (FinacialConstant.VALID_TRUE.equals(stayRadios)) {//未达
			compareSigns[1] = FinacialConstant.COMPARE_SIGN_STAY;
		}
		if (FinacialConstant.VALID_TRUE.equals(handRadios)) {//手动勾兑
			compareSigns[2] = FinacialConstant.COMPARE_SIGN_HAND;
		}
		if (FinacialConstant.VALID_TRUE.equals(checkRadios)) {//已对账
			compareSigns[3] = FinacialConstant.COMPARE_SIGN_YES;
		}
		Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		BigDecimal beginM = new BigDecimal(0);
		BigDecimal endM = new BigDecimal(0);
		if(StringUtils.isNotEmpty(beginMoney) && StringUtils.isNotEmpty(endMoney)){
			beginM =NumberUtil.objToBigDecimal(beginMoney);
			endM =NumberUtil.objToBigDecimal(endMoney);
		}
		String title = "中心对账";
		if(EmptyUtils.isEmpty(bankAcc) || EmptyUtils.isEmpty(account) || EmptyUtils.isEmpty(checkFlag)||"0".equals(checkFlag)){
			list = new ArrayList<>();
			fcCompareCenterService.export(request,response,list,title);
		}else {
			if ("0".equals(queryType)) {//中心对账数据
				list = queryCntrCheckDatas(bankAcc, account, transDate_s, compareSigns, beginM, endM);
				title = "中心对账";
				fcCompareCenterService.export(request,response,list,title);
			} else {//银行对账数据
				list = queryBankCheckDatas(bankAcc, account, transDate_s, compareSigns, beginM, endM);
				title = "银行对账";
				fcCompareCenterService.exportBank(request,response,list,title);
			}
		}
	}
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportBill")
    public void exportBill(HttpServletRequest request, HttpServletResponse response, String queryType,String bankAcc,String account,
                       String transDate) throws Exception {
        List<Map<String,Object>> list;
        String[] compareSigns = new String[]{"0","1", "2", "4"};
        Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
        String title = "中心对账";
        if(EmptyUtils.isEmpty(bankAcc) || EmptyUtils.isEmpty(account)){
            list = new ArrayList<>();
            fcCompareCenterService.export(request,response,list, title);
        }else {
            if ("0".equals(queryType)) {//中心对账数据
                list = queryCntrCheckDatas(bankAcc, account, transDate_s, compareSigns, null, null);
                title = "中心对账";
                fcCompareCenterService.export(request,response,list,title);
            } else {//银行对账数据
                list = queryBankCheckDatas(bankAcc, account, transDate_s, compareSigns, null, null);
                title = "银行对账";
                fcCompareCenterService.exportBank(request,response,list,title);
            }
        }
    }
}
