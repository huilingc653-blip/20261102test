package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.inneraccount.service.BisCorpPoolBalService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.biz.inneraccount.vo.v1.BisCorpPoolBalPageVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 多级单位余额核算-单位归集余额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Api(value = "多级单位余额核算-单位归集余额表",tags = "多级单位余额核算-单位归集余额表")
@RestController("v1BisCorpPoolBalController")
@RequestMapping("/v1-payment/bankacc/biscorppoolbal")
public class BisCorpPoolBalController extends BaseController {
    @Autowired
    private BisCorpPoolBalService bisCorpPoolBalService;

    /**
     * 查询 单位归集余额维护 列表数据
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询 单位归集余额维护 列表数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisCorpPoolBalVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisCorpPoolBalService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    @ApiOperation("根据Id查询单位归集余额数据")
    @GetMapping("/getById")
    @ResponseBody
    public AjaxJson<BisCorpPoolBalPageVO> getById(String id){
        BisCorpPoolBalPageVO pageVo = new BisCorpPoolBalPageVO();
        BisCorpPoolBalVO bisCorpPoolBal = bisCorpPoolBalService.getByIdUnion(id);
        pageVo.setBisCorpPoolBalPage(bisCorpPoolBal);
        return success(bisCorpPoolBal);
    }

    /**
     * 新增 单位归集余额数据 保存
     * @param bisCorpPoolBal
     * @return
     */
    @ApiOperation("新增单位归集余额表")
    @LogMessage(content = "新增单位归集余额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisCorpPoolBalVO bisCorpPoolBal) {
        if (checkData(bisCorpPoolBal)) {
            bisCorpPoolBalService.saveBisCorpPoolBal(bisCorpPoolBal, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 编辑 单位归集余额数据 保存
     * @param bisCorpPoolBal
     * @return
     */
    @ApiOperation("修改单位归集余额表")
    @LogMessage(content = "修改单位归集余额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody BisCorpPoolBalVO bisCorpPoolBal) {
        if (checkData(bisCorpPoolBal)) {
            bisCorpPoolBalService.updateBisCorpPoolBal(bisCorpPoolBal, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 导入 单位归集余额数据
     * @param list
     * @return
     */

    @ApiOperation("导入 单位归集余额数据")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BisCorpPoolBalVO> list) {
        bisCorpPoolBalService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 存储校验
     * @param bisCorpPoolBal
     */
    public boolean checkData(BisCorpPoolBalVO bisCorpPoolBal) {
        // 同单位，同币种，同余额日期只能有一条数据
        if (!bisCorpPoolBalService.validRepeat(bisCorpPoolBal)) {
            throw new ParameterException(NetbankMessageEnum.NETBANK_MESSAGE_BISCORPPOOLBAL_REPEAT);
        }
        return true;
    }

    /**
     * 删除 单位归集余额数据
     * @param id
     * @return
     */
    @ApiOperation("删除单位归集余额表")
    @LogMessage(content = "删除单位归集余额表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        bisCorpPoolBalService.delBisCorpPoolBal(id);
        return success();
    }

    /**
     * 批量删除 单位归集余额数据
     * @param ids
     * @return
     */
    @ApiOperation("批量删除单位归集余额表")
    @LogMessage(content = "批量删除单位归集余额表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.bisCorpPoolBalService.delBisCorpPoolBal(ids);
        return success();
    }
}
