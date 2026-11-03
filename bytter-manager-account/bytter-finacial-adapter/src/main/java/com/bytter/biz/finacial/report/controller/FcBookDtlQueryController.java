package com.bytter.biz.finacial.report.controller;
import com.bytter.biz.finacial.report.service.FcBookDtlQueryService;
import com.bytter.biz.finacial.report.vo.FcBookDtlBatchPrintVO;
import com.bytter.biz.finacial.report.vo.FcBookDtlListVO;
import com.bytter.biz.finacial.report.vo.FcBookDtlSearchVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
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
import java.util.*;
@Controller
@RequestMapping("/finacial/report/fcbookdtlquery")
public class FcBookDtlQueryController extends BaseController{
	@Autowired
	private FcBookDtlQueryService fcBookDtlQueryService;
	/**
	 * 明细账页面跳转
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_FCBOOKDTL_QUERY)
	@LogMessage(content = "明细账查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model){
		return "finacial/report/fcBookDtlList";
	}
	/**
	 * 明细账查询
	 * @param searchVO
	 * @param model
	 * @param start
	 * @param subjectId
	 * @param querySign
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_FCBOOKDTL_QUERY)
	@LogMessage(content = "明细账表头查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/loadData")
	@ResponseBody
	public DataTablesOutput loadData(FcBookDtlSearchVO searchVO,Model model,Integer start,String subjectId, String querySign) {
        if (EmptyUtils.isEmpty(querySign)) {
            return new DataTablesOutput();
        }
		DataTablesInput input = new DataTablesInput();
		DataTablesOutput output = null;
		input.setStart(start);
		getQueryParam(model,input,searchVO);
		return fcBookDtlQueryService.queryAccbookDtls(input, ResourceConstant.FINACIAL_FCBOOKDTL_QUERY, getCurrentUser());
	}
	/**
	 * 查询参数
	 * @param input
	 * @return
	 */
	public DataTablesInput getQueryParam(Model model,DataTablesInput input,FcBookDtlSearchVO vo){
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
		if(!EmptyUtils.isBlank(vo.getAccount())){//帐号
			input.addFilter(MatchTypeEnum.EQ, "account", vo.getAccount());
			model.addAttribute("account", vo.getAccount());
		}
		if(!EmptyUtils.isBlank(vo.getLesVoucherNo())){//凭证号
			input.addFilter(MatchTypeEnum.LE, "voucherNo", vo.getLesVoucherNo());
			model.addAttribute("lesVoucherNo", vo.getLesVoucherNo());
		}
		if(!EmptyUtils.isBlank(vo.getGesVoucherNo())){//凭证号
			input.addFilter(MatchTypeEnum.GE, "voucherNo", vo.getGesVoucherNo());
			model.addAttribute("gesVoucherNo", vo.getGesVoucherNo());
		}
		if(!EmptyUtils.isBlank(vo.getCurCode())){//币别
			input.addFilter(MatchTypeEnum.EQ, "curCode", vo.getCurCode());
			model.addAttribute("curCode", vo.getCurCode());
		}
		if(!EmptyUtils.isEmpty(vo.getDebitMoney())){//借方发生额
			input.addFilter(MatchTypeEnum.EQ, "debitMoney", vo.getDebitMoney());
			model.addAttribute("gesDebitMoney", vo.getDebitMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getCreditMoney())){//贷方发生额
			input.addFilter(MatchTypeEnum.EQ, "creditMoney", vo.getCreditMoney());
			model.addAttribute("gesCreditMoney", vo.getCreditMoney());
		}
		if(!EmptyUtils.isBlank(vo.getVoucherState())){//凭证状态
			input.addFilter(MatchTypeEnum.EQ, "voucherState", vo.getVoucherState());
			model.addAttribute("voucherState", vo.getVoucherState());
		}
		if(!EmptyUtils.isBlank(vo.getSubjectId())){//科目
			input.addFilter(MatchTypeEnum.EQ, "subjectId", vo.getSubjectId());
			model.addAttribute("subjectId", vo.getSubjectId());
		}
		return input;
	}
	/**
	 * 导出
	 * **/
	@LogMessage(content = "导出明细账查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
	@PostMapping("/exportExcel")
	public void doExportExcel(HttpServletRequest request, HttpServletResponse response, FcBookDtlSearchVO vo, Model model) throws Exception{
		DataTablesInput input = new DataTablesInput();
		getQueryParam(model,input,vo);
		// 导出的文件名
		String fileName = "明细账查询表.xls";
		if (fcBookDtlQueryService.isIE(request)) {//判断浏览器类型
			fileName = URLEncoder.encode(fileName, "UTF8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		//导出数据
		HSSFWorkbook workbook = fcBookDtlQueryService.createExcelContent(input, ResourceConstant.FINACIAL_FCBOOKDTL_QUERY, getUserId());
		//设置导出流
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setHeader("Pragma", "No-cache");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	/**
	 * 批量打印
	 *
	 * @return
	 */
	@GetMapping("/batchPrint")
	public String batchPrint(FcBookDtlSearchVO vo, Model model) {
		DataTablesInput input = new DataTablesInput();
		getQueryParam(model,input,vo);
		Map<String,FcBookDtlBatchPrintVO> map = new LinkedHashMap<>();
		List<FcBookDtlListVO> list = fcBookDtlQueryService.batchPrint(input,ResourceConstant.FINACIAL_FCBOOKDTL_QUERY,getUserId());
		for(FcBookDtlListVO fcBookDtlListVO :list){
			if(map.containsKey(fcBookDtlListVO.getAccount())){
				FcBookDtlBatchPrintVO  fcBookDtlBatchPrintVO = map.get(fcBookDtlListVO.getAccount());
				List<FcBookDtlListVO> childList = fcBookDtlBatchPrintVO.getFcBookDtlListVOList();
				fcBookDtlBatchPrintVO.setSumCreditMoney(fcBookDtlBatchPrintVO.getSumCreditMoney().add(fcBookDtlListVO.getCreditMoney()));
				fcBookDtlBatchPrintVO.setSumDebitMoney(fcBookDtlBatchPrintVO.getSumDebitMoney().add(fcBookDtlListVO.getDebitMoney()));
				childList.add(fcBookDtlListVO);
			}else{
				FcBookDtlBatchPrintVO  fcBookDtlBatchPrintVO = new FcBookDtlBatchPrintVO();
				fcBookDtlBatchPrintVO.setFcBookDtlListVO(fcBookDtlListVO);
				List<FcBookDtlListVO> childList = new ArrayList<>();
				childList.add(fcBookDtlListVO);
				fcBookDtlBatchPrintVO.setFcBookDtlListVOList(childList);
				fcBookDtlBatchPrintVO.setSumCreditMoney(fcBookDtlListVO.getCreditMoney());
				fcBookDtlBatchPrintVO.setSumDebitMoney(fcBookDtlListVO.getDebitMoney());
				map.put(fcBookDtlListVO.getAccount(),fcBookDtlBatchPrintVO);
			}
		}
 		model.addAttribute("map", map);
		model.addAttribute("size", map.size());
		return "finacial/report/fcBookDtlBatchPrint";
	}
}
