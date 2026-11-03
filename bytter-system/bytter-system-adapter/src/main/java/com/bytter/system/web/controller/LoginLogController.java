package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.service.LoginLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
@Controller
@RequestMapping("/sys/loginLog")
public class LoginLogController extends BaseController {
    @Resource
    private LoginLogService loginLogService;
    @GetMapping("/index")
    public void index() {
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput listLoginLog(@RequestBody DataTablesInput dataTablesInput) {
        return loginLogService.listLoginLog(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/userLoginInfo")
    public String userLoginInfo(Model model) {
        return "sys/loginLog/listCurrentUserLoginLog";
    }
    @PostMapping("/datatableByCurrentUser")
    @ResponseBody
    public DataTablesOutput listLoginLogByCurrentUser(@RequestBody DataTablesInput dataTablesInput, String selfFlag) {
        dataTablesInput.getFormdata().put("onlyUser", true);
        return loginLogService.listLoginLog(dataTablesInput, this.getCurrentUser());
    }
}
