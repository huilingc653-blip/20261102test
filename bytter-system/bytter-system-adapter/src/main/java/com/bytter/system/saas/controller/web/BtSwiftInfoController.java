package com.bytter.system.saas.controller.web;

import com.bytter.basic.service.BtSwiftInfoService;
import com.bytter.basic.vo.BtSwiftInfoVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 境外银行行号信息维护
 *
 * @author qinl
 * @date 2019-07-25
 */
@Api(value = "/v1-system/basic/business/btswiftinfo", tags = "境外银行行号信息维护")
@RestController("v1BtSwiftInfoController")
@RequestMapping("/v1-system/basic/business/btswiftinfo")
public class BtSwiftInfoController extends BaseController {
    /**
     * 境外银行行号信息
     */
    @Autowired
    private BtSwiftInfoService btSwiftInfoService;

    /**
     * <p>
     * （数据）境外银行信息列表数据源
     * </p>
     *
     * @param dataTablesInput [封装数据]
     * @return DataTablesOutput
     */
    @ApiOperation(value = "境外银行信息列表数据源")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btSwiftInfoService.getDataTablesUnion(input));
    }
    /**
     * <p>
     * （数据）境外银行信息列表数据源(选择组件，无需权限控制)
     * </p>
     *
     * @param dataTablesInput [封装数据]
     * @return DataTablesOutput
     */
    @ApiOperation("（数据）境外银行信息列表数据源(选择组件，无需权限控制)")
    @PostMapping("/dataTableSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btSwiftInfoService.getDataTablesUnion(input));
    }
}
