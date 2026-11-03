package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrdScoringIndicatorValueService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/credit/crdScoringIndicatorValue")
public class CrdScoringIndicatorValueController extends BaseController {

    @Autowired
    private CrdScoringIndicatorValueService scoringIndicatorValueService;

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/save")
    @ResponseBody
    public AjaxJson save(List<String> scoringIds, List<String> indicatorIds, List<String> rawDataIds ){
        scoringIndicatorValueService.save(scoringIds, indicatorIds, rawDataIds, getCurrentUser());
        return success();
    }

}
