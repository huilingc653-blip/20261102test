package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/cms/queryLease/financingInfo")
public class CmsQueryLeaseInfoController extends BaseController {
    /**
     * 融资租赁申请列表-页面跳转
     */
    @LogMessage(content = "查询融资租赁申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "cms/lease/listQueryLeaseInfo";
    }
}
