package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.InnerAccChangingService;
import com.bytter.biz.report.vo.InnerAccChangingVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
/**
 * @author zhangxb
 * @Description 内部账户改户
 */
@Controller
@RequestMapping("/netbank/report/innerAccChanging")
public class InnerAccChangingController extends BaseController{
	@Autowired
	private InnerAccChangingService innerAccChangingService;
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_CHANGING_QUERY)
	@GetMapping("/list")
	public String list(Model model){
	return "report/listInnerAccChanging";
	}
	/**展示页面**/
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_CHANGING_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public List<InnerAccChangingVO> getList(@RequestParam("corpId") String corpId,@RequestParam("netId") String netId,
			@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime){
		if(EmptyUtils.isEmpty(startTime) && EmptyUtils.isEmpty(endTime)
				&&EmptyUtils.isEmpty(netId) && EmptyUtils.isEmpty(corpId)){
			return new ArrayList<InnerAccChangingVO>();
		}else{
			return innerAccChangingService.getList(startTime,endTime,corpId,netId,getCurrentUser());
		}
	}
	@GetMapping("/accList")
	public String accList(String corpId,String startTime,String endTime,String type, Model model){
		model.addAttribute("corpId", corpId);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("type", type);
		return "report/listInnerAccChangingDetail";
	}
	/**明细表**/
	@PostMapping("/detailDatatable")
	@ResponseBody
	public List<InnerAccChangingVO> getAccByCorpId(String corpId,String startTime,String endTime,String type){
		return innerAccChangingService.getAccByCorpId(corpId,startTime,endTime,type);
	}
	/**详情表**/
	@GetMapping("/showView")
	public String showView(String account,String startTime,String endTime,Model model){
		List<InnerAccChangingVO>  history = innerAccChangingService.getHistoryByAccount(account,startTime,endTime);
		List<InnerAccChangingVO>  freeze = innerAccChangingService.getFreezeByAccount(account,startTime,endTime,null);//冻结
		if(EmptyUtils.isEmptyList(history)){
			model.addAttribute("history", new ArrayList<InnerAccChangingVO>());
		}else{
			model.addAttribute("history", history);
		}
		if(EmptyUtils.isEmptyList(freeze)){
			model.addAttribute("freeze", new ArrayList<InnerAccChangingVO>());
		}else{
			model.addAttribute("freeze", freeze);
		}
		return "report/listInnerAccChangingShowView";
	}
	/**
	 * 导出
	 * **/
	@LogMessage(content = "内部账户增减变动情况表导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
	@PostMapping("/exportExcel")
	public void doExportExcel(HttpServletRequest request, HttpServletResponse response, InnerAccChangingVO vo) throws Exception{
		String startDate = DateUtils.formatDate(vo.getGed_accOpenTime(),"yyyy-MM-dd");
		String endDate = DateUtils.formatDate(vo.getLed_accOpenTime(),"yyyy-MM-dd");
		List<InnerAccChangingVO> res = innerAccChangingService.getList(startDate,endDate,vo.getCorpId(),vo.getNetId(),getCurrentUser());
		if(EmptyUtils.isEmptyList(res)){
			res = new ArrayList<>();
		}
		// 导出的文件名
		String fileName = "内部账户增减变动情况表.xls";
		if (innerAccChangingService.isIE(request)) {//判断浏览器类型
			fileName = URLEncoder.encode(fileName, "UTF8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		//导出数据
		HSSFWorkbook workbook = innerAccChangingService.createExcelContent(res);
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
