package com.bytter.credential.web.international.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credential.api.enums.LcMessageEnum;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.ArrivalLetterService;
import com.bytter.credential.domain.international.service.BillArrivalService;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
/**
 * @Title 到单控制层
 * @Description 到单控制层
 * @Author liusy
 * @Date 2018-12-17 18:53
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/billArrival")
public class BillArrivalController extends BaseController {
    @Autowired
    private BillArrivalService billArrivalService;
    @Autowired
    private CreatedCreditService creditService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private ArrivalLetterService arrivalLetterService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
    /**
     * 到单登记列表页面
     */
    @LogMessage(content = "到单登记列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listStorageCredit")
    public String listArrival(Model model) {
        return "international/credit/billArrival/listBillArrival";
    }
    /**
     * 到单登记修改列表页面
     * @param model
     * @return
     */
    @LogMessage(content = "到单登记修改列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listArrivalUpdate")
    public String listArrivalUpdate(Model model) {
        return "international/credit/billArrival/listBillArrivalUpdate";
    }
    /**
     * 到单列表显示
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "信用证开证列表显示（已入库数据）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryStorageCredit")
    @ResponseBody
    public DataTablesOutput queryStorageCredit(@RequestBody DataTablesInput dataTablesInput){
        return billArrivalService.listStorageCredit(dataTablesInput, getUserId());
    }
    /**
     *到单登记列表数据查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "到单登记列表数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryArrivalUpdate")
    @ResponseBody
    public DataTablesOutput queryArrivalUpdate(@RequestBody DataTablesInput dataTablesInput){
        return billArrivalService.listArrivalUpdate(dataTablesInput, getUserId());
    }
    /**
     * 到单登记列表页面
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "跳转到单登记新增页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addBillArrival")
    public String addBillArrival(@RequestParam String id,Model model) {
        Map<String, Object> params = new HashedMap();
        params.put("lcId", id);
        Map<String, Object> resultMap = (Map<String, Object>) billArrivalService.getArrivalCountAndSumbillMoney(params).get(0);
        LcEnrol lcEnrol = creditService.get(id);
        String newCreditNo = arrivalLetterService.getNextArrivalNo(lcEnrol.getCreditNo());
/*        if ("0".equals(lcEnrol.getLcInter())) {
            BigDecimal exchange = excrateAdjustService.getExchangeRateBy(lcEnrol.getIssuingCurId(), new Date());
            lcEnrol.setExchangeRate(exchange);
        }*/
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("corpName", corpService.get(lcEnrol.getCorpId()).getName());
        model.addAttribute("billArrivalCount",resultMap.get("BILLARRIVALCOUNT"));
        model.addAttribute("sumBillMoney",resultMap.get("SUMBILLMONEY"));
        model.addAttribute("newCreditNo",newCreditNo);
        return "international/credit/billArrival/addBillArrival";
    }
    /**
     * 到单编辑修改
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "到单编辑修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editBillArrival")
    public String editBillArrival(@RequestParam String id, Model model) {
        LcBillArrival lcBillArrival = billArrivalService.get(id);
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("lcId", lcBillArrival.getLcId());
        Map<String, Object> resultMap = (Map<String, Object>) billArrivalService.getArrivalCountAndSumbillMoney(params).get(0);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("corpName", corpService.get(lcEnrol.getCorpId()).getName());
        model.addAttribute("billArrivalCount",resultMap.get("BILLARRIVALCOUNT"));
        model.addAttribute("sumBillMoney",resultMap.get("SUMBILLMONEY"));
        return "international/credit/billArrival/editBillArrival";
    }
    /**
     * 到单登记入库
     * @param lcBillArrival 到单实体类
     * @param model
     * @return
     */
    @LogMessage(content = "到单登记入库", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ResponseBody
    @PostMapping("/saveBillArrival")
    public AjaxJson saveBillArrival(LcBillArrival lcBillArrival, Model model) {
        ICurrentUser user = getCurrentUser();
        BigDecimal number0 = new BigDecimal(0);
        if(number0.compareTo(lcBillArrival.getPayMoney())  > 0 || number0.compareTo(lcBillArrival.getBillMoney()) > 0) {
        	return new AjaxJson(false,LcMessageEnum.DZ_EXIST.getName());
        }
        billArrivalService.saveOrTmpBillArrival(lcBillArrival, user);
        return success();
    }
    /**
     * 到单报表查询页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "到单报表查询页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listReportBillArrival")
    public String listReportBillArrival(Model model) {
        return "international/report/listReportBillArrival";
    }
    /**
     * 到单报表数据查询
     * @param dataTablesInput
     * @param model
     * @return
     */
    @LogMessage(content = "到单报表数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryReportBillArrival")
    @ResponseBody
    public DataTablesOutput listReportBillArrival(@RequestBody DataTablesInput dataTablesInput, Model model) {
        ICurrentUser user = getCurrentUser();
        return billArrivalService.listReportBillArrival(dataTablesInput, user);
    }
    /**
     * 到单查看显示
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "到单查看显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewBillArrival")
    public String viewBillArrival(@RequestParam String id, Model model) {
        LcBillArrival lcBillArrival = billArrivalService.get(id);
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        Map<String, Object> params = new HashedMap();
        params.put("lcId", lcBillArrival.getLcId());
        Map<String, Object> resultMap = (Map<String, Object>) billArrivalService.getArrivalCountAndSumbillMoney(params).get(0);
        model.addAttribute("lcBillArrival", lcBillArrival);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("corpName", corpService.get(lcEnrol.getCorpId()).getName());
        model.addAttribute("billArrivalCount",resultMap.get("BILLARRIVALCOUNT"));
        model.addAttribute("sumBillMoney",resultMap.get("SUMBILLMONEY"));
        return "international/credit/billArrival/viewBillArrival";
    }
}
