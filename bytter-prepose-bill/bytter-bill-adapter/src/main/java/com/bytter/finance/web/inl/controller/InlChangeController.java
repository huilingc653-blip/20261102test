package com.bytter.finance.web.inl.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlChange;
import com.bytter.finance.domain.inl.entity.CmsInlRepay;
import com.bytter.finance.domain.inl.service.InlChangeService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlChangeVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/inl/change")
public class InlChangeController extends BaseController {
    @Autowired
    private InlChangeService inlChangeService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private InlRepayService inlRepayService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @Autowired
    private RepayTypeService repayTypeService;
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
        return inlChangeService.datatable(input, getCurrentUser());
    }
    /**
     * 利率调整新增
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
        /**
         * 获取最大还款日期
         * 当存在归还利息时则取最大归还利息日期
         */
        Date maxRepayedDate = cmsProvideInterestService.getMaxRepayedDate(cmsInlSendVO.getBillNo(),cmsInlSendVO.getSendDate());//获取最大的还款开始日期
        Date maxRepayStartDate = DateUtils.addDay(maxRepayedDate, 1);
        model.addAttribute("maxRepayStartDate", maxRepayStartDate);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
//        model.addAttribute("repayPlans", inlChangeService.listRepayPlan(cmsInlSendVO.getId()));
        CmsRepaymentSchedule cmsRepaymentSchedule=inlChangeService.getCmsRepaymentSchedule(cmsInlSendVO.getId());
        if(!EmptyUtils.isEmpty(cmsRepaymentSchedule)) {
            model.addAttribute("planId", cmsRepaymentSchedule.getId());
        }
        return "cms/inl/change/addInlChange";
    }

    /**
     * 利率调整新增
     * @param sendBillCode
     * @param model
     * @return
     */
    @GetMapping("/addUpdate")
    public String addUpdate(String sendBillCode, Model model){
        CmsInlSendVO cmsInlSendVO = inlSendService.getSendByBillNo(sendBillCode);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        /**
         * 获取最大还款日期
         * 当存在归还利息时则取最大归还利息日期
         */
        Date maxRepayedDate = cmsProvideInterestService.getMaxRepayedDate(cmsInlSendVO.getBillNo(),cmsInlSendVO.getSendDate());//获取最大的还款开始日期
        Date maxRepayStartDate = DateUtils.addDay(maxRepayedDate, 1);
        model.addAttribute("maxRepayStartDate", maxRepayStartDate);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
//        model.addAttribute("repayPlans", inlChangeService.listRepayPlan(cmsInlSendVO.getId()));
        CmsRepaymentSchedule cmsRepaymentSchedule=inlChangeService.getCmsRepaymentSchedule(cmsInlSendVO.getId());
        if(!EmptyUtils.isEmpty(cmsRepaymentSchedule)) {
            model.addAttribute("planId", cmsRepaymentSchedule.getId());
        }
        return "cms/inl/change/addInlChange";
    }

    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInlChangeVO cmsInlChangeVO, Model model) {
        inlChangeService.save(cmsInlChangeVO, getCurrentUser());
        return success();
    }

    /**
     * 校验选择的计划数据
     *
     * @return
     */
    @PostMapping("/checkInterest")
    @ResponseBody
    public AjaxJson checkInterest(String inlInterestId,String sendBillCode, Model model) {
        inlChangeService.checkInterest(inlInterestId,sendBillCode);
        return success();
    }

    /**
     * 申请列表
     *
     * @param model
     * @return
     */
    @GetMapping("/listInlChange")
    public String listInlInfo(Model model) {
        return "cms/inl/change/listInlChange";
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
        return "cms/inl/change/listApproveInlChange";
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
        return inlChangeService.approveDataTable(input, getCurrentUser());
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
        return "cms/inl/change/viewApproveInlChange";
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
        CmsInlChange cmsInlChange = inlChangeService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlChange.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlChange.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlChange", cmsInlChange);
//        model.addAttribute("repayMainInfo", inlChangeService.getCmsRepaymentSchedule(cmsInlSendVO.getId()));
        model.addAttribute("repayPlans", inlChangeService.listRepaymentScheduleApproveByBillNo(cmsInlSendVO.getId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        model.addAttribute("planId", inlChangeService.getApproveSchedule(cmsInlSendVO.getId()).getId());
        return "cms/inl/change/editInlChange";
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
        CmsInlChange cmsInlChange = inlChangeService.get(id);
        String repayTypeName = "";
        if (StringUtils.isNotEmpty(cmsInlChange.getRepayType())) {
            // 获取还款方式
            RepayType repayType = repayTypeService.get(cmsInlChange.getRepayType());
            repayTypeName = repayType.getRepayName();
        }

        CmsInlChangeVO cmsInlChangeVO = new CmsInlChangeVO();
        BeanUtils.copyNotNullProperties(cmsInlChange, cmsInlChangeVO);
        cmsInlChangeVO.setRepayTypeName(repayTypeName);

        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlChange.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlChange.getInlInfoId());
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlChange", cmsInlChangeVO);
        model.addAttribute("repayPlans", inlChangeService.listRepaymentScheduleApproveByBillNo(cmsInlSendVO.getId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/change/viewInlChange";
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
        model.addAttribute("cmsInlChanges", inlChangeService.getCmsInlChangeBySendId(id));
        model.addAttribute("repayPlans", inlChangeService.listRepayPlan(cmsInlSendVO.getId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return "cms/inl/change/viewAllInlChange";
    }
    @PostMapping("/deletById")
    @ResponseBody
    public AjaxJson deletById(String id) {
        inlChangeService.deleteById(id);
        return success();
    }
}
