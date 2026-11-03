package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.NisAllocationReportService;
import com.bytter.biz.report.vo.NisAllocationReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
////////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 付款指令表
 * @date 2017-06-28 16:31:56
 */
@Controller
@RequestMapping("/report/allocation")
public class NisAllocationReportController extends BaseController {
	@Autowired
	private NisAllocationReportService nisAllocationReportService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IDictService dictService;
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATION_QUERY)
	@GetMapping("/list")
	public String list(Model model) {
		// 判断当前用户是否为单位用户
		ICurrentUser user = this.getCurrentUser();
		CorpDTO corp = corpService.get(user.getCorpId());
		model.addAttribute("corpType", corp.getType());
		return "report/listNisAllocationReport";
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATIONSUM_QUERY)
	@GetMapping("/showReport")
	public String showReport(Model model) {
		// 判断当前用户是否为单位用户
		ICurrentUser user = this.getCurrentUser();
		CorpDTO corp = corpService.get(user.getCorpId());
		model.addAttribute("corpType", corp.getType());
		return "report/nisAllocationReportBar";
	}
	/**
	 * 资金调拨明细表
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATION_QUERY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return nisAllocationReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 资金调拨明细表
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATIONSUM_QUERY)
	@PostMapping("/dataTableSum")
	@ResponseBody
	public DataTablesOutput dataTableSum(@RequestBody DataTablesInput dataTablesInput, Model model) {
		return nisAllocationReportService.dataTableSum(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 创建资金调拨汇总表（柱状图）
	 *
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/createNisAllocationSumBar")
	@ResponseBody
	public Object createNisAllocationSumBar(NisAllocationReportVO nisAllocationReportVO) throws IOException {
		return this.nisAllocationReportService.createNisAllocationSumBar(nisAllocationReportVO, this.getCurrentUser());
	}
	/**
	 * 柱状图跳转明细页面
	 *
	 * @return
	 */
	@GetMapping("/open")
	public String open(Model model, String corpId, String voucherFrom, String curId, Date beginReqDate, Date endReqDate) {
		// 判断当前用户是否为单位用户
		ICurrentUser user = this.getCurrentUser();
		CorpDTO corp = corpService.get(user.getCorpId());
		model.addAttribute("corpType", corp.getType());
		model.addAttribute("corpId", corpId);
		model.addAttribute("voucherFrom", voucherFrom);
		model.addAttribute("curId", curId);
		model.addAttribute("beginReqDate", beginReqDate);
		model.addAttribute("endReqDate", endReqDate);
		model.addAttribute("viewType", "open");
		return "report/listNisAllocationReport";
	}
	/**
	 * 柱状图JS
	 *
	 * @param model
	 * @param response
	 * @return
	 */
	@GetMapping("/allocationBar.js")
	public String barJs(Model model, HttpServletResponse response, NisAllocationReportVO nisAllocationReportVO) {
		//缓存当前js
		response.setHeader("Cache-Control", "max-age=31536000");
		response.setContentType("text/javascript;charset=utf-8");
		model.addAttribute("report", this.nisAllocationReportService.createNisAllocationSumBar(nisAllocationReportVO, this.getCurrentUser()));
		return "report/nisAllocationBar";
	}
	/**
	 * 资金调拨明细表报表
	 * @param model
	 * @param vo
	 * @return
	 * @throws
	 */
	@RequestMapping("/mobileAllocation")
	public String mobileAllocation(Model model,NisAllocationReportVO vo) {
		// 判断当前用户是否为单位用户
		int type = 1;
		if(!EmptyUtils.isEmpty(vo.getCorpId())){
			CorpDTO corpDTO = corpService.get(vo.getCorpId());
			type = corpDTO.getType();
		}
		model.addAttribute("corpType", String.valueOf(type));
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("curList", curList);
		return "report/nisAllocationMoblie";
	}
	/**
	 * 资金调拨明细表报表详情信息
	 * @param model
	 * @return
	 */
	@RequestMapping("/mobileViewAllocation")
	public String mobileViewAllocation(Model model,NisAllocationReportVO info) {
		NisAllocationReportVO vo = new NisAllocationReportVO();
		List<NisAllocationReportVO> voList = nisAllocationReportService.getReportMoblieList(info,this.getCurrentUser());
		if(!EmptyUtils.isEmptyList(voList)){
			vo = voList.get(0);
			vo.setPayMoneyStr(NumberUtil.formatNumeric(vo.getPayMoney()));
			vo.setVoucherFromName(dictService.get("nisAllocationReport.voucherFrom", vo.getVoucherFrom()));
			if(!EmptyUtils.isEmpty(voList.get(0).getReqDate())){
				vo.setReqDateStr(DateUtils.formatDate((vo.getReqDate())));
			}
		}
		model.addAttribute("info",vo);
		return "report/viewNisAllocationMoblie";
	}
	/**
	 * 资金调拨明细数据列表
	 */
	@PostMapping("/getAllocationMoblieList")
	@ResponseBody
	public Object getAllocationMoblieList(Model model,NisAllocationReportVO vo){
		String startDate = "";
		String endDate = "";
		if(StringUtils.isNotBlank(vo.getStartDateStr())){
			startDate = vo.getStartDateStr();
			endDate = vo.getEndDateStr();
		}else{
			endDate = DateUtils.formatDate(DateUtils.getDate());
			//sign:1:7天内,2:15天内,3：30天内
			if(StringUtils.isNotBlank(vo.getSign())){
				if("1".equals(vo.getSign())){
					startDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(),-7));
				}else if("2".equals(vo.getSign())){
					startDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(),-15));
				}else if("3".equals(vo.getSign())){
					startDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(),-30));
				}
			}
		}
		vo.setStartDateStr(startDate);
		vo.setEndDateStr(endDate);
		List<NisAllocationReportVO> voList = nisAllocationReportService.getReportMoblieList(vo,this.getCurrentUser());
		for(NisAllocationReportVO info : voList){
//			info.setCurSign(Base64Util.decodeToString(info.getCurSign()));
			info.setPayMoneyStr(NumberUtil.formatNumeric(info.getPayMoney()));
			info.setVoucherFromName(dictService.get("nisAllocationReport.voucherFrom", info.getVoucherFrom()));
			if(!EmptyUtils.isEmpty(info.getReqDate())){
				info.setReqDateStr(DateUtils.formatDate((info.getReqDate())));
			}
		}
		return voList;
	}
	@GetMapping("/mobileAllocation1")
	public String mobileAllocation1(Model model,NisAllocationReportVO vo) {
		// 判断当前用户是否为单位用户
		int type = 1;
		if(!EmptyUtils.isEmpty(vo.getCorpId())){
			CorpDTO corpDTO = corpService.get(vo.getCorpId());
			type = corpDTO.getType();
		}
		model.addAttribute("corpType", String.valueOf(type));
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("curList", curList);
		return "report/nisAllocationMoblie";
	}
}
