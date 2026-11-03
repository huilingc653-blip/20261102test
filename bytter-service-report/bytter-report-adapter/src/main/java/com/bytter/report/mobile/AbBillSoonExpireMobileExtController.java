package com.bytter.report.mobile;

import com.bytter.report.domain.billl.service.AbBillSoonExpireService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.report.api.dto.BiRemainTermDTO;
import com.bytter.report.api.service.IBiRemainTermService;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.cloud.common.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/mobile/bill/accebill/soonExpireBill")
public class AbBillSoonExpireMobileExtController extends BaseController {

    @Autowired
    private AbBillSoonExpireService abBillSoonExpireService;
    @Autowired(required=false)
    private IBiRemainTermService biRemainTermService;
    /**
     * 即将到期票据分析报表页面跳转
     * @param model
     * @return
     */
    @RequestMapping("/mobile/list")
    public String loadAbSoonList(Model model){
        List<BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs(null);
        model.addAttribute("dataList", biRemainTermDTOs);
        model.addAttribute("billSoonExpireReson", AccebillConstant.AB_BILL_SOON_EXPIRE_QUERY);
        return "mobile/bill/mobileAbBillSoonExpire";
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
        return "mobile/bill/mobileAbBillSoonExpire";
    }


    /**
     *
     * @param corpId
     * @param billType
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
        String name = "应收票据";
        if(CommonConstant.YES.equals(buisTypeName)){
            name = "应付票据";
        }
        @SuppressWarnings("unchecked")
        Object jsonObj = abBillSoonExpireService.appJsonObject(out.getData(),name,true,remainMonId);
        return jsonObj;
    }


    /**
     * 【到期票据分析 图标】
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
        String name = "应收票据";
        if(CommonConstant.YES.equals(buisTypeName)){
            name = "应付票据";
        }
        @SuppressWarnings("rawtypes")
        DataTablesOutput out = abBillSoonExpireService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(),null);
        @SuppressWarnings("unchecked")
        Object jsonObj = abBillSoonExpireService.appJsonObject(out.getData(),name,false,remainMonId);
        return jsonObj;
    }
}
