package com.bytter.check.web.controller;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareCheckType;
import com.bytter.check.service.FcCompareCheckTypeService;
import com.bytter.check.vo.FcCompareCheckTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FC_COMPARE_CHECK_TYPE
 * @date 2018-01-03 14:14:11
 */
@Controller
@RequestMapping("/finacial/paramconfig/fccomparechecktype")
public class FcCompareCheckTypeController extends BaseController {
    @Autowired
    private FcCompareCheckTypeService fcCompareCheckTypeService;
    /**
     * FC_COMPARE_CHECK_TYPE列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @LogMessage(content = "查询FC_COMPARE_CHECK_TYPE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "fcchecktype/listFcCompareCheckType";
    }
    /**
     * FC_COMPARE_CHECK_TYPE table列表
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcCompareCheckTypeService.getDataTablesList(dataTablesInput);
    }
    /**
     * FC_COMPARE_CHECK_TYPE新增页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @GetMapping("/add")
    public String add(FcCompareCheckType fcCompareCheckType, Model model) {
        if (StringUtils.isNotEmpty(fcCompareCheckType.getId())) {
            fcCompareCheckType = fcCompareCheckTypeService.get(fcCompareCheckType.getId());
            model.addAttribute("fcCompareCheckTypePage", fcCompareCheckType);
        }
        return "fcchecktype/addFcCompareCheckType";
    }
    /**
     * FC_COMPARE_CHECK_TYPE编辑页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @GetMapping("/update")
    public String update(FcCompareCheckType fcCompareCheckType, Model model) {
        if (StringUtils.isNotEmpty(fcCompareCheckType.getId())) {
            fcCompareCheckType = fcCompareCheckTypeService.get(fcCompareCheckType.getId());
            model.addAttribute("fcCompareCheckTypePage", fcCompareCheckType);
            model.addAttribute("checkTypes", fcCompareCheckTypeService.getcheckTypesBycheckTypeId(fcCompareCheckType.getId()).get("checkTypes"));
        }
        return "fcchecktype/updateFcCompareCheckType";
    }
    /**
     * FC_COMPARE_CHECK_TYPE详情页
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @LogMessage(content = "查询FC_COMPARE_CHECK_TYPE详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/enable/{id}")
    @ResponseBody
    public AjaxJson enable(@PathVariable("id") String id) {
        FcCompareCheckType fcCompareCheckType = fcCompareCheckTypeService.get(id);
        if (FinacialConstant.VALID_TRUE.equals(fcCompareCheckType.getValidSign())) {
            fcCompareCheckType.setValidSign(FinacialConstant.VALID_FALSE);
        } else {
            fcCompareCheckType.setValidSign(FinacialConstant.VALID_TRUE);
        }
        fcCompareCheckTypeService.saveOrUpdate(fcCompareCheckType);
        return success();
    }
    /**
     * 添加FC_COMPARE_CHECK_TYPE
     *
     * @param fcCompareCheckType
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @LogMessage(content = "添加FC_COMPARE_CHECK_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcCompareCheckType fcCompareCheckType) {
        fcCompareCheckTypeService.saveCompareCheckType(fcCompareCheckType, getUserId());
        return success();
    }
    /**
     * 修改FC_COMPARE_CHECK_TYPE
     *
     * @param fcCompareCheckType
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @LogMessage(content = "修改FC_COMPARE_CHECK_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcCompareCheckType fcCompareCheckType) {
        fcCompareCheckTypeService.updateCompareCheckType(fcCompareCheckType, getUserId());
        return success();
    }
    /**
     * 删除FC_COMPARE_CHECK_TYPE
     *
     * @param id
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY)
    @LogMessage(content = "删除FC_COMPARE_CHECK_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FcCompareCheckType fcCompareCheckType = fcCompareCheckTypeService.get(id);
        fcCompareCheckType.setStatus(CommonConstant.STATUE_DELETE);//逻辑删除
        fcCompareCheckTypeService.updateEntity(fcCompareCheckType);
        return success();
    }
    /**
     * 批量删除FC_COMPARE_CHECK_TYPE
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions("finacial.paramconfig:fccomparechecktype:delete")
    @LogMessage(content = "批量删除FC_COMPARE_CHECK_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            FcCompareCheckType fcCompareCheckType = fcCompareCheckTypeService.get(id);
            fcCompareCheckTypeService.deleteEntity(fcCompareCheckType);
        }
        return success();
    }
    /**
     * <b>功能介绍：</b>核对标准数据集<br>
     * <b>日期：</b>2018年1月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return
     * @version 1.0
     */
    @GetMapping("/json")
    @ResponseBody
    public List<FcCompareCheckTypeVO> json() {
        return fcCompareCheckTypeService.getCheckTypesJson();
    }
}
