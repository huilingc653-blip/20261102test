package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.lease.entity.LeaseObjectInfo;
import com.bytter.finance.domain.lease.service.LeaseObjectInfoService;
import com.bytter.finance.domain.lease.service.LeaseUpdateObjectInfoService;
import com.bytter.finance.domain.lease.vo.ContractLeaseObjectInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoPageVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(value = "/v1-preposebill/cms/lease/UpdateObjectInfo", tags = "融资租赁变更-标的物管理")
@RestController("v1LeaseUpdateObjectInfoController")
@RequestMapping("/v1-preposebill/cms/lease/UpdateObjectInfo")
public class LeaseUpdateObjectInfoController extends BaseController {
    @Autowired
    private LeaseUpdateObjectInfoService leaseUpdateObjectInfoService;
    @Autowired
    private LeaseObjectInfoService leaseObjectInfoService;

    /**
     * 融资变更标的物主列表数据
     */
    @ApiOperation("融资变更标的物主列表数据")
    @LogMessage(content = "跳转融资租赁标的物修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/leaseDatatable")
    public AjaxJson<DataTablesOutput> leaseDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseUpdateObjectInfoService.getDataleaseDatatables(input, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 租赁物明细列表
     */
    @ApiOperation("租赁物明细列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input, String leaseId) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = leaseUpdateObjectInfoService.queryLeaseObjectInfoList(dataInput, getUserId(), leaseId);
        return AjaxJson.success(output);
    }

    /**
     * 租赁物详情查看
     */
    @ApiOperation("租赁物详情查看")
    @GetMapping("/view/{id}")
    public AjaxJson<LeaseObjectInfoPageVO> view(@PathVariable("id") String id) {
        LeaseObjectInfoPageVO result = new LeaseObjectInfoPageVO();
        LeaseObjectInfoVO leaseObjectInfoVO = leaseObjectInfoService.queryLeaseObjectInfo(id);
        if (EmptyUtils.isEmpty(leaseObjectInfoVO.getOldMoney())) {
            leaseObjectInfoVO.setOldMoney(BigDecimal.ZERO);
        }
        result.setLeaseObjectInfoVO(leaseObjectInfoVO);
        return AjaxJson.success(result);
    }

    /**
     * 删除租赁物
     */
    @ApiOperation("删除租赁物")
    @PostMapping("/del/{id}")
    public AjaxJson del(@PathVariable("id") String id) {
        LeaseObjectInfo leaseObjectInfo = leaseObjectInfoService.get(id);
        leaseObjectInfo.setStatus(CmsConstant.DELETED);
        leaseObjectInfoService.updateEntity(leaseObjectInfo);
        return AjaxJson.success();
    }

    /**
     * 导入租赁物
     */
    @ApiOperation("导入租赁物")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<ContractLeaseObjectInfoVO> list,
                               @RequestParam("leaseId") String leaseId) {
        this.leaseUpdateObjectInfoService.saveImport(list, this.getCurrentUser(), leaseId);
        return AjaxJson.success();
    }

    /**
     * 新增租赁物初始化数据
     */
    @ApiOperation("新增租赁物页面初始化数据")
    @GetMapping("/add/{leaseId}")
    public AjaxJson<LeaseObjectInfoPageVO> add(@PathVariable("leaseId") String leaseId) {
        LeaseObjectInfoPageVO result = new LeaseObjectInfoPageVO();
        result.setLeaseId(leaseId);
        return AjaxJson.success(result);
    }

    /**
     * 保存租赁物（新增）
     */
    @ApiOperation("新增租赁物保存")
    @LogMessage(content = "添加租赁物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(LeaseObjectInfoVO leaseObjectInfoVO) {
        if (leaseObjectInfoVO.getStatus() == 0) {// 暂存
            leaseObjectInfoVO.setStatus(CmsConstant.LEASE_TEMP);
        } else if (leaseObjectInfoVO.getStatus() == 1) {// 提交
            leaseObjectInfoVO.setStatus(CmsConstant.LEASE_COMMITAPPROVE);
        }
        leaseUpdateObjectInfoService.saveLeaseObjectInfo(leaseObjectInfoVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 租赁物编号唯一性校验
     */
    @ApiOperation("租赁物编号唯一性校验")
    @PostMapping("/checkLeaseItemCode/{leaseItemCode}/{id}")
    public AjaxJson<Boolean> checkLeaseItemCode(@PathVariable("leaseItemCode") String leaseItemCode,
                                                @PathVariable("id") String id) {
        AjaxJson result = new AjaxJson();
        result.setSuccess(true);
        result.setObj(leaseUpdateObjectInfoService.checkLeaseItemCode(leaseItemCode, id));
        return result;
    }

    /**
     * 修改租赁物初始化数据
     */
    @ApiOperation("修改租赁物初始化数据")
    @GetMapping("/update/{id}/{leaseId}")
    public AjaxJson<LeaseObjectInfoPageVO> update(@PathVariable("id") String id, @PathVariable("leaseId") String leaseId) {
        LeaseObjectInfoPageVO result = new LeaseObjectInfoPageVO();
        result.setLeaseId(leaseId);
        result.setLeaseObjectInfoVO(leaseUpdateObjectInfoService.queryLeaseObjectInfo(id));
        return AjaxJson.success(result);
    }

    /**
     * 修改租赁物保存
     */
    @ApiOperation("修改租赁物保存")
    @LogMessage(content = "添加租赁物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody LeaseObjectInfoVO entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(CmsConstant.LEASE_TEMP); // 修改暂存
        } else if (entity.getStatus() == 1) {// 提交
            entity.setStatus(CmsConstant.LEASE_COMMITAPPROVE);// 修改待审批
        }
        leaseUpdateObjectInfoService.modifyLeaseObjectInfo(entity, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 标的物变更审批列表数据
     */
    @ApiOperation("标的物变更审批列表数据")
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = leaseUpdateObjectInfoService.approveDataTable(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }
}
