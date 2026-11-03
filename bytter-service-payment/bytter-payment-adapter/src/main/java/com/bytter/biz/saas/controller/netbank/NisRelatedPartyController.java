package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.NisRelatedParty;
import com.bytter.biz.netbank.service.NisRelatedPartyService;
import com.bytter.biz.netbank.vo.NisRelatedPartyReportVO;
import com.bytter.biz.netbank.vo.NisRelatedPartyVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 关联方维护与报表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/17
 */
@Api(value = "/v1-payment/netbank/relatedParty", tags = "关联方维护")
@RestController("v1NisRelatedPartyController")
@RequestMapping("/v1-payment/netbank/relatedParty")
public class NisRelatedPartyController extends BaseController {
    @Autowired
    private NisRelatedPartyService nisRelatedPartyService;

    @ApiOperation("查询列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisRelatedParty>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisRelatedPartyService.getDataTables(input, false));
    }

    @ApiOperation("修改")
    @GetMapping("/update")
    public AjaxJson<NisRelatedParty> update(@RequestBody NisRelatedParty entity) {
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = nisRelatedPartyService.get(entity.getId(), true);
            return AjaxJson.success(entity);
        }
        return AjaxJson.success();
    }

    @ApiOperation("报表查询")
    @PostMapping("/reportDatatable")
    public AjaxJson<DataTablesOutput<NisRelatedPartyReportVO>> reportDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisRelatedPartyService.reportDatatable(input, getCurrentUser()));
    }


    @ApiOperation("添加")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisRelatedPartyVO vo) {
        nisRelatedPartyService.save(vo, this.getCurrentUser());
        return success();
    }

    @ApiOperation("修改")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody NisRelatedPartyVO vo) {
        nisRelatedPartyService.update(vo, this.getCurrentUser());
        return success();
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        nisRelatedPartyService.delete(id);
        return success();
    }

    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        if (ObjectUtil.isNotEmpty(ids)) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                nisRelatedPartyService.delete(id);
            }
        }
        return success();
    }

    @ApiOperation("保存导入")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisRelatedPartyVO> list) {
        nisRelatedPartyService.saveImport(list, this.getCurrentUser());
        return success();
    }


}
