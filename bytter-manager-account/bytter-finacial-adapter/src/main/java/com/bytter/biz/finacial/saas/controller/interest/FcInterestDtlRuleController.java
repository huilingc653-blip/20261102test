package com.bytter.biz.finacial.saas.controller.interest;

import com.bytter.biz.finacial.interest.service.FcInterestDtlRuleService;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlRuleVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 利息流水规则控制器。
 *
 * @author zhaohc
 * @date 2024/4/29 16:30
 */
@Api(value = "/v1-finacial/finacial/interest/fcinterestdtlrule", tags = "利息流水规则维护")
@RestController("V1FcInterestDtlRuleController")
@RequestMapping("/v1-finacial/finacial/interest/fcinterestdtlrule")
public class FcInterestDtlRuleController extends BaseController {

    @Autowired
    private FcInterestDtlRuleService fcInterestDtlRuleService;


    /**
     * 利息流水规则表列表 table列表
     */
    @ApiOperation("利息流水规则表列表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcInterestDtlRuleVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns( );
        return AjaxJson.success(fcInterestDtlRuleService.listDataTable(input));
    }

    /**
     * 添加利息流水规则表
     */
    @ApiOperation("添加利息流水规则表")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody FcInterestDtlRuleVo interestDtlRuleVo) {
        fcInterestDtlRuleService.saveOrModify(interestDtlRuleVo);
        return success();
    }

    /**
     * 利息流水规则表编辑页面跳转
     */
    @ApiOperation("利息流水规则表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson  <FcInterestDtlRuleVo> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(fcInterestDtlRuleService.getInterestRuleById(id));
        }
        return AjaxJson.success();
    }

    /**
     * 利息流水规则表详情页
     */
    @ApiOperation("利息流水规则表详情页")
    @GetMapping("/view/{id}")
    public AjaxJson<FcInterestDtlRuleVo> view(@PathVariable("id") String id) {
        FcInterestDtlRuleVo interestDtlRule = fcInterestDtlRuleService.getInterestRuleById(id);
        return AjaxJson.success(interestDtlRule);
    }

    /**
     * 删除利息流水规则表
     */
    @ApiOperation("删除利息流水规则表")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        FcInterestDtlRuleVo interestDtlRule = fcInterestDtlRuleService.getInterestRuleById(id);
        fcInterestDtlRuleService.deleteInterestDtlRule(interestDtlRule);
        return success();
    }

}
