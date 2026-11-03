package com.bytter.finance.web.security.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityAssetType;
import com.bytter.finance.domain.security.service.CmsSecurityAssetTypeService;
import com.bytter.finance.domain.security.vo.CmsSecurityAssetTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 基础资产类型表
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/assetType")
public class CmsSecurityAssetTypeController extends BaseController {
    @Autowired
    private CmsSecurityAssetTypeService cmsSecurityAssetTypeService;
    /**
     * 基础资产类型查询列表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityAssetType";
    }
    /**
     * 获取基础资产类型列表数据
     *
     * @return 基础资产类型数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return cmsSecurityAssetTypeService.getDataTable(dataTablesInput);
    }
    /**
     * 获取基础资产类型新增页路径
     *
     * @return 股权融资类型新增页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_ADD)
    @GetMapping("/add")
    public String add() {
        return "cms/security/addSecurityAssetType";
    }
    /**
     * 获取基础资产类型编辑页路径
     *
     * @param id    基础资产类型id
     * @param model 模型
     * @return 基础资产类型编辑页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_EDIT)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("cmsSecurityAssetType", cmsSecurityAssetTypeService.get(id, true));
        return "cms/security/updateSecurityAssetType";
    }
    /**
     * 新增基础资产类型
     *
     * @param cmsSecurityAssetType 基础资产类型
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_ADD)
    @LogMessage(content = "新增基础资产类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsSecurityAssetType cmsSecurityAssetType) {
        cmsSecurityAssetTypeService.saveSecurityAssetType(cmsSecurityAssetType, this.getCurrentUser());
        return success();
    }
    /**
     * 修改基础资产类型
     *
     * @param cmsSecurityAssetType 基础资产类型
     * @return 是否修改成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_EDIT)
    @LogMessage(content = "修改基础资产类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsSecurityAssetType cmsSecurityAssetType) {
        cmsSecurityAssetTypeService.updateSecurityAssetType(cmsSecurityAssetType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除基础资产类型
     *
     * @param id 基础资产类型id
     * @return 是否删除成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ASSETTYPE_DELETE)
    @LogMessage(content = "删除基础资产类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityAssetTypeService.deleteSecurityAssetType(id, this.getCurrentUser());
        return success();
    }
    /**
     * 获取有效的基础资产类型集合
     *
     * @return 有效的基础资产类型集合
     */
    @GetMapping("/listCmsSecurityAssetType")
    @ResponseBody
    public List<CmsSecurityAssetTypeVO> listType() {
        return cmsSecurityAssetTypeService.listSecurityAssetType();
    }
}
