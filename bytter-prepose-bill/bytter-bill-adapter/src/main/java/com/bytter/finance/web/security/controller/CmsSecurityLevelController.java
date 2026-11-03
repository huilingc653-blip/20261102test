package com.bytter.finance.web.security.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityLevel;
import com.bytter.finance.domain.security.service.CmsSecurityLevelService;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 资产证券化层级表
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/level")
public class CmsSecurityLevelController extends BaseController {
    @Autowired
    private CmsSecurityLevelService cmsSecurityLevelService;
    /**
     * 资产证券化层级查询列表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityLevel";
    }
    /**
     * 获取资产证券化层级列表数据
     *
     * @return 资产证券化层级数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return cmsSecurityLevelService.getDataTable(dataTablesInput);
    }
    /**
     * 获取资产证券化层级新增页路径
     *
     * @return 资产证券化层级新增页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_ADD)
    @GetMapping("/add")
    public String add() {
        return "cms/security/addSecurityLevel";
    }
    /**
     * 获取资产证券化层级编辑页路径
     *
     * @param id    资产证券化层级d
     * @param model 模型
     * @return 资产证券化层级编辑页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_EDIT)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("cmsSecurityLevel", cmsSecurityLevelService.get(id, true));
        return "cms/security/updateSecurityLevel";
    }
    /**
     * 新增资产证券化层级
     *
     * @param cmsSecurityLevel 资产证券化层级
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_ADD)
    @LogMessage(content = "新增资产证券化层级", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsSecurityLevel cmsSecurityLevel) {
        cmsSecurityLevelService.saveSecurityLevel(cmsSecurityLevel, this.getCurrentUser());
        return success();
    }
    /**
     * 修改资产证券化层级
     *
     * @param cmsSecurityLevel 资产证券化层级
     * @return 是否修改成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_EDIT)
    @LogMessage(content = "修改股权融资层级", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsSecurityLevel cmsSecurityLevel) {
        cmsSecurityLevelService.updateSecurityLevel(cmsSecurityLevel, this.getCurrentUser());
        return success();
    }
    /**
     * 删除资产证券化层级
     *
     * @param id 资产证券化层级id
     * @return 是否删除成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_LEVEL_DELETE)
    @LogMessage(content = "删除股权融资层级", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityLevelService.deleteSecurityLevel(id, this.getCurrentUser());
        return success();
    }
    /**
     * 获取有效的资产证券化层级集合
     *
     * @return 有效的资产证券化层级集合
     */
    @GetMapping("/listCmsSecurityLevel")
    @ResponseBody
    public List<CmsSecurityLevelVO> listLevel() {
        return cmsSecurityLevelService.listSecurityLevel();
    }
}
