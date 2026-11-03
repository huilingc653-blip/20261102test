package com.bytter.bill.web.saas.controller.bill;
import com.bytter.bill.domain.bill.service.AbBillDiscountAccountService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.bill.domain.bill.vo.v1.AbBillDiscountAccountListPageVO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description:贴现到账Controller
 * @author onlineGenerator
 * @date 2017-7-21 13:49:49
 * @version 1.0
 *
 */
@Api(value = "贴现到账",tags = "贴现到账")
@RestController("v1AbBillDiscountAccountController")
@RequestMapping("/v1-preposebill/bill/accebill/abBillDiscountAccount")
public class AbBillDiscountAccountController extends BaseController {
	@Autowired
	private AbBillDiscountAccountService abBillDiscountAccountService;
	@Autowired
	private AbCorpService abCorpService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	/**
	 * 贴现到账页面跳转
	 * 用于收款管理
	 *
	 * @return
	 */
	@ApiOperation(value = "贴现到账页面跳转")
	@LogMessage(content = "贴现到账页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listDiscountAccount")
	public AjaxJson<AbBillDiscountAccountListPageVO> abBillDiscountAccountPage(AbVoucherinfoViceVO reVo) {
		if(StringUtils.isNotEmpty(reVo.getCorpId())){
			reVo.setCorpName(abCorpService.get(reVo.getCorpId()).getName());
		}
		AbBillDiscountAccountListPageVO pageVO = new AbBillDiscountAccountListPageVO();
		pageVO.setBillInfo(reVo);
		BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(reVo.getId());
		if(StringUtils.isNotEmpty(bisAccDtlDTO)){
			// 获取交易日期
			pageVO.setBusinessDate(DateUtils.date2Str(bisAccDtlDTO.getTransTime() ,DateUtils.date_sdf()));
			// 获取交易金额
			pageVO.setBillMoney(bisAccDtlDTO.getAmt());
		}
		return success(pageVO);
	}
	/**
	 * 贴现到账列表
	 * 
	 * @param dataTablesInput
	 * @param isApply 是否含有审批中数据 0表示不含
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "贴现到账列表")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String isApply) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(abBillDiscountAccountService.listAbBillDiscountAccount(input, isApply));
	}
}
