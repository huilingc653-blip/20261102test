package com.bytter.system.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IBlackWhiteService;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.dto.BlackWhiteDTO;
import com.bytter.system.service.BlackWhiteListService;
import com.bytter.system.vo.BlackWhiteListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 黑白名单controll
 */
@Controller
@RequestMapping("/blackwhite/configure")
public class BlackWhiteListController extends BaseController {
    @Autowired
    public BlackWhiteListService blackWhiteListService;
   @Autowired
   public IBlackWhiteService blackWhiteServiceImpl;
    /**
     * 黑/白名单配置
     *
     * @return
     */
    @GetMapping("/{busType}/list")
    public String blackList(Model model, @PathVariable("busType") String busType) {
        model.addAttribute("intercept", SysBlackWhiteConstant.getBwType(busType));
        return "sys/blackWhite/listBlackWhite";
    }
    /**
     * table/列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    @LogMessage(content = "黑/白名单table", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return blackWhiteListService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 详情
     *
     * @return
     */
    @GetMapping("/view/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        blackWhiteListService.getInfoVo(id, model);
        return "sys/blackWhite/viewBlackWhite";
    }
    /**
     * 跳转到新增
     *
     * @return
     */
    @GetMapping("/add/{operate}")
    public String add(Model model, @PathVariable("operate") String operate) {
        model.addAttribute("intercept", operate);
        model.addAttribute("operator", AccebillConstant.BILL_OPERATE_ADD);
        return "sys/blackWhite/addBlackWhite";
    }
    /**
     * @return
     */
    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable("id") String id) {
        model.addAttribute("operator", AccebillConstant.BILL_OPERATE_UPDATE);
        blackWhiteListService.getInfoVo(id, model);
        return "sys/blackWhite/addBlackWhite";
    }
    /**
     * 新增/修改
     *
     * @param
     * @return
     */
    @LogMessage(content = "新增-黑白名单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdate(@PathVariable("operator") String operator, BlackWhiteListVO blackWhiteListVO) {
        return blackWhiteListService.saveOrUpdate(operator, blackWhiteListVO, getCurrentUser());
    }
    @LogMessage(content = "删除-黑白名单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
    @PostMapping("/batchDel")
    @ResponseBody
    public AjaxJson batchdata(String ids) {
        return blackWhiteListService.batchdata(ids, this.getCurrentUser());
    }
    /**
     * 导入保证申请
     *
     * @param list
     * @return
     */
    @LogMessage(content = "导入黑白名单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BlackWhiteListVO> list, String intercept) {
        AjaxJson json = success();
        boolean flag = blackWhiteListService.saveImport(list, this.getCurrentUser(), intercept);
        json.setSuccess(flag);
        return success();
    }
    @GetMapping("/importConf/{intercept}")
    public String importConf(Model model, @PathVariable("intercept") String intercept) {
        model.addAttribute("intercept", intercept);
        return "sys/blackWhite/importConf";
    }
    /**
     * 获取黑名单收付限制配置信息
     *
     * @return
     */
    @GetMapping("/blacklistCheckType")
    @ResponseBody
    public String getBlacklistCheckType() {
        return blackWhiteServiceImpl.getBlacklistCheck();
    }
    /**
     * 前端客商和承兑行校验
     *
     * @return
     */
    @PostMapping("/blackWhiteCheck")
    @ResponseBody
    public BlackWhiteDTO blackWhiteCheck(@RequestBody BlackWhiteListVO blackWhiteListVO) {
        return blackWhiteServiceImpl.blackWhiteCheckBatch(blackWhiteListVO.getDtoList(), blackWhiteListVO.getIsControl(), blackWhiteListVO.getListType());
    }
}
