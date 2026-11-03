package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.netbank.entity.NisShareBusType;
import com.bytter.biz.netbank.service.NisShareBusTypeService;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.netbank.vo.NisShareBusTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 共享业务类型维护控制层
 *
 * @author fangcf
 * @date 2025-05-15
 */
@Api(value = "/v1-payment/netbank/nissharebustype", tags = "共享业务类型维护")
@RestController("v1NisShareBusTypeController")
@RequestMapping("/v1-payment/netbank/nissharebustype")
public class NisShareBusTypeController extends BaseController {

    @Autowired
    private NisShareBusTypeService nisShareBusTypeService;


    /**
     * 共享业务类型 table列表
     *
     * @return 共享业务类型数据
     */
    @ApiOperation("共享业务类型 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisShareBusTypeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisShareBusTypeService.getDataTablesUnion(input));
    }


    /**
     * 新增共享业务类型
     *
     * @param id 对象
     * @return 路径
     */
    @ApiOperation("新增共享业务类型")
    @GetMapping("/update/{id}")
    public AjaxJson<NisShareBusType> update(@PathVariable String id) {
        return AjaxJson.success(nisShareBusTypeService.get(id, true));
    }


    /**
     * 获取“收付款标志=付款，有效标志=有效”的业务种类
     *
     * @param cdSign 收付款标志
     * @return 业务类型数据集合
     */
    @ApiOperation("获取“收付款标志=付款，有效标志=有效”的业务种类")
    @GetMapping("/listValidBusTypeByCdSign/{cdSign}")
    public AjaxJson<List<NisBusTypeVO>> listValidBusTypeByCdSign(@PathVariable("cdSign") String cdSign) {
        return AjaxJson.success(nisShareBusTypeService.listValidBusTypeByCdSign(cdSign));
    }

    /**
     * 根据业务类别获取所有正常的共享业务类型
     *
     * @param busTypeId 业务类型id
     * @return 共享业务类型集合
     */
    @ApiOperation("根据业务类别获取所有正常的共享业务类型")
    @GetMapping("/listValidShareBusTypeByBusType")
    public AjaxJson<List<NisShareBusType>> listValidShareBusTypeByBusType(@RequestParam("busTypeId") String busTypeId) {
        return AjaxJson.success(nisShareBusTypeService.listValidShareBusTypeByBusType(busTypeId));
    }


    /**
     * 新增共享业务类型
     *
     * @param vo form表单
     * @return 操作结果
     */
    @ApiOperation("新增共享业务类型")
    @LogMessage(content = "新增共享业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisShareBusTypeVO vo) {
        nisShareBusTypeService.saveShareBusType(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 修改共享业务类型
     *
     * @param vo form表单
     * @return 操作结果
     */
    @ApiOperation("修改共享业务类型")
    @LogMessage(content = "修改共享业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody  NisShareBusTypeVO vo) {
        nisShareBusTypeService.updateShareBusType(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 删除共享业务类型
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除共享业务类型")
    @LogMessage(content = "删除共享业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        nisShareBusTypeService.deleteShareBusType(id);
        return success();
    }

}
