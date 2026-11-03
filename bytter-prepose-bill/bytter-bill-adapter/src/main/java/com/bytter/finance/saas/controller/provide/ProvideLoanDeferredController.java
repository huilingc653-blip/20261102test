package com.bytter.finance.saas.controller.provide;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.provide.entity.ProvideLoanDeferred;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.service.ProvideLoanDeferredService;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.domain.provide.vo.ProvideLoanDeferredVO;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengxn
 * @version 1.0
 * @Title: 借款展期申请
 * @Description: CMS_PROVIDE_LOAN_SEND
 * @date 2017-07-24 14:13:13
 */
@Api(value = "银行借款发放表",tags = "借款展期申请")
@RestController("v1ProvideLoanDeferredController")
@RequestMapping("/v1-preposebill/cms/provide/provideloandeferred")
public class ProvideLoanDeferredController extends BaseController {
    @Autowired
    private ProvideLoanDeferredService provideLoanDeferredService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsProvideLoanRepayService cmsProvideLoanRepayService;
    /**
     * 功能介绍 :借款展期申请查询页面跳转<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<Map<String,Object>> list() {
        Map<String,Object> model=new HashMap<>();
        model.put("loanNum",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.BORROWDFNUM));
        model.put("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 :借款展期申请查询页面table表<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput  dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(provideLoanDeferredService.queryAll(input, getCurrentUser()));
    }
    /**
     * 功能介绍 :借款展期申请页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/application")
    public AjaxJson<Map<String, Object>> application(ProvideLoanDeferredVO vo) {
        Map<String,Object> model=new HashMap<>();
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        // 如果上次展期到期日不为空，则初始值追加一天，否则以到期日期追加一天
        if (ObjectUtil.isNotEmpty(deferredvo.getDeferredRepayDate())) {
            DateTime dateTime = DateUtil.offsetDay(deferredvo.getDeferredRepayDate(), 1);
            deferredvo.setDeferredRepayDateInit(dateTime);
        } else if (ObjectUtil.isNotEmpty(deferredvo.getRepayDate())) {
            DateTime dateTime = DateUtil.offsetDay(deferredvo.getRepayDate(), 1);
            deferredvo.setDeferredRepayDateInit(dateTime);
        }
        model.put("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        model.put("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.put("plRate", send.getPlRate());
        model.put("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = new ProvideLoanDeferred();
        provideLoanDeferred.setDeferredLimt(1);
        provideLoanDeferred.setCreateDate(new Date());
        provideLoanDeferred.setDeferredRate(send.getDeferredRate());
        provideLoanDeferred.setDeferredPoints(send.getDeferredPoints());
        model.put("provideLoanDeferred", provideLoanDeferred);
        model.put("loanNum",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.BORROWDFNUM));
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍。借款展期申请保存<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @param status状态
     *            0暂存 11提交审批 -1 打回 21补录审批 110业务结束
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/conserve")
    public AjaxJson conserve(@RequestBody  ProvideLoanDeferred provideLoanDeferred) {
        provideLoanDeferredService.save(provideLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 功能介绍 :借款展期补录查询页面跳转<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listAdditional")
    public AjaxJson<Map<String, Object>> listAdditional() {
        Map<String,Object> model=new HashMap<>();
        model.put("pageType", "listAddItional");
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 :借款展期补录明细页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/defadditional")
    public AjaxJson<Map<String, Object>> defAdditional(ProvideLoanDeferredVO vo) {
        Map<String,Object> model=new HashMap<>();
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        model.put("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        model.put("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.put("plRate", send.getPlRate());
        model.put("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = provideLoanDeferredService.get(vo.getDeferredId());
        model.put("provideLoanDeferred", provideLoanDeferred);
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 :借款展期修改跳转<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listUpdate")
    public AjaxJson<Map<String, Object>> listUpdate() {
        Map<String,Object> model=new HashMap<>();
        model.put("pageType", "listUpdate");
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 :借款展期修改明细页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/defupdate")
    public AjaxJson<Map<String, Object>> defUpdate(ProvideLoanDeferredVO vo) {
        Map<String,Object> model=new HashMap<>();
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        model.put("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        model.put("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.put("plRate", send.getPlRate());
        model.put("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = provideLoanDeferredService.get(vo.getDeferredId());
        model.put("provideLoanDeferred", provideLoanDeferred);
        return AjaxJson.success(model);
    }

    /**
     * 功能介绍 :借款展期处理table表<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTablehandle")
    public AjaxJson<DataTablesOutput> dataTableHandle(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(provideLoanDeferredService.queryHandle(input, getCurrentUser()));
    }
    /**
     * 功能介绍 :借款展期处理明细页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/defhandle")
    public AjaxJson<Map<String, Object>> defHandle(ProvideLoanDeferredVO vo) {
        Map<String,Object> model=new HashMap<>();
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        deferredvo.setRepayDate(new Date());
        model.put("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        BigDecimal m = money.getPassageMoney();
        model.put("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.put("plRate", send.getPlRate());
        model.put("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = provideLoanDeferredService.get(vo.getDeferredId());
        model.put("provideLoanDeferred", provideLoanDeferred);
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 :借款展期补录table表<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryTable")
    public AjaxJson<DataTablesOutput> dataQueryTable(@RequestBody V1DataTablesInput dataTablesInput, String pageType) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(provideLoanDeferredService.queryTable(input, getCurrentUser(), pageType));
    }
    /**
     * 功能介绍 :借款展期修改、打回页面table表<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/tableUpadate")
    public AjaxJson<DataTablesOutput>  tableUpadate(@RequestBody V1DataTablesInput dataTablesInput, String pageType) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(provideLoanDeferredService.queryTable(input, getCurrentUser(), pageType));
    }
    /**
     * 功能介绍 :借款展期修改/打回<br>
     * <b>日期：</b>2017年7月24日<br>
     * ersion 1.0
     *
     * @param status状态
     *            0暂存 11提交审批 -1 打回 21补录审批 110业务结束
     * @author zhengxn
     */
    @PostMapping("/modify")
    public AjaxJson update(String modifySign, ProvideLoanDeferred provideLoanDeferred) {
        provideLoanDeferredService.saveOrUpdate(modifySign, provideLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 借款展期修改-删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        provideLoanDeferredService.deleteDef(CmsConstant.DELETED, id, getCurrentUser());// -2已删除
        return success();
    }
    /**
     * <b>功能介绍：</b>多次展期列表跳转<br>
     * <b>日期：</b>2017年9月18日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    @GetMapping("/deferredInfolist")
    public AjaxJson<Map<String, Object>> deferredInfolist(ProvideLoanDeferred provideLoanDeferred) {
        Map<String,Object> model=new HashMap<>();
        model.put("liCode", provideLoanDeferred.getLiCode());
        return AjaxJson.success(model);
    }
    /**
     * <b>功能介绍：</b>多次展期列表明细<br>
     * <b>日期：</b>2017年9月27日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    @PostMapping("/deferredInfoItems")
    public AjaxJson<List<ProvideLoanDeferredVO>> deferredInfoItems(String liCode) {
        return AjaxJson.success(provideLoanDeferredService.deferredInfoItems(liCode));
    }
    // *****************审批BEGIN******************/
    /**
     * 功能介绍：借款展期申请审核列表<br>
     */
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String,Object> model=new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }
    /**
     * 借款展期申请审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(provideLoanDeferredService.dataTableApprove(input));
    }
    /**
     * 展期申请审批 详情页面
     */
    @GetMapping("/viewDeferredApprove")
    public AjaxJson<Map<String, Object>> viewDeferredApprove(String businessId, String taskId) {
        Map<String,Object> model=new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanDeferred deferred = provideLoanDeferredService.get(businessId);
            model.put("provideLoanDeferred", deferred);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(deferred.getLiCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
            model.put("loanInfo", loanInfoVo);
            ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(deferred.getLiCode());
            model.put("deferredvo", deferredvo);
            ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(deferred.getLiCode());
            model.put("money", money);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }
    /**
     * 借款展期补录审核列表
     **/
    @GetMapping("/listRepairApprove")
    public AjaxJson<Map<String, Object>>  listRepairApprove(String flowStatus, String cycleTime) {
        Map<String,Object> model=new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }
    /**
     * 借款展期补录审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/listdatatable")
    public AjaxJson<DataTablesOutput> listdatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(provideLoanDeferredService.getListDataTable(input));
    }
    /**
     * 展期申请审批 详情页面
     */
    @GetMapping("/viewDeferredRepairApprove")
    public AjaxJson<Map<String, Object>> viewDeferredRepairApprove(String businessId, String taskId) {
        Map<String,Object> model=new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanDeferred deferred = provideLoanDeferredService.get(businessId);
            model.put("provideLoanDeferred", deferred);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(deferred.getLiCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
            model.put("loanInfo", loanInfoVo);
            ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(deferred.getLiCode());
            model.put("deferredvo", deferredvo);
            ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(deferred.getLiCode());
            model.put("money", money);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }
    /**
     * 手机审批 申请、补录
     */
    @GetMapping("/mobileDeferredApprove")
    public AjaxJson<Map<String, Object>> mobileDeferredApprove(String businessId, String taskId) {
        Map<String,Object> model=new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanDeferred deferred = provideLoanDeferredService.get(businessId);
            model.put("provideLoanDeferred", deferred);
            LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(deferred.getLiCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());
            BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(loanSendVo.getBillCode(),
                    "1");// 在途金额
            BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(loanSendVo.getBillCode(),
                    "2");// 已还金额
            model.put("loanSend", loanSendVo);
            model.put("loanInfo", loanInfoVo);
            model.put("repayPassage", repayPassage);
            model.put("repayFinish", repayFinish);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }
}
