package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.bond.entity.CmsBondType;
import com.bytter.finance.domain.bond.service.CmsBondTypeService;
import com.bytter.finance.domain.bond.vo.CmsBondTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 债券类型Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/cms/bond/type")
public class CmsBondTypeController extends BaseController {
    @Autowired
    private CmsBondTypeService cmsBondTypeService;
    /**
     * 债券类型查询列表跳转
     *
     * @return 页面路径
     */
    @GetMapping("/list")
    public String list() {
        return "cms/bond/listBondType";
    }
    /**
     * 获取债券类型新增页路径
     *
     * @return 债券类型新增页路径
     */
    @GetMapping("/add")
    public String add() {
        return "cms/bond/addBondType";
    }
    /**
     * 获取债券类型编辑页路径
     *
     * @param id
     *            债券类型id
     * @param model
     *            模型
     * @return 债券类型编辑页路径
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("cmsBondType", cmsBondTypeService.get(id, true));
        return "cms/bond/updateBondType";
    }
    /**
     * 获取债券类型列表数据
     *
     * @return 债券类型列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return cmsBondTypeService.getDataTable(dataTablesInput);
    }
    /**
     * 新增债券类型
     *
     * @param cmsBondType
     *            债券类型
     * @return 是否新增成功
     */
    @LogMessage(content = "新增债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsBondType cmsBondType) {
        cmsBondTypeService.saveBondType(cmsBondType, this.getCurrentUser());
        return success();
    }
    /**
     * 修改债券类型
     *
     * @param cmsBondType
     *            债券类型
     * @return 是否修改成功
     */
    @LogMessage(content = "修改债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsBondType cmsBondType) {
        cmsBondTypeService.updateBondType(cmsBondType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除债券类型
     *
     * @param id
     *            债券类型id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsBondTypeService.deleteBondType(id, this.getCurrentUser());
        return success();
    }
    /**
     * 获取有效的债券类型集合
     *
     * @return 有效的债券类型集合
     */
    @GetMapping("/listValidBondType")
    @ResponseBody
    public List<CmsBondTypeVo> listValidBondType() {
        return cmsBondTypeService.listValidBondType();
    }
}
