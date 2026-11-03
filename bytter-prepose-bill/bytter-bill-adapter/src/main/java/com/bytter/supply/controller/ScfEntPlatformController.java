package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.supply.domain.platform.entity.ScfEntBankOpen;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.domain.platform.vo.ScfBankOpenVo;
import com.bytter.supply.domain.platform.vo.ScfEntBankOpenVo;
import com.bytter.supply.domain.platform.vo.ScfEntPlatformListVo;
import com.bytter.supply.domain.platform.vo.ScfEntPlatformViewVo;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;

import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: ScfEntPlatformController
 * @Description: 接口开通配置管理
 * @version 1.0
 */
@Api(value = "/v1-supply/supplyChain/entPlatform",tags = "平台直连设置")
@RestController("v1ScfEntPlatformController")
@RequestMapping("/v1-supply/supplyChain/entPlatform")
public class ScfEntPlatformController extends BaseController {

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;

    /**
     * 平台列表
     */
    @ApiOperation("平台列表")
    @PostMapping("/datatable")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public AjaxJson<DataTablesOutput<ScfEntPlatformListVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(scfEntBankOpenService.listPlatform(input));
    }

    /**
     * 平台详情（返回：平台信息 + 全部接口字典项 + 内管端配置 + 企业端已保存配置 + 是否具备直连接口）
     */
    @ApiOperation("平台详情")
    @GetMapping("/view/{entPlatformId}")
    @ResponseBody
    public AjaxJson<ScfEntPlatformViewVo> view(@PathVariable("entPlatformId") String entPlatformId) {
        // 获取平台基本信息
        ScfPlatformVo platform = scfEntBankOpenService.getPlatformById(entPlatformId);
        // 获取全部接口字典项（供前端展示勾选框）
        List<ScfBankOpenVo> allItems = scfEntBankOpenService.getAllDictItems();
        // 获取内管端已配置的接口（从 scf_bank_open 查）
        List<ScfBankOpenVo> bankOpenConfig = scfEntBankOpenService.getBankOpenByPlatformId(entPlatformId);
        // 获取企业端已保存的接口配置（从 scf_ent_bank_open 查）
        List<ScfEntBankOpenVo> configList = scfEntBankOpenService.getConfigByEntPlatformId(entPlatformId);
        ScfEntPlatformViewVo result = new ScfEntPlatformViewVo();
        result.setPlatform(platform);
        result.setAllItems(allItems);
        result.setBankOpenConfig(bankOpenConfig);
        result.setConfigList(configList);
        // 判断是否具备直连接口（除平台直联标记接口外还有其他接口）
        result.setHasDirect(scfEntBankOpenService.hasDirectInterface(configList));
        return success(result);
    }

    /**
     * 保存接口开通配置（先删后插）
     */
    @ApiOperation("保存平台/接口开通配置")
    @LogMessage(content = "保存接口开通配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{entPlatformId}")
    @ResponseBody
    public AjaxJson save(@PathVariable("entPlatformId") String entPlatformId, @RequestBody List<ScfEntBankOpen> configList) {
        scfEntBankOpenService.saveConfig(entPlatformId, configList, getCurrentUser());
        return success();
    }

    /**
     * 修改
     */
    @ApiOperation("修改平台/接口开通配置")
    @LogMessage(content = "修改接口开通配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify/{entPlatformId}")
    @ResponseBody
    public AjaxJson modify(@PathVariable("entPlatformId") String entPlatformId, @RequestBody List<ScfEntBankOpen> configList) {
        scfEntBankOpenService.updateConfig(entPlatformId, configList, getCurrentUser());
        return success();
    }

    /**
     * 删除平台接口开通配置
     */
    @ApiOperation("删除平台/接口开通配置")
    @GetMapping("/delete/{entPlatformId}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("entPlatformId") String entPlatformId) {
        scfEntBankOpenService.deleteConfigByEntPlatformId(entPlatformId);
        return success();
    }
}
