package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import com.bytter.finance.domain.inl.service.InlChangeService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlChangeVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "内部拆解还款计划")
@RestController("v1InlChangeController")
@RequestMapping("/v1-preposebill/inl/change")
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
     * @param dataTablesInput
     * @return
     */
    @ResponseBody
    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> DataTablesOutput(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlChangeService.datatable(input, getCurrentUser()));
    }

    /**
     * 利率调整新增
     *
     * @param id
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        /**
         * 获取最大还款日期
         * 当存在归还利息时则取最大归还利息日期
         */
        Date maxRepayedDate = cmsProvideInterestService.getMaxRepayedDate(cmsInlSendVO.getBillNo(), cmsInlSendVO.getSendDate());//获取最大的还款开始日期
        Date maxRepayStartDate = DateUtils.addDay(maxRepayedDate, 1);
        model.put("maxRepayStartDate", maxRepayStartDate);
        model.put("inlInfo", cmsInlInfoVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
//        model.put("repayPlans", inlChangeService.listRepayPlan(cmsInlSendVO.getId()));
        CmsRepaymentSchedule cmsRepaymentSchedule = inlChangeService.getCmsRepaymentSchedule(cmsInlSendVO.getId());
        if (!EmptyUtils.isEmpty(cmsRepaymentSchedule)) {
            model.put("planId", cmsRepaymentSchedule.getId());
        }
        return AjaxJson.success(model);
    }

    /**
     * 利率调整新增
     *
     * @param sendBillCode
     * @return
     */
    @GetMapping("/addUpdate")
    public AjaxJson<Map<String, Object>> addUpdate(String sendBillCode) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getSendByBillNo(sendBillCode);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        /**
         * 获取最大还款日期
         * 当存在归还利息时则取最大归还利息日期
         */
        Date maxRepayedDate = cmsProvideInterestService.getMaxRepayedDate(cmsInlSendVO.getBillNo(), cmsInlSendVO.getSendDate());//获取最大的还款开始日期
        Date maxRepayStartDate = DateUtils.addDay(maxRepayedDate, 1);
        model.put("maxRepayStartDate", maxRepayStartDate);
        model.put("inlInfo", cmsInlInfoVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
//        model.put("repayPlans", inlChangeService.listRepayPlan(cmsInlSendVO.getId()));
        CmsRepaymentSchedule cmsRepaymentSchedule = inlChangeService.getCmsRepaymentSchedule(cmsInlSendVO.getId());
        if (!EmptyUtils.isEmpty(cmsRepaymentSchedule)) {
            model.put("planId", cmsRepaymentSchedule.getId());
        }
        return AjaxJson.success(model);
    }

    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody CmsInlChangeVO cmsInlChangeVO) {
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
    public AjaxJson checkInterest(String inlInterestId, String sendBillCode) {
        inlChangeService.checkInterest(inlInterestId, sendBillCode);
        return success();
    }


    /**
     * 待审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listAbolishApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlChangeService.approveDataTable(input, getCurrentUser()));
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> edit(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlChange cmsInlChange = inlChangeService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlChange.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlChange.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlChange", cmsInlChange);
//        model.put("repayMainInfo", inlChangeService.getCmsRepaymentSchedule(cmsInlSendVO.getId()));
        model.put("repayPlans", inlChangeService.listRepaymentScheduleApproveByBillNo(cmsInlSendVO.getId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        model.put("planId", inlChangeService.getApproveSchedule(cmsInlSendVO.getId()).getId());
        return AjaxJson.success(model);
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> view(String id) {
        Map<String, Object> model = new HashMap<>();
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
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlChange", cmsInlChangeVO);
        model.put("repayPlans", inlChangeService.listRepaymentScheduleApproveByBillNo(cmsInlSendVO.getId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @GetMapping("/viewAll")
    public AjaxJson<Map<String, Object>> viewAll(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("cmsInlChanges", inlChangeService.getCmsInlChangeBySendId(id));
        model.put("repayPlans", inlChangeService.listRepayPlan(cmsInlSendVO.getId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    @PostMapping("/deletById")
    @ResponseBody
    public AjaxJson deletById(String id) {
        inlChangeService.deleteById(id);
        return success();
    }
}
