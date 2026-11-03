package com.bytter.biz.bankacc.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AllocationConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccMaintainService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC 银行账号父子关系维护
 * @date 2017-06-02 17:50:16
 */
@Controller
@RequestMapping("/bankacc/btbankaccmaintain")
public class BtBankAccMaintainController extends BaseController {
    @DubboReference(check=false)
    private IBtBankAccMaintainService btBankAccMaintainService;//变接口
    @DubboReference(check=false)
    private IFcAccService fcAccService;
    @Autowired(required=false)
    private IFcCorpPoolService fcCorpPoolService;
    /**
     * BT_BANK_ACC列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_QUERY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_QUERY},
//            logical = Logical.OR)
    @LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{poolSign}")
    public String list(@PathVariable("poolSign") String poolSign,Model model) {
        model.addAttribute("poolSign", poolSign);
        return "bankacc/listBtBankAccMaintain";
    }
    /**
     * BT_BANK_ACC table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_QUERY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
            return btBankAccMaintainService.getDataTablesUnionForPool(dataTablesInput, this.getCurrentUser());
        }else {
            return btBankAccMaintainService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        }
    }
    /**
     * BT_BANK_ACC详情页
     *
     * @return
     */
    @LogMessage(content = "查询内部账号详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/showCounterAccList")
    public String showCounterAccList(BtBankAccVO vo, Model model) {
        model.addAttribute("btBankAccPage", vo);
        return "bankacc/showCounterAccList";
    }
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_APPLY},
//            logical = Logical.OR)
    @PostMapping("/dataTableForCounterAcc")
    @ResponseBody
    public DataTablesOutput dataTableForCounterAcc(@RequestBody DataTablesInput dataTablesInput, String corpId,String id) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
            return btBankAccMaintainService.dataTableForCounterPoolAcc(dataTablesInput, this.getCurrentUser(), corpId);
        }else {
            return btBankAccMaintainService.dataTableForCounterAcc(dataTablesInput, this.getCurrentUser(), corpId,id);
        }
    }
    /**
     * 增加子账号
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_APPLY},
//            logical = Logical.OR)
    @LogMessage(content = "增加内部账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/toAdd")
    @ResponseBody
    public AjaxJson toAdd(@RequestParam("id") String id, @RequestParam("blnctrAcc") String blnctrAcc, @RequestParam("flag") String flag, @RequestParam("curId") String curId) {
        btBankAccMaintainService.toAdd(id, blnctrAcc, flag, curId);
        return success();
    }
    /**
     * 增加子账号
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_APPLY},
//            logical = Logical.OR)
    @LogMessage(content = "增加内部账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/toDel")
    @ResponseBody
    public AjaxJson toDel(@RequestParam("id") String id, @RequestParam("flag") String flag, @RequestParam("curId") String curId) {
        btBankAccMaintainService.toDel(id, flag, curId);
        return success();
    }
    /**
     * 检查内部账户是否存在对应的联行网点及账号
     *
     * @param blnctrAcc
     * @return
     */
    @RequestMapping(params = "checkUnionnetOrUnionacc")
    @ResponseBody
    public AjaxJson checkUnionnetOrUnionacc(String blnctrAcc) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(blnctrAcc);
        if (EmptyUtils.isBlank(fcAccDTO.getUnionNet()) || EmptyUtils.isBlank(fcAccDTO.getUnionAcc())) {
            j.setObj(true);
        }
        return j;
    }
    /**
     * 获取资金池
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_QUERY;
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }
    /**
     * 获取资金池
     * @param poolId
     * @return
     */
    @RequestMapping(params = "getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_QUERY;
        return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }
}
