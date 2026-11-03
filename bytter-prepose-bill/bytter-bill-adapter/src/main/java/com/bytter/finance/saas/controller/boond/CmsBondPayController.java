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
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.service.CmsBondIssueInfoService;
import com.bytter.finance.domain.bond.service.CmsBondPayService;
import com.bytter.finance.domain.bond.service.CmsBondRegisterInfoService;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondPayVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券还款
 * @date 2022-06-07 10:07:46
 */
@Api(value = "/v1-preposebill/cms/bond/cmsbondpay", tags = "债券还款管理")
@RestController("v1CmsBondPayController")
@RequestMapping("/v1-preposebill/cms/bond/cmsbondpay")
public class CmsBondPayController extends BaseController {
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;

    /**
     * 债券还款计划登记 table列表
     */
    @LogMessage(content = "查询债券还款处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券还款列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsBondPayService.getDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 债券还款新增/编辑页面初始化数据
     */
    @ApiOperation("债券还款编辑初始化数据")
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> edit(String tempOrSubmitSign, CmsBondPayVo cmsBondPayVo) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(cmsBondPayVo.getId())) {
            List<CmsBondPayVo> leasePayVOList = cmsBondPayService.getByRegisterId(cmsBondPayVo.getId(),
                    CmsConstant.IS_FALSE);
            if (!ListUtils.isNullOrEmpty(leasePayVOList)) {
                cmsBondPayVo = leasePayVOList.get(0);
            }
            cmsBondPayVo.setCreateDate(new Date());
        } else {
            cmsBondPayVo.setCreateDate(new Date());
        }
        CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService
                .getRegisterById(cmsBondPayVo.getRegisterId());
        String onlineSign = cmsBondPayVo.getOnlineSign();
        resultMap.put("onlineSign", onlineSign);
        resultMap.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
        resultMap.put("cmsBondPayVo", cmsBondPayVo);
        resultMap.put("tempOrSubmitSign", tempOrSubmitSign);
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券还款申请查询页面初始化
     */
    @LogMessage(content = "债券还款申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券还款查看页面初始化数据")
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
     * 根据债券主表id查询债券还款详情
     *
     * @param registerId 债劵注册id
     */
    @ApiOperation("根据注册ID查询债券还款明细")
    @PostMapping("/listBondPayByRegisterId")
    public AjaxJson<List<CmsBondPayVo>> listBondPayByRegisterId(String registerId) {
        List<CmsBondPayVo> list = cmsBondPayService.getBondPayByRegisterId(registerId);
        return AjaxJson.success(list);
    }

    /**
     * 编辑债券还款（暂存/提交）
     */
    @LogMessage(content = "编辑债券还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("债券还款暂存或提交")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(CmsBondPayVo vo) {
        // 校验该发行是否还有在审批中的数据
        cmsBondPayService.checkCmsBondPayStatus(vo.getIssueId());
        cmsBondPayService.tempOrSubmit(vo, vo.getSaveType(), vo.getTempOrSubmitSign(), getCurrentUser(), vo.getSign());
        return AjaxJson.success();
    }

    /******************** 审批BEGIN **********************/
    /**
     * 债券还款申请审批列表 - 数据
     */
    @ApiOperation("债券还款审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsBondPayService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 债券还款申请审批详情页面初始化
     */
    @ApiOperation("债券还款审批详情初始化数据")
    @GetMapping("/viewApprove")
    public AjaxJson<Map<String, Object>> viewApprove(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsBondPay cmsBondPay = cmsBondPayService.get(businessId);
            if (StringUtils.isNotEmpty(cmsBondPay) && StringUtils.isNotEmpty(cmsBondPay.getId())) {
                CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService
                        .getRegisterById(cmsBondPay.getRegisterId());
                CmsBondPayVo cmsBondPayVo = cmsBondPayService.getById(cmsBondPay.getId());
                cmsBondPayVo.setPurpose(cmsBondPay.getPurpose());
                resultMap.put("cmsBondPayVo", cmsBondPayVo);
                resultMap.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            }
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }
}