package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaPledgeType;
import com.bytter.guarantee.domain.service.GuaPledgeTypeService;
import com.bytter.guarantee.domain.vo.GuaPledgeTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPledgeTypeController
 * @Description 抵质押物类型维护Controller
 * @date 2018年12月11日
 */
@Controller
@RequestMapping("/cms/guarantee/pledgeType")
public class GuaPledgeTypeController extends BaseController {
    @Autowired
    GuaPledgeTypeService guaPledgeTypeService;
    /** 列表页面跳转 **/
    @LogMessage(content = "查询GUA_PLEDGE_TYPE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/guarantee/listGuaPledgeType";
    }
    /** 列表内容 **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaPledgeTypeService.queryGuaPledgeType(dataTablesInput);
    }
    @GetMapping("/add")
    public String add(GuaPledgeType guaPledgeType, Model model) {
        return "cms/guarantee/addGuaPledgeType";
    }
    /**
     * 抵质押物类型编号
     * 
     * @return true-重复 false-不重复
     */
    @PostMapping("/checkTypeCode")
    @ResponseBody
    public boolean checkTypeCode(String typeCode, String id) {
        List<GuaPledgeType> typeList = guaPledgeTypeService.queryTypeByCode(typeCode);
        String typeId = null;
        if (typeList.size() > 0) {
            typeId = typeList.get(0).getId();// 编号不允许重复，最多只有一条
            if (StringUtils.isNotEmpty(id)) {// 编辑时需要与本来的编号做对比
                if (typeId.equals(id)) {
                    return false;
                } else {
                    return true;
                }
            } else {// 新增数据时根据编号查到抵质押物类型信息说明编号重复
                return true;
            }
        }
        return false;
    }
    @LogMessage(content = "增加抵质押物类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(GuaPledgeType guaPledgeType) {
        guaPledgeTypeService.saveOrUpdateGuaPledgeType(guaPledgeType, this.getCurrentUser());
        return success();
    }
    /**
     * 修改抵质押物类型页面跳转
     * 
     * @return
     */
    @LogMessage(content = "修改抵质押物类型页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @GetMapping("/update")
    public String update(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            guaPledgeTypeService.dateCheck(id);
            GuaPledgeType guaPledgeType = guaPledgeTypeService.getGuaPledgeTypeById(id);
            model.addAttribute("guaPledgeType", guaPledgeType);
        }
        return "cms/guarantee/updateGuaPledgeType";
    }
    /**
     * 修改抵质押物类型
     * 
     * @param guaPledgeType
     * @return
     */
    @LogMessage(content = "修改抵质押物类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(GuaPledgeType guaPledgeType) {
        GuaPledgeType entity = new GuaPledgeType();
        BeanUtils.copyNotNullProperties(guaPledgeType, entity);
        guaPledgeTypeService.saveOrUpdateGuaPledgeType(guaPledgeType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除授信品种类型
     * 
     * @param id
     * @return
     */
    @LogMessage(content = "删除授信品种类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaPledgeTypeService.deleteGuaPledgeType(id, this.getCurrentUser());
        return success();
    }
    /**
     * 抵质押物类型维护列表
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<GuaPledgeTypeVO> getListByValidSignNoPledgeAttr() {
        List<GuaPledgeTypeVO> list = guaPledgeTypeService.getListByValidSign();
        return list;
    }
    /**
     * 抵质押物类型维护列表
     *
     * @return
     */
    @GetMapping("/json/{typeAttr}")
    @ResponseBody
    public List<GuaPledgeTypeVO> getListByValidSignWithPledgeAttr(@PathVariable("typeAttr") String typeAttr) {
        List<GuaPledgeTypeVO> guaPledgeTypes = guaPledgeTypeService.getListByValidSign1(typeAttr);
        return guaPledgeTypes;
    }
    /**
     * 判断抵质押物类型能否被删除或是修改
     * 
     * @param id
     * @return
     */
    @PostMapping("/checkData")
    @ResponseBody
    public String checkData(String id) {
        if (guaPledgeTypeService.dateCheck(id)) {
            return "true";
        } else {
            return "false";
        }
    }
}
