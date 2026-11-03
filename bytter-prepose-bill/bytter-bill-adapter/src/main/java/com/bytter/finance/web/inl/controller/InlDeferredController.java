package com.bytter.finance.web.inl.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.ListUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlDeferred;
import com.bytter.finance.domain.inl.service.InlDeferredService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/inl/deferred")
public class InlDeferredController extends BaseController {
    @Autowired
    private InlDeferredService inlDeferredService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private InlRepayService inlRepayService;
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
        return inlDeferredService.datatable(input, getCurrentUser());
    }
    /**
     * 申请列表
     *
     * @param model
     * @return
     */
    @GetMapping("/listInlDeferred")
    public String listInlInfo(Model model) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.INL, BaseDataConstant.DEFERRED_NUM);
        model.addAttribute("config", configDTO);
        return "cms/inl/deferred/listInlDeferred";
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
        return "cms/inl/deferred/listApproveInlDeferred";
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
        return inlDeferredService.approveDataTable(input, getCurrentUser());
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
        return "cms/inl/deferred/viewApproveInlDeferred";
    }
    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInlDeferredVO cmsInlDeferredVO, Model model) {
        inlDeferredService.save(cmsInlDeferredVO, getCurrentUser());
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
        List<CmsInlDeferredVO> cmsInlDeferredVOS = inlDeferredService.getAllCmsInlDeferredBySendId(id);
        if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS)) {
            model.addAttribute("lastDeferredDate", cmsInlDeferredVOS.get(0).getNowDeferredDate());
            model.addAttribute("deferredCount", cmsInlDeferredVOS.size());
        } else {
            model.addAttribute("deferredCount", 0);
        }
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/deferred/addInlDeferred";
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
        CmsInlDeferred cmsInlDeferred = inlDeferredService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlDeferred.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlDeferred.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("cmsInlDeferred", cmsInlDeferred);
        List<CmsInlDeferredVO> cmsInlDeferredVOS = inlDeferredService
                .getAllCmsInlDeferredBySendId(cmsInlDeferred.getInlSendId());
        if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS)) {
            model.addAttribute("deferredCount", cmsInlDeferredVOS.size());
        } else {
            model.addAttribute("deferredCount", 0);
        }
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/deferred/editInlDeferred";
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
        CmsInlDeferred cmsInlDeferred = inlDeferredService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlDeferred.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlDeferred.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("cmsInlDeferred", cmsInlDeferred);
        List<CmsInlDeferredVO> cmsInlDeferredVOS = inlDeferredService
                .getAllCmsInlDeferredBySendId(cmsInlDeferred.getInlSendId());
        if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS)) {
            model.addAttribute("deferredCount", cmsInlDeferredVOS.size());
        } else {
            model.addAttribute("deferredCount", 0);
        }
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/deferred/viewInlDeferred";
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
        CmsInlDeferred cmsInlDeferred = inlDeferredService.get(id);
        model.addAttribute("cmsInlDeferreds", inlDeferredService.getAllCmsInlDeferredBySendId(id));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/deferred/viewAllInlDeferred";
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
        inlDeferredService.deleteById(id);
        return success();
    }
}
