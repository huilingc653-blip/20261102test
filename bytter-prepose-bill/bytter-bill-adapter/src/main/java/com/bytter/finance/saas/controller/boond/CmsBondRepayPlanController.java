package com.bytter.finance.saas.controller.boond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.service.*;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券还款计划登记
 * @date 2022-06-06 10:07:46
 */
@Api(value = "/v1-preposebill/cms/bond/cmsbondrepayplan", tags = "债券还款计划登记管理")
@RestController("v1CmsBondRepayPlanController")
@RequestMapping("/v1-preposebill/cms/bond/cmsbondrepayplan")
public class CmsBondRepayPlanController extends BaseController {
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;

    /**
     * 债券还款计划登记列表页面初始化参数
     */
    @LogMessage(content = "查询债券还款计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券还款计划列表页面初始化参数")
    @GetMapping("/list/{isSaveSign}")
    public AjaxJson<Map<String, Object>> list(@PathVariable("isSaveSign") String isSaveSign) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSaveSign", isSaveSign);
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券还款计划登记 table列表
     */
    @ApiOperation("债券还款计划分页列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(String isSaveSign, @RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsBondRepayPlanService.getDataTablesUnion(input, isSaveSign);
        return AjaxJson.success(output);
    }

    /**
     * 债券还款计划登记表新增页面初始化数据
     */
    @ApiOperation("债券还款计划新增页面初始化数据")
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(String registerId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(registerId)) {
            CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            resultMap.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                // 债券发行信息
                CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
                resultMap.put("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
                // 债券计划主表信息
                CmsBondRepayPlanVo planVO = cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfoVo.getId());
                if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    planVO.setRepayType(CmsConstant.REPAY_TYPE_07);
                    resultMap.put("cmsBondRepayPlanVo", planVO);
                }
            }
        }
        return AjaxJson.success(resultMap);
    }

    /**
     * 新增/修改-保存债券还款计划修改数据
     */
    @LogMessage(content = "新增/修改-保存LeaseCapitalPlan", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存或更新债券还款计划")
    @PostMapping("/save/{operator}")
    public AjaxJson saveOrUpdate(@PathVariable("operator") String operator,@RequestBody CmsBondRepayPlanVo cmsBondRepayPlanVo) throws ParseException {
        AjaxJson json = AjaxJson.success();
        json.setObj(cmsBondRepayPlanService.saveOrUpdate(operator, cmsBondRepayPlanVo, getCurrentUser()));
        return json;
    }

    /**
     * 债券还款计划登记表详情页初始化数据
     */
    @LogMessage(content = "还款计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("还款计划查看页面初始化数据")
    @GetMapping("/view/{registerId}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("registerId") String registerId) {
        Map<String, Object> resultMap = new HashMap<>();
        CmsBondRegisterInfoVo cmsBondRegisterInfoVo = new CmsBondRegisterInfoVo();
        CmsBondIssueInfoVo cmsBondIssueInfoVo = new CmsBondIssueInfoVo();
        if (StringUtils.isNotEmpty(registerId)) {
            cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
        }
        resultMap.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
        resultMap.put("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
        return AjaxJson.success(resultMap);
    }

    /**
     * 根据注册id查询还款计划明细
     */
    @ApiOperation("根据债券注册ID查询还款计划明细")
    @PostMapping("/listPlanDtlByRegisterId")
    public AjaxJson<List<CmsBondRepayPlanDtlVo>> listPlanDtlByRegisterId(String registerId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.getByRegisterId(registerId);
        return AjaxJson.success(list);
    }

    /**
     * 根据发行Id查询还款计划明细
     */
    @ApiOperation("根据债券发行ID查询还款计划明细")
    @PostMapping("/listPlanDtlByIssueId")
    public AjaxJson<List<CmsBondRepayPlanDtlVo>> listPlanDtlByIssueId(String issueId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.listPlanDtlByIssueId(issueId);
        return AjaxJson.success(list);
    }

    /**
     * 还款计划明细弹窗初始化数据
     */
    @ApiOperation("还款计划明细弹窗初始化数据")
    @GetMapping("/getPlanDtlList")
    public AjaxJson<Map<String, Object>> getPlanDtlList(String registerId, String planDtlId, String showSign) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("showSign", showSign);
        resultMap.put("interestID", planDtlId);
        resultMap.put("registerId", registerId);
        String issueId = "";
        String bondIssueNum = "";
        List<CmsBondIssueInfoVo> cmsBondIssueInfoVos = cmsBondIssueInfoService.getRegisterById(registerId);
        if (!ListUtils.isNullOrEmpty(cmsBondIssueInfoVos)) {
            CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoVos.get(0);
            issueId = cmsBondIssueInfoVo.getId();
            bondIssueNum = cmsBondIssueInfoVo.getBondIssueNum();
        }
        resultMap.put("issueId", issueId);
        resultMap.put("bondIssueNum", bondIssueNum);
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.getByRegisterId(registerId);
        for (CmsBondRepayPlanDtlVo vo : list) {
            // 最大输入金额控制
            BigDecimal sendBalance = cmsBondPayService.completeSendBalance(vo.getIssueId(), null);
            vo.setUnpaidMoney(sendBalance);
            // 字典值：是否已还 1是 0否
            vo.setIsRepayName("1".equals(vo.getIsRepay()) ? "是" : "否");
        }
        resultMap.put("list", list);
        return AjaxJson.success(resultMap);
    }

    /**
     * 导出债券还款计划模板
     */
    @ApiOperation("导出债券还款计划模板")
    @GetMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response, CmsBondRepayPlanVo planVO) throws Exception {
        cmsBondRepayPlanService.exportBudget(request, response, planVO);
    }

    /**
     * 还款计划查询详情初始化数据
     */
    @LogMessage(content = "还款计划查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("还款计划查询详情页面初始化数据")
    @GetMapping("/viewQuery/{id}")
    public AjaxJson<Map<String, Object>> viewQuery(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = new ArrayList<>();
        CmsBondRegisterInfoVo cmsBondRegisterInfoVo = new CmsBondRegisterInfoVo();
        CmsBondRepayPlanVo cmsBondRepayPlanVo = cmsBondRepayPlanService.getInfoById(id);
        if (StringUtils.isNotEmpty(cmsBondRepayPlanVo)) {
            String registerId = cmsBondRepayPlanVo.getRegisterId();
            cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            cmsBondRepayPlanDtlVoList = cmsBondRepayPlanDtlService.getByRegisterId(registerId);
        }
        resultMap.put("leaseCapitalPlanDtlId", id);
        resultMap.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
        resultMap.put("leaseCapitalPlanDelVoList", cmsBondRepayPlanDtlVoList);
        return AjaxJson.success(resultMap);
    }
}