package com.bytter.accountReceivable.docManage.controller;

import com.bytter.accountReceivable.constant.AccountReceivableConstant;
import com.bytter.accountReceivable.docManage.service.DocManageService;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.docManage.vo.InvoiceDocVo;

import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yangxd
 * @version 1.0
 * @Title:
 * @Description:
 * @date 2025-03-26
 */
@Controller
@RequestMapping("/accountReceivable/docManage")
public class DocManageController extends BaseController {
    @Autowired
    private DocManageService docManageService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    /**
     * 应收单管理列表页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_QUERY)
    @LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "docManage/listDocManage";
    }


    /**
     * 应收单管理新增页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_APPLY)
    @GetMapping("/add")
    public String add(Model model) {
        //新建单据号
        String docManageCode = docManageService.getDocManageCode();
        ConfigDTO config2 = configService.getConfig(BudgetConstant.BUSINESS_NAME_BASIC, BudgetConstant.BUSINESS_NAME_DEFAULT_BUDGETSEL);
        ConfigDTO budgetSel = configService.getConfig(BudgetConstant.BUSINESS_NAME_BASIC, BudgetConstant.BUSINESS_NAME_BUDGETSEL);
        model.addAttribute("budgetSel", budgetSel.getValue());
        model.addAttribute("defItemCode", config2.getValue());
        model.addAttribute("docManageCode", docManageCode);
        return "docManage/addDocManage";
    }

    /**
     * 应收单管理查看页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_EDIT)
    @GetMapping("/view")
    public String view(Model model, String id) {
        DocManageVo docManage = docManageService.getDocManageDetailById(id);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(BudgetConstant.BUSINESS_NAME_BASIC);
        model.addAttribute("config", config);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_DOCMANAGE);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BUDGETSEL);
        model.addAttribute("processSysname", BudgetConstant.BUSINESS_NAME_BASIC);
        model.addAttribute("docManage", docManage);
        return "docManage/viewDocManage";
    }

    /**
     * 应收单管理变更
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_EDIT)
    @GetMapping("/change")
    public String change(Model model, String id, @RequestParam(required = false) String reasonTitle, @RequestParam(required = false) String reasonContent) {
        DocManageVo docManageVo = docManageService.queryDocManageById(id);
        int rowIndex = 0;
        if (!EmptyUtils.isEmpty(docManageVo.getInvoiceVos())) {
            rowIndex = docManageVo.getInvoiceVos().size();
        }
        model.addAttribute("docManageVo", docManageVo);
        model.addAttribute("rowIndex", rowIndex);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(BudgetConstant.BUSINESS_NAME_BASIC);
        model.addAttribute("config", config);
        model.addAttribute("reasonTitle", reasonTitle);
        model.addAttribute("reasonContent", reasonContent);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BUDGETSEL);
        model.addAttribute("processSysname", BudgetConstant.BUSINESS_NAME_BASIC);
        return "docManage/changeDocManage";
    }


    /**
     * 应收单管理编辑页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_EDIT)
    @GetMapping("/update")
    public String update(Model model, String id) {
        DocManageVo docManageVo = docManageService.queryDocManageById(id);
        int rowIndex = 0;
        if (!EmptyUtils.isEmpty(docManageVo.getInvoiceVos())) {
            rowIndex = docManageVo.getInvoiceVos().size();
        }
        model.addAttribute("docManageVo", docManageVo);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(BudgetConstant.BUSINESS_NAME_BASIC);
        model.addAttribute("config", config);
        model.addAttribute("rowIndex", rowIndex);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BUDGETSEL);
        model.addAttribute("processSysname", BudgetConstant.BUSINESS_NAME_BASIC);
        return "docManage/editDocManage";
    }

    /**
     * 应收单管理变更页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_EDIT)
    @GetMapping("/addChange")
    public String addChange(Model model, String id) {
        model.addAttribute("id", id);
        model.addAttribute("status", AccountReceivableConstant.STATUS__WAIT_INVALIDATED);
        return "docManage/changeOrInvalidated";
    }

    /**
     * 应收单管理作废页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_EDIT)
    @GetMapping("/invalidated")
    public String invalidated(Model model, String id, @RequestParam(required = false) String type) {
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        if (EmptyUtils.isNotEmpty(type) && "2".equals(type)) {
            model.addAttribute("status", AccountReceivableConstant.STATUS__WAIT_INVALIDATED);
        } else {
            model.addAttribute("status", AccountReceivableConstant.STATUS_WAIT_CHANGE_REFUSE);
        }
        return "docManage/changeOrInvalidated";
    }

    /**
     * 应收单管理审批列表页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.SYS_FLOW_APPROVE_DOC_MANAGE)
    @LogMessage(content = "查询审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(Model model, String flowStatus) {
        model.addAttribute("flowStatus", flowStatus);
        return "docManage/listDocManageApprove";
    }

    /**
     * 应收单管理查看页面跳转
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_EDIT)
    @GetMapping("/viewDocApprove")
    public String viewDocApprove(Model model, String id, String taskId) {
        DocManageVo docManage = docManageService.getDocManageDetailById(id);
        model.addAttribute("docManage", docManage);
        model.addAttribute("taskId", taskId);
        model.addAttribute("docManage", docManage);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(BudgetConstant.BUSINESS_NAME_BASIC);
        model.addAttribute("config", config);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_DOCMANAGE);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BUDGETSEL);
        model.addAttribute("processSysname", BudgetConstant.BUSINESS_NAME_BASIC);
        model.addAttribute("docManage", docManage);
        return "docManage/ViewDocManageApprove";
    }

//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_APPLY)
    @LogMessage(content = "新增应收单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DocManageVo manageVo) {
        docManageService.saveDocManage(manageVo, getCurrentUser());
        return success();
    }


//	/**
//	 * 批量操作，已删除.未写完未验证
//	 * */
//	@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_APPLY)
//	@LogMessage(content = "批量操作应收单据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
//	@PostMapping("/batchSubmit")
//	@ResponseBody
//	public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
//		docManageService.batchSub(ids, AccountReceivableConstant.STATUS_WAIT_CHECK,getCurrentUser());
//		return success();
//	}


