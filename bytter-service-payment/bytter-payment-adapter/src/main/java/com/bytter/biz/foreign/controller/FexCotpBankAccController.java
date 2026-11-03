package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCotpBankAccService;
import com.bytter.biz.foreign.vo.FexCotpBankAccVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fusb
 * @version 1.0
 * @Title: FexCotpBankAccController
 * @Description: 交易对手银行账户管理Controller
 * @date 2020-02-17 15:20:33
 */
@Controller
@RequestMapping("/foreign/fexcotpbankacc")
public class FexCotpBankAccController extends BaseController {
    @Autowired
    private FexCotpBankAccService fexCotpBankAccService;
    @DubboReference(check = false)
    private IUserService userService;

    @LogMessage(content = "跳转交易对手银行账户管理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_QUERY)
    @GetMapping("/list")
    public String list(Model model){
        return "fexBankAcc/listForeignCotpBankAcc";
    }

    @LogMessage(content = "交易对手银行账户列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
       return fexCotpBankAccService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    @LogMessage(content = "跳转到新增交易对手银行账户信息页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_APPLY)
    @GetMapping("/add")
    public String addFexCotpBankAcc(Model model){
        return "fexBankAcc/addFexCotpBankAcc";
    }

    @LogMessage(content = "保存交易对手银行账户信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(FexCotpBankAccVO vo){
        fexCotpBankAccService.saveFexCotpBankAcc(vo,this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "跳转到修改交易对手银行账户信息页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_APPLY)
    @GetMapping("/edit/{id}")
    public String editFexCotpBankAcc(@PathVariable("id") String id,Model model){
        FexCotpBankAccVO fcBankAcc = fexCotpBankAccService.getFexFexCotpBankAcc(id);
        model.addAttribute("vo",fcBankAcc);
        return "fexBankAcc/updateFexCotpBankAcc";
    }

    @LogMessage(content = "删除交易对手银行账户信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_APPLY)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id,Model model){
        fexCotpBankAccService.updateFexCotpBankAccSatus(id,-2);
        return success();
    }

    @LogMessage(content = "使交易对手银行账户信息作废", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_APPLY)
    @PostMapping("/makeInvalid/{id}")
    @ResponseBody
    public AjaxJson makeInvalid(@PathVariable("id") String id,Model model){
        fexCotpBankAccService.updateFexCotpBankAccSatus(id,-3);
        return success();
    }

    @LogMessage(content = "使交易对手银行账户信息启用", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_COTP_BANKACC_APPLY)
    @PostMapping("/makeEnable/{id}")
    @ResponseBody
    public AjaxJson makeEnable(@PathVariable("id") String id,Model model){
        fexCotpBankAccService.updateFexCotpBankAccSatus(id,95);
        return success();
    }

    @LogMessage(content = "使交易对手银行账户信息失效", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String viewFexCotpBankAcc(@PathVariable("id") String id,Model model){
        FexCotpBankAccVO fcBankAcc = fexCotpBankAccService.getFexFexCotpBankAcc(id);
        model.addAttribute("vo",fcBankAcc);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_COTP_BANKACC);
        return "fexBankAcc/viewFexCotpBankAcc";
    }

    @GetMapping("/listApprove")
    @LogMessage(content = "主体单位默认银行账户信息审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime,Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "fexBankAcc/listFexCotpBankAccApprove";
    }

    /**
     * 审批列表
     */
    @PostMapping("/approveTables")
    @ResponseBody
    public DataTablesOutput approveTables(@RequestBody  DataTablesInput dataTablesInput){
        return fexCotpBankAccService.getApproveDataTables(dataTablesInput);
    }

    /**
     * 查看审批详情
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model){
        if (StringUtils.isNotEmpty(id)) {
            FexCotpBankAccVO vo = fexCotpBankAccService.getFexFexCotpBankAcc(id);
            if(vo!=null){
                model.addAttribute("vo", vo);
                model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_COTP_BANKACC);
            }
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "fexBankAcc/viewFexCotpBankAccApprove";
    }

    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model){
        if (StringUtils.isNotEmpty(businessId)) {
            FexCotpBankAccVO vo = fexCotpBankAccService.getFexFexCotpBankAcc(businessId);
            if (null != vo){
                if (StringUtils.isNotEmpty(vo.getUpdateBy())){
                    vo.setUpdateByName(userService.getByUserId(vo.getUpdateBy()).getUsername());
                }
                if (StringUtils.isNotEmpty(vo.getCreateBy())){
                    vo.setCreateByName(userService.getByUserId(vo.getCreateBy()).getUsername());
                }
            }
            model.addAttribute("vo",vo);
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "fexBankAcc/mobileFexCotpBankAcc";
    }

    /**
     * 获取交易对手银行账户下拉框数据
     * @return
     */
    @RequestMapping("/select")
    @ResponseBody
    public List<FexCotpBankAccVO> getSelectFexCotpBankAcc(String cotpId,String curCode,
                                                          @RequestParam(required = false,defaultValue = "") String bankId){
        return fexCotpBankAccService.getSelectFexCotpBankAcc(cotpId, curCode, bankId);
    }

    /**
     * 获取交易对手银行账户下拉框数据
     * @return
     */
    @RequestMapping("/selectJoinCur")
    @ResponseBody
    public List<FexCotpBankAccVO> getSelectFexCotpBankAccJoinCur(String cotpId,String curCode,
                                                          @RequestParam(required = false,defaultValue = "") String bankId){
        return fexCotpBankAccService.getSelectFexCotpBankAccJoinCur(cotpId, curCode, bankId);
    }
}
