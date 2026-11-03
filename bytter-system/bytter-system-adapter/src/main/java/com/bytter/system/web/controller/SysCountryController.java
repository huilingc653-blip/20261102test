package com.bytter.system.web.controller;
import com.bytter.basic.entity.SysCountry;
import com.bytter.basic.service.SysCountryService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 国家信息维护
 *
 * @author qinl
 * @date 2019-07-25
 */
@Controller
@RequestMapping("/basic/business/syscountry")
public class SysCountryController extends BaseController {
    /**
     * 国家信息业务层
     */
    @Autowired
    private SysCountryService sysCountryService;
    /**
     * <p>
     * （页面）国家信息列表页
     * </p>
     *
     * @return null
     */
    @GetMapping("/list")
    public String viewList() {
        SysCountry sysCountry = sysCountryService.get("070d46d8d2814adb8a437e67fed72397");
        return null;
    }
    /**
     * <p>
     * （数据）国家信息列表数据源
     * </p>
     *
     * @param dataTablesInput [封装数据]
     * @return DataTablesOutput
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput getDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return sysCountryService.getDataTable(dataTablesInput);
    }
    /**
     * <p>
     * （数据）获取国家集合的json数据
     * </p>
     *
     * @return List
     */
    @PostMapping("/jsonList")
    @ResponseBody
    public List getJsonList() {
        return sysCountryService.getList();
    }
    @GetMapping("/json")
    @ResponseBody
    public List json() {
        return sysCountryService.getList();
    }
    /**
     * <p>
     * （选择组件）国家信息选择页面
     * </p>
     *
     * @return selectSysCountry
     */
    @GetMapping("/select")
    public String select() {
        return "basic/business/selectSysCountry";
    }
}
