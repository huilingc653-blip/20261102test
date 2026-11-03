package com.bytter.bill.web.saas.controller.bill;
import com.bytter.bill.domain.bill.service.AbVoucherinfoService;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.bill.vo.v1.AbVoucherinfoPageVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: Controller  
 * @Description: 票据主信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abvoucherinfo",tags = "票据主信息")
@RestController("v1AbVoucherinfoController")
@RequestMapping("/v1-preposebill/bill/accebill/abvoucherinfo")
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
	@ApiOperation("票据详细信息查看")
	@GetMapping("/batchBillEcdsInfoView")
	public AjaxJson<AbVoucherinfoPageVO> batchBillEcdsInfoView(AbVoucherinfoVO voucherinfoVO, Model model) {
		AbVoucherinfoPageVO pageVO = new AbVoucherinfoPageVO();
		if("post".equals(voucherinfoVO.getAccIds())){
			pageVO.setIsPost("1");
			pageVO.setVoucherinfoVO(voucherinfoVO);
			return success(pageVO);
		}
		abVoucherinfoService.initBatchBillEcdsInfo(voucherinfoVO, pageVO);
		pageVO.setBillShowType(voucherinfoVO.getBillShowType());
		return success(pageVO);
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
