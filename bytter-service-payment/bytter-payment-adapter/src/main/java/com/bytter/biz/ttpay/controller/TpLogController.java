package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpLogService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: TP_LOG
 * @date 2017-09-14 15:07:53
 */
@Controller
@RequestMapping("/ttpay/tpLog")
public class TpLogController extends BaseController {
    @Autowired
    private TpLogService tpLogService;
    /**
     * TP_LOG列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询TP_LOG列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listMsgData")
    public String list(String quoteId, Model model) {
        model.addAttribute("tpLogList", tpLogService.listTpLog(quoteId));
        return "ttpay/listTpLog";
    }
}
