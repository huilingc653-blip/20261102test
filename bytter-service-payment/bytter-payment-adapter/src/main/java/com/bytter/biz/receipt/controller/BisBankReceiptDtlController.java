package com.bytter.biz.receipt.controller;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.biz.receipt.service.BisBankReceiptDtlService;
import com.bytter.biz.receipt.vo.BisBankReceiptDtlArrayVO;
import com.bytter.biz.receipt.vo.EBankMoreBillVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Controller
@RequestMapping("/receipt/bisbankreceiptdtl")
public class BisBankReceiptDtlController extends BaseController {
    @Autowired
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

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
        return "receipt/bankbill/listEBankBill";
    }
    /**
     * BIS_ACC_BAL table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisBankReceiptDtlService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 预览
     *
     * @return
     */
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("id", id);
        return "receipt/bankbill/viewEBankBill";
    }
    /**
     * 预览页面加载图片
     *
     * @return
     */
    @GetMapping("/initImg")
    public void initImg(String id, HttpServletRequest request, HttpServletResponse response) {
     try {
         this.bisBankReceiptDtlService.initImgV2(id,request,response);
     } catch (Exception e) {
         logger.error("文件预览错误，", e);
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
        this.bisBankReceiptDtlService.updatePrint(id);
        return "receipt/bankbill/printEBankBill";
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
        return "receipt/bankbill/viewEBankBillBatch";
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
            this.bisBankReceiptDtlService.updatePrint(id);
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/printEBankBillBatch";
    }
    /**
     * 批量修改
     *
     * @return
     */
    @GetMapping("/batchUpdate")
    public String batchUpdate(String ids, Model model) {
        List<BisBankReceiptDtlVO> listDtl = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisBankReceiptDtlVO entity = this.bisBankReceiptDtlService.getByIdUnion(id);
            if (null != entity){
                listDtl.add(entity);
            }
        }
        List<String> accNos = Lists.newArrayList();
        accNos.addAll(listDtl.stream().filter(item -> null != item && CommonConstant.YES.equals(item.getCdSign()) && StringUtils.isNotEmpty(item.getPayAccNo())).map(BisBankReceiptDtlVO::getPayAccNo).collect(Collectors.toList()));
        accNos.addAll(listDtl.stream().filter(item -> null != item && CommonConstant.NO.equals(item.getCdSign()) && StringUtils.isNotEmpty(item.getPayeeAccNo())).map(BisBankReceiptDtlVO::getPayeeAccNo).collect(Collectors.toList()));
        Map<String, BtBankAccDTO> bankAccMap= bankAccService.getBankAccInfoByBankAccList(accNos);
        Map<String, String> corpMap = corpService.getNameByIds(bankAccMap.values().stream().map(BtBankAccDTO::getCorpId).collect(Collectors.toList()));
        for (BisBankReceiptDtlVO bisBankReceiptDtlVO : listDtl) {
            if (CommonConstant.YES.equals(bisBankReceiptDtlVO.getCdSign())) {
                bisBankReceiptDtlVO.setCorpId(bankAccMap.get(bisBankReceiptDtlVO.getPayAccNo()).getCorpId());
            }else {
                bisBankReceiptDtlVO.setCorpId(bankAccMap.get(bisBankReceiptDtlVO.getPayeeAccNo()).getCorpId());
            }
            bisBankReceiptDtlVO.setCorpName(corpMap.get(bisBankReceiptDtlVO.getCorpId()));
        }
        model.addAttribute("listDtl", listDtl);
        return "receipt/bankbill/updateEBankBill";
    }
    /**
     * 查看关联流水
     *
     * @return
     */
    @GetMapping("/dtl")
    public String dtl(String id, Model model) {
        List<String> ids = StringUtils.splitToList("/", id);
        List list = new ArrayList();
        for (String idi : ids) {
            list.add(bisAccDtlService.getById(idi));
        }
        model.addAttribute("list", list);
        return "receipt/bankbill/listBisAccDtl";
    }
    /**
     * 查看关联付款单
     *
     * @return
     */
    @GetMapping("/bill")
    public String bill(String id, Model model) {
        List<String> ids = StringUtils.splitToList("/", id);
        List list = new ArrayList();
        for (String idi : ids) {
            VNisPayBillDTO vNisPayBillDTO = bisBankReceiptDtlService.getVNisPayBill(idi);
            if(StringUtils.isNotEmpty(vNisPayBillDTO.getCurId())) {
                BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(vNisPayBillDTO.getCurId());
                if(StringUtils.isNotEmpty(btCurrencyDTO)) {
                    vNisPayBillDTO.setCurName(btCurrencyDTO.getCurName());
                }
            }
            list.add(vNisPayBillDTO);
        }
        model.addAttribute("list", list);
        return "receipt/bankbill/listENisBillhead";
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
        	BisAccDtlDTO bisAccDtlDTO =bisAccDtlService.getById(idi);
            EBankMoreBillVO eBankMoreBillVO =bisBankReceiptDtlService.getMoreBill(idi);
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
            list1.add(eBankMoreBillVO);
        }
        model.addAttribute("list", list1);
        return "receipt/bankbill/listEBankBillDtl";
    }
    /**
     * 编辑
     *
     * @return
     */
    @GetMapping("/updatepage")
    public String updatepage(String id, String corpName,Model model) {
        List<BisBankReceiptDtlVO> listDtl = new ArrayList<>();
        BisBankReceiptDtlVO bisBankReceiptDtlVO = bisBankReceiptDtlService.getByIdUnion(id);
        bisBankReceiptDtlVO.setCorpName(corpName);
        listDtl.add(bisBankReceiptDtlVO);
        model.addAttribute("listDtl", listDtl);
        return "receipt/bankbill/updateEBankBill";
    }
    //修改编辑
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(BisBankReceiptDtlArrayVO bisBankReceiptDtlArrayVO) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        simpleDateFormat.parse("2019-09-02");
        String[] ids = bisBankReceiptDtlArrayVO.getId().split(",");
        String[] payAccNo = bisBankReceiptDtlArrayVO.getPayAccNo().split(",", ids.length);
        String[] payAccName = bisBankReceiptDtlArrayVO.getPayAccName().split(",", ids.length);
        String[] payBankName = bisBankReceiptDtlArrayVO.getPayBankName().split(",", ids.length);
        String[] bifCode = bisBankReceiptDtlArrayVO.getBifCode().split(",", ids.length);
        String[] payeeAccNO = bisBankReceiptDtlArrayVO.getPayeeAccNo().split(",", ids.length);
        String[] payeeAccName = bisBankReceiptDtlArrayVO.getPayeeAccName().split(",", ids.length);
        String[] payeeBankName = bisBankReceiptDtlArrayVO.getPayeeBankName().split(",", ids.length);
        String[] transAmt = bisBankReceiptDtlArrayVO.getTransAmt().split(",", ids.length);
        String[] curId = bisBankReceiptDtlArrayVO.getCurId().split(",", ids.length * 2);
        String[] usefor = bisBankReceiptDtlArrayVO.getUsefor().split(",", ids.length);
        String[] abs = bisBankReceiptDtlArrayVO.getAbs().split(",", ids.length);
        String[] remark = bisBankReceiptDtlArrayVO.getRemark().split(",", ids.length);
        String[] busType = bisBankReceiptDtlArrayVO.getBusType().split(",", ids.length);
        String[] busNo = bisBankReceiptDtlArrayVO.getBusNo().split(",", ids.length);
        String[] charageDate = bisBankReceiptDtlArrayVO.getCharageDate().split(",", ids.length);
        String[] receiptNo = bisBankReceiptDtlArrayVO.getReceiptNo().split(",", ids.length);
        String[] receiptCheckCode = bisBankReceiptDtlArrayVO.getReceiptCheckCode().split(",", ids.length);
        String[] transSerialNo = bisBankReceiptDtlArrayVO.getTransSerialNo().split(",", ids.length);
        String[] netCode = bisBankReceiptDtlArrayVO.getNetCode().split(",", ids.length);
        String[] tellerNo = bisBankReceiptDtlArrayVO.getTellerNo().split(",", ids.length);
        String[] ebillimg = bisBankReceiptDtlArrayVO.getEbillimg().split(",", ids.length);
        String[] filetype = bisBankReceiptDtlArrayVO.getFiletype().split(",", ids.length);
        String[] pdfToImg = bisBankReceiptDtlArrayVO.getPdfToImg().split(",", ids.length);
        String[] prints = bisBankReceiptDtlArrayVO.getPrints().split(",", ids.length);
        String[] cdSign = bisBankReceiptDtlArrayVO.getCdSign().split(",", ids.length * 2);
        String[] isFrom = bisBankReceiptDtlArrayVO.getIsFrom().split(",", ids.length);
        String[] status = bisBankReceiptDtlArrayVO.getStatus().split(",", ids.length);
        for (int i = 0; i < ids.length; i++) {
            BisBankReceiptDtl bisBankReceiptDtls = new BisBankReceiptDtl();
            bisBankReceiptDtls.setId(ids[i]);
            bisBankReceiptDtls.setPayAccNo(payAccNo[i]);
            bisBankReceiptDtls.setPayAccName(payAccName[i]);
            bisBankReceiptDtls.setPayBankName(payBankName[i]);
            bisBankReceiptDtls.setBifCode(bifCode[i]);
            bisBankReceiptDtls.setPayeeAccNo(payeeAccNO[i]);
            bisBankReceiptDtls.setPayeeAccName(payeeAccName[i]);
            bisBankReceiptDtls.setPayeeBankName(payeeBankName[i]);
            if(StringUtils.isNotEmpty(transAmt[i])){
                bisBankReceiptDtls.setTransAmt(new BigDecimal(transAmt[i]));
            }
            bisBankReceiptDtls.setCurId(curId[i * 2 + 1]);
            bisBankReceiptDtls.setUsefor(usefor[i]);
            bisBankReceiptDtls.setAbs(abs[i]);
            bisBankReceiptDtls.setRemark(remark[i]);
            bisBankReceiptDtls.setBusType(busType[i]);
            bisBankReceiptDtls.setBusNo(busNo[i]);
            String charageDateStr=  charageDate[i];
            if(StringUtils.isNotEmpty(charageDateStr)){
               bisBankReceiptDtls.setCharageDate(simpleDateFormat.parse(charageDateStr));
            }
            bisBankReceiptDtls.setReceiptNo(receiptNo[i]);
            bisBankReceiptDtls.setReceiptCheckCode(receiptCheckCode[i]);
            bisBankReceiptDtls.setTransSerialNo(transSerialNo[i]);
            bisBankReceiptDtls.setNetCode(netCode[i]);
            bisBankReceiptDtls.setTellerNo(tellerNo[i]);
            bisBankReceiptDtls.setEbillimg(ebillimg[i].getBytes());
            bisBankReceiptDtls.setFiletype(filetype[i]);
            bisBankReceiptDtls.setPdfToImg(pdfToImg[i].getBytes());
            if(StringUtils.isNotEmpty(prints[i])){
                bisBankReceiptDtls.setPrints(Integer.valueOf(prints[i]));
            }
            bisBankReceiptDtls.setCdSign(cdSign[i * 2 + 1]);
            bisBankReceiptDtls.setIsFrom(isFrom[i]);
            bisBankReceiptDtls.setStatus(status[i]);
            bisBankReceiptDtlService.saveOrUpdate(bisBankReceiptDtls);
        }
        return success();
    }
    /**
     * 预览页面加载图片
     *
     * @return
     * @throws Exception 
     */
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        bisBankReceiptDtlService.download(id, request, response);
    }
    /**
     * 批量预览
     *
     * @return
     * @throws Exception 
     */
    @RequestMapping("/batchDownload")
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        bisBankReceiptDtlService.batchDownload(ids, request, response);
    }
}
