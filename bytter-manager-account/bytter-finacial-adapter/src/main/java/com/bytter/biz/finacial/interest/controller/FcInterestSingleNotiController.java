package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcAccDTO;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.interest.bean.AccTypeInterestInfoBean;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.service.FcInterestService;
import com.bytter.biz.finacial.interest.service.FcInterestSingleNotiService;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
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
 * 企业通知存款个别计息
 */
@Controller
@RequestMapping("/finacial/interest/single/noti")
public class FcInterestSingleNotiController extends BaseController {
    @Autowired
    private FcInterestSingleNotiService fcInterestSingleDdaService;
    @DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
    @Autowired
	private FcOnlineLedgerService iFcOnlineLedgerService;
    @Autowired(required = false)
    private FcInterestService interestService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * <b>功能介绍:企业通知存款个别计息-经办</b><br>
     * <b>日期：</b>2020-12-14<br>
     * <b>作者：</b>xiejh<br>
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_HAND)
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/interest/single/noti/listInterestSingleNoti";
    }
    /**
     * <b>功能介绍：</b>企业通知存款个别计息-复核<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>xiejh<br>
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_CHECK)
    @GetMapping("/check")
    public String checkBatchInterest(Model model) {
//        model.addAttribute("opType","checkList");
        return "finacial/interest/single/noti/checkInterestSingleNoti";
    }
    /**
     * 查询企业通知存款个别计息列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcInterestSingleDdaService.queryFcInterestSingleDda(dataTablesInput, getCurrentUser());
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_HAND)
    @GetMapping("/add")
    public String add(FcInterestSingle fcInterestSingle, Model model) {
        return "finacial/interest/single/noti/addInterestSingleNoti";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_HAND)
    @GetMapping("/update")
    public String update(String id, Model model) {
        FcInterestSingleVO entity = fcInterestSingleDdaService.getEntityById(id);
        model.addAttribute("entityInfo", entity);
        Date closeDate = dayCloseService.getDayCloseFinalDate(entity.getNetId());//根据内部账户所属网点查询该网点的日结日期
        FcAccDTO fcAcc = new FcAccDTO();
        fcAcc.setNetId(entity.getNetId());
        fcAcc.setBlnctrAcc(entity.getAccount());
        fcAcc.setAccType(entity.getAccType());
        List<AccTypeInterestInfoBean> accTypeInterestInfos = fcInterestSingleDdaService.queryCanEditAccTypeInterestInfo(fcAcc, getUserId(), "initList");
        Map<String, Object> map = fcInterestSingleDdaService.tallyDateInit(accTypeInterestInfos, closeDate);
        FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
        BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
        model.addAttribute("dataMap", map);
        model.addAttribute("balance", checkBalance);//联机余额
        return "finacial/interest/single/noti/updateInterestSingleNoti";
    }
    ////@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_TDA_HAND)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcInterestSingleVO entity = fcInterestSingleDdaService.getEntityById(id);
        entity.setEndDate(DateUtils.addDay(entity.getEndDate(), 1));
        model.addAttribute("entityInfo", entity);
        FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
        BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
        model.addAttribute("balance", checkBalance);//联机余额
        model.addAttribute("pageFlag", "3");
        model.addAttribute("opType", "initList");
        return "finacial/interest/single/noti/viewInterestSingleNoti";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_CHECK)
    @GetMapping("/check/{id}")
    public String check(@PathVariable("id") String id, Model model) {
        FcInterestSingleVO entity = fcInterestSingleDdaService.getEntityById(id);
        entity.setEndDate(DateUtils.addDay(entity.getEndDate(), 1));
        model.addAttribute("entityInfo", entity);
        FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
        BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
        model.addAttribute("balance", checkBalance);//联机余额
        model.addAttribute("pageFlag", "2");
        model.addAttribute("opType", "checkList");
        return "finacial/interest/single/noti/viewInterestSingleNoti";
    }
    /**
     * 根据账户类型查询状态正常的内部账户
     *
     * @param accType 01-企业通知 02-银行通知 03-放款[内部贷款] 04-银行借款 05-企业通知 08-通知存款*#
     * @return
     */
    @PostMapping("/getInterestAccount")
    @ResponseBody
    public List<AccountDTO> getInterestAccount(String accType, String resource) {
        return fcInterestSingleDdaService.getInterestAccount(getCurrentUser(), accType, resource);
    }
    /**
     * 根据选中账户信息加载记账日期和起息日期初始化
     *
     * @param accountId 内部账户id
     * @param opType    initList-经办 check-复核
     * @return
     */
    @PostMapping("/tallyDateInit")
    @ResponseBody
    public Map<String, Object> tallyDateInit(String accountId, String opType) {
        return fcInterestSingleDdaService.tallyDateInit(accountId, opType, getCurrentUser());
    }
    /**
     * <br>
     * <b>功能：</b> 跳转计息凭证页 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    @GetMapping("/toLoadInterestVoucher")
    public String listLoadInterestVoucher(Model model, FcInterestSingleVO entity, String opType) {
        Map<String, Object> result = fcInterestSingleDdaService.createInterestVoucher(entity, opType, getCurrentUser());
        if (EmptyUtils.isEmpty(result.get("interestTot")) || Integer.valueOf(String.valueOf(result.get("interestTot"))) <= 1) {
            result.put("interestTot", 0);
        }
        result.put("pageType", "interest");
        model.addAttribute("model", result);
        return "finacial/interest/interestVoucher";
    }
    /**
     * <br>
     * <b>功能：</b> 跳转积数清单 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/26 0026 <br>
     */
    @GetMapping("/accuBill")
    public String listaccuBill(Model model, String netId, String accType, String opType, String id,
                               String account, String generateMode) {
        model.addAttribute("opType", opType);
        model.addAttribute("netId", netId);
        model.addAttribute("accType", accType);
        model.addAttribute("account", account);
        model.addAttribute("generateMode", generateMode);
        model.addAttribute("id", id);
        return "finacial/interest/single/noti/accuBill";
    }
    /**
     * <b>功能介绍：</b>加载积数清单<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/toLoadAccuBill")
    @ResponseBody
    public DataTablesOutput toLoadAccuBill(@RequestBody DataTablesInput dataTablesInput) {
        Map<String, Object> search = dataTablesInput.getFormdata();
        DataTablesOutput out = new DataTablesOutput();
        String netId = (String) (search.get("netId") == null ? "" : search.get("netId"));
        String accType = (String) (search.get("accType") == null ? "" : search.get("accType"));
        String opType = (String) (search.get("opType") == null ? "" : search.get("opType"));
        String account = (String) (search.get("account") == null ? "" : search.get("account"));
        String generateMode = (String) (search.get("generateMode") == null ? "" : search.get("generateMode"));
        dataTablesInput.setFormdata(null);
        Map<String, Object> result = interestService.queryAccountAccuBalance(dataTablesInput, netId, accType, opType, getUserId(), account, generateMode);
        List result1 = (List) result.get("rows");
        result1.addAll((List) result.get("footer"));
        out.setRecordsTotal(Long.valueOf((Integer) result.get("rowsCount")));
        out.setData(result1);
        return new DataTablesOutput(dataTablesInput.getDraw(), result1, (Integer) result.get("rowsCount"), (Integer) result.get("rowsCount"));
    }
    /**
     * <b>功能介绍: </b>计息/提交<br>
     *
     * @version 1.0
     */
    @RequestMapping(params = "doInterestaccrualByBatch")
    @ResponseBody
    public AjaxJson doInterestaccrualByBatch(FcInterestSingleVO entity) {
        AjaxJson json = new AjaxJson();
        try {
            if (entity.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_MONEY_ZERO);//不满足条件
            }
            json = fcInterestSingleDdaService.doInterestaccrualBySingle(entity, getCurrentUser());
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
                fcInterestSingleDdaService.checkSingleInterest(entity, opType, user, status);
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
            json = fcInterestSingleDdaService.deleteInterestSingle(id, account, netCode, accTypeCode, getCurrentUser());
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
        Map<String, Object> result = fcInterestSingleDdaService.createInterestVoucher(entity, opType, getCurrentUser());
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
