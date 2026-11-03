package com.bytter.system.web.controller;
import com.bytter.basic.entity.SysCountryCust;
import com.bytter.basic.entity.SysTimeZone;
import com.bytter.basic.service.SysCountryCustService;
import com.bytter.basic.service.SysTimeZoneService;
import com.bytter.basic.vo.SysCountryCustVO;
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
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 国家地区表
 * @date 2020-1-19 11:14:22
 */
@Controller
@RequestMapping("/basic/business/syscountrycust")
public class SysCountryCustController extends BaseController {
    @Autowired
    private SysCountryCustService sysCountryCustService;
    @Autowired
    private SysTimeZoneService sysTimeZoneService;
    /**
     * 国家地区表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询国家地区表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listSysCountryCust";
    }
    /**
     * 国家地区表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return sysCountryCustService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 国家地区表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(SysCountryCust sysCountryCust, Model model) {
        if (StringUtils.isNotEmpty(sysCountryCust.getId())) {
            sysCountryCust = sysCountryCustService.get(sysCountryCust.getId());
            model.addAttribute("sysCountryCustPage", sysCountryCust);
        }
        return "basic/business/addSysCountryCust";
    }
    /**
     * 国家地区表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(SysCountryCust sysCountryCust, Model model) {
        if (StringUtils.isNotEmpty(sysCountryCust.getId())) {
            SysCountryCustVO vo = this.sysCountryCustService.getByUnion(sysCountryCust.getId());
            model.addAttribute("sysCountryCustPage", vo);
            List<SysCountryCustVO> list = this.sysCountryCustService.listVOBySysId(vo.getId());
            model.addAttribute("dataList", list);
            model.addAttribute("dataListSize", list.size());
        }
        return "basic/business/updateSysCountryCust";
    }
    /**
     * 国家地区表详情页
     *
     * @return
     */
    @LogMessage(content = "查询国家地区表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        SysCountryCustVO vo = sysCountryCustService.getByUnion(id);
        model.addAttribute("sysCountryCustPage", vo);
        List<SysCountryCustVO> list = this.sysCountryCustService.listVOBySysId(vo.getId());
        model.addAttribute("dataList", list);
        return "basic/business/viewSysCountryCust";
    }
    /**
     * 添加国家地区表
     *
     * @param sysCountryCust
     * @return
     */
    @LogMessage(content = "添加国家地区表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(SysCountryCustVO sysCountryCust,
                         @RequestParam("nameZhs[]") List<String> nameZhs, @RequestParam("nameEns[]") List<String> nameEns,
                         @RequestParam("codes[]") List<String> codes, @RequestParam("timeZones[]") List<String> timeZones) {
        sysCountryCustService.saveSysCountryCust(sysCountryCust, nameZhs, nameEns, codes, timeZones, getUserLoginName());
        return success();
    }
    /**
     * 修改国家地区表
     *
     * @param sysCountryCust
     * @return
     */
    @LogMessage(content = "修改国家地区表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(SysCountryCustVO sysCountryCust, @RequestParam("ids[]") List<String> ids,
                           @RequestParam("nameZhs[]") List<String> nameZhs, @RequestParam("nameEns[]") List<String> nameEns,
                           @RequestParam("codes[]") List<String> codes, @RequestParam("timeZones[]") List<String> timeZones) {
        sysCountryCustService.updateSysCountryCust(sysCountryCust, ids, nameZhs, nameEns, codes, timeZones, getUserLoginName());
        return success();
    }
    /**
     * 删除国家地区表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除国家地区表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        SysCountryCust sysCountryCust = sysCountryCustService.get(id);
        sysCountryCustService.deleteEntity(sysCountryCust);
        //同步删除国家下面的地区
        List<SysCountryCust> list = this.sysCountryCustService.listBySysId(sysCountryCust.getId());
        for (SysCountryCust entity : list) {
            sysCountryCustService.deleteEntity(entity);
        }
        return success();
    }
    /**
     * 获取系统时区
     *
     * @return
     */
    @GetMapping("/getSysTimeZone")
    @ResponseBody
    public List<SysTimeZone> json() {
        return sysTimeZoneService.listAll();
    }
    /**
     * 获取有效的国家信息
     *
     * @return
     */
    @RequestMapping("/listSysCountryCust")
    @ResponseBody
    public List<SysCountryCustVO> listSysCountryCust() {
        List<SysCountryCustVO> list = sysCountryCustService.listByCountryLevel(CommonConstant.NO);
        return list;
    }
    /**
     * 获取有效的所有城市地区信息
     *
     * @return
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public List<SysCountryCustVO> listAll() {
        List<SysCountryCustVO> list = sysCountryCustService.listByCountryLevel(CommonConstant.YES);
        return list;
    }
    /**
     * 获取有效的地区信息
     *
     * @return
     */
    @RequestMapping("/listAllSysCountryArea")
    @ResponseBody
    public List<SysCountryCustVO> listAllSysCountryArea() {
        List<SysCountryCustVO> list = sysCountryCustService.listByCountryLevel(CommonConstant.YES);
        return list;
    }
    /**
     * 获取国家ID获取地区信息
     *
     * @return
     */
    @RequestMapping("/listSysCountryArea/{sysId}")
    @ResponseBody
    public List<SysCountryCustVO> listSysCountryArea(@PathVariable("sysId") String sysId) {
        List<SysCountryCustVO> list = sysCountryCustService.listVOBySysId(sysId);
        return list;
    }
    /**
     * 获取国家ID获取地区信息
     *
     * @param sysId
     * @return
     */
    @RequestMapping(params = "getSysCountryCustBySysId")
    @ResponseBody
    public AjaxJson getSysCountryCustBySysId(String sysId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(sysCountryCustService.listVOBySysId(sysId));
        return j;
    }
}
