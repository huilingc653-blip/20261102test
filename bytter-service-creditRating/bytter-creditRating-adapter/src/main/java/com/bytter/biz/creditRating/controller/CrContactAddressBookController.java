package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrContactAddressBookService;
import com.bytter.biz.creditRating.vo.CrContactAddressBookVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/credit/contactAddressBook")
public class CrContactAddressBookController extends BaseController {

    @Autowired
    private CrContactAddressBookService bookService;
    /**
     * 联系人通讯录 明细列表 - 数据
     */
    @PostMapping("/detailDatatable")
    @ResponseBody
    public List<CrContactAddressBookVo> detailDatatable(String ids) {
        if (StringUtils.isBlank(ids)){
            return null;
        }
        String[] allId = ids.split(",");
        List<String> arrayList = new ArrayList<>();
        for (String id : allId) {
            arrayList.add(id);
        }
        return bookService.findByIds(arrayList);
    }


    /**
     * 增加 联系人通讯录
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "creditRating/addContactAddressBook";
    }

    /**
     * 保存明细
     */
    @LogMessage(content = "联系人通讯录明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDetail")
    @ResponseBody
    public AjaxJson saveDetail(CrContactAddressBookVo detail) {
        AjaxJson ajaxJson = bookService.saveDetail(detail, getCurrentUser());
        return ajaxJson;
    }

    @LogMessage(content = "查询联系人通讯录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/viewContactAddressBook/{id}")
    public String viewOwnershipStructure(@PathVariable("id") String id , Model model) {
        if (StringUtils.isNotEmpty(id)) {
            CrContactAddressBookVo bookVo = bookService.getById(id);
            model.addAttribute("bookVo", bookVo);
        }
        return "creditRating/viewContactAddressBook";
    }

    @LogMessage(content = "更新联系人通讯录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/update/{id}")
    public String updateOwnershipStructure(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            CrContactAddressBookVo bookVo = bookService.getById(id);
            model.addAttribute("bookVo", bookVo);
        }
        return "creditRating/updateContactAddressBook";
    }

    @LogMessage(content = "删除联系人通讯录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteContactAddressBook/{id}")
    @ResponseBody
    public AjaxJson deleteContactAddressBook(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            bookService.delete(id);
        }
        return success();
    }

    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CrContactAddressBookVo> list) {
        return bookService.saveImport(list, getCurrentUser());
    }

    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, CrContactAddressBookVo vo) throws Exception {
        bookService.exportAll(request, response, vo);
    }

}
