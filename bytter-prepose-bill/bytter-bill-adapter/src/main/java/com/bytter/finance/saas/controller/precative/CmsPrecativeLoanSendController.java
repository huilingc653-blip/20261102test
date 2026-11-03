package com.bytter.finance.saas.controller.precative;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款发放表
 * @date 2017-07-29 10:45:29
 */

@Api(value = "/v1-cms/cms/precative/loansend", tags = "委托贷款发放表")
@RestController("v1CmsPrecativeLoanSendController")
@RequestMapping("/v1-cms/cms/precative/loansend")
public class CmsPrecativeLoanSendController extends BaseController {
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;

    /**
     * 委托贷款发放表 table列表
     */
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanSendService.getLoanSend(input, getUserId()));
    }

    /**
     * 委托贷款发放表新增页面跳转
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(CmsPrecativeLoanSend cmsPrecativeLoanSend) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(cmsPrecativeLoanSend.getId())) {
            cmsPrecativeLoanSend = cmsPrecativeLoanSendService.get(cmsPrecativeLoanSend.getId());
            model.put("cmsPrecativeLoanSendPage", cmsPrecativeLoanSend);
        }
        return AjaxJson.success(model);
    }

    /**
     * 委托贷款发放表详情页
     */
    // @LogMessage(content = "查询委托贷款发放表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> view(String fatherCode) {
        Map<String, Object> model = new HashMap<>();
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendService.getLoanSendVoByFatherCode(fatherCode);
        model.put("sendList", list);
        model.put("billCode", fatherCode);
        model.put("show", "1");
        return AjaxJson.success(model);
    }

    @GetMapping("/viewbybillcode")
    public AjaxJson<Map<String, Object>> viewByBillCode(String billCode) {
        Map<String, Object> model = new HashMap<>();
        PrecativeLoanSendVo vo = cmsPrecativeLoanSendService.getSendByBillCode(billCode);
        List<PrecativeLoanSendVo> list = new ArrayList<PrecativeLoanSendVo>();
        list.add(vo);
        model.put("sendList", list);
        model.put("billCode", vo.getFatherCode());
        model.put("show", "0");
        return AjaxJson.success(model);
    }

    /**
     * 添加委托贷款发放表
     */
    // @LogMessage(content = "添加委托贷款发放表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody CmsPrecativeLoanSend cmsPrecativeLoanSend) {
        cmsPrecativeLoanSendService.save(cmsPrecativeLoanSend);
        return success();
    }

    /**
     * 修改委托贷款发放表
     */
    // @LogMessage(content = "修改委托贷款发放表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsPrecativeLoanSend cmsPrecativeLoanSend) {
        CmsPrecativeLoanSend t = cmsPrecativeLoanSendService.get(cmsPrecativeLoanSend.getId());
        BeanUtils.copyNotNullProperties(cmsPrecativeLoanSend, t);
        cmsPrecativeLoanSendService.saveOrUpdate(t);
        return success();
    }

    /****
     * 已还记录查询
     */
    @GetMapping("/sendList")
    public AjaxJson<Map<String, Object>> repayList(String fatherCode) {
        Map<String, Object> model = new HashMap<>();
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendService.getLoanSendVoByFatherCode(fatherCode);
        model.put("sendList", list);
        return AjaxJson.success(model);
    }

    /**
     * 删除委托贷款发放表
     */
    // @LogMessage(content = "删除委托贷款发放表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsPrecativeLoanSend cmsPrecativeLoanSend = cmsPrecativeLoanSendService.get(id);
        cmsPrecativeLoanSendService.deleteEntity(cmsPrecativeLoanSend);
        return success();
    }

    /**
     * 发放申请页面跳转
     */
    @GetMapping("/send")
    public AjaxJson<Map<String, Object>> send(PrecativeLoanSendVo pre, String type) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(pre.getInfoBillCode())) {
            CmsPrecativeLoanInfoVo preVoInfo = cmsPrecativeLoanInfoService.queryInfo(pre.getInfoBillCode());
            preVoInfo.setMoney(new BigDecimal(preVoInfo.getMoney().doubleValue()));
            PrecativeLoanSendVo preVoSend = cmsPrecativeLoanSendService.getLoanSend(preVoInfo.getBillCode());
            int sendCount = cmsPrecativeLoanSendService.sendCount(preVoInfo.getBillCode());
            // 判断是否本位币
            boolean isNatualCurrency = cmsPrecativeLoanSendService.isNatualCurrency(preVoInfo.getCurId(),
                    preVoInfo.getCorpId());
            /*
             * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); String opdate = sdf.format(new Date());
             */
            if (!EmptyUtils.isEmpty(preVoSend.getId())) {
                CmsRepaymentSchedule repayPlan = repaymentScheduleService
                        .getCmsRepaymentScheduleByBillCode(preVoSend.getBillCode());
                if (!EmptyUtils.isEmpty(repayPlan)) {
                    preVoSend.setPlanId(repayPlan.getId());
                }
            }
            model.put("type", type);
            model.put("isNatualCurrency", isNatualCurrency);
            model.put("precativeInfoPage", preVoInfo);
            model.put("opDate", preVoInfo.getCreateDate());
            model.put("precativeSendPage", preVoSend);
            model.put("sendCount", sendCount);
        }
        return AjaxJson.success(model);
    }

    /**
     * 发放打回
     */
    @PostMapping("/back/{infoId}")
    @ResponseBody
    public AjaxJson back(@PathVariable("infoId") String infoId) {
        cmsPrecativeLoanSendService.back(infoId, getCurrentUser());
        return success();
    }

    /**
     * 发放提交暂存
     */
    @PostMapping("/submitOrTemp")
    @ResponseBody
    public AjaxJson submit( PrecativeLoanSendVo pre, String saveType) {
        cmsPrecativeLoanSendService.submitOrTemp(pre, saveType, getCurrentUser(), false);
        return success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeLoanSendService.dataTableApprove(input));
    }

    /**
     * 跳转至审核详情界面
     *
     * @param
     * @param taskId
     * @return String
     */
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(businessId);
        CmsPrecativeLoanInfoVo preVoInfo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
        model.put("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.put("precativeSendPage", sendVo);
        model.put("precativeInfoPage", preVoInfo);
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核列表
     */
    @GetMapping("/mobileSendApprove")
    public AjaxJson<Map<String, Object>> mobileSendApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendById(businessId);
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
            model.put("precativeSendPage", sendVo);
            model.put("precativePage", preVo);
            model.put("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
            model.put("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
            model.put("taskId", taskId);
        }
        return AjaxJson.success(model);
    }

    /**
     * 引入贷款发放
     */
    @PostMapping("/pullLoan")
    public AjaxJson<Map<String, Object>> pullLoan(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        String billCode = request.getParameter("billCode");
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendService.getLoanSendVoByFatherCode(billCode);
        model.put("sendList", list);
        model.put("billCode", billCode);
        return AjaxJson.success(model);
    }
}
