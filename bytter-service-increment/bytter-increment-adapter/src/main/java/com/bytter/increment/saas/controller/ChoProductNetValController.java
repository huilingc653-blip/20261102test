package com.bytter.increment.saas.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.service.ChoProductNetValService;
import com.bytter.increment.domain.service.ChoProductReqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 理财产品净值维护Controller
 *
 * @author ouyj
 */
@Api(value = "理财产品净值维护", tags = "理财产品净值维护")
@RestController("v1ChoProductNetValController")
@RequestMapping("/v1-increment/chowmatistic/choproductNetVal")
public class ChoProductNetValController extends BaseController {
    @Autowired
    private ChoProductNetValService choProductNetValService;
    @Autowired
    private ChoProductReqService choProductReqService;

    @ApiOperation(value = "查询净值维护列表", notes = "查询净值维护列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductNetValService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    @ApiOperation(value = "查询净值维护详情列表", notes = "查询净值维护详情列表")
    @PostMapping("/editDataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> editDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductNetValService.getEditDataTable(input));
    }

    @ApiOperation(value = "获取净值编辑数据", notes = "获取净值编辑数据")
    @GetMapping("/update")
    public AjaxJson<ChoProductNetVal> update(String id) {
        ChoProductNetVal netVal = null;
        if (StringUtils.isNotEmpty(id)) {
            netVal = choProductNetValService.get(id);
        }
        return success(netVal);
    }

    @ApiOperation(value = "获取净值详情", notes = "获取净值详情")
    @GetMapping("/view")
    public AjaxJson<ChoProductNetVal> view(String id) {
        ChoProductNetVal netVal = null;
        if (StringUtils.isNotEmpty(id)) {
            netVal = choProductNetValService.get(id);
        }
        return success(netVal);
    }

    @ApiOperation(value = "保存净值维护", notes = "保存净值维护")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ChoProductNetVal choProductNetVal) {
        choProductNetVal.setCreateBy(this.getCurrentUser().getLoginName());
        choProductNetVal.setCreateDate(DateUtils.getDate());
        choProductNetValService.save(choProductNetVal);
        return success();
    }

    @ApiOperation(value = "修改净值维护", notes = "修改净值维护")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProductNetVal choProductNetVal) {
        this.choProductNetValService.modify(choProductNetVal, this.getCurrentUser());
        return success();
    }

    @ApiOperation(value = "删除净值维护", notes = "删除净值维护")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.choProductNetValService.delete(id);
        return success();
    }

    @ApiOperation(value = "查询赎回余额", notes = "查询赎回余额")
    @PostMapping("/queryRedeemBal")
    @ResponseBody
    public AjaxJson queryRedeemBal(@RequestBody ChoProductNetVal choProductNetVal) {
        return this.choProductNetValService.queryRedeemBal(choProductNetVal);
    }
}
