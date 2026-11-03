package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.ConWarnRulesService;
import com.bytter.guarantee.domain.vo.ConWarnRulesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangzy
 * @Title: ConWarnRulesController
 * @Description: 合同预警规则Controller
 * @date 2019/11/4 15:20
 */
@Api(value = "/v1-guarantee/contract/conWarnRules", tags = "合同预警规则")
@RestController("v1ConWarnRulesController")
@RequestMapping("/v1-guarantee/contract/conWarnRules")
public class ConWarnRulesController extends BaseController {
    @Autowired
    private ConWarnRulesService conWarnRulesService;

    /**
     * 合同预警规则保存
     */
    @ApiOperation("合同预警规则保存")
    @LogMessage(content = "合同预警规则保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/save")
    public AjaxJson saveConWarnRules(@RequestBody ConWarnRulesVO info) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conWarnRulesService.saveConWarnRules(info, this.getCurrentUser()));
        return ajaxJson;
    }
}