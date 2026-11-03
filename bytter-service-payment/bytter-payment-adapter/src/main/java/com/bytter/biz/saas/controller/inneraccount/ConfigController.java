package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhengzf
 * @date 2026/1/5
 */
@Api(value = "获取参数配置", tags = "获取参数配置")
@RestController("v1ConfigController")
@RequestMapping("/v1-payment/config")
public class ConfigController extends BaseController {

    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 内部账户管理参数配置
     *
     * @return
     */
    @ApiOperation("获取参数配置")
    @ResponseBody
    @GetMapping("/getConfig/{type}")
    public AjaxJson<Map<String, Object>> fcsConfig(@PathVariable @ApiParam("fcs:内部账户管理参数配置,ist:利息管理参数,fcl:核算系统," +
            "fbs:资金计划,international:信用证开证参数配置条状,receive:信用证收证参数配置条状,fex:外汇管理参数配置,nis:支付结算参数配置,exp:费用报销参数配置," +
            "allot:资金调拨参数配置,wage:代发工资参数配置,pool:资金池参数配置,ele:电子回单参数配置,receivable:收款认领管理参数,tp:第三方支付参数配置列表," +
            "bankacc:银行账户管理参数配置,fundjr:企e金服系统参数配置")
                                                   String type) {
        return AjaxJson.success(configService.listByType(type));
    }
}
