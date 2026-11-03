package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.vo.RatBkrationVO;
import com.bytter.report.domain.cms.service.BankRatStatisticsService;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cms/report/bankratstatistics")
public class BankRatStatisticsController extends BaseController {
    @Autowired
    private ICorpService corpService;
    @Autowired
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private BankRatStatisticsService bankRatStatisticsService;

    //@RequiresPermissions(ResourceConstant.CMS_REPORT_BANKRATSTATISTICS_QUERY)
    @GetMapping("/list")
    public String list(Model model){
        // 判断当前用户是否为单位用户
        StringBuilder corpIds = new StringBuilder();
        ICurrentUser user = this.getCurrentUser();
        CorpDTO corp = corpService.get(user.getCorpId());
        corpIds.append(corp.getId());
        if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())){
            List<CorpDTO> corps = corpService.listCorpByNetId(corp.getNetId());
            //根据资源获取单位,有查询资源的单位与网点下的单位取交集
            List<CorpDTO> resCorps = corpService.listUserCorp(ResourceConstant.CMS_REPORT_BANKRATSTATISTICS_QUERY,getUserId());
            corps.retainAll(resCorps);
            for (CorpDTO dto : corps) {
                corpIds.append(",").append(dto.getId());
            }
        }

        model.addAttribute("corpIds", corpIds.toString());
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        model.addAttribute("curId",natualCurrency.getId());
        return "cms/report/ratBankRation/listBankRatStatistics";
    }

    //@RequiresPermissions(ResourceConstant.CMS_REPORT_BANKRATSTATISTICS_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        DataTablesOutput output = bankRatStatisticsService.getDataTablesUnion(input,getCurrentUser());
        return output;
    }

    @PostMapping("/getBankBal")
    @ResponseBody
    public Object getBankBal(RatBkrationVO vo){
        return bankRatStatisticsService.getBankBal(vo,getCurrentUser());
    }
}
