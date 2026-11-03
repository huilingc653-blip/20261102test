package com.bytter.finance.web.equity.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.equity.entity.CmsEquityType;
import com.bytter.finance.domain.equity.service.CmsEquityTypeService;
import com.bytter.finance.domain.equity.vo.CmsEquityTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 股权融资类型
 *
 * @author wangf
 */
@Controller
@RequestMapping("/cms/equity/type")
public class CmsEquityTypeController extends BaseController {
    @Autowired
    private CmsEquityTypeService cmsEquityTypeService;
    /**
     * 股权融资类型查询列表跳转
     *
     * @return 页面路径
     */
    @GetMapping("/list")
    public String list() {
        return "cms/equity/listEquityType";
    }
    /**
     * 获取股权融资类型新增页路径
     *
     * @return 股权融资类型新增页路径
     */
    @GetMapping("/add")
    public String add() {
        return "cms/equity/addEquityType";
    }
    /**
     * 获取股权融资类型编辑页路径
     *
     * @param id
     *            股权融资类型id
     * @param model
     *            模型
     * @return 股权融资类型编辑页路径
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("cmsEquityType", cmsEquityTypeService.get(id, true));
        return "cms/equity/updateEquityType";
    }
    /**
     * 获取股权融资类型列表数据
     *
     * @return 股权融资类型列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return cmsEquityTypeService.getDataTable(dataTablesInput);
    }
    /**
     * 新增股权融资类型
     *
     * @param cmsEquityType
     *            股权融资类型
     * @return 是否新增成功
     */
    @LogMessage(content = "新增债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsEquityType cmsEquityType) {
        cmsEquityTypeService.saveEquityType(cmsEquityType, this.getCurrentUser());
        return success();
    }
    /**
     * 修改股权融资类型
     *
     * @param cmsEquityType
     *            股权融资类型
     * @return 是否修改成功
     */
    @LogMessage(content = "修改股权融资类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsEquityType cmsEquityType) {
        cmsEquityTypeService.updateEquityType(cmsEquityType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除股权融资类型
     *
     * @param id
     *            股权融资类型id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除股权融资类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsEquityTypeService.deleteEquityType(id, this.getCurrentUser());
        return success();
    }
    /**
     * 获取有效的股权融资类型集合
     *
     * @return 有效的股权融资类型集合
     */
    @GetMapping("/listCmsEquityType")
    @ResponseBody
    public List<CmsEquityTypeVo> listEquityType() {
        return cmsEquityTypeService.listEquityType();
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsEquityTypeVo getTypeByCode(String typeCode) {
        return cmsEquityTypeService.getTypeByCode(typeCode);
    }
}
