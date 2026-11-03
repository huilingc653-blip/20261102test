package com.bytter.guarantee.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.GuaContractOccupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author luxy
 * @Title: GuaContractOccupController
 * @ProjectName T2_IDEA
 * @Description: 担保合同占用控制层
 * @date 2019/4/30 19:47
 */
@Controller
@RequestMapping("/cms/guarantee/guaContractOccup")
public class GuaContractOccupController extends BaseController {
	
	@Autowired
    private GuaContractOccupService guaContractOccupService;
	
    @GetMapping("/occupList")
    @LogMessage(content = "担保合同占用列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String occupList(String id, Model model) {
        model.addAttribute("contractId", id);
        return "cms/guarantee/guaContractOccupyIndexTab";
    }
    @GetMapping("/listGuaContractRatOccup")
    @LogMessage(content = "授信使用担保合同列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listGuaContractRatOccup(String contractId, String startDate, String endDate, String status,
            @RequestParam(name = "page", defaultValue = "listGuaContractRatOccup") String page, Model model) {
        model.addAttribute("id", contractId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("status", status);
        return "cms/guarantee/" + page;
    }
    @RequestMapping("/mobileContractUseView")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String mobileContractUseView(Model model, String contractId) {
        model.addAttribute("contractId", contractId);
        return "cms/guarantee/mobileGuaContractUseTab";
    }
    
    @PostMapping("/getUseContractList")
    @ResponseBody
    public DataTablesOutput getUseContractList(@RequestBody DataTablesInput dataTablesInput){
        return guaContractOccupService.getUseContractList(dataTablesInput, getCurrentUser());
    }
}
