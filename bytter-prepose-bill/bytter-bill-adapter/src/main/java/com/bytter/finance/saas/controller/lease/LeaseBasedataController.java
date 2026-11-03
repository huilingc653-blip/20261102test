package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.lease.dao.LeaseTypeDao;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.service.LeaseBasedataService;
import com.bytter.finance.domain.lease.vo.LeaseItemTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title
 * @Description 融资租赁基础数据controller
 * @Author wanggl
 * @Date 2022-4-2
 * @Version 1.0
 */
@Api(value = "/v1-preposebill/cms/lease/baseData", tags = "融资租赁基础数据")
@RestController("v1LeaseBasedataController")
@RequestMapping("/v1-preposebill/cms/lease/baseData")
public class LeaseBasedataController extends BaseController {
    @Autowired
    private LeaseBasedataService leaseBasedataService;
    @Autowired
    private LeaseTypeDao leaseTypeDao;

    /**
     * 租赁类型列表数据
     */
    @ApiOperation("租赁类型列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseBasedataService.listLeaseType(input);
        return AjaxJson.success(output);
    }

    /**
     * 租赁类型保存
     */
    @ApiOperation("租赁类型保存")
    @LogMessage(content = "租赁类型保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LeaseItemType entity) {
        entity.setStatus(CmsConstant.APPROVED);
        leaseBasedataService.saveLeaseType(entity);
        leaseBasedataService.saveEntity(entity);
        return AjaxJson.success();
    }

    /**
     * 租赁类型更新
     */
    @ApiOperation("租赁类型更新")
    @LogMessage(content = "租赁类型更新", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/updatedate")
    public AjaxJson update(@RequestBody LeaseItemType leaseItemType) {
        leaseBasedataService.verify(leaseItemType);
        String id = leaseItemType.getId();
        String code = leaseItemType.getCode();
        String name = leaseItemType.getName();
        String rmk = leaseItemType.getRmk();
        leaseBasedataService.updateLeaseType(id, code, name, rmk);
        return AjaxJson.success();
    }

    /**
     * 编码唯一性校验
     */
    @ApiOperation("编码唯一性校验")
    @PostMapping("/validate")
    public AjaxJson validate(String id, String Code) {
        boolean result = leaseBasedataService.validateCode(id, Code);
        AjaxJson ajaxJson = success();
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    /**
     * 删除租赁类型
     */
    @ApiOperation("删除租赁类型")
    @LogMessage(content = "删除租赁类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete")
    public AjaxJson delete(String id) {
        LeaseItemType entity = leaseBasedataService.get(id);
        leaseBasedataService.deleteLeaseType(id);
        entity.setStatus(CmsConstant.DELETED);
        leaseBasedataService.updateEntity(entity);
        return AjaxJson.success();
    }

    /**
     * 租赁类型编辑页初始化
     * 旧接口：/cms/lease/baseData/update
     */
    @ApiOperation("租赁类型编辑页初始化")
    @GetMapping("/update")
    public AjaxJson<LeaseItemTypeVO> update(String id) {
        return AjaxJson.success(leaseBasedataService.getLeaseTypeById(id));
    }
}
