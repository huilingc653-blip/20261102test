package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CtrScoringCorpContrastService;
import com.bytter.biz.creditRating.vo.CtrScoringCorpContrastVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 单位评分指标对照表controller
 *
 * @author huangyl
 */
@Controller
@RequestMapping("/credit/ctrScoringCorpContrast")
public class CrScoringCorpContrastController extends BaseController {

    @Autowired
    private CtrScoringCorpContrastService corpContrastService;

    /**
     * 功能介绍 :查询固化指标对象<br>
     */
    @GetMapping("/getModelScoringJson")
    @ResponseBody
    public List<CtrScoringCorpContrastVo> getModelScoringJson(String taskNo, String corpId) {
        return corpContrastService.getTaskScoringJson(taskNo, corpId);
    }
}
