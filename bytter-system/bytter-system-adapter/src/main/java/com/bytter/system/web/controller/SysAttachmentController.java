package com.bytter.system.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.query.SysAttachmentForm;
import com.bytter.system.service.ConfigService;
import com.bytter.system.service.SysAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: SYS_ATTACHMENT
 * @date 2017-05-26 12:20:21
 */
@Controller
@RequestMapping("/attachment/sysattachment")
public class SysAttachmentController extends BaseController {
    protected final String attachmentCacheName = "AttachmentCache";
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private ConfigService configService;
    @PostMapping("/upload")
    @ResponseBody
    public List<AttachmentDTO> upload(HttpServletRequest request) throws Exception {
        return sysAttachmentService.uploadAttachment(request, AttachmentCategoryEnum.CATEGORY_BILL);
    }
    /**
     * 预留印鉴上传接口
     */
    @PostMapping("/netBankUpload")
    @ResponseBody
    public List<AttachmentDTO> netBankUpload(HttpServletRequest request) throws Exception{
        return sysAttachmentService.uploadAttachment(request, AttachmentCategoryEnum.CATEGORY_NETBANK);

    }
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        sysAttachmentService.downloadAttachment(id, request, response);
    }
    /**
     * 文件分片上传
     * 不支持断点续传,分片传递完自动合并
     */
    @PostMapping("/webUploader")
    @ResponseBody
    public List<AttachmentDTO> webUploader(@ModelAttribute SysAttachmentForm attachmentForm) throws Exception {
        return sysAttachmentService.webUploader(attachmentForm);
    }
    //删除附件
    @PostMapping("/deleteFile")
    @ResponseBody
    public AjaxJson deleteFile(String id) throws IOException {
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isNotBlank(id)) {
            sysAttachmentService.deleteAttachment(id);
            ajaxJson.setSuccess(true);
        }
        return ajaxJson;
    }
    //获取附件列表
    @PostMapping("/getFileList")
    @ResponseBody
    public List<AttachmentDTO> getFileList(String id) throws IOException {
        return sysAttachmentService.findAttachmentByGroupId(id);
    }
    //获取最大上传数量
    @PostMapping("/getLimitNum")
    @ResponseBody
    public ConfigDTO getLimitNum(String type, String name) {
        return configService.getDTOByTypeAndName(type, name);
    }
    /**
     * SYS_ATTACHMENT 列表 页面跳转
     *
     * @return
     */
    //
    @LogMessage(content = "查询SYS_ATTACHMENT列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "attachment/listSysAttachment";
    }
    /**
     * SYS_ATTACHMENT table列表
     *
     * @return
     */
    //
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return sysAttachmentService.getDataTables(dataTablesInput);
    }
    /**
     * SYS_ATTACHMENT新增页面跳转
     *
     * @return
     */
    //
    @GetMapping("/add")
    public String add(SysAttachment sysAttachment, Model model) {
        if (StringUtils.isNotEmpty(sysAttachment.getId())) {
            sysAttachment = sysAttachmentService.get(sysAttachment.getId());
            model.addAttribute("sysAttachmentPage", sysAttachment);
        }
        return "attachment/addSysAttachment";
    }
    /**
     * SYS_ATTACHMENT编辑页面跳转
     *
     * @return
     */
    //
    @GetMapping("/update")
    public String update(SysAttachment sysAttachment, Model model) {
        if (StringUtils.isNotEmpty(sysAttachment.getId())) {
            sysAttachment = sysAttachmentService.get(sysAttachment.getId());
            model.addAttribute("sysAttachmentPage", sysAttachment);
        }
        return "attachment/updateSysAttachment";
    }
    /**
     * SYS_ATTACHMENT详情页
     *
     * @return
     */
    //
    @LogMessage(content = "查询SYS_ATTACHMENT详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        SysAttachment sysAttachment = sysAttachmentService.get(id);
        model.addAttribute("sysAttachmentPage", sysAttachment);
        return "attachment/viewSysAttachment";
    }
    /**
     * 添加SYS_ATTACHMENT
     *
     * @param sysAttachment
     * @return
     */
    //
    @LogMessage(content = "添加SYS_ATTACHMENT", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(SysAttachment sysAttachment) {
        sysAttachmentService.save(sysAttachment);
        return success();
    }
    /**
     * 修改SYS_ATTACHMENT
     *
     * @param sysAttachment
     * @return
     */
    //
    @LogMessage(content = "修改SYS_ATTACHMENT", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(SysAttachment sysAttachment) {
        SysAttachment t = sysAttachmentService.get(sysAttachment.getId());
        BeanUtils.copyNotNullProperties(sysAttachment, t);
        sysAttachmentService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除SYS_ATTACHMENT
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除SYS_ATTACHMENT", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        SysAttachment sysAttachment = sysAttachmentService.get(id);
        sysAttachmentService.deleteEntity(sysAttachment);
        return success();
    }
    /**
     * 批量删除SYS_ATTACHMENT
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除SYS_ATTACHMENT", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            SysAttachment sysAttachment = sysAttachmentService.get(id);
            sysAttachmentService.deleteEntity(sysAttachment);
        }
        return success();
    }
}
