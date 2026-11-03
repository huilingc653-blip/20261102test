package com.bytter.system.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.entity.Dict;
import com.bytter.system.entity.DictGroup;
import com.bytter.system.service.DictService;
import com.bytter.system.service.I18nService;
import com.bytter.system.vo.DictVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 字典管理Controller
 *
 * @author zhoul
 * @date 2017/4/11
 */

@Controller
@RequestMapping("/sys/dict")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    @Autowired
    @Qualifier("i18nServiceImpl")
    private Ii18nService i18nServiceI;

    @Autowired
    @Qualifier("i18nService")
    private I18nService all18nService;

    //@RequiresPermissions("sys:dict:view")
    @GetMapping("/list")
    public void index(Model model) {
    }

    @GetMapping("/list.json")
    @ResponseBody
    public Object listJson() {
        return dictService.listAllDictGroup();
    }

    @PostMapping("/datatable.json")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return dictService.getDataTables(dataTablesInput);
    }

    //@RequiresPermissions("sys:dict:tableGrid")
    @PostMapping("/tableGrid.json")
    @ResponseBody
    public TableGridResponse<Dict> tableGrid(@RequestBody TableGridInput tableGridInput) {
        return dictService.getTreeGrid(tableGridInput);
    }

    //@RequiresPermissions("sys:dict:view")
    @GetMapping("/view")
    public void view(String id, String type, Model model) {
        model.addAttribute("dictGroupList", dictService.listAllDictGroup());
        model.addAttribute("type", type);
        if (StringUtils.isNotEmpty(id)) {
            Dict dict = dictService.get(id);
            model.addAttribute("dict", dict);
        }
    }

    //@RequiresPermissions("sys:dict:save")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(Dict dict) {
        dict.setCreateName(this.getUserId());
        dictService.saveDict(dict);
        return success();
    }

    /**
     * 多语言列表页面跳转
     *
     * @return
     */
    //@RequiresPermissions("sys:dict:update")
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(Dict dict) {
        dict.setCreateName(this.getUserId());
        dictService.updateDict(dict);
        return success();
    }

    //@RequiresPermissions("sys:dict:delete")
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("dictIds[]") List<String> dictIds) {
        dictService.deleteDict(dictIds);
        return success();
    }

    @RequestMapping("/dictlist")
    @ResponseBody
    public List<DictDTO> dictlist(@RequestParam("code[]") List<String> codes) {
        List<DictDTO> result = new ArrayList<DictDTO>();
        if (!EmptyUtils.isEmptyList(codes)) {
            for (String code : codes) {
                List<DictVO> dictList = dictService.getDictGroupByCode(code);
                List<DictDTO> list = BeanUtils.copyListProperties(dictList, DictDTO.class);
                result.addAll(list);
            }
        }
        return result;
    }

    @RequestMapping("/listByGroupCode")
    @ResponseBody
    public List<DictVO> listByGroupCode(String code) {
        return dictService.getDictGroupByCode(code);
    }

    /**
     * 根据单位ID和资源代码获取对应的用户信息
     */
    @PostMapping("/allDictGroup")
    @ResponseBody
    public List<DictGroup> allDictGroup() {
        Locale locale = LocaleContextHolder.getLocale();
        List<DictGroup> groupList = all18nService.getALLBusinessBaseContext(locale.toString());
        return groupList;
    }

    /**
     * 数据字典组分页查询
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatableDictGroup.json")
    @ResponseBody
    public DataTablesOutput dataTableDictGroup(@RequestBody DataTablesInput dataTablesInput) {
        return dictService.dataTableDictGroup(dataTablesInput);
    }

}
