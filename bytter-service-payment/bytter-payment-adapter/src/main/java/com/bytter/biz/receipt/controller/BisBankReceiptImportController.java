package com.bytter.biz.receipt.controller;

/**
 * @author caop
 * @date 2024/8/2 15:19
 */

import com.bytter.biz.receipt.entity.BisBankReceiptImport;
import com.bytter.biz.receipt.service.BisBankReceiptImportService;
import com.bytter.biz.receipt.vo.BisBankReceiptImportVO;
import com.bytter.biz.receipt.vo.BisRunBlendingVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 离线银行电子回单管理
 */
@Controller
@RequestMapping("/receipt/bisbankreceiptimport")
public class BisBankReceiptImportController extends BaseController {

    @Autowired
    private BisBankReceiptImportService bisBankReceiptImportService;

    @DubboReference
    private IBtBankAccService bankAccService;

    @DubboReference
    private ICorpService corpService;

    @DubboReference
    private IAccountGrantService accountGrantService;

    /**
     * 离线银行电子回单管理列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:query")
    @LogMessage(content = "查询回单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        // 设置为当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 获取当月第一天的Date对象
        Date firstDayOfMonth = calendar.getTime();
        model.addAttribute("ged_tradeTime", DateUtils.formatDate(firstDayOfMonth,DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("led_tradeTime", DateUtils.formatDate(new Date(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "receipt/bankbill/listBisBankReceiptImport";
    }

    /**
     * 离线银行电子回单管理 table列表
     *
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:query")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisBankReceiptImportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }


    @LogMessage(content = "查询SYS_ATTACHMENT详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisBankReceiptImportVO bankReceiptImportVO = bisBankReceiptImportService.getById(id);
        model.addAttribute("bankReceiptImportVO", bankReceiptImportVO);
        return "receipt/bankbill/viewBisbankreceiptimport";
    }

    /**
     * 离线银行电子回单管理 新增跳转
     *
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @GetMapping("/add")
    public String add() {
        return "receipt/bankbill/addBisBankReceiptImport";
    }

    /**
     * 离线银行电子回单管理 上传保存
     *
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "上传回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisBankReceiptImportVO importVO) {
        bisBankReceiptImportService.importFile(importVO, this.getCurrentUser());
        return success();
    }

    /**
     * 离线银行电子回单管理   跳转流水认领
     *
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:query")
    @LogMessage(content = "查询情景列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listAccDtl/{billId}/{bankAcc}")
    public String listAccDtl(@PathVariable("billId") String billId, @PathVariable("bankAcc") String bankAcc, Model model) {
        model.addAttribute("billId", billId);
        if (StringUtils.isEmpty(bankAcc)){
            return "receipt/bankbill/listImportReceiptBlend";
        }
        model.addAttribute("bankAcc", bankAcc);
        BtBankAccDTO dto = bankAccService.getBankAccInfoByBankAcc(bankAcc);
        if (dto != null) {
            model.addAttribute("accName", dto.getAccName());
        }
        return "receipt/bankbill/listImportReceiptBlend";
    }

    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:query")
    @LogMessage(content = "查询情景列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listAccDtl/{billId}")
    public String listAccDtl(@PathVariable("billId") String billId, Model model) {
        model.addAttribute("billId", billId);
        return "receipt/bankbill/listImportReceiptBlend";
    }
    /**
     * 离线银行电子回单管理 流水勾对列表
     *
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:query")
    @PostMapping("/accDtldatatable")
    @ResponseBody
    public DataTablesOutput accDtldatatable(@RequestBody DataTablesInput dataTablesInput) {
        ICurrentUser user= getCurrentUser();
        //增加单位权限过滤
        List<String> corpAuth = corpService.getUserCorpIds("netbank:receipt:bisbankreceiptImport:query", user.getId());//        //增加账号权限过滤
//        List<String> bankAccGrantList = accountGrantService.listByMultiValAccs(user.getId(), "netbank:receipt:bisbankreceiptImport:query", null,
//                AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
//                AccountGrantConstant.QUERY_DETAIL);

        dataTablesInput.getFormdata().put("ins_corpId",String.join(",", corpAuth));
//        dataTablesInput.getFormdata().put("ins_bankAcc",String.join(",", bankAccGrantList));

        return bisBankReceiptImportService.getDtlDataTablesUnion(dataTablesInput);
    }

    @PostMapping("/runBlending")
    @ResponseBody
    public AjaxJson runBlending(@RequestBody BisRunBlendingVO bisRunBlendingVO) {
        logger.info("自动勾兑参数："+bisRunBlendingVO.toString());
        bisBankReceiptImportService.runBlending(bisRunBlendingVO);
        return success();
    }

    /**
     * 手工勾兑
     *
     * @param dtlId     流水id
     * @param receiptId 电子回单id
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "勾对流水", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping(params = "blendDtl")
    @ResponseBody
    public AjaxJson blendDtl(String receiptId, String dtlId) {


       List<BisBankReceiptImport> list= bisBankReceiptImportService.queryListInId(receiptId);

//        BisBankReceiptImport importDtl = bisBankReceiptImportService.get(receiptId, true);
//        bisBankReceiptImportService.blendDtl(importDtl, dtlId);

        bisBankReceiptImportService.blendDtlBatch(list,dtlId);

        AjaxJson aj = new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.paymentblending.success");
        return aj;
    }

    /**
     * 取消勾兑
     *
     * @param dtlId 流水id
     * @return
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "取消勾对", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping(params = "cancel")
    @ResponseBody
    public AjaxJson cancel(String dtlId) {
        bisBankReceiptImportService.cancelBlending(dtlId);
        AjaxJson aj = new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.cancel.paymentblending.success");
        return aj;
    }

    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "删除电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        bisBankReceiptImportService.deleteById(id, getCurrentUser());
        return success();
    }

    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "删除电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        this.bisBankReceiptImportService.batchDelete(ids, getCurrentUser());
        return success();
    }

    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "取消勾对电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/cancleBlend/{dtlId}")
    @ResponseBody
    public AjaxJson cancleBlend(@PathVariable("dtlId") String dtlId) {
        bisBankReceiptImportService.cancelBlending(dtlId);
        return success();
    }

    //@RequiresPermissions("netbank:receipt:bisbankreceiptImport:apply")
    @LogMessage(content = "批量取消勾对电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchCancleBlend")
    @ResponseBody
    public AjaxJson batchCancleBlend(@RequestParam("dtlIds") String dtlIds) {
        this.bisBankReceiptImportService.cancelBlending(dtlIds);
        return success();
    }

}
