package com.bytter.system.saas.controller.rate;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.TbBaseRate;
import com.bytter.system.service.BaseRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 基准利率表
 * @date 2017-07-26 17:33:59
 */
@Api(value = "/v1-system/rate/tbbaserate", tags = "基准利率表")
@RestController("v1BaseRateController")
@RequestMapping("/v1-system/rate/tbbaserate")
public class BaseRateController extends BaseController {
    @Autowired
    private BaseRateService baseRateService;

    /**
     * 基准利率表 table列表
     *
     * @return
     */
    @ApiOperation("查询基准利率表列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(baseRateService.getDataTables(input));
    }

    /**
     * 基准利率表新增页面数据
     *
     * @return
     */
    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<TbBaseRate> add(TbBaseRate tbBaseRate) {
        if (StringUtils.isNotEmpty(tbBaseRate.getId())) {
            tbBaseRate = baseRateService.get(tbBaseRate.getId());
        }
        return success(tbBaseRate);
    }

    /**
     * 基准利率表编辑页面数据
     *
     * @return
     */
    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<TbBaseRate> updatePage(TbBaseRate tbBaseRate) {
        if (StringUtils.isNotEmpty(tbBaseRate.getId())) {
            tbBaseRate = baseRateService.get(tbBaseRate.getId());
        }
        return success(tbBaseRate);
    }

    /**
     * 基准利率表详情页
     *
     * @return
     */
    @LogMessage(content = "查询基准利率表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询基准利率表详情")
    @GetMapping("/view/{id}")
    public AjaxJson<TbBaseRate> view(@PathVariable("id") String id) {
        TbBaseRate tbBaseRate = baseRateService.get(id);
        return success(tbBaseRate);
    }

    /**
     * 添加基准利率表
     *
     * @param tbBaseRate
     * @return
     */
    @LogMessage(content = "添加基准利率表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("添加基准利率表")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody TbBaseRate tbBaseRate) {
        baseRateService.save(tbBaseRate);
        return success();
    }

    /**
     * 修改基准利率表
     *
     * @param tbBaseRate
     * @return
     */
    @LogMessage(content = "修改基准利率表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改基准利率表")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody TbBaseRate tbBaseRate) {
        TbBaseRate t = baseRateService.get(tbBaseRate.getId());
        BeanUtils.copyNotNullProperties(tbBaseRate, t);
        baseRateService.doUpdate(t, tbBaseRate.getId());
        return success();
    }

    /**
     * 删除基准利率表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除基准利率表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除基准利率表")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        baseRateService.doDelete(id);
        return success();
    }

    /**
     * 获取基准利率列表
     *
     * @return
     */
    @ApiOperation("获取基准利率列表")
    @GetMapping("/json")
    public AjaxJson<List<TbBaseRate>> getListByValidSign() {
        return success(baseRateService.getListTbBaseRate());
    }
}
