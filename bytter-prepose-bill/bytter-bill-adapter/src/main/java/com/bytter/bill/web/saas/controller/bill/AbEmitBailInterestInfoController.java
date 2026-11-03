package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbEmitBailInterestInfo;
import com.bytter.bill.domain.bill.service.AbEmitBailInterestInfoService;
import com.bytter.bill.domain.bill.vo.AbEmitBailInterestInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 票据保证金利息维护
 * @author zhous
 * @date 2025/3/5 14:38
 */
@Api(value = "/v1-preposebill/bill/accebill/bailInterestInfo", tags = "票据保证金利息维护")
@RestController("v1AbEmitBailInterestInfoController")
@RequestMapping("/v1-preposebill/bill/accebill/bailInterestInfo")
public class AbEmitBailInterestInfoController extends BaseController {

    @Autowired
    private AbEmitBailInterestInfoService abEmitBailInterestInfoService;

    /**
     * 票据保证金利息维护列表查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "票据保证金利息维护列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("票据保证金利息维护分页列表")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = abEmitBailInterestInfoService.dataTable(dataInput);
        return AjaxJson.success(output);
    }



    /**
     * 编辑页面初始化
     */
    @GetMapping("/update")
    @ApiOperation("票据保证金利息编辑页面初始化")
    public AjaxJson<Map<String, Object>> update(String id) {
        Map<String, Object> dataMap = new HashMap<>(4);
        if (StringUtils.isNotBlank(id)) {
            AbEmitBailInterestInfo abEmitBailInterestInfo = abEmitBailInterestInfoService.get(id, true);
            dataMap.put("info", abEmitBailInterestInfo);
        }
        dataMap.put("forwardPath", "bill/accebill/updateAbEmitBailInterestInfo");
        return AjaxJson.success(dataMap);
    }

    /**
     * 票据保证金利息维护新增或修改操作
     */
    @LogMessage(content = "票据保证金利息维护列表新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveOrUpdate")
    @ApiOperation("票据保证金利息新增或修改")
    public AjaxJson saveOrUpdate(@RequestBody AbEmitBailInterestInfoVO info) {
        abEmitBailInterestInfoService.saveOrUpdate(info, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 自动计算生成保证金利息记录
     */
    @LogMessage(content = "自动计算生成保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/autoCalculatedInterest")
    @ApiOperation("自动计算生成保证金利息记录")
    public AjaxJson autoCalculatedInterest(@RequestBody AbEmitBailInterestInfoVO vo) {
        abEmitBailInterestInfoService.autoCalculatedInterest(vo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量确认保证金利息记录
     */
    @LogMessage(content = "批量确认保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSure")
    @ApiOperation("批量确认保证金利息记录")
    public AjaxJson batchSure(String ids) {
        abEmitBailInterestInfoService.batchSureOrNotSure(true, ids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量反确认保证金利息记录
     */
    @LogMessage(content = "批量反确认保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchNotSure")
    @ApiOperation("批量反确认保证金利息记录")
    public AjaxJson batchNotSure(String ids) {
        abEmitBailInterestInfoService.batchSureOrNotSure(false, ids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 推送共享保证金利息记录
     */
    @LogMessage(content = "推送共享保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/pushSharing")
    @ApiOperation("推送共享保证金利息记录")
    public AjaxJson pushSharing(String ids) {
        abEmitBailInterestInfoService.pushSharing(ids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除记录
     */
    @PostMapping("/delete/{id}")
    @ApiOperation("删除票据保证金利息记录")
    public AjaxJson delete(@PathVariable("id") String id) {
        abEmitBailInterestInfoService.delete(id);
        return AjaxJson.success();
    }
}