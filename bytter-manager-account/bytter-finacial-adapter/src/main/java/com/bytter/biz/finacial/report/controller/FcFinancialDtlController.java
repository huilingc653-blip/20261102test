package com.bytter.biz.finacial.report.controller;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
import com.bytter.biz.finacial.report.service.FcFinancialDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
/**
 * @Title: Controller
 * @Description: FC_ACCBOOK
 * @author onlineGenerator
 * @date 2018-01-08 15:02:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/report/fcfinacialdtl")
public class FcFinancialDtlController extends BaseController {
	@Autowired
	private FcFinancialDtlService fcFinancialDtlService;
	/**
	 * FC_ACCBOOK列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_FINACIALDTL_QUERY)
	@LogMessage(content = "流水账查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(VFcAccbookAndBlnedsVO vo,Model model, Integer start, Integer len) {
		DataTablesInput input = new DataTablesInput();
		String opt = vo.getOpt();
		DataTablesOutput output = null;
		if (null == start) {
			start = Integer.valueOf(1);
		}
		if (null != len) {
			input.setLength(len);//页面数据条数
		}
		input.setStart((start - 1) * input.getLength());
		if("query".equals(opt)){
			getQueryParam(model,input,vo);
			output = fcFinancialDtlService.queryFcAccbookAndBlnedsData(input, ResourceConstant.FINACIAL_FINACIALDTL_QUERY, getCurrentUser());
		}else{
			output = new DataTablesOutput();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		long resultCount = output.getRecordsTotal();
		int pageSize = input.getLength();
		int pageCount = 0;
		if (resultCount % pageSize == 0) {
			//整除则直接取整相除
			pageCount = (int) (resultCount / pageSize);
		} else {
			//否则取整相除后加一
			pageCount = (int) ((resultCount / pageSize) + 1);
		}
		
		result.put("bookAndBlneds", output.getData());
		result.put("total", output.getRecordsTotal());
		result.put("curr", start);
		result.put("len", input.getLength());
		model.addAttribute("model", result);
		return "finacial/report/listFcAccbookAndBlned";
	}
	
	/**
	 * 查询参数
	 * @param input
	 * @return
	 */
	public DataTablesInput getQueryParam(Model model,DataTablesInput input,VFcAccbookAndBlnedsVO vo){
		if(!EmptyUtils.isEmpty(vo.getNetCode())){//网点
			input.addFilter(MatchTypeEnum.EQ, "netId", vo.getNetCode());
			model.addAttribute("netId", vo.getNetCode());
		}
		if(!EmptyUtils.isEmpty(vo.getSysRaceCode())){//系统业务种类
			input.addFilter(MatchTypeEnum.EQ, "sysRaceCode", vo.getSysRaceCode());
			model.addAttribute("sysRaceCode", vo.getSysRaceCode());
		}
		if(!EmptyUtils.isEmpty(vo.getLedTallyDate())){//
			input.addFilter(MatchTypeEnum.LE, "tallyDate", DateUtils.formatDate(vo.getLedTallyDate()), Date.class);//大于等于记账开始日期
			model.addAttribute("ledTallyDate", DateUtils.date2Str(vo.getLedTallyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getGedTallyDate())){//
			input.addFilter(MatchTypeEnum.GE, "tallyDate", DateUtils.formatDate(vo.getGedTallyDate()), Date.class);//小于等于记账结束日期
			model.addAttribute("gedTallyDate", DateUtils.date2Str(vo.getGedTallyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getAccount())){//帐号
			input.addFilter(MatchTypeEnum.EQ, "account", vo.getAccount());
			model.addAttribute("account", vo.getAccount());
		}
		if(!EmptyUtils.isEmpty(vo.getLesVoucherNo())){//凭证号
			input.addFilter(MatchTypeEnum.LE, "voucherNo", vo.getLesVoucherNo());
			model.addAttribute("lesVoucherNo", vo.getLesVoucherNo());
		}
		if(!EmptyUtils.isEmpty(vo.getGesVoucherNo())){//凭证号
			input.addFilter(MatchTypeEnum.GE, "voucherNo", vo.getGesVoucherNo());
			model.addAttribute("gesVoucherNo", vo.getGesVoucherNo());
		}
		if(!EmptyUtils.isEmpty(vo.getVoucherType())){//结算方式
			input.addFilter(MatchTypeEnum.EQ, "voucherType", vo.getVoucherType());
			model.addAttribute("voucherType", vo.getVoucherType());
		}
		if(!EmptyUtils.isEmpty(vo.getCurCode())){//币别
			input.addFilter(MatchTypeEnum.EQ, "curCode", vo.getCurCode());
			model.addAttribute("curCode", vo.getCurCode());
		}
		if(!EmptyUtils.isEmpty(vo.getLesOrderNo())){//流水号
			input.addFilter(MatchTypeEnum.LE, "orderNo", vo.getLesOrderNo());
			model.addAttribute("lesOrderNo", vo.getLesOrderNo());
		}
		if(!EmptyUtils.isEmpty(vo.getGesOrderNo())){//流水号
			input.addFilter(MatchTypeEnum.GE, "orderNo", vo.getGesOrderNo());
			model.addAttribute("gesOrderNo", vo.getGesOrderNo());
		}
		if(!EmptyUtils.isEmpty(vo.getOrigVoucherNo())){//原始凭证号
			input.addFilter(MatchTypeEnum.EQ, "origVoucherNo", vo.getOrigVoucherNo());
			model.addAttribute("origVoucherNo", vo.getOrigVoucherNo());
		}
		if(!EmptyUtils.isEmpty(vo.getGesDebitMoney())){//借方发生额
			input.addFilter(MatchTypeEnum.GE, "debitMoney", vo.getGesDebitMoney());
			model.addAttribute("gesDebitMoney", vo.getGesDebitMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getLesDebitMoney())){//借方发生额
			input.addFilter(MatchTypeEnum.LE, "debitMoney", vo.getLesDebitMoney());
			model.addAttribute("lesDebitMoney", vo.getLesDebitMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getGesCreditMoney())){//贷方发生额
			input.addFilter(MatchTypeEnum.GE, "creditMoney", vo.getGesCreditMoney());
			model.addAttribute("gesCreditMoney", vo.getGesCreditMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getLesCreditMoney())){//贷方发生额
			input.addFilter(MatchTypeEnum.LE, "creditMoney", vo.getLesCreditMoney());
			model.addAttribute("lesCreditMoney", vo.getLesCreditMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getVoucherState())){//凭证状态
			input.addFilter(MatchTypeEnum.EQ, "voucherState", vo.getVoucherState());
			model.addAttribute("voucherState", vo.getVoucherState());
		}
		if(!EmptyUtils.isEmpty(vo.getOperatorCode())){//记账人
			input.addFilter(MatchTypeEnum.LIKE, "operatorLoginName", vo.getOperatorCode());
			model.addAttribute("operatorCode", vo.getOperatorCode());
		}
		if(!EmptyUtils.isEmpty(vo.getCheckerCode())){//复核人
			input.addFilter(MatchTypeEnum.LIKE, "checkLoginName", vo.getCheckerCode());
			model.addAttribute("checkerCode", vo.getCheckerCode());
		}
		if(!EmptyUtils.isEmpty(vo.getSummaryContent())){//摘要
			input.addFilter(MatchTypeEnum.LIKE, "summaryContent", vo.getSummaryContent());
			model.addAttribute("summaryContent", vo.getSummaryContent());
		}
		return input;
	}
	/**
	 * 导出
	 * **/
	@LogMessage(content = "导出流水账查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
	@PostMapping("/exportExcel")
	public void doExportExcel(HttpServletRequest request, HttpServletResponse response, VFcAccbookAndBlnedsVO vo,Model model) throws Exception{
		DataTablesInput input = new DataTablesInput();
		getQueryParam(model,input,vo);
		// 导出的文件名
		String fileName = "流水账查询表.xls";
		if (fcFinancialDtlService.isIE(request)) {//判断浏览器类型
			fileName = URLEncoder.encode(fileName, "UTF8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		//导出数据
		HSSFWorkbook workbook = fcFinancialDtlService.createExcelContent(input, ResourceConstant.FINACIAL_FINACIALDTL_QUERY, getUserId());
		//设置导出流
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setHeader("Pragma", "No-cache");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	
}
