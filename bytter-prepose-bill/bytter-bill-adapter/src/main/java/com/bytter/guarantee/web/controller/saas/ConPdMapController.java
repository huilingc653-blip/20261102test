package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.ConPdMapDtlService;
import com.bytter.guarantee.domain.service.ConPdMapService;
import com.bytter.guarantee.domain.vo.ConPdMapDtlVO;
import com.bytter.guarantee.domain.vo.ConPdMapVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangzy
 * @Title: ConPdMapController
 * @Description: 合同类型要素映射Controller
 * @date 2019/11/8 13:34
 */
@Api(value = "/v1-guarantee/contract/conPdMap", tags = "合同类型要素映射")
@RestController("v1ConPdMapController")
@RequestMapping("/v1-guarantee/contract/conPdMap")
public class ConPdMapController extends BaseController {
    @Autowired
    private ConPdMapService conPdMapService;
    @Autowired
    private ConPdMapDtlService conPdMapDtlService;

    /**
     * 合同类型要素映射 table列表
     */
    @ApiOperation("查询合同类型要素映射列表")
    @LogMessage(content = "查询合同类型要素映射", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conPdMapService.getDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 根据合同类型Id查询关键要素
     */
    @ApiOperation("根据合同类型ID查询关键要素")
    @PostMapping("/listTypeField")
    public AjaxJson<List<ConPdMapDtlVO>> listTypeField(String conTypeId, String pdCode) {
        List<ConPdMapDtlVO> resultList = conPdMapDtlService.listConPdMapDtlVO(pdCode, conTypeId);
        return AjaxJson.success(resultList);
    }

    /**
     * 合同类型关系映射保存或修改
     */
    @ApiOperation("合同类型关系映射保存或修改")
    @LogMessage(content = "合同类型关系映射保存或修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOrUpdate")
    public AjaxJson saveOrUpdateConPdMapDtl(ConPdMapVO info) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conPdMapService.saveOrUpdateConPdMapDtl(info, this.getCurrentUser()));
        ajaxJson.setMsg("操作成功");
        return ajaxJson;
    }

    /**
     * 合同类型关系映射删除
     */
    @ApiOperation("删除合同类型关系映射")
    @LogMessage(content = "合同类型关系映射删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson deleteConPdMap(String id) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conPdMapService.deleteConPdMapAndDtl(id));
        ajaxJson.setMsg("操作成功");
        return ajaxJson;
    }
}