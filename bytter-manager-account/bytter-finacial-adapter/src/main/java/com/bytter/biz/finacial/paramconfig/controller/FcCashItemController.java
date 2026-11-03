package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.biz.finacial.paramconfig.entity.FcCashItem;
import com.bytter.biz.finacial.paramconfig.service.FcCashItemService;
import com.bytter.biz.finacial.paramconfig.vo.FcCashItemVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author
 * @version V1.0
 * @Title: dao
 * @Description: 现金流量项目controller
 * @date 2020-09-10 09:30:39
 */
@Controller
@RequestMapping("/finacial/paramconfig/cashItem")
public class FcCashItemController extends BaseController {
    @Autowired
    private FcCashItemService cashItemService;
    @DubboReference(check = false)
	private Ii18nService ii18nService;
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_CASHITEM_APPLY)
    @LogMessage(content = "查询现金流量科目列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/paramconfig/cashItem/listCashItem";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_CASHITEM_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cashItemService.listCashItem(dataTablesInput);
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_CASHITEM_APPLY)
    @LogMessage(content = "查询现金流量科目列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add(Model model) {
        return "finacial/paramconfig/cashItem/addCashItem";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_CASHITEM_APPLY)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcCashItem cashItem = cashItemService.get(id, true);
            model.addAttribute("cashItem", cashItem);
        }
        return "finacial/paramconfig/cashItem/updateCashItem";
    }
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(FcCashItem cashItem, Model model) {
        cashItemService.submitCashItem(cashItem, getCurrentUser());
        return success();
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_CASHITEM_APPLY)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String cashItemId, Model model) {
        cashItemService.deleteCashItem(cashItemId, getCurrentUser());
        return success();
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_CASHITEM_APPLY)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcCashItem cashItem = cashItemService.get(id, true);
            model.addAttribute("cashItem", cashItem);
        }
        return "finacial/paramconfig/cashItem/viewCashItem";
    }
    /**
     * 查询所有有效现金流项目
     * @return
     */
    @PostMapping("/listAllFcCashItem")
    @ResponseBody
    public List<FcCashItemVo> listAllFcCashItem(){
        return cashItemService.getAllCashItem();
    }
    /**
     *
     * @return
     */
    @GetMapping("/selecCashItemData")
    public String selectData(){
        return "finacial/paramconfig/cashItem/selectCashItem";
    }
}
