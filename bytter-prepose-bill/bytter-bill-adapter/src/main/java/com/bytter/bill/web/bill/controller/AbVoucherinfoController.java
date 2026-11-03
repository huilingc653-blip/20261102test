package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbVoucherinfoService;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**   
 * @Title: Controller  
 * @Description: 票据主信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abvoucherinfo")
public class AbVoucherinfoController extends BaseController {
	
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	
	/**
	 * 票据详细信息查看
	 * @return
	 */
	@GetMapping("/billEcdsInfoView")
	public String billEcdsInfoView(AbVoucherinfoVO voucherinfoVO, Model model) {
		abVoucherinfoService.initBillEcdsInfo(voucherinfoVO, model);
		return "bill/accebill/viewAbVoucherEcdsInfo";
	}
	
	/**
	* 票据详细信息查看
	* @return
	*/
	@GetMapping("/batchBillEcdsInfoView")
	public String batchBillEcdsInfoView(AbVoucherinfoVO voucherinfoVO, Model model) {
		if("post".equals(voucherinfoVO.getAccIds())){
			model.addAttribute("isPost","1");
			model.addAttribute("voucherinfoVO",voucherinfoVO);
			return "bill/accebill/viewAbVoucherEcdsInfoBatch";
		}
		abVoucherinfoService.initBatchBillEcdsInfo(voucherinfoVO, model);
		model.addAttribute("billShowType", voucherinfoVO.getBillShowType());
		return "bill/accebill/viewAbVoucherEcdsInfoBatch";
	}

	/**
	 * 票据详细信息查看
	 * @return
	 */
	@PostMapping("/batchBillEcdsInfoPostView")
	public String batchBillEcdsInfoPostView(AbVoucherinfoVO voucherinfoVO, Model model) {
		abVoucherinfoService.initBatchBillEcdsInfo(voucherinfoVO, model);
		return "bill/accebill/viewAbVoucherEcdsInfoBatch";
	}
}
