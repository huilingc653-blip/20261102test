package com.bytter.finance.saas.controller.loan;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.loan.entity.CmsLoanDeferred;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanDeferredService;
import com.bytter.finance.domain.loan.vo.CmsLoanDeferredVO;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款展期申请表
 * @date 2017-08-04 14:22:49
 */
@Api(value = "/v1-preposebill/cms/loan/cmsloandeferred",tags = "贷款展期申请表")
@RestController("v1CmsLoanDeferredController")
@RequestMapping("/v1-preposebill/cms/loan/cmsloandeferred")
public class CmsLoanDeferredController extends BaseController {
    @Autowired
    private CmsLoanDeferredService cmsLoanDeferredService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private LoanTypeService loanTypeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
 
 
    /**
     * 贷款展期申请修改列表
     *
     * @return
     */
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanDeferredService.getEditDataTables(input, CmsConstant.IS_FALSE, getUserId()));
    }
    /**
     * 贷款展期处理列表
     *
     * @return
     */
    @PostMapping("/datatablePro")
    public AjaxJson<DataTablesOutput> datatablePro(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanDeferredService.getEditDataTables(input, CmsConstant.IS_TRUE, getUserId()));
    }
   
    /**
     * <b>功能介绍：</b>跳转单笔审批页面<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String id, String taskId) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(id)) {
            CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.put("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.put("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.put("cmsLoanDeferredPage", cmsLoanDeferred);
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            if (deferred.size() > 0) {// 上期展期到期日期
                model.put("deferredNum", deferred.size());// 展期次数
                model.put("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.put("taskId", taskId);
            // 已还金额
            BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
            model.put("repayedMoney", repayedMoney);
        }
        return AjaxJson.success(model);
    }
    
    /**
     * <b>功能介绍：</b>展期流程手机端详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileDeferredApprove")
    public AjaxJson<Map<String, Object>> mobileDeferredApprove(String businessId, String taskId) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(businessId);
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.put("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.put("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.put("cmsLoanDeferredPage", cmsLoanDeferred);
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            model.put("deferredNum", deferred.size());// 展期次数
            if (deferred.size() > 0) {// 上期展期到期日期
                model.put("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.put("taskId", taskId);
        }
        return AjaxJson.success(model);
    }
    /**
     * <b>功能介绍：</b>发放对应的展期列表数据<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/deferredInfoItems")
    public AjaxJson<List<CmsLoanDeferredVO>> deferredInfoItems(String liCode) {
        return AjaxJson.success(cmsLoanDeferredService.getCmsLoanDeferredItems(liCode));
    }


    /**
     * <b>功能介绍：</b>审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listDeferredApproveData")
    public AjaxJson<DataTablesOutput> listDeferredApproveData(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanDeferredService.listDeferredApproveData(input));
    }
    /**
     * 贷款展期申请表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(CmsLoanDeferred cmsLoanDeferred) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(cmsLoanDeferred.getLiCode())) {
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            //TODO 补充已还金额
            BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            model.put("deferredNum", deferred.size());// 展期次数
            if (deferred.size() > 0) {// 上期展期到期日期
                model.put("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.put("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.put("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.put("repayedMoney", repayedMoney);
            model.put("createDate", DateUtils.date2Str(DateUtils.date_sdf()));// 录入时间
        }
        return AjaxJson.success(model);
    }
    /**
     * 贷款展期申请表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<Map<String, Object>> update(String id) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(id)) {
            CmsLoanDeferred   cmsLoanDeferred = cmsLoanDeferredService.get(id);
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            //TODO 补充已还金额
            BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            model.put("deferredNum", deferred.size());// 展期次数
            model.put("isMoreDeferred", cmsLoanDeferredService.isMoreDeferred());// 是否多次展期
            if (deferred.size() > 0) {// 上期展期到期日期
                model.put("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.put("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.put("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.put("cmsLoanDeferredPage", cmsLoanDeferred);
            model.put("repayedMoney", repayedMoney);
        }
        return AjaxJson.success(model);
    }
    /**
     * 贷款展期申请表处理页面跳转
     *
     * @return
     */
    @GetMapping("/handle")
    public AjaxJson<Map<String, Object>> handle(CmsLoanDeferred cmsLoanDeferred) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(cmsLoanDeferred.getId())) {
            cmsLoanDeferred = cmsLoanDeferredService.get(cmsLoanDeferred.getId());
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            //TODO 不用减一，因为已经提交
            model.put("deferredNum", deferred.size());// 展期次数
            model.put("isMoreDeferred", cmsLoanDeferredService.isMoreDeferred());// 是否多次展期
            if (deferred.size() - 1 > 0) {// 上期展期到期日期
                model.put("lastDeferredReplayDate", deferred.get(deferred.size() - 2).getDeferredRepayDate());
            }
            model.put("cmsLoanSendPage", cmsLoanSend);// 发放页
            model.put("cmsLoanInfoPage", cmsLoanInfo);// 申请页
            model.put("cmsLoanDeferredPage", cmsLoanDeferred);
        }
        return AjaxJson.success(model);
    }
    /**
     * 贷款展期申请表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款展期申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String,Object> model=new HashMap<>();
        CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
        CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
        CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
        //TODO 补充已还金额
        BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        model.put("cmsLoanSendPage", cmsLoanSend);// 贷款发放
        model.put("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
        model.put("cmsLoanDeferredPage", cmsLoanDeferred);
        model.put("repayedMoney", repayedMoney);
        List<CmsLoanDeferred> deferred = cmsLoanDeferredService.getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
        if (deferred.size() > 0) {// 上期展期到期日期
            model.put("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
        }
        model.put("deferredNum", deferred.size());// 展期次数
        return AjaxJson.success(model);
    }
    /**
     * 添加贷款展期申请表
     *
     * @param cmsLoanDeferred
     * @return
     */
    @LogMessage(content = "添加贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(CmsLoanDeferred cmsLoanDeferred) {
        cmsLoanDeferredService.saveLoanDeferred(cmsLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 修改贷款展期申请表
     *
     * @param cmsLoanDeferred
     * @return
     */
    @LogMessage(content = "修改贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody  CmsLoanDeferred cmsLoanDeferred) {
        cmsLoanDeferredService.modifyCmsLoanDeferred(cmsLoanDeferred, getCurrentUser());
        return success();
    }

    @LogMessage(content = "贷款展期处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/doHandle")
    public AjaxJson doHandle(@RequestBody  CmsLoanDeferred cmsLoanDeferred) {
        cmsLoanDeferredService.doHandleLoanDeferred(cmsLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 删除贷款展期申请表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
        cmsLoanDeferred.setStatus(CmsConstant.DELETED);// 更新状态为删除
        cmsLoanDeferredService.updateEntity(cmsLoanDeferred);
        cmsLoanDeferredService.setSendDeferreding(cmsLoanDeferred, CmsConstant.COMPELETOTAL);// 恢复发放状态为记账完成
        msService.insertProcessMsg(cmsLoanDeferred.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_DEF_APPLY_DELETE, getCurrentUser());
        msService.insertProcessMsg(cmsLoanDeferred.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_DEF_APPLY_DELETE, getCurrentUser());
        notificationService.complete(NotificationConstant.CMS.LOAN_DEFERRED, cmsLoanDeferred.getId(),
                getCurrentUser().getId());
        return success();
    }
    /**
     * 批量删除贷款展期申请表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
            cmsLoanDeferredService.deleteEntity(cmsLoanDeferred);
        }
        return success();
    }
}
