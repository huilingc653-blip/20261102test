package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.biz.netbank.dto.FcReceiptTypeDTO;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 单据类型Controller
 * @date 2017-05-12 13:44:54
 */
@Api(value = "/v1-payment/netbank/receipttype", tags = "单据类型维护")
@RestController("v1FcReceiptTypeController")
@RequestMapping("/v1-payment/netbank/receipttype")
public class FcReceiptTypeController extends BaseController {
    @Autowired
    private FcReceiptTypeService fcReceiptTypeService;

    /**
     * 单据类型表 table列表
     *
     * @return
     */
    @ApiOperation("单据类型表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcReceiptTypeDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<FcReceiptTypeDTO> result = new DataTablesOutput<>();
        DataTablesOutput<FcReceiptType> out = fcReceiptTypeService.getDataTables(input);
        BeanUtils.copyProperties(out, result);
        result.setData(BeanUtil.copyToList(out.getData(), FcReceiptTypeDTO.class));
        return AjaxJson.success(result);
    }


    /**
     * 系统结算方式表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("系统结算方式表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<FcReceiptType> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(fcReceiptTypeService.getById(id));
        }
        return AjaxJson.success();
    }

    /**
     * 系统结算方式表详情页
     *
     * @return
     */
    @ApiOperation("系统结算方式表详情页")
    @LogMessage(content = "单据类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FcReceiptType> view(@PathVariable("id") String id, Model model) {
        FcReceiptType fcReceiptType = fcReceiptTypeService.get(id, true);
        return AjaxJson.success(fcReceiptType);
    }

    /**
     * 添加系统结算方式表
     *
     * @param fcReceiptType
     * @return
     */
    @ApiOperation("添加系统结算方式表")
    @LogMessage(content = "添加系统结算方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")

    public AjaxJson save(@RequestBody FcReceiptType fcReceiptType) {
        if (fcReceiptType.getValidSign() == null) {
            fcReceiptType.setValidSign("1");
        }
        fcReceiptTypeService.save(fcReceiptType);
        return success();
    }

    /**
     * 修改系统结算方式表
     *
     * @param fcReceiptType
     * @return
     */
    @ApiOperation("修改系统结算方式表")
    @LogMessage(content = "修改系统结算方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody FcReceiptType fcReceiptType) {
        FcReceiptType t = fcReceiptTypeService.get(fcReceiptType.getId());
        BeanUtils.copyNotNullProperties(fcReceiptType, t);
        fcReceiptTypeService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除系统结算方式表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除系统结算方式表")
    @LogMessage(content = "删除系统结算方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        fcReceiptTypeService.deleteById(id);
        return success();
    }

    @ApiOperation("获取有效的系统结算方式")
    @GetMapping("/json")
    public AjaxJson<List<FcReceiptType>> json() {
        return AjaxJson.success(fcReceiptTypeService.getValidFcReceiptType());
    }
}
