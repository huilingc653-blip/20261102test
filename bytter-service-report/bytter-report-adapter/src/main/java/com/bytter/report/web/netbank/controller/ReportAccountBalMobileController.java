package com.bytter.report.web.netbank.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.report.domain.netbank.service.ReportAccountBalMobileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * APP首页账户余额报表Controller
 *
 * @author zuoy 2020-06-02
 */
@Controller
@RequestMapping("/bi/reportAccountBalMobile")
public class ReportAccountBalMobileController extends BaseController {

    @Autowired
    private ReportAccountBalMobileServer reportAccountBalMobileServer;

    /**
     * app报表首页 - 账户余额
     *
     * @param type type
     * @return 账户余额
     */
    @PostMapping("/getAccountBal")
    @ResponseBody
    public Object getAccountBal(String type) {
        ICurrentUser currentUser = getCurrentUser();
        return this.reportAccountBalMobileServer.getAccountBal(currentUser, type);
    }

    /**
     * app报表首页 - 资金概况
     *
     * @param corpId 单位id
     * @return 资金概况
     */
    @PostMapping("/getDayBankAccountCapital")
    @ResponseBody
    public Object getDayBankAccountCapital(String corpId) {
        return this.reportAccountBalMobileServer.getBankAccountCapital(getCurrentUser(), corpId);
    }
}
