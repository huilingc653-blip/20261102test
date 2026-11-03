package com.bytter.finance.web.directFinance.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.directFinance.entity.DfCostType;
import com.bytter.finance.domain.directFinance.service.DfCostTypeService;
import com.bytter.finance.domain.directFinance.vo.DfCostTypeVO;
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
@RequestMapping("/cms/dfCostType")
public class DfCostTypeController extends BaseController {
    @Autowired
    private DfCostTypeService dfCostTypeService;
    /**
     * 列表跳转
     *
     * @return
     */
    @GetMapping("/listCostType")
    public String listCostType() {
        return "cms/directFinance/dfCostType/listDfCostType";
    }
    /**
     * 新增跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "cms/directFinance/dfCostType/editDfCostType";
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
        return dfCostTypeService.listCostType(input);
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
        model.addAttribute("dfCostTypeVO", dfCostTypeService.getDfCostTypeById(id));
        return "cms/directFinance/dfCostType/editDfCostType";
    }
    /**
     * 保存数据
     *
     * @param dfCostType
     * @param model
     * @return
     */
    @LogMessage(content = "费用类型保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DfCostType dfCostType, Model model) {
        dfCostTypeService.saveOrUpdate(dfCostType);
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
        dfCostTypeService.deleteCheck(id);
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
    public AjaxJson validate(String id, String costCode, Model model) {
        boolean result = dfCostTypeService.validateCostCode(id, costCode);
        AjaxJson ajaxJson = new AjaxJson();
        if (result) {
            ajaxJson.setSuccess(true);
        } else {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
    /**
     * 费用类型查看
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "费用类型查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("dfCostTypeVO", dfCostTypeService.getDfCostTypeById(id));
        return "cms/directFinance/dfCostType/viewDfCostType";
    }
    /**
     * 查询生效的费用类型
     *
     * @return
     */
    @GetMapping("/getValidDfCostType")
    @ResponseBody
    public List<DfCostTypeVO> getValidDfCostType() {
        return dfCostTypeService.getValidDfcostType();
    }
}
