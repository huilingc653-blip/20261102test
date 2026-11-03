package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.entity.ECompareBill;
import com.bytter.biz.receipt.service.ECompareBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.ECompareBillEntityVO;
import com.bytter.biz.receipt.vo.ECompareBillPageVO;
import com.bytter.biz.receipt.vo.ECompareBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 对账单电子回单表
 * @date 2017-07-04 15:30:28
 */
@Api(value = "/v1-payment/receipt/ecomparebill",tags = "对账单电子回单表")
@RestController("v1ECompareBillController")
@RequestMapping("/v1-payment/receipt/ecomparebill")
public class ECompareBillController extends BaseController {
    @Autowired
    private ECompareBillService eCompareBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;

    /**
     * 对账单电子回单表 table列表
     *
     * @return
     */
    @ApiOperation("对账单电子回单表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eCompareBillService.listForUnion(input, this.getCurrentUser()));
    }

    /**
     * 对账单电子回单表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("对账单电子回单表编辑页面跳转前数据获取")
    @GetMapping("/update")
    public AjaxJson<ECompareBillEntityVO> update(String id) {
        ECompareBillEntityVO entityVO = new ECompareBillEntityVO();
        if (StringUtils.isNotEmpty(id)) {
            ECompareBill eCompareBill = eCompareBillService.get(id);
            entityVO.setECompareBillPage(eCompareBill);
            return success(entityVO);
        }
        return success();
    }
    /**
     * 对账单电子回单表详情页
     *
     * @return
     */
    @ApiOperation("对账单电子回单表详情页")
    @LogMessage(content = "查询对账单电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{billNo}/{opType}")
    public AjaxJson<ECompareBillPageVO> view(@PathVariable("billNo") String billNo, @PathVariable("opType") String opType, Model model) {
        ECompareBillPageVO pageVO = new ECompareBillPageVO();
        List<ECompareBillVO> list  = eCompareBillService.getVOByBillNo(billNo);
        BigDecimal sumDebitMoney = new BigDecimal(0);
        BigDecimal sumCreditMoney = new BigDecimal(0);
        BigDecimal sumBalance = new BigDecimal(0);
        if(EmptyUtils.isNotEmpty(list) && list.size()>0) {
            String netId = list.get(0).getNetId();
            String billType = list.get(0).getBillType();
            if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                pageVO.setEVisaTypePage(eVisaType);
            }
            ECompareBillVO sumVO = eCompareBillService.getSumCompareBills(billNo);
            if(!EmptyUtils.isEmpty(sumVO)){
                sumDebitMoney = sumVO.getSumDebitMoney();
                sumCreditMoney = sumVO.getSumCreditMoney();
                sumBalance = sumVO.getSumBalance();
            }
            pageVO.setECompareBillVO(list.get(0));
        }
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        pageVO.setList(list);
        pageVO.setSumDebitMoney(sumDebitMoney);
        pageVO.setSumCreditMoney(sumCreditMoney);
        pageVO.setSumBalance(sumBalance);
        return success(pageVO);
    }
    /**
     * 添加对账单电子回单表
     *
     * @param eCompareBill
     * @return
     */
    @ApiOperation("添加对账单电子回单表")
    @LogMessage(content = "添加对账单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ECompareBill eCompareBill) {
        eCompareBillService.save(eCompareBill);
        return success();
    }
    /**
     * 修改对账单电子回单表
     *
     * @param eCompareBill
     * @return
     */
    @ApiOperation("修改对账单电子回单表")
    @LogMessage(content = "修改对账单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ECompareBill eCompareBill) {
        ECompareBill t = eCompareBillService.get(eCompareBill.getId());
        BeanUtils.copyNotNullProperties(eCompareBill, t);
        eCompareBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除对账单电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除对账单电子回单表")
    @LogMessage(content = "删除对账单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ECompareBill eCompareBill = eCompareBillService.get(id);
        eCompareBillService.deleteEntity(eCompareBill);
        return success();
    }
    /**
     * 批量删除对账单电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除对账单电子回单表")
    @LogMessage(content = "批量删除对账单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ECompareBill eCompareBill = eCompareBillService.get(id);
            eCompareBillService.deleteEntity(eCompareBill);
        }
        return success();
    }
}
