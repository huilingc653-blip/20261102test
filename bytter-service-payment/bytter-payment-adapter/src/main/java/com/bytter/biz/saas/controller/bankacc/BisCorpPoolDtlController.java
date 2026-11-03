package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.inneraccount.service.BisCorpPoolDtlService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolDtlVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccGenBalConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "多级单位余额核算-单位归集流水维护表",tags = "多级单位余额核算-单位归集流水维护表")
@RestController("v1BisCorpPoolDtlController")
@RequestMapping("/v1-payment/bankacc/biscorppooldtl")
public class BisCorpPoolDtlController extends BaseController {

    @Autowired
    private BisCorpPoolDtlService bisCorpPoolDtlService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 查询 单位归集透支金额维护 列表数据
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询 单位归集流水维护 列表数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisCorpPoolDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisCorpPoolDtlService.getDataTablesUnion(input, this.getCurrentUser()));
    }


    /**
     * 批量认领 保存
     * @param vo
     * @return
     */
    @ApiOperation("批量认领 保存")
    @LogMessage(content = "修改单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchClaimSave")
    @ResponseBody
    public AjaxJson batchClaimSave(@RequestBody BisCorpPoolDtlVO vo) {
        bisCorpPoolDtlService.batchClaimSave(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 由明细生成余额
     * @return
     */

    @ApiOperation("由明细生成余额跳转前的参数获取")
    @GetMapping("/getGenBalConfig")
    @ResponseBody
    public AjaxJson<BtBankAccGenBalConfigVO> genBal() {
        BtBankAccGenBalConfigVO configVO = new BtBankAccGenBalConfigVO();
        configVO.setNatualCur(btCurrencyService.getNatualCurrency());
        configVO.setCurDate(DateUtils.formatDate(DateUtils.getDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(configVO);
    }

    /**
     * 由明细生成余额 保存
     * @param vo
     * @return
     */
    @ApiOperation("由明细生成余额 保存")
    @LogMessage(content = "由明细生成余额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/genBalSave")
    @ResponseBody
    public AjaxJson genBalSave(@RequestBody BisCorpPoolBalVO vo) {
        bisCorpPoolDtlService.genBalSave(vo, this.getCurrentUser());
        return success();
    }
}
