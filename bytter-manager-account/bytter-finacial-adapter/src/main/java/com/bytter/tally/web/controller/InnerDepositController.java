package com.bytter.tally.web.controller;

import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.tally.service.InnerDepositService;
import com.bytter.tally.vo.FcAccbookPageVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * <br>
 * <b>类功能：</b>内部存款手工记账<br>
 * <b>作者：</b>zhengxn<br>
 * <b>日期：</b> 2019年11月4日 <br>
 * <b>版权所有：<b>版权所有(C) 2019，拜特科技<br>
 */
@Controller
@RequestMapping("/finacial/bankCollect/innerDeposit")
public class InnerDepositController extends BaseController {
    @Autowired
	private InnerDepositService innerDepositService;
    @DubboReference(check = false)
	private IChoInnerFinanceService iChoInnerFinanceService;
    @DubboReference(check = false)
	private IAccebillService iAccebillService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
    @DubboReference(check = false)
	private ICorpService iCorpService;
    @DubboReference(check = false)
	private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
	private IFcAccService iFcAccService;
    //@RequiresPermissions(ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY)
    @LogMessage(content = "查询记账列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{menuType}/{type}")
    public String list(@PathVariable("menuType") String menuType, @PathVariable("type") String type, Model model) {
        model.addAttribute("opType", type);
        model.addAttribute("menuType", menuType);
        return "finacial/bankcollect/innerdeposit/listInnerDeposit";
    }
    @GetMapping("/manualPage")
    public String manualPage(String opType, String menuType, Model model) {
        model.addAttribute("opType", opType);
        model.addAttribute("menuType", menuType);
        if(FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType)){
            model.addAttribute("isCurrent", "1");
        }
        return "finacial/bankcollect/" + FinacialManualConstant.AccTypeMap.get(menuType);
    }
    
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input,String menuType,String opType){
        return innerDepositService.getDateTable(input,menuType,opType, getUserId());
    }
    /**
     * 构建记账分录
     */
    @PostMapping("/processAccbook")
    @ResponseBody
    public FcAccbookPageVO processAccbook(String id, String type , String menuType, String sysRaceCode){
        return innerDepositService.createAccbook(id, type, menuType, sysRaceCode,getCurrentUser());
    }
    /**
     * 提交FC_ACCBOOK
     */
    @LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String id, String menuType, String pageType, BigDecimal interest) {
        innerDepositService.submit(fcAccbook, id, menuType, getCurrentUser(),pageType,interest);
        return success();
    }
    @LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook,String opType,String menuType,String id) {
        innerDepositService.save( fcAccbook,getCurrentUser(),opType,menuType,id);
        return success();
    }
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String id,String menuType,String pageType) {
        innerDepositService.backFcAccbook(fcAccbook, id, getCurrentUser(),menuType,pageType);
        return success();
    }
    
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/pass")
    @ResponseBody
    public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String id,String menuType,String pageType,BigDecimal interest) {
        innerDepositService.createVoucher(fcAccbook, id, getCurrentUser(),menuType,pageType,interest);
        return success();
    }
    /**
     * 查询有权限的内部帐号
     */
    @PostMapping("/getFcAcc")
    @ResponseBody
    public List<FcAccDTO> getFcAcc(HttpServletRequest request, String netId, String corpId, String subjectCode, String curCode) {
        FcSubjectDTO subjectDTO = fcSubjectService.findSubjectById(subjectCode);
        List<FcAccDTO> list = null;
        if(SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.getValue().equals(subjectDTO.getSysSubjectCode()) ||
                SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_NOTICE.getValue().equals(subjectDTO.getSysSubjectCode()) ||
                SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.getValue().equals(subjectDTO.getSysSubjectCode()) ||
                SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_NOTICE.getValue().equals(subjectDTO.getSysSubjectCode())){ //521001,521003,201002,201003
            if(configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {//结算中心
                list = innerAccountService.listAccountByParamsAndGrant(curCode, netId, netId, subjectCode,null,getUserId());
            }else {
                list = innerAccountService.listAccountByParamsAndGrant(curCode, netId, corpId, subjectCode,null,getUserId());
            }
        }else{
            list = innerAccountService.listAccountByParamsAndGrant(curCode, netId, corpId, subjectCode,null,getUserId());
        }
        return EmptyUtils.isEmptyList(list)? new ArrayList<FcAccDTO>() : list;
    }
    /**
     * 跳转到开户页面
     *
     * @param
     * @param model
     * @return
     */
    @GetMapping("/createFcAcc")
    public String toRepayPlan(String billCode,String subjectCode,String menuType,String account,Model model) {
        ChoInnerFinanceDTO info = iChoInnerFinanceService.getInnerDepositByBillNo(billCode);
        if (StringUtils.isNotEmpty(info.getNoticeType())) {
            if (info.getNoticeType().equals("2")){
                info.setNoticeType("7");
            }
        }
        FcSubjectDTO subject = fcSubjectService.findSubjectById(subjectCode);
        // 兼容预置科目账户类型的尾部空格，仅规范化开户页面使用的 DTO。
        if (subject != null && subject.getAccType() != null) {
            subject.setAccType(subject.getAccType().trim());
        }
        CorpDTO net = iCorpService.getNetByCorpId(info.getCorpId());
        // info.setCorpName(net.getName());
        Date closeDay = iFcDayCloseService.getDayCloseFinalDate(net.getNetId());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(closeDay);
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动 
        Date closeDate = calendar.getTime(); //这个时间就是日期往后推一天的结果 
        model.addAttribute("closeDate", DateUtils.format(closeDate,"yyyy-MM-dd"));
        model.addAttribute("net", net);
        model.addAttribute("info", info);
        model.addAttribute("subject", subject);
        model.addAttribute("menuType",menuType);
        if(EmptyUtils.isNotEmpty(account)){
            model.addAttribute("relationAcc",account);
        }
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
        model.addAttribute("seqNumPage", seqGeneratingMode);
        int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
        model.addAttribute("accAccNoRule", accAccNoRule);
        model.addAttribute("curInfo", iBtCurrencyService.getObjectById(info.getCurId()));
        //内部账号生成法则
        String reVal = iFcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        model.addAttribute("relen", relen);
        return "finacial/bankcollect/innerdeposit/addDepositFcAcc";
    }
}
