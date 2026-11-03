package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**   
 * @Title: Controller  
 * @Description: 票据附件操作
 * @author onlineGenerator
 * @date 2017-10-24 14:33:51
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillattachment")
public class AbBillAttachmentController extends BaseController {
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	
	/**
	* 票据附件操作详情页
	*
	* @return
	*/
	@GetMapping("/view")
	public String attachmentInfo(AbAccrecordsVO acVoInfo, Model model) {
		AbBillAttachmentVO attachmentInfo = abBillAttachmentService.getAbBillAttachmentVO(acVoInfo);
		if(StringUtils.isNotEmpty(acVoInfo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(acVoInfo.getBriefId())){
			AbBillAttachmentVO attachmentInfoDiscount = abBillAttachmentService.getAbBillAttachmentVOByDiscount(acVoInfo);
			model.addAttribute("attachmentInfoDiscount", attachmentInfoDiscount);
			//不启用贴现申请功能直接贴现办理   不显示贴现申请附件
			model.addAttribute("isNotDiscountApply",abVoucherinfoViceService.isNotDiscountApply());
		}
		model.addAttribute("attachmentInfo", attachmentInfo);
		return "bill/accebill/viewAttachmentInfo";
	}
	/**
	 * 添加票据附件操作
	 * @param abBillAttachment
	 * @return
	 */
	@PostMapping("/opInfo")
	@ResponseBody
	public AjaxJson opInfo(AbAccrecordsVO acVoInfo){
		abBillAttachmentService.opAttachmentInfo(acVoInfo,this.getCurrentUser());
		return success();
	}
	
}
