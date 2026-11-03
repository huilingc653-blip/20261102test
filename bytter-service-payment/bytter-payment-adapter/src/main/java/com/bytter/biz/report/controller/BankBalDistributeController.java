package com.bytter.biz.report.controller;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.report.service.BankBalDistributeService;
import com.bytter.biz.report.vo.BankBalDistributeVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author shenc
 * @Description 银行余额统计报表
 * @date 2018.12.14
 */
@Controller
@RequestMapping("/report/bankBalDistribute")
public class BankBalDistributeController extends BaseController {
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired
    private BankBalDistributeService bankBalDistributeService;
    //////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_BANKBALDISTRIBUTE_QUERY)
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
            List<CorpDTO> resCorps = corpService.listUserCorp(ResourceConstant.NETBANK_REPORT_BANKBALDISTRIBUTE_QUERY,getUserId());
            corps.retainAll(resCorps);
            for (CorpDTO dto : corps) {
                corpIds.append(",").append(dto.getId());
            }
        }
        model.addAttribute("corpIds", corpIds.toString());
        //BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        String[] parentIds = corp.getParentIds().split(",");
        String curId = corpService.get(parentIds[0]).getCurId();
        model.addAttribute("curId",curId);
        return "report/listBankBalDistribute";
    }
    //////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_BANKBALDISTRIBUTE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        DataTablesOutput output = bankBalDistributeService.getDataTablesUnion(input);
        return output;
    }
    @PostMapping("/getBankBal")
    @ResponseBody
    public Object getBankBal(BankBalDistributeVo vo){
        return bankBalDistributeService.getBankBal(vo);
    }
}
