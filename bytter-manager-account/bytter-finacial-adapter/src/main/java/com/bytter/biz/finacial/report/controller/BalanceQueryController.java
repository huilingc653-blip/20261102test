package com.bytter.biz.finacial.report.controller;

import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.report.service.BalanceQueryService;
import com.bytter.biz.finacial.report.vo.BalanceQueryVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.MapExcelConstants;
import com.bytter.system.api.dto.CorpDTO;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * @Title:账户实时余额查询
 * @Description: FC_ACCU_BALANCE
 * @author zhengxn
 * @date 2017-7-8 14:27:04
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/report/BalanceQuery")
public class BalanceQueryController extends BaseController{
	@Autowired
	private BalanceQueryService balanceQueryService;
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_BALANCEQUERY_QUERY)
	@GetMapping("/getList")
	public String list(Model model,BalanceQueryVo search) {
		List<BalanceQueryVo> map = balanceQueryService.queryOnlineLedgerResult(search,FinacialResourceConstant.FINACIAL_BALANCEQUERY_QUERY,getUserId());
		CorpDTO net = null;
		if(!EmptyUtils.isEmpty(StringUtils.trimToEmpty(search.getNetId()))){
			net = balanceQueryService.getNetId(search.getNetId());
			model.addAttribute("tableHead", net.getName());
		}
		model.addAttribute("search", search);
		model.addAttribute("balBeans", map);
		model.addAttribute("sumType", search.getSumType());
		return "finacial/report/balanceQuery";
	}
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_BALANCEQUERY_QUERY)
	@PostMapping("/queryListAccount")
	@ResponseBody
	public  List<FcAccDTO> queryListAccount(String netId, String corpId){
		if("undefined".equals(corpId)){
			corpId = null;
		}
		return balanceQueryService.queryListAccount(netId, corpId);
	}
	/**
	 * 导出
	 * **/
	@GetMapping("/excel")
	public void excel(ModelMap map,BalanceQueryVo vo,HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<Map<String,Object>> res = balanceQueryService.excel(vo,FinacialResourceConstant.FINACIAL_BALANCEQUERY_QUERY,getUserId());
		List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
		if("1".equals(vo.getSumType())){
			entity.add(new ExcelExportEntity("单位名称", "corpName"));
			entity.add(new ExcelExportEntity("科目", "subjectName"));
		}else if("2".equals(vo.getSumType())){
			entity.add(new ExcelExportEntity("科目", "corpName"));
			entity.add(new ExcelExportEntity("单位名称", "corpName"));
		}else if ("3".equals(vo.getSumType())){
			entity.add(new ExcelExportEntity("科目", "corpName"));
			entity.add(new ExcelExportEntity("银行类别", "subjectName"));
		} else if ("4".equals(vo.getSumType())) {
			entity.add(new ExcelExportEntity("科目", "subjectName"));
		}
		entity.add(new ExcelExportEntity("账号", "blnctrAcc"));
		entity.add(new ExcelExportEntity("账户名称", "accName"));
		entity.add(new ExcelExportEntity("借贷方向", "balanceName"));
		ExcelExportEntity freezeMoneyEntity = new ExcelExportEntity("冻结金额", "freezeMoney");
		freezeMoneyEntity.setNumFormat(",##0.00");
		entity.add(freezeMoneyEntity);
		ExcelExportEntity onlineBalanceEntity = new ExcelExportEntity("记账余额", "onlineBalance");
		onlineBalanceEntity.setNumFormat(",##0.00");
		entity.add(onlineBalanceEntity);
		ExcelExportEntity checkBalanceEntity = new ExcelExportEntity("复核余额", "checkBalance");
		checkBalanceEntity.setNumFormat(",##0.00");
		entity.add(checkBalanceEntity);
		ExcelExportEntity noCheckBalanceEntity = new ExcelExportEntity("未复核金额", "noCheckBalance");
		noCheckBalanceEntity.setNumFormat(",##0.00");
		entity.add(noCheckBalanceEntity);
		entity.add(new ExcelExportEntity("记账笔数", "tallyCnt"));
		entity.add(new ExcelExportEntity("复核笔数", "checkCnt"));
		map.put(MapExcelConstants.FILE_NAME,"账户实时余额查询表");
		map.put(MapExcelConstants.CLASS,BalanceQueryVo.class);
        map.put(MapExcelConstants.PARAMS,new ExportParams("账户实时余额查询表","导出人:"+getCurrentUser().getUsername(), "账户实时余额查询表"));
        map.put(MapExcelConstants.ENTITY_LIST,entity);
        if(!EmptyUtils.isEmptyList(res)){
        	map.put(MapExcelConstants.MAP_LIST,res);
        }else{
        	map.put(MapExcelConstants.MAP_LIST,new ArrayList<Map<String,Object>>());
        }
		renderMergedOutputModel(map,request,response);
	}

	public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String codedFileName = "临时文件";
		Workbook workbook = ExcelExportUtil.exportExcel((ExportParams) model.get("params"), (List) model.get("data"), (Collection) model.get("mapList"));
		if (model.containsKey("fileName")) {
			codedFileName = (String) model.get("fileName");
		}
		if (workbook instanceof HSSFWorkbook) {
			codedFileName = codedFileName + ".xls";
		} else {
			codedFileName = codedFileName + ".xlsx";
		}
		if (this.isIE(request)) {
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
