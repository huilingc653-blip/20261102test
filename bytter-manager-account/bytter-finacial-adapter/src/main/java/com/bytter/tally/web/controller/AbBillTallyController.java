package com.bytter.tally.web.controller;

import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefineDTO;
import com.bytter.biz.finacial.api.dto.FcVoucherEntryDefineDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.service.AbBillTallyService;
import com.bytter.tally.vo.FcAccbookPageVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * <br>
 * <b>类功能：</b>承兑汇票手工记账<br>
 * <b>作者：</b>zhengxn<br>
 * <b>日期：</b> 2018年9月4日 <br>
 * <b>版权所有：<b>版权所有(C) 2018，拜特科技<br>
 */
@Controller
@RequestMapping("/finacial/bankCollect/abBillTallyController")
public class AbBillTallyController  extends BaseController {
    @Autowired
    private AbBillTallyService abBillTallyService;
    @DubboReference(check = false)
	private IAccebillService iAccebillService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    //@RequiresPermissions(ResourceConstant.FINACIAL_ABBILLTALLY_APPLY)
    @LogMessage(content = "查询记账列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{menuType}/{type}")
    public String list(@PathVariable("menuType") String menuType, @PathVariable("type") String type, Model model) {
        model.addAttribute("opType", type);
        model.addAttribute("menuType", menuType);
        return "finacial/bankcollect/abbilltally/listAbBillTally";
    }
    @GetMapping("/manualPage")
    public String manualPage(String opType, String menuType, Model model) {
        model.addAttribute("opType", opType);
        model.addAttribute("menuType", menuType);
        return "finacial/bankcollect/" + FinacialManualConstant.AccTypeMap.get(menuType);
    }
    
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput,String menuType,String opType){
        return abBillTallyService.getAbBillDateTable(dataTablesInput,menuType,opType,getCurrentUser());
    }
    /**
     * 构建记账分录
     */
    @PostMapping("/processAccbook")
    @ResponseBody
    public FcAccbookPageVO processAccbook(String id, String type , String menuType, String sysRaceCode){
        return abBillTallyService.createAccbook(id, type, menuType, sysRaceCode,getCurrentUser());
    }
    /**
     * 提交FC_ACCBOOK
     */
    @LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String id, String menuType,String pageType) {
        abBillTallyService.submit(fcAccbook, id, menuType, getCurrentUser(),pageType);
        return success();
    }
    @LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook,String opType,String menuType,String id) {
        abBillTallyService.save( fcAccbook,getCurrentUser(),opType,menuType,id);
        return success();
    }
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String id,String menuType,String pageType) {
        abBillTallyService.backFcAccbook(fcAccbook, id, getCurrentUser(),menuType,pageType);
        return success();
    }
    
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/pass")
    @ResponseBody
    public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String id,String menuType,String pageType) {
        abBillTallyService.createVoucher(fcAccbook, id, getCurrentUser(),menuType,pageType);
        return success();
    }
    /**
     * 查询有权限的内部帐号
     */
    @PostMapping("/getFcAcc")
    @ResponseBody
    public List<FcAccDTO> getFcAcc(HttpServletRequest request, String netId, String corpId, String sysRaceCode, String curCode, Integer balanceWay, Integer row) {
        FcBusinessRuleDefineDTO ruleDefine = abBillTallyService.getBusinessRuleExp(sysRaceCode);
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        String corpId1 = null;
        if (balanceWay == 1) {
            entrys.get(0).setBalanceWay(balanceWay);
            // todo:数据转换
            com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO entryDefineDTO = convertEntryDefine(entrys.get(0));
            corpId1 = iAccebillService.getFcAccCorpId(netId, corpId, null, ruleDefine.getSysRaceCode(), entryDefineDTO);
            return innerAccountService.listAccountByParams(curCode, netId, corpId1, entrys.get(0).getSubjectCode());
        } else {
            if(row == 2){
                entrys.get(2).setBalanceWay(balanceWay);
                // todo:数据转换
                com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO entryDefineDTO = convertEntryDefine(entrys.get(2));
                corpId1 = iAccebillService.getFcAccCorpId(netId, corpId, null, ruleDefine.getSysRaceCode(), entryDefineDTO);
                return innerAccountService.listAccountByParams(curCode, netId, corpId1, entrys.get(2).getSubjectCode());
            }else {
                entrys.get(1).setBalanceWay(balanceWay);
                // todo:数据转换
                com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO entryDefineDTO = convertEntryDefine(entrys.get(1));
                corpId1 = iAccebillService.getFcAccCorpId(netId, corpId, null, ruleDefine.getSysRaceCode(), entryDefineDTO);
                return innerAccountService.listAccountByParams(curCode, netId, corpId1, entrys.get(1).getSubjectCode());
            }
        }
    }
    private com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO convertEntryDefine(FcVoucherEntryDefineDTO entrys) {
        return BeanUtils.copyNewInstanceProperties(entrys, com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO.class);
    }
}
