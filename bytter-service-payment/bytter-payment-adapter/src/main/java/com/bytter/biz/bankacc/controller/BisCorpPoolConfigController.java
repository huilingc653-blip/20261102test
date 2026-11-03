package com.bytter.biz.bankacc.controller;

import com.bytter.biz.inneraccount.entity.BisCorpPoolConfig;
import com.bytter.biz.inneraccount.service.BisCorpPoolConfigService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigRuleVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
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
@Controller
@RequestMapping("/bankacc/biscorppoolconfig")
public class BisCorpPoolConfigController extends BaseController {
    @Autowired
    private BisCorpPoolConfigService bisCorpPoolConfigService;

    /**
     * 跳转 单位归集余额维护 列表
     *
     * @return
     */
    @LogMessage(content = "查询单位归集余额表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "bankacc/listBisCorpPoolConfig";
    }

    /**
     * 查询 单位归集余额维护 列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisCorpPoolConfigService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 跳转 单位归集配置数据 新增
     *
     * @param bisCorpPoolConfigVO
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(BisCorpPoolConfigVO bisCorpPoolConfigVO, Model model) {
        if (StringUtils.isNotEmpty(bisCorpPoolConfigVO.getId())) {
            BisCorpPoolConfig bisCorpPoolConfigDO = bisCorpPoolConfigService.get(bisCorpPoolConfigVO.getId());
            BeanUtils.copyNotNullProperties(bisCorpPoolConfigDO, bisCorpPoolConfigVO);
            model.addAttribute("bisCorpPoolConfigPage", bisCorpPoolConfigVO);
            model.addAttribute("configCode", bisCorpPoolConfigDO.getConfigCode());
        } else {
            model.addAttribute("configCode", bisCorpPoolConfigService.getConfigRuleCode());
        }
        return "bankacc/addBisCorpPoolConfig";
    }

    /**
     * 跳转 单位归集配置数据 编辑
     *
     * @param bisCorpPoolConfigVO
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String update(BisCorpPoolConfigVO bisCorpPoolConfigVO, Model model) {
        if (StringUtils.isNotEmpty(bisCorpPoolConfigVO.getId())) {
            BisCorpPoolConfig bisCorpPoolConfigDO = bisCorpPoolConfigService.get(bisCorpPoolConfigVO.getId(), true);
            List<BisCorpPoolConfigRuleVO> bisCorpPoolConfigRuleList = bisCorpPoolConfigService.getConfigRuleByConfigId(bisCorpPoolConfigVO.getId());
            int length = bisCorpPoolConfigRuleList.size();
            model.addAttribute("bisCorpPoolConfigRuleList", bisCorpPoolConfigRuleList);
            model.addAttribute("bisCorpPoolConfigPage", bisCorpPoolConfigDO);
            model.addAttribute("length", length);
        }
        return "bankacc/updateBisCorpPoolConfig";
    }

    /**
     * 新增 单位归集配置数据
     *
     * @param bisCorpPoolConfigVO
     * @return
     */
    @LogMessage(content = "新增单位归集配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisCorpPoolConfigVO bisCorpPoolConfigVO, HttpServletRequest request) {
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
    @LogMessage(content = "批量禁用单位归集配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchDisable")
    @ResponseBody
    public AjaxJson batchDisable(@RequestParam("ids") String ids) {
        bisCorpPoolConfigService.batchDisableConfig(ids);
        return success();
    }
}
