package com.bytter.report.web.netbank.controller;
import com.bytter.core.base.BaseController;
import com.bytter.report.domain.netbank.service.BankAccBalTrendService;
import com.bytter.report.domain.netbank.vo.BankAccBalTrendVO;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * 银行存款余额趋势Controller
 *
 * @author zuoy 2018-12-05
 */
@Controller
@RequestMapping("/bi/bankAccBalTrend")
public class BankAccBalTrendController extends BaseController {
	@Autowired
	private BankAccBalTrendService bankAccBalTrendService;
	@DubboReference(check = false)
	private IUserService userService;
		@GetMapping("/list")
	public String list(Model model) {
		String corpId = userService.getByLoginName(this.getCurrentUser().getLoginName()).getCorpId();
		model.addAttribute("corpId", corpId);
		return "netbank/listBankAccBalTrend";
	}
	/**
	 * NIS_ALLOCATION_REQ table列表
	 *
	 * @return DataTablesOutput
	 */
		@PostMapping("/listDataTables")
	@ResponseBody
	public List<BankAccBalTrendVO> listDataTables(String curId, String corpId, String seachBeginDate , String seachEndDate) {
		return this.bankAccBalTrendService.listDataTables(curId, corpId, seachBeginDate, seachEndDate);
	}
	/**
	 * 银行存款余额趋势图表
	 * @param curId
	 * @param corpId
	 * @param seachBeginDate
	 * @param seachEndDate
	 * @return
	 */
	@PostMapping("/getBankAccBal")
	@ResponseBody
	public Object getBankAccBal(String curId, String corpId, String seachBeginDate , String seachEndDate){
		List<BankAccBalTrendVO> list = this.listDataTables(curId, corpId, seachBeginDate, seachEndDate);
		return bankAccBalTrendService.getBankAccBal(list);
	}
}
