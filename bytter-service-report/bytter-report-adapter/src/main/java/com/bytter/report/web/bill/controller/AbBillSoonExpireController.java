package com.bytter.report.web.bill.controller;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.increment.api.service.IBiRemainTermService;
import com.bytter.report.domain.billl.service.AbBillSoonExpireService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 即将到期票据分析
 * @author zhenghj
 * @date 2020-02-10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/report/soonExpireBill")
//@RequestMapping("/bill/accebill/soonExpireBill")
public class AbBillSoonExpireController extends BaseController {
    @Autowired
    private AbBillSoonExpireService abBillSoonExpireService;
    @DubboReference(check = false)
	private IBiRemainTermService biRemainTermService;
    /**
     * 即将到期票据分析报表页面跳转
     * @param model
     * @return
     */
    @GetMapping ("/list")
    public String corplist(Model model){
        List<com.bytter.increment.api.dto.BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs(null);
        model.addAttribute("terms", biRemainTermDTOs);
        return "bill/report/listAbBillSoonExpire";
    }
    /**
     * 即将到期票据 分析报表
     *
     * @return
     */
    @LogMessage(content = "查询即将到期票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        DataTablesOutput out = abBillSoonExpireService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(),null);
        return out;
    }
    /**
     * 即将到期票据分析图表
     * @param corpId
     * @param moneyUnit
     * @param billFlagName
     * @return
     */
    @PostMapping("/getData")
    @ResponseBody
    public Object getData(String corpId, String type, String moneyUnit, String billFlagName){
        DataTablesInput input = new DataTablesInput();
        if(StringUtils.isNotEmpty(type)){
            input.getFormdata().put("type", type);
        }
        if(StringUtils.isNotEmpty(corpId)){
            input.addFilter(MatchTypeEnum.IN,"corpId",corpId);
        }
        if(StringUtils.isNotEmpty(billFlagName)){
            input.getFormdata().put("billFlagName", billFlagName);
        }
        DataTablesOutput out = abBillSoonExpireService.getDataTablesUnion(input, this.getCurrentUser(),moneyUnit);
        return abBillSoonExpireService.getData(out.getData(),moneyUnit);
    }
    /**
     * 即将到期票据明细页面
     *
     * @return
     */
    @GetMapping("/open")
    public String open(Model model, String corpId,String termId, String type, String billFlag) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("billFlag", billFlag);
        model.addAttribute("type", type);
        model.addAttribute("termId", termId);
        return "bill/report/listAbBillSoonExpireDetail";
    }
    /**
     * 即将到期票据明细表数据
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/detailedDatatable")
    @ResponseBody
    public DataTablesOutput detailedDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillSoonExpireService.listSoonExpireDetailed(dataTablesInput);
    }
    /**
     * 即将到期票据分析报表页面跳转
     * @param model
     * @return
     */
    @RequestMapping("/mobile/list")
    public String loadAbSoonList(Model model){
        List<com.bytter.increment.api.dto.BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs(null);
        model.addAttribute("dataList", biRemainTermDTOs);
        model.addAttribute("billSoonExpireReson", AccebillConstant.AB_BILL_SOON_EXPIRE_QUERY);
        return "bill/report/mobile/mobileAbBillSoonExpire";
    }
    /**
     * 即将到期票据分析报表页面跳转
     * @param model
     * @return
     */
    @GetMapping("/mobile/gcMobileList")
    public String gcMobileList(Model model){
        List<BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs(null);
        model.addAttribute("dataList", biRemainTermDTOs);
        model.addAttribute("billSoonExpireReson", AccebillConstant.AB_BILL_SOON_EXPIRE_QUERY);
        return "bill/report/mobile/mobileAbBillSoonExpire";
    }
    /**
     *
     * @param corpId
     * @param type
     * @param billTypeName
     * @param buisTypeName
     * @param remainMonId
     * @return
     */
    @PostMapping("/getAppData")
    @ResponseBody
    public Object getAppData(String corpId, String billType,
    		String billTypeName,String buisTypeName,String remainMonId){
    	DataTablesInput dataTablesInput = new DataTablesInput();
    	dataTablesInput.getFormdata().put("moneyUnit", "3");
    	dataTablesInput.getFormdata().put("ins_corpId", corpId);
    	dataTablesInput.getFormdata().put("type", billType);
    	dataTablesInput.getFormdata().put("billFlagName", billTypeName);
        @SuppressWarnings("rawtypes")
		DataTablesOutput out = abBillSoonExpireService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(),null);
        String name = StringUtils.getUtf8String(buisTypeName);
        @SuppressWarnings("unchecked")
		Object jsonObj = abBillSoonExpireService.appJsonObject(out.getData(),name,true,remainMonId);
        return jsonObj;
    }
    /**
     *
     * @param corpId
     * @param type
     * @param billTypeName
     * @param viewType
     * @param buisTypeName
     * @param remainMonId
     * @return
     */
    @PostMapping("/getMapAppData")
    @ResponseBody
    public Object getMapAppData(String corpId, String billType,
    		String billTypeName,String viewType,String buisTypeName,String remainMonId){
    	DataTablesInput dataTablesInput = new DataTablesInput();
    	dataTablesInput.getFormdata().put("moneyUnit", "3");
    	dataTablesInput.getFormdata().put("ins_corpId", corpId);
    	dataTablesInput.getFormdata().put("type", billType);
    	dataTablesInput.getFormdata().put("billFlagName", billTypeName);
        @SuppressWarnings("rawtypes")
		DataTablesOutput out = abBillSoonExpireService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(),null);
        @SuppressWarnings("unchecked")
		Object jsonObj = abBillSoonExpireService.appJsonObject(out.getData(),buisTypeName,false,remainMonId);
        return jsonObj;
    }
}
