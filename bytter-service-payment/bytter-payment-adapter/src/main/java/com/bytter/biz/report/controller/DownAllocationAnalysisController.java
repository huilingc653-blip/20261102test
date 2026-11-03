package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.DownAllocationAnalysisService;
import com.bytter.biz.report.vo.DownAllocationAnalysisListVO;
import com.bytter.biz.report.vo.DownAllocationAnalysisSearchVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
/**
 * @author zuoy
 * @Description 资金调拨明细表
 * @date 2017.10.10
 */
@Controller
@RequestMapping("/report/downAllocationAnalysis")
public class DownAllocationAnalysisController extends BaseController {
	@Autowired
	private DownAllocationAnalysisService downAllocationAnalysisService;
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_DOWNALLOCATIONANALYSIS_QUERY)
	@GetMapping("/list")
	public String list(Model model) {
		return "report/listDownAllocationAnalysis";
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_DOWNALLOCATIONANALYSIS_QUERY)
	@LogMessage(content = "下拨金额分析查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/loadData")
	@ResponseBody
	public DataTablesOutput loadData(DownAllocationAnalysisSearchVO searchVO, Model model, Integer start) {
		DataTablesInput input = new DataTablesInput();
		DataTablesOutput output = null;
		if(null == start){
			start = 1;
		}
		input.setStart(start);
		getQueryParam(model,input,searchVO);
		return downAllocationAnalysisService.queryAccbookDtls(input, ResourceConstant.NETBANK_REPORT_DOWNALLOCATIONANALYSIS_QUERY, getCurrentUser());
	}
	/**
	 * 查询参数
	 * @param input
	 * @return
	 */
	public DataTablesInput getQueryParam(Model model,DataTablesInput input,DownAllocationAnalysisSearchVO vo){
		if(!EmptyUtils.isBlank(vo.getNetId())){//网点
			input.addFilter(MatchTypeEnum.EQ, "netId", vo.getNetId());
			model.addAttribute("netId", vo.getNetId());
		}
		if(!EmptyUtils.isBlank(vo.getCorpId())){//单位
			input.addFilter(MatchTypeEnum.IN, "corpId", vo.getCorpId());
			model.addAttribute("CorpId", vo.getCorpId());
		}
		if(!EmptyUtils.isEmpty(vo.getLedApplyDate())){//申请日期
			input.addFilter(MatchTypeEnum.LT, "applyDate", DateUtils.formatDate(DateUtils.rollDay(vo.getLedApplyDate(),1)),Date.class);//小于等于申请日期
			model.addAttribute("ledApplyDate", DateUtils.date2Str(vo.getLedApplyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getGedApplyDate())){//申请日期
			input.addFilter(MatchTypeEnum.GE, "applyDate", DateUtils.formatDate(vo.getGedApplyDate()), Date.class);//大于等于申请日期
			model.addAttribute("gedApplyDate", DateUtils.date2Str(vo.getGedApplyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getVoucherType())){//付款方式
			input.addFilter(MatchTypeEnum.EQ, "voucherType", vo.getVoucherType());
			model.addAttribute("voucherType", vo.getVoucherType());
		}else{
			input.addFilter(MatchTypeEnum.IN,"voucherType",NetbankConstant.PAY_TYPE_02 +","+ NetbankConstant.PAY_TYPE_10);
		}
		if(!EmptyUtils.isEmpty(vo.getGegPayMoney())){//金额
			input.addFilter(MatchTypeEnum.GE, "payMoney", vo.getGegPayMoney());//大于等于开始金额
			model.addAttribute("gegPayMoney", vo.getGegPayMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getLegPayMoney())){//金额
			input.addFilter(MatchTypeEnum.LE, "payMoney", vo.getLegPayMoney());//小于等于开始金额
			model.addAttribute("legPayMoney", vo.getLegPayMoney());
		}
		return input;
	}
	/**
	 * 查看详情页面
	 * @param billNo
	 * @param model
	 * @return
	 */
	@LogMessage(content = "查询下拨余额详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(String billNo, String payerBank,Model model) {
		DownAllocationAnalysisListVO downAllocationAnalysis = downAllocationAnalysisService.getByUnion(billNo,payerBank);
		model.addAttribute("downAllocationAnalysisPage", downAllocationAnalysis);
		return "report/viewDownAllocationAnalysis";
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
									 HttpServletResponse response,DownAllocationAnalysisSearchVO searchVO, Model model) throws Exception{
		String filename = downAllocationAnalysisService.getLangStr("downAllocationAnalysis.detailInformation") + ".xls";//下拨金额分析.xls
		if(isIE(request)) {//判断浏览器类型
			filename = URLEncoder.encode(filename, "UTF8");
		} else {
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		response.setHeader("Pragma", "No-cache");
		getQueryParam(model,input,searchVO);
		HSSFWorkbook workbook = downAllocationAnalysisService.doExportInterestBillXls(input, ResourceConstant.NETBANK_REPORT_DOWNALLOCATIONANALYSIS_QUERY,getUserId(),impType);
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
}
