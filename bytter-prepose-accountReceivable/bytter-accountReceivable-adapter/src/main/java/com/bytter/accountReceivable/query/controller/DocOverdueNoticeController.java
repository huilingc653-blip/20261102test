package com.bytter.accountReceivable.query.controller;

import com.bytter.accountReceivable.query.service.DocOverdueNoticeService;
import com.bytter.accountReceivable.query.vo.DocOverdueNoticeVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *	逾期催收登记表
 */
@Controller
@RequestMapping("/accountReceivable/query/docOverdueNotice")
public class DocOverdueNoticeController extends BaseController {

    @Autowired
    private DocOverdueNoticeService docOverdueNoticeService;

    /**
     * 逾期催收登记表 列表页面跳转
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_QUERY)
    @LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "query/listDocOverdueNotice";
    }

    /**
     * 查询列表
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input){
        return docOverdueNoticeService.datatable(input, this.getCurrentUser());
    }

    /**
     * 跳转 逾期催收登记表-查看页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_QUERY)
    @GetMapping("/view")
    public String view(Model model, String docId) {
        model.addAttribute("docId", docId);
        return "query/viewDocOverdueNotice";
    }

    /**
     * 跳转 逾期催收登记表-编辑页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_UPDATE)
    @GetMapping("/edit")
    public String edit(Model model, String docId) {
        model.addAttribute("docId", docId);
        return "query/editDocOverdueNotice";
    }

    /**
     * 逾期催收登记列表
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_UPDATE)
    @PostMapping("/datatableNotice")
    @ResponseBody
    public DataTablesOutput datatableNotice(String docId){
        return docOverdueNoticeService.queryOverDueNoticeByDocId(docId, this.getCurrentUser());
        //return docOverdueNoticeService.getOverDueNoticeByDocId(docId, this.getCurrentUser());
    }

//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_UPDATE)
    @LogMessage(content = "新增逾期催收登记",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DocOverdueNoticeVo noticeVo) {
        docOverdueNoticeService.saveNoticeList(noticeVo,getCurrentUser());
        return success();
    }

}
