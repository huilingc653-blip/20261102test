package com.bytter.accountReceivable.query.controller;

import com.bytter.accountReceivable.docManage.service.DocManageService;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.writeOff.service.WriteOffService;
import com.bytter.accountReceivable.writeOff.vo.DocWriteOffRelationVo;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *	应收查询
 */
@Controller
@RequestMapping("/accountReceivable/query/docManage")
public class DocManageQueryController extends BaseController {

	@Autowired
	private DocManageService docManageService;
	@Autowired
	private WriteOffService writeOffService;

	@Autowired
	private IBtCurrencyService currencyServiceImpl;
	@Autowired(required = false)
	private IConfigService configService;

	/**
	 * 应收查询 列表页面跳转
	 * @return
	 */
//	@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_QUERYDOCMANAGE_QUERY)
	@LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		BtCurrencyDTO natualCurrency = currencyServiceImpl.getNatualCurrency();
		String curIdRMB = natualCurrency.getId();
		model.addAttribute("curIdRMB",curIdRMB);
		return "query/listQueryDocManage";
	}

	/**
	 * 查询列表
	 * @return
	 */
//	@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_QUERYDOCMANAGE_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput datatable(@RequestBody DataTablesInput input){
		return docManageService.listQueryDocManage(input, getCurrentUser());
	}

	/**
	 * 应收单管理查看页面跳转
	 * @returnwriteOffHisList = {ArrayList@30822}  size = 1
	 */
//	@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_QUERYDOCMANAGE_QUERY)
	@GetMapping("/view")
	public String view(Model model, String id) {
		DocManageVo docManage = docManageService.getDocManageDetailById(id);
		model.addAttribute("docManage", docManage);

		List<DocWriteOffRelationVo> writeOffHisList = writeOffService.getWriteOffHisDetail(id);
		if(EmptyUtils.isNotEmpty(writeOffHisList)){
			for (DocWriteOffRelationVo relationVo : writeOffHisList) {
				//NisReceivableDTO nisReceivableByBillId = writeOffService.getNisReceivableByBillId(relationVo.getNisReceivableBillId());
				NisReceivableDTO nisReceivableByBillId = writeOffService.getNisReceivableByBillIdAndBatchNo(relationVo.getNisReceivableBillId(), relationVo.getBatchNo());
				if (!EmptyUtils.isEmpty(nisReceivableByBillId))
					relationVo.setNisReceivableBillId(nisReceivableByBillId.getId());
			}
		}
		model.addAttribute("writeOffHisList", writeOffHisList);

		/**
		 * 资金计划相关参数
		 */
		Map<String,Object> config = configService.listByType(BudgetConstant.BUSINESS_NAME_BASIC);
		model.addAttribute("config", config);
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BUDGETSEL);
		model.addAttribute("processSysname", BudgetConstant.BUSINESS_NAME_BASIC);
		return "query/viewQueryDocManage";
	}

}
