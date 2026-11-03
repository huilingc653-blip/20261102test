package com.bytter.credit.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.credit.domain.service.RatCprationLogService;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationMsgController
 * @Description:授信日志控制层
 * @date 2018年11月24日 上午9:13:55
 */
@Controller
@RequestMapping("/cms/rat/ratcprationlog")
public class RatCprationMsgController extends BaseController {
    @Autowired
    RatCprationLogService ratLogService;
    /**
     * 跳转到日志页面
     * 
     * @param model
     * @param id
     * @return
     */
    @LogMessage(content = "查询RAT_RATION_LOG列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("listlog")
    public String listLog(Model model, String rationId) {
        model.addAttribute("rationId", rationId);
        return "cms/rat/cpration/listRatCpRationLogMsg";
    }
    /**
     * 通过授信ID查询相应的日志信息
     * 
     * @param input
     * @param rationId
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("listlogdata")
    @ResponseBody
    public DataTablesOutput listLogDate(@RequestBody DataTablesInput input, String rationId) {
        return ratLogService.listRationMsg(rationId, input);
    }
}
