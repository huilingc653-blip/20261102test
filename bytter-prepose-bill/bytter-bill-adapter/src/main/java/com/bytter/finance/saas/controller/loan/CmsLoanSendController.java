package com.bytter.finance.saas.controller.loan;

import com.bytter.bill.domain.bill.vo.v1.CmsLoanSendDetailVO;
import com.bytter.bill.domain.bill.vo.v1.CmsLoanSendSaveVO;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanRepay;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanInfoService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款发放表
 * @date 2017-07-28 11:27:00
 */
@Api(value = "/v1-preposebill/cms/loan/cmsloansend",tags = "贷款申请")
@RestController("v1CmsLoanSendController")
@RequestMapping("/v1-preposebill/cms/loan/cmsloansend")
public class CmsLoanSendController extends BaseController {
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private IRatCpUrationService ucprationService;
    @Autowired
    private LoanTypeService loanTypeService;
    /**
     * 贷款合同生成列表
     *
     * @return
     */
    // @LogMessage(content = "查询贷款发放表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<Map<String, Object>> list() {
        Map<String,Object> model=new HashMap<>();
        model.put("pageType", "createContract");
        return AjaxJson.success(model);
    }
    /**
     * <b>功能介绍：</b>待还款申请列表<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listStayRepay")
    public AjaxJson<Map<String, Object>> listStayRepay( String repayPlan) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(repayPlan)) {
            model.put("repayPlan", repayPlan);
        }
        model.put("pageType", "stayRepay");
        return AjaxJson.success(model);
    }
    /**
     * <b>功能介绍：</b>待展期申请列表<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listDeferred")
    public AjaxJson<Map<String, Object>> listDeferred() {
        Map<String,Object> model=new HashMap<>();
        model.put("pageType", "createDeferred");
        model.put("isMoreDeferred", cmsLoanSendService.isMoreDeferred());
        return AjaxJson.success(model);
    }
    /**
     * 贷款发放表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String pageType) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanSendService.listCmsLoanSend(input, pageType));
    }
    /**
     * <b>功能介绍：</b>贷款合同生成列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/loanContractList")
    public AjaxJson<DataTablesOutput>  loanContractList(@RequestBody V1DataTablesInput dataTablesInput ) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanSendService.loanContractList(input, getUserId()));
    }
    /**
     * <b>功能介绍：</b>贷款还款申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/loanRepayList")
    public AjaxJson<DataTablesOutput>  loanRepayList(@RequestBody V1DataTablesInput dataTablesInput ) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanSendService.loanRepayList(input, getUserId()));
    }
    /**
     * <b>功能介绍：</b>贷款还款计划修改列表<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>songkk<br>
     *
     * @version 1.0
     */
    @PostMapping("/dataRepayPlanable")
    public AjaxJson<DataTablesOutput>  dataRepayPlanable(@RequestBody V1DataTablesInput dataTablesInput ) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanSendService.dataRepayPlanable(input));
    }
    /**
     * <b>功能介绍：</b>贷款展期申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/loanDeferredList")
    public AjaxJson<DataTablesOutput>  loanDeferredList(@RequestBody V1DataTablesInput dataTablesInput ) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanSendService.loanDeferredList(input, getUserId()));
    }
    /**
     * <b>功能介绍：</b>还款明细<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendCode
     * @return
     * @version 1.0
     */
    @GetMapping("/repayInfolist")
    public AjaxJson<List<CmsLoanRepay>> repayInfolist(String sendCode, String type ) {
        if (EmptyUtils.isNotEmpty(sendCode)) {
            List<CmsLoanRepay> repays = cmsLoanSendService.getCmsLoanRepayBySendCode(sendCode, type);
           return AjaxJson.success(repays);
        }
        return AjaxJson.success();
    }
    /**
     * <b>功能介绍：</b>发放审批列表跳转<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listSendApprove")
    public AjaxJson<Map<String, Object>> listSendApprove(String flowStatus, String cycleTime) {
        Map<String,Object> model=new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }
    /**
     * <b>功能介绍：</b>发放审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listSendApproveData")

    public AjaxJson<DataTablesOutput>  listSendApproveData(@RequestBody V1DataTablesInput dataTablesInput ) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanSendService.listSendApproveData(input));
    }
    /**
     * <b>功能介绍：</b>跳转单笔审批页面<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/approve")
    public AjaxJson<CmsLoanSendDetailVO> approve(String id, String taskId) {
        CmsLoanSendDetailVO model=new CmsLoanSendDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            boolean isCharging = false;
            CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(id);
            CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.get(id));
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
                model.setCpURationDTO(
                        ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
            }
            ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
            model.setIsGuarante(  isGuarantee.getValue());
            model.setCmsLoanInfo(  cmsLoanInfo);// 申请页
            model.setCmsLoanSendPage(  cmsLoanSend);
            model.setInwayMoney(  cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
            model.setRepayedMoney( cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
            model.setIsCharging(  isCharging);
            model.setTaskId(   taskId);
        }
        return AjaxJson.success(model);
    }
    /**
     * <b>功能介绍：</b>发放流程手机端详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileSendApprove")
    public AjaxJson<CmsLoanSendDetailVO> mobileSendApprove(String businessId, String taskId ) {
        CmsLoanSendDetailVO model=new CmsLoanSendDetailVO();
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(businessId);
            CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.get(businessId));
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.setCmsLoanInfoPage( cmsLoanInfo);// 申请页
            model.setCmsLoanSendPage(  cmsLoanSend);
            model.setLoanMoneyUpper(  StringUtils.chineseMoney(cmsLoanSend.getLoanMoney()));
            model.setTaskId(  taskId);
        }
        return AjaxJson.success(model);
    }


    /**
     * 贷款发放表编辑页面跳转
     *
     * @return
     */
    // @GetMapping("/update")
    public AjaxJson<CmsLoanSend> update(String id) {
        if (StringUtils.isNotEmpty(id)) {
            CmsLoanSend    cmsLoanSend = cmsLoanSendService.get(id);
            return AjaxJson.success(cmsLoanSend);
        }
        return AjaxJson.success();
    }
    /**
     * 贷款发放表详情页
     *
     * @return
     */
    // @LogMessage(content = "查询贷款发放表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<CmsLoanSendDetailVO> view(@PathVariable("id") String id, String param ) {
        CmsLoanSendDetailVO model=new CmsLoanSendDetailVO();
        boolean isCharging = false;
        TbBaseRateDTO baseRateDTO;
        TbCreditPeriodDTO creditPeriodDTO;
        CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(id);
        CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.get(id, true));
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
            model.setCpURationDTO(
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
        }
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.setIsGuarante(  isGuarantee.getValue());
        model.setCmsLoanSendPage(  cmsLoanSend);// 发放页
        model.setCmsLoanInfoPage(  cmsLoanInfo);// 申请页
        model.setInwayMoney(  cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
        model.setRepayedMoney(  cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
        model.setLoanMoneyUpper(  StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
        model.setIsCharging(  isCharging);
        if (StringUtils.isEmpty(param)) {
            model.setParam(  true);
        } else {
            model.setParam( false);
        }
        model.setShow( 1);
        return AjaxJson.success(model);
    }
    @GetMapping("/viewByBillCode/{billCode}")
    public AjaxJson<CmsLoanSendDetailVO>  viewByBillCode(@PathVariable("billCode") String billCode, String param) {
        CmsLoanSendDetailVO model=new CmsLoanSendDetailVO();
        boolean isCharging = false;
        CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendVOByBillCode(billCode);
        CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService
                .getLoanInfoByLoanSend(cmsLoanSendService.get(cmsLoanSend.getId(), true));
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
            model.setCpURationDTO(
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
        }
        model.setCmsLoanSendPage(  cmsLoanSend);// 发放页
        model.setCmsLoanInfoPage(  cmsLoanInfo);// 申请页
        model.setInwayMoney(  cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
        model.setRepayedMoney(  cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
        model.setLoanMoneyUpper(  StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
        model.setIsCharging( isCharging);
        if (StringUtils.isEmpty(param)) {
            model.setParam( true);
        } else {
            model.setParam(  false);
        }
        model.setShow(  0);
        model.setViewFlag( "0");// 查看标识
        return AjaxJson.success(model);
    }
    /**
     * 报表查询贷款详情
     *
     * @return
     */
    // @LogMessage(content = "报表查询贷款详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/queryView")
    public  AjaxJson<CmsLoanSendDetailVO>  queryView(String billCode, int type) {
        CmsLoanSendDetailVO model=new CmsLoanSendDetailVO();
        boolean isCharging = false;
        model.setIsCharging(  isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.setIsGuarante(  isGuarantee.getValue());
        String page = "";
        CmsLoanInfoVO cmsLoanInfo;
        if (type == 1) {
            CmsLoanInfo loanInfo = cmsLoanInfoService.getCmsLoanInfoBybillCode(billCode);
            cmsLoanInfo = cmsLoanInfoService.getCmsLoanInfoById(loanInfo.getId());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.setLoanMoneyUpper(  StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
            model.setCmsLoanInfoPage(  cmsLoanInfo);
            model.setViewType(  "loanInfo");
            page = "viewCmsLoanInfo";
        } else {
            CmsLoanSend sendInfo = cmsLoanSendService.getLoanSendByBillCode(billCode);
            if(sendInfo == null){
                cmsLoanInfo = null;
            }else{
                CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(sendInfo.getId());
                cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(sendInfo);
                cmsLoanInfo.setLoanGenreName(loanTypeService
                        .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
                model.setLoanMoneyUpper(   StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
                model.setCmsLoanSendPage(  cmsLoanSend);// 发放页
                model.setCmsLoanInfoPage(  cmsLoanInfo);// 申请页
                model.setInwayMoney(  cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
                model.setRepayedMoney( cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
            }
            page = "viewCmsLoanSend";
        }
        if (cmsLoanInfo!=null && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            model.setCpURationDTO(
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
        }
        model.setForwardUrl("cms/loan/createLoan/" + page);
        return AjaxJson.success(model);
    }
    /**
     * 添加贷款发放表
     *
     * @return
     */
    @LogMessage(content = "添加贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsLoanSendSaveVO cmsLoanSendVo) {
        CmsLoanSend cmsLoanSend=new CmsLoanSend();
        BeanUtil.copyProperties(cmsLoanSendVo,cmsLoanSend);
        cmsLoanSend.setStatus(CmsConstant.COMMITAPPROVE);
        cmsLoanSend.setBillCode(cmsLoanSendService.getLoanSendBillCode(CmsConstant.LOAN_SEND_BILL_CODE_PREFIX));
        cmsLoanSendService.saveLoanSendInfo(cmsLoanSend, getCurrentUser(), cmsLoanSendVo.getSendBillCode());
        return success();
    }
    /**
     * 修改贷款发放表
     *
     * @param cmsLoanSend
     * @return
     */
    @LogMessage(content = "修改贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody  CmsLoanSend cmsLoanSend) {
        CmsLoanSend t = cmsLoanSendService.get(cmsLoanSend.getId());
        BeanUtils.copyNotNullProperties(cmsLoanSend, t);
        cmsLoanSendService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除贷款发放表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanSend cmsLoanSend = cmsLoanSendService.get(id);
        cmsLoanSend.setStatus(CmsConstant.DELETED);// 更新状态为删除
        cmsLoanSendService.updateEntity(cmsLoanSend);
        return success();
    }
    /**
     * 批量删除贷款发放表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            CmsLoanSend cmsLoanSend = cmsLoanSendService.get(id);
            cmsLoanSendService.deleteEntity(cmsLoanSend);
        }
        return success();
    }
    @PostMapping("/getLoanTallyList")
    public AjaxJson<DataTablesOutput> getLoanTallyList(@RequestBody V1DataTablesInput  dataTablesInput, String opType) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(new DataTablesOutput());

    }
}
