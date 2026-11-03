package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.inneraccount.dto.v1.*;
import com.bytter.biz.inneraccount.service.FcAccBalanceQueryService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.inneraccount.vo.FcAccBalanceQueryVO;
import com.bytter.biz.inneraccount.vo.v1.FcAccCommonVO;
import com.bytter.biz.inneraccount.vo.v1.FcAccOperatorVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccPlusVO;
import com.bytter.system.api.vo.FcAccVO;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
    *@Description 内部账户信息相关接口
    *  该Controller包含内容较多 内部账户 资金池管理账户 以及相关组件
    *@Author zhukr
    *@Date 2026/1/8
*/
@Api(value = "内部账户信息相关",tags = "内部账户信息相关")
@RestController("v1FcAccController")
@RequestMapping("/v1-payment/inneraccount/fcacc")
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
     * 内部账户信息表 table列表
     *及
     * @return
     */
    @ApiOperation("/datatable 内部账户信息表开户 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccListVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(iFcAccService.getDataTablesUnion(input, getCurrentUser()));
    }

    @ApiOperation("/datatablePool 资金池内部账户信息表开户 table列表")
    @PostMapping("/datatablePool")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccVO>> dataTablePool(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(iFcAccService.getDataTablesUnionForPool(input, getCurrentUser()));
    }

    /**
     * 内部账户信息表新增页面跳转
     *
     * @return
     */
    @ApiOperation(value = "/getAddConfig 内部账户开户新增所需参数",
                notes = "原先add跳转前获取的内容 代入新的跳转")
    @GetMapping("/getAddConfig")
    public AjaxJson<FcAccCommonVO> add() {
        AjaxJson<FcAccCommonVO> ajaxJson = new AjaxJson<>();
        FcAccCommonVO fcAccCommonVO = new FcAccCommonVO();
        FcAccVO fcAcc = new FcAccVO();
        int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
        fcAcc.setAccAccNoRule(accAccNoRule);
        fcAcc.setPoolSign(AllocationConstant.POOL_SIGN_0);
        fcAccCommonVO.setFcAccPage(fcAcc);
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs", "seqGeneratingMode").toInteger(1).intValue();
        fcAccCommonVO.setSeqNumModel(seqGeneratingMode);
        //根据资金池标志生成内部账号生成法则
        String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        fcAccCommonVO.setRenlen(relen);
        //获取人民币，开户币种默认人民币
        fcAccCommonVO.setRmbCur(btCurrencyService.getRMBCurrency());
        ajaxJson.setSuccess( true);
        ajaxJson.setObj(fcAccCommonVO);
        return ajaxJson;
    }

    /**
     * 内部账户信息表新增页面跳转
     *
     * @return
     */
    @ApiOperation(value = "/getAddPoolConfig 资金池内部账户开户新增所需参数"
            ,notes = "原先add跳转前获取的内容 代入新的跳转")
    @GetMapping("/getAddPoolConfig")
    public AjaxJson<FcAccCommonVO> addPool() {
        AjaxJson<FcAccCommonVO> ajaxJson = new AjaxJson<>();
        FcAccCommonVO fcAccCommonVO = new FcAccCommonVO();
        FcAccVO fcAcc = new FcAccVO();
        int accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
        fcAcc.setAccAccNoRule(accAccNoRule);
        fcAcc.setPoolSign(AllocationConstant.POOL_SIGN_1);
        fcAccCommonVO.setFcAccPage(fcAcc);
        //获取人民币，开户币种默认人民币
        fcAccCommonVO.setRmbCur(btCurrencyService.getRMBCurrency());
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(fcAccCommonVO);
        return ajaxJson;
    }

    /**
     * 内部账户信息表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("/getById 根据id查询账户信息")
    @ResponseBody
    @GetMapping("/getById")
    public AjaxJson<FcAccVO> getById(String id){
        FcAccCommonVO fcAccCommonVo = new FcAccCommonVO();
        fcAccService.search(id,fcAccCommonVo);
        return success(fcAccCommonVo);
    }
    /**
     * 内部账户信息表详情页
     *
     * @return
     */
    @ApiOperation(value = "/view 查询内部账户信息表详情所需信息",
            notes = "原先跳转逻辑\n" +
                    "1.viewType为cancel和cancelFl 跳转inneraccount/cancelFcAcc\n" +
                    "2.viewType为cancelEdit 和 cancelEditFL 跳转inneraccount/cancelEditFcAcc\n" +
                    "3.viewType为change时 跳转inneraccount/changeFcAcc\n" +
                    "4.其他情况跳转inneraccount/viewFcAcc")
    @LogMessage(content = "查询内部账户信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{viewType}")
    public AjaxJson<FcAccCommonVO> view(@PathVariable("id") String id, @PathVariable("viewType") String viewType, Model model) {
        AjaxJson<FcAccCommonVO> ajaxJson = new AjaxJson<>();
        FcAccCommonVO fcAccCommonVo = new FcAccCommonVO();
        fcAccService.search(id,fcAccCommonVo);
        fcAccCommonVo.setIsView("v");
        if ("cancel".equals(viewType) || "cancelFL".equals(viewType)) {
            fcAccCommonVo.setIsFl("cancel");
        } else if ("cancelEdit".equals(viewType) || "cancelEditFL".equals(viewType)) {
            fcAccCommonVo.setIsFl("cancelEdit");
        } else if ("change".equals(viewType)) {
        } else {
            if (InnerAccountConstant.LOG_TYPE_ALL.equals(viewType)) {
                //为日志查询指定
                fcAccCommonVo.setLogType(InnerAccountConstant.LOG_TYPE_ALL);
            }
        }
        ajaxJson.setObj(fcAccCommonVo);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    /**
     * 添加内部账户信息表
     *
     * @param fcAcc
     * @return
     */
    @ApiOperation(value = "/save 添加内部账户信息表")
    @LogMessage(content = "添加内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FcAccVO fcAcc) {
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
    @ApiOperation("/modify 修改内部账户信息表")
    @LogMessage(content = "修改内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody FcAccVO fcAcc) {
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
    @ApiOperation("/delete/{id} 删除内部账户信息表")
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
    @ApiOperation(value = "/batchDelete 批量删除内部账户信息表")
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
     * @param fcAccSequenceDTO
     * @return
     */
    @ApiOperation("/createSequence 自动生成顺序号  返回顺序号")
    @LogMessage(content = "自动生成顺序号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/createSequence")
    @ResponseBody
    public AjaxJson<String> createSequence(FcAccSequenceDTO fcAccSequenceDTO) {
        AjaxJson json = new AjaxJson();
        String netId    = fcAccSequenceDTO.getNetId();
        String curId    = fcAccSequenceDTO.getCurId();
        String accType  = fcAccSequenceDTO.getAccType();
        String poolSign = fcAccSequenceDTO.getPoolSign();
        String seqNum   = fcAccSequenceDTO.getSeqNum();
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
    @ApiOperation(value = "/businessAccount 对账号做操作前获取账号相关内容 原先是做页面跳转用",
                notes = "这里需要根据账号类型的accTypeCode跳转不同的页面\n" +
                        "01 FCACC_BUSINESS_ACCOUNT, \"FcAccBusinessAccount\");\n" +
                        "02 FCACC_BANK_DEMAND, \"FcAccBankDemand\");\n" +
                        "03 FCACC_LOAN, \"FcAccLoan\");\n" +
                        "04 FCACC_BANK_BORROW, \"FcAccBankBorrow\");\n" +
                        "05 FCACC_BUSINESS_BEGULAR, \"FcAccBusinessBegular\");\n" +
                        "06 FCACC_BANK_BEGULAR, \"FcAccBankBegular\");\n" +
                        "07 FCACC_INNER_ACCOUNT, \"FcAccInnerAccount\");\n" +
                        "08 FCACC_BANK_DEPOSIT, \"FcAccBankDeposit\");\n" +
                        "09 FCACC_LOANBYMANDATE, \"FcAccLoanByMandate\");\n" +
                        "10 FCACC_ENTRUSTED_DEPOSIT, \"FcAccEntrustedDeposit\");\n" +
                        "14 FCACC_OFFBALANCE, \"FcAccOffBalance\");\n" +
                        "16 FCACC_AGREEMENT, \"FcAccAgreement\");")
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/businessAccount")
    public AjaxJson<FcAccOperatorVO> businessAccount(FcAccOperatorDTO fcAccOperatorDTO) {
        FcAccOperatorVO fcAccOperatorVO = new FcAccOperatorVO();
        String id = fcAccOperatorDTO.getId();
        String netId = fcAccOperatorDTO.getNetId();
        String accTypeCode = fcAccOperatorDTO.getAccTypeCode();
        String type = fcAccOperatorDTO.getType();//add or update
        String opType = fcAccOperatorDTO.getOpType(); //改户时有值  == change
        String poolSign = fcAccOperatorDTO.getPoolSign();
        String curId = fcAccOperatorDTO.getCurId();
        fcAccOperatorVO.setType(type);
        if (StringUtils.isNotEmpty(id)) {
            FcAccVO fcAcc = null;
            if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
                fcAcc = fcAccService.findFcAccByIdForPool(id);
            } else {
                fcAcc = fcAccService.findFcAccById(id);
            }
            fcAccOperatorVO.setFcAccPage(fcAcc);
            fcAccOperatorVO.setOpType(opType);
        }
        if (StringUtils.isNotEmpty(netId)) {
            Date accStartDate = null;
            if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
                accStartDate = fcAccService.getValidDateForPool(netId);
            } else {
                accStartDate = fcAccService.getValidDate(netId);
            }
            fcAccOperatorVO.setAccStartDate(DateUtils.date2Str(accStartDate, DateUtils.date_sdf()));
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(accTypeCode) || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accTypeCode)
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accTypeCode)) {
                DepositRateDTO depositRateDTO = pricingDepositRateService.getDepositRateByAccType(accTypeCode, accStartDate, curId);
                if (depositRateDTO != null) {
                    fcAccOperatorVO.setInterestRate(depositRateDTO.getRate());
                }
            }
        }
        fcAccOperatorVO.setPoolSign(poolSign);
        fcAccOperatorVO.setAccTypeCode(accTypeCode);
        return success(fcAccOperatorVO);
    }

    /**
     * 内部账户复核页面跳转
     *
     * @return
     */
    @ApiOperation(value = "/check内部账户复核信息获取",
                    notes = "原先的check接口 \n" +
                            "跳转的页面为根据是否传值cancelId\n" +
                            "如果有跳转inneraccount/checkFcAccCancel\n" +
                            "如果没有跳转inneraccount/checkFcAcc\n")
    @GetMapping("/check")
    public AjaxJson<FcAccCommonVO> check(FcAccCheckDTO fcAccCheckDTO) {
        FcAccCommonVO fcAccCommonVO = new FcAccCommonVO();
        String cancelId = fcAccCheckDTO.getCancelId();
        String busGlobalId = fcAccCheckDTO.getBusGlobalId();
        if (StringUtils.isNotEmpty(fcAccCheckDTO.getId())) {
            fcAccService.search(fcAccCheckDTO.getId(), fcAccCommonVO);
            fcAccCommonVO.setIsView("C");
        }
        if (StringUtils.isNotEmpty(cancelId)) {//注销复核
            fcAccService.search(cancelId, fcAccCommonVO);
            fcAccCommonVO.setBusGlobalId(busGlobalId);
        }
        return success(fcAccCommonVO);
    }

    /**
     * 开户复核通过或者失败
     *
     * @param fcAcc
     * @return
     */
    @ApiOperation("/checkPassOrBack 修改内部账户信息表")
    @LogMessage(content = "修改内部账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/checkPassOrBack")
    @ResponseBody
    public AjaxJson checkPassOrBack(@RequestBody FcAccVO fcAcc) {
        fcAccService.check(fcAcc, getCurrentUser());
        return success();
    }

    /**
     * 根据币别，网点 [可空]获取有效的内部账户。
     * @return
     */
    @ApiOperation("/getAccountTableData 根据币别，网点 [可空]获取有效的内部账户")
    @PostMapping("/getAccountTableData")
    @ResponseBody
    public AjaxJson<List<FcAccPlusVO>> getAccountTableData(@RequestBody FcAccSearchDTO fcAccSearchDTO) {
        String netId   = fcAccSearchDTO.getNetId();
        String corpId  = fcAccSearchDTO.getCorpId();
        String curId   = fcAccSearchDTO.getCurId();
        String type    = fcAccSearchDTO.getType();
        String accType = fcAccSearchDTO.getAccType();
        if (StringUtils.isNotEmpty(type) && "init".equals(type)) {
            return success();
        }
        //String sysSubjectCode=SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get();//企业活期存款
        String sysSubjectCode = null; //联行账号放开账户类型控制
        if (InnerAccountConstant.FCACC_LOANBYMANDATE.equals(accType)) {
            sysSubjectCode = SubjectConstant.SysSubject.INTEREST_ENTUST_DEPOSIT.get(); //委托存款
        }
        List<FcAccPlusVO> fcAccListVO = fcAccService.listAccountByMoreParams(curId, netId, corpId, null, sysSubjectCode);
        return success(fcAccListVO);
    }

    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @ApiOperation("/getValidAccount 获取有效的内部账户。")
    @RequestMapping(path = "/getValidAccount",
            method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public AjaxJson<List<FcAccPlusVO>> getValidAccount() {
        return success(fcAccService.getValidAccount());
    }

    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @ApiOperation("/getValidAccountIsHavDel 获取有效的内部账户(包含已注销的)。")
    @PostMapping("/getValidAccountIsHavDel")
    @ResponseBody
    public AjaxJson<List<FcAccPlusVO>> getValidAccountIsHavDel() {
        return success(fcAccService.getValidAccountIsHavDel());
    }




    /**
     * 账户管理 ——> 内部账户管理——> 查询 ——> 内部账户查询 内部账户信息表 table列表
     *
     * @return
     */
    @ApiOperation("账户管理 ——> 内部账户管理——> 查询 账户信息表 table列表")
    @PostMapping("/dataTableAll")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccListVO>> dataTableAll(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAccService.getDataTableAll(input, getCurrentUser()));

    }

    /**
     * 资金池内部账户信息表 table列表
     *
     * @return
     */
    @ApiOperation("账户管理 ——> 单位资金池账户管理 ——> 查询 ——> 资金池内部账户表 table列表")
    @PostMapping("/dataTablePoolAll")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccListVO>> dataTablePoolAll(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAccService.getDataTableAllForPool(input, getCurrentUser()));

    }

    /**
     * 内部账户信息表 table列表
     *
     * @return
     */
    @ApiOperation("/dataTableAllNormal 暂时未找到前端对应使用")
    @PostMapping("/dataTableAllNormal")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccListVO>> dataTableAllNormal(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAccService.getDataTableAllNormal(input, getCurrentUser()));
    }

    /**
     * 根据币别，网点 [可空]获取有效的内部账户。
     *
     * @return
     */
    @ApiOperation("/getChangeAcc 根据币别，网点 [可空]获取有效的内部账户")
    @GetMapping(path = "/getChangeAcc")
    @ResponseBody
    public AjaxJson<FcAccPlusVO> getChangeAcc(@RequestParam(required = false) String netId,@RequestParam(required = false) String curId,
                                              @RequestParam(required = false) String corpId,@RequestParam(required = false) String accType,
                                              @RequestParam(required = false) String poolSign,@RequestParam(required = false) String elem) {
         //elem 业务元素[chargeAcc手续费收取账号/accForDeduct利息账号/accCussent贷款账号]
        String sysSubjectCode = SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get();//企业活期存款
        if (InnerAccountConstant.FCACC_OFFBALANCE.equals(accType)) {
            if ("accCussent".equals(elem)) {
                sysSubjectCode = SubjectConstant.SysSubject.ENTERPRISE_LOAN.get(); //企业放款
            } else {
                sysSubjectCode = SubjectConstant.SysSubject.INTEREST_WAIT_COMPOUND_LOAN.get(); //利息账号
            }
        }
        List<FcAccPlusVO> fcAccListVO = null;
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
    @ApiOperation("/getUserCorpPools 获取资源权限的资金池")
    @GetMapping(path = "/getUserCorpPools")
    @ResponseBody
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY;
        return success(fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource));
    }

    /**
     * 获取资源权限的资金池的子单位(包含主体单位本身)
     *
     * @param poolId
     * @return
     */
    @ApiOperation("/getUserCorpPoolChilds 获取资源权限的资金池的子单位(包含主体单位本身)")
    @GetMapping("/getUserCorpPoolChilds")
    @ResponseBody
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY;
        return success(fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId));
    }

    /**
     * 获取基准利率
     *
     * @param curId
     * @param subjectId
     * @return
     */
    @ApiOperation("/getInterestRate 获取基准利率")
    @GetMapping("/getInterestRate")
    @ResponseBody
    public AjaxJson<BigDecimal> getInterestRate(String curId, String subjectId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        BigDecimal rate = fcInstrateService.getFcInstrateByObjectId(subjectId, curId);
        j.setObj(rate);
        return j;
    }

    /**
     * 获取关联内部账号
     */
    @ApiOperation("/listFcAccByCorpId 获取关联内部账号")
    @GetMapping("/listFcAccByCorpId")
    @ResponseBody
    public AjaxJson<List<FcAccVO>> listFcAccByCorpId(@RequestParam(required = false) String corpId,
                                                     @RequestParam(required = false) String curId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        List<FcAccVO> list = fcAccService.listFcAccByCorpId(corpId, curId);
        j.setObj(list);
        return j;
    }

    /**
     * 获取账号信息
     */
    @ApiOperation("/getAccInfo 获取账号信息")
    @GetMapping("/getAccInfo")
    @ResponseBody
    public AjaxJson<FcAccVO> getAccInfo(String account) {
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
    @ApiOperation("/getAccList 根据网点单位科目币种和银行代码查询账户列表")
    @PostMapping("/getAccList")
    @ResponseBody
    public AjaxJson<List<FcAccVO>> getAccList(@RequestBody FcAccListSearchDTO fcAccListSearchDTO) {
        String corpId = fcAccListSearchDTO.getCorpId();
        String netId = fcAccListSearchDTO.getNetId();
        String sysSubjectCode = fcAccListSearchDTO.getSysSubjectCode();
        String curId = fcAccListSearchDTO.getCurId();
        String bankCode = fcAccListSearchDTO.getBankCode();

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
        return success(accList);
    }

    /**
     * 列表显示内部账号信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("/accInfoDataTable 列表显示内部账号信息")
    @PostMapping("/accInfoDataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccListVO>> accInfoDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAccService.listFcAccInfoDataTable(input, this.getCurrentUser()));
    }

    /**
     * 获取内部账号信息
     *
     * @param corpId
     * @param curId
     * @return
     */
    @ApiOperation("/listAccount 根据单位ID 币别 获取内部账号信息列表")
    @PostMapping("/listAccount")
    @ResponseBody
    public AjaxJson<List<FcAccVO>> listAccount(String corpId, String curId) {
        return success(fcAccService.listFcAccByCorpId(corpId, curId));
    }

//    /**
//     * 内部账户选择组件
//     *
//     * @param corpId
//     * @param model
//     * @return
//     */
//    @GetMapping("/selectAccountByCorp")
//    public String selectAccountByCorp(String corpId, String curId, Model model) {
//        model.addAttribute("corpId", corpId);
//        model.addAttribute("curId", curId);
//        model.addAttribute("poolSign", CommonConstant.NO);
//        return "inneraccount/selectAccount";
//    }

    @ApiOperation(value = "/dataTableSelect 内部账户选择组件",
    notes = "原先是通过/selectAccountByCorp接口 跳转前设置corpId，curId poolSign 跳转selectAccount.vm页面\n" +
            "现在不通过poolSign来判别页面  现在拆分为了两个接口dataTableSelect内部账户 和 dataTablePoolSelect资金池账户")
    @PostMapping("/dataTableSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccBalanceQueryVO>> dataTableSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAccBalanceQueryService.getDataTablesUnion(input, getUserId(),"1"));
    }

    @ApiOperation(value = "/dataTablePoolSelect 资金池内部账户选择组件",
            notes = "原先是通过/selectAccountByCorp接口 跳转前设置corpId，curId poolSign 跳转selectAccount.vm页面" +
                    "现在不通过poolSign来判别页面  现在拆分为了两个接口dataTableSelect内部账户 和 dataTablePoolSelect资金池账户")
    @PostMapping("/dataTablePoolSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccBalanceQueryVO>> dataTablePoolSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAccBalanceQueryService.getDataTablesUnionForPool(input, getUserId()));
    }

    /**
     * 获取单个账号的余额信息
     *
     * @param account
     * @return
     */
    @ApiOperation("/getFcAccBalByAccount 获取单个账号的余额信息")
    @GetMapping(path = "/getFcAccBalByAccount")
    @ResponseBody
    public AjaxJson<FcAccBalanceQueryVO> getFcAccBalByAccount(String account) {
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
