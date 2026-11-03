package com.bytter.biz.finacial.interest.controller;

import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.interest.bean.AccInterestBean;
import com.bytter.biz.finacial.interest.bean.InterestCommonBean;
import com.bytter.biz.finacial.interest.bean.InterestSearchBean;
import com.bytter.biz.finacial.interest.service.FcInterestService;
import com.bytter.biz.finacial.interest.vo.FcInterestVO;
import com.bytter.biz.finacial.interest.vo.InterestRateNoticeVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/finacial/interest/fcinterest")
@Slf4j
public class FcInterestController extends BaseController {
    @Autowired
    private FcInterestService fcInterestService;
    @DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * <b>功能介绍:批量计息-经办</b><br>
     * <b>日期：</b>2017年6月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @LogMessage(content = "查询FC_INTEREST_DTL_TEMP列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listBatchInterest")
    public String list(Model model, String opType, String netId, String accType) {
        if (EmptyUtils.isEmpty(opType)) {
            opType = "initList";
        }
        initBatchInterest(model, opType, netId, accType, "batchInterest", "calc_hand");
        if ("initList".equals(opType)) {
            model.addAttribute("notAuthNets", fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_INTEREST_CALCBYBATCH_HAND, getUserId()));
        } else {
            model.addAttribute("notAuthNets", fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_INTEREST_CALCBYBATCH_CHECK, getUserId()));
        }
        model.addAttribute("userName", getUserLoginName());
        return doDispatchPage("batchInterest");
    }
    /**
     * <b>功能介绍：</b>批量计息-复核<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/checkBatchInterest")
    public String checkBatchInterest(Model model, String opType, String netId, String accType) {
        initBatchInterest(model, "checkList", netId, accType, "batchInterest", "calc_check");
        model.addAttribute("notAuthNets", fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_INTEREST_CALCBYBATCH_CHECK, getUserId()));
        return doDispatchPage("batchInterest");
    }
    /**
     * <b>功能介绍:资金池批量计息-经办</b><br>
     * <b>日期：</b>2019年5月20日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_HAND)
    @LogMessage(content = "查询FC_INTEREST_DTL_TEMP列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listBatchInterestPool")
    public String listPool(Model model, String opType, String netId, String accType) {
        if (EmptyUtils.isEmpty(opType)) {
            opType = "initList";
        }
        initBatchInterest(model, opType, netId, accType, "batchInterest", InterestConstant.INTEREST_TYPE_POOL_CALC);
		/*if("initList".equals(opType)){
			model.addAttribute("notAuthNets",fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_HAND,getUserId()));
		}else{
			model.addAttribute("notAuthNets",fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_CHECK,getUserId()));
		}*/
        return doDispatchPage("batchInterestPool");
    }
    /**
     * <b>功能介绍：</b>批量计息-复核<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_CHECK)
    @GetMapping("/checkBatchInterestPool")
    public String checkBatchInterestPool(Model model, String opType, String netId, String accType) {
        initBatchInterest(model, "checkList", netId, accType, "batchInterest", InterestConstant.INTEREST_TYPE_POOL_CHECK);
//		model.addAttribute("notAuthNets",fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_INTEREST_CALCBYBATCH_CHECK,getUserId()));
        return doDispatchPage("batchInterestPool");
    }
    /**
     * 获取资金池
     *
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools(String opType) {
        String resource = "";
        if (EmptyUtils.isEmpty(opType) || "initList".equals(opType)) {
            resource = FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_HAND;
        } else {
            resource = FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_CHECK;
        }
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }
    /**
     * <b>功能介绍：</b>冲预提-经办<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listBatchRush")
    public String listBatchRush(Model model, String opType, String netId, String accType) {
        if (EmptyUtils.isEmpty(opType)) {
            opType = "initList";
        }
        initBatchInterest(model, opType, netId, accType, "batchRush", "runsh_hand");
        model.addAttribute("notAuthNets", fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_INTEREST_RUNSH_HAND, getUserId()));
        return doDispatchPage("batchInRunsh");
    }
    /**
     * <b>功能介绍：</b>冲预提-复核<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/checkBatchRush")
    public String checkBatchRush(Model model, String opType, String netId, String accType) {
        initBatchInterest(model, "checkList", netId, accType, "batchRush", "runsh_check");
        model.addAttribute("notAuthNets", fcInterestService.getNotAuthNets(FinacialResourceConstant.FINACIAL_INTEREST_RUNSH_CHECK, getUserId()));
        return doDispatchPage("batchInRunsh");
    }
    /**
     * <b>功能介绍:</b>利息清单页<br>
     * <b>日期：</b>2017年6月30日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param model
     * @param netId     网点ID
     * @param accType   01-企业活期 02-银行活期 03-放款[内部贷款] 04-银行借款 05-企业定期 08-通知存款
     * @param opType    操作类型 initList-经办
     * @param curr
     * @param poolSign  资金池标识 0-非资金池；1-资金池
     * @param account 内部账户
     * @return
     * @version 1.0
     */
    @GetMapping("/interestBill")
    public String listinterestBill(Model model, String netId, String accType, String opType, int curr, String poolSign, String account,String generateMode) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        String userid = getUserId();
        accType = (accType == null ? "" : accType);
        Map<String, Object> result = fcInterestService.queryCanEditInterestDtl(curr, netId, accType, opType, userid, poolFlag, account,generateMode);
        result.put("opType", opType);
        result.put("pageType", "interest");
        result.put("netId", netId);
        if (poolFlag) {
            result.put("netName", fcCorpPoolService.getById(netId).getPoolName());//网点名称
        } else {
            result.put("netName", corpService.get(netId).getName());//网点名称
        }
        result.put("accType", accType);
        result.put("currentPage", 1);
        result.put("method", "interestBill");
        result.put("interestRateType", "Y".equals(fcInterestService.getSysInstRateType()) ? "%" : "‰");
        result.put("poolSign", poolSign);
        model.addAttribute("model", result);
        return "finacial/interest/interestBill";
    }
    /**
     * <br>
     * <b>功能：</b> 取 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/26 0026 <br>
     */
    @PostMapping("/istBillAccType")
    @ResponseBody
    public Object istBillAccType(Model model, String netId, String accType, String opType, int curr, String poolSign) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        String userid = getUserId();
        accType = (accType == null ? "" : accType);
        List<Map<String, Object>> result = fcInterestService.queryInterestAccType(curr, netId, accType, opType, userid, poolFlag);
        return result;
    }
    /**
     * <b>功能介绍:</b>利息清单页<br>
     * <b>日期：</b>2017年6月30日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/interestBillQuery")
    @ResponseBody
    public Object interestBillQuery(Model model, String netId, String accType, String opType, int curr, String poolSign, String interestSign, String curId) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        String userid = getUserId();
        accType = (accType == null ? "" : accType);
        Map<String, Object> result = fcInterestService.queryInterestDtlByAccType(netId, accType, opType, curr, poolFlag, interestSign, curId, getCurrentUser());
        result.put("opType", opType);
        result.put("pageType", "interest");
        result.put("netId", netId);
        result.put("netName", corpService.get(netId).getName());//网点名称
        result.put("accType", accType);
        result.put("currentPage", 1);
        result.put("method", "interestBill");
        result.put("interestRateType", "Y".equals(fcInterestService.getSysInstRateType()) ? "%" : "‰");
        result.put("poolSign", poolSign);
        return result;
    }
    /**
     * <b>功能介绍：</b>冲预提利息清单页<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param model
     * @param netId
     * @param accType
     * @param opType
     * @param tallyDate
     * @param endInstDate
     * @param curr
     * @return
     * @version 1.0
     */
    @GetMapping("/interestRushBill")
    public String listinterestRushBill(Model model, String netId, String accType, String opType, String tallyDate, String endInstDate, String account, int curr) {
        InterestSearchBean searchBean = new InterestSearchBean();
        searchBean.setAccType(accType == null ? "" : accType);
        log.info("endInstDate:" + endInstDate);
        searchBean.setBlnInstDate(DateUtils.str2Date(endInstDate, DateUtils.date_sdf()));
        searchBean.setNetId(netId);
        searchBean.setTallyDate(DateUtils.str2Date(tallyDate, DateUtils.date_sdf()));
        searchBean.setUserId(getUserId());
        Map<String, Object> result = fcInterestService.queryRushFcInterestDtls(curr, searchBean, account, opType);
        result.put("opType", opType);
        result.put("pageType", "rush");
        result.put("netId", netId);
        result.put("accType", accType);
        result.put("currentPage", 1);
        result.put("method", "interestRushBill");
        result.put("tallyDate", tallyDate);
        result.put("endInstDate", endInstDate);
        result.put("interestRateType", "Y".equals(fcInterestService.getSysInstRateType()) ? "%" : "‰");
        model.addAttribute("model", result);
        return "finacial/interest/interestBill";
    }
    /**
     * <b>功能介绍:跳转计息凭证页</b><br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param model
     * @param netId     网点ID
     * @param accType   01-企业活期 02-银行活期 03-放款[内部贷款] 04-银行借款 05-企业定期 08-通知存款
     * @param opType    操作类型 initList-经办
     * @param curr
     * @param poolSign  资金池标识 0-非资金池；1-资金池
     * @param account 内部账户
     * @return
     * @version 1.0
     */
    @GetMapping("/toLoadInterestVoucher")
    public String listLoadInterestVoucher(Model model, String netId, String accType, String opType, int curr,
                                          String poolSign, String account,String generateMode, Integer affixAmount) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        Map<String, Object> result = fcInterestService.createCanEditInterestVoucher(curr, netId, accType, opType, getCurrentUser(),
                poolFlag, account,generateMode, affixAmount);
        if (EmptyUtils.isEmpty(result.get("interestTot")) || Integer.valueOf(String.valueOf(result.get("interestTot"))) <= 1) {
            result.put("interestTot", 0);
        }
        result.put("curr", curr);
        result.put("netId", netId);
        result.put("accType", accType);
        result.put("opType", opType);
        result.put("pageType", "interest");
        result.put("poolSign", poolSign);
        model.addAttribute("model", result);
        return "finacial/interest/interestVoucher";
    }
    /**
     * <b>功能介绍:跳转冲预提凭证页</b><br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/toLoadInterestRushVoucher")
    public String toLoadInterestRushVoucher(Model model, String netId, String accType, String opType, String tallyDate, String endInstDate, String account, int curr) {
        InterestSearchBean searchBean = new InterestSearchBean();
        searchBean.setAccType(accType);
        log.debug(endInstDate);
        searchBean.setBlnInstDate(DateUtils.str2Date(endInstDate, DateUtils.date_sdf()));
        searchBean.setNetId(netId);
        searchBean.setTallyDate(DateUtils.str2Date(tallyDate, DateUtils.date_sdf()));
        searchBean.setUserId(getUserId());
        Map<String, Object> result = fcInterestService.createCanInterestRushVoucher(curr, searchBean, account, opType, getCurrentUser());
        result.put("curr", curr);
        result.put("netId", netId);
        result.put("accType", accType);
        result.put("opType", opType);
        result.put("tallyDate", tallyDate);
        result.put("endInstDate", endInstDate);
        result.put("pageType", "rush");
        model.addAttribute("model", result);
        return "finacial/interest/interestVoucher";
    }
    /**
     * <b>功能介绍:跳转积数清单</b><br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param model
     * @param netId     网点ID
     * @param accType   01-企业活期 02-银行活期 03-放款[内部贷款] 04-银行借款 05-企业定期 08-通知存款
     * @param opType    操作类型 initList-经办
     * @param curr
     * @param account 内部账户
     * @return
     * @version 1.0
     */
    @GetMapping("/accuBill")
    public String listaccuBill(Model model, String netId, String accType, String opType, int curr, String account,String generateMode) {
        model.addAttribute("opType", opType);
        model.addAttribute("netId", netId);
        model.addAttribute("accType", accType);
        model.addAttribute("account", account);
        model.addAttribute("generateMode", generateMode);
        return "finacial/interest/accuBill";
    }
    /**
     * <b>功能介绍：</b>跳转预提清单<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/predrawBill")
    public String listpredrawBill(Model model, String netId, String accType, String opType, String endInstDate, String account) {
        model.addAttribute("opType", opType);
        model.addAttribute("netId", netId);
        model.addAttribute("accType", accType);
        model.addAttribute("endInstDate", endInstDate);
        model.addAttribute("account", account);
        return "finacial/interest/predrawBill";
    }
    /**
     * <b>功能介绍:跳转不足款清单</b><br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/lessPayBill")
    public String listlessPayBill(Model model, String netId, String accType, String opType, int curr) {
        model.addAttribute("netId", netId);
        model.addAttribute("accType", accType);
        return "finacial/interest/lessPayBill";
    }
    /**
     * <b>功能介绍：</b>凭证打印跳转<br>
     * <b>日期：</b>2017年11月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param model
     * @param opType
     * @param netCode
     * @param accTypeCode
     * @return
     * @version 1.0
     */
    @GetMapping("/doBatchPrint")
    public String doBatchPrint(Model model, String opType, String netCode, String accTypeCode, String poolSign,
                               String account,String generateMode, Integer affixAmount) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        Map<String, Object> result = fcInterestService
                .createCanEditInterestVoucher(1, netCode, accTypeCode, opType, getCurrentUser(), poolFlag, account, generateMode, affixAmount);
        int sum = 0;
        if (!EmptyUtils.isEmpty(result.get("interestTot"))) {
            sum = (int) result.get("interestTot");
        }
        List<Map<String, Object>> voucherInfos = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= sum; i++) {
            Map<String, Object> res = fcInterestService
                    .createCanEditInterestVoucher(i, netCode, accTypeCode, opType, getCurrentUser(), poolFlag, account, generateMode, affixAmount);
            voucherInfos.add(res);
        }
        model.addAttribute("voucherInfos", voucherInfos);
        model.addAttribute("sum", sum);
        return "finacial/interest/voucherPrintTemplate";
    }
    /**
     * <b>功能介绍：</b>打印利息通知单跳转<br>
     * <b>日期：</b>2017年11月23日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param model
     * @param opType
     * @param netCode
     * @param accType
     * @param tallyDate
     * @param blnInstDate
     * @return
     * @version 1.0
     */
    @GetMapping("/doPrintRateNotice")
    public String doPrintRateNotice(Model model, String opType, String netCode, String accType, String tallyDate, String blnInstDate,String account,String generateMode) {
        List<InterestRateNoticeVO> rateNotices = fcInterestService.getRateNotice(opType, netCode, accType, tallyDate, blnInstDate, getUserId(),account,generateMode);
        model.addAttribute("rateNotices", rateNotices);
        model.addAttribute("noticesSize", rateNotices.size());
        return "finacial/interest/rateNoticePrint";
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
        Map<String, Object> result = fcInterestService.queryCanEditAccuBalance(dataTablesInput, netId, accType, opType, getUserId(),account,generateMode);
        List result1 = (List) result.get("rows");
        result1.addAll((List) result.get("footer"));
        out.setRecordsTotal(Long.valueOf((Integer) result.get("rowsCount")));
        out.setData(result1);
        return new DataTablesOutput(dataTablesInput.getDraw(), result1, (Integer) result.get("rowsCount"), (Integer) result.get("rowsCount"));
    }
    /**
     * <b>功能介绍：</b>加载预提清单<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/doPreviewRush")
    @ResponseBody
    public DataTablesOutput doPreviewRush(@RequestBody DataTablesInput dataTablesInput) {
        Map<String, Object> search = dataTablesInput.getFormdata();
        String netId = (String) (search.get("netId") == null ? "" : search.get("netId"));
        String opType = (String) (search.get("opType") == null ? "" : search.get("opType"));
        String accType = (String) (search.get("accType") == null ? "" : search.get("accType"));
        String account = (String) (search.get("account") == null ? "" : search.get("account"));
        String endInstDate = (String) (search.get("endInstDate") == null ? "" : search.get("endInstDate"));
        List<Map<String, Object>> unRushPredraws = fcInterestService.queryCanRushPredrawDtls(dataTablesInput, netId, accType,
                DateUtils.str2Date(endInstDate, DateUtils.date_sdf()), account, opType, getUserId());

        return new DataTablesOutput(dataTablesInput.getDraw(), unRushPredraws, dataTablesInput.getStart()+unRushPredraws.size(), dataTablesInput.getStart()+unRushPredraws.size());
    }
    /**
     * <b>功能介绍：</b>查询不足款清单<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/toLoadLessPayBill")
    @ResponseBody
    public DataTablesOutput toLoadLessPayBill(@RequestBody DataTablesInput dataTablesInput) {
        Map<String, Object> search = dataTablesInput.getFormdata();
        String netId = (String) (search.get("netId") == null ? "" : search.get("netId"));
        String opType = (String) (search.get("opType") == null ? "" : search.get("opType"));
        dataTablesInput.setFormdata(null);
        List<FcInterestVO> result = fcInterestService.createCanEditLessPay(dataTablesInput, netId, opType, getUserId());
        return new DataTablesOutput(dataTablesInput.getDraw(), result, result.size(), result.size());
    }
    public String doDispatchPage(String pageName) {
        if ("batchInterest".equals(pageName) || "caBatchInterest".equals(pageName) || "caUnInterest".equals(pageName)) { //批量计息页面
//			initBatchInterest(request, pageName);
        } else if ("batchUnInterest".equals(pageName)) { //批量反计息页面
//            initBatchUnInterest(request);
        } else if ("interestCalc".equals(pageName)) { //利息测算
//            initInterestCalc(request);
        } else if ("batchRush".equals(pageName)) { //批量冲预提
//            initBatchInterest(request, pageName);
        }
        return "finacial/interest/" + pageName;
    }
    /**
     * <b>功能介绍：</b>初始化批量计息列表页面<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private void initBatchInterest(Model model, String opType, String netId, String accType, String pageName, String resouceType) {
        model.addAttribute("opType", opType);
        Map<String, Object> dataInfo = new HashMap<String, Object>();
        if (EmptyUtils.isEmpty(netId)) {
            if (InterestConstant.INTEREST_TYPE_CALC.equals(resouceType)) {//批量计息-经办
                netId = fcInterestService.getInitNetId(FinacialResourceConstant.FINACIAL_INTEREST_CALCBYBATCH_HAND, getUserId());
            } else if (InterestConstant.INTEREST_TYPE_CHECK.equals(resouceType)) {//批量计息-复核
                netId = fcInterestService.getInitNetId(FinacialResourceConstant.FINACIAL_INTEREST_CALCBYBATCH_CHECK, getUserId());
            } else if (InterestConstant.INTEREST_TYPE_RUSH_HAND.equals(resouceType)) {//冲预提-经办
                netId = fcInterestService.getInitNetId(FinacialResourceConstant.FINACIAL_INTEREST_RUNSH_HAND, getUserId());
            } else if (InterestConstant.INTEREST_TYPE_RUSH_CHECK.equals(resouceType)) {//冲预提-复核
                netId = fcInterestService.getInitNetId(FinacialResourceConstant.FINACIAL_INTEREST_RUNSH_CHECK, getUserId());
            } else if (InterestConstant.INTEREST_TYPE_POOL_CALC.equals(resouceType)) {//资金池批量记账-经办
                List<FcCorpPoolDTO> corpPoolDTOS = fcCorpPoolService.getUserCorpPools(getCurrentUser(),
                        FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_HAND);
                if (!EmptyUtils.isEmptyList(corpPoolDTOS)) {
                    netId = corpPoolDTOS.get(0).getId();//资金池ID
                } else {
                    throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOT_PERMISSION);//资源权限
                }
            } else if (InterestConstant.INTEREST_TYPE_POOL_CHECK.equals(resouceType)) {//资金池批量记账-复核
                List<FcCorpPoolDTO> corpPoolDTOS = fcCorpPoolService.getUserCorpPools(getCurrentUser(),
                        FinacialResourceConstant.FINACIAL_POOLINTEREST_CALCBYBATCH_CHECK);
                if (!EmptyUtils.isEmptyList(corpPoolDTOS)) {
                    netId = corpPoolDTOS.get(0).getId();//资金池ID
                } else {
                    throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOT_PERMISSION);//资源权限
                }
            }
        }
        Date closeDate = null;//日结日期
        if (InterestConstant.INTEREST_TYPE_POOL_CALC.equals(resouceType) ||
                InterestConstant.INTEREST_TYPE_POOL_CHECK.equals(resouceType)) {//资金池批量记账-经办
            closeDate = dayCloseService.getPoolDayCloseDate(netId);//资金池日结日期
            model.addAttribute("netName", fcCorpPoolService.getById(netId).getPoolName());
        } else {
            closeDate = dayCloseService.getDayCloseFinalDate(netId);//日结日期
            model.addAttribute("netName", corpService.get(netId).getName());
        }
        InterestSearchBean searchBean = new InterestSearchBean();
        searchBean.setNetId(netId);
        searchBean.setAccType(accType);
        //查询 利息明细临时表，拿到 BLN_INST_DATE, TALLY_DATE
        List<Object[]> accTypeInterestInfos = fcInterestService.queryCanEditAccTypeInterestInfo(netId, accType, getUserId(),
                opType, pageName);
        initBlnInstDateShow(accTypeInterestInfos, dataInfo);
        if (dataInfo.containsKey("blnInstDate") && dataInfo.get("blnInstDate") != null) {
            Date blnInstDate = (Date) dataInfo.get("blnInstDate");
            searchBean.setBlnInstDate(DateUtils.addDay(blnInstDate, 1));
        } else {
            searchBean.setBlnInstDate(DateUtils.addDay(closeDate, 1));
        }
        if (dataInfo.containsKey("tallyDate") && dataInfo.get("tallyDate") != null) {
            searchBean.setTallyDate((Date) dataInfo.get("tallyDate"));
        } else {
            searchBean.setTallyDate(DateUtils.addDay(closeDate, 1));
        }
        Map<String, Object> ConfigMap = configService.listByType("ist");
        Map<String, Object> istConfigMap = (Map<String, Object>) ConfigMap.get("ist");
        String batchInstWay = istConfigMap.get("batchInstWay").toString();
        //		判断是否控制结息日
        if ("0".equals(batchInstWay)) {//不控制结息日
//            closeDate=DateUtils.addDay(DateUtils.getDate(), -1);
            model.addAttribute("batchInstWay",batchInstWay);
        }

        model.addAttribute("closeDate", DateUtils.date2Str(closeDate, DateUtils.date_sdf()));
        model.addAttribute("searchBean", searchBean);
    }
    /**
     * <b>功能介绍：</b>初始化日期显示<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private void initBlnInstDateShow(List<Object[]> accTypeInterestInfos, Map<String, Object> dataInfo) {
        Date blnInstDate = null;
        Date tallyDate = null;
        boolean isShowBlnInstDate = true;
        Long compareBlnInstDateTime = null;
        if (accTypeInterestInfos == null || accTypeInterestInfos.size() == 0) {
            return;
        }
        for (Object[] accTypeInterestInfo : accTypeInterestInfos) {
            Long blnInstDateTime = ((Date) accTypeInterestInfo[1]).getTime();
            if (compareBlnInstDateTime == null) {
                compareBlnInstDateTime = blnInstDateTime;
                blnInstDate = (Date) accTypeInterestInfo[1];
                tallyDate = (Date) accTypeInterestInfo[2];
                continue;
            }
            if (compareBlnInstDateTime.longValue() != blnInstDateTime.longValue()) {
                isShowBlnInstDate = false;
                break;
            }
        }
        if (!isShowBlnInstDate) {
            blnInstDate = null;
            tallyDate = null;
        }
        dataInfo.put("blnInstDate", blnInstDate);
        dataInfo.put("tallyDate", tallyDate);
    }
    /**
     * <b>功能介绍: </b>计息/提交<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping(params = "doInterestaccrualByBatch")
    @ResponseBody
    public AjaxJson doInterestaccrualByBatch(String opTypestr, String netCode, String accTypeCode, String tallyDate,
                                             String endInstDate, boolean IsHoldOn, String poolSign) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        AjaxJson json = new AjaxJson();
        OperateType opType = null;
        accTypeCode = accTypeCode == null ? "" : accTypeCode;
        if ("interest".equals(opTypestr)) {
            opType = OperateType.INTEREST;
        } else if ("submit".equals(opTypestr)) {
            opType = OperateType.SUBMIT;
        }
        try {
            if (accTypeCode.startsWith(",")) {
                accTypeCode = accTypeCode.substring(accTypeCode.indexOf(",") + 1);
            }
            fcInterestService.interestaccrualByBatch(netCode, accTypeCode, DateUtils.str2Date(tallyDate, DateUtils.date_sdf()),
                    DateUtils.str2Date(endInstDate, DateUtils.date_sdf()),
                    getCurrentUser(), opType, IsHoldOn, poolFlag, null,null, null);
            json.setSuccess(true);
            if (opType == OperateType.INTEREST) {
                String itsSuccess = multiLangServiceI.getLang("finacial.interest.interest.success");
                json.setObj(itsSuccess);
            } else if (opType == OperateType.SUBMIT) {
                String itsSuccess = multiLangServiceI.getLang("common.message.submit.success");
                json.setObj(itsSuccess);
            }
        } catch (Exception e) {
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                if (InterestMessageEnum.FIN_INTEREST_NOTENOUGH_BALANCE.equals(new_name.getIMessageEnum())) {
                    json.setObj("notBalance");
                }
            }
            e.printStackTrace();
        }
        return json;
    }
    @RequestMapping(path = "/doInterestaccrualByBatch")
    @ResponseBody
    public AjaxJson doInterestaccrualByBatch1(String opTypestr, String netCode, String accTypeCode, String tallyDate,
                                             String endInstDate, boolean IsHoldOn, String poolSign) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        AjaxJson json = new AjaxJson();
        OperateType opType = null;
        accTypeCode = accTypeCode == null ? "" : accTypeCode;
        if ("interest".equals(opTypestr)) {
            opType = OperateType.INTEREST;
        } else if ("submit".equals(opTypestr)) {
            opType = OperateType.SUBMIT;
        }
        try {
            if (accTypeCode.startsWith(",")) {
                accTypeCode = accTypeCode.substring(accTypeCode.indexOf(",") + 1);
            }
            fcInterestService.interestaccrualByBatch(netCode, accTypeCode, DateUtils.str2Date(tallyDate, DateUtils.date_sdf()),
                    DateUtils.str2Date(endInstDate, DateUtils.date_sdf()),
                    getCurrentUser(), opType, IsHoldOn, poolFlag, null,null, null);
            json.setSuccess(true);
            if (opType == OperateType.INTEREST) {
                String itsSuccess = multiLangServiceI.getLang("finacial.interest.interest.success");
                json.setObj(itsSuccess);
            } else if (opType == OperateType.SUBMIT) {
                String itsSuccess = multiLangServiceI.getLang("common.message.submit.success");
                json.setObj(itsSuccess);
            }
        } catch (Exception e) {
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                if (InterestMessageEnum.FIN_INTEREST_NOTENOUGH_BALANCE.equals(new_name.getIMessageEnum())) {
                    json.setObj("notBalance");
                }
            }
            e.printStackTrace();
        }
        return json;
    }

    /**
     * <b>功能介绍: </b>资金池计息/提交<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping(params = "doAccInterestaccrual")
    @ResponseBody
    public AjaxJson doAccInterestaccrual(String opTypestr, String netCode, String accTypeCode, String tallyDate,
                                         String endInstDate, String account) {
        AjaxJson json = new AjaxJson();
        OperateType opType = null;
        accTypeCode = accTypeCode == null ? "" : accTypeCode;
        if ("interest".equals(opTypestr)) {
            opType = OperateType.INTEREST;
        } else if ("submit".equals(opTypestr)) {
            opType = OperateType.SUBMIT;
        }
        try {
            if (accTypeCode.startsWith(",")) {
                accTypeCode = accTypeCode.substring(accTypeCode.indexOf(",") + 1);
            }
            fcInterestService.istPoolAccrualByBatch(netCode, accTypeCode, DateUtils.str2Date(tallyDate, DateUtils.date_sdf()),
                    DateUtils.str2Date(endInstDate, DateUtils.date_sdf()), getCurrentUser(), opType, account);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setSuccess(false);
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
                if (InterestMessageEnum.FIN_INTEREST_NOTENOUGH_BALANCE.equals(new_name.getIMessageEnum())) {
                    json.setObj("notBalance");
                }
            }
            e.printStackTrace();
        }
        return json;
    }
    /**
     * <b>功能介绍：</b>利息管理 批量计息复核/打回<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     * @return
     * @version 1.0
     */
    @RequestMapping(params = "doProcessInterestCheck")
    @ResponseBody
    public AjaxJson doProcessInterestCheck(String opTypestr, String netCode, String accTypeCode,
                                           String tallyDate, String endInstDate, String account, String poolSign, Integer affixAmount) {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        AjaxJson json = new AjaxJson();
        OperateType opType = null;
        if ("check".equals(opTypestr)) {
            opType = OperateType.CHECK;
        } else if ("back".equals(opTypestr)) {
            opType = OperateType.BACK;
        }
        AccInterestBean accInterestBean = new AccInterestBean(netCode, accTypeCode, DateUtils.str2Date(tallyDate, DateUtils.date_sdf()),
                DateUtils.str2Date(endInstDate, DateUtils.date_sdf()), getCurrentUser(), opType, account, poolFlag,
                InterestConstant.GENERATEMODE_INTEREST_BATCH, null, null);
        fcInterestService.processInterestCheck(accInterestBean);
        json.setSuccess(true);
        return json;
    }
    /**
     * <b>功能介绍：</b>点击：冲预提、 提交冲预提<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping(params = "doSubmitRush")
    @ResponseBody
    public AjaxJson doSubmitRush(String opType, String netId, String accType, String tallyDate, String endInstDate, String account, String rushMoney) {
        AjaxJson json = new AjaxJson();
        BigDecimal rushMoney_ = EmptyUtils.isEmpty(rushMoney) ? null : new BigDecimal(rushMoney.replaceAll(",", ""));
        try {
            String userId = UserContextHelper.helper().getUserId();
            InterestCommonBean bean = new InterestCommonBean(netId, accType, DateUtils.str2Date(tallyDate, DateUtils.date_sdf()),
                    DateUtils.str2Date(endInstDate, DateUtils.date_sdf()), account, userId);

            fcInterestService.submitRushPredraw(bean, rushMoney_, opType);
            json.setSuccess(true);
        } catch (Exception e) {
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                json.setMessageEnum(new_name.getIMessageEnum());
                json.setParams(new_name.getParams());
            }
            e.printStackTrace();
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * <b>功能介绍：</b>利息删除<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/doDeleteByBatch")
    @ResponseBody
    public AjaxJson doDeleteByBatch(String opTypestr, String netCode, String accTypeCode) {
        AjaxJson json = new AjaxJson();
        try {
            if (StringUtils.isNotEmpty(accTypeCode) && accTypeCode.startsWith(",")) {
                accTypeCode = accTypeCode.substring(accTypeCode.indexOf(",") + 1);
            }
            if(InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(opTypestr)){
                fcInterestService.doDeleteByBatch(netCode, accTypeCode, getUserId(),null,InterestConstant.GENERATEMODE_INTEREST_BATCH);
            }else {
                fcInterestService.doDeleteByBatch(netCode, accTypeCode, getUserId(),null,InterestConstant.GENERATEMODE_INTEREST_PART);
            }
            json.setSuccess(true);
        } catch (Exception e) {
            json.setSuccess(false);
            e.printStackTrace();
        }
        return json;
    }
    /**
     * <b>功能介绍：</b>冲预提删除<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/doDeleteByRush")
    @ResponseBody
    public AjaxJson doDeleteByRush(String netId, String accType, String blnInstDate, String account) {
        AjaxJson json = new AjaxJson();
        InterestSearchBean searchBean = new InterestSearchBean();
        searchBean.setAccType(accType);
        searchBean.setBlnInstDate(DateUtils.str2Date(blnInstDate, DateUtils.date_sdf()));
        searchBean.setNetId(netId);
        searchBean.setUserId(getUserId());
        try {
            fcInterestService.deletePredrawRushDtls(searchBean, account);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setSuccess(false);
            e.printStackTrace();
        }
        return json;
    }
    /**
     * <b>功能介绍：</b>根据网点获取日结日期<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping(params = "getDayCloseDate")
    @ResponseBody
    public AjaxJson getDayCloseDate(String netCode) {
        AjaxJson json = new AjaxJson();
        String dayCloseDateStr = "";
        if (!StringUtils.isBlank(netCode)) {
            Date dayCloseDate = fcInterestService.getDayOverDate(netCode);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dayCloseDateStr = simpleDateFormat.format(dayCloseDate);
        }
        json.setObj(dayCloseDateStr);
        json.setSuccess(true);
        return json;
    }
    /**
     * <b>功能介绍：</b>获取待冲销预提的账户信息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/getPreRushAcc")
    @ResponseBody
    public List<Map<String, Object>> getPreRushAcc(String netId, String accType) {
        return fcInterestService.getPreRushAcc(netId, accType);
    }
    /**
     * <b>功能介绍：</b>利息清单导出<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param opType
     * @param netCode
     * @param accTypeCode
     * @param impType
     * @param request
     * @param response
     * @throws Exception
     * @version 1.0
     */
    @GetMapping("/doExportInterestBill")
    public void doExportInterestBill(String opType, String netCode, String accTypeCode, String impType, String poolSign, String account, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(poolSign);
        String filename = fcInterestService.getLangStr("table.interet.lxqd") + ".xls";//利息清单.xls
        if (isIE(request)) {//判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        HSSFWorkbook workbook = fcInterestService.doExportInterestBillXls(netCode, accTypeCode, opType, impType, getUserId(), poolFlag, account);
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
    /**
     * 获取资金池
     *
     * @return
     */
    @RequestMapping(params = "getPoolAccount")
    @ResponseBody
    public List<FcAccDTO> getPoolAccount(String netId, String accType) {
        return fcAccService.getNetAccByPoolSign(netId, accType, InnerAccountConstant.POOL_SIGN_1, InnerAccountConstant.INTERESTSIGN_YES);
    }
    /**
     * 获取资金池计息配置，判断是否是结息日
     */
    @PostMapping("/checkIstConfig")
    @ResponseBody
    public AjaxJson checkIstConfig(Date interestDate) {
        Map<String, Object> ConfigMap = configService.listByType("ist");
        Map<String, Object> istConfigMap = (Map<String, Object>) ConfigMap.get("ist");
        String batchInstWay = istConfigMap.get("batchInstWay").toString();
        String batchInstDay = istConfigMap.get("batchInstDay").toString();
        AjaxJson json = new AjaxJson();
//		判断是否结息日解析
        if ("1".equals(batchInstWay)) {
            if (DateUtils.getCalendar(interestDate.getTime()).get(Calendar.DAY_OF_MONTH) != Integer.valueOf(batchInstDay)) {
                //非结息日结息
                json.setSuccess(false);
            } else {
                json.setSuccess(true);
            }
        } else {
            json.setSuccess(true);
        }
        return json;
    }
    /**
     * 根据账户类型查询状态正常的内部账户
     *
     * @param accType 01-企业活期 02-银行活期 03-放款[内部贷款] 04-银行借款 05-企业定期 08-通知存款*#
     * @return
     */
    @PostMapping("/getInterestAccount")
    @ResponseBody
    public List<AccountDTO> getInterestAccount(String accType, String resource) {
        return fcInterestService.getInterestAccount(getCurrentUser(), accType, resource);
    }
}
