package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCorpBankAccDtlService;
import com.bytter.biz.foreign.service.FexCorpBankAccService;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
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
@RequestMapping("/foreign/fexcorpbankacc")
public class FexCorpBankAccController extends BaseController {
    @Autowired
    private FexCorpBankAccService fexCorpBankAccService;
    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;

    @LogMessage(content = "主体单位默认银行账户列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_QUERY)
    @GetMapping("/list")
    public String list(Model model){
        return "fexBankAcc/listFexCorpBankAcc";
    }

    @LogMessage(content = "主体单位默认银行账户列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexCorpBankAccService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    @LogMessage(content = "跳转到新增主体单位默认银行账户信息页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    public String addFexCotpBankAcc(Model model){
        return "fexBankAcc/addFexCorpBankAcc";
    }

    @LogMessage(content = "保存主体单位默认银行账户信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson saveFexCorpBankAcc(FexCorpBankAccVO fexCorpBankAccVO){
        fexCorpBankAccService.saveFexCorpBankAcc(fexCorpBankAccVO,this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "编辑主体单位默认银行账户信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @GetMapping("/edit")
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    public String updateFexCorpBankAcc(String id,Model model){
        FexCorpBankAccVO vo = fexCorpBankAccService.getFexCorpBankAccById(id);
        model.addAttribute("vo",vo);
        return "fexBankAcc/updateFexCorpBankAcc";
    }

    @LogMessage(content = "删除主体单位默认银行账户信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id,Model model){
        fexCorpBankAccService.deleteFexCorpBankAcc(id);
        return success();
    }

    @LogMessage(content = "查看主体单位默认银行账户详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, Model model) {
        FexCorpBankAccVO vo = fexCorpBankAccService.getFexCorpBankAccById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_0);
        return "fexBankAcc/viewFexCorpBankAcc";
    }

    @LogMessage(content = "使主体单位默认银行账户信息作废", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    @PostMapping("/makeInvalid/{id}")
    @ResponseBody
    public AjaxJson makeInvalid(@PathVariable("id") String id,Model model){
        fexCorpBankAccService.updateFexCorpBankAccSatus(id,-3);
        return success();
    }

    @LogMessage(content = "使主体单位默认银行账户启用", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //RequiresPermissions(ResourceConstant.FOREIGN_FEX_CORP_BANKACC_APPLY)
    @PostMapping("/makeEnable/{id}")
    @ResponseBody
    public AjaxJson makeEnable(@PathVariable("id") String id,Model model){
        fexCorpBankAccService.updateFexCorpBankAccSatus(id,95);
        return success();
    }

    @GetMapping("/listApprove")
    @LogMessage(content = "主体单位默认银行账户信息审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime,Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "fexBankAcc/listFexCorpBankAccApprove";
    }

    /**
     * 审批列表
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/approveTables")
    @ResponseBody
    public DataTablesOutput approveTables(@RequestBody  DataTablesInput dataTablesInput){
        return fexCorpBankAccService.getApproveDataTables(dataTablesInput, getCurrentUser());
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
            FexCorpBankAccVO vo = fexCorpBankAccService.getFexCorpBankAccById(id);
            if(vo!=null){
                model.addAttribute("vo", vo);
                model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_0);
            }
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "fexBankAcc/viewFexCorpBankAccApprove";
    }

    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model){
        if (StringUtils.isNotEmpty(businessId)) {
            FexCorpBankAccVO fexCorpBankAccVO = fexCorpBankAccService.getFexCorpBankAccById(businessId);
            model.addAttribute("vo",fexCorpBankAccVO);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CORP_BANKACC_0);
        }
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "fexBankAcc/mobileFexCorpBankAcc";
    }

    /**
     * 获取主体单位默认银行账户下拉框数据
     * @return
     */
    @GetMapping("/select")
    @ResponseBody
    public List<FexCorpBankAccVO> getSelectFexCorpBankAcc(String corpId){
        return fexCorpBankAccService.getSelectFexCorpBankAcc(corpId);
    }

    @GetMapping("/selectDtl")
    @ResponseBody
    public List<FexCorpBankAccDtlVO> getSelectFexCorpBankAccDtl(String id){
        return fexCorpBankAccDtlService.getFecFexCorpBankAccDtlVOById(id);
    }

    /**
     * 获取账号信息
     *
     * @param busType
     * @param tranType
     * @param corpId
     * @param proId
     * @return
     */
    @RequestMapping(params = "getBankAcc")
    @ResponseBody
    public AjaxJson getBankAcc(String busType, String tranType, String corpId, String curId, String proId, String cashFlowType, String cdSign,String oppCorpId) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        FexCorpBankAccVO searchVO = new FexCorpBankAccVO();
        searchVO.setBusType(busType);
        searchVO.setTranType(tranType);
        searchVO.setCorpId(corpId);
        searchVO.setCurId(curId);
        searchVO.setCashFlowType(cashFlowType);
        searchVO.setProId(proId);
        searchVO.setCdSign(cdSign);
        searchVO.setOppCorpId(oppCorpId);
        List<FexCorpBankAccDtlVO> bankAccList = null;
        //资金调拨无需取规则里面的支付方式
        if(!ForeignConstant.TRAN_TYPE_3.equals(tranType)){
            bankAccList = fexCorpBankAccDtlService.getBankAccToTran(searchVO);
        }
        FexCorpBankAccDtlVO dtl = new FexCorpBankAccDtlVO();
        if (!EmptyUtils.isEmpty(bankAccList)) {
            BeanUtils.copyNotNullProperties(bankAccList.get(0), dtl);
        }else{
            //则默认取系统结算方式为34-外付网银的
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
            if (voucherType != null) {
                dtl.setVoucherTypeId(voucherType.getId());
                dtl.setVoucherName(voucherType.getVoucherName());
            }
        }
        ajaxJson.setObj(dtl);
        return ajaxJson;
    }

    /**
     * 获取账号信息 (交易对手发生变更)
     *
     * @param busType
     * @param tranType
     * @param corpId
     * @param proId
     * @return
     */
    @PostMapping("/getNewBankAcc")
    @ResponseBody
    public AjaxJson getNewBankAcc(String busType, String tranType, String corpId, String curId, String proId, String cashFlowType, String cdSign,String oppCorpId) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        FexCorpBankAccVO searchVO = new FexCorpBankAccVO();
        searchVO.setBusType(busType);
        searchVO.setTranType(tranType);
        searchVO.setCorpId(corpId);
        searchVO.setCurId(curId);
        searchVO.setCashFlowType(cashFlowType);
        searchVO.setProId(proId);
        searchVO.setCdSign(cdSign);
        searchVO.setOppCorpId(oppCorpId);
        List<FexCorpBankAccDtlVO> bankAccList = null;
        //资金调拨无需取规则里面的支付方式
        if(!ForeignConstant.TRAN_TYPE_3.equals(tranType)){
            bankAccList = fexCorpBankAccDtlService.getNewBankAccToTran(searchVO);
        }
        FexCorpBankAccDtlVO dtl = new FexCorpBankAccDtlVO();
        if (!EmptyUtils.isEmpty(bankAccList)) {
            BeanUtils.copyNotNullProperties(bankAccList.get(0), dtl);
        }else{
            //则默认取系统结算方式为34-外付网银的
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
            if (voucherType != null) {
                dtl.setVoucherTypeId(voucherType.getId());
                dtl.setVoucherName(voucherType.getVoucherName());
            }
        }
        if(StringUtils.isNotBlank(dtl.getBankAcc())) {
            fexCorpBankAccDtlService.setParamInfo(dtl);
        }
        ajaxJson.setObj(dtl);
        return ajaxJson;
    }


    /**
     *
     * 查询主体单位账号信息
     *
     * @param corpBankAcc
     * @return
     */
    @RequestMapping(params = "getCorpAccInfo")
    @ResponseBody
    public AjaxJson getCorpAccInfo(String corpBankAcc, String corpAccCurId){
        FexCorpBankAccDtlVO corpAccInfo = fexCorpBankAccDtlService.getCorpAccInfo(corpBankAcc, corpAccCurId);
        return success(corpAccInfo);
    }
}
