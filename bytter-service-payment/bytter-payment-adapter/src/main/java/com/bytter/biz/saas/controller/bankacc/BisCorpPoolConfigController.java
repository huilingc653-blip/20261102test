package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.inneraccount.entity.BisCorpPoolConfig;
import com.bytter.biz.inneraccount.service.BisCorpPoolConfigService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigRuleVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigVO;
import com.bytter.biz.inneraccount.vo.v1.BisCorpPoolConfigPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xiay
 * @version 1.0
 * @Title: Controller
 * @Description: 多级单位余额核算-单位归集配置表
 * @date 2025年6月23日10:03:04
 */
@Api(value = "多级单位余额核算-单位归集配置表",tags = "多级单位余额核算-单位归集配置表")
@RestController("v1BisCorpPoolConfigController")
@RequestMapping("/v1-payment/bankacc/biscorppoolconfig")
public class BisCorpPoolConfigController extends BaseController {
    @Autowired
    private BisCorpPoolConfigService bisCorpPoolConfigService;

    /**
     * 查询 单位归集余额维护 列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询 单位归集配置维护 列表数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisCorpPoolConfigVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisCorpPoolConfigService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 跳转 单位归集配置数据 新增
     *
     * @return
     */
    @ApiOperation("跳转 单位归集配置数据 新增数据获取")
    @GetMapping("/getInfoBeforeAdd")
    @ResponseBody
    public AjaxJson<BisCorpPoolConfigPageVO> add() {
        BisCorpPoolConfigPageVO pageVO = new BisCorpPoolConfigPageVO();
        pageVO.setConfigCode(bisCorpPoolConfigService.getConfigRuleCode());
        return success(pageVO);
    }

    /**
     * 跳转 单位归集配置数据 编辑
     *
     * @return
     */
    @ApiOperation("在跳转更新页面前 获取对应的内容信息")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<BisCorpPoolConfigPageVO> getInfoBeforeUpdate(String id) {
        if (StringUtils.isNotEmpty(id)) {
            BisCorpPoolConfig bisCorpPoolConfigDO = bisCorpPoolConfigService.get(id, true);
            List<BisCorpPoolConfigRuleVO> bisCorpPoolConfigRuleList = bisCorpPoolConfigService.getConfigRuleByConfigId(id);
            int length = bisCorpPoolConfigRuleList.size();
            BisCorpPoolConfigPageVO pageVO = new BisCorpPoolConfigPageVO();
            pageVO.setBisCorpPoolConfigRuleList(bisCorpPoolConfigRuleList);
            pageVO.setBisCorpPoolConfigPage(bisCorpPoolConfigDO);
            pageVO.setLength(length);
            return success(pageVO);
        }
        return success();
    }

    /**
     * 新增 单位归集配置数据
     *
     * @param bisCorpPoolConfigVO
     * @return
     */
    @ApiOperation("新增单位归集配置")
    @LogMessage(content = "新增单位归集配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisCorpPoolConfigVO bisCorpPoolConfigVO, HttpServletRequest request) {
        BisCorpPoolConfig bisCorpPoolConfig = new BisCorpPoolConfig();
        BeanUtils.copyNotNullProperties(bisCorpPoolConfigVO, bisCorpPoolConfig);
        bisCorpPoolConfig.setCreateBy(this.getCurrentUser().getLoginName());
        bisCorpPoolConfig.setCreateTime(DateUtils.getDate());
        bisCorpPoolConfigService.save(bisCorpPoolConfig, request);
        return success();
    }

    /**
     * 编辑 单位归集配置数据
     *
     * @param bisCorpPoolConfigVO
     * @return
     */
    @ApiOperation("修改单位归集配置")
    @LogMessage(content = "修改单位归集配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisCorpPoolConfigVO bisCorpPoolConfigVO, HttpServletRequest request) {
        BisCorpPoolConfig bisCorpPoolConfigDO = bisCorpPoolConfigService.get(bisCorpPoolConfigVO.getId());
        BeanUtils.copyNotNullProperties(bisCorpPoolConfigVO, bisCorpPoolConfigDO);
        bisCorpPoolConfigDO.setUpdateBy(this.getCurrentUser().getLoginName());
        bisCorpPoolConfigDO.setUpdateTime(DateUtils.getDate());
        bisCorpPoolConfigService.saveOrUpdate(bisCorpPoolConfigDO, request);
        return success();
    }

    /**
     * 删除单位归集配置表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除单位归集配置表")
    @LogMessage(content = "删除单位归集配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BisCorpPoolConfig bisCorpPoolConfig = bisCorpPoolConfigService.get(id);
        bisCorpPoolConfigService.deleteBisCorpPoolConfig(bisCorpPoolConfig);
        return success();
    }

    /**
     * 批量删除单位归集配置表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除单位归集配置表")
    @LogMessage(content = "批量删除单位归集配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        bisCorpPoolConfigService.batchDeleteConfig(ids);
        return success();
    }

    /**
     * 批量禁用单位归集配置表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量禁用单位归集配置表")
    @LogMessage(content = "批量禁用单位归集配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchDisable")
    @ResponseBody
    public AjaxJson batchDisable(@RequestParam("ids") String ids) {
        bisCorpPoolConfigService.batchDisableConfig(ids);
        return success();
    }
}
