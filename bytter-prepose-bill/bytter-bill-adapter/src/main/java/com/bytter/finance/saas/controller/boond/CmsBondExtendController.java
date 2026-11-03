package com.bytter.finance.saas.controller.boond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.bond.service.CmsBondExtendService;
import com.bytter.finance.domain.bond.service.CmsBondIssueInfoService;
import com.bytter.finance.domain.bond.vo.CmsBondExtendVO;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债券展期
 *
 * @author wangf
 */
@Api(value = "/v1-preposebill/cms/bond/extend", tags = "债券展期管理")
@RestController("v1CmsBondExtendController")
@RequestMapping("/v1-preposebill/cms/bond/extend")
public class CmsBondExtendController extends BaseController {
    @Autowired
    private CmsBondExtendService cmsBondExtendService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;

    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @return 有效的债券类型集合
     */
    @ApiOperation("根据债券注册ID获取有效债券发行编号")
    @GetMapping("/listValidBondIssueNum/{registerId}")
    public AjaxJson<List<CmsBondIssueInfoVo>> listValidBondIssueNum(@PathVariable("registerId") String registerId) {
        List<CmsBondIssueInfoVo> result = cmsBondExtendService.listValidBondIssueNum(registerId);
        return AjaxJson.success(result);
    }

    /**
     * 查询债券展期注册列表
     *
     * @param input 前端传参
     * @return 债券注册信息列表数据
     */
    @LogMessage(content = "查询债券展期注册列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券展期注册主列表数据")
    @PostMapping("/extendRegisterInfo/datatable")
    public AjaxJson<DataTablesOutput> dataTableForTransferableRegisterInfo(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondExtendService.getTransferableRegisterDataTable(dataInput, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 债券展期列表数据
     *
     * @param registerId 债券注册ID
     * @param input      前端传参
     * @return 债券展期列表数据
     */
    @LogMessage(content = "查询债券展期列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券展期明细列表数据")
    @PostMapping("/datatable/{registerId}")
    public AjaxJson<DataTablesOutput> dataTable(@PathVariable String registerId, @RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondExtendService.getDataTable(registerId, dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 债券展期编辑页面初始化数据
     *
     * @param vo 债券展期vo
     * @return 页面所需数据
     */
    @ApiOperation("债券展期编辑初始化数据")
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> editpage(CmsBondExtendVO vo) {
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("registerId", vo.getRegisterId());
        if (!EmptyUtils.isEmpty(vo) && EmptyUtils.isNotEmpty(vo.getId())) {
            vo = cmsBondExtendService.getCmsBondExtendById(vo.getId());
            // 获取最新展期申请通过的数据
            CmsBondExtendVO lastExtend = cmsBondExtendService.getCmsBondExtendByIssueId(vo.getIssueId());
            Date lastExtendDate = lastExtend.getExtendEndDate();
            if (lastExtend.getExtendEndDate() != null) {
                resultMap.put("lastExtendDate", DateUtils.formatDate(DateUtils.addDay(lastExtendDate, 1)));
            }
        }
        resultMap.put("cmsBondExtendVO", vo);
        return AjaxJson.success(resultMap);
    }

    /**
     * 获取上一笔展期审批通过的到期日期
     *
     * @param issueId 发行id
     * @return 上一笔展期审批通过的到期日期
     */
    @ApiOperation("获取上一笔审批通过展期到期日")
    @GetMapping("/getLastExtendDate/{issueId}")
    public AjaxJson<Map<String, Object>> getLastExtendDate(@PathVariable("issueId") String issueId) {
        Map<String, Object> params = new HashMap<>(2);
        if (StringUtils.isNotEmpty(issueId)) {
            // 获取最新展期申请通过的数据
            CmsBondExtendVO lastExtend = cmsBondExtendService.getCmsBondExtendByIssueId(issueId);
            Date lastExtendDate = lastExtend.getExtendEndDate();
            if (lastExtend.getExtendEndDate() != null) {
                params.put("lastExtendDate", DateUtils.formatDate(lastExtendDate));
            }
        }
        return AjaxJson.success(params);
    }

    /**
     * 新增或修改债券展期
     *
     * @param vo       展期信息
     * @return 是否操作成功
     */
    @LogMessage(content = "新增或修改展期信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("新增/修改债券展期")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsBondExtendVO vo) {
        cmsBondExtendService.save(vo, vo.getSaveType(), this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除债券展期
     *
     * @param id 债券注册id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除展期信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除债券展期")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        // 此id为债券注册id,需要查到债券发行id,再查出并删除最后一个展期
        cmsBondExtendService.delete(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 查看某个债券展期信息
     *
     * @param id 债券展期id
     * @return 债券展期信息
     */
    @LogMessage(content = "查看某个债券展期信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查看债券展期详情")
    @GetMapping("/{id}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>(1);
        CmsBondExtendVO vo = cmsBondExtendService.getCmsBondExtendById(id);
        resultMap.put("cmsBondTransferVO", vo);
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券展期审批列表数据
     */
    @ApiOperation("债券展期审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondExtendService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 债券展期审批详情
     *
     * @param businessId 业务ID
     * @param taskId     审批任务id
     * @return 详情数据
     */
    @ApiOperation("债券展期审批详情")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("taskId", taskId);
        resultMap.put("cmsBondExtendVO", cmsBondExtendService.getCmsBondExtendById(businessId));
        return AjaxJson.success(resultMap);
    }
}
