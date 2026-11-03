package com.bytter.system.saas.controller.pricing;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.pricing.service.DepositRateReCheckService;
import com.bytter.pricing.vo.DepositRateVO;
import com.bytter.system.pricing.constant.PricingConstant;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author luxy
 * @Title: DepositRateReCheckController
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/23 21:02
 */
@Api(value = "/v1-system/pricing/depositRate/interestRateReCheck")
@RestController("v1DepositRateReCheckController")
@RequestMapping("/v1-system/pricing/depositRate/interestRateReCheck")
public class DepositRateReCheckController extends BaseController {

    @Autowired
    private DepositRateReCheckService depositRateReCheckService;

    /**
     * 查询存款利率待复核列表
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    @LogMessage(content = "查询存款利率待复核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.EQ, "status", PricingConstant.STATUS_RECHECK);
        return AjaxJson.success(depositRateReCheckService.datatable(input, getCurrentUser()));
    }

    /**
     * 进入复核数据页面
     */
    @GetMapping("/reCheck")
    @LogMessage(content = "进入复核存款利率页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DepositRateVO> reCheck(String id) {
        DepositRateVO depositRateVO = depositRateReCheckService.getDepositRateById(id);
        return AjaxJson.success(depositRateVO);
    }

    /**
     * 批量操作
     *
     * @param ids        存款利率的id
     * @param handleType 操作类型：2-通过，-1打回
     */
    @PostMapping("/checkBatch")
    @ResponseBody
    @LogMessage(content = "批量执行复核操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson checkBatch(String ids, String handleType, String returndeScribe) {
        depositRateReCheckService.reCheckBatch(ids, handleType, returndeScribe, this.getCurrentUser());
        return success();
    }
}
