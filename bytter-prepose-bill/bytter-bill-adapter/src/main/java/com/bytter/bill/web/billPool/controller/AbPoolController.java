package com.bytter.bill.web.billPool.controller;

import com.bytter.bill.domain.billPool.service.AbPoolService;
import com.bytter.bill.domain.billPool.vo.AbPoolVO;
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
 * @Title: 票据池Controller
 * @Description: 票据池控制器类
 * @date 2020-1-3
 */
@Controller
@RequestMapping("/billpool/pool")
public class AbPoolController extends BaseController {
    @Autowired
    private AbPoolService abPoolService;
    /**
     * 票据池列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPool";
    }
    /**
     * 票据池 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolService.getDataTablesUnion(input);
    }
    /**
     * 票据池新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "billPool/addAbPool";
    }
    /**
     * 保存票据池
     *
     * @param abPoolVO
     * @return
     */
    @LogMessage(content = "保存票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbPoolVO abPoolVO, HttpServletRequest request) {
        abPoolService.saveAbPool(abPoolVO, this.getCurrentUser());
        return success();
    }
    /**
     * 票据池编辑页面跳转
     *
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        AbPoolVO abPoolVO = abPoolService.getAbPoolVO(id);
        model.addAttribute("abPoolVO", abPoolVO);
        return "billPool/updateAbPool";
    }
    /**
     * 票据池查看页面跳转
     *
     * @return
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        AbPoolVO abPoolVO = abPoolService.getAbPoolVO(id);
        model.addAttribute("abPoolVO", abPoolVO);
        return "billPool/viewAbPool";
    }
    /**
     * 修改票据池
     *
     * @param abPoolVO
     * @return
     */
    @LogMessage(content = "修改票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(AbPoolVO abPoolVO, HttpServletRequest request) {
        abPoolService.updateAbPoolByAbPoolVO(abPoolVO, this.getCurrentUser());
        return success();
    }
    /**
     * 删除票据池
     *
     * @param ids 票据池ids
     * @return
     */
    @LogMessage(content = "删除票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(String ids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolService.batchDelete(ids, this.getCurrentUser()));
        return success();
    }
    /**
     * 修改票据池状态
     *
     * @param id     票据池id
     * @param status 票据池状态
     * @return
     */
    @LogMessage(content = "修改票据池状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateStatus")
    @ResponseBody
    public AjaxJson updateStatus(String id, String status) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abPoolService.updateStatus(id, status, this.getCurrentUser()));
        return success();
    }
    /**
     * 判断是否有相同的接口代码或接口名称
     *
     * @param bpCode 票据池代码
     * @param bpName 票据池名称
     * @param id     票据池id
     * @param pifId  池接口id
     * @return
     */
    @PostMapping("/checkBpCodeOrBpName")
    @ResponseBody
    public AjaxJson checkBpCodeOrBpName(String bpCode, String bpName, String id, String pifId) {
        AjaxJson j = success();
        j.setSuccess(abPoolService.checkBpCodeOrBpNameOrPifId(bpCode, bpName, id, pifId));
        return j;
    }
    /**
     * 获取维护的启用状态的票据池
     *
     * @return
     */
    @GetMapping("/getAllPool")
    @ResponseBody
    public List<AbPoolVO> getAllPool(AbPoolVO abPoolVO) {
        return abPoolService.getAllPool(abPoolVO);
    }
    /**
     * 根据银行类型获取有效票据池（银行自动配置的银行有效且为票据池开票开启的银行）
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<AbPoolVO> getAbPoolByBankAuto() {
        List<AbPoolVO> abPoolList=abPoolService.getAbPoolByBankAuto();
        return abPoolList;
    }
}
