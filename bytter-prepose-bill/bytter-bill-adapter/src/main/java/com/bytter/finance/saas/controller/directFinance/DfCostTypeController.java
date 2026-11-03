package com.bytter.finance.saas.controller.directFinance;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.directFinance.entity.DfCostType;
import com.bytter.finance.domain.directFinance.service.DfCostTypeService;
import com.bytter.finance.domain.directFinance.vo.DfCostTypeVO;
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
@Api(value = "/v1-cms/dfCostType", tags = "费用类型")
@RestController("v1DfCostTypeController")
@RequestMapping("/v1-cms/cms/dfCostType")
public class DfCostTypeController extends BaseController {
    @Autowired
    private DfCostTypeService dfCostTypeService;

    /**
     * 列表查询
     *
     * @param input
     * @return
     */
    @LogMessage(content = "费用类型列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询费用类型列表")
    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput param = input.convertAndInitColumns();
        return success(dfCostTypeService.listCostType(param));
    }

    /**
     * 编辑跳转
     *
     * @param id
     * @return
     */
    @LogMessage(content = "费用类型编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("编辑页面数据")
    @GetMapping("/edit")
    public AjaxJson<DfCostTypeVO> edit(String id) {
        return success(dfCostTypeService.getDfCostTypeById(id));
    }

    /**
     * 保存数据
     *
     * @param dfCostType
     * @return
     */
    @LogMessage(content = "费用类型保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("添加费用类型")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody DfCostType dfCostType) {
        dfCostTypeService.saveOrUpdate(dfCostType);
        return success();
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除费用类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除费用类型")
    @PostMapping("/delete")
    public AjaxJson delete(String id) {
        dfCostTypeService.deleteCheck(id);
        return success();
    }

    /**
     * 数据校验
     *
     * @param id
     * @param costCode
     * @return
     */
    @ApiOperation("校验费用编码")
    @PostMapping("/validate")
    public AjaxJson validate(String id, String costCode) {
        boolean result = dfCostTypeService.validateCostCode(id, costCode);
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(result);
        return ajaxJson;
    }

    /**
     * 费用类型查看
     *
     * @param id
     * @return
     */
    @LogMessage(content = "费用类型查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询费用类型详情")
    @GetMapping("/view")
    public AjaxJson<DfCostTypeVO> view(String id) {
        return success(dfCostTypeService.getDfCostTypeById(id));
    }

    /**
     * 查询生效的费用类型
     *
     * @return
     */
    @ApiOperation("获取生效的费用类型列表")
    @GetMapping("/getValidDfCostType")
    public AjaxJson<List<DfCostTypeVO>> getValidDfCostType() {
        return success(dfCostTypeService.getValidDfcostType());
    }
}
