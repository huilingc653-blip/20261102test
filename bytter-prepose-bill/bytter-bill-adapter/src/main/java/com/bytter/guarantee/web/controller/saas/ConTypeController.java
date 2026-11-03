package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.guarantee.domain.service.ConTypeService;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import com.bytter.guarantee.domain.vo.ConTypeVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @Title: 合同类型控制类
 * @Package com.bytter.biz.contract.controller
 * @Description: 合同类型管理
 * @author: Jinyong.cao
 * @date: 2019-11-4 下午4:19:43
 */
@Api(value = "/v1-guarantee/contract/conType", tags = "合同类型")
@RestController("v1ConTypeController")
@RequestMapping("/v1-guarantee/contract/conType")
public class ConTypeController extends BaseController {
    @Autowired
    private ConTypeService conTypeService;

    /**
     * 查询合同类型列表数据
     */
    @ApiOperation("查询合同类型列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conTypeService.getDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 新增合同类型
     */
    @ApiOperation("新增合同类型")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ConTypeVO conTypeVO) {
        if (conTypeService.queryConTractIsExist(conTypeVO.getTypeCode(), null)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_CODE_ISEXIST);
        }
        conTypeService.save(conTypeVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改合同类型
     */
    @ApiOperation("修改合同类型")
    @PostMapping("/updateConType")
    public AjaxJson updateConType(@RequestBody  ConTypeVO conTypeVO) {
        if (conTypeService.queryConTractIsExist(conTypeVO.getTypeCode(), conTypeVO.getId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_CODE_ISEXIST);
        }
        conTypeService.updateContractType(conTypeVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 获取有效的合同类型集合
     */
    @ApiOperation("获取有效的合同类型集合")
    @GetMapping("/getAllConType")
    public List<ConTypeVO> getAllConType(ConTypeVO params) {
        return conTypeService.getAllConType(params, this.getCurrentUser());
    }

    /**
     * 删除合同类型
     */
    @ApiOperation("删除合同类型")
    @PostMapping("/del")
    public AjaxJson delete(@RequestParam("externalIds[]") List<String> externalIds) {
        if (null != externalIds && conTypeService.vnGtOccupy(externalIds)) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_OCCUPY);
        }
        conTypeService.delete(externalIds);
        return AjaxJson.success();
    }

    /**
     * 根据类型ID获取字段配置
     */
    @ApiOperation("根据类型ID获取字段配置")
    @PostMapping("/conFiled")
    public AjaxJson<List<ConTypeFieldVO>> conFiled(String id) {
        return AjaxJson.success(conTypeService.getTypeFieldByTypeId(id));
    }
}