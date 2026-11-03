package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.vo.v1.RatItemCustomAddVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.credit.domain.service.RatItemTypeService;
import com.bytter.credit.domain.vo.RatItemCustomVO;
import com.bytter.credit.dto.RatItemCustomDTO;
import com.bytter.credit.dto.RatItemTypeDTO;
import com.bytter.credit.dto.RatSysItemDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 15:44:10
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/cms/rat/ratitemcustom",tags = "授信品种")
@RestController("v1RatItemCustomController")
@RequestMapping("/v1-preposebill/cms/rat/ratitemcustom")
public class RatItemCustomController extends BaseController {
    @Autowired
    private RatItemCustomService ratItemCustomService;
    @Autowired
    private RatItemTypeService ratItemTypeService;

    /**
     * 授信品种 table列表
     *
     * @return
     */
    @ApiOperation(" 授信品种 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<RatItemCustomVO> >  dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(ratItemCustomService.listOutputData(input));
    }
    /**
     * 取系统维护的自定义授信品种
     *
     * @return
     */
    @ApiOperation("取系统维护的自定义授信品种")
    @GetMapping("/getAllRatItem")
    public AjaxJson<List<RatItemCustomDTO> > getAllRatItem(String type,String ritCode) {
        if (StringUtils.isEmpty(type)) {
            type = RationConstant.RAT_TYPE_0;
        }
        return AjaxJson.success(ratItemCustomService.getAllItemCustom(type,ritCode));
    }
    /**
     * 取系统授信品种
     *
     * @return
     */
    @ApiOperation("取系统授信品种")
    @PostMapping("/sysItemJson")
    public AjaxJson<List<RatSysItemDTO>> sysItemJson(String type) {
        if (StringUtils.isEmpty(type)) {
            type = RationConstant.RAT_TYPE_0;
        }
        return AjaxJson.success(ratItemCustomService.getSysRatItemByType(type));
    }

    /**
     * 授信品种新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<RatItemCustomAddVO> add() {
        RatItemCustomAddVO model=new RatItemCustomAddVO();
        // 系统授信品种
        List<RatSysItemDTO> sysItemCodes = ratItemCustomService.getSysRatItemByType(RationConstant.RAT_TYPE_0);
        List<RatItemTypeDTO> ritCodes = ratItemTypeService.getAllRatItemType();// 授信品种类型
        model.setSysItemCodes(  sysItemCodes);
        model.setRitCodes(  ritCodes);
        return AjaxJson.success(model);
    }

    /**
     * 授信品种编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update/{id}")
    public AjaxJson<RatItemCustomAddVO> update(@PathVariable String id) {
        RatItemCustomAddVO model=new RatItemCustomAddVO();
        if (StringUtils.isNotEmpty(id)) {
            RatItemCustom  ratItemCustom = ratItemCustomService.get(id);
            model.setRatItemCustomPage(  ratItemCustom);
            // 系统授信品种
            List<RatSysItemDTO> sysItemCodes = ratItemCustomService
                    .getSysRatItemByType(ratItemCustom.getType().toString());
            List<RatItemTypeDTO> ritCodes = ratItemTypeService.getAllRatItemType();// 授信品种类型
            model.setSysItemCodes( sysItemCodes);
            model.setRitCodes(ritCodes);
        }
        return AjaxJson.success(model);
    }
    /**
     * 授信品种详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信品种详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public  AjaxJson<RatItemCustomAddVO>  view(@PathVariable("id") String id) {
        RatItemCustomAddVO model=new RatItemCustomAddVO();
        RatItemCustom ratItemCustom = ratItemCustomService.get(id);
        model.setRatItemCustomPage(  ratItemCustom);
        return AjaxJson.success(model);
    }
    /**
     * 添加授信品种
     * 
     * @param ratItemCustom
     * @return
     */
    @LogMessage(content = "添加授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatItemCustom ratItemCustom) {
        ratItemCustomService.saveRatItemCustom(ratItemCustom, getCurrentUser());
        return success();
    }
    /**
     * 修改授信品种
     * 
     * @param ratItemCustom
     * @return
     */
    @LogMessage(content = "修改授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatItemCustom ratItemCustom) {
        ratItemCustomService.updateRatItemCustom(ratItemCustom, getCurrentUser());
        return success();
    }
    /**
     * 删除授信品种
     * 
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatItemCustom ratItemCustom = ratItemCustomService.get(id);
        ratItemCustomService.deleteRatItemCustom(ratItemCustom);
        return success();
    }
    /**
     * 批量删除授信品种
     * 
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatItemCustom ratItemCustom = ratItemCustomService.get(id);
            ratItemCustomService.deleteEntity(ratItemCustom);
        }
        return success();
    }
}
