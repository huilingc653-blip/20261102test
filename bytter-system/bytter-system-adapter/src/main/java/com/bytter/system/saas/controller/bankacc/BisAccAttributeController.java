package com.bytter.system.saas.controller.bankacc;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.entity.BisAccAttribute;
import com.bytter.system.service.BisAccAttributeService;
import com.bytter.system.vo.BisAccAttributeVO;
import com.bytter.system.vo.BtAccNatureVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 账户属性表
 * @date 2017-05-12 17:18:17
 */
@Api(value = "v1-system/bankacc/bisaccattribute", tags = "账户属性维护")
@RestController("v1BisAccAttributeController")
@RequestMapping("/v1-system/bankacc/bisaccattribute")
public class BisAccAttributeController extends BaseController {

    @Autowired
    private BisAccAttributeService bisAccAttributeService;

    /**
     * 账户属性表 table列表
     *
     * @return
     */
    @ApiOperation("账户属性表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccAttributeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisAccAttributeService.dataTable(input));
    }

    /**
     * 账户属性表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("账户属性表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<BisAccAttribute> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
          return   AjaxJson.success(bisAccAttributeService.get(id));
        }
        return AjaxJson.success();
    }

    /**
     * 账户属性表详情页
     *
     * @return
     */
    @ApiOperation("账户属性表详情页")
    @LogMessage(content = "查询账户属性表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisAccAttributeVO> view(@PathVariable("id") String id) {
        BisAccAttributeVO bisAccAttribute = bisAccAttributeService.getVO(id);
        return AjaxJson.success(bisAccAttribute);
    }

    /**
     * 判断账户属性是否在使用中
     *
     * @return
     */

    @LogMessage(content = "判断账户属性是否在使用中", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/queryUse/{id}")
    @ResponseBody
    public AjaxJson<String> queryUse(@PathVariable("id") String id) {
        if (this.bisAccAttributeService.queryUse(id)) {   //使用中
            return AjaxJson.success("true");
        }
        return AjaxJson.success("false");   //未使用
    }


    /**
     * 添加账户属性表
     *
     * @param bisAccAttribute
     * @return
     */
    @ApiOperation("添加账户属性")
    @LogMessage(content = "添加账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisAccAttribute bisAccAttribute) {
        if (bisAccAttributeService.findByCode(bisAccAttribute.getAttributeCode()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_ACCATTR_MESSAGE_ATTRIBUTECODE_UNIQUE);
        }
        bisAccAttribute.setCreateBy(this.getCurrentUser().getLoginName());
        bisAccAttribute.setCreateDate(DateUtils.getDate());
        this.bisAccAttributeService.save(bisAccAttribute, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 修改账户属性表
     *
     * @param bisAccAttribute
     * @return
     */
    @ApiOperation("修改账户属性表")
    @LogMessage(content = "修改账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody BisAccAttribute bisAccAttribute) {
        BisAccAttribute t = bisAccAttributeService.get(bisAccAttribute.getId());
        BeanUtils.copyNotNullProperties(bisAccAttribute, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.bisAccAttributeService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 删除账户属性表
     *
     * @param id
     * @return
     */

    @ApiOperation("删除账户属性表")
    @LogMessage(content = "删除账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@ApiParam("id") @PathVariable("id") String id) {
        BisAccAttribute bisAccAttribute = bisAccAttributeService.get(id);
        bisAccAttributeService.deleteBisAccAttribute(bisAccAttribute);
        return success();
    }

    /**
     * 批量删除账户属性表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除账户属性表")
    @LogMessage(content = "批量删除账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        this.bisAccAttributeService.batchDelete(ids);
        return success();
    }

    @ApiOperation("获取json数据")
    @GetMapping("/json")
    @ResponseBody
    public AjaxJson<List<BisAccAttributeVO>> json() {
        return AjaxJson.success(bisAccAttributeService.getValidAttribute());
    }

}
