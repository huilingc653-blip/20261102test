package com.bytter.biz.netbank.controller;

import com.bytter.biz.netbank.entity.NisShareBusType;
import com.bytter.biz.netbank.service.NisShareBusTypeService;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.netbank.vo.NisShareBusTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 共享业务类型维护控制层
 *
 * @author fangcf
 * @date 2025-05-15
 */
@Controller
@RequestMapping("/netbank/nissharebustype")
public class NisShareBusTypeController extends BaseController {

    @Autowired
    private NisShareBusTypeService nisShareBusTypeService;

    /**
     * 共享业务类型列表 页面跳转
     *
     * @return 共享业务类型列表
     */
    @LogMessage(content = "查询共享业务类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listNisShareBusType";
    }

    /**
     * 共享业务类型 table列表
     *
     * @return 共享业务类型数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return nisShareBusTypeService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 新增共享业务类型
     *
     * @param model model对象
     * @return 路径
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "netbank/addNisShareBusType";
    }

    /**
     * 新增共享业务类型
     *
     * @param model model对象
     * @return 路径
     */
    @GetMapping("/update")
    public String update(NisShareBusType entity, Model model) {
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = nisShareBusTypeService.get(entity.getId(), true);
            model.addAttribute("entity", entity);
        }
        return "netbank/updateNisShareBusType";
    }

    /**
     * 新增共享业务类型
     *
     * @param vo form表单
     * @return 操作结果
     */
    @LogMessage(content = "新增共享业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisShareBusTypeVO vo) {
        nisShareBusTypeService.saveShareBusType(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 修改共享业务类型
     *
     * @param vo form表单
     * @return 操作结果
     */
    @LogMessage(content = "修改共享业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(NisShareBusTypeVO vo) {
        nisShareBusTypeService.updateShareBusType(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 删除共享业务类型
     *
     * @param id 主键id
     * @return 操作结果
     */
    @LogMessage(content = "删除共享业务类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        nisShareBusTypeService.deleteShareBusType(id);
        return success();
    }

    /**
     * 获取“收付款标志=付款，有效标志=有效”的业务种类
     *
     * @param cdSign 收付款标志
     * @return 业务类型数据集合
     */
    @GetMapping("/listValidBusTypeByCdSign")
    @ResponseBody
    public List<NisBusTypeVO> listValidBusTypeByCdSign(String cdSign) {
        return nisShareBusTypeService.listValidBusTypeByCdSign(cdSign);
    }

    /**
     * 根据业务类别获取所有正常的共享业务类型
     *
     * @param busTypeId 业务类型id
     * @return 共享业务类型集合
     */
    @GetMapping("/listValidShareBusTypeByBusType")
    @ResponseBody
    public List<NisShareBusType> listValidShareBusTypeByBusType(String busTypeId) {
        return nisShareBusTypeService.listValidShareBusTypeByBusType(busTypeId);
    }
}
