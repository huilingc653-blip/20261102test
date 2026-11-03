package com.bytter.finance.web.lease.controller;
import com.bytter.finance.domain.lease.service.LeaseInterestService;
import com.bytter.finance.domain.lease.vo.LeaseInterestVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁租金支付表
 * @date 2022-04-15 10:07:46
 */
@Controller
@RequestMapping("/cms/lease/leaseinterest")
public class LeaseInterestController extends BaseController {
    @Autowired
    private LeaseInterestService leaseInterestService;
    /**
     * 预计利息明细
     *
     * @param leaseId
     * @param showSign
     *            显示标识 0查看.1带选择
     * @return
     */
    @GetMapping("/list")
    public String list(Model model, String leaseId, String showSign) {
        model.addAttribute("showSign", showSign);
        List<LeaseInterestVo> list = leaseInterestService.queryInterestList(leaseId);
        model.addAttribute("list", list);
        return "cms/provide/repay/cmsInterestWay";
    }
}
