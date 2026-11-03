package com.bytter.bill.web.bill.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 手机端统一入口
 * @author caojy
 *
 */
@Controller
@RequestMapping("/bill/mobile/port")
public class AbBillMobilePortController extends BaseController{
	
    @DubboReference(check = false)
	private IUserService userService;
	 /**
     * APP报表-承兑汇票
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appAccebillReportIndex")
    public String appAccebillReportIndex(Model model) {
        String userId = getCurrentUser().getId();
        UserDTO userDTO = userService.getByUserId(userId);
        CorpDTO corp = userDTO.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        return "bill/accebill/mobile/appAccebillReportIndex";
    }
}
