package com.bytter.system.pricing.controller;
import com.bytter.pricing.service.DepositRateReCheckService;
import com.bytter.pricing.service.DepositRateService;
import com.bytter.pricing.vo.DepositRateVO;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author luxy
 * @Title: DepositRateReCheckController
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/23 21:02
 */
@Controller
@RequestMapping("/pricing/depositRate/interestRateReCheck")
public class DepositRateReCheckController extends BaseController {
    @Autowired
    private DepositRateService depositRateService;
    @Autowired
    private DepositRateReCheckService depositRateReCheckService;
    @GetMapping("/list")
    @LogMessage(content = "存款利率待复核列表页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public String list(){
        return "pricing/depositRate/listDepositRateReCheck";
    }
    /**
     * 查询存款利率待复核列表
     * @param input
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    @LogMessage(content = "查询存款利率待复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input){
        input.addFilter(MatchTypeEnum.EQ, "status", PricingConstant.STATUS_RECHECK);
        return depositRateReCheckService.datatable(input,getCurrentUser());
    }
    /**
     * 进入复核数据页面
     */
    @GetMapping("/reCheck")
    @LogMessage(content="进入复核存款利率页面",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    public String reCheck(String id, Model model){
        DepositRateVO depositRateVO=depositRateReCheckService.getDepositRateById(id);
        model.addAttribute("depositRateVO",depositRateVO);
        return "pricing/depositRate/reCheckDepositRate";
    }
    /**
     * 批量操作
     * @param ids 存款利率的id
     * @param handleType 操作类型：2-通过，-1打回
     */
    @PostMapping("/checkBatch")
    @ResponseBody
    @LogMessage(content = "批量执行复核操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public AjaxJson checkBatch(String ids,String handleType,String returndeScribe){
        depositRateReCheckService.reCheckBatch(ids,handleType,returndeScribe,this.getCurrentUser());
        return success();
    }
}
