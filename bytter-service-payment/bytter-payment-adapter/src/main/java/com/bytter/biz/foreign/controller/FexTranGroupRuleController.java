package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexTranGroupRule;
import com.bytter.biz.foreign.entity.FexTranGroupRuleDtl;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.service.FexTranGroupRuleDtlService;
import com.bytter.biz.foreign.service.FexTranGroupRuleService;
import com.bytter.biz.foreign.vo.FexTranGroupRuleDtlVO;
import com.bytter.biz.foreign.vo.FexTranGroupRuleVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author qinl
 * @version 1.0
 * @description: 外汇交易组规则
 * @date 2020-02-18
 */
@Controller
@RequestMapping("/foreign/fextrangrouprule")
public class FexTranGroupRuleController extends BaseController {
    @Autowired
    private FexTranGroupRuleService fexTranGroupRuleService;
    @Autowired
    private FexTranGroupRuleDtlService fexTranGroupRuleDtlService;
    @Autowired
    private FexProductService fexProductService;

    /**
     * <p>
     * （页面）查询页面
     * </p>
     *
     * @return 页面
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUPRULE_APPLY)
    @GetMapping("/list")
    public String list() {
        return "foreign/listFexTranGroupRule";
    }

    /**
     * <p>
     * （页面）新增或编辑
     * </p>
     *
     * @param id [id]
     * @return 页面
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUPRULE_APPLY)
    @GetMapping("/edit")
    public String edit(Model model, String id) {
        if (StringUtils.isBlank(id)) {
            return "foreign/addFexTranGroupRule";
        }
        FexTranGroupRuleDtlVO detailsById = fexTranGroupRuleDtlService.getDetailsById(id);
        FexTranGroupRule fexTranGroupRule = fexTranGroupRuleService.get(detailsById.getRuleId());
        model.addAttribute("fexTranGroupRuleVO", fexTranGroupRule);
        return "foreign/updateFexTranGroupRule";
    }

    /**
     * <p>
     * （页面）查询页面
     * </P>
     *
     * @return view
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUPRULE_APPLY)
    @GetMapping("/view/{id}")
    public String view(Model model, @PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("fexTranGroupRuleDtlVO", new FexTranGroupRuleDtlVO());
            model.addAttribute("fexTranGroupRuleVO", new FexTranGroupRuleVO());
        }
        FexTranGroupRuleDtlVO detailsById = fexTranGroupRuleDtlService.getDetailsById(id);
        FexTranGroupRule fexTranGroupRule = fexTranGroupRuleService.get(detailsById.getRuleId());
        model.addAttribute("fexTranGroupRuleDtlVO", null);
        model.addAttribute("fexTranGroupRuleVO", fexTranGroupRule);
        return "foreign/viewFexTranGroupRule";
    }

    /**
     * <p>
     * （操作）删除
     * </p>
     *
     * @param id [id]
     * @return 执行情况
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson edit(@PathVariable String id) {
        FexTranGroupRuleDtl fexTranGroupRuleDtl = fexTranGroupRuleDtlService.get(id);
        List<FexTranGroupRuleDtlVO> fexTranGroupRuleDtlVOS = fexTranGroupRuleDtlService.listDetailsById(fexTranGroupRuleDtl.getRuleId());
        if (fexTranGroupRuleDtlVOS.size() == 1) {
            if (fexProductService.countByTranGroupRuleId(fexTranGroupRuleDtl.getRuleId()) > 0) {
                throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
            }

            fexTranGroupRuleService.delete(fexTranGroupRuleDtl.getRuleId());
        }
        fexTranGroupRuleDtlService.delete(id);
        return success();
    }

    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexTranGroupRuleVO fexTranGroupRuleVO) {
        fexTranGroupRuleService.saveRuleAndDetails(fexTranGroupRuleVO);
        return success();
    }

    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(FexTranGroupRuleVO fexTranGroupRuleVO) {
        fexTranGroupRuleService.updateRuleAndDetails(fexTranGroupRuleVO);
        return success();
    }

    /**
     * <p>
     * （数据）外汇交易组规则页面数据源
     * </p>
     *
     * @param dataTablesInput [查询条件]
     * @return DataTablesOutput
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexTranGroupRuleDtlService.getDataTable(dataTablesInput);
    }

    @PostMapping("/detail/{ruleId}")
    @ResponseBody
    public List<FexTranGroupRuleDtlVO> ruleDetails(@PathVariable String ruleId) {
        return fexTranGroupRuleDtlService.listDetailsById(ruleId);
    }

    /**
     * <p>
     * （数据）外汇交易组规则列表数据源
     * </p>
     *
     * @return List<FexTranGroupRule>
     */
    @GetMapping("/json")
    @ResponseBody
    public List<FexTranGroupRule> json() {
        return fexTranGroupRuleService.listByValidSign(true);
    }
}
