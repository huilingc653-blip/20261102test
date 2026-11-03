package com.bytter.system.web.controller;

import com.bytter.basic.entity.BtInputBankSwift;
import com.bytter.basic.service.BtInputBankSwiftService;
import com.bytter.basic.vo.BtInputBankSwiftVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.SysResourceConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName BtInputBankSwiftController
 * @Author zx
 * @Description 境内银行Swift Code管理
 * @Date 2026/1/21 13:53
 */
@Controller
@RequestMapping("/basic/business/btinputbankswift")
public class BtInputBankSwiftController extends BaseController {

    @Autowired
    private BtInputBankSwiftService btInputBankSwiftService;

    /**
     * 境内银行Swift Code管理页面跳转
     *
     * @param model
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_QUERY)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtInputBankSwift";
    }

    /**
     * 境内银行Swift Code管理数据查询
     *
     * @param dataTablesInput
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btInputBankSwiftService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 境内银行Swift Code管理新增页面跳转
     *
     * @param model
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_ADD)
    @GetMapping("/add")
    public String add(Model model) {
        return "basic/business/addBtInputBankSwift";
    }

    /**
     * 新增境内银行Swift Code管理数据
     *
     * @param btInputBankSwiftVO
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_ADD)
    @LogMessage(content = "新增境内银行Swift Code管理数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtInputBankSwiftVO btInputBankSwiftVO) {
        btInputBankSwiftService.saveBtInputBankSwift(btInputBankSwiftVO, getCurrentUser());
        return success();
    }

    /**
     * 境内银行Swift Code管理编辑页面跳转
     *
     * @param vo
     * @param model
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_EDIT)
    @GetMapping("/update")
    public String update(BtInputBankSwiftVO vo, Model model) {
        if (StringUtils.isNotEmpty(vo.getId())) {
            BtInputBankSwift btInputBankSwift = btInputBankSwiftService.get(vo.getId());
            model.addAttribute("btInputBankSwift", btInputBankSwift);
        }
        return "basic/business/updateBtInputBankSwift";
    }

    /**
     * 修改境内银行Swift Code管理
     *
     * @param vo
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_EDIT)
    @LogMessage(content = "修改境内银行Swift Code管理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(BtInputBankSwiftVO vo) {
        btInputBankSwiftService.modify(vo, getCurrentUser());
        return success();
    }

    /**
     * 境内银行Swift Code管理查看
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_QUERY)
    @LogMessage(content = "境内银行Swift Code管理查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtInputBankSwift btInputBankSwift = btInputBankSwiftService.get(id);
        model.addAttribute("btInputBankSwift", btInputBankSwift);
        return "basic/business/viewBtInputBankSwift";
    }

    /**
     * 删除境内银行Swift Code管理数据
     *
     * @param id
     * @return
     */
    @RequiresPermissions(SysResourceConstant.BT_INPUT_BANK_SWIFT_DELETE)
    @LogMessage(content = "删除境内银行Swift Code管理数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtInputBankSwift btInputBankSwift = btInputBankSwiftService.get(id);
        btInputBankSwiftService.deleteEntity(btInputBankSwift);
        return success();
    }

}
