package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpQuotaService;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.vo.TpQuotaVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author chenf
 * @version 1.0
 * @Title: Controller
 * @Description: 限额管理对应Controller
 * @date 2022-05-05 15:43:05
 */
@Controller
@RequestMapping("/ttpay/quota")
public class TpQuotaController extends BaseController {
    @Autowired
    private TpQuotaService tpQuotaService;
    /**
     * 限额查询列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    public String list(Model model) {
        return "ttpay/listTpQuota";
    }
    /**
     * 限额查询列表页面
     *
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return tpQuotaService.getDataTables(dataTablesInput);
    }
    /**
     * @param model
     * @return 页面地址
     * @Title: 限额管理新增页面
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    @GetMapping("/add")
    public String add(Model model) {
        return "ttpay/addTpQuota";
    }
    /**
     * @param id
     * @param model
     * @return 页面地址
     * @Title: 限额管理修改页面
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    @GetMapping("/update")
    public String update(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            TpQuotaVO tpQuotaVO = tpQuotaService.getTpQuotaVO(id);
            tpQuotaVO = tpQuotaService.getTpQuotaAvail(tpQuotaVO, null,null);
            model.addAttribute("info", tpQuotaVO);
        }
        return "ttpay/updateTpQuota";
    }
    /**
     * @param tpQuotaVO 限额VO
     * @return 处理结果
     * @Title: 判断限额是否还有限额
     */
    @PostMapping("/getAvailQuota")
    @ResponseBody
    public TpQuotaVO getAvailQuota(TpQuotaVO tpQuotaVO) {
        return tpQuotaService.getTpQuotaAvail(tpQuotaVO, null,null);
    }
    /**
     * @param info 限额VO
     * @return 处理结果
     * @Title: 限额管理新增或修改操作
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    @LogMessage(content = "限额信息新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(TpQuotaVO info) {
        tpQuotaService.saveOrUpdate(info, this.getCurrentUser());
        return success();
    }
    /**
     * @param id 需要删除的ID
     * @return AjaxJson    返回类型
     * @throws
     * @Title: 通过ID删除限额
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    @LogMessage(content = "限额信息删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        tpQuotaService.delete(id);
        return success();
    }
    /**
     * 查询限额信息
     *
     * @param model
     * @param id
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_QUERY)
    @LogMessage(content = "查询限额信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/view/{id}")
    public String getTpQuotaViewById(@PathVariable("id") String id, Model model) {
        TpQuotaVO tpQuotaVO = tpQuotaService.getTpQuotaInfoById(id);
        tpQuotaVO = tpQuotaService.getTpQuotaAvail(tpQuotaVO, null,null);
        model.addAttribute("tpQuotaInfo", tpQuotaVO);
        return "ttpay/viewTpQuota";
    }
    /**
     * @param ids        需要修改的ID
     * @param validSign 有效标识
     * @return AjaxJson    返回类型
     * @throws
     * @Title: 修改有效标识
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTA_OP)
    @LogMessage(content = "修改有效标识", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/updateValidSign")
    @ResponseBody
    public AjaxJson updateValidSign(@RequestParam("ids") String ids, @RequestParam("validSign")String validSign) {
        tpQuotaService.updateValidSign(ids, validSign, this.getCurrentUser());
        return success();
    }
}
