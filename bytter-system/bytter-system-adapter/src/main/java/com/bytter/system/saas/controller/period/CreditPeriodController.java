package com.bytter.system.saas.controller.period;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import com.bytter.system.entity.TbCreditPeriod;
import com.bytter.system.service.CreditPeriodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款期限表
 * @date 2017-07-26 17:50:25
 */
@Api(value = "/v1-system/period/tbcreditperiod", tags = "贷款期限表")
@RestController("v1CreditPeriodController")
@RequestMapping("/v1-system/period/tbcreditperiod")
public class CreditPeriodController extends BaseController {
    @Autowired
    private CreditPeriodService creditPeriodService;

    /**
     * 贷款期限表 table列表
     *
     * @return
     */
    @ApiOperation("查询贷款期限表列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.IS_NULL, "interestRateType", 0);
        return success(creditPeriodService.getDataTables(input));
    }

    /**
     * 本位币贷款期限
     */
    @ApiOperation("查询本位币贷款期限")
    @GetMapping("/allJson")
    public AjaxJson<List<TbCreditPeriod>> getTbCreditPeriod() {
        return success(creditPeriodService.getTbCreditPeriod());
    }

    /**
     * 本位币贷款期限
     */
    @ApiOperation("查询本位币贷款期限(rmb)")
    @GetMapping("/rmbJson")
    public AjaxJson<List<TbCreditPeriod>> getrmbTbCreditPeriod() {
        return success(creditPeriodService.getrmbTbCreditPeriod());
    }

    /**
     * 外币贷款期限
     */
    @ApiOperation("查询外币贷款期限")
    @GetMapping("/foreignJson")
    public AjaxJson<List<TbCreditPeriod>> getForeignTbCreditPeriod() {
        return success(creditPeriodService.getforeignTbCreditPeriod());
    }

    /**
     * 贷款期限表新增页面数据
     *
     * @return
     */
    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<TbCreditPeriod> add(TbCreditPeriod tbCreditPeriod) {
        if (StringUtils.isNotEmpty(tbCreditPeriod.getId())) {
            tbCreditPeriod = creditPeriodService.get(tbCreditPeriod.getId());
        }
        return success(tbCreditPeriod);
    }

    /**
     * 贷款期限表编辑页面数据
     *
     * @return
     */
    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<TbCreditPeriod> update(TbCreditPeriod tbCreditPeriod) {
        if (StringUtils.isNotEmpty(tbCreditPeriod.getId())) {
            tbCreditPeriod = creditPeriodService.get(tbCreditPeriod.getId());
        }
        return success(tbCreditPeriod);
    }

    /**
     * 贷款期限表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款期限表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询贷款期限表详情")
    @GetMapping("/view/{id}")
    public AjaxJson<TbCreditPeriod> view(@PathVariable("id") String id) {
        TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
        return success(tbCreditPeriod);
    }

    /**
     * 添加贷款期限表
     *
     * @param tbCreditPeriod
     * @return
     */
    @LogMessage(content = "添加贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("添加贷款期限表")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody TbCreditPeriod tbCreditPeriod) {
        creditPeriodService.savePeriod(tbCreditPeriod);
        return success();
    }

    /**
     * 修改贷款期限表
     *
     * @param tbCreditPeriod
     * @return
     */
    @LogMessage(content = "修改贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改贷款期限表")
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody TbCreditPeriod tbCreditPeriod) {
        TbCreditPeriod t = creditPeriodService.get(tbCreditPeriod.getId());
        if (!tbCreditPeriod.getCode().equals(t.getCode())) {// 编码发生变化需要验证是否重复
            if (creditPeriodService.findCreditPeriodByCode(tbCreditPeriod.getCode()) != null) {// 期限是否存在
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_CREDITPERIOD_EXIST);
            }
        }
        creditPeriodService.isAllowOp(t);// 是否允许操作验证
        BeanUtils.copyNotNullProperties(tbCreditPeriod, t);
        creditPeriodService.setCreditDays(t);// 设置贷款总天数
        creditPeriodService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除贷款期限表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除贷款期限表")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
        creditPeriodService.isAllowOp(tbCreditPeriod);// 是否允许操作验证
        creditPeriodService.deleteEntity(tbCreditPeriod);
        return success();
    }

    /**
     * 批量删除贷款期限表
     *
     * @param body
     * @return
     */
    @LogMessage(content = "批量删除贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("批量删除贷款期限表")
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        for (String id : ids.split(",")) {
            TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
            creditPeriodService.isAllowOp(tbCreditPeriod);// 是否允许操作验证
            creditPeriodService.deleteEntity(tbCreditPeriod);
        }
        return success();
    }
}
