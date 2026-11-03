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
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.lease.entity.LeaseObjectInfo;
import com.bytter.finance.domain.lease.service.LeaseObjectInfoService;
import com.bytter.finance.domain.lease.vo.ContractLeaseObjectInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseItemTypeVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoApproveViewVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoPageVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(value = "/v1-preposebill/cms/LeaseObjectInfo", tags = "融资租赁标的物登记")
@RestController("v1LeaseObjectInfoController")
@RequestMapping("/v1-preposebill/cms/LeaseObjectInfo")
public class LeaseObjectInfoController extends BaseController {
    @Autowired
    private LeaseObjectInfoService leaseObjectInfoService;

    /**
     * 租赁物列表分页数据
     */
    @ApiOperation("租赁物列表分页数据")
    @LogMessage(content = "跳转融资租赁标的物登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input, String leaseId) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = leaseObjectInfoService.queryLeaseObjectInfoList(dataInput, getUserId(), leaseId);
        return AjaxJson.success(output);
    }

    /**
     * 导入保存租赁物
     */
    @ApiOperation("导入保存租赁物")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<ContractLeaseObjectInfoVO> list, @RequestParam("leaseId") String leaseId) {
        leaseObjectInfoService.saveImport(list, getCurrentUser(), leaseId);
        return AjaxJson.success();
    }

    /**
     * 新增租赁物初始化数据
     * 旧接口：/cms/LeaseObjectInfo/add/{leaseId}
     */
    @ApiOperation("新增租赁物页面初始化数据")
    @GetMapping("/add/{leaseId}")
    public AjaxJson<LeaseObjectInfoPageVO> add(@PathVariable("leaseId") String leaseId) {
        LeaseObjectInfoPageVO result = new LeaseObjectInfoPageVO();
        result.setLeaseId(leaseId);
        return AjaxJson.success(result);
    }

    /**
     * 判断租赁物编号是否已经存在
     */
    @ApiOperation("租赁物编号唯一性校验")
    @PostMapping("/checkLeaseItemCode/{leaseItemCode}")
    public AjaxJson<Boolean> checkLeaseItemCode(@PathVariable("leaseItemCode") String leaseItemCode) {
        AjaxJson result = new AjaxJson();
        result.setSuccess(true);
        result.setObj(leaseObjectInfoService.checkLeaseItemCode(leaseItemCode));
        return result;
    }

    /**
     * 保存租赁物（新增）
     */
    @ApiOperation("新增租赁物")
    @LogMessage(content = "添加租赁物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LeaseObjectInfoVO leaseObjectInfoVO) {
        if (leaseObjectInfoVO.getStatus() == 0) {// 暂存
            leaseObjectInfoVO.setStatus(InlConstant.TMP);
        } else if (leaseObjectInfoVO.getStatus() == 1) {// 提交
            leaseObjectInfoVO.setStatus(InlConstant.COMMIT_APPROVE);
        }
        leaseObjectInfoService.saveLeaseObjectInfo(leaseObjectInfoVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改租赁物初始化数据
     * 旧接口：/cms/LeaseObjectInfo/update/{id}/{leaseId}
     */
    @ApiOperation("修改租赁物初始化数据")
    @GetMapping("/update/{id}/{leaseId}")
    public AjaxJson<LeaseObjectInfoPageVO> update(@PathVariable("id") String id, @PathVariable("leaseId") String leaseId) {
        LeaseObjectInfoPageVO result = new LeaseObjectInfoPageVO();
        result.setLeaseId(leaseId);
        result.setLeaseObjectInfoVO(leaseObjectInfoService.queryLeaseObjectInfo(id));
        return AjaxJson.success(result);
    }

    /**
     * 修改租赁物
     */
    @ApiOperation("修改租赁物")
    @LogMessage(content = "添加租赁物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody LeaseObjectInfoVO entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(CmsConstant.TEMP);
        } else if (entity.getStatus() == 1) {// 提交
            entity.setStatus(CmsConstant.COMMITAPPROVE);
        }
        leaseObjectInfoService.modifyLeaseObjectInfo(entity, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 查看租赁物详情
     */
    @ApiOperation("查看租赁物详情")
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
     * 标的物审批列表数据
     */
    @ApiOperation("标的物审批列表数据")
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listInlInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = leaseObjectInfoService.approveDataTable(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 审批详情查看租赁物
     */
    @ApiOperation("审批页面租赁物详情")
    @GetMapping("/viewApproveDate")
    public AjaxJson<LeaseObjectInfoApproveViewVO> viewApproveDate(String id, @RequestParam(required = false) String flag) {
        LeaseObjectInfoApproveViewVO result = new LeaseObjectInfoApproveViewVO();
        result.setFlag(flag);
        result.setLeaseObjectInfoVO(leaseObjectInfoService.queryLeaseObjectInfo(id));
        return AjaxJson.success(result);
    }

    /**
     * 获取租赁物类型字典
     */
    @ApiOperation("获取租赁物类型列表")
    @GetMapping("/getModuleName")
    public AjaxJson<List<LeaseItemTypeVO>> getModuleName() {
        List<LeaseItemTypeVO> list = leaseObjectInfoService.getModuleName();
        return AjaxJson.success(list);
    }
}
