package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatCreditType;
import com.bytter.credit.domain.service.RatCreditTypeService;
import com.bytter.credit.domain.vo.RatCreditTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 授信额度类型
 * @author onlineGenerator
 * @date 2017-07-20 15:44:28
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/cms/rat/ratcredittype")
public class RatCreditTypeController extends BaseController {
    @Autowired
    private RatCreditTypeService ratCreditTypeService;
    /**
     * 授信额度类型列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询授信额度类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/rat/credittype/listRatCreditType";
    }
    /**
     * 授信额度类型 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        dataTablesInput.addFilter(MatchTypeEnum.NE, "status", CommonConstant.STATUE_DELETE);
        return ratCreditTypeService.dataTables(dataTablesInput);
    }
    /**
     * 授信额度类型新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatCreditType ratCreditType, Model model) {
        if (StringUtils.isNotEmpty(ratCreditType.getId())) {
            ratCreditType = ratCreditTypeService.get(ratCreditType.getId());
            model.addAttribute("ratCreditTypePage", ratCreditType);
        }
        return "cms/rat/credittype/addRatCreditType";
    }
    /**
     * 授信额度类型编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatCreditType ratCreditType, Model model) {
        if (StringUtils.isNotEmpty(ratCreditType.getId())) {
            ratCreditType = ratCreditTypeService.get(ratCreditType.getId());
            model.addAttribute("ratCreditTypePage", ratCreditType);
        }
        return "cms/rat/credittype/updateRatCreditType";
    }
    /**
     * 授信额度类型详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信额度类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RatCreditType ratCreditType = ratCreditTypeService.get(id);
        RatCreditTypeVO ratCreditTypeVO = new RatCreditTypeVO();
        BeanUtils.copyNotNullProperties(ratCreditType, ratCreditTypeVO);
        model.addAttribute("ratCreditTypePage", ratCreditTypeVO);
        return "cms/rat/credittype/viewRatCreditType";
    }
    /**
     * 添加授信额度类型
     * 
     * @param ratCreditType
     * @return
     */
    @LogMessage(content = "添加授信额度类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatCreditType ratCreditType) {
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
    @ResponseBody
    public AjaxJson update(RatCreditType ratCreditType) {
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public List<RatCreditTypeVO> getAllCreditType() {
        return ratCreditTypeService.getAllCreditType();
    }
}
