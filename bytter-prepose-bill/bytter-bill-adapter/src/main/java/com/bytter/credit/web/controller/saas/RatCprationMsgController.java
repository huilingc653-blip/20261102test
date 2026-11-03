package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credit.domain.service.RatCprationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luxy
 * @version 1.0
 * @Title:RatCprationMsgController
 * @Description:授信日志控制层
 * @date 2018年11月24日 上午9:13:55
 */
@Api(tags = "授信日志", value = "/v1-cms/cms/rat/ratcprationlog")
@RestController("v1RatCprationMsgController")
@RequestMapping("/v1-cms/cms/rat/ratcprationlog")
public class RatCprationMsgController extends BaseController {
    @Autowired
    RatCprationLogService ratLogService;

    /**
     * 授信日志列表页面数据
     *
     * @param rationId 授信ID
     * @return 授信ID
     */
    @ApiOperation("授信日志列表页面数据")
    @LogMessage(content = "查询RAT_RATION_LOG列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listlog")
    public AjaxJson<String> listLog(String rationId) {
        return success(rationId);
    }

    /**
     * 通过授信ID查询相应的日志信息
     *
     * @param dataTablesInput 查询参数
     * @param rationId        授信ID
     * @return 日志表格数据
     */
    @ApiOperation("查询授信日志数据")
    @PostMapping("/listlogdata")
    public AjaxJson<DataTablesOutput> listLogDate(@RequestBody V1DataTablesInput dataTablesInput,
            @RequestParam String rationId) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ratLogService.listRationMsg(rationId, input));
    }
}
