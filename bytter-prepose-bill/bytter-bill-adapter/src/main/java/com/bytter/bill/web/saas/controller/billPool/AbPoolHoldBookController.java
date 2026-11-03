package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolHoldBookService;
import com.bytter.bill.domain.billPool.vo.AbPoolHoldBookVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 票据池持有账簿信息
 */
@Api(value = "/v1-preposebill/billPool/holdBook", tags = "票据池持有账簿信息")
@RestController("v1AbPoolHoldBookController")
@RequestMapping("/v1-preposebill/billPool/holdBook")
public class AbPoolHoldBookController extends BaseController {

    @Autowired
    private AbPoolHoldBookService abPoolHoldBookService;

    /**
     * 查询票据池持有账簿明细数据
     */
    @ApiOperation("查询票据池持有账簿明细数据")
    @GetMapping("/viewInfo")
    public AjaxJson<AbPoolHoldBookVO> viewInfo(String id) {
        return success(abPoolHoldBookService.getAbPoolHoldBookVOById(id));
    }

    /**
     * 查询票据池持有账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询票据池持有账簿信息列表数据")
    @LogMessage(content = "查询票据池持有账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableQuery")
    public AjaxJson<DataTablesOutput> datatableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolHoldBookService.datatableQuery(input));
    }
}
