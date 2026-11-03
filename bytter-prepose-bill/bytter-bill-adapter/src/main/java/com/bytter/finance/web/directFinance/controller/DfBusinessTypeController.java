package com.bytter.finance.web.directFinance.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.directFinance.entity.DfBusinessType;
import com.bytter.finance.domain.directFinance.service.DfBusinessTypeService;
import com.bytter.finance.domain.directFinance.vo.DfBusinessTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:39
 * @Version 1.0
 */
@Controller
@RequestMapping("/cms/dfBusinessType")
public class DfBusinessTypeController extends BaseController {
    @Autowired
    private DfBusinessTypeService dfBusinessTypeService;
    /**
     * 列表跳转
     *
     * @return
     */
    @GetMapping("/listBusinessType")
    public String listCostType() {
        return "cms/directFinance/dfBusinessType/listDfBusinessType";
    }
    /**
     * 新增跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "cms/directFinance/dfBusinessType/editDfBusinessType";
    }
    /**
     * 列表查询
     *
     * @param input
     * @param model
     * @return
     */
    @LogMessage(content = "费用类型列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input, Model model) {
        return dfBusinessTypeService.listBusinessType(input);
    }
    /**
     * 编辑跳转
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "费用类型编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/edit")
    public String add(String id, Model model) {
        model.addAttribute("dfBusinessTypeVO", dfBusinessTypeService.getDfBusinessTypeById(id));
        return "cms/directFinance/dfBusinessType/editDfBusinessType";
    }
    /**
     * 保存数据
     *
     * @param dfBusinessType
     * @param model
     * @return
     */
    @LogMessage(content = "费用类型保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DfBusinessType dfBusinessType, Model model) {
        dfBusinessTypeService.saveOrUpdate(dfBusinessType);
        return success();
    }
    /**
     * 删除数据
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "删除费用类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id, Model model) {
        dfBusinessTypeService.deleteCheck(id);
        return success();
    }
    /**
     * 数据校验
     *
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/validate")
    @ResponseBody
    public AjaxJson validate(String id, String businessCode, Model model) {
        boolean result = dfBusinessTypeService.validateBusinessCode(id, businessCode);
        AjaxJson ajaxJson = new AjaxJson();
        if (result) {
            ajaxJson.setSuccess(true);
        } else {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
    /**
     * 编辑跳转
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "费用类型编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("dfBusinessTypeVO", dfBusinessTypeService.getDfBusinessTypeById(id));
        return "cms/directFinance/dfBusinessType/viewDfBusinessType";
    }
    /**
     * 直融类别数据返回
     */
    @GetMapping("/getValidBusinessTypes")
    @ResponseBody
    public List<DfBusinessTypeVO> listBusinessType() {
        return dfBusinessTypeService.listBusinessTypeByParams(null);
    }
}
