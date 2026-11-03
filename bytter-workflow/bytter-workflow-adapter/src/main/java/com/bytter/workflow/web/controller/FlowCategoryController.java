package com.bytter.workflow.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.workflow.constant.FlowCategoryConstant;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.service.FlowCategoryService;
import com.bytter.workflow.domain.vo.FlowCategoryVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
/**
 * Created by zhoul on 2017/7/13.
 * 流程类型
 */
@Controller
@RequestMapping("/bpm/flowCategory")
public class FlowCategoryController extends BaseController {
    private final FlowCategoryService categoryService;
    private final Ii18nService i18nService;
    public FlowCategoryController(FlowCategoryService flowCategoryService, Ii18nService i18nServiceI) {
        this.categoryService = flowCategoryService;
        this.i18nService = i18nServiceI;
    }
    @GetMapping("/index")
    public void index() {
       //do nothing
    }
    @GetMapping("/allList.json")
    @ResponseBody
    public TreeGridResponse<FlowCategoryVo> allList() {
        return new TreeGridResponse<>(categoryService.listAllApproveCategoryTreeTable());
    }
    @GetMapping("/view")
    public String view(String id, String parentId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FlowCategoryVo flowCategory = categoryService.findVoById(id);
            model.addAttribute("flowCategory", flowCategory);
        }
        if (StringUtils.isNotEmpty(parentId)) {
            FlowCategoryVo flowCategory = new FlowCategoryVo();
            flowCategory.setParentId(parentId);
            model.addAttribute("flowCategory", flowCategory);
        }
        return "bpm/flowCategory/view";
    }
    @GetMapping("/json")
    @ResponseBody
    public List<FlowCategoryVo> json(String id) {
        return categoryService.findByParentId(id);
    }
    @GetMapping("/tree")
    @ResponseBody
    public Object tree() throws Exception {
        List<FlowCategoryVo> menuList = categoryService.listAllApproveCategoryTreeTable();
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        for (FlowCategoryVo flowCategory : menuList) {
            if (flowCategory.getCateKey().startsWith("sys")) {
                continue;
            }
            //添加license控制
            String flowName = i18nService.getBusinessBaseContext(flowCategory.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", flowCategory.getId());
            map.put("pId", flowCategory.getParentId());
            map.put("name", StringUtils.isBlank(flowName) ? flowCategory.getName() : flowName);
            tempList.add(map);
        }
        return tempList;
    }
    @PostMapping("/update")
    @ResponseBody
    public Object update(FlowCategory flowCategory) {
        flowCategory.setType(FlowCategoryConstant.FLOW_CATEGORY_TYPE_APPROVE);
        categoryService.update(flowCategory);
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public Object save(FlowCategory flowCategory) {
        flowCategory.setType(FlowCategoryConstant.FLOW_CATEGORY_TYPE_APPROVE);
        categoryService.add(flowCategory);
        return success();
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") String id) {
        categoryService.delete(id);
        return success();
    }
    @GetMapping("/revokableUrl")
    @ResponseBody
    public Set<String> batchRevokeUrl() {
        return categoryService.batchRevokeUrl();
    }
}
