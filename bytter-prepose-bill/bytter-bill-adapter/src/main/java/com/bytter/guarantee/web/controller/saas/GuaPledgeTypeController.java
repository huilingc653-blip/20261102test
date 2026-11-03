package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaPledgeType;
import com.bytter.guarantee.domain.service.GuaPledgeTypeService;
import com.bytter.guarantee.domain.vo.GuaPledgeTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fusb
 * @version 1.0
 * @Title GuaPledgeTypeController
 * @Description 抵质押物类型维护Controller
 * @date 2018年12月11日
 */
@Api(tags = "抵质押物类型维护",value = "/v1-guarantee/cms/guarantee/pledgeType")
@RestController("v1GuaPledgeTypeController")
@RequestMapping("/v1-guarantee/cms/guarantee/pledgeType")
public class GuaPledgeTypeController extends BaseController {
    @Autowired
    GuaPledgeTypeService guaPledgeTypeService;

    /** 列表内容 **/
    @ApiOperation(value = "抵质押物列表内容")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(guaPledgeTypeService.queryGuaPledgeType(input));
    }
    /**
     * 抵质押物类型编号
     *
     * @return true-重复 false-不重复
     */
    @PostMapping("/checkTypeCode")
    @ResponseBody
    public AjaxJson<Boolean> checkTypeCode(String typeCode, String id) {
        List<GuaPledgeType> typeList = guaPledgeTypeService.queryTypeByCode(typeCode);
        String typeId = null;
        Boolean flag = false;
        if (typeList.size() > 0) {
            typeId = typeList.get(0).getId();// 编号不允许重复，最多只有一条
            if (StringUtils.isNotEmpty(id)) {// 编辑时需要与本来的编号做对比
                if (typeId.equals(id)) {
                    flag = false;
                } else {
                    flag = true;
                }
            } else {// 新增数据时根据编号查到抵质押物类型信息说明编号重复
                flag = true;
            }
        }
        return success(flag);
    }
    @ApiOperation("增加抵质押物类型")
    @LogMessage(content = "增加抵质押物类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody GuaPledgeType guaPledgeType) {
        guaPledgeTypeService.saveOrUpdateGuaPledgeType(guaPledgeType, this.getCurrentUser());
        return success();
    }
    /**
     * 修改抵质押物类型页面跳转
     *
     * @return
     */
    @ApiOperation("修改抵质押物类型页面跳转")
    @LogMessage(content = "修改抵质押物类型页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @GetMapping("/update")
    public AjaxJson<GuaPledgeType> update(String id) {
        if (StringUtils.isNotEmpty(id)) {
            guaPledgeTypeService.dateCheck(id);
            GuaPledgeType guaPledgeType = guaPledgeTypeService.getGuaPledgeTypeById(id);
            return success(guaPledgeType);
        }
        return AjaxJson.success();
    }
    /**
     * 修改抵质押物类型
     *
     * @param guaPledgeType
     * @return
     */
    @ApiOperation("修改抵质押物类型")
    @LogMessage(content = "修改抵质押物类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody GuaPledgeType guaPledgeType) {
        GuaPledgeType entity = new GuaPledgeType();
        BeanUtils.copyNotNullProperties(guaPledgeType, entity);
        guaPledgeTypeService.saveOrUpdateGuaPledgeType(guaPledgeType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除授信品种类型
     *
     * @param id
     * @return
     */
    @ApiOperation("删除授信品种类型")
    @LogMessage(content = "删除授信品种类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaPledgeTypeService.deleteGuaPledgeType(id, this.getCurrentUser());
        return success();
    }
    /**
     * 抵质押物类型维护列表
     *
     * @return
     */
    @ApiOperation("抵质押物类型维护列表")
    @GetMapping("/json")
    @ResponseBody
    public AjaxJson<List<GuaPledgeTypeVO>> getListByValidSignNoPledgeAttr() {
        List<GuaPledgeTypeVO> list = guaPledgeTypeService.getListByValidSign();
        return success(list);
    }
    /**
     * 抵质押物类型维护列表
     *
     * @return
     */
    @ApiOperation("抵质押物类型维护列表")
    @GetMapping("/json/{typeAttr}")
    @ResponseBody
    public AjaxJson<List<GuaPledgeTypeVO>> getListByValidSignWithPledgeAttr(@PathVariable("typeAttr") String typeAttr) {
        List<GuaPledgeTypeVO> guaPledgeTypes = guaPledgeTypeService.getListByValidSign1(typeAttr);
        return success(guaPledgeTypes);
    }
    /**
     * 判断抵质押物类型能否被删除或是修改
     *
     * @param id
     * @return
     */
    @GetMapping("/checkData")
    @ResponseBody
    public AjaxJson<Boolean> checkData(String id) {
        if (guaPledgeTypeService.dateCheck(id)) {
            return success(Boolean.TRUE);
        } else {
            return success(Boolean.FALSE);
        }
    }
}
