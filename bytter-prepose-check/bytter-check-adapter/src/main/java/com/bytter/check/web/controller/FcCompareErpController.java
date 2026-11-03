package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.entity.FcCheckInit;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.service.FcBankBillService;
import com.bytter.check.service.FcCheckInitService;
import com.bytter.check.service.FcCompareCenterService;
import com.bytter.check.service.FcCompareErpService;
import com.bytter.check.vo.FcBankCheckSearchVo;
import com.bytter.check.vo.FcBankErpBalInfoVO;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.check.vo.FcCompareErpSearchVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FC_COMPARE_ACC ERP对账
 * @date 2018-11-8
 */
@Controller
@RequestMapping("/finacial/check/fccompareerp")
public class FcCompareErpController extends BaseController {
    @Autowired
    private FcCompareErpService fcCompareErpService;
    @Autowired
    private FcBankBillService fcBankBillService;
    @Autowired
    private FcCompareCenterService fcCompareCenterService;
    @Autowired
    private FcCheckInitService fcCheckInitService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IInputBankInfoService inputBankInfoService;
    @DubboReference(check = false)
	private IBtBankAccService bankAccService;
    /**
     * FC_COMPARE_ACC列表 页面跳转
     * ---------------------------ok
     *
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK)
    @LogMessage(content = "查询FC_COMPARE_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(FcBankCheckSearchVo checkVO, Model model) {
        //BtBankAccDTO bankAccDTO = bankAccService.getBankAccByBankAccAndCurId(checkVO.getBankAcc(), checkVO.getCurId());
        if (EmptyUtils.isNotEmpty(checkVO.getBankAcc())) {
            FcCheckInit fcCheckInit = fcCheckInitService.getCheckInitByAccount(checkVO.getBankAcc(), checkVO.getAccount(), FinacialConstant.CHECK_TYPE_ERP, checkVO.getCurId());
            Date carryDate = fcCheckInit.getCarryDate();
            if (EmptyUtils.isEmpty(carryDate)) {
                carryDate = fcCheckInit.getCheckDate();
            }
            BtBankAccDTO bankAccDTO = bankAccService.getBankAccInfo(checkVO.getBankAcc());
            checkVO.setCorpName(bankAccDTO.getCorpName());
            checkVO.setBankName(bankAccDTO.getBankName());
            model.addAttribute("checkVO", checkVO);//初始化网点
            model.addAttribute("carryDate", DateUtils.format(DateUtils.addDay(carryDate, 1), "yyyy-MM-dd"));//最小对账结束日期
            return "check/fcCompareErp/addFcCompareErp";
        } else {
            String initNetId = fcCompareErpService.getInitNetId(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK, getUserId());
            model.addAttribute("opType", "init");
            model.addAttribute("initNetId", initNetId);//初始化网点
            model.addAttribute("notAuthNets", fcCompareCenterService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK, getUserId()));//初始化网点
            //model.addAttribute("initNetOverDate",fcCompareErpService.getDayOverByNetId(initNetId));//初始化网点日结日期 ---- 结束日期
            return "check/fcCompareErp/listFcCompareErp";
        }
    }
    /**
     * FC_COMPARE_ACC列表 页面跳转
     * ---------------------------ok
     *
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK)
    @LogMessage(content = "查询FC_COMPARE_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addCorpCompareErp")
    public String addCorpCompareErp(FcBankCheckSearchVo checkVO, Model model) {
        FcCheckInit fcCheckInit = fcCheckInitService.getCheckInitByAccount(checkVO.getBankAcc(), checkVO.getAccount(), FinacialConstant.CHECK_TYPE_ERP, checkVO.getCurId());
        Date carryDate = fcCheckInit.getCarryDate();
        if (EmptyUtils.isEmpty(carryDate)) {
            carryDate = fcCheckInit.getCheckDate();
        }
        BtBankAccDTO bankAccDTO = bankAccService.getBankAccInfo(checkVO.getBankAcc());
        checkVO.setCorpName(bankAccDTO.getCorpName());
        checkVO.setBankName(bankAccDTO.getBankName());
        model.addAttribute("checkVO", checkVO);//初始化网点
        model.addAttribute("carryDate", DateUtils.format(DateUtils.addDay(carryDate, 1), "yyyy-MM-dd"));//最小对账结束日期
        return "check/fcCompareErp/addFcCompareErp";
    }
    /**
     * FC_COMPARE_ACC table列表
     *
     * @return
     */
    ////@RequiresPermissions("finacial.check:fccompareacc:index")
  /*  @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcCompareErpService.getDataTables(dataTablesInput);
    }*/
    /**
     * <b>功能介绍:获取未指定格式的银行帐号
     * -------------------------------ok
     * </b><br>
     */
    @PostMapping("/getBankAccByOpType")
    @ResponseBody
    public List<FcCompareAccVO> getBankAccByOpType(String bankAcc, String netCode, String natureCode) {
        return fcCompareErpService.getBankAccByOpType(bankAcc, netCode, natureCode, getUserId());
    }
    /**
     * 功能介绍:获取银行对账数据<br>
     */
    public List<Map<String, Object>> queryErpBankCheckDatas(FcCompareErpSearchVO fcCompareErpSearchVO, Date transDate, String[] compareSigns) {
        return fcBankBillService.queryErpBankCheckDatas(fcCompareErpSearchVO, transDate, compareSigns);
    }
    /**
     * 功能介绍:获取erp对账数据<br>
     */
    public List<Map<String, Object>> queryErpCheckDatas(FcCompareErpSearchVO fcCompareErpSearchVO,Date transDate, String[] compareSigns) {
        return fcCompareErpService.queryErpCheckDatas(fcCompareErpSearchVO,transDate,compareSigns);
    }
    /**
     * <b>功能介绍:获取对账数据</b><br>
     * -------------------------------------------------------------------------------
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/toLoadCheckDatas")
    @ResponseBody
    public List<Map<String, Object>> toLoadCheckDatas(@RequestBody FcCompareErpSearchVO fcCompareErpSearchVO) {
        if (EmptyUtils.isEmpty(fcCompareErpSearchVO.getBankAcc())) {
            return new ArrayList();
        }
        if (EmptyUtils.isEmpty(fcCompareErpSearchVO.getAccount())) {
            return new ArrayList();
        }
        if (EmptyUtils.isEmpty(fcCompareErpSearchVO.getCheckFlag()) || "0".equals(fcCompareErpSearchVO.getCheckFlag())) {
            return new ArrayList();
        }
        String[] compareSigns = new String[4];
        if (FinacialConstant.VALID_TRUE.equals(fcCompareErpSearchVO.getNoHandRadios())) {//未勾兑
            compareSigns[0] = FinacialConstant.COMPARE_SIGN_NO;
        }
        if (FinacialConstant.VALID_TRUE.equals(fcCompareErpSearchVO.getStayRadios())) {//未达
            compareSigns[1] = FinacialConstant.COMPARE_SIGN_STAY;
        }
        if (FinacialConstant.VALID_TRUE.equals(fcCompareErpSearchVO.getHandRadios())) {//手动勾兑
            compareSigns[2] = FinacialConstant.COMPARE_SIGN_HAND;
        }
        if (FinacialConstant.VALID_TRUE.equals(fcCompareErpSearchVO.getCheckRadios())) {//已对账
            compareSigns[3] = FinacialConstant.COMPARE_SIGN_YES;
        }
//        logger.info("transDate= "+fcCompareErpSearchVO.getTransDate()+" ;");
        Date transDate_s = DateUtils.str2Date(fcCompareErpSearchVO.getTransDate(), DateUtils.date_sdf());
        if ("1".equals(fcCompareErpSearchVO.getQueryType())) {//erp对账数据
            return queryErpCheckDatas(fcCompareErpSearchVO,transDate_s, compareSigns);
        } else {//银行对账数据
            return queryErpBankCheckDatas(fcCompareErpSearchVO,transDate_s, compareSigns);
        }
    }
    /**
     * <b>功能介绍:</b>获取调节平衡余额<br>
     * -----------------------------------------------------------------------------------------
     */
    @RequestMapping(params = "toLoadAdjustBalance")
    @ResponseBody
    public AjaxJson toLoadAdjustBalance(String account, String bankAcc, String checkDate, String transDate, String paramType, String blnBalance, String bankBalance, String curId) {
        AjaxJson json = new AjaxJson();
        Map<String, Object> model = new HashMap<String, Object>();
        if (StringUtils.isBlank(bankAcc)) {
            model.put("paramType", paramType);
            json.setObj(model);
            return json;
        }
        Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
        BigDecimal blnBalance_s = BigDecimal.valueOf(Double.valueOf(blnBalance.trim()));//中心账面余额
        BigDecimal bankBalance_s = BigDecimal.valueOf(Double.valueOf(bankBalance.trim()));//银行账面余额
        model = fcCompareErpService.loadAdjustErpBalance(blnBalance_s, bankBalance_s, bankAcc, account, transDate_s, curId);
        model.put("transDateStr", transDate);
        model.put("paramType", paramType);
        json.setObj(model);
        json.setSuccess(true);
        return json;
    }
    /**
     * 功能介绍:获取账面余额<br>
     * --------------------------------------------------------------------------------
     */
    @RequestMapping(params = "getAccBalance")
    @ResponseBody
    public AjaxJson getAccBalance(String account, String bankAcc, String curId, String transDate, String paramType, String accDisplayType) {
        AjaxJson json = new AjaxJson();
        Map<String, Object> model = new HashMap<String, Object>();
        FcBankErpBalInfoVO balInfo;
        try {
            balInfo = fcCompareErpService.getAccBalInfo(account, bankAcc, curId, transDate, paramType, accDisplayType);
            model.put("blnBalance", balInfo.getAccBal());
            model.put("bankBalance", balInfo.getBankBal());
            model.put("beginDate", DateUtils.date2Str(DateUtils.addDay(balInfo.getLastOverDate(), 1), DateUtils.date_sdf()));
            model.put("lastOverDate", DateUtils.date2Str(balInfo.getLastOverDate(), DateUtils.date_sdf()));
            json.setObj(model);
            json.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ParameterException) {
                json.setMessageEnum(((ParameterException) e).getIMessageEnum());
                json.setParams(((ParameterException) e).getParams());
            }
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * 功能介绍:对账操作处理<br>
     * ---------------------------------------------------------------------------
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK)
    @PostMapping("/docheck")
    @ResponseBody
    public AjaxJson docheck(FcBankCheckSearchVo fca, Date checkDate) {
        AjaxJson json = new AjaxJson();
        try {
            fcCompareErpService.doCheck(fca, checkDate, getCurrentUser());
            json.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * <b>功能介绍:手工勾兑操作</b><br>
     * ------------------------------------------------------------------------------------
     */
    //只能做前端权限控制
    @RequestMapping(params = "doHandOver")
    @ResponseBody
    public AjaxJson doHandOver(String account, String bankAcc, String paramType, String checkDate, String transDate,
                               String opType, String ctrPk, String bankPk) {
        AjaxJson json = new AjaxJson();
        Date chekDate_s = DateUtils.str2Date(checkDate, DateUtils.date_sdf());
        Date transDate_s = DateUtils.str2Date(transDate, DateUtils.date_sdf());
        ctrPk = ctrPk == null ? "" : ctrPk;
        bankPk = bankPk == null ? "" : bankPk;
        fcCompareErpService.doHandOver(account, bankAcc, ctrPk, bankPk, chekDate_s, transDate_s, opType, paramType, getCurrentUser());
        json.setSuccess(true);
        return json;
    }
    /**
     * <b>功能介绍:结转操作</b><br>
     * -------------------------------------------------------------------------------------------------------
     */
    @RequestMapping(params = "doAdjustOverOther")
    @ResponseBody
    public AjaxJson doAdjustOverOther(FcBankCheckSearchVo fca, String checkDate) throws Exception {
        AjaxJson json = new AjaxJson();
        String adjustId = fcCompareErpService.doAdjustOverOther(fca, DateUtils.str2Date(checkDate, DateUtils.date_sdf()), getCurrentUser(), "1");
        json.setObj(adjustId);
        json.setSuccess(true);
        return json;
    }
    /**
     * <b>功能介绍：</b>根据网点获取日结日期<br>
     * <b>日期：</b>2017年9月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @return
     * @version 1.0
     */
    @RequestMapping(params = "getDayCloseDate")
    @ResponseBody
    public AjaxJson getDayCloseDate(String netId) {
        AjaxJson json = new AjaxJson();
        Date dayCloseDate = new Date();
        if (EmptyUtils.isNotEmpty(netId)) {
            dayCloseDate = fcCompareErpService.getDayOverByNetId(netId);
        }
        json.setObj(dayCloseDate);
        json.setSuccess(true);
        return json;
    }
    /**
     * 功能介绍:反结转查询 页面跳转<br>
     * <b>日期：</b>2017年6月2日<br>
     *
     * @version 1.0
     * @author caozx
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK)
/*    @LogMessage(content = "反结转查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listFcBankCheck")
    public String redoAdjustOverlist(Model model) {
        return "check/fcCompareErp/listFcBankCheck";
    }*/
    /**
     * 帐号对账查询列表页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/listFcBankCheck")
    public String listFcBankCheck(Model model) {
        model.addAttribute("pageContent", "bankCheck");
        return "check/fcCompareErp/listFcCheck";
    }
    /**
     * 单位对账查询列表页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/listFcCorpCheck")
    public String listFcCorpCheck(Model model) {
        model.addAttribute("pageContent", "corpCheck");
        return "check/fcCompareErp/listFcCorpCheck";
    }
    /**
     * <br>
     * <b>功能：</b> 查下已初始化对账余额的ERP科目对账情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/15 0015 <br>
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        String calcDateStr = (String) input.getFormdata().get("endDate");
        String compareSign = (String) input.getFormdata().get("eqs_compareSign");
        Date calcDate = null;
        if (EmptyUtils.isEmpty(calcDateStr)) {
            calcDate = DateUtils.getDate();
        } else {
            calcDate = DateUtils.parseDate(calcDateStr);
        }
        return fcCompareErpService.queryCheckList(input, getCurrentUser(), calcDate, compareSign);
    }
    /**
     * <br>
     * <b>功能：</b> 查下已初始化对账余额的ERP单位科目对账情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2022/9/22 <br>
     */
    @PostMapping("/dataTableCorp")
    @ResponseBody
    public DataTablesOutput dataTableCorp(@RequestBody DataTablesInput input) {
        String calcDateStr = (String) input.getFormdata().get("endDate");
        String compareSign = (String) input.getFormdata().get("eqs_compareSign");
        Date calcDate = null;
        if (EmptyUtils.isEmpty(calcDateStr)) {
            calcDate = DateUtils.getDate();
        } else {
            calcDate = DateUtils.parseDate(calcDateStr);
        }
        return fcCompareErpService.queryCorpCheckList(input, getCurrentUser(), calcDate, compareSign);
    }
    /**
     * 检查单位是否有银行账号所有权限
     * @return true   false
     */
    @LogMessage(content = "检查单位是否有银行账号所有权限", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/checkPermission")
    @ResponseBody
    public boolean checkPermission(String corpId){
        return fcCompareErpService.getPermissionByCorp(corpId,getCurrentUser());
    }
    /**
     * 一键对账
     * @param account 内部账号
     * @param bankAcc 银行账号
     * @param checkType 核对方式
     * @param curId 币别id
     * @param paramType 对账类型（0中心对账 1erp对账） 默认中心对账
     * @param transTime 结束日期
     * @param checkDate 对账日期
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK)
    @LogMessage(content = "一键对账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoCompare")
    @ResponseBody
    public AjaxJson autoCompare(String account,String bankAcc,String checkType,String curId,String paramType,Date transTime,String checkDate,String endDate) {
        AjaxJson json=new AjaxJson();
        FcBankCheckSearchVo fca =null;
        String accounts[] =account.split(CommonConstant.COMMA_SPLIT);
        String bankAccs[] =bankAcc.split(CommonConstant.COMMA_SPLIT);
        String curIds[] =curId.split(CommonConstant.COMMA_SPLIT);
        String checkDates[]  = checkDate.split(CommonConstant.COMMA_SPLIT);
        String endDates[]  = endDate.split(CommonConstant.COMMA_SPLIT);
        for (int i=0;i<accounts.length;i++) {
            fca = new FcBankCheckSearchVo();
            fca.setAccount(accounts[i]);
            fca.setBankAcc(bankAccs[i]);
            fca.setCurId(curIds[i]);
            fca.setCheckType(checkType);
            fca.setTransTime(transTime);
            fca.setParamType(paramType);
            try {
                Date lastOverDate = null;
                if(!EmptyUtils.isEmpty(endDates[i]) && !"null".equals(endDates[i])){
                    lastOverDate = DateUtils.str2Date(endDates[i], DateUtils.date_sdf());
                }else{
                    lastOverDate = DateUtils.str2Date(checkDates[i], DateUtils.date_sdf());
                }
                if (transTime.before(DateUtils.addDay(lastOverDate, 1))) {//结束日期应大于上次结转日期
                    //logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
                    throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE, DateUtils.date2Str(transTime, DateUtils.date_sdf()));
                }
                fcCompareErpService.doCheck(fca,lastOverDate,getCurrentUser());
                json.setSuccess(true);
            } catch (Exception e) {
                if(e instanceof ParameterException){
                    json.setMessageEnum(((ParameterException) e).getIMessageEnum());
                    json.setParams(((ParameterException) e).getParams());
                }
                json.setSuccess(false);
                break;
            }
        }
        return json;
    }
    /**
     * 一键结转列表，查询数据
     * @param model
     * @param fca
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_CHECK)
    @LogMessage(content = "一键结转列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/adjustList")
    public String adjustList(Model model,FcBankCheckSearchVo fca) {
        String initNetId = fcCompareCenterService.getInitNetId(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId());
        model.addAttribute("fcBankCheck",fca);
        model.addAttribute("opType","init");
        model.addAttribute("initNetId",initNetId);//初始化网点
        model.addAttribute("notAuthNets",fcCompareCenterService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK,getUserId()));//初始化网点
        model.addAttribute("initNetOverDate",fcCompareCenterService.getDayOverByNetId(initNetId));//初始化网点日结日期
        return "check/fcCompareErp/listFcCompareErpAdjust";
    }
    /**
     * 查询批量结转列表
     * @param input
     * @return
     */
    @PostMapping("/autoAdjustDataTable")
    @ResponseBody
    public DataTablesOutput autoAdjustDataTable(@RequestBody DataTablesInput input) {
        return fcCompareErpService.queryAdjustList(input, getCurrentUser(), null);
    }
    /**
     * 一键结转
     * @param account 内部账号
     * @param bankAcc 银行账号
     * @param checkType 核对方式
     * @param curId 币别id
     * @param paramType 对账类型（0中心对账 1erp对账） 默认中心对账
     * @param transTime 对账日期
     * @param lastEndDate 上次结转日期
     * @param blnBal  企业账面余额
     * @param bankBal 银行账面余额
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK)
    @LogMessage(content = "一键结转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoAdjust")
    @ResponseBody
    public AjaxJson autoAdjust(String account,String bankAcc,String checkType,String curId,String paramType,Date transTime,String lastEndDate,String checkDate,String  blnBal,String bankBal) {
        AjaxJson json=new AjaxJson();
        FcBankCheckSearchVo fca =null;
        //内部账户
        String accounts[] =account.split(CommonConstant.COMMA_SPLIT);
        //银行账户
        String bankAccs[] =bankAcc.split(CommonConstant.COMMA_SPLIT);
        //币别
        String curIds[] =curId.split(CommonConstant.COMMA_SPLIT);
        //企业账面余额
        String blnBalance[] =blnBal.split(CommonConstant.COMMA_SPLIT);
        //银行账面余额
        String bankBalance[] =bankBal.split(CommonConstant.COMMA_SPLIT);
        //上次结转日期
        String lastEndDates[] =lastEndDate.split(CommonConstant.COMMA_SPLIT);
        String checkDates[] = checkDate.split(CommonConstant.COMMA_SPLIT);
        Boolean success = true;
        for (int i=0;i<accounts.length;i++) {
            fca = new FcBankCheckSearchVo();
            fca.setAccount(accounts[i]);
            fca.setBankAcc(bankAccs[i]);
            fca.setCurId(curIds[i]);
            fca.setCheckType(checkType);
            fca.setTransTime(transTime);
            fca.setParamType(paramType);
            fca.setBlnBalance(NumberUtil.objToBigDecimal(blnBalance[i]));
            fca.setBankBalance(NumberUtil.objToBigDecimal(bankBalance[i]));
            try {
                Date lastOverDate = null;
                if(!EmptyUtils.isEmpty(lastEndDates[i]) && !"null".equals(lastEndDates[i])){
                    lastOverDate = DateUtils.str2Date(lastEndDates[i], DateUtils.date_sdf());
                }else{
                    lastOverDate = DateUtils.str2Date(checkDates[i], DateUtils.date_sdf());
                }
                if (transTime.before(DateUtils.addDay(lastOverDate, 1))) {//对账结束日期应大于上次结转日期
                    //logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
                    throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE,DateUtils.date2Str(transTime, DateUtils.date_sdf()));
                }
                fcCompareErpService.doAdjustOverOther(fca,lastOverDate,getCurrentUser(),null);
            } catch (Exception e) {
                //logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
                if(e instanceof ParameterException){
                    json.setMessageEnum(((ParameterException) e).getIMessageEnum());
                    json.setParams(((ParameterException) e).getParams());
                }
                success = false;
                break;
            }
        }
        json.setSuccess(success);
        return json;
    }
}
