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
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.service.CmsBondIssueInfoService;
import com.bytter.finance.domain.bond.service.CmsBondPayInterestService;
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
 * @Description: 债券还息
 * @date 2022-06-07 10:07:46
 */
@Api(value = "/v1-preposebill/cms/bond/cmsbondpayinterest", tags = "债券还息管理")
@RestController("v1CmsBondPayInterestController")
@RequestMapping("/v1-preposebill/cms/bond/cmsbondpayinterest")
public class CmsBondPayInterestController extends BaseController {
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondPayInterestService cmsBondPayInterestService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;

    /**
     * 债券还息处理 table列表
     */
    @LogMessage(content = "查询债券还息处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券还息列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsBondPayInterestService.getDataTablesUnion(input, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 债券还息新增/编辑初始化数据
     */
    @ApiOperation("债券还息编辑页面初始化数据")
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> edit(String tempOrSubmitSign, CmsBondPayVo cmsBondPayVo) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(cmsBondPayVo.getId())) {
            List<CmsBondPayVo> bondPayVOList = cmsBondPayInterestService.getByRegisterId(cmsBondPayVo.getId());
            if (!ListUtils.isNullOrEmpty(bondPayVOList)) {
                cmsBondPayVo = bondPayVOList.get(0);
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
     * 债券还息申请查询页面初始化
     */
    @LogMessage(content = "债券还息申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券还息查看页面初始化数据")
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
     * 根据债券注册id查询债券还息详情
     *
     * @param registerId 债劵注册id
     */
    @ApiOperation("根据注册ID查询债券还息明细")
    @PostMapping("/listBondPayInterestByRegisterId")
    public AjaxJson<List<CmsBondPayVo>> getBondPayInterestByRegisterId(String registerId) {
        List<CmsBondPayVo> list = cmsBondPayInterestService.getBondPayInterestByRegisterId(registerId);
        return AjaxJson.success(list);
    }

    /**
     * 编辑债券还息（暂存/提交）
     */
    @LogMessage(content = "编辑债券还息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("债券还息暂存或提交")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(CmsBondPayVo vo) {
        // 校验该发行是否还有在审批中的数据
        cmsBondPayService.checkCmsBondPayStatus(vo.getIssueId());
        cmsBondPayInterestService.tempOrSubmit(vo, vo.getSaveType(), vo.getTempOrSubmitSign(), getCurrentUser(), vo.getSign());
        return AjaxJson.success();
    }

    /******************** 审批BEGIN **********************/
    /**
     * 债券还息申请审批列表 - 数据
     */
    @ApiOperation("债券还息审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsBondPayInterestService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 债券还息申请审批详情初始化数据
     */
    @ApiOperation("债券还息审批详情初始化数据")
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