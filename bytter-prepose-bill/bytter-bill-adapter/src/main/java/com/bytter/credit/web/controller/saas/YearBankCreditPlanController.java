package com.bytter.credit.web.controller.saas;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.credit.domain.service.YearBankCreditPlanService;
import com.bytter.credit.domain.vo.YearBankCreditPlanFormVO;
import com.bytter.credit.domain.vo.YearBankCreditPlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 银行授信年度计划
 */
@Api(tags = "银行授信年度计划", value = "/v1-cms/bank/credit/plan")
@RestController("v1YearBankCreditPlanController")
@RequiredArgsConstructor
@RequestMapping("/v1-cms/bank/credit/plan")
public class YearBankCreditPlanController extends BaseController {

    private final YearBankCreditPlanService yearBankCreditPlanService;

    /**
     * 银行授信年度计划新增/编辑页面数据
     *
     * @param id 主键id
     * @return 页面数据(编辑时返回数据, 新增时返回当前年份)
     */
    @ApiOperation("银行授信年度计划新增/编辑页面数据")
    @GetMapping("/addEdit")
    public AjaxJson<YearBankCreditPlanFormVO> addEdit(String id) {
        YearBankCreditPlanFormVO result = new YearBankCreditPlanFormVO();
        if (CharSequenceUtil.isNotBlank(id)) {
            result.setInfo(yearBankCreditPlanService.getById(id));
        } else {
            result.setCurrentYear(DateUtil.thisYear());
        }
        return success(result);
    }

    /**
     * 银行授信年度计划表格数据
     *
     * @param dataTablesInput 表格入参
     * @return 计划列表数据
     */
    @ApiOperation("查询银行授信年度计划表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<YearBankCreditPlanVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(yearBankCreditPlanService.dataTableQuery(input, getCurrentUser()));
    }

    /**
     * 删除银行授信年度计划
     *
     * @param body 主键id集合
     * @return 操作结果
     */
    @ApiOperation("删除银行授信年度计划")
    @LogMessage(content = "删除银行授信年度计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/deleteByIds")
    public AjaxJson deleteByIds(@RequestBody Map<String, String> body) {
        String ids = body.get("ids");
        if (CharSequenceUtil.isBlank(ids)) {
            return success();
        }
        yearBankCreditPlanService.deleteByIds(Arrays.asList(ids.split(",")));
        return success();
    }

    /**
     * 新增/修改银行授信年度计划
     *
     * @param detail 年度计划数据
     * @return 操作结果
     */
    @ApiOperation("新增/修改银行授信年度计划")
    @PostMapping("/saveOrUpdate")
    public AjaxJson saveOrUpdate(@RequestBody YearBankCreditPlanVO detail) {
        if (CharSequenceUtil.isEmpty(detail.getCorpId())) {
            throw new BusinessException("单位为空");
        }
        if (Objects.isNull(detail.getYear())) {
            throw new BusinessException("年份为空");
        }
        if (CharSequenceUtil.hasBlank(detail.getBankId(), detail.getBankCode(), detail.getBankName())) {
            throw new BusinessException("银行信息为空");
        }
        if (CharSequenceUtil.isBlank(detail.getCreditType())) {
            throw new BusinessException("授信类型为空");
        }
        if (CharSequenceUtil.isBlank(detail.getCurId())) {
            throw new BusinessException("币别为空");
        }
        if (Objects.isNull(detail.getCreditAmount())) {
            throw new BusinessException("金额为空");
        }
        yearBankCreditPlanService.saveOrUpdate(detail, getCurrentUser());
        return success();
    }
}
