package com.bytter.finance.web.security.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityInfo;
import com.bytter.finance.domain.security.service.CmsSecurityAssetTypeService;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityTypeService;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 资产证券化登记主信息表
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/info")
public class CmsSecurityInfoController extends BaseController {
    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;
    @Autowired
    private CmsSecurityTypeService cmsSecurityTypeService;
    @Autowired
    private CmsSecurityAssetTypeService cmsSecurityAssetTypeService;
    /**
     * 资产证券化登记主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityInfo";
    }
    /**
     * 资产证券化登记主信息列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsSecurityInfoService.getDataTable(dataTablesInput, getCurrentUser(), null);
        return out;
    }
    /**
     * 获取资产证券化登记主信息新增页路径
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_ADD)
    @GetMapping("/add")
    public String add() {
        return "cms/security/addSecurityInfo";
    }
    /**
     * 新增资产证券化登记主信息
     *
     * @param cmsSecurityInfoVO 资产证券化登记主信息
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_ADD)
    @LogMessage(content = "新增资产证券化登记主信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody CmsSecurityInfoVO cmsSecurityInfoVO) {
        cmsSecurityInfoService.save(cmsSecurityInfoVO, this.getCurrentUser());
        return success();
    }
    /**
     * 获取资产证券化登记主信息编辑页路径
     *
     * @param id    资产证券化登记主信息id
     * @param model 模型
     * @return 资产证券化登记主信息编辑页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_EDIT)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        CmsSecurityInfo cmsSecurityInfo = cmsSecurityInfoService.get(id);
        model.addAttribute("info", cmsSecurityInfo);
        List<CmsSecurityLevelInfoVO> levelInfoVOList = cmsSecurityInfoService.listLevelInfo(id);
        model.addAttribute("levelInfoVOList", levelInfoVOList);
        return "cms/security/updateSecurityInfo";
    }
    /**
     * 修改资产证券化登记信息
     *
     * @param cmsSecurityInfoVO 资产证券化登记信息
     * @return 是否修改成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_EDIT)
    @LogMessage(content = "修改资产证券化登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody CmsSecurityInfoVO cmsSecurityInfoVO) {
        cmsSecurityInfoService.modify(cmsSecurityInfoVO, this.getCurrentUser());
        return success();
    }
    /**
     * 删除资产证券化登记信息
     *
     * @param id 资产证券化登记信息id
     * @return 是否删除成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_DELETE)
    @LogMessage(content = "删除资产证券化登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityInfoService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * 资产证券化登记信息详情页
     *
     * @param id    id
     * @param model model
     * @return 前端页面路径
     */
    @LogMessage(content = "查询资产证券化登记信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsSecurityInfoVO cmsSecurityInfoVO = cmsSecurityInfoService.getInfoById(id);
        cmsSecurityInfoVO.setSecurityTypeName(cmsSecurityTypeService.getTypeById(cmsSecurityInfoVO.getSecurityTypeId()).getTypeName());
        cmsSecurityInfoVO.setAssetTypeName(cmsSecurityAssetTypeService.getAssetTypeByCode(cmsSecurityInfoVO.getAssetTypeCode()).getAssetTypeName());
        model.addAttribute("info", cmsSecurityInfoVO);
        List<CmsSecurityLevelInfoVO> levelInfoVOList = cmsSecurityInfoService.listLevelInfo(id);
        model.addAttribute("levelInfoVOList", levelInfoVOList);
        return "cms/security/viewSecurityInfo";
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/security/approve/listSecurityInfoApprove";
    }
    /**
     * 资产证券化登记信息审批列表 - 数据
     *
     * @param input 前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return cmsSecurityInfoService.dataTableApprove(input);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId id
     * @param taskId     审批任务id
     * @param model      模型
     * @return 前端页面路径
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        model.addAttribute("taskId", taskId);
        CmsSecurityInfoVO cmsSecurityInfoVO = cmsSecurityInfoService.getInfoById(businessId);
        cmsSecurityInfoVO.setSecurityTypeName(cmsSecurityTypeService.getTypeById(cmsSecurityInfoVO.getSecurityTypeId()).getTypeName());
        cmsSecurityInfoVO.setAssetTypeName(cmsSecurityAssetTypeService.getAssetTypeByCode(cmsSecurityInfoVO.getAssetTypeCode()).getAssetTypeName());
        model.addAttribute("info", cmsSecurityInfoVO);
        List<CmsSecurityLevelInfoVO> levelInfoVOList = cmsSecurityInfoService.listLevelInfo(businessId);
        model.addAttribute("levelInfoVOList", levelInfoVOList);
        return "cms/security/approve/viewSecurityInfoApprove";
    }
}
