package com.bytter.system.rate.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.service.RateMaintenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/rate/maintence")
public class RateMaintenceController extends BaseController {
    @Autowired
    private RateMaintenceService rateMaintenceService;
    /**
     * 获取数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        String loginName = UserContextHelper.helper().getCurrentUser().getLoginName();
        return rateMaintenceService.listLoanRateMaintence(dataTablesInput, loginName);
    }
}
