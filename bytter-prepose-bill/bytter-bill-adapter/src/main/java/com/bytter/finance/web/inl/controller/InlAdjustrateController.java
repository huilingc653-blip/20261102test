package com.bytter.finance.web.inl.controller;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlAdjustrate;
import com.bytter.finance.domain.inl.service.InlAdjustrateService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlAdjustrateVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/inl/adjustrate")
public class InlAdjustrateController extends BaseController {
    @Autowired
    private InlAdjustrateService inlAdjustrateService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private InlRepayService inlRepayService;
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
        return inlAdjustrateService.datatable(input, getCurrentUser());
    }
    /**
     * 申请列表
     *
     * @param model
     * @return
     */
    @GetMapping("/listInlAdjustrate")
    public String listInlInfo(Model model) {
        return "cms/inl/adjustrate/listInlAdjustrate";
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
        return "cms/inl/adjustrate/listApproveInlAdjustrate";
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
        return inlAdjustrateService.approveDataTable(input, getCurrentUser());
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
        return "cms/inl/adjustrate/viewApproveInlAdjustrate";
    }
    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInlAdjustrateVO cmsInlAdjustrateVO, Model model) {
        inlAdjustrateService.save(cmsInlAdjustrateVO, getCurrentUser());
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
        model.addAttribute("cmsInlAdjustrateVOs", inlAdjustrateService.getCmsInlAdjustrateVOBySendId(id));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/adjustrate/addInlAdjustrate";
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
        CmsInlAdjustrate cmsInlAdjustrate = inlAdjustrateService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlAdjustrate.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAdjustrate.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlAdjustrate", cmsInlAdjustrate);
        model.addAttribute("cmsInlAdjustrateVOs",
                inlAdjustrateService.getCmsInlAdjustrateVOBySendId(cmsInlAdjustrate.getInlSendId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/adjustrate/editInlAdjustrate";
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
        CmsInlAdjustrate cmsInlAdjustrate = inlAdjustrateService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlAdjustrate.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAdjustrate.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlAdjustrate", cmsInlAdjustrate);
        model.addAttribute("cmsInlAdjustrateVOs",
                inlAdjustrateService.getCmsInlAdjustrateVOBySendId(cmsInlAdjustrate.getInlSendId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/adjustrate/viewInlAdjustrate";
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
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("cmsInlAdjustrateVOs", inlAdjustrateService.getCmsInlAdjustrateVOBySendId(id));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/adjustrate/viewAllInlAdjustrate";
    }
    /**
     * 通过id删除暂存数据
     *
     * @param id
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/deletById")
    public AjaxJson deleteById(String id, Model model) {
        inlAdjustrateService.deleteById(id);
        return success();
    }
}
