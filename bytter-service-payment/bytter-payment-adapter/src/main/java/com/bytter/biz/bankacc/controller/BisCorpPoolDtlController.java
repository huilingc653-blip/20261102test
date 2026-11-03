package com.bytter.biz.bankacc.controller;

import com.bytter.biz.inneraccount.service.BisCorpPoolDtlService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 多级单位余额核算-单位归集流水维护表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bankacc/biscorppooldtl")
public class BisCorpPoolDtlController extends BaseController {

    @Autowired
    private BisCorpPoolDtlService bisCorpPoolDtlService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 跳转 单位归集流水维护表 列表
     * @return
     */
    @LogMessage(content = "查询单位归集流水维护表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "bankacc/listBisCorpPoolDtl";
    }

    /**
     * 查询 单位归集透支金额维护 列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisCorpPoolDtlService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 批量认领
     * @param model
     * @return
     */
    @GetMapping("/batchClaim")
    public String batchClaim(Model model) {
        return "bankacc/claimBisCorpPoolDtl";
    }

    /**
     * 批量认领 保存
     * @param vo
     * @return
     */
    @LogMessage(content = "修改单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchClaimSave")
    @ResponseBody
    public AjaxJson batchClaimSave(BisCorpPoolDtlVO vo) {
        bisCorpPoolDtlService.batchClaimSave(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 由明细生成余额
     * @param model
     * @return
     */
    @GetMapping("/genBal")
    public String genBal(Model model) {
        model.addAttribute("natualCur", btCurrencyService.getNatualCurrency());
        model.addAttribute("curDate", DateUtils.formatDate(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "bankacc/genBalByBisCorpPoolDtl";
    }

    /**
     * 由明细生成余额 保存
     * @param vo
     * @return
     */
    @LogMessage(content = "修改单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/genBalSave")
    @ResponseBody
    public AjaxJson genBalSave(BisCorpPoolBalVO vo) {
        bisCorpPoolDtlService.genBalSave(vo, this.getCurrentUser());
        return success();
    }
}
