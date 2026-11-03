package com.bytter.finance.saas.controller.equity;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.equity.entity.CmsEquityType;
import com.bytter.finance.domain.equity.service.CmsEquityTypeService;
import com.bytter.finance.domain.equity.vo.CmsEquityTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 股权融资类型Controller
 *
 * @author wangf
 */
@Api(value = "/v1-cms/cms/equity/type", tags = "股权融资类型")
@RestController("v1CmsEquityTypeController")
@RequestMapping("/v1-cms/cms/equity/type")
public class CmsEquityTypeController extends BaseController {
    @Autowired
    private CmsEquityTypeService cmsEquityTypeService;

    /**
     * 获取股权融资类型编辑页初始化数据
     *
     * @param id 股权融资类型id
     */
    @ApiOperation("股权融资类型编辑页面初始化数据")
    @GetMapping("/update/{id}")
    public AjaxJson<CmsEquityType> updatePage(@PathVariable("id") String id) {
        return success(cmsEquityTypeService.get(id, true));
    }

    /**
     * 获取股权融资类型列表数据
     */
    @ApiOperation("股权融资类型分页列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityTypeService.getDataTable(input));
    }

    /**
     * 新增股权融资类型
     *
     * @param cmsEquityType 股权融资类型
     */
    @LogMessage(content = "新增债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("新增股权融资类型")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsEquityType cmsEquityType) {
        cmsEquityTypeService.saveEquityType(cmsEquityType, this.getCurrentUser());
        return success();
    }

    /**
     * 修改股权融资类型
     *
     * @param cmsEquityType 股权融资类型
     */
    @LogMessage(content = "修改股权融资类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("修改股权融资类型")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody CmsEquityType cmsEquityType) {
        cmsEquityTypeService.updateEquityType(cmsEquityType, this.getCurrentUser());
        return success();
    }

    /**
     * 删除股权融资类型
     *
     * @param id 股权融资类型id
     */
    @LogMessage(content = "删除股权融资类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除股权融资类型")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsEquityTypeService.deleteEquityType(id, this.getCurrentUser());
        return success();
    }

    /**
     * 获取有效的股权融资类型集合
     */
    @ApiOperation("查询有效股权融资类型下拉列表")
    @GetMapping("/listCmsEquityType")
    public AjaxJson<List<CmsEquityTypeVo>> listEquityType() {
        return success(cmsEquityTypeService.listEquityType());
    }
}
