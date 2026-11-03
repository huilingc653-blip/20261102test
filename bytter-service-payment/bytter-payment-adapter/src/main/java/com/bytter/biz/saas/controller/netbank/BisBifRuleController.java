package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.service.BisBifRuleService;
import com.bytter.biz.netbank.vo.BisBifRuleVO;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BisBifRuleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/// /import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 接口代码业务规则表
 * @date 2017-05-17 10:16:20
 */
@Api(value = "/v1-payment/netbank/bisbifrule",tags = "接口代码业务规则维护")
@RestController("v1BisBifRuleController")
@RequestMapping("/v1-payment/netbank/bisbifrule")
public class BisBifRuleController extends BaseController {
    @Autowired
    private BisBifRuleService bisBifRuleService;

    /**
     * 接口代码业务规则表 table列表
     *
     * @return
     */
    @ApiOperation("接口代码业务规则表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BisBifRuleVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisBifRuleService.getDataTablesUnion(input));
    }


    /**
     * 接口代码业务规则表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("接口代码业务规则表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<BisBifRuleDTO> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
         return    AjaxJson.success(bisBifRuleService.getById(id));
        }
        return AjaxJson.success();
    }

    /**
     * 接口代码业务规则表详情页
     *
     * @return
     */
    @ApiOperation("接口代码业务规则表详情页")
    @LogMessage(content = "查询接口代码业务规则表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisBifRuleDTO> view(@PathVariable("id") String id) {
        BisBifRuleDTO bisBifRule = bisBifRuleService.getByIdUnion(id);
        return AjaxJson.success(bisBifRule);
    }




    /**
     * 添加接口代码业务规则表
     *
     * @param bisBifRule
     * @return
     */
    @ApiOperation("添加接口代码业务规则")
    @LogMessage(content = "添加接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BisBifRuleDTO bisBifRule) {
        if (StringUtils.isEmpty(bisBifRule.getBifCode().trim())) {
            throw new ParameterException(NetbankMessageEnum.NIS_BISBIF_MESSAGE_BIFCODE_NULL);
        }
        if (bisBifRuleService.findByBifCode(bisBifRule.getBifCode()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_BISBIF_MESSAGE_BIFCODE_UNIQUE);
        }
        bisBifRule.setCreateBy(this.getCurrentUser().getLoginName());
        bisBifRule.setCreateDate(DateUtils.getDate());
        this.bisBifRuleService.save(bisBifRule);
        return success();
    }

    /**
     * 修改接口代码业务规则表
     *
     * @param bisBifRule
     * @return
     */
    
    @ApiOperation("修改接口代码业务规则表")
    @LogMessage(content = "修改接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody  BisBifRuleDTO bisBifRule) {
        BisBifRuleDTO t = bisBifRuleService.getById(bisBifRule.getId());
        BeanUtils.copyNotNullProperties(bisBifRule, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        bisBifRuleService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除接口代码业务规则表
     *
     * @param id
     * @return
     */
    
    @ApiOperation("删除接口代码业务规则表")
    @LogMessage(content = "删除接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@ApiParam("id") @PathVariable("id") String id) {
        bisBifRuleService.delete(id);
        return success();
    }
    /**
     * 批量删除接口代码业务规则表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除接口代码业务规则表")
    @LogMessage(content = "批量删除接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ApiParam("ids") @RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            bisBifRuleService.delete(id);
        }
        return success();
    }
}
