package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.inneraccount.service.BisCorpPoolOverdraftService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolOverdraftVO;
import com.bytter.biz.inneraccount.vo.v1.BisCorpPoolOverdraftPageVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 多级单位余额核算-单位归集透支金额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Api(value = "多级单位余额核算-单位归集透支金额表",tags = "多级单位余额核算-单位归集透支金额表")
@RestController("v1BisCorpPoolOverdraftController")
@RequestMapping("/v1-payment/bankacc/biscorppooloverdraft")
public class BisCorpPoolOverdraftController extends BaseController {

    @Autowired
    private BisCorpPoolOverdraftService bisCorpPoolOverdraftService;

    /**
     * 查询 单位归集透支金额维护 列表数据
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询 单位归集透支金额维护 列表数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisCorpPoolOverdraftVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisCorpPoolOverdraftService.getDataTablesUnion(input, this.getCurrentUser()));
    }


    @ApiOperation("根据ID查询单位归集透支金额数据")
    @GetMapping("/getById")
    @ResponseBody
    public AjaxJson<BisCorpPoolOverdraftPageVO> getById(String id) {
        BisCorpPoolOverdraftPageVO pageVO = new BisCorpPoolOverdraftPageVO();
        if (StringUtils.isNotEmpty(id)) {
            BisCorpPoolOverdraftVO bisCorpPoolOverdraft = bisCorpPoolOverdraftService.getByIdUnion(id);
            pageVO.setBisCorpPoolOverdraft(bisCorpPoolOverdraft);
            return success(pageVO);
        }
        return success();
    }

    /**
     * 新增 单位归集透支金额数据 保存
     * @param bisCorpPoolOverdraft
     * @return
     */
    @ApiOperation("新增单位归集透支金额表")
    @LogMessage(content = "新增单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisCorpPoolOverdraftVO bisCorpPoolOverdraft) {
        if (checkData(bisCorpPoolOverdraft)) {
            bisCorpPoolOverdraftService.saveBisCorpPoolOverdraft(bisCorpPoolOverdraft, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 编辑 单位归集透支金额数据 保存
     * @param bisCorpPoolOverdraft
     * @return
     */
    @ApiOperation("修改单位归集透支金额表")
    @LogMessage(content = "修改单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BisCorpPoolOverdraftVO bisCorpPoolOverdraft) {
        if (checkData(bisCorpPoolOverdraft)) {
            bisCorpPoolOverdraftService.updateBisCorpPoolOverdraft(bisCorpPoolOverdraft, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 导入 单位归集透支金额数据
     * @param list
     * @return
     */
    @ApiOperation("导入 单位归集透支金额数据")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BisCorpPoolOverdraftVO> list) {
        bisCorpPoolOverdraftService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 存储校验
     * @param bisCorpPoolOverdraft
     */
    public boolean checkData(BisCorpPoolOverdraftVO bisCorpPoolOverdraft) {
        // 同单位，同币种只能有一条数据
        if (!bisCorpPoolOverdraftService.validRepeat(bisCorpPoolOverdraft)) {
            throw new ParameterException(NetbankMessageEnum.NETBANK_MESSAGE_BISCORPPOOLOVERDRAFT_REPEAT);
        }
        return true;
    }

    /**
     * 删除 单位归集透支金额数据
     * @param id
     * @return
     */
    @ApiOperation("删除单位归集透支金额表")
    @LogMessage(content = "删除单位归集透支金额表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        bisCorpPoolOverdraftService.delBisCorpPoolOverdraft(id);
        return success();
    }

    /**
     * 批量删除 单位归集透支金额数据
     * @param ids
     * @return
     */
    @ApiOperation("批量删除单位归集透支金额表")
    @LogMessage(content = "批量删除单位归集透支金额表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.bisCorpPoolOverdraftService.delBisCorpPoolOverdraft(ids);
        return success();
    }
}
