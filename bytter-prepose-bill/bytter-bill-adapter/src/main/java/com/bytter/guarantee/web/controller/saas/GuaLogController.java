package com.bytter.guarantee.web.controller.saas;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.GuaLogService;
import com.bytter.system.api.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Api(tags = "担保日志",value = "/v1-guarantee/cms/guarantee/gualog")
@RestController("v1GuaLogController")
@RequestMapping("/v1-guarantee/cms/guarantee/gualog")
public class GuaLogController extends BaseController {
    @Autowired
    private GuaLogService guaLogService;

    /**
     * 通过担保ID查询相应的日志信息
     *
     * @param input
     * @param bondId
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "通过担保ID查询相应的日志信息")
    @PostMapping("/listlogdata")
    @ResponseBody
    public AjaxJson<DataTablesOutput> listGuaLogInfo(@RequestBody V1DataTablesInput input, String guaId) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(guaLogService.listGuaLog(guaId, dataTablesInput));
    }
}
