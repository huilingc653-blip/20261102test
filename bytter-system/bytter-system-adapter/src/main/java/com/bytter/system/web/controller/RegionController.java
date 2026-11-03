package com.bytter.system.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.Region;
import com.bytter.system.service.RegionService;
import com.bytter.system.vo.RegionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author zhoul
 * @date 2017/4/11
 */
@Controller
@RequestMapping("/sys/region")
public class RegionController extends BaseController {
    @Autowired
    private RegionService regionService;
    @GetMapping("/index")
    public void index(String parentId, Model model) {
        if (StringUtils.isNotBlank(parentId)) {
            Region region = regionService.get(parentId);
            model.addAttribute("region", region);
        }
        List<Region> regions = regionService.listByParentId(parentId);
        model.addAttribute("regions", regions);
        model.addAttribute("parentId", parentId);
    }
    @GetMapping("/json")
    @ResponseBody
    public List<RegionVO> json(String id) {
        return regionService.findByParentId(id);
    }
    @GetMapping("/view")
    public void view(String id, String parentId, Model model,String onlyViewFlag) {
        Region region = null;
        if (StringUtils.isNotBlank(id)) {
            region = regionService.get(id);
        }
        if (StringUtils.isNotBlank(parentId)) {
            region = new Region();
            region.setParentId(parentId);
        }
        model.addAttribute("region", region);
        model.addAttribute("onlyViewFlag", onlyViewFlag);
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return regionService.getDataTables(dataTablesInput);
    }
    @LogMessage(content = "更新 省市事件", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(Region region) {
        regionService.updateRegion(region);
        return success();
    }
    @LogMessage(content = "保存省市事件", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(Region region) {
        regionService.saveRegion(region);
        return success();
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        regionService.deleteRegion(id);
        return success();
    }
}
