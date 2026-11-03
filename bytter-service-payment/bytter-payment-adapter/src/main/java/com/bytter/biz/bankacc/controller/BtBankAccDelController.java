package com.bytter.biz.bankacc.controller;
import com.bytter.biz.inneraccount.service.BtBankAccDelService;
import com.bytter.biz.inneraccount.vo.BtBankAccAppVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccAppService;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号注销
 * @date 2017-05-23 17:37:39
 */
@Controller
@RequestMapping("/bankacc/btbankaccdel")
public class BtBankAccDelController extends BaseController {
    @Autowired
    private BtBankAccDelService btBankAccDelService;
    @DubboReference(check = false)
    private IBtBankAccAppService btBankAccAppService;
    @DubboReference(check = false)
    private IBtBankAccCommonService btBankAccCommonService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    /**
     * 银行账号注销表列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_QUERY)
    @LogMessage(content = "查询银行账号申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bankacc/listBtBankAccDel";
    }
    /**
     * 银行账号注销表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccDelService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 银行账号注销表编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_APPLY)
    @GetMapping("/update")
    public String update(BtBankAccAppVO vo, Model model) {
        if (StringUtils.isNotEmpty(vo.getId())) {
            if (BtBankAccConstant.FORM_APP.equals(vo.getIsForm())) {
                BtBankAccAppDTO btBankAccAppVO = btBankAccAppService.getBankAccApp(vo.getId());
                String curId = btBankAccCommonService.setCurId(btBankAccAppVO.getId(), "app");
                model.addAttribute("curId", curId);
                model.addAttribute("btBankAccAppPage", btBankAccAppVO);
                //银行类别对应的境内外标识
                BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccAppVO.getBankTypeId());
                model.addAttribute("isDomesticBankType", bankTypeDTO == null ? "" : bankTypeDTO.getIsDomesticBank());
            } else {
                BtBankAccDTO btBankAccVO = btBankAccCommonService.getBtBankAccVOById(vo.getId());
                String curId = btBankAccCommonService.setCurId(btBankAccVO.getId(), "acc");
                model.addAttribute("curId", curId);
                model.addAttribute("btBankAccAppPage", btBankAccVO);
                //银行类别对应的境内外标识
                BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccVO.getBankTypeId());
                model.addAttribute("isDomesticBankType", bankTypeDTO == null ? "" : bankTypeDTO.getIsDomesticBank());
            }
        }
        return "bankacc/updateBtBankAccDel";
    }
    /**
     * 银行账号注销表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_QUERY)
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtBankAccAppDTO vo = btBankAccAppService.getBankAccApp(id);
        model.addAttribute("btBankAccAppPage", vo);
        model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_DEL);
        return "bankacc/viewBtBankAccRev";
    }
    /**
     * 修改银行账号注销
     *
     * @param btBankAccApp
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_APPLY)
    @LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtBankAccAppDTO btBankAccApp) {
        btBankAccDelService.toModify(btBankAccApp, this.getCurrentUser());
        return success();
    }
    /**
     * 删除银行账号注销
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_APPLY)
    @LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.btBankAccDelService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * 批量删除银行账号注销
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_APPLY)
    @LogMessage(content = "批量删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.btBankAccDelService.delete(id, this.getCurrentUser());
        }
        return success();
    }
}
