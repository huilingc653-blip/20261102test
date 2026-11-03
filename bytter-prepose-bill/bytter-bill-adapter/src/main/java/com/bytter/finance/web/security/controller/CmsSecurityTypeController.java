package com.bytter.finance.web.security.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityType;
import com.bytter.finance.domain.security.service.CmsSecurityTypeService;
import com.bytter.finance.domain.security.vo.CmsSecurityTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 资产证券化类型表
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/type")
public class CmsSecurityTypeController extends BaseController {
    @Autowired
    private CmsSecurityTypeService cmsSecurityTypeService;
    /**
     * 资产证券化类型查询列表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityType";
    }
    /**
     * 获取资产证券化类型列表数据
     *
     * @return 资产证券化类型数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return cmsSecurityTypeService.getDataTable(dataTablesInput);
    }
    /**
     * 获取资产证券化类型新增页路径
     *
     * @return 股权融资类型新增页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_ADD)
    @GetMapping("/add")
    public String add() {
        return "cms/security/addSecurityType";
    }
    /**
     * 获取资产证券化类型编辑页路径
     *
     * @param id    资产证券化类型id
     * @param model 模型
     * @return 资产证券化类型编辑页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_EDIT)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("cmsSecurityType", cmsSecurityTypeService.get(id, true));
        return "cms/security/updateSecurityType";
    }
    /**
     * 新增资产证券化类型
     *
     * @param cmsSecurityType 资产证券化类型
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_ADD)
    @LogMessage(content = "新增债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsSecurityType cmsSecurityType) {
        cmsSecurityTypeService.saveSecurityType(cmsSecurityType, this.getCurrentUser());
        return success();
    }
    /**
     * 修改资产证券化类型
     *
     * @param cmsSecurityType 资产证券化类型
     * @return 是否修改成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_EDIT)
    @LogMessage(content = "修改股权融资类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsSecurityType cmsSecurityType) {
        cmsSecurityTypeService.updateSecurityType(cmsSecurityType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除资产证券化类型
     *
     * @param id 资产证券化类型id
     * @return 是否删除成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TYPE_DELETE)
    @LogMessage(content = "删除股权融资类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityTypeService.deleteSecurityType(id, this.getCurrentUser());
        return success();
    }
    /**
     * 获取有效的资产证券化类型集合
     *
     * @return 有效的资产证券化类型集合
     */
    @GetMapping("/listCmsSecurityType")
    @ResponseBody
    public List<CmsSecurityTypeVO> listType() {
        return cmsSecurityTypeService.listSecurityType();
    }
}
