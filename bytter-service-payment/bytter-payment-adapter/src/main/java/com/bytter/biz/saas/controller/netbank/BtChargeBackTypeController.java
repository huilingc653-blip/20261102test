package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.netbank.vo.BtChargeBackTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 退单类型表
 * @date 2017-05-17 14:26:13
 */
@Api(value = "/v1-paymment/netbank/btchargebacktype", tags = "退单类型维护")
@RestController("v1BtChargeBackTypeController")
@RequestMapping("/v1-payment/netbank/btchargebacktype")
public class BtChargeBackTypeController extends BaseController {
    @Autowired
    private BtChargeBackTypeService btChargeBackTypeService;

    /**
     * 退单类型表 table列表
     *
     * @return
     */

    @ApiOperation("退单类型表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtChargeBackTypeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(btChargeBackTypeService.getDataTablesUnion(input));
    }


    /**
     * 退单类型表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("退单类型表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<BtChargeBackType> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
        return     AjaxJson.success(btChargeBackTypeService.getById(id));
        }
        return AjaxJson.success();
    }

    /**
     * 退单类型表详情页
     *
     * @return
     */
    @ApiOperation("退单类型表详情页")
    @LogMessage(content = "查询退单类型表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BtChargeBackTypeVO> view(@PathVariable("id") String id) {
        BtChargeBackTypeVO btChargeBackType = btChargeBackTypeService.getByIdUnion(id);
        return AjaxJson.success(btChargeBackType);
    }


    /**
     * 获取有效的退单类型
     *
     * @return
     */
    @ApiOperation("获取有效的退单类型")
    @GetMapping("/json")
    public AjaxJson<List<BtChargeBackTypeVO>> json() {
        return AjaxJson.success(btChargeBackTypeService.getValidChargeBackType());
    }


    /**
     * 添加退单类型表
     *
     * @param btChargeBackType
     * @return
     */
    @ApiOperation("添加退单类型表")
    @LogMessage(content = "添加退单类型表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  BtChargeBackType btChargeBackType) {
        if (btChargeBackTypeService.findByCode(btChargeBackType.getBusType()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_BACKTYPE_MESSAGE_BUSTYPE_UNIQUE);
        }
        btChargeBackType.setCreateBy(this.getCurrentUser().getLoginName());
        btChargeBackType.setCreateDate(DateUtils.getDate());
        this.btChargeBackTypeService.save(btChargeBackType, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 修改退单类型表
     *
     * @param btChargeBackType
     * @return
     */
    @ApiOperation("修改退单类型表")
    @LogMessage(content = "修改退单类型表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify( @RequestBody  BtChargeBackType btChargeBackType) {
        BtChargeBackType t = btChargeBackTypeService.getById(btChargeBackType.getId());
        BeanUtils.copyNotNullProperties(btChargeBackType, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.btChargeBackTypeService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 删除退单类型表
     *
     * @param id
     * @return
     */

    @ApiOperation("删除退单类型表")
    @LogMessage(content = "删除退单类型表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtChargeBackType btChargeBackType = btChargeBackTypeService.get(id);
        btChargeBackTypeService.deleteEntity(btChargeBackType);
        return success();
    }

    /**
     * 批量删除退单类型表
     *
     * @param ids
     * @return
     */

    @ApiOperation("批量删除退单类型表")
    @LogMessage(content = "批量删除退单类型表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtChargeBackType btChargeBackType = btChargeBackTypeService.get(id);
            btChargeBackTypeService.deleteEntity(btChargeBackType);
        }
        return success();
    }


}
