package com.bytter.finance.web.security.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.security.entity.CmsSecurityRepaySure;
import com.bytter.finance.domain.security.service.CmsSecurityRepaySureService;
import com.bytter.finance.domain.security.vo.CmsSecurityRepaySureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 资产证券化还款确认表
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/repaySure")
public class CmsSecurityRepaySureController extends BaseController {
    @Autowired
    private CmsSecurityRepaySureService cmsSecurityRepaySureService;
    /**
     * 资产证券化登记主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityInfoForRepay";
    }
    /**
     * 融资申请列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_51);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_55);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_97);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_98);
        DataTablesOutput out = cmsSecurityRepaySureService.getDataTableInfo(dataTablesInput, this.getCurrentUser(), bizStatuss);
        return out;
    }
    /**
     * 资产证券化还款确认跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_OP)
    @GetMapping("/toRepay/{id}")
    public String toRepay(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/addSecurityRepaySure";
    }
    /**
     * 资产证券化还款确认明细查询跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_QUERY)
    @GetMapping("/viewRepay/{id}")
    public String viewRepay(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/viewSecurityRepaySureList";
    }
    /**
     * 股权融资明细列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_OP)
    @PostMapping("/listRepaySure")
    @ResponseBody
    public List<CmsSecurityRepaySure> listRepaySure(CmsSecurityRepaySureVO vo) {
        return cmsSecurityRepaySureService.listRepaySure(vo);
    }
    /**
     * 股权融资明细列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_OP)
    @PostMapping("/listRepaySureChose")
    @ResponseBody
    public List<CmsSecurityRepaySure> listRepaySureChose(CmsSecurityRepaySureVO vo) {
        vo.setIsChose("1");
        return cmsSecurityRepaySureService.listRepaySure(vo);
    }
    /**
     * 股权融资明细列表 - 全部数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_REPAY_SURE_OP)
    @PostMapping("/listRepaySureView")
    @ResponseBody
    public List<CmsSecurityRepaySure> listRepaySureView(CmsSecurityRepaySureVO vo) {
        return cmsSecurityRepaySureService.listRepaySureView(vo);
    }
    /**
     * 对数据进行选择 - 数据
     */
    @PostMapping("/addPaySure")
    @ResponseBody
    public AjaxJson addPaySure(@RequestBody CmsSecurityRepaySureVO vo) {
        cmsSecurityRepaySureService.addPaySure(vo);
        return success();
    }
    /**
     * 对选择数据进行删除 - 数据
     */
    @PostMapping("/deletePaySure")
    @ResponseBody
    public AjaxJson deletePaySure(@RequestBody CmsSecurityRepaySureVO vo) {
        cmsSecurityRepaySureService.deletePaySure(vo);
        return success();
    }
    /**
     * 对选择数据进行批量提交
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(String infoId) {
        //获取已选择的数据
        CmsSecurityRepaySureVO vo = new CmsSecurityRepaySureVO();
        vo.setSecurityInfoId(infoId);
        vo.setIsChose(CmsConstant.IS_TRUE);
        List<CmsSecurityRepaySure> repaySureList = cmsSecurityRepaySureService.listRepaySure(vo);
        //批量还款
        for (CmsSecurityRepaySure repaySure : repaySureList) {
            cmsSecurityRepaySureService.save(repaySure, this.getCurrentUser());
        }
        return success();
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
        return "cms/security/approve/listSecurityRepaySureApprove";
    }
    /**
     * 信息审批列表 - 数据
     *
     * @param input 前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput input) {
        return cmsSecurityRepaySureService.dataTableApprove(input);
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
        CmsSecurityRepaySure repaySure = cmsSecurityRepaySureService.get(businessId);
        model.addAttribute("repaySure", repaySure);
        return "cms/security/approve/viewSecurityRepaySureApprove";
    }
}
