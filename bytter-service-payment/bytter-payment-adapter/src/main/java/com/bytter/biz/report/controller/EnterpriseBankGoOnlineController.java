package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.EnterpriseBankGoOnlineService;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * @author zhoup
 * @version 1.0
 * @Title: Controller
 * @Description: 银企上线表
 * @date 2024-05-09
 */
@Controller
@RequestMapping("/report/enterpriseBankGoOnline")
public class EnterpriseBankGoOnlineController extends BaseController {
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private EnterpriseBankGoOnlineService enterpriseBankGoOnlineService;

	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATION_QUERY)
	@GetMapping("/list")
	public String list(Model model) {
		// 判断当前用户是否为单位用户
		ICurrentUser user = this.getCurrentUser();
//		CorpDTO corp = corpService.get(user.getCorpId());
//		model.addAttribute("corpType", corp.getType());
		return "report/enterpriseBankGoOnline";
	}
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return enterpriseBankGoOnlineService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
}