package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.ConWarnRulesService;
import com.bytter.guarantee.domain.vo.ConWarnRulesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author wangzy
 * @Title: ConWarnRulesController
 * @Description: 合同预警规则Controller
 * @date 2019/11/4 15:20
 */
@Controller
@RequestMapping("/contract/conWarnRules")
public class ConWarnRulesController extends BaseController {
    @Autowired
    private ConWarnRulesService conWarnRulesService;
    /**
     * 合同预警规则设置页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listConWarnRules(Model model) {
        model.addAttribute("listConWarnRulesVO", conWarnRulesService.listConWarnRulesVO());
        return "contract/listConWarnRules";
    }
    /**
     * @param
     * @return 结果
     * @Title: 合同预警规则保存
     */
    @LogMessage(content = "合同预警规则保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson saveConWarnRules(ConWarnRulesVO info) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conWarnRulesService.saveConWarnRules(info, this.getCurrentUser()));
        return ajaxJson;
    }
}
