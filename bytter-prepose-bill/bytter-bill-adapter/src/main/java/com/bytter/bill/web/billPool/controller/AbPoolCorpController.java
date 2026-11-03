package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolCorpService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: AbPoolCorpController
 * @Description: 票据池单位Controller
 * @author wangzy
 * @date 2019/12/30 15:18
 *
 */
@Controller
@RequestMapping("/billPool/corp")
public class AbPoolCorpController extends BaseController {
    @Autowired
    private AbPoolCorpService abPoolCorpService;
    /**
     * 票据池单位维护列表页面
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listAbPoolCorpVO(AbPoolCorpVO queryVO, Model model) {
        model.addAttribute("listInfo", abPoolCorpService.listAbPoolCorpVOTree(queryVO));
        model.addAttribute("queryVO", queryVO);
        return "billPool/listAbPoolCorp";
    }
    /**
     * 票据池单位维护列表页面
     * @param model
     * @return
     */
    @PostMapping("/listPoolCorp")
    public String listPoolCorp(AbPoolCorpVO queryVO, Model model) {
        model.addAttribute("listInfo", abPoolCorpService.listAbPoolCorpVOTree(queryVO));
        model.addAttribute("queryVO", queryVO);
        return "billPool/listAbPoolCorp";
    }
    /**
     * 获取树形结构单位信息
     * @return
     */
    @GetMapping("/treeData")
    @ResponseBody
//    @RequiresPermissions(AbPoolConstant.AB_POLL_CORP_OP)
    public Object treeCorpData(String resource) {
        return abPoolCorpService.getCorpTreeMapList(resource, this.getCurrentUser());
    }
    /**
     * 票据池单位新增页面
     * @param  model
     *
     */
    @GetMapping("/add")
    public String addBillPool(Model model) {
        model.addAttribute("corpIds",abPoolCorpService.listAllPoolCorpIds(null));
        return "billPool/addAbPoolCorp";
    }
    /**
     * 下级单位新增页面
     * @param  model
     *
     */
    @GetMapping("/addLower")
    public String addLowerCorp(String parentId, String bustypeLevel, Model model) {
        model.addAttribute("parentId", parentId);
        model.addAttribute("bustypeLevel", bustypeLevel);
        model.addAttribute("corpIds",abPoolCorpService.listAllPoolCorpIds(null));
        return "billPool/addAbPoolCorp";
    }
    /**
     * 池单位修改页面
     * @param  model
     *
     */
    @GetMapping("/update")
    public String updateCorp(String id,Model model) {
        model.addAttribute("abPoolCorpVO", abPoolCorpService.getAbPoolCorpInfo(id));
        model.addAttribute("corpIds",abPoolCorpService.listAllPoolCorpIds(id));
        model.addAttribute("id", id);
        return "billPool/addAbPoolCorp";
    }
    /**
     * 票据池单位维护新增或修改操作
     * @param  infoVO 信息
     * @return 处理结果
     */
    @LogMessage(content = "票据池单位维护新增或修改操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(AbPoolCorpVO infoVO){
        AjaxJson ajaxJson = abPoolCorpService.saveOrUpdate(infoVO, getCurrentUser());
        return ajaxJson;
    }
    /**
     * 池单位删除
     * @param  model
     *
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson deleteBillPoolCorp(String ids, Model model) {
        return abPoolCorpService.deleteAbPoolCorp(ids, getCurrentUser());
    }
    /**
     * 池单位禁用或启用
     * @param @param operate
     * @param @param id
     * @return AjaxJson    返回类型
     */
    @LogMessage(content = "启用/禁用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
    @PostMapping("/disableOrEnable/{operate}")
    @ResponseBody
    public AjaxJson disableOrEnable(@PathVariable("operate") String operate, String id){
        return  abPoolCorpService.disableOrEnable(operate, id, this.getCurrentUser());
    }
}
