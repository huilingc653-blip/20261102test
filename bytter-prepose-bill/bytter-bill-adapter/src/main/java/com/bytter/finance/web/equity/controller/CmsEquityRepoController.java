package com.bytter.finance.web.equity.controller;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityRepo;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 股权融资回购
 */
@Controller
@RequestMapping("/cms/equity/repo")
public class CmsEquityRepoController extends BaseController {
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityRepoService cmsEquityRepoService;
    @Autowired
    private CmsEquityRepoPlanService cmsEquityRepoPlanService;
    @Autowired
    private CmsEquityRepoPlanDetailService cmsEquityRepoPlanDetailService;
    /**
     * 查询股权融资列表-页面跳转
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/equity/listEquityRepo";
    }
    /**
     * 股权融资列表 - 数据
     */
    @PostMapping("/datatablePass")
    @ResponseBody
    public DataTablesOutput datatablePass(@RequestBody DataTablesInput dataTablesInput) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_52);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_55);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_51);// 打回
        return  cmsEquityInfoService.getDataEquityPassTable(dataTablesInput, getCurrentUser(),
                ResourceConstant.CMS_EQUITY_REPO_VIEW, bizStatus);
    }
    /**
     * 缴付明细列表-页面跳转
     */
    @GetMapping("/equityDetails/{id}")
    public String equityDetails(@PathVariable("id") String id, Model model) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_52);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_55);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_51);// 打回
        List<CmsEquityInfoDetailVO> detailList = cmsEquityInfoService.getDetails(id, bizStatus);
        model.addAttribute("list", detailList);
        return "cms/equity/listRepoEquityDetail";
    }
    /**
     * 缴付-页面跳转
     */
    @GetMapping("/repoEquity")
    public String repoEquity(String equityCode, String type, Model model) {
        CmsEquityRepoVO repoVO = new CmsEquityRepoVO();
        CmsEquityInfoDetailVO detailVO = cmsEquityInfoService.getDetail(equityCode);
        CmsEquityInfo info = cmsEquityInfoService.get(detailVO.getEquityId());
        model.addAttribute("corpId", info.getCorpId());
        model.addAttribute("curId", info.getCurId());
        if (type.equals("3")) {
            repoVO = cmsEquityRepoService.getByCode(equityCode, "temp").get(0);
        } else {
            // 计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
            repoVO.setRepoPlanId(repoPlanVO.getId());
            repoVO.setRepoMoney(repoPlanVO.getRepoMoney());
            cmsEquityRepoService.createRepo(repoVO, detailVO, type);
        }
        model.addAttribute("repoVO", repoVO);
        if (repoVO.getOnlineSign().equals("1")) {
            return "cms/equity/onlineRepoEquity";
        } else {
            return "cms/equity/offlineRepoEquity";
        }
    }
    /**
     * 缴付明细列表-页面跳转
     */
    @GetMapping("/equityPlans")
    public String equityPlans(String equityCode, String repoPlanDetailId, Model model) {
        // 计划明细
        List<CmsEquityRepoPlanDetailVO> planDetailList = cmsEquityRepoPlanDetailService
                .listRepoPlanDetailByEquityCodeOrPlanId(null, equityCode);
        model.addAttribute("planDetailList", planDetailList);
        model.addAttribute("detailPlanID", repoPlanDetailId);
        return "cms/equity/listEquityDetailRepoPlans";
    }
    /**
     * 提交
     */
    @LogMessage(content = "回购表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsEquityRepoVO cmsEquityRepoVO, String saveType) {
        cmsEquityRepoService.save(cmsEquityRepoVO, saveType, getCurrentUser());
        return success();
    }
    /**
     * 查看-页面跳转
     */
    @GetMapping("/view")
    public String view(String equityCode, String invWay, Model model) {
        if (StringUtils.isEmpty(equityCode) || "undefined".equals(equityCode)) {
            return null;
        }
        CmsEquityRepoVO repoVO = cmsEquityRepoService.getByCode(equityCode, "comm").get(0);// 找审批中的缴付
        model.addAttribute("repoVO", repoVO);
        if (repoVO.getOnlineSign().equals("1")) {
            return "cms/equity/viewOnlineRepoEquity";
        } else {
            return "cms/equity/viewOfflineRepoEquity";
        }
    }
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String equityCode) {
        cmsEquityRepoService.deleteRepo(equityCode, getCurrentUser());
        return success();
    }
    /******************** 审批 **********************/
    /**
     * 股权缴付过户审批列表-页面跳转
     */
    @LogMessage(content = "查询股权缴付过户审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityRepoApprove";
    }
    /**
     * 股权缴付过户审批列表 - 数据
     */
    @PostMapping("/datatableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityRepoService.getDatatableApprove(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/payApprove")
    public String payApprove(String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            CmsEquityRepo repo = cmsEquityRepoService.get(id);
            model.addAttribute("repo", repo);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/equity/approve/viewEquityRepoApprove";
    }
}
