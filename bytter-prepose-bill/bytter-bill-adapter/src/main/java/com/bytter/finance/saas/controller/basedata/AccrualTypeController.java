package com.bytter.finance.saas.controller.basedata;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.service.AccrualTypeService;
import com.bytter.finance.domain.basedata.vo.AccrualTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mof
 * @version 1.0
 * @Title: 计提方式维护
 * @Description: CMS_ACCRUAL_TYPE
 * @date 2022-08-10 15:47:15
 */
@Api(value = "/v1-cms/accrual/accrualtype", tags = "计提方式维护")
@RestController("v1AccrualTypeController")
@RequestMapping("/v1-cms/cms/accrual/accrualtype")
public class AccrualTypeController extends BaseController {
    @Autowired
    private AccrualTypeService accrualTypeService;

    /**
     * 查询计提方式数据
     **/
    @ApiOperation("查询计提方式列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(accrualTypeService.queryAccrualType(input));
    }

    /**
     * 新增页面数据
     **/
    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<AccrualTypeVO> add(AccrualTypeVO accrualTypeVo) {
        if (StringUtils.isNotEmpty(accrualTypeVo.getId())) {
            accrualTypeVo = accrualTypeService.queryAccrualTypeById(accrualTypeVo.getId());
        }
        return success(accrualTypeVo);
    }

    /**
     * 编辑页面数据
     **/
    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<AccrualTypeVO> update(AccrualTypeVO accrualTypeVo, String type) {
        accrualTypeVo = accrualTypeService.getisInput(accrualTypeVo);
        return success(accrualTypeVo);
    }

    /**
     * 计提方式详情页
     *
     * @return
     */
    @LogMessage(content = "查询计提方式详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询计提方式详情")
    @GetMapping("/view/{id}")
    public AjaxJson<AccrualTypeVO> view(@PathVariable("id") String id) {
        return success(accrualTypeService.queryAccrualTypeById(id));
    }

    /**
     * 增加计提方式
     **/
    @LogMessage(content = "添加CMS_ACCRUAL_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("添加计提方式")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AccrualTypeVO accrualTypeVo) {
        accrualTypeVo.setStatus(BaseDataConstant.STATUS_REDACT);
        accrualTypeService.saveAccrualType(accrualTypeVo, getCurrentUser());
        return success();
    }

    /**
     * 编辑计提方式
     **/
    @LogMessage(content = "修改CMS_ACCRUAL_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改计提方式")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody AccrualTypeVO accrualTypeVo) {
        accrualTypeService.updateAccrualType(accrualTypeVo, getCurrentUser());
        return success();
    }

    /**
     * 删除计提方式
     **/
    @LogMessage(content = "删除CMS_ACCRUAL_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除计提方式")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        // 是否已被引用，否则不可删除
        boolean flag = accrualTypeService.judgeAccrualTypeByCode(id);
        if (!flag) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
        }
        accrualTypeService.deleteAccrualType(id);
        return success();
    }

    /**
     * 获取计提方式
     */
    @ApiOperation("获取计提方式列表")
    @GetMapping("/queryAccrualTypeList")
    public AjaxJson<List<AccrualTypeVO>> queryAccrualTypeList() {
        return success(accrualTypeService.queryAccrualTypeList());
    }

    /**
     * 启用/禁用
     *
     * @param id       主键id
     * @param validSign 启用/禁用状态
     * @return
     */
    @ApiOperation("启用/禁用")
    @PostMapping("/updateValidSign")
    public AjaxJson updateValidSign(String id, String validSign) {
        accrualTypeService.setValidSign(id, validSign);
        return success();
    }
}
