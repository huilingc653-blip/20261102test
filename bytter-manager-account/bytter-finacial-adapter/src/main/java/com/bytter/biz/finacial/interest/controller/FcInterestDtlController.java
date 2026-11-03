package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.interest.service.FcInterestDtlService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author zhengxn
 * @version 1.0
 * @Title:利息管理--利息明细查询表
 * @Description: FC_INTEREST_DTL
 * @date 2017-06-23 14:34:54
 */
@Controller
@RequestMapping("/finacial/interest/fcinterestdtl")
public class FcInterestDtlController extends BaseController {
    @Autowired
    private FcInterestDtlService fcInterestDtlService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
    /**
     * 利息明细查询表 列表 页面跳转
     */
    @LogMessage(content = "查询FC_INTEREST_DTL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("isPool","0");
        return "finacial/interest/listFcInterestDtl";
    }
    /**
     * 利息明细查询表  table列表*
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCINTERESTDTL_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput,String isPool) {
        return fcInterestDtlService.listAccount(dataTablesInput, FinacialResourceConstant.FINACIAL_FCINTERESTDTL_QUERY, getCurrentUser(),isPool);
    }
    
    /**
     * 利息明细查询表 列表 页面跳转
     */
    @LogMessage(content = "查询FC_INTEREST_DTL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listPool")
    public String listPool(Model model) {
        model.addAttribute("isPool","1");
        return "finacial/interest/listFcInterestDtl";
    }
    /**
     * 获取资金池-主体
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = FinacialResourceConstant.FINACIAL_FCINTERESTDTL_QUERY;
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }
    
    
    /**
     * 获取资金池-子单位
     * @param poolId
     * @return
     */
    @RequestMapping(params = "getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
        String resource = FinacialResourceConstant.FINACIAL_FCINTERESTDTL_QUERY;
        return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }
}
