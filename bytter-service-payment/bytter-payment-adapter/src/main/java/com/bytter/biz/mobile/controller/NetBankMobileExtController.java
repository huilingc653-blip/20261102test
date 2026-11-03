package com.bytter.biz.mobile.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.report.service.NisAllocationReportService;
import com.bytter.biz.report.vo.NisAllocationReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 付款指令表
 * @date 2017-06-28 16:31:56
 */
@Controller
@RequestMapping("/mobile/report/netbank")
public class NetBankMobileExtController extends BaseController {
	@Autowired
	private NisAllocationReportService nisAllocationReportService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IDictService dictService;

	@Value("${bytter.app.url}")
	private String appUrl;

	/**
	 * 资金调拨明细表报表-币别
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
		return "mobile/report/netbank/nisAllocationMoblie";

//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
////		String url = request.getParameter("forwardURL");
//		String url = "/mobile/report/netbank/nisAllocationMoblie";
//		Map<String, String[]> queryParams = request.getParameterMap();
//		String newUriPath = url + "?" + queryParams.entrySet().stream()
//				.filter(entry -> ObjectUtil.notEqual(entry.getKey(), "forwardURL"))
//				.map(entry -> entry.getKey() + "=" + entry.getValue()[0])
//				.collect(Collectors.joining("&"));
//		return "redirect:" + appUrl + newUriPath + "&customMethod=GET";

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
		return "mobile/report/netbank/viewNisAllocationMoblie";
	}

	/**
	 * 资金调拨明细数据列表
	 * 【资金调拨明细表】交易成功的才会展示
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
			info.setCurSign(Base64Util.decodeToString(info.getCurSign()));
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
		return "mobile/report/netbank/nisAllocationMoblie";
	}
}
