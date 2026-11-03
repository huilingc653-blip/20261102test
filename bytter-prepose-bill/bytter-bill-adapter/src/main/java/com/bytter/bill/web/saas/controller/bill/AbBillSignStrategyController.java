package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillSignStrategyService;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: AbBillSignStrategyController
 * @Description: 电票自动签收配置Controller
 * @author wangzy
 * @date 2020/7/13 14:54
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillsignstrategy", tags = "电票自动签收配置")
@RestController("v1AbBillSignStrategyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillsignstrategy")
public class AbBillSignStrategyController extends BaseController {
    @Autowired
    public AbBillSignStrategyService abBillSignStrategyService;

    /**
     * 电票自动签收配置 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询电票自动签收配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("电票自动签收配置列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillSignStrategyService.getDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 电票自动签收配置新增页面初始化
     */
    @GetMapping("/add")
    @ApiOperation("电票自动签收配置新增页面初始化")
    public AjaxJson<Map<String, Object>> add() {
        Map<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("autoSignNo", SequenceSingleton.getSequence(SequenceEnum.BILL_AUTO_SIGN_CODE));
        dataMap.put("forwardPath", "bill/accebill/addAbBillSignStrategy");
        return AjaxJson.success(dataMap);
    }

    /**
     * 电票自动签收配置编辑页面初始化
     */
    @GetMapping("/update/{id}")
    @ApiOperation("电票自动签收配置编辑页面初始化")
    public AjaxJson<Map<String, Object>> update(@PathVariable("id") String id) {
        AbBillSignStrategyVO abBillSignStrategyVO = new AbBillSignStrategyVO();
        abBillSignStrategyVO = abBillSignStrategyService.combinationBillVo(abBillSignStrategyVO, id);
        Map<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("abBillSignStrategyVO", abBillSignStrategyVO);
        dataMap.put("forwardPath", "bill/accebill/updateAbBillSignStrategy");
        return AjaxJson.success(dataMap);
    }

    /**
     * 新增/修改-保存电票自动签收配置数据
     */
    @LogMessage(content = "新增-电票自动签收配置数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save/{operator}")
    @ApiOperation("新增/修改电票自动签收配置数据")
    public AjaxJson saveOrUpdate(@PathVariable("operator") String operator, AbBillSignStrategyVO abBillSignStrategyVO) {
        return abBillSignStrategyService.saveOrUpdate(operator, abBillSignStrategyVO, getCurrentUser());
    }

    /**
     * 批量删除电票自动签收配置
     */
    @LogMessage(content = "删除-电票自动签收配置数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchDel")
    @ApiOperation("批量删除电票自动签收配置")
    public AjaxJson batchDel(String ids) {
        return abBillSignStrategyService.saveOrUpdate(ids, this.getCurrentUser());
    }
}