package com.bytter.credit.web.controller.mobile;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.RationConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.domain.service.RatBkStatisticsService;
import com.bytter.credit.domain.service.RatUrationService;
import com.bytter.credit.domain.vo.RatBkStatisticSearchVO;
import com.bytter.credit.domain.vo.RatBkStatisticVO;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/mobile/report/cms/ratbkstatistics")
public class RatBkStatisticsMobileExtController extends BaseController{
	
	@Autowired
	private RatBkStatisticsService ratBkStatisticsService;

	@Autowired
	private RatUrationService ratUrationService;

	@DubboReference(check = false)
	private IDictService dictService;

	@DubboReference(check = false)
	private IBtCurrencyService currencyService;

	@DubboReference(check = false)
	private IBankTypeService bankTypeService;

	@RequestMapping("/mobileBkAmountRation")
	public String mobileBkAmountRation(RatBkStatisticSearchVO searchVO, Model model){
		searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
		searchVO.setCorpId(null);
		if (null == searchVO.getRatDate()) {
			searchVO.setRatDate(new Date());
		}
		if (null == searchVO.getMoneyType()) {
			searchVO.setMoneyType("10000");
		}
		List<RatBkStatisticVO> list = ratBkStatisticsService.getMobileData(searchVO, getCurrentUser());
		model.addAttribute("curList", currencyService.getBtCurrency());
		model.addAttribute("list", list);
		model.addAttribute("bankList", bankTypeService.listByValidSign(null));
		model.addAttribute("ratModes", dictService.list("RatBkration.RatMode"));
//		model.addAttribute("now", DateUtils.format(new Date(), "yyyy-MM-dd"));
		return "mobile/report/ratBankRation/mobileAmountStatics";
	}

	/**
	*app报表后台数据交互
	 * 【授信额度综合统计-条件查询】
	*@Param searchVO
	*@Param model
	*@return
	*/
	@RequestMapping("/getMobileData")
	@ResponseBody
	public AjaxJson getmobileData(RatBkStatisticSearchVO searchVO, Model model){
		searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
		if (null == searchVO.getMoneyType()) {
			searchVO.setMoneyType("10000");
		}
		List<RatBkStatisticVO> list = ratBkStatisticsService.getMobileData(searchVO, getCurrentUser());
		//设置授信模式
		for (RatBkStatisticVO item : list) {
			item.setRatModeName(dictService.getByName("RatBkration.RatMode",item.getRatMode()));
		}
		AjaxJson json = new AjaxJson();
		json.setObj(list);
		json.setSuccess(true);
		return json;
	}


	@RequestMapping("/viewMobileItemRatio")
	public String viewMobileItemRatio(UrationParamDTO paramDTO, Model model){
		paramDTO.setMoneyType(1);
		List<RatUrationUseVO> rations = null;
		if (EmptyUtils.isEmpty(paramDTO.getBankCode())) {
			rations = new ArrayList<RatUrationUseVO>();
		} else {
			paramDTO.setStatus(RationConstant.STATUS_CHECK.toString());
			rations = ratUrationService.queryRationUseDtl(paramDTO);
		}
		if (!EmptyUtils.isEmpty(rations)){
			RatUrationUseDtlVO totalVo = new RatUrationUseDtlVO();
			totalVo.setMoney(new BigDecimal(0));
			totalVo.setUseMoney(new BigDecimal(0));
			totalVo.setApproveMoney(new BigDecimal(0));
			totalVo.setOverPlusMoney(new BigDecimal(0));
			totalVo.setLeftMoney(new BigDecimal(0));
			for (RatUrationUseDtlVO rationDltItem : rations.get(0).getRationDlts()){
				totalVo.setMoney(totalVo.getMoney().add(rationDltItem.getMoney()));
				totalVo.setUseMoney(totalVo.getUseMoney().add(rationDltItem.getUseMoney()));
				totalVo.setApproveMoney(totalVo.getApproveMoney().add(rationDltItem.getApproveMoney()));
				totalVo.setOverPlusMoney(totalVo.getOverPlusMoney().add(rationDltItem.getOverPlusMoney()));
				totalVo.setLeftMoney(totalVo.getLeftMoney().add(rationDltItem.getLeftMoney()));
			}
			rations.get(0).getRationDlts().add(totalVo);
		}
		model.addAttribute("rations", rations);
		return "mobile/report/ratBankRation/mobileRationItemDtl";
	}

}
