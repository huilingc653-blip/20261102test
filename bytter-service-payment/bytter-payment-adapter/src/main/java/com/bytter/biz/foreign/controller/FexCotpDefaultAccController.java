package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCotpDefaultAccService;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.dto.BtAssignPayWayDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fusb
 * @version 1.0
 * @Title: Controller
 * @Description: 主体单位默认银行账户维护Controller
 * @date 2020-02-17 10:20:33
 */
@Controller
@RequestMapping("/foreign/fexcotpdefaultacc")
public class FexCotpDefaultAccController extends BaseController {

    @Autowired
    private FexCotpDefaultAccService fexCotpDefaultAccService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;

    @LogMessage(content = "交易对手默认银行账户列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_DEFAULTACC_QUERY)
    @GetMapping("/list")
    public String list(Model model){
        return "fexBankAcc/listFexCotpDefaultAcc";
    }

    @LogMessage(content = "交易对手默认银行账户列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_DEFAULTACC_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexCotpDefaultAccService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    @LogMessage(content = "跳转到新增交易对手默认银行账户信息页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_DEFAULTACC_APPLY)
    public String addFexCotpBankAcc(Model model){
        return "fexBankAcc/addFexCotpDefaultAcc";
    }

    @LogMessage(content = "保存交易对手默认银行账户信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson saveFexCorpBankAcc(FexCorpBankAccVO fexCorpBankAccVO){
        fexCotpDefaultAccService.saveFexCorpBankAcc(fexCorpBankAccVO,this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "编辑交易对手默认银行账户信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @GetMapping("/edit")
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_DEFAULTACC_APPLY)
    public String updateFexCorpBankAcc(String id,Model model){
        FexCorpBankAccVO vo = fexCotpDefaultAccService.getFexCorpBankAccById(id);
        model.addAttribute("vo",vo);
        addQuickPayProductModel(vo, model);
        return "fexBankAcc/updateFexCotpDefaultAcc";
    }

    private void addQuickPayProductModel(FexCorpBankAccVO vo, Model model) {
        String proId = vo == null ? null : vo.getProId();
        boolean isPayPalProduct = isCurrentTenantProductId(proId, ForeignConstant.FEX_PRODUCT_PAYPAL);
        boolean isQuickPayProduct = isPayPalProduct || isCurrentTenantProductId(proId, ForeignConstant.FEX_PRODUCT_FPS);
        model.addAttribute("isQuickPayProduct", isQuickPayProduct);
        model.addAttribute("isPayPalProduct", isPayPalProduct);
    }

    private boolean isCurrentTenantProductId(String proId, String productId) {
        if (StringUtils.isBlank(proId) || StringUtils.isBlank(productId)) {
            return false;
        }
        if (productId.equals(proId)) {
            return true;
        }
        ICurrentUser currentUser = getCurrentUser();
        if (currentUser == null || StringUtils.isBlank(currentUser.getTenantId())) {
            return false;
        }
        return (productId + "_" + currentUser.getTenantId()).equals(proId);
    }

    @LogMessage(content = "删除交易对手默认银行账户信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_DEFAULTACC_APPLY)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id,Model model){
        fexCotpDefaultAccService.deleteFexCorpBankAcc(id);
        return success();
    }

    @LogMessage(content = "查看交易对手默认银行账户详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        FexCorpBankAccVO vo = fexCotpDefaultAccService.getFexCorpBankAccById(id);
        model.addAttribute("vo",vo);
        addQuickPayProductModel(vo, model);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_1);
        return "fexBankAcc/viewFexCotpDefaultAcc";
    }

    @LogMessage(content = "使交易对手默认银行账户信息作废", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    @PostMapping("/makeInvalid/{id}")
    @ResponseBody
    public AjaxJson makeInvalid(@PathVariable("id") String id,Model model){
        fexCotpDefaultAccService.updateFexCotpDefaultAccSatus(id,-3);
        return success();
    }

    @LogMessage(content = "使交易对手默认银行账户启用", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    @PostMapping("/makeEnable/{id}")
    @ResponseBody
    public AjaxJson makeEnable(@PathVariable("id") String id,Model model){
        fexCotpDefaultAccService.updateFexCotpDefaultAccSatus(id,95);
        return success();
    }

    @GetMapping("/listApprove")
    @LogMessage(content = "主体单位默认银行账户信息审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime,Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "fexBankAcc/listFexCotpDefaultAccApprove";
    }

    /**
     * 审批列表
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/approveTables")
    @ResponseBody
    public DataTablesOutput approveTables(@RequestBody  DataTablesInput dataTablesInput){
        return fexCotpDefaultAccService.getApproveDataTables(dataTablesInput, getCurrentUser());
    }

    /**
     * 查看审批详情
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model){
        if (StringUtils.isNotEmpty(id)) {
            FexCorpBankAccVO vo = fexCotpDefaultAccService.getFexCorpBankAccById(id);
            if(vo!=null){
                model.addAttribute("vo", vo);
                model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_1);
            }
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "fexBankAcc/viewFexCotpDefaultAccApprove";
    }

    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model){
        if (StringUtils.isNotEmpty(businessId)) {
            FexCorpBankAccVO vo = fexCotpDefaultAccService.getFexCorpBankAccById(businessId);
            model.addAttribute("vo",vo);
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "fexBankAcc/mobileFexCotpDefaultAcc";
    }

    /**
     * 根据单位ID获取分配的付款方式信息
     * @param corpId
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BtAssignPayWayDTO> json(String corpId) {
        List<BtAssignPayWayDTO> btAssignPayWayDTOS = fcVoucherTypeService.getBtAssignPayWays(corpId);
        return btAssignPayWayDTOS;
    }
}
