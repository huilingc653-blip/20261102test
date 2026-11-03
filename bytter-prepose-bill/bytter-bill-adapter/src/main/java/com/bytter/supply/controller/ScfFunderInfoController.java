package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.platform.entity.ScfFunderInfo;
import com.bytter.supply.domain.platform.service.ScfFunderInfoService;
import com.bytter.supply.domain.platform.vo.ScfFunderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: ScfFunderInfoController
 * @Description: 资金方
 * @version 1.0
 */
@Api(value = "/v1-supply/supplyChain/funderInfo", tags = "资金方")
@RestController("v1ScfFunderInfoController")
@RequestMapping("/v1-supply/supplyChain/funderInfo")
public class ScfFunderInfoController extends BaseController {

    @Autowired
    private ScfFunderInfoService scfFunderInfoService;

    /**
     * 查询所有资金方列表
     */
    @ApiOperation(value = "查询所有资金方列表")
    @ResponseBody
    @GetMapping("/getFunderJson")
    public AjaxJson<List<ScfFunderInfoVo>> getFunderJson(){
        return success(scfFunderInfoService.getFunderJson());
    }

    /**
     * 资金方 table列表
     */
    @ApiOperation("资金方 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<ScfFunderInfoVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(scfFunderInfoService.listScfFunderInfo(input));
    }

    /**
     * 资金方编辑页面跳转
     */
    @ApiOperation("资金方编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<ScfFunderInfo> update(@PathVariable String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(scfFunderInfoService.get(id));
        }
        return AjaxJson.success();
    }

    /**
     * 资金方详情页
     */
    @ApiOperation("资金方详情页")
    @LogMessage(content = "查询资金方详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<ScfFunderInfo> view(@PathVariable("id") String id) {
        ScfFunderInfo scfFunderInfo = scfFunderInfoService.get(id);
        return AjaxJson.success(scfFunderInfo);
    }

    /**
     * 添加资金方
     */
    @ApiOperation("添加资金方")
    @LogMessage(content = "添加资金方", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ScfFunderInfo scfFunderInfo) {
        // 创建人信息
        if (EmptyUtils.isBlank(scfFunderInfo.getCreateBy())) {
            scfFunderInfo.setCreateBy(this.getCurrentUser().getLoginName());
            scfFunderInfo.setCreateDate(DateUtils.getDate());
        }
        scfFunderInfoService.save(scfFunderInfo);
        return success();
    }

    /**
     * 修改资金方
     */
    @ApiOperation("修改资金方")
    @LogMessage(content = "修改资金方", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody ScfFunderInfo scfFunderInfo) {
        // 修改人信息
        scfFunderInfo.setUpdateBy(this.getCurrentUser().getLoginName());
        scfFunderInfo.setUpdateDate(DateUtils.getDate());
        scfFunderInfoService.update(scfFunderInfo);
        return success();
    }

    /**
     * 删除资金方
     */
    @ApiOperation("删除资金方")
    @LogMessage(content = "删除资金方", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ScfFunderInfo scfFunderInfo = scfFunderInfoService.get(id);
        scfFunderInfoService.deleteEntity(scfFunderInfo);
        return success();
    }


}
