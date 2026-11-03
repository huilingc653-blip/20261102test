package com.bytter.finance.web.inl.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlRepayVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/inl/repay")
public class InlRepayController extends BaseController {
    @Autowired
    private InlRepayService inlRepayService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 申请列表数据
     *
     * @param input
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/dataTable")
    public DataTablesOutput DataTablesOutput(@RequestBody DataTablesInput input, Model model) {
        return inlRepayService.datatable(input, getCurrentUser());
    }
    /**
     * 查询暂存打回数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/listSendUpdateDataTable")
    public DataTablesOutput listSendUpdateDataTable(@RequestBody DataTablesInput input) {
        return inlRepayService.listRepayUpdateDataTable(input, getCurrentUser());
    }
    @GetMapping("/listRepayUpdate")
    public String listRepayUpdate(Model model) {
        return "cms/inl/repay/listInlRepayUpdate";
    }
    @GetMapping("/listRepayConfirm")
    public String listRepayConfirm(Model model) {
        return "cms/inl/repay/listInlRepayConfirm";
    }
    /**
     * 查询待确认数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/listRepayConfirmDataTable")
    public DataTablesOutput listRepayConfirmDataTable(@RequestBody DataTablesInput input) {
        return inlRepayService.listRepayConfirmDataTable(input, getCurrentUser());
    }
    /**
     * 还款确认
     *
     * @param id
     * @param realRepayDate
     * @return
     */
    @ResponseBody
    @PostMapping("/repayConfirm")
    public AjaxJson repayConfirm(String id, Date realRepayDate) {
        inlRepayService.otherDo(realRepayDate, id);
        return success();
    }
    /**
     * 申请列表
     *
     * @param model
     * @return
     */
    @GetMapping("/listInlRepay")
    public String listInlInfo(Model model) {
        return "cms/inl/repay/listInlRepay";
    }
    /**
     * 待审批列表
     *
     * @param flowStatus
     * @param model
     * @return
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/inl/repay/listApproveInlRepay";
    }
    /**
     * 待审批列表数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public DataTablesOutput listAbolishApprove(@RequestBody DataTablesInput input) {
        return inlRepayService.approveDataTable(input, getCurrentUser());
    }
    /**
     * 查看页面跳转
     *
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/inl/repay/viewApproveInlRepay";
    }
    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInlRepayVO cmsInlRepayVO, Model model) {
        inlRepayService.save(cmsInlRepayVO, getCurrentUser());
        return success();
    }
    /**
     * 展期申请新增
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(String id, Model model) {
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/repay/addInlRepay";
    }
    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        CmsInlRepayVO cmsInlRepayVO = inlRepayService.getById(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlRepayVO.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlRepayVO.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlRepay", cmsInlRepayVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/repay/editInlRepay";
    }
    /**
     * 查看
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String view(String id, Model model) {
        CmsInlRepayVO cmsInlRepayVO = inlRepayService.getById(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlRepayVO.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlRepayVO.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlRepay", cmsInlRepayVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/repay/viewInlRepay";
    }
    /**
     * 通过id删除暂存数据
     *
     * @param id
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/delete")
    public AjaxJson deleteById(String id, Model model) {
        inlRepayService.deleteById(id);
        return success();
    }
    /**
     * 查看
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewAll")
    public String viewAll(String id, Model model) {
        List<CmsInlRepayVO> cmsInlRepayVOS = inlRepayService.getAllRepayInfoBySendId(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlRepays", cmsInlRepayVOS);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/repay/viewAllInlRepay";
    }
}
