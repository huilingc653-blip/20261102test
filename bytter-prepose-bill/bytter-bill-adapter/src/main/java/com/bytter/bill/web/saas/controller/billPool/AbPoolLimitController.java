package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolLimitService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhengg
 * @version 1.0
 * @Title: Controller
 * @Description: 票据池额度信息控制器
 * @date 2020-03-13 15:25:10
 *
 */
@Api(value = "/v1-preposebill/billPool/limitInfo", tags = "票据池额度信息控制器")
@RestController("v1AbPoolLimitController")
@RequestMapping("/v1-preposebill/billPool/limitInfo")
public class AbPoolLimitController extends BaseController {
    @Autowired
    private AbPoolLimitService abPoolLimitService;

    /**
     * 跳转查询明细页面
     *
     * @return
     */
    @ApiOperation("跳转查询明细页面")
    @GetMapping("/view/id")
    public AjaxJson<AbPoolLimitVO> view(@PathVariable String id) {
        return AjaxJson.success(abPoolLimitService.getAbPoolLimitVOById(id));
    }

    /**
     * "查询票据池额度信息列表数据
     *
     * @return
     */
    @ApiOperation("查询票据池额度信息列表数据")
    @PostMapping("/datatableQuery")
    @LogMessage(content = "查询票据池额度信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public AjaxJson<DataTablesOutput<AbPoolLimitVO>> datatableQuery(@RequestBody DataTablesInput dataTablesInput) {
        return AjaxJson.success(abPoolLimitService.datatableQuery(dataTablesInput));
    }

    /**
     * 根据单位ID和票据池ID或者账号查询额度信息
     *
     * @param corpId  单位ID
     * @param bpId    票据池ID
     * @param bankAcc 银行账号
     * @return 额度信息
     */
    @ApiOperation("根据单位ID和票据池ID或者账号查询额度信息")
    @PostMapping("/getLimitInfo")
    public AjaxJson<AbPoolLimitVO> getLimitInfo(String corpId, String bpId, String bankAcc) {
        return AjaxJson.success(abPoolLimitService.getLimitInfo(corpId, bpId, bankAcc));
    }
}
