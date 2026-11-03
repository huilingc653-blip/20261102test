package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatItemType;
import com.bytter.credit.domain.service.RatItemTypeService;
import com.bytter.credit.dto.RatItemTypeDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信品种
 * @date 2017-07-20 14:52:52
 *
 */

@Api(tags = "授信品种", value = "/v1-credit/cms/rat/ratitemtype")
@RestController("v1RatItemTypeController")
@RequestMapping("/v1-credit/cms/rat/ratitemtype")
public class RatItemTypeController extends BaseController {
    @Autowired
    private RatItemTypeService ratItemTypeService;

    /**
     * 授信品种 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.NE, "status", CommonConstant.STATUE_DELETE);
        return AjaxJson.success(ratItemTypeService.dataTables(input));
    }

    /**
     * 授信品种编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<RatItemType> update(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(ratItemTypeService.get(id));
        }
        return AjaxJson.success();
    }

    /**
     * 授信品种详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信品种详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatItemType> view(@PathVariable("id") String id) {
        return AjaxJson.success(ratItemTypeService.get(id));
    }

    /**
     * 添加授信品种
     *
     * @param ratItemType
     * @return
     */
    @LogMessage(content = "添加授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatItemType ratItemType) {
        ratItemTypeService.saveOrUpdateItemType(ratItemType);
        return success();
    }

    /**
     * 修改授信品种
     *
     * @param ratItemType
     * @return
     */
    @LogMessage(content = "修改授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatItemType ratItemType) {
        RatItemType t = ratItemTypeService.get(ratItemType.getId());
        BeanUtils.copyNotNullProperties(ratItemType, t);
        ratItemTypeService.saveOrUpdateItemType(t);
        return success();
    }

    /**
     * 删除授信品种类型
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信品种类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratItemTypeService.deleteItemType(id);
        return success();
    }

    /**
     * 批量删除授信品种
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信品种", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatItemType ratItemType = ratItemTypeService.get(id);
            ratItemTypeService.deleteEntity(ratItemType);
        }
        return success();
    }

    /**
     * 功能介绍 : 获取所有有效的授信业务品种类型。<br>
     * <b>日期：</b>2017年7月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/getAll")
    @ResponseBody
    public AjaxJson<List<RatItemTypeDTO>> getAll(@ModelAttribute("ids") String ids) {
        return AjaxJson.success(ratItemTypeService.getAllRatItemType());
    }
}
