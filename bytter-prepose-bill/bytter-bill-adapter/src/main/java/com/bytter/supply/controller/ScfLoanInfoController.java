package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.receivable.dto.ScfLoanInfoDTO;
import com.bytter.supply.domain.receivable.service.ScfLoanInfoService;
import com.bytter.supply.domain.receivable.vo.ScfLoanInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfLoanInfoController
 * @Description: 融资业务主信息控制器
 * @since 2026/7/3
 */
@Api(value = "/v1-supply/receivable/loan", tags = "债权凭证融资业务主信息控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1-supply/receivable/loan")
public class ScfLoanInfoController extends BaseController {

    @Autowired
    private ScfLoanInfoService scfLoanInfoService;

    /**
     * 查询融资业务主信息列表
     */
    @ApiOperation("查询融资业务主信息列表")
    @PostMapping("/list")
    public AjaxJson list(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(scfLoanInfoService.dataTable(dataTablesInput.convertAndInitColumns()));
    }

    /**
     * 融资业务-发起融资
     */
    @ApiOperation("融资业务-发起融资")
    @LogMessage(content = "融资业务-发起融资", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/sendLoan")
    public AjaxJson sendLoan(@RequestBody ScfLoanInfoDTO loanInfoDTO) {
        if (StringUtils.isEmpty(loanInfoDTO.getSendStatus())) {
            return AjaxJson.failed("提交状态不能为空");
        }
        scfLoanInfoService.sendLoan(loanInfoDTO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改融资业务主信息
     */
    @ApiOperation("修改融资业务主信息")
    @LogMessage(content = "修改融资业务主信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody ScfLoanInfoDTO loanInfoDTO) {
        if (StringUtils.isEmpty(loanInfoDTO.getId())) {
            return AjaxJson.failed("主键ID不能为空");
        }
        scfLoanInfoService.updateLoanInfo(loanInfoDTO);
        return AjaxJson.success();
    }

    /**
     * 删除融资业务主信息
     */
    @ApiOperation("删除融资业务主信息")
    @LogMessage(content = "删除融资业务主信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        scfLoanInfoService.deleteLoanInfo(id);
        return AjaxJson.success();
    }

    /**
     * 根据债权凭证编号查询融资业务详情
     */
    @ApiOperation("根据债权凭证编号查询融资业务详情")
    @GetMapping("/viewByVoucherCode/{voucherCode}")
    public AjaxJson view(@PathVariable("voucherCode") String voucherCode) {
        ScfLoanInfoVO loanInfo = scfLoanInfoService.getLoanInfoByVoucherCode(voucherCode);
        return AjaxJson.success(loanInfo);
    }

    /**
     * 根据主键查询融资业务详情（含关联应收债权凭证信息）
     */
    @ApiOperation("根据主键查询融资业务详情")
    @GetMapping("/viewById/{id}")
    public AjaxJson viewById(@PathVariable("id") String id) {
        return AjaxJson.success(scfLoanInfoService.getLoanInfo(id));
    }
}
