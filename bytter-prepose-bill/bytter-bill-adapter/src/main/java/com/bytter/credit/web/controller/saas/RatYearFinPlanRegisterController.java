package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatYearFinPlanRegister;
import com.bytter.credit.domain.service.RatYearFinPlanRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 年度融资规划登记
 *
 * @author fangcf
 * @date 2024-06-28
 */
@Api(tags = "年度融资规划登记", value = "/v1-cms/cms/rat/yearFinPlanRegister")
@RestController("v1RatYearFinPlanRegisterController")
@RequestMapping("/v1-cms/cms/rat/yearFinPlanRegister")
public class RatYearFinPlanRegisterController extends BaseController {

    @Autowired
    private RatYearFinPlanRegisterService ratYearFinPlanRegisterService;

    /**
     * 年度融资规划登记 table列表
     *
     * @param dataTablesInput 入参对象
     * @return 出参对象
     */
    @ApiOperation("查询年度融资规划登记表格数据")
    @LogMessage(content = "查询年度融资规划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ratYearFinPlanRegisterService.listOutputData(input, getUserId()));
    }

    /**
     * 年度融资规划登记编辑页面数据
     *
     * @param entity 年度融资规划登记对象
     * @return 出参对象
     */
    @ApiOperation("年度融资规划登记编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatYearFinPlanRegister> updatePage(RatYearFinPlanRegister entity) {
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = ratYearFinPlanRegisterService.get(entity.getId(), true);
        }
        return success(entity);
    }

    /**
     * 新增年度融资规划登记
     *
     * @param entity 年度融资规划登记对象
     * @return 出参对象
     */
    @ApiOperation("新增年度融资规划登记")
    @LogMessage(content = "新增年度融资规划登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatYearFinPlanRegister entity) {
        ratYearFinPlanRegisterService.saveYearFinPlanRegister(entity, getCurrentUser());
        return success();
    }

    /**
     * 删除年度融资规划登记
     *
     * @param id 主键id
     * @return 出参对象
     */
    @ApiOperation("删除年度融资规划登记")
    @LogMessage(content = "删除年度融资规划登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratYearFinPlanRegisterService.deleteYearFinPlanRegister(id);
        return success();
    }

    /**
     * 修改年度融资规划登记
     *
     * @param entity 年度融资规划登记对象
     * @return 出参对象
     */
    @ApiOperation("修改年度融资规划登记")
    @LogMessage(content = "修改年度融资规划登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatYearFinPlanRegister entity) {
        ratYearFinPlanRegisterService.updateYearFinPlanRegister(entity, getCurrentUser());
        return success();
    }

    /**
     * 年度融资规划登记导入
     *
     * @param list 导入数据集合
     * @return 出参对象
     */
    @ApiOperation("年度融资规划登记导入")
    @LogMessage(content = "年度融资规划登记导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<RatYearFinPlanRegister> list) {
        ratYearFinPlanRegisterService.saveImport(list, getCurrentUser());
        return success();
    }
}
