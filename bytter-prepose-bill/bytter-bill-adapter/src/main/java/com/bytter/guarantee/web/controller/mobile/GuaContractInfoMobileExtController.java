package com.bytter.guarantee.web.controller.mobile;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.vo.GuaConrrantChangeTrackVO;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author luxy
 * @version 1.0
 * @Title:GuaContractInfoController
 * @Description:被担保合同管理控制层
 * @date 2018年12月12日 上午9:45:46
 */
@Controller
@RequestMapping("/mobile/cms/guarantee/guaContractInfo")
public class GuaContractInfoMobileExtController extends BaseController {

    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private IGuaWayServie iGuaWayServie;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;


    @RequestMapping("/appReportlist")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String guaContractAppReport(Model model) {
        String relateCon = guaContractInfoService.relateCon();
        model.addAttribute("relateCon", relateCon);
        List<BtCurrencyDTO> curList = currencyService.getBtCurrency();
        model.addAttribute("curList", curList);
        List<GuaWayDTO> guaWayList = iGuaWayServie.getAllGuaWay();
        model.addAttribute("guaWayList", guaWayList);
        return "mobile/cms/guarantee/guaContractAppReport";
    }

    @PostMapping("/appReportData")
    @ResponseBody
    public Object appReportData(String corpIds, String guaWayCode, String guaNo, String contractCode, String startDate, String endDate) {
        DataTablesInput input = new DataTablesInput();
        HashMap<String, Object> params = new HashMap<>();
        params.put("ins_corpId", corpIds);
        params.put("ins_guaType", guaWayCode);
        params.put("likes_contractCode", contractCode);
        params.put("likes_guaNo", guaNo);
        if (EmptyUtils.isNotEmpty(startDate)) {
            params.put("ged_startDate", DateUtils.formatDate(startDate));
        }
        if (EmptyUtils.isNotEmpty(endDate)) {
            params.put("led_endDate", DateUtils.formatDate(endDate));
        }
        input.setFormdata(params);
        input.setLength(1000);
        DataTablesOutput output = guaContractInfoService.searchGuaContractTables(input, this.getCurrentUser());
        List<GuaContractInfoVO> contractInfoVOList = output.getData();
        return contractInfoVOList;
    }


    @RequestMapping("/mobileGuaTypeTrack")
    public String mobileGuaTypeTrack(String contractId, Model model) {
        List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(contractId);
        model.addAttribute("trackList", trackList);
        return "mobile/cms/guarantee/mobileGuaTypeTrack";
    }
}
