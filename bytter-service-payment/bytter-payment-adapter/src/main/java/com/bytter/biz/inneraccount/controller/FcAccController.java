package com.bytter.biz.inneraccount.controller;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.inneraccount.service.FcAccBalanceQueryService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.inneraccount.vo.FcAccBalanceQueryVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcAccPlusVO;
import com.bytter.system.api.vo.FcAccVO;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Controller
@RequestMapping("/inneraccount/fcacc")
public class FcAccController extends BaseController {
    @Autowired
    private FcAccManageService fcAccService;
    @Autowired
    private FcAccBalanceQueryService fcAccBalanceQueryService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
    private IPricingDepositRateService pricingDepositRateService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IFcInstrateService fcInstrateService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 内部账户信息表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY},
//			logical = Logical.OR)
    @LogMessage(content = "查询内部账户信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{poolSign}")
    public String list(@PathVariable("poolSign") String poolSign, Model model) {
        model.addAttribute("poolSign", poolSign);
        return "inneraccount/listFcAcc";
    }

    /**
     * 内部账户信息表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY},
//			logical = Logical.OR)
    @LogMessage(content = "查询待复核的内部账户信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCheck/{poolSign}")
    public String listCheck(@PathVariable("poolSign") String poolSign, Model model) {
        model.addAttribute("poolSign", poolSign);
        return "inneraccount/listFcAccCheck";
    }

    /**
     * 内部账户信息表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY},
//			logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
            return iFcAccService.getDataTablesUnionForPool(dataTablesInput, getCurrentUser());
        } else {
            return iFcAccService.getDataTablesUnion(dataTablesInput, getCurrentUser());
        }
    }

    /**
     * 内部账户信息表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @GetMapping("/add")
    public String add(FcAccVO fcAcc, Model model) {
        if (StringUtils.isNotEmpty(fcAcc.getId())) {
            fcAcc = fcAccService.getFcAcc(fcAcc.getId());
            int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
            fcAcc.setAccAccNoRule(accAccNoRule);
        } else {
            fcAcc = new FcAccVO();
            int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
            fcAcc.setAccAccNoRule(accAccNoRule);
        }
        fcAcc.setPoolSign(AllocationConstant.POOL_SIGN_0);
        model.addAttribute("fcAccPage", fcAcc);
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs", "seqGeneratingMode").toInteger(1).intValue();
        model.addAttribute("seqNumPage", seqGeneratingMode);
        //根据资金池标志生成内部账号生成法则
        String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        model.addAttribute("relen", relen);
        //获取人民币，开户币种默认人民币
        model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
        return "inneraccount/addFcAcc";
    }

    /**
     * 内部账户信息表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @GetMapping("/addPool")
    public String addPool(FcAccVO fcAcc, Model model) {
        if (StringUtils.isNotEmpty(fcAcc.getId())) {
            fcAcc = fcAccService.getFcAcc(fcAcc.getId());
            int accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
            fcAcc.setAccAccNoRule(accAccNoRule);
        } else {
            fcAcc = new FcAccVO();
            int accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
            fcAcc.setAccAccNoRule(accAccNoRule);
        }
        fcAcc.setPoolSign(AllocationConstant.POOL_SIGN_1);
        model.addAttribute("fcAccPage", fcAcc);
        //获取人民币，开户币种默认人民币
        model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
        return "inneraccount/addFcAcc";
    }

    /**
     * 内部账户信息表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @GetMapping("/update")
    public String update(FcAccVO fcAcc, Model model) {
        if (StringUtils.isNotEmpty(fcAcc.getId())) {
            fcAccService.search(fcAcc.getId(), model);
        }
        return "inneraccount/updateFcAcc";
    }

    /**
     * 内部账户信息表详情页
     *
     * @return
     */
    @LogMessage(content = "查询内部账户信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{viewType}")
    public String view(@PathVariable("id") String id, @PathVariable("viewType") String viewType, Model model) {
        fcAccService.search(id, model);
        model.addAttribute("isView", "v");
        if ("cancel".equals(viewType) || "cancelFL".equals(viewType)) {
            model.addAttribute("isFL", "cancel");
            return "inneraccount/cancelFcAcc";
        } else if ("cancelEdit".equals(viewType) || "cancelEditFL".equals(viewType)) {
            model.addAttribute("isFL", "cancelEdit");
            return "inneraccount/cancelEditFcAcc";
        } else if ("change".equals(viewType)) {
            return "inneraccount/changeFcAcc";
        } else {
            if (InnerAccountConstant.LOG_TYPE_ALL.equals(viewType)) {
                //为日志查询指定
                model.addAttribute("logType", InnerAccountConstant.LOG_TYPE_ALL);
            }
            return "inneraccount/viewFcAcc";
        }
    }

    /**
     * 添加内部账户信息表
     *
     * @param fcAcc
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "添加内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcAccVO fcAcc) {
        if (fcAccService.isExistMulByBlnctrAcc(fcAcc.getBlnctrAcc(), CommonConstant.SUBMIT_ADD, fcAcc.getId())) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_UNION);
        }
        if(fcAcc.getReferenceRateType()==null||fcAcc.getReferenceRateType().isEmpty()){
            fcAcc.setReferenceRateType("0");
        }
        fcAccService.add(fcAcc, getCurrentUser());
        return success();
    }

    /**
     * 修改内部账户信息表
     *
     * @param fcAcc
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "修改内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcAccVO fcAcc) {
        FcAccVO t = fcAccService.getFcAcc(fcAcc.getId());
        if (fcAccService.isExistMulByBlnctrAcc(fcAcc.getBlnctrAcc(), CommonConstant.SUBMIT_MODIFY, fcAcc.getId())) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_UNION);
        }
        BeanUtils.copyNotNullProperties(fcAcc, t);
        fcAccService.modify(t, getCurrentUser());
        return success();
    }

    /**
     * 删除内部账户信息表
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "删除内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FcAccVO fcAcc = fcAccService.getFcAcc(id);
        //先删除计息账号
        fcAccService.delInterestAcc(fcAcc);
        fcAccService.deleteEntity(fcAcc, getCurrentUser());
        return success();
    }

    /**
     * 批量删除内部账户信息表
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "批量删除内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FcAccVO fcAcc = fcAccService.getFcAcc(id);
            fcAccService.deleteEntity(fcAcc, null);
        }
        return success();
    }

    /**
     * 生成顺序号
     *
     * @param request
     * @return
     */
    @LogMessage(content = "自动生成顺序号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/createSequence")
    @ResponseBody
    public AjaxJson createSequence(HttpServletRequest request) {
        AjaxJson json = new AjaxJson();
        String netId = request.getParameter("netId");
        String curId = request.getParameter("curId");
        String accType = request.getParameter("accType");
        String poolSign = request.getParameter("poolSign");
        String seqNum = request.getParameter("seqNum");
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(curId) && StringUtils.isNotEmpty(accType)) {
            String sequenceNo = "";
            if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
                sequenceNo = fcAccService.createSequenceForPool(netId.trim(), curId.trim(), accType.trim());
            } else {
                //手动生成
                if (EmptyUtils.isNotEmpty(seqNum)) {
                    sequenceNo = fcAccService.createManualSequence(netId.trim(), curId.trim(), accType.trim(), seqNum);
                } else {
                    sequenceNo = fcAccService.createSequence(netId.trim(), curId.trim(), accType.trim());
                }
            }
            json.setObj(sequenceNo);
            json.setSuccess(true);
        }
        return json;
    }

    /**
     * 页面跳转
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/businessAccount")
    public String businessAccount(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String netId = request.getParameter("netId");
        String accTypeCode = request.getParameter("accTypeCode");
        String type = request.getParameter("type");//add or update
        String opType = request.getParameter("opType"); //改户时有值  == change
        String poolSign = request.getParameter("poolSign");
        String curId = request.getParameter("curId");
        if (StringUtils.isNotEmpty(id)) {
            com.bytter.system.api.vo.FcAccVO fcAcc = null;
            if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
                fcAcc = fcAccService.findFcAccByIdForPool(id);
            } else {
                fcAcc = fcAccService.findFcAccById(id);
            }
            model.addAttribute("fcAccPage", fcAcc);
            model.addAttribute("opType", opType);
        }
        if (StringUtils.isNotEmpty(netId)) {
            Date accStartDate = null;
            if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
                accStartDate = fcAccService.getValidDateForPool(netId);
            } else {
                accStartDate = fcAccService.getValidDate(netId);
            }
            model.addAttribute("accStartDate", DateUtils.date2Str(accStartDate, DateUtils.date_sdf()));
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(accTypeCode) || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accTypeCode)
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accTypeCode)) {
                DepositRateDTO depositRateDTO = pricingDepositRateService.getDepositRateByAccType(accTypeCode, accStartDate, curId);
                if (depositRateDTO != null) {
                    model.addAttribute("interestRate", depositRateDTO.getRate());
                }
            }
        }
        model.addAttribute("poolSign", poolSign);
        return "inneraccount/" + type + InnerAccountConstant.AccTypeMap.get(accTypeCode);
    }

    /**
     * 内部账户复合页面跳转
     *
     * @return
     */
    @GetMapping("/check")
    public String check(FcAccVO fcAcc, Model model, HttpServletRequest request) {
        String cancelId = request.getParameter("cancelId");
        String busGlobalId = request.getParameter("busGlobalId");
        if (StringUtils.isNotEmpty(fcAcc.getId())) {
            fcAccService.search(fcAcc.getId(), model);
            model.addAttribute("isView", "C");
        }
        if (StringUtils.isNotEmpty(cancelId)) {//注销复核
            fcAccService.search(cancelId, model);
            model.addAttribute("busGlobalId", busGlobalId);
            return "inneraccount/checkFcAccCancel";
        }
        return "inneraccount/checkFcAcc";
    }

    /**
     * 开户复核通过或者失败
     *
     * @param fcAcc
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_APPLY)
    @LogMessage(content = "修改内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/checkPassOrBack")
    @ResponseBody
    public AjaxJson checkPassOrBack(FcAccVO fcAcc) {
        fcAccService.check(fcAcc, getCurrentUser());
        return success();
    }

    /**
     * 根据币别，网点 [可空]获取有效的内部账户。
     *
     * @param request
     * @return
     */
    @PostMapping("/getAccountTableData")
    @ResponseBody
    public List<FcAccPlusVO> getAccountTableData(HttpServletRequest request) {
        String netId = request.getParameter("netId");
        String corpId = request.getParameter("corpId");
        String curId = request.getParameter("curId");
        String type = request.getParameter("type");
        String accType = request.getParameter("accType");
        if (StringUtils.isNotEmpty(type) && "init".equals(type)) {
            return new ArrayList<FcAccPlusVO>();
        }
        //String sysSubjectCode=SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get();//企业活期存款
        String sysSubjectCode = null; //联行账号放开账户类型控制
        if (InnerAccountConstant.FCACC_LOANBYMANDATE.equals(accType)) {
            sysSubjectCode = SubjectConstant.SysSubject.INTEREST_ENTUST_DEPOSIT.get(); //委托存款
        }
        List<com.bytter.system.api.vo.FcAccPlusVO> fcAccListVO = fcAccService.listAccountByMoreParams(curId, netId, corpId, null, sysSubjectCode);
        return fcAccListVO;
    }

    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @PostMapping("/getValidAccount")
    @ResponseBody
    public List<com.bytter.system.api.vo.FcAccPlusVO> getValidAccount() {
        return fcAccService.getValidAccount();
    }

    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @PostMapping("/getValidAccountIsHavDel")
    @ResponseBody
    public List<com.bytter.system.api.vo.FcAccPlusVO> getValidAccountIsHavDel() {
        return fcAccService.getValidAccountIsHavDel();
    }

    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/getValidAccount")
    @ResponseBody
    public List<com.bytter.system.api.vo.FcAccPlusVO> getValidAccount2() {
        return fcAccService.getValidAccount();
    }

    /**
     * 内部账户信息表查询列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_ALLQUERY},
//			logical = Logical.OR)
    @LogMessage(content = "查询内部账户信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listAll/{poolSign}")
    public String listAll(@PathVariable("poolSign") String poolSign, Model model) {
        model.addAttribute("poolSign", poolSign);
        return "inneraccount/listAllFcAcc";
    }

    /**
     * 内部账户信息表 table列表
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_ALLQUERY},
//			logical = Logical.OR)
    @PostMapping("/dataTableAll")
    @ResponseBody
    public DataTablesOutput dataTableAll(@RequestBody DataTablesInput dataTablesInput) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
            return fcAccService.getDataTableAllForPool(dataTablesInput, getCurrentUser());
        } else {
            return fcAccService.getDataTableAll(dataTablesInput, getCurrentUser());
        }
    }

    /**
     * 内部账户信息表 table列表
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY)
    @PostMapping("/dataTableAllNormal")
    @ResponseBody
    public DataTablesOutput dataTableAllNormal(@RequestBody DataTablesInput dataTablesInput) {
        return fcAccService.getDataTableAllNormal(dataTablesInput, getCurrentUser());
    }

    /**
     * 根据币别，网点 [可空]获取有效的内部账户。
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "getChangeAcc")
    @ResponseBody
    public AjaxJson getChangeAcc(HttpServletRequest request) {
        String netId = request.getParameter("netId");
        String curId = request.getParameter("curId");
        String corpId = request.getParameter("corpId");
        String accType = request.getParameter("accType");
        String poolSign = request.getParameter("poolSign");
        String elem = request.getParameter("elem"); //业务元素[chargeAcc手续费收取账号/accForDeduct利息账号/accCussent贷款账号]
        String sysSubjectCode = SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get();//企业活期存款
        if (InnerAccountConstant.FCACC_OFFBALANCE.equals(accType)) {
            if ("accCussent".equals(elem)) {
                sysSubjectCode = SubjectConstant.SysSubject.ENTERPRISE_LOAN.get(); //企业放款
            } else {
                sysSubjectCode = SubjectConstant.SysSubject.INTEREST_WAIT_COMPOUND_LOAN.get(); //利息账号
            }
        }
        List<com.bytter.system.api.vo.FcAccPlusVO> fcAccListVO = null;
        if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
            fcAccListVO = fcAccService.listAccountByMoreParamsForPool(curId, netId, corpId, null, sysSubjectCode);
        } else {
            fcAccListVO = iFcAccService.listAccountByMoreParams(curId, netId, corpId, null, sysSubjectCode);
        }
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(fcAccListVO);
        return j;
    }

    /**
     * 获取资金池
     *
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY;
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }

    /**
     * 获取资金池
     *
     * @param poolId
     * @return
     */
    @RequestMapping(params = "getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY;
        return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }

    /**
     * 获取基准利率
     *
     * @param curId
     * @param subjectId
     * @return
     */
    @RequestMapping(params = "getInterestRate")
    @ResponseBody
    public AjaxJson getInterestRate(String curId, String subjectId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        BigDecimal rate = fcInstrateService.getFcInstrateByObjectId(subjectId, curId);
        j.setObj(rate);
        return j;
    }

    /**
     * 获取关联内部账号
     */
    @RequestMapping(params = "listFcAccByCorpId")
    @ResponseBody
    public AjaxJson listFcAccByCorpId(String corpId, String curId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        List<FcAccVO> list = fcAccService.listFcAccByCorpId(corpId, curId);
        j.setObj(list);
        return j;
    }

    /**
     * 获取账号信息
     */
    @RequestMapping(params = "getAccInfo")
    @ResponseBody
    public AjaxJson getAccInfo(String account) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        FcAccVO fcAccVO = fcAccService.getFcAccVOByBlnctrAcc(account);
        j.setObj(fcAccVO);
        return j;
    }

    /**
     * 根据网点单位科目币种和银行代码查询账户列表
     *
     * @return
     */
    @PostMapping("/getAccList")
    @ResponseBody
    public List<FcAccVO> getAccList(String netId, String corpId, String sysSubjectCode, String curId, String bankCode) {
        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
        Set<String> corpSet = new HashSet<>();
        corpSet.add(corpId);
        List<FcAccVO> accList = new ArrayList<>();
        if (entrySign == 0) {//结算中心
            accList = fcAccService.getAccListByInfo(netId, netId, sysSubjectCode, curId, "");
        } else {//成员单位或银行
            if (EmptyUtils.isEmpty(bankCode)) {
                accList = fcAccService.getAccListByInfo(netId, corpId, sysSubjectCode, curId, bankCode);
            } else {
                //根据联行号获取的银行类型信息
                BankTypeDTO dto = fcAccService.getInfoByBankCode(bankCode);
                if (!EmptyUtils.isEmpty(dto)) {
                    accList = fcAccService.getAccListByInfo(netId, corpId, sysSubjectCode, curId, dto.getId());
                }
            }
        }
        return accList;
    }

    /**
     * 计息账号信息查看
     *
     * @return
     */
    @GetMapping("/fcAccInfoPage")
    public String fcAccInfoPage(FcAccVO vo, Model model) {
        model.addAttribute("params", vo);
        return "inneraccount/fcAccInfoPage";
    }

    /**
     * 列表显示内部账号信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/accInfoDataTable")
    @ResponseBody
    public DataTablesOutput accInfoDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcAccService.listFcAccInfoDataTable(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 获取内部账号信息
     *
     * @param corpId
     * @param curId
     * @return
     */
    @PostMapping("/listAccount")
    @ResponseBody
    public List<FcAccVO> listAccount(String corpId, String curId) {
        return fcAccService.listFcAccByCorpId(corpId, curId);
    }

    /**
     * 内部账户选择组件
     *
     * @param corpId
     * @param model
     * @return
     */
    @GetMapping("/selectAccountByCorp")
    public String selectAccountByCorp(String corpId, String curId, Model model) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("curId", curId);
        model.addAttribute("poolSign", CommonConstant.NO);
        return "inneraccount/selectAccount";
    }

    @PostMapping("/dataTableSelect")
    @ResponseBody
    public DataTablesOutput dataTableSelect(@RequestBody DataTablesInput dataTablesInput) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
            return fcAccBalanceQueryService.getDataTablesUnionForPool(dataTablesInput, getUserId());
        } else {
            return fcAccBalanceQueryService.getDataTablesUnion(dataTablesInput, getUserId(),"0");
        }
    }

    /**
     * 获取单个账号的余额信息
     *
     * @param account
     * @return
     */
    @RequestMapping(params = "getFcAccBalByAccount")
    @ResponseBody
    public AjaxJson getFcAccBalByAccount(String account) {
        AjaxJson json = new AjaxJson();
        FcAccBalanceQueryVO fcAccBalanceQueryVO = null;
        if (StringUtils.isNotBlank(account)) {
            fcAccBalanceQueryVO = fcAccBalanceQueryService.getFcAccBalByAccount(account);
        }
        json.setSuccess(true);
        json.setObj(fcAccBalanceQueryVO);
        return json;
    }
}
