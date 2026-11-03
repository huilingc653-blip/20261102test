package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcAccDTO;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.interest.bean.AccTypeInterestInfoBean;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.service.FcInterestSingleLoanService;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.AccountDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * <br>
 * <b>功能：</b> 内部贷款个别计息:提供内部贷款的个别计息功能。核算管理》利息管理》个别计息》内部贷款个别计息》经办  <br>
 * <b>作者：</b> chenlw <br>
 * <b>日期：</b> 2020-12-24  <br>
 */
@Controller
@RequestMapping("/finacial/interest/single/loan")
public class FcInterestSingleLoanController extends BaseController {
    @Autowired
    private FcInterestSingleLoanService fcInterestSingleLoanService;
    @DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
    @Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    /**
     * 列表页面
     *
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_LOAN_HAND)
    @GetMapping("/list")
    public String list(Model model) {
        // 账户类别：03-放款
        model.addAttribute("accType", InnerAccountConstant.FCACC_LOAN);
        return "finacial/interest/single/loan/listInterestSingleLoan";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_LOAN_CHECK)
    @GetMapping("/check")
    public String checkBatchInterest(Model model) {
        // 账户类别：03-放款
        model.addAttribute("accType", InnerAccountConstant.FCACC_LOAN);
        return "finacial/interest/single/loan/checkInterestSingleLoan";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_TDA_CHECK)
    @GetMapping("/check/{id}")
    public String check(@PathVariable("id") String id, Model model) {
        FcInterestSingleVO entity = fcInterestSingleLoanService.getEntityById(id);
        entity.setEndDate(DateUtils.addDay(entity.getEndDate(), 1));
        model.addAttribute("entityInfo", entity);
        FcOnlineLedgerDTO onlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
        BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
        model.addAttribute("balance", checkBalance);//联机余额
        model.addAttribute("pageFlag", "2");
        model.addAttribute("opType", "checkList");
        return "finacial/interest/single/loan/viewInterestSingleLoan";
    }
    /**
     * 新增页面
     *
     * @param fcInterestSingle 个别计息实体
     * @param model            model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_LOAN_HAND)
    @GetMapping("/add")
    public String add(FcInterestSingle fcInterestSingle, Model model) {
        // 账户类别：03-放款
        model.addAttribute("accType", InnerAccountConstant.FCACC_LOAN);
        return "finacial/interest/single/loan/addInterestSingleLoan";
    }
    /**
     * 根据账户类型查询状态正常的内部账户
     *
     * @param accType 01-企业定期 02-银行定期 03-放款[内部贷款] 04-银行借款 05-企业定期 08-通知存款*#
     * @return
     */
    @PostMapping("/getInterestAccount")
    @ResponseBody
    public List<AccountDTO> getInterestAccount(String accType, String resource) {
        return fcInterestSingleLoanService.getInterestAccount(getCurrentUser(), accType, resource);
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcInterestSingleLoanService.queryFcInterestSingleLoan(dataTablesInput, getCurrentUser());
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_LOAN_HAND)
    @GetMapping("/update")
    public String update(String id, Model model) {
        FcInterestSingleVO entity = fcInterestSingleLoanService.getEntityById(id);
        model.addAttribute("entityInfo", entity);
        Date closeDate = dayCloseService.getDayCloseFinalDate(entity.getNetId());//根据内部账户所属网点查询该网点的日结日期
        FcAccDTO fcAccVO = new FcAccDTO();
        fcAccVO.setNetId(entity.getNetId());
        fcAccVO.setBlnctrAcc(entity.getAccount());
        fcAccVO.setAccType(entity.getAccType());
        // 查询内部账户余额
        FcOnlineLedgerDTO fcOnlineLedger = fcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());
        if (fcOnlineLedger != null) {
            // 余额 = 复核联机余额 - 当前冻结金额
            if (fcOnlineLedger.getCheckBalance() != null) {
                fcAccVO.setFcBalance(fcOnlineLedger.getCheckBalance().subtract(fcOnlineLedger.getFreezeMoney()));
            }
        }
        model.addAttribute("accEntity", fcAccVO);
        List<AccTypeInterestInfoBean> accTypeInterestInfos = fcInterestSingleLoanService.queryCanEditAccTypeInterestInfo(fcAccVO, getUserId(), "initList");
        Map<String, Object> map = fcInterestSingleLoanService.tallyDateInit(accTypeInterestInfos, closeDate);
        model.addAttribute("dataMap", map);
        return "finacial/interest/single/loan/updateInterestSingleLoan";
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcInterestSingleVO entity = fcInterestSingleLoanService.getEntityById(id);
        model.addAttribute("entityInfo", entity);
        // 账户类别：03-放款
        model.addAttribute("accType", InnerAccountConstant.FCACC_LOAN);
        FcOnlineLedgerDTO onlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
        BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
        model.addAttribute("balance", checkBalance);//联机余额
        model.addAttribute("pageFlag", "3");
        model.addAttribute("opType", "initList");
        return "finacial/interest/single/loan/viewInterestSingleLoan";
    }
    /**
     * 根据选中账户信息加载记账日期和起息日期初始化
     *
     * @param accountId 内部账户id
     * @param opType    initList-经办 check-复核
     * @return 记账日期和起息日期
     */
    @PostMapping("/tallyDateInit")
    @ResponseBody
    public Map<String, Object> tallyDateInit(String accountId, String opType) {
        return fcInterestSingleLoanService.tallyDateInit(accountId, opType, getCurrentUser());
    }
    /**
     * 计息/提交
     *
     * @param entity 参数实体
     * @return AjaxJson
     */
    @PostMapping("/doInterestaccrualByBatch")
    @ResponseBody
    public AjaxJson doInterestaccrualByBatch(FcInterestSingleVO entity) {
        AjaxJson json = new AjaxJson();
        try {
            if (!CmsConstant.REPAY_TYPE_4.equals(entity.getPayWay())) {
                if (entity.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_REPAYMONEY_ZERO);//不满足条件
                }
            }
            json = fcInterestSingleLoanService.doInterestaccrualBySingle(entity, getCurrentUser());
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                json.setMsg(new_name.getMessage());
                if (InterestMessageEnum.FIN_INTEREST_NOTENOUGH_BALANCE.equals(new_name.getIMessageEnum())) {
                    json.setObj("notBalance");
                }
            }
        }
        return json;
    }
    /**
     * <br>
     * <b>功能：</b> 跳转计息凭证页 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    @GetMapping("/toLoadInterestVoucher")
    public String listLoadInterestVoucher(Model model, FcInterestSingleVO entity, String opType) {
        Map<String, Object> result = fcInterestSingleLoanService.createInterestVoucher(entity, opType, getCurrentUser());
        if (EmptyUtils.isEmpty(result.get("interestTot")) || Integer.valueOf(String.valueOf(result.get("interestTot"))) <= 1) {
            result.put("interestTot", 0);
        }
        result.put("pageType", "interest");
        com.bytter.system.api.dto.FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(entity.getAccount());
        result.put("curName", fcAccDTO.getCurName());
        model.addAttribute("model", result);
        return "finacial/interest/single/interestVoucher";
    }
    /**
     * <br>
     * <b>功能：</b> 跳转积数清单 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/26 0026 <br>
     */
    @GetMapping("/accuBill")
    public String listaccuBill(Model model, String netId, String accType, String opType, String id, String account, String generateMode) {
        model.addAttribute("opType", opType);
        model.addAttribute("netId", netId);
        model.addAttribute("accType", accType);
        model.addAttribute("account", account);
        model.addAttribute("generateMode", generateMode);
        model.addAttribute("id", id);
        return "finacial/interest/single/loan/accuBill";
    }
    /**
     * <br>
     * <b>功能：</b> 查询积数清单 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/26 0026 <br>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/toLoadAccuBill")
    @ResponseBody
    public DataTablesOutput toLoadAccuBill(@RequestBody DataTablesInput dataTablesInput, FcInterestSingleVO entity) {
        Map<String, Object> search = dataTablesInput.getFormdata();
        DataTablesOutput out = new DataTablesOutput();
        String netId = (String) (search.get("netId") == null ? "" : search.get("netId"));
        String accType = (String) (search.get("accType") == null ? "" : search.get("accType"));
        String opType = (String) (search.get("opType") == null ? "" : search.get("opType"));
        String account = (String) (search.get("account") == null ? "" : search.get("account"));
        String id = (String) (search.get("id") == null ? "" : search.get("id"));
        entity.setId(id);
        entity.setAccount(account);
        entity.setNetId(netId);
        entity.setAccType(accType);
        dataTablesInput.setFormdata(null);
        Map<String, Object> result = fcInterestSingleLoanService.queryCanEditAccuBalance(dataTablesInput, entity, opType, getUserId());
        List result1 = (List) result.get("rows");
        result1.addAll((List) result.get("footer"));
        out.setRecordsTotal(Long.valueOf((Integer) result.get("rowsCount")));
        out.setData(result1);
        return new DataTablesOutput(dataTablesInput.getDraw(), result1, (Integer) result.get("rowsCount"), (Integer) result.get("rowsCount"));
    }
    /**
     * <b>功能介绍：</b>利息复核/打回<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return
     * @version 1.0
     */
    @RequestMapping(params = "doProcessInterestCheck")
    @ResponseBody
    public AjaxJson doProcessInterestCheck(@RequestBody List<FcInterestSingleVO> fisList) {
        AjaxJson json = new AjaxJson();
        try {
            if (fisList == null || fisList.size() == 0) {
                json.setSuccess(false);
                json.setMsg(multiLangServiceI.getLang("common.message.confirm.selectData"));
                return json;
            }
            Integer status = null;
            OperateType opType = null;
            if ("check".equals(fisList.get(0).getOpType())) {
                opType = OperateType.CHECK;
                status = InterestConstant.INTEREST_STATUE_CHECK;
            } else if ("back".equals(fisList.get(0).getOpType())) {
                opType = OperateType.BACK;
                status = InterestConstant.INTEREST_BACK;
            }
            ICurrentUser user = getCurrentUser();
            for (FcInterestSingleVO entity : fisList) {
                fcInterestSingleLoanService.checkSingleInterest(entity, opType, user, status);
            }
            json.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                json.setMsg(new_name.getMessage());
            }
        }
        return json;
    }
    /**
     * <b>功能介绍：</b>利息删除<br>
     *
     * @version 1.0
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id, String account, String netCode, String accTypeCode) {
        AjaxJson json = new AjaxJson();
        try {
            json = fcInterestSingleLoanService.deleteInterestSingle(id, account, netCode, accTypeCode, getCurrentUser());
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                json.setMsg(new_name.getMessage());
            }
        }
        return json;
    }
    /**
     * <br>
     * <b>功能：</b> 凭证打印跳转 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    @GetMapping("/doBatchPrint")
    public String doBatchPrint(Model model, FcInterestSingleVO entity, String opType) {
        Map<String, Object> result = fcInterestSingleLoanService.createInterestVoucher(entity, opType, getCurrentUser());
        if (EmptyUtils.isEmpty(result.get("interestTot")) || Integer.valueOf(String.valueOf(result.get("interestTot"))) <= 1) {
            result.put("interestTot", 0);
        }
        result.put("pageType", "interest");
        List<Map<String, Object>> voucherInfos = new ArrayList<Map<String, Object>>();
        voucherInfos.add(result);
        model.addAttribute("voucherInfos", voucherInfos);
        model.addAttribute("model", result);
        model.addAttribute("sum", 1);
        return "finacial/interest/voucherPrintTemplate";
    }
}
