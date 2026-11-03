package com.bytter.finance.web.basedata.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.service.AccrualTypeService;
import com.bytter.finance.domain.basedata.vo.AccrualTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: 计提方式维护
 * @Description: CMS_ACCRUAL_TYPE
 * @date 2022-08-10 15:47:15
 */
@Controller
@RequestMapping("/cms/accrual/accrualtype")
public class AccrualTypeController extends BaseController {
    @Autowired
    private AccrualTypeService accrualTypeService;
    /**
     * 查询计提方式列表
     **/
    @LogMessage(content = "查询CMS_ACCRUAL_TYPE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/accrualtype/listAccrualType";
    }
    /**
     * 查询计提方式数据
     **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return accrualTypeService.queryAccrualType(dataTablesInput);
    }
    /**
     * 新增页面跳转
     **/
    @GetMapping("/add")
    public String add(AccrualTypeVO accrualTypeVo, Model model) {
        if (StringUtils.isNotEmpty(accrualTypeVo.getId())) {
            accrualTypeVo = accrualTypeService.queryAccrualTypeById(accrualTypeVo.getId());
            model.addAttribute("accrualTypePage", accrualTypeVo);
        }
        return "cms/accrualtype/addAccrualType";
    }
    /**
     * 编辑、复制页面跳转
     **/
    @GetMapping("/update")
    public String update(AccrualTypeVO accrualTypeVo, String type, Model model) {
        accrualTypeVo = accrualTypeService.getisInput(accrualTypeVo);
        model.addAttribute("accrualTypePage", accrualTypeVo);
        model.addAttribute("type", type);
        return "cms/accrualtype/updateAccrualType";
    }
    /**
     * 计提方式详情页
     *
     * @return
     */
    @LogMessage(content = "查询计提方式详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        AccrualTypeVO accrualTypeVo = accrualTypeService.queryAccrualTypeById(id);
        model.addAttribute("accrualTypePage", accrualTypeVo);
        return "cms/accrualtype/viewAccrualType";
    }
    /**
     * 增加计提方式
     **/
    @LogMessage(content = "添加CMS_ACCRUAL_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AccrualTypeVO accrualTypeVo) {
        accrualTypeVo.setStatus(BaseDataConstant.STATUS_REDACT);
        accrualTypeService.saveAccrualType(accrualTypeVo, getCurrentUser());
        return success();
    }
    /**
     * 编辑计提方式
     **/
    @LogMessage(content = "修改CMS_ACCRUAL_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(AccrualTypeVO accrualTypeVo) {
        accrualTypeService.updateAccrualType(accrualTypeVo, getCurrentUser());
        return success();
    }
    /**
     * 删除计提方式
     **/
    @LogMessage(content = "删除CMS_ACCRUAL_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        // 是否已被引用，否则不可删除
        boolean flag = accrualTypeService.judgeAccrualTypeByCode(id);
        if (!flag) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
        }
        accrualTypeService.deleteAccrualType(id);
        return success();
    }
    /**
     * 获取计提方式
     */
    @GetMapping("/queryAccrualTypeList")
    @ResponseBody
    public List<AccrualTypeVO> queryAccrualTypeList() {
        return accrualTypeService.queryAccrualTypeList();
    }
    /**
     * 启用/禁用
     *
     * @param id
     *            主键id
     * @param validSign
     *            启用/禁用状态
     * @return
     */
    @PostMapping("/updateValidSign")
    @ResponseBody
    public AjaxJson updateValidSign(String id, String validSign) {
        accrualTypeService.setValidSign(id, validSign);
        return success();
    }
}
