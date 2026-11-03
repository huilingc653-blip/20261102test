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
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.entity.BtAccNature;
import com.bytter.system.service.BtAccNatureService;
import com.bytter.system.vo.BtAccNatureVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 账户性质表
 * @date 2017-05-12 17:06:43
 */
@Api(value = "/v1-system/bankacc/btaccnature", tags = "账户性质维护")
@RestController("v1BtAccNatureController")
@RequestMapping("/v1-system/bankacc/btaccnature")
public class BtAccNatureController extends BaseController {
    @Autowired
    private BtAccNatureService btAccNatureService;

    /**
     * 账户性质表 table列表
     *
     * @return
     */
    @ApiOperation("账户性质表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtAccNatureVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Map<String, Object> vo = dataTablesInput.getParams();
        if (vo != null && !vo.isEmpty()) {
            Optional.ofNullable(vo.get("natureCode"))
                    .ifPresent(status -> input.addFormData(MatchTypeEnum.LIKE, "natureCode",
                            status, String.class));
            Optional.ofNullable(vo.get("natureName"))
                    .ifPresent(status -> input.addFormData(MatchTypeEnum.LIKE, "natureName",
                            status, String.class));
        }
        return AjaxJson.success(btAccNatureService.dataTable(input));
    }


    /**
     * 账户性质表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("账户性质表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<BtAccNature> update(@PathVariable("id") String id) {
        return AjaxJson.success(btAccNatureService.get(id));
    }

    @ApiOperation("查询账户性质表详情")
    @LogMessage(content = "查询账户性质表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BtAccNatureVO> view(@PathVariable("id") String id) {
        BtAccNatureVO btAccNature = btAccNatureService.getVO(id);
        return AjaxJson.success(btAccNature);
    }

    /**
     * 添加账户性质表
     *
     * @param btAccNature
     * @return
     */
    @ApiOperation("添加账户性质")
    @LogMessage(content = "添加账户性质表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BtAccNature btAccNature) {
        //判断账户性质编码是否重复
        boolean result = this.btAccNatureService.isRepeatCurCode(btAccNature.getId(), btAccNature.getNatureCode());
        if (result) {
            throw new ParameterException(NetbankMessageEnum.NIS_ACCNATURE_MESSAGE_NATURECODE_UNIQUE);
        }
        btAccNature.setCreateBy(this.getCurrentUser().getLoginName());
        btAccNature.setCreateDate(DateUtils.getDate());
        btAccNatureService.save(btAccNature, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 修改账户性质表
     *
     * @param btAccNature
     * @return
     */
    @ApiOperation("修改账户性质")
    @LogMessage(content = "修改账户性质表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BtAccNature btAccNature) {
        //判断账户性质编码是否重复
        boolean result = this.btAccNatureService.isRepeatCurCode(btAccNature.getId(), btAccNature.getNatureCode());
        if (result) {
            throw new ParameterException(NetbankMessageEnum.NIS_ACCNATURE_MESSAGE_NATURECODE_UNIQUE);
        }
        BtAccNature t = btAccNatureService.get(btAccNature.getId());
        BeanUtils.copyNotNullProperties(btAccNature, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.btAccNatureService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 删除账户性质表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除账户性质")
    @LogMessage(content = "删除账户性质表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@ApiParam("id") @PathVariable("id") String id) {
        BtAccNature btAccNature = btAccNatureService.get(id);
        btAccNatureService.deleteBtAccNature(btAccNature);
        return success();
    }

    /**
     * 批量删除账户性质表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除账户性质")
    @LogMessage(content = "批量删除账户性质表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@ApiParam("修改的数据ID数据，逗号,隔开") @RequestParam("ids") String ids) {
        this.btAccNatureService.batchDelete(ids);
        return success();
    }

    @ApiOperation("获取有效的账户属性")
    @GetMapping("/json")
    @ResponseBody
    public AjaxJson<List<BtAccNatureVO>> json() {
        return AjaxJson.success(btAccNatureService.getValidAccNature());
    }

}
