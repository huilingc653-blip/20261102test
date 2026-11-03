package com.bytter.biz.receipt.controller;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.receipt.service.BisBankReceiptDtlHisService;
import com.bytter.biz.receipt.service.BisBankReceiptDtlService;
import com.bytter.biz.receipt.vo.EBankMoreBillVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Controller
@RequestMapping("/receipt/bisbankreceiptdtlhis")
public class BisBankReceiptDtlHisController extends BaseController {
    @Autowired
    private BisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @Autowired
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
    /**
     * BIS_ACC_BAL列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询BIS_ACC_BAL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        Date endDate = DateUtils.getDate();
        Date startDate = DateUtils.addWeek(DateUtils.getDate(), -1);
        model.addAttribute("endDate", endDate);
        model.addAttribute("startDate", startDate);
        return "receipt/bankbill/listEBankBillHis";
    }
    /**
     * BIS_ACC_BAL table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisBankReceiptDtlHisService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 预览
     *
     * @return
     */
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("id", id);
        return "receipt/bankbill/viewEBankBillHis";
    }
    /**
     * 预览页面加载图片
     *
     * @return
     */
    @GetMapping("/initImg")
    public void initImg(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            BisBankReceiptDtlHisVO bisBankReceiptDtlHis = this.bisBankReceiptDtlHisService.getByIdUnion(id);
            if ("pdf".equalsIgnoreCase(bisBankReceiptDtlHis.getFiletype().trim())) {
                this.bisBankReceiptDtlHisService.streamOperPdf(request, response, bisBankReceiptDtlHis);
            } else {
                this.bisBankReceiptDtlHisService.streamOper(request, response, bisBankReceiptDtlHis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 列表打印
     *
     * @return
     */
    @GetMapping("/print")
    public String print(String id, Model model) {
        model.addAttribute("id", id);
        this.bisBankReceiptDtlHisService.updatePrint(id);
        return "receipt/bankbill/printEBankBillHis";
    }
    /**
     * 批量预览
     *
     * @return
     */
    @GetMapping("/batchPreview")
    public String batchPreview(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/viewEBankBillHisBatch";
    }
    /**
     * 批量打印
     *
     * @return
     */
    @GetMapping("/batchPrint")
    public String batchPrint(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.bisBankReceiptDtlHisService.updatePrint(id);
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/printEBankBillHisBatch";
    }
    /**
     * 下载
     */
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        bisBankReceiptDtlHisService.download(id, request, response);
    }
    /**
     * 批量下载
     */
    @RequestMapping("/batchDownload")
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        bisBankReceiptDtlHisService.batchDownload(ids, request, response);
    }
    /**
     * 关联付款单跳列表页面
     *
     * @return
     */
    @GetMapping("/morebill")
    public String morebill(String id, Model model) {
        List<String> ids = StringUtils.splitToList("/", id);
        List list1 = new ArrayList();
        for (String idi : ids) {
        	BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(idi);
            EBankMoreBillVO eBankMoreBillVO = bisBankReceiptDtlService.getMoreBill(idi);
            eBankMoreBillVO.setTransTime(bisAccDtlDTO.getTransTime());
            eBankMoreBillVO.setCorpName(bisAccDtlDTO.getCorpName());
            eBankMoreBillVO.setBankAcc(bisAccDtlDTO.getBankAcc());
            eBankMoreBillVO.setAccName(bisAccDtlDTO.getAccName());
            eBankMoreBillVO.setBankName(bisAccDtlDTO.getBankName());
            eBankMoreBillVO.setCdSignName(bisAccDtlDTO.getCdSignName());
            eBankMoreBillVO.setCurName(bisAccDtlDTO.getCurName());
            eBankMoreBillVO.setAmt(bisAccDtlDTO.getAmt());
            eBankMoreBillVO.setBal(bisAccDtlDTO.getBal());
            eBankMoreBillVO.setOppAccNo(bisAccDtlDTO.getOppAccNo());
            eBankMoreBillVO.setOppAccName(bisAccDtlDTO.getOppAccName());
            eBankMoreBillVO.setOppAccBank(bisAccDtlDTO.getOppAccBank());
            eBankMoreBillVO.setRemark(bisAccDtlDTO.getRemark());
            eBankMoreBillVO.setPostscript(bisAccDtlDTO.getPostscript());
            eBankMoreBillVO.setUses(bisAccDtlDTO.getUses());
            eBankMoreBillVO.setDtlId(bisAccDtlDTO.getId());
            if(StringUtils.isNotEmpty(eBankMoreBillVO.getBillId())) {
                list1.add(eBankMoreBillVO);
            }
        }
        model.addAttribute("list", list1);
        return "receipt/bankbill/listEBankBillDtl";
    }
}
