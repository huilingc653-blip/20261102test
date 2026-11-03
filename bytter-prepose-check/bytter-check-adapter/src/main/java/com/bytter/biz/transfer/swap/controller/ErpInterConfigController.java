package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.service.ErpInterConfigService;
import com.bytter.biz.transfer.swap.vo.ErpInterConfigFillVO;
import com.bytter.check.constant.InterConfigConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Created by zhouj on 2018/5/28.
 */
@Controller
@RequestMapping("/erp/swap/interconfig")
public class ErpInterConfigController extends BaseController {
    @Autowired
    private ErpInterConfigService erpInterConfigService;
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAP_INTERCONFIG_APPLY)
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("payer",erpInterConfigService.selectByConfig(InterConfigConstant.PAYER));
        model.addAttribute("payee",erpInterConfigService.selectByConfig(InterConfigConstant.PAYEE));
        model.addAttribute("accebill",erpInterConfigService.selectByConfig(InterConfigConstant.ACCEBILL));
        return "transfer/swap/config_index";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAP_INTERCONFIG_APPLY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request) {
        erpInterConfigService.saveConfigs(request.getParameterMap());
        return success();
    }
    /**
     * 补录配置页面跳转
     *
     * @return
     */
    @LogMessage(content = "补录配置页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/set/{type}")
    public String set(@PathVariable("type") String type, Model model) {
        if (StringUtils.isNotEmpty(type)) {
            model.addAttribute("configType", type);
        }
        return "transfer/swap/configNewSetInfo";
    }
    /**
     * 配置明细信息
     *
     * @return
     */
    @PostMapping("/setDTLInfoDatatable")
    @ResponseBody
    public List<ErpInterConfigFillVO> setDTLInfoDatatable(String type){
        List<ErpInterConfigFillVO> matchRuleList=erpInterConfigService.listErpInterConfigFillInfoByType(type);
        return matchRuleList;
    }
}
