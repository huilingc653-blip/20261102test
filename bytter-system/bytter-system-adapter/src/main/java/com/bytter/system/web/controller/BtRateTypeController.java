package com.bytter.system.web.controller;
import com.bytter.basic.entity.BtRateType;
import com.bytter.basic.service.BtRateTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 汇率类型
 * @date 2020-1-19 11:14:22
 */
@Controller
@RequestMapping("/basic/business/btratetype")
public class BtRateTypeController extends BaseController {
    @Autowired
    private BtRateTypeService btRateTypeService;
    @Autowired
    private Ii18nService i18nServiceI;
    /**
     * 汇率类型列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询汇率类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtRateType";
    }
    /**
     * 汇率类型 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btRateTypeService.getDataTables(dataTablesInput);
    }
    /**
     * 汇率类型新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(BtRateType btRateType, Model model) {
        if (StringUtils.isNotEmpty(btRateType.getId())) {
            btRateType = btRateTypeService.get(btRateType.getId());
            model.addAttribute("btRateTypePage", btRateType);
        }
        return "basic/business/addBtRateType";
    }
    /**
     * 汇率类型编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(BtRateType btRateType, Model model) {
        if (StringUtils.isNotEmpty(btRateType.getId())) {
            BtRateType entity = this.btRateTypeService.get(btRateType.getId());
            model.addAttribute("btRateTypePage", entity);
        }
        return "basic/business/updateBtRateType";
    }
    /**
     * 汇率类型详情页
     *
     * @return
     */
    @LogMessage(content = "查询汇率类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("btRateTypePage", btRateTypeService.get(id));
        return "basic/business/viewBtRateType";
    }
    /**
     * 添加汇率类型
     *
     * @param btRateType
     * @return
     */
    @LogMessage(content = "添加汇率类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtRateType btRateType) {
        if (btRateTypeService.isExistRateType(btRateType.getTypeName(), null)) {
            throw new ParameterException(CurrencyMessageEnum.BTRATETYPE_MESSAGE_TYPENAME_UNIQUE);
        }
        btRateTypeService.saveBtRateType(btRateType, getUserLoginName(), LocaleContextHolder.getLocale().toString());
        return success();
    }
    /**
     * 修改汇率类型
     *
     * @param btRateType
     * @return
     */
    @LogMessage(content = "修改汇率类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtRateType btRateType) {
        if (btRateTypeService.isExistRateType(btRateType.getTypeName(), btRateType.getId())) {
            throw new ParameterException(CurrencyMessageEnum.BTRATETYPE_MESSAGE_TYPENAME_UNIQUE);
        }
        BtRateType t = btRateTypeService.get(btRateType.getId());
        BeanUtils.copyNotNullProperties(btRateType, t);
        btRateTypeService.updateBtRateType(t, getUserLoginName(), LocaleContextHolder.getLocale().toString());
        return success();
    }
    /**
     * 删除汇率类型
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除汇率类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        //校验数据是否被引用
        btRateTypeService.checkAssociated(id);
        BtRateType btRateType = btRateTypeService.get(id);
        btRateTypeService.deleteEntity(btRateType);
        return success();
    }
    /**
     * 获取所有有效汇率类型
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BtRateType> json() {
        List<BtRateType> list = btRateTypeService.listByValidSign();
        for (BtRateType btRateType : list) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(btRateType.getId());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                btRateType.setTypeName(businessBaseContext);
            }
        }
        return list;
    }
    /**
     * 获取币别汇率
     *
     * @return
     */
    @PostMapping("/getRateDecimalPlaces")
    @ResponseBody
    public Integer getRateDecimalPlaces(String typeId) {
        Integer decimalPlaces = 4;
        if (EmptyUtils.isBlank(typeId)) {
            typeId = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        }
        BtRateType entity = this.btRateTypeService.get(typeId, true);
        if (!EmptyUtils.isEmpty(entity)) {
            decimalPlaces = entity.getDecimalPlaces();
        }
        return decimalPlaces;
    }
}
