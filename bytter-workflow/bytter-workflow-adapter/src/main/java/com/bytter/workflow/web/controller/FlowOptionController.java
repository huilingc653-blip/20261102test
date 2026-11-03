package com.bytter.workflow.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.constant.FlowOptioneConst;
import com.bytter.workflow.domain.entity.FlowModel;
import com.bytter.workflow.domain.service.FlowModelService;
import com.bytter.workflow.domain.service.FlowOptionService;
import com.bytter.workflow.domain.vo.FlowOptionVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Created by zhoul on 2017/8/03.
 * 流程配置
 *
 * @history
 * 1. 2019-12-30  chenlw  移除单据配置功能后，修改配置的查询方法(list())。
 *
 */
@Controller
@RequestMapping("/bpm/option")
public class FlowOptionController extends BaseController {
    private final FlowOptionService optionService;
    private final FlowModelService flowModelService;
    public FlowOptionController(FlowOptionService optionService, FlowModelService flowModelService) {
        this.optionService = optionService;
        this.flowModelService = flowModelService;
    }
    @GetMapping("/index")
    public void index() {
        //do nothing
    }
    @GetMapping("/list/{categoryId}")
    @ResponseBody
    public Object list(@PathVariable("categoryId") String categoryId,Integer type) {
        if (FlowOptioneConst.OptionType._01.getCode().equals(type)) {
            return optionService.listConditionByCategoryId(categoryId);
        }
        return optionService.listByCategoryId(categoryId);
    }
    @GetMapping("/listByModelId")
    @ResponseBody
    public Object listByModelId(String modelId) {
        //todo
        FlowModel flowModel = flowModelService.getById(modelId);
        return optionService.listByCategoryId(flowModel.getCategoryId());
    }
    
    @PostMapping("/save")
    @ResponseBody
    public Object save(@RequestBody List<FlowOptionVO> optionList) {
        optionService.saveOrUpdateVO(optionList);
        return success();
    }
    
    @GetMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            optionService.delete(id);
        }
        return success();
    }
}
