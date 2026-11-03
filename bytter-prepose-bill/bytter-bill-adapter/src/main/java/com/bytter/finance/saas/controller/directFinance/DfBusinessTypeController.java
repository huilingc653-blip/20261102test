package com.bytter.finance.saas.controller.directFinance;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.directFinance.entity.DfBusinessType;
import com.bytter.finance.domain.directFinance.service.DfBusinessTypeService;
import com.bytter.finance.domain.directFinance.vo.DfBusinessTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:39
 * @Version 1.0
 */
@Api(value = "/v1-preposebill/cms/dfBusinessType", tags = "直融业务类型")
@RestController("v1DfBusinessTypeController")
@RequestMapping("/v1-preposebill/cms/dfBusinessType")
public class DfBusinessTypeController extends BaseController {

    @Autowired
    private DfBusinessTypeService dfBusinessTypeService;

    /**
     * 业务类型列表查询
     */
    @LogMessage(content = "查询业务类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询业务类型分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfBusinessTypeService.listBusinessType(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询业务类型详情
     */
    @LogMessage(content = "查询业务类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询业务类型详情")
    public AjaxJson<DfBusinessTypeVO> getById(@PathVariable("id") String id) {
        DfBusinessTypeVO vo = dfBusinessTypeService.getDfBusinessTypeById(id);
        return AjaxJson.success(vo);
    }

    /**
     * 保存业务类型
     */
    @LogMessage(content = "保存业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存业务类型")
    public AjaxJson save(@RequestBody DfBusinessType dfBusinessType) {
        dfBusinessTypeService.saveOrUpdate(dfBusinessType);
        return AjaxJson.success();
    }

    /**
     * 删除业务类型
     */
    @LogMessage(content = "删除业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除业务类型")
    public AjaxJson delete(@PathVariable("id") String id) {
        dfBusinessTypeService.deleteCheck(id);
        return AjaxJson.success();
    }

    /**
     * 数据校验
     */
    @PostMapping("/validate")
    @ApiOperation("校验业务编码是否重复")
    public AjaxJson<Boolean> validate(@RequestParam(required = false) String id, @RequestParam String businessCode) {
        AjaxJson ajaxJson=new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(dfBusinessTypeService.validateBusinessCode(id, businessCode));
        return ajaxJson;
    }

    /**
     * 获取有效的业务类型列表
     */
    @LogMessage(content = "查询有效的业务类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取有效的业务类型列表")
    public AjaxJson<List<DfBusinessTypeVO>> list() {
        List<DfBusinessTypeVO> list = dfBusinessTypeService.listBusinessTypeByParams(null);
        return AjaxJson.success(list);
    }

}