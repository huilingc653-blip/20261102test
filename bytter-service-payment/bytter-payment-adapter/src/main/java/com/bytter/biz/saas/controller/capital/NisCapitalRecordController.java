package com.bytter.biz.saas.controller.capital;

import com.bytter.biz.capital.entity.NisCapitalRecord;
import com.bytter.biz.capital.service.NisCapitalRecordService;
import com.bytter.biz.capital.vo.NisCapitalRecordVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shenc
 * @date 2019.3.28
 * 资金备案台账
 */
@Api(value = "/v1-payment/capital/record", tags = "资金备案台账")
@RestController("V1NisCapitalRecordController")
@RequestMapping("/v1-payment/capital/record")
public class NisCapitalRecordController extends BaseController {
    @Autowired
    private NisCapitalRecordService nisCapitalRecordService;

    @ApiOperation("列表查询")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisCapitalRecordVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisCapitalRecordService.getDataTablesUnion(input, getCurrentUser()));
    }

    @ApiOperation("添加页面数据,返回当前用户的单位ID")
    @GetMapping("/add")
    public AjaxJson<String> add() {
        return AjaxJson.success(getCurrentUser().getCorpId());
    }

    @ApiOperation("新增")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisCapitalRecord nisCapitalRecord) {
        nisCapitalRecord.setCreateBy(this.getCurrentUser().getLoginName());
        nisCapitalRecord.setCreateDate(DateUtils.getDate());
        nisCapitalRecordService.saveNisCapitalRecord(nisCapitalRecord);
        return success();
    }

    @ApiOperation("修改页面数据")
    @GetMapping("/update/{id}")
    public AjaxJson<NisCapitalRecordVo> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(nisCapitalRecordService.getNisCapitalRecordVoById(id));
        }
        return AjaxJson.success();
    }

    @ApiOperation("修改")
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody NisCapitalRecord choProductReq) {
        NisCapitalRecord t = nisCapitalRecordService.get(choProductReq.getId());
        choProductReq.setUpdateBy(this.getCurrentUser().getLoginName());
        choProductReq.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(choProductReq, t);
        nisCapitalRecordService.toModify(t);
        return success();
    }

    @ApiOperation("查看")
    @GetMapping("/view/{id}")
    public AjaxJson<NisCapitalRecordVo> view(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(nisCapitalRecordService.getNisCapitalRecordVoById(id));
        }
        return AjaxJson.success();
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public AjaxJson delete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisCapitalRecord record = nisCapitalRecordService.get(id);
            nisCapitalRecordService.deleteEntity(record);
        }
        return success();
    }

    @ApiOperation("保存导入")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<NisCapitalRecordVo> list) {
        if (EmptyUtils.isEmptyList(list)) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        this.nisCapitalRecordService.saveImport(list, getCurrentUser());
        return success();
    }
}
