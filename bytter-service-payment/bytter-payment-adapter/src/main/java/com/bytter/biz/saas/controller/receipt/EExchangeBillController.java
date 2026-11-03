package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.entity.EExchangeBill;
import com.bytter.biz.receipt.service.EExchangeBillService;
import com.bytter.biz.receipt.vo.EExchangeBillEntityVO;
import com.bytter.biz.receipt.vo.EExchangeBillPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 内部结售汇电子回单
 * @date 2017-07-04 15:30:33
 */
@Api(value = "/v1-payment/receipt/eexchangebill",tags = "内部结售汇电子回单")
@RestController("v1EExchangeBillController")
@RequestMapping("/v1-payment/receipt/eexchangebill")
public class EExchangeBillController extends BaseController {
    @Autowired
    private EExchangeBillService eExchangeBillService;

    /**
     * 内部结售汇电子回单 table列表
     *
     * @return
     */
    @ApiOperation(value = "内部结售汇电子回单列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eExchangeBillService.getDataTables(input));
    }

    /**
     * 内部结售汇电子回单编辑页面跳转
     *
     * @return
     */
    @ApiOperation("内部结售汇电子回单编辑页面跳转前数据获取")
    @GetMapping("/update")
    public AjaxJson<EExchangeBillEntityVO> update(String id) {
        EExchangeBillEntityVO entityVO = new EExchangeBillEntityVO();
        if (StringUtils.isNotEmpty(id)) {
            EExchangeBill eExchangeBill = eExchangeBillService.get(id);
            entityVO.setEExchangeBillPage(eExchangeBill);
            return success(entityVO);
        }
        return success();
    }
    /**
     * 内部结售汇电子回单详情页
     *
     * @return
     */
    @ApiOperation("内部结售汇电子回单详情页")
    @LogMessage(content = "查询内部结售汇电子回单详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<EExchangeBillPageVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EExchangeBillPageVO pageVO = new EExchangeBillPageVO();
        EExchangeBill eExchangeBill = eExchangeBillService.get(id);
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        pageVO.setEExchangeBillPage(eExchangeBill);
        return success(pageVO);
    }
    /**
     * 添加内部结售汇电子回单
     *
     * @param eExchangeBill
     * @return
     */
    @ApiOperation("添加内部结售汇电子回单")
    @LogMessage(content = "添加内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody EExchangeBill eExchangeBill) {
        eExchangeBillService.save(eExchangeBill);
        return success();
    }
    /**
     * 修改内部结售汇电子回单
     *
     * @param eExchangeBill
     * @return
     */
    @ApiOperation("修改内部结售汇电子回单")
    @LogMessage(content = "修改内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody EExchangeBill eExchangeBill) {
        EExchangeBill t = eExchangeBillService.get(eExchangeBill.getId());
        BeanUtils.copyNotNullProperties(eExchangeBill, t);
        eExchangeBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除内部结售汇电子回单
     *
     * @param id
     * @return
     */
    @ApiOperation("删除内部结售汇电子回单")
    @LogMessage(content = "删除内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EExchangeBill eExchangeBill = eExchangeBillService.get(id);
        eExchangeBillService.deleteEntity(eExchangeBill);
        return success();
    }
    /**
     * 批量删除内部结售汇电子回单
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除内部结售汇电子回单")
    @LogMessage(content = "批量删除内部结售汇电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EExchangeBill eExchangeBill = eExchangeBillService.get(id);
            eExchangeBillService.deleteEntity(eExchangeBill);
        }
        return success();
    }
}
