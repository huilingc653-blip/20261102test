package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCorpPoolBankaccQuotaService;
import com.bytter.biz.foreign.service.FexCorpPoolBankaccService;
import com.bytter.biz.foreign.service.FexCorpPoolLimitService;
import com.bytter.biz.foreign.service.FexCorpPoolService;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.biz.foreign.vo.FexCorpPoolBankaccQuotaVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金池账户额度查询
 * @date 2020-03-11 10:31:56
 */
@Controller
@RequestMapping("/foreign/fexcorppoolbankaccquota")
public class FexCorpPoolBankaccQuotaController extends BaseController {

    @Autowired
    private FexCorpPoolBankaccQuotaService fexCorpPoolBankaccQuotaService;
    @Autowired
    private FexCorpPoolService fexCorpPoolService;
    @Autowired
    private FexCorpPoolBankaccService fexCorpPoolBankaccService;
    @Autowired
    private FexCorpPoolLimitService fexCorpPoolLimitService;


    /**
     * 资金池账户额度查询列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCORPPOOLBANKACCQUOTA_QUERY)
    @LogMessage(content = "资金池账户额度查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(FexCorpPoolBankaccQuotaVO fexCorpPoolBankaccQuotaVO, Model model) {
        return "foreign/listFexCorpPoolBankaccQuota";
    }

    /**
     * 外币汇款指令表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexCorpPoolBankaccQuotaService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 获取有效外汇资金池
     *
     * @return
     */
    @RequestMapping(params = "getFexCorpPools")
    @ResponseBody
    public List<FexCorpPoolBankAccVO> getPoolAcc() {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        return fexCorpPoolService.getFexCorpPools(this.getCurrentUser(), resource);
    }

}
