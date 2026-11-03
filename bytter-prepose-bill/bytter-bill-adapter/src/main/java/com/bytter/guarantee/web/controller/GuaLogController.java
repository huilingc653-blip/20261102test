package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.GuaLogService;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/cms/guarantee/gualog")
public class GuaLogController extends BaseController {
    @Autowired
    private GuaLogService guaLogService;
    @DubboReference()
    private IUserService iUserService;
    /**
     * 跳转到日志页面
     * 
     * @param model
     * @param id
     * @return
     */
    @LogMessage(content = "跳转到日志页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/listlog")
    public String listLog(Model model, String guaId) {
        //logger.info(iUserService.getByLoginName("admin").getLoginName());
        model.addAttribute("guaId", guaId);
        return "cms/guarantee/listGuaLog";
    }
    /**
     * 通过担保ID查询相应的日志信息
     * 
     * @param input
     * @param bondId
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/listlogdata")
    @ResponseBody
    public DataTablesOutput listGuaLogInfo(@RequestBody DataTablesInput input, String guaId) {
        return guaLogService.listGuaLog(guaId, input);
    }
}
