package com.bytter.biz.finacial.report.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.report.service.FcAccrualRecordQueryService;
import com.bytter.biz.finacial.report.vo.FcLedgerSearchVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
@Controller
@RequestMapping("/finacial/report/fcaccrualrecordquery")
public class FcAccrualRecordQueryController extends BaseController{
	
	@Autowired
	private FcAccrualRecordQueryService fcAccrualRecordQueryService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	
	/**
	 * 手续费结转查询页面跳转
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_ACCRUALRECORD_QUERY)
	@LogMessage(content = "手续费结转记录查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model){
		return "finacial/report/fcAccrualRecordQuery";
	}
	
	/**
	 * 手续费结转查询列表
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_ACCRUALRECORD_QUERY)
	@LogMessage(content = "手续费结转查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(FcLedgerSearchVO searchVO,Model model,Integer start) {
		DataTablesInput input = new DataTablesInput();
		DataTablesOutput output = null;
		input.setStart(start);
		getQueryParam(model,input,searchVO);
		return fcAccrualRecordQueryService.queryFcAccrualRecordData(input, ResourceConstant.FINACIAL_ACCRUALRECORD_QUERY, getCurrentUser());
	}
	
	/**
	 * 查询参数
	 * @param input
	 * @return
	 */
	public DataTablesInput getQueryParam(Model model,DataTablesInput input,FcLedgerSearchVO vo){
		if(!EmptyUtils.isBlank(vo.getNetId())){//网点
			input.addFilter(MatchTypeEnum.EQ, "netId", vo.getNetId());
			model.addAttribute("netId", vo.getNetId());
		}
		if(!EmptyUtils.isEmpty(vo.getLedTallyDate())){//
			input.addFilter(MatchTypeEnum.LE, "tallyDate", DateUtils.formatDate(vo.getLedTallyDate()), Date.class);//大于等于记账开始日期
			model.addAttribute("ledTallyDate", DateUtils.date2Str(vo.getLedTallyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getGedTallyDate())){//
			input.addFilter(MatchTypeEnum.GE, "tallyDate", DateUtils.formatDate(vo.getGedTallyDate()), Date.class);//小于等于记账结束日期
			model.addAttribute("gedTallyDate", DateUtils.date2Str(vo.getGedTallyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isBlank(vo.getCurCode())){//币别
			BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(vo.getCurCode());
			if (ObjectUtil.isNotEmpty(currencyDTO)){
				input.addFilter(MatchTypeEnum.EQ, "curCode", currencyDTO.getCurCode());
				input.addFilter(MatchTypeEnum.EQ, "curId", currencyDTO.getId());
			}
			model.addAttribute("curCode", vo.getCurCode());
		}
		return input;
	}
	/**
	 * 导出
	 * **/
	@LogMessage(content = "手续费结转记录查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
	@PostMapping("/exportExcel")
	public void doExportExcel(HttpServletRequest request, HttpServletResponse response, FcLedgerSearchVO vo, Model model) throws Exception{
		DataTablesInput input = new DataTablesInput();
		getQueryParam(model,input,vo);
		// 导出的文件名
		String fileName = "手续费结转记录查询表.xls";
		if (fcAccrualRecordQueryService.isIE(request)) {//判断浏览器类型
			fileName = URLEncoder.encode(fileName, "UTF8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		//导出数据
		HSSFWorkbook workbook = fcAccrualRecordQueryService.createExcelContent(input, ResourceConstant.FINACIAL_ACCRUALRECORD_QUERY, getUserId());
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
