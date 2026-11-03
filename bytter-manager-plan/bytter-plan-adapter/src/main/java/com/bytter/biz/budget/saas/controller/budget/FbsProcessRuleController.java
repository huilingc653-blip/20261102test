package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsProcessRule;
import com.bytter.biz.budget.service.FbsProcessRuleService;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.vo.FbsProcessRuleVO;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划执行规则表
 * @date 2020-08-31 14:51:48
 */
@Api(value = "/v1-plan/budget/fbsprocessrule", tags = "资金计划执行规则表")
@RestController("v1FbsProcessRuleController")
@RequestMapping("/v1-plan/budget/fbsprocessrule")
public class FbsProcessRuleController extends BaseController {
    @Autowired
    private FbsProcessRuleService fbsProcessRuleService;


    /**
     * 资金计划执行规则表 table列表
     *
     * @return
     */
    @ApiOperation(value = "资金计划执行规则表 table列表")
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fbsProcessRuleService.getDataTables(input));
    }

    /**
     * 资金计划执行规则表编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "资金计划执行规则表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<FbsProcessRule> update(@PathVariable String id) {
        FbsProcessRule data = fbsProcessRuleService.get(id);
        return AjaxJson.success(data);
    }

    /**
     * 资金计划执行规则表详情页
     *
     * @return
     */
    @ApiOperation(value = "资金计划执行规则表详情页")
    @LogMessage(content = "查询资金计划执行规则表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FbsProcessRule> view(@PathVariable("id") String id) {
        FbsProcessRule fbsProcessRule = fbsProcessRuleService.get(id);
        return AjaxJson.success(fbsProcessRule);
    }

    /**
     * 添加资金计划执行规则表
     *
     * @param fbsProcessRule
     * @return
     */
    @ApiOperation(value = "添加资金计划执行规则表")
    @LogMessage(content = "添加资金计划执行规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FbsProcessRule fbsProcessRule) {
        this.dataCheck(fbsProcessRule);
        fbsProcessRule.setCreateBy(this.getCurrentUser().getLoginName());
        fbsProcessRule.setCreateDate(DateUtils.getDate());
        fbsProcessRuleService.saveEntity(fbsProcessRule);
        return success();
    }

    /**
     * 修改资金计划执行规则表
     *
     * @param fbsProcessRule
     * @return
     */
    @ApiOperation(value = "修改资金计划执行规则表")
    @LogMessage(content = "修改资金计划执行规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody FbsProcessRule fbsProcessRule) {
        this.dataCheck(fbsProcessRule);
        FbsProcessRule f = fbsProcessRuleService.get(fbsProcessRule.getId());
        fbsProcessRule.setUpdateBy(this.getCurrentUser().getLoginName());
        fbsProcessRule.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(fbsProcessRule, f);
        fbsProcessRuleService.saveOrUpdate(f);
        return success();
    }

    /**
     * 数据校验
     *
     * @param fbsProcessRule
     * @return
     */
    public boolean dataCheck(FbsProcessRule fbsProcessRule) {
        FbsProcessRuleVO fbsProcessRuleVO = fbsProcessRuleService.isExistProcessRule(fbsProcessRule.getProcessRule(), fbsProcessRule.getId());
        if (fbsProcessRuleVO != null) {
            //执行规则[×××]已存在，请重新输入!
            throw new ParameterException(FbsBudgetMessageEnum.FBS_PROCESS_RULE_MESSAGE_EXIST_PROCESSRULE, fbsProcessRuleVO.getProcessRuleName());
        }
        fbsProcessRuleVO = fbsProcessRuleService.isExistSort(fbsProcessRule.getSort(), fbsProcessRule.getId());
        if (fbsProcessRuleVO != null) {
            //优先级[×××]已存在，请重新输入!
            throw new ParameterException(FbsBudgetMessageEnum.FBS_PROCESS_RULE_MESSAGE_EXIST_SORT, fbsProcessRuleVO.getSort().toString());
        }
        return true;
    }

    /**
     * 删除资金计划执行规则表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除资金计划执行规则表")
    @LogMessage(content = "删除资金计划执行规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FbsProcessRule fbsProcessRule = fbsProcessRuleService.get(id);
        this.fbsProcessRuleService.deleteEntity(fbsProcessRule);
        return success();
    }

    /**
     * 批量启用资金计划执行规则
     *
     * @return
     */
    @LogMessage(content = "批量启用资金计划执行规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchEnable")
    @ResponseBody
    public AjaxJson batchEnable(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        this.fbsProcessRuleService.batchUpdateValidSign(ids, CommonConstant.YES);
        return success();
    }

    /**
     * 批量停用资金计划执行规则
     *
     * @return
     */
    @ApiOperation(value = "批量停用资金计划执行规则")
    @LogMessage(content = "批量停用资金计划执行规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchStop")
    @ResponseBody
    public AjaxJson batchStop(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        this.fbsProcessRuleService.batchUpdateValidSign(ids, CommonConstant.NO);
        return success();
    }
}
