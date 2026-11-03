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
import com.bytter.credit.domain.entity.RatCreditType;
import com.bytter.credit.domain.service.RatCreditTypeService;
import com.bytter.credit.domain.vo.RatCreditTypeVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信额度类型
 * @date 2017-07-20 15:44:28
 *
 */
@Api(tags = "授信额度类型", value = "/v1-credit/cms/rat/ratcredittype")
@RestController("v1RatCreditTypeController")
@RequestMapping("/v1-credit/cms/rat/ratcredittype")
public class RatCreditTypeController extends BaseController {
    @Autowired
    private RatCreditTypeService ratCreditTypeService;

    /**
     * 授信额度类型 table列表
     *
     * @return
     */
    @PostMapping("/datatable")

    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.NE, "status", CommonConstant.STATUE_DELETE);
        return AjaxJson.success(ratCreditTypeService.dataTables(input));
    }

    /**
     * 授信额度类型编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<RatCreditType> update(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(ratCreditTypeService.get(id));
        }
        return AjaxJson.success();
    }

    /**
     * 授信额度类型详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信额度类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatCreditType> view(@PathVariable("id") String id) {
        return AjaxJson.success(ratCreditTypeService.get(id));
    }

    /**
     * 添加授信额度类型
     *
     * @param ratCreditType
     * @return
     */
    @LogMessage(content = "添加授信额度类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatCreditType ratCreditType) {
        ratCreditTypeService.saveOrUpdateCreditType(ratCreditType);
        return success();
    }

    /**
     * 修改授信额度类型
     *
     * @param ratCreditType
     * @return
     */
    @LogMessage(content = "修改授信额度类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatCreditType ratCreditType) {
        RatCreditType t = ratCreditTypeService.get(ratCreditType.getId());
        BeanUtils.copyNotNullProperties(ratCreditType, t);
        ratCreditTypeService.saveOrUpdateCreditType(t);
        return success();
    }

    /**
     * 删除授信额度类型
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信额度类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratCreditTypeService.deleteCreditType(id);
        return success();
    }

    /**
     * 批量删除授信额度类型
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信额度类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatCreditType ratCreditType = ratCreditTypeService.get(id);
            ratCreditTypeService.deleteEntity(ratCreditType);
        }
        return success();
    }

    /**
     * 功能介绍 : 获取所有额度类型。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/getAll")
    public AjaxJson<List<RatCreditTypeVO>> getAllCreditType() {
        return AjaxJson.success(ratCreditTypeService.getAllCreditType());
    }
}
