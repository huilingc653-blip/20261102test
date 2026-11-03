package com.bytter.check.web.controller;
import com.bytter.check.service.FcIncomStatementService;
import com.bytter.check.vo.FcIncomStatementVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/finacial/check/fcIncomStatement")
public class FcIncomStatementController extends BaseController{
	
	@Autowired
	private FcIncomStatementService fcIncomStatementService;
	//@RequiresPermissions(ResourceConstant.FINACIAL_INCOMSTATEMEN_QUERY)
	@LogMessage(content = "查询损益表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model){
		return "check/fcCompareSeach/listFcIncomeStatement";
	}
	//@RequiresPermissions(ResourceConstant.FINACIAL_BANLANCESHEET_QUERY)
	@LogMessage(content = "查询资产负债表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listFcBanlanceSheet")
	public String listFcBanlanceSheet(Model model){
		return "check/fcCompareSeach/listFcBanlanceSheet";
	}
	
	//损益表datatable
	//@RequiresPermissions(ResourceConstant.FINACIAL_INCOMSTATEMEN_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput datatable(@RequestBody DataTablesInput input){
		return fcIncomStatementService.getFcIncomeStatement(input);
	}
	
	//资产负债表datatable
	//@RequiresPermissions(ResourceConstant.FINACIAL_BANLANCESHEET_QUERY)
	@PostMapping("/datatableSheet")
	@ResponseBody
	public DataTablesOutput datatableSheet(@RequestBody DataTablesInput input){
		return fcIncomStatementService.getFcBanlanceSheet(input);
	}
	
	/**
	 * 查询参数
	 * @param input
	 * @return
	 */
	public DataTablesInput getQueryParam(Model model, DataTablesInput input, FcIncomStatementVO vo){
		if(!EmptyUtils.isBlank(vo.getEqs_netCode())){//网点
			input.addFilter(MatchTypeEnum.EQ, "netCode", vo.getEqs_netCode());
			model.addAttribute("netCode", vo.getEqs_netCode());
		}
		if(!EmptyUtils.isBlank(vo.getEqs_curCode())){//币别
			input.addFilter(MatchTypeEnum.EQ, "curCode",vo.getEqs_curCode());
			model.addAttribute("curCode", vo.getEqs_curCode());
		}
		if(!EmptyUtils.isEmpty(vo.getCheckDate())){//申请日期
			input.getFormdata().put("checkDate", vo.getCheckDate());
			model.addAttribute("checkDate", vo.getCheckDate());
		}
		if(!EmptyUtils.isEmpty(vo.getIsConvert())){//是否折算本位币
			input.getFormdata().put("isConvert", vo.getIsConvert());
			model.addAttribute("isConvert", vo.getIsConvert());
			if (CommonConstant.YES.equals(vo.getIsConvert())) {
				input.getFormdata().put("rateType", vo.getRateType());
			}
		}
		if(!EmptyUtils.isEmpty(vo.getPreDate())){//申请日期
			input.getFormdata().put("preDate", vo.getPreDate());
			model.addAttribute("preDate", vo.getPreDate());
		}else {
			String date = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
			input.getFormdata().put("preDate", date);
			model.addAttribute("preDate", date);
			vo.setPreDate(date);
		}
		if(!EmptyUtils.isEmpty(vo.getConvertCurId())){//申请日期
			input.getFormdata().put("convertCurId", vo.getConvertCurId());
			model.addAttribute("convertCurId", vo.getConvertCurId());
		}
		return input;
	}
	
	/**
	 * 导出
	 * @param input
	 * @param impType
	 * @param request
	 * @param response
	 * @param searchVO
	 * @param model
	 * @throws Exception
	 */
	@PostMapping("/doExportInterestBill")
	public void doExportInterestBill(DataTablesInput input,String impType,HttpServletRequest request,
									 HttpServletResponse response,FcIncomStatementVO searchVO, Model model) throws Exception{
		String filename =null;
		if(impType.equals("1")){
			 filename = fcIncomStatementService.getLangStr("fcIncomStatement.list") + ".xls";//损益表.xls
		}else{
			filename = fcIncomStatementService.getLangStr("fcBalanceSheet.list") + ".xls";//资产负债表.xls
		}
		if(isIE(request)) {//判断浏览器类型
			filename = URLEncoder.encode(filename, "UTF8");
		} else {
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		response.setHeader("Pragma", "No-cache");
		getQueryParam(model,input,searchVO);
		HSSFWorkbook workbook = fcIncomStatementService.doExportInterestBillXls(input,impType);
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	/**
	 * 判断是否是IE
	 * @param request
	 * @return
	 */
	private boolean isIE(HttpServletRequest request) {
		return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
	}
	/**
	 * 汇率这算校验
	 * @param convertCurId
	 * @param rateType
	 * @param netCode
	 * @param yearMonth
	 * @param preDate
	 * @param oriCurId
	 * @param banlanceSheet
	 * @return
	 */
	@PostMapping("/checkCurRate")
	@ResponseBody
	public AjaxJson checkCurRate(String convertCurId, String rateType,String netCode,String yearMonth,
								 @RequestParam(required = false) Date preDate,
								 @RequestParam(required = false) String oriCurId,
								 @RequestParam(required = false) String banlanceSheet){
		if (null == preDate) {
			preDate = new Date();
		}
		if (StringUtils.isEmpty(rateType)) {
			rateType = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
		}
		fcIncomStatementService.checkConvertCurId(oriCurId, convertCurId, preDate, rateType, banlanceSheet);
		return success();
	}
}