    /**
     * 查询列表
     *
     * @return
     */
    @LogMessage(content = "查询应收单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
        return docManageService.listDocManage(input, getCurrentUser());
    }


    @LogMessage(content = "查询应收单发票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editInvoiceDoc")
    @ResponseBody
    public List<InvoiceDocVo> queryeditInvoiceDocById(String id) {
        return docManageService.queryeditInvoiceDocById(id);
    }

    @LogMessage(content = "查询应收单据审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
//    @RequiresPermissions(ResourceConstant.SYS_FLOW_APPROVE_DOC_MANAGE)
    @PostMapping("/datatableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput input) {
        return docManageService.listDocManageApprove(input, getCurrentUser());
    }

    @LogMessage(content = "保存导入应收单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<DocManageVo> list) {
        docManageService.saveImport(list, getCurrentUser());
        return success();
    }


//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_APPLY)
    @LogMessage(content = "应收单编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxJson edit(DocManageVo manageVo) {
        docManageService.updateDocManage(manageVo, getCurrentUser());
        return success();
    }

    @LogMessage(content = "删除应收单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("ids") String ids) {
        List<String> idArr = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        docManageService.deleteDocManage(idArr);
        return success();
    }

//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_APPLY)
    @LogMessage(content = "应收单作废", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/changeOrInvalidated")
    @ResponseBody
    public AjaxJson changeOrInvalidated(String status, String reasonTitle, String reasonContent, String id) {
        docManageService.changeOrInvalidated(reasonTitle, reasonContent, id, status);
        return success();
    }


    /**
     * 获取收款账号
     *
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayerAcc")
    @ResponseBody
    public List<BtBankAccDTO> getPayerAcc(String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if(StringUtils.isNotBlank(corpId)){
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String accountCorpId = StringUtils.isBlank(corp.getUserAccountCode()) ? corp.getId() : corp.getUserAccountCode();
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
            List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), accountCorpId, null, null, "1", AccountGrantConstant.QUERY_BALANCE);
            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                for (FcAccDTO fcAcc : listFcAcc) {
                    //过滤企业活期账号
                    if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
                        BtBankAccDTO bankAcc = new BtBankAccDTO();
                        bankAcc.setId(fcAcc.getId());
                        bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                        bankAcc.setAccName(fcAcc.getAccName());
                        bankAcc.setCurId(fcAcc.getCurId());
                        bankAcc.setCurName(fcAcc.getCurName());
                        bankAcc.setBankName(fcAcc.getNetName());
                        bankAcc.setBalance(fcAcc.getFcBalance());
                        listAcc.add(bankAcc);
                    }
                }
            }
            //获取余额权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), accountCorpId, null, null, "2,3", AccountGrantConstant.QUERY_BALANCE);
            if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
            /*
            这段造成越界
            for (BtBankAccDTO dto : listBankAccTemp) {
                //账号属性对外付款不为付款的账号才能收款
                if ("0".equals(dto.getForeignDes()) || "2".equals(dto.getForeignDes())) {
                    listBankAccTemp.remove(dto);
                }
            }
            */
                for (int i=0;i<listBankAccTemp.size();i++){
                    BtBankAccDTO dto = listBankAccTemp.get(i);
                    if ("0".equals(dto.getForeignDes()) || "2".equals(dto.getForeignDes())) {
                        listBankAccTemp.remove(dto);
                    }
                }
            }
            listAcc.addAll(listBankAccTemp);
        }
        return listAcc;
    }
}
