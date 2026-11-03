package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolifService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolifVO;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 票据池接口Controller
 * @Description: 票据池接口控制器类
 * @date 2019-12-30
 */
@Controller
@RequestMapping("/billpool/poolif")
public class AbPoolifController extends BaseController {
    @Autowired
    private AbPoolifService abPoolifService;
    /**
     * 票据池接口列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolif";
    }
    /**
     * 票据池接口 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolifService.getDataTablesUnion(input);
    }
    /**
     * 票据池接口新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "billPool/addAbPoolif";
    }
    /**
     * 保存票据池接口
     *
     * @param abPoolifVO
     * @return
     */
    @LogMessage(content = "保存票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolifVO abPoolifVO, HttpServletRequest request) {
        abPoolifService.saveAbPoolif(abPoolifVO, this.getCurrentUser());
        return success();
    }
    /**
     * 票据池接口编辑页面跳转
     *
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        AbPoolifVO abPoolifVO = abPoolifService.getAbPoolifVO(id);
        model.addAttribute("abPoolifVO", abPoolifVO);
        return "billPool/updateAbPoolif";
    }
    /**
     * 修改票据池接口
     *
     * @param abPoolifVO
     * @return
     */
    @LogMessage(content = "修改票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(AbPoolifVO abPoolifVO, HttpServletRequest request) {
        abPoolifService.updateAbPoolifByAbPoolifVO(abPoolifVO, this.getCurrentUser());
        return success();
    }
    /**
     * 删除票据池接口
     *
     * @param ids 池接口ids
     * @return
     */
    @LogMessage(content = "删除票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(String ids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolifService.batchDelete(ids, this.getCurrentUser()));
        return success();
    }
    /**
     * 修改票据池接口状态
     *
     * @param id     池接口id
     * @param status 池接口状态
     * @return
     */
    @LogMessage(content = "修改票据池接口状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateStatus")
    @ResponseBody
    public AjaxJson updateStatus(String id, String status) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolifService.updateStatus(id, status, this.getCurrentUser()));
        return success();
    }
    /**
     * 判断是否有相同的接口代码、接口名称、银行接口代码
     *
     * @param pifCode 接口代码
     * @param pifName 接口名称
     * @param id      接口id
     * @param bifCode 银行接口代码
     * @return
     */
    @PostMapping("/checkPifCodeOrPifName")
    @ResponseBody
    public AjaxJson checkPifCodeOrPifName(String pifCode, String pifName, String id, String bifCode) {
        AjaxJson j = success();
        j.setSuccess(abPoolifService.checkPifCodeOrPifNameOrBifcode(pifCode, pifName, id, bifCode));
        return j;
    }
    /**
     * 获取维护的启用状态的票据池接口
     *
     * @return
     */
    @GetMapping("/getAllPoolif")
    @ResponseBody
    public List<AbPoolifVO> getAllPoolif(AbPoolifVO abPoolifVO) {
        return abPoolifService.getAllPoolif(abPoolifVO);
    }
}
