package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.credit.domain.service.RatItemTypeService;
import com.bytter.credit.dto.RatItemCustomDTO;
import com.bytter.credit.dto.RatItemTypeDTO;
import com.bytter.credit.dto.RatSysItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 15:44:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/cms/rat/ratitemcustom")
public class RatItemCustomController extends BaseController {
    @Autowired
    private RatItemCustomService ratItemCustomService;
    @Autowired
    private RatItemTypeService ratItemTypeService;
    /**
     * 授信品种列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询授信品种列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/rat/itemcustom/listRatItemCustom";
    }
    /**
     * 授信品种 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ratItemCustomService.listOutputData(dataTablesInput);
    }
    /**
     * 取系统维护的自定义授信品种
     *
     * @return
     */
    @GetMapping("/getAllRatItem")
    @ResponseBody
    public List<RatItemCustomDTO> getAllRatItem(String type,String ritCode) {
        if (StringUtils.isEmpty(type)) {
            type = RationConstant.RAT_TYPE_0;
        }
        return ratItemCustomService.getAllItemCustom(type);
    }
    /**
     * 取系统授信品种
     *
     * @return
     */
    @PostMapping("/sysItemJson")
    @ResponseBody
    public List<RatSysItemDTO> sysItemJson(String type) {
        if (StringUtils.isEmpty(type)) {
            type = RationConstant.RAT_TYPE_0;
        }
        return ratItemCustomService.getSysRatItemByType(type);
    }
    /**
     * 授信品种新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatItemCustom ratItemCustom, Model model) {
        if (StringUtils.isNotEmpty(ratItemCustom.getId())) {
            ratItemCustom = ratItemCustomService.get(ratItemCustom.getId());
            model.addAttribute("ratItemCustomPage", ratItemCustom);
        }
        // 系统授信品种
        List<RatSysItemDTO> sysItemCodes = ratItemCustomService.getSysRatItemByType(RationConstant.RAT_TYPE_0);
        List<RatItemTypeDTO> ritCodes = ratItemTypeService.getAllRatItemType();// 授信品种类型
        model.addAttribute("sysItemCodes", sysItemCodes);
        model.addAttribute("ritCodes", ritCodes);
        return "cms/rat/itemcustom/addRatItemCustom";
    }
    /**
     * 授信品种编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatItemCustom ratItemCustom, Model model) {
        if (StringUtils.isNotEmpty(ratItemCustom.getId())) {
            ratItemCustom = ratItemCustomService.get(ratItemCustom.getId());
            model.addAttribute("ratItemCustomPage", ratItemCustom);
            // 系统授信品种
            List<RatSysItemDTO> sysItemCodes = ratItemCustomService
                    .getSysRatItemByType(ratItemCustom.getType().toString());
            List<RatItemTypeDTO> ritCodes = ratItemTypeService.getAllRatItemType();// 授信品种类型
            model.addAttribute("sysItemCodes", sysItemCodes);
            model.addAttribute("ritCodes", ritCodes);
        }
        return "cms/rat/itemcustom/updateRatItemCustom";
    }
    /**
     * 授信品种详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信品种详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RatItemCustom ratItemCustom = ratItemCustomService.get(id);
        model.addAttribute("ratItemCustomPage", ratItemCustom);
        return "cms/rat/itemcustom/viewRatItemCustom";
    }
    /**
     * 添加授信品种
     * 
     * @param ratItemCustom
     * @return
     */
    @LogMessage(content = "添加授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatItemCustom ratItemCustom) {
        ratItemCustomService.saveRatItemCustom(ratItemCustom, getCurrentUser());
        return success();
    }
    /**
     * 修改授信品种
     * 
     * @param ratItemCustom
     * @return
     */
    @LogMessage(content = "修改授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatItemCustom ratItemCustom) {
        ratItemCustomService.updateRatItemCustom(ratItemCustom, getCurrentUser());
        return success();
    }
    /**
     * 删除授信品种
     * 
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatItemCustom ratItemCustom = ratItemCustomService.get(id);
        ratItemCustomService.deleteRatItemCustom(ratItemCustom);
        return success();
    }
    /**
     * 批量删除授信品种
     * 
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatItemCustom ratItemCustom = ratItemCustomService.get(id);
            ratItemCustomService.deleteEntity(ratItemCustom);
        }
        return success();
    }
}
