package com.bytter.biz.finacial.report.controller;

import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.report.service.AccountChangeService;
import com.bytter.biz.finacial.report.vo.AccountChangeVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import com.bytter.poi.excel.export.ExcelExportServer;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/finacial/report/accountChange")
public class AccountChangeController extends BaseController {
	@Autowired
	private AccountChangeService accountChangeService;
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_ACCOUNT_CHANGE_QUERY)
	@GetMapping("/queryList")
	public String queryList(Model model,AccountChangeVo vo){
		List<AccountChangeVo> list = null;
		if(StringUtils.isNoneBlank(vo.getNetId()) || StringUtils.isNoneBlank(vo.getCorpId())
				|| StringUtils.isNoneBlank(vo.getLe_account())
				|| StringUtils.isNoneBlank(vo.getSubjectId())
				|| vo.getLe_balanceDate() != null
				|| vo.getGe_balanceDate() != null){
			list = accountChangeService.listAll(vo,FinacialResourceConstant.FINACIAL_ACCOUNT_CHANGE_QUERY,getUserId());
		}else{
			list = new ArrayList<AccountChangeVo>();
		}
		model.addAttribute("list", list);
		model.addAttribute("search", vo);
		String gBalanceDate = null;
		if(vo.getGe_balanceDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			gBalanceDate = sdf.format(vo.getGe_balanceDate());
		}
		String lBalanceDate = null;
		if(vo.getLe_balanceDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			lBalanceDate = sdf.format(vo.getLe_balanceDate());
		}
		model.addAttribute("gBalanceDate", gBalanceDate);
		model.addAttribute("lBalanceDate", lBalanceDate);
		return "finacial/report/accountChange";
	}
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_ACCOUNT_CHANGE_QUERY)
	@PostMapping("/queryListAccount")
	@ResponseBody
	public  List<FcAccDTO> queryListAccount(String netId, String corpId){
		if("undefined".equals(corpId)){
			corpId = null;
		}
		return accountChangeService.queryListAccount(netId, corpId);
	}
	/**导出excel**/
	@PostMapping("/excel")
	public void excel(ModelMap map,AccountChangeVo vo,HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<AccountChangeVo> list = null;
		if(StringUtils.isNoneBlank(vo.getNetId()) || StringUtils.isNoneBlank(vo.getCorpId())
				|| StringUtils.isNoneBlank(vo.getLe_account())
				|| StringUtils.isNoneBlank(vo.getSubjectId())
				|| vo.getLe_balanceDate() != null
				|| vo.getGe_balanceDate() != null)
		{
			list = accountChangeService.listAll(vo,FinacialResourceConstant.FINACIAL_ACCOUNT_CHANGE_QUERY,getUserId());
		}else{
			list = new ArrayList<AccountChangeVo>();
		}
		String curAndDate = "";
		Iterator<AccountChangeVo> iterator = list.iterator();
		while (iterator.hasNext()){
			AccountChangeVo next = iterator.next();
			if(org.apache.commons.lang.StringUtils.isBlank(next.getAccName())){
				String[] s = next.getAccount().split("_");
				for (String s1 : s) {
					curAndDate += s1 + "  ";
				}
				iterator.remove();
				break;
			}
		}
//		List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
//
//		entity.add(new ExcelExportEntity("账号", "account"));
//		entity.add(new ExcelExportEntity("户名", "accName"));
//		entity.add(new ExcelExportEntity("方向", "balanceName"));
//		ExcelExportEntity yesterBalanceEntity = new ExcelExportEntity("期初余额", "yesterBalance");
//		yesterBalanceEntity.setNumFormat(",##0.00");
//		entity.add(yesterBalanceEntity);
//		ExcelExportEntity debitMoneyEntity = new ExcelExportEntity("借方发生额", "debitMoney");
//		debitMoneyEntity.setNumFormat(",##0.00");
//		entity.add(debitMoneyEntity);
//		ExcelExportEntity creditMoneyEntity = new ExcelExportEntity("贷方发生额", "creditMoney");
//		creditMoneyEntity.setNumFormat(",##0.00");
//		entity.add(creditMoneyEntity);
//		ExcelExportEntity balanceEntity = new ExcelExportEntity("期末余额", "balance");
//		balanceEntity.setNumFormat(",##0.00");
//		entity.add(balanceEntity);

		map.put(NormalExcelConstants.FILE_NAME, "账户变化情况表");
        map.put(NormalExcelConstants.CLASS,AccountChangeVo.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("账户变化情况表", curAndDate + "导出人:"+getCurrentUser().getUsername(), "账户变化情况表"));

		map.put(NormalExcelConstants.DATA_LIST,list);
//        map.put(NormalExcelConstants.MAP_LIST,list);
		renderMergedOutputModel(map,request,response);
//		map.put(NormalExcelConstants.DATA_LIST,list);
//		return  NormalExcelConstants.EXCEL_VIEW;
	}

	public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String codedFileName = "临时文件";
		Workbook workbook = null;
		if(model.containsKey("mapList")) {
			List out = (List)model.get("mapList");
			if(out.size() == 0) {
				throw new RuntimeException("MAP_LIST IS NULL");
			}
			workbook = ExcelExportUtil.exportExcel((ExportParams)((Map)out.get(0)).get("params"), (Class)((Map)out.get(0)).get("entity"), (Collection)((Map)out.get(0)).get("data"));
			for(int i = 1; i < out.size(); ++i) {
				(new ExcelExportServer()).createSheet(workbook, (ExportParams)((Map)out.get(i)).get("params"), (Class)((Map)out.get(i)).get("entity"), (Collection)((Map)out.get(i)).get("data"));
			}
		} else {
			workbook = ExcelExportUtil.exportExcel((ExportParams)model.get("params"), (Class)model.get("entity"), (Collection)model.get("data"));
		}
		if(model.containsKey("fileName")) {
			codedFileName = (String)model.get("fileName");
		}
		if(workbook instanceof HSSFWorkbook) {
			codedFileName = codedFileName + ".xls";
		} else {
			codedFileName = codedFileName + ".xlsx";
		}
		if(this.isIE(request)) {
			codedFileName = URLEncoder.encode(codedFileName, "UTF8");
		} else {
			codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}

	protected boolean isIE(HttpServletRequest request) {
		return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
	}
}
