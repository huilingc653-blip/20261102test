package com.bytter.accountReceivable.writeOff.controller;

import com.alibaba.fastjson.JSONObject;
import com.bytter.accountReceivable.docManage.entity.DocManage;
import com.bytter.accountReceivable.docManage.vo.DocManageWriteOffVo;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.query.vo.ForceWriteOffVo;
import com.bytter.accountReceivable.writeOff.constant.WriteoffConstant;
import com.bytter.accountReceivable.writeOff.entity.DocWriteOffRelation;
import com.bytter.accountReceivable.writeOff.service.WriteOffService;
import com.bytter.accountReceivable.writeOff.vo.AbBillPayAndReceVO;
import com.bytter.accountReceivable.writeOff.vo.BisAccDtlVO;
import com.bytter.accountReceivable.writeOff.vo.DocWriteOffRelationVo;
import com.bytter.accountReceivable.writeOff.vo.LcBillArrivalVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应收款管理-核销处理
 */
@Controller
@RequestMapping("/accountReceivable/writeOff")
public class WriteOffController extends BaseController {

    @Autowired
    private WriteOffService writeOffService;

    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyServiceImpl;

    /**
     * 核销处理页面跳转
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "操作列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        ConfigDTO paymentMeansDto = configService.getConfig(ConfigConstant.BASIC, "paymentMeans");
        String paymentMeans = "";
        String paymentMeansDefault = "";
        if(paymentMeansDto!=null && StringUtils.isNotEmpty(paymentMeansDto.getValue())){
            paymentMeans = paymentMeansDto.getValue().substring(1,paymentMeansDto.getValue().length()-1);
            if(StringUtils.isNotEmpty(paymentMeans)){
                //设置默认显示的结算类型
                paymentMeansDefault = paymentMeans.split(",")[0];
            }
        }
        BtCurrencyDTO natualCurrency = currencyServiceImpl.getNatualCurrency();
        String curIdRMB = natualCurrency.getId();
        model.addAttribute("paymentMeans",paymentMeans);
        model.addAttribute("paymentMeansDefault",paymentMeansDefault);
        model.addAttribute("curIdRMB",curIdRMB);
        return "writeOff/listWriteOff";
    }

    /**
     * 查询核销处理 应收单列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/docDatatable")
    @ResponseBody
    public DataTablesOutput docDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getDocDataTablesOfWriteOff(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 查询核销处理 流水列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/dtlDatatable")
    @ResponseBody
    public DataTablesOutput dtlDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getDtlDataTablesOfWriteOff(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 查询核销处理 票据列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/billDatatable")
    @ResponseBody
    public DataTablesOutput billDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.billReceDataTable(dataTablesInput,getCurrentUser());
    }

    /**
     * 查询核销处理 票据列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/lcDatatable")
    @ResponseBody
    public DataTablesOutput lcDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getLcDataTablesOfWriteOff(dataTablesInput,getCurrentUser());
    }

    /**
     * 确认核销
     * @param relationVo
     * @return
     */
    @PostMapping("/sureWriteOff")
    @ResponseBody
    public AjaxJson sureWriteOff(DocWriteOffRelationVo relationVo){
        writeOffService.sureWriteOff(relationVo, this.getCurrentUser());
        return success();
    }

    /**
     * 跳转核销确认页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转核销汇率填写页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editWriteOffExchangeRate")
    public String editWriteOffExchangeRate(Model model, DocWriteOffRelationVo relationVo) {
        BigDecimal exchangeRate = BigDecimal.ZERO;
        if(!EmptyUtils.isEmpty(relationVo.getRelationOriginMoney()) && !BigDecimal.ZERO.equals(relationVo.getRelationOriginMoney())){
            exchangeRate = relationVo.getDocMoney().divide(relationVo.getRelationOriginMoney(), 4, BigDecimal.ROUND_HALF_UP);
        }
        model.addAttribute("exchangeRate", exchangeRate);
        relationVo.setRelationConvertMoney(relationVo.getRelationOriginMoney().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP));
        model.addAttribute("relationVo", relationVo);
        return "writeOff/editWriteOffExchangeRate";
    }
    // 审批详情页面

    @GetMapping("/infoApproveDetail")
    public String infoApproveDetail(Model model, DocWriteOffRelationVo relationVo){
        String docId = relationVo.getDocId();// 应收单据ID
        String batchNo =relationVo.getBatchNo() ;
        String type = relationVo.getType();
        // 应收单币种
        String docCurId=null;
        // 流水币种
        String flow="";

        if(WriteoffConstant.WRITE_OFF_RULE_3.equals(relationVo.getWriteOffType())){
            ForceWriteOffVo forceWriteOffVo = writeOffService.queryForceWriteOff(docId,batchNo);
            docCurId = forceWriteOffVo.getCurId();

            model.addAttribute("docManageVo", forceWriteOffVo);
        }else{
            List<DocManageWriteOffVo> docList = writeOffService.getDocRelation(docId, batchNo);
            model.addAttribute("docList", docList);
            docCurId=docList.get(0).getCurId();

            relationVo.setRate(docList.get(0).getExchangeRate());

            switch (type){
                case "0":
                    break;
                case "1":
                    List<BisAccDtlVO> dtlList = writeOffService.getDtlsOfWriteOffByList(relationVo.getBusinessId(),relationVo.getBatchNo());
                    for(BisAccDtlVO bisAccDtlVo:dtlList){
                        Date transTime = bisAccDtlVo.getTransTime();
                        String strTransTime = transTime.toString();
                        String dateTime = strTransTime.substring(11,strTransTime.length()-2);

                        bisAccDtlVo.setTransDateTime(dateTime);
                    }
                    flow=dtlList.get(0).getCurId();
                    model.addAttribute("dtlList", dtlList);
                    break;
                case "2":
                    List<AbBillPayAndReceVO> billList = writeOffService.billRecesByIds( relationVo.getBusinessId(),relationVo.getBatchNo());
                    flow=billList.get(0).getCurId();
                    model.addAttribute("billList", billList);
                    break;
                case "3":
                    List<LcBillArrivalVO> lcList = writeOffService.getLcsOfWriteOffByIds(relationVo.getBusinessId(),relationVo.getBatchNo());
                    flow=lcList.get(0).getCurId();
                    model.addAttribute("lcList", lcList);
                    break;
            }
        }

        model.addAttribute("isApprove", relationVo.getIsApprove());
        model.addAttribute("rate0",StringUtils.isEmpty(flow)||flow.equals(docCurId)?-1:relationVo.getRate());
        return "writeOff/detailWriteOffInfoDetail";
    }
    /**
     * 跳转手工核销详情页面
     * @return
     */
    @LogMessage(content = "详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/info")
    public String toWriteOffInfo(Model model, DocWriteOffRelationVo relationVo) {
        String type = relationVo.getType();
        if(EmptyUtils.isEmpty(relationVo.getRate())){
            relationVo.setRate(BigDecimal.ONE);
        }
        if(EmptyUtils.isNotEmpty(relationVo.getRateOpenStatus()) && !"undefined".equals(relationVo.getRateOpenStatus())){
            model.addAttribute("rate0", relationVo.getRate().setScale(4));
        }
        switch (type){
            case "0":

                relationVo.setDocId(relationVo.getBusinessId());
                List<DocManageWriteOffVo> docList = writeOffService.getDocsOfWriteOffDocDetail(relationVo);
                model.addAttribute("docList", docList);
                break;
            case "1":
                List<BisAccDtlVO> dtlList = writeOffService.getDocsOfWriteOffDtlDetail(relationVo);
                model.addAttribute("dtlList", dtlList);
                break;
            case "2":
                List<AbBillPayAndReceVO> billList = writeOffService.getDocsOfWriteOffBillDetail( relationVo);
                model.addAttribute("billList", billList);
                break;
            case "3":
                List<LcBillArrivalVO> lcList = writeOffService.getDocsOfWriteOffLcBillDetail( relationVo);
                model.addAttribute("lcList", lcList);
                break;
        }
        return "writeOff/viewWriteOffInfo";
    }

    /**
     * 跳转自动核销匹配规则设置页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转自动核销匹配规则设置页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/autoRulesSetting")
    public String autoRulesSetting(Model model,String docId) {
        model.addAttribute("docId",docId);
        return "writeOff/autoRulesSetting";
    }

    /**
     * 跳转核销确认页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转核销确认页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewSure")
    public String viewSure(Model model, DocWriteOffRelationVo relationVo) {
        List<DocManageWriteOffVo> docList = writeOffService.getDocsOfWriteOffByIds(relationVo.getDocId());
        String paymentMeans = relationVo.getPaymentMeans();
        if(!relationVo.getDocId().contains(",") && !relationVo.getRelationId().contains(",")){
            DocManageWriteOffVo doc = docList.get(0);
            // 一对一
            model.addAttribute("docIdOO",doc.getId());
            model.addAttribute("operationType", "OO");
            model.addAttribute("curType2", doc.getEnCode());
            model.addAttribute("writeOffStatus1", doc.getWriteOffStatus());
            model.addAttribute("docCode", doc.getCode());
            model.addAttribute("showCurMoney1", doc.getEnCode() + " " + doc.getWriteOffBal());
            BigDecimal executeMoney=new BigDecimal(0);
            switch (paymentMeans){
                case "1":
                    List<BisAccDtlVO> dtlList = writeOffService.getDtlsOfWriteOffByList(relationVo.getRelationId());
                    BisAccDtlVO dtl = dtlList.get(0);
                    executeMoney=(doc.getWriteOffBal().compareTo(dtl.getWriteOffBal()) > 0?dtl.getWriteOffBal(): doc.getWriteOffBal());
                    model.addAttribute("curType1", dtl.getEnCode());
                    model.addAttribute("money0", dtl.getWriteOffBal());
                    model.addAttribute("showCurMoney2", doc.getEnCode() + " " + executeMoney);
                    model.addAttribute("relationNo", dtl.getBankSerialNo());
                    model.addAttribute("showCurMoney3", dtl.getEnCode() + " " + dtl.getWriteOffBal());
                    model.addAttribute("showCurMoney4", doc.getEnCode() + " " + executeMoney);
                    model.addAttribute("writeOffStatus3", dtl.getWriteOffStatus());
                    model.addAttribute("writeOffStatus4", doc.getWriteOffBal().compareTo(dtl.getWriteOffBal()) >= 0?"2":"1");
                    model.addAttribute("writeOffStatus2", doc.getWriteOffBal().compareTo(dtl.getWriteOffBal()) > 0?"1":"2");

                    break;
                case "2":
                    List<AbBillPayAndReceVO> billList = writeOffService.billRecesByIds(relationVo.getRelationId());
                    AbBillPayAndReceVO bill = billList.get(0);
                    bill.setEnCode("CNY");
                    executeMoney=(doc.getWriteOffBal().compareTo(bill.getWriteOffBal()) > 0?bill.getWriteOffBal(): doc.getWriteOffBal());
                    model.addAttribute("curType1", bill.getEnCode());
                    model.addAttribute("money0", bill.getWriteOffBal());
                    model.addAttribute("executeMoney", bill.getWriteOffBal());
                    model.addAttribute("showCurMoney2", doc.getEnCode() + " " + executeMoney);
                    model.addAttribute("relationNo", bill.getOrigNo());
                    model.addAttribute("showCurMoney3", bill.getEnCode() + " " + bill.getWriteOffBal());
                    model.addAttribute("showCurMoney4", doc.getEnCode() + " " + executeMoney);
                    model.addAttribute("writeOffStatus3", bill.getWriteOffStatus());
                    model.addAttribute("writeOffStatus4", doc.getWriteOffBal().compareTo(bill.getWriteOffBal()) >= 0?"2":"1");
                    model.addAttribute("writeOffStatus2", doc.getWriteOffBal().compareTo(bill.getWriteOffBal()) > 0?"1":"2");
                    model.addAttribute("sbbllRngStrtSn", bill.getSbbllRngStrtSn());
                    model.addAttribute("sbbllRngEndSn", bill.getSbbllRngEndSn());
                    break;
                case "3":
                    List<LcBillArrivalVO> lcList = writeOffService.getLcsOfWriteOffByIds(relationVo.getRelationId());
                    LcBillArrivalVO lc = lcList.get(0);
                    executeMoney=(doc.getWriteOffBal().compareTo(lc.getWriteOffBal()) > 0?lc.getWriteOffBal(): doc.getWriteOffBal());
                    model.addAttribute("curType1", lc.getEnCode());
                    model.addAttribute("money0", lc.getWriteOffBal());
                    model.addAttribute("executeMoney", lc.getWriteOffBal());
                    model.addAttribute("showCurMoney2", doc.getEnCode() + " " + executeMoney);
                    model.addAttribute("relationNo", lc.getArrivalCode());
                    model.addAttribute("showCurMoney3", lc.getEnCode() + " " + lc.getWriteOffBal());
                    model.addAttribute("showCurMoney4", doc.getEnCode() + " " + executeMoney);
                    model.addAttribute("writeOffStatus3", lc.getWriteOffStatus());
                    model.addAttribute("writeOffStatus4", doc.getWriteOffBal().compareTo(lc.getWriteOffBal()) >= 0?"2":"1");
                    model.addAttribute("writeOffStatus2", doc.getWriteOffBal().compareTo(lc.getWriteOffBal()) > 0?"1":"2");
                    break;
            }
        }else if(relationVo.getDocId().contains(",") && !relationVo.getRelationId().contains(",")){
            DocManageWriteOffVo doc = docList.get(0);
            model.addAttribute("operationType", "MO");
            model.addAttribute("curType2", doc.getEnCode());
            model.addAttribute("docList", docList);
            BigDecimal tempMoney = BigDecimal.ZERO;
            for (DocManageWriteOffVo vo : docList) {
                tempMoney = tempMoney.add(vo.getWriteOffBal());
            }
            switch (paymentMeans){
                case "1":
                    List<BisAccDtlVO> dtlList = writeOffService.getDtlsOfWriteOffByList(relationVo.getRelationId());
                    BisAccDtlVO dtl = dtlList.get(0);
                    model.addAttribute("curType1", dtl.getEnCode());
                    model.addAttribute("money0", dtl.getWriteOffBal());
                    model.addAttribute("relationNo", dtl.getBankSerialNo());
                    model.addAttribute("showCurMoney3", dtl.getEnCode() + " " + dtl.getWriteOffBal());
                    model.addAttribute("writeOffStatus3", dtl.getWriteOffStatus());
                    model.addAttribute("writeOffStatus4", tempMoney.compareTo(dtl.getWriteOffBal()) >= 0?"2":"1");
                    BigDecimal sumExecuteMoney = BigDecimal.ZERO;
                    for (DocManageWriteOffVo temp : docList) {
                        temp.setExecuteMoney(temp.getWriteOffBal().compareTo(dtl.getWriteOffBal()) > 0?dtl.getWriteOffBal(): temp.getWriteOffBal());
                        temp.setNewWriteOffStatus(temp.getWriteOffBal().compareTo(dtl.getWriteOffBal()) > 0?"1":"2");
                        sumExecuteMoney = sumExecuteMoney.add(temp.getExecuteMoney());
                    }
                    model.addAttribute("sumWriteOffBal", doc.getEnCode() + " " + sumExecuteMoney);
                    model.addAttribute("showCurMoney4", doc.getEnCode() + " " + (sumExecuteMoney.compareTo(dtl.getWriteOffBal()) > 0?dtl.getWriteOffBal(): sumExecuteMoney));
                    break;
                case "2":
                    List<AbBillPayAndReceVO> billList = writeOffService.billRecesByIds(relationVo.getRelationId());
                    AbBillPayAndReceVO bill = billList.get(0);
                    bill.setEnCode("CNY");
                    model.addAttribute("curType1", bill.getEnCode());
                    model.addAttribute("money0", bill.getWriteOffBal());
                    model.addAttribute("relationNo", bill.getOrigNo());
                    model.addAttribute("showCurMoney3", bill.getEnCode() + " " + bill.getWriteOffBal());
                    model.addAttribute("writeOffStatus3", bill.getWriteOffStatus());
                    model.addAttribute("writeOffStatus4", tempMoney.compareTo(bill.getWriteOffBal()) >= 0?"2":"1");
                    model.addAttribute("sbbllRngStrtSn", bill.getSbbllRngStrtSn());
                    model.addAttribute("sbbllRngEndSn", bill.getSbbllRngEndSn());
                    BigDecimal sumExecuteMoney2 = BigDecimal.ZERO;
                    for (DocManageWriteOffVo temp : docList) {
                        temp.setExecuteMoney(temp.getWriteOffBal().compareTo(bill.getWriteOffBal()) > 0?bill.getWriteOffBal(): temp.getWriteOffBal());
                        temp.setNewWriteOffStatus(temp.getWriteOffBal().compareTo(bill.getWriteOffBal()) > 0?"1":"2");
                        sumExecuteMoney2 = sumExecuteMoney2.add(temp.getExecuteMoney());
                    }
                    model.addAttribute("sumWriteOffBal", doc.getEnCode() + " " + sumExecuteMoney2);
                    model.addAttribute("showCurMoney4", doc.getEnCode() + " " + (sumExecuteMoney2.compareTo(bill.getWriteOffBal()) > 0?bill.getWriteOffBal(): sumExecuteMoney2));
                    break;
                case "3":
                    List<LcBillArrivalVO> lcList = writeOffService.getLcsOfWriteOffByIds(relationVo.getRelationId());
                    LcBillArrivalVO lc = lcList.get(0);
                    model.addAttribute("curType1", lc.getEnCode());
                    model.addAttribute("money0", lc.getWriteOffBal());
                    model.addAttribute("relationNo", lc.getArrivalCode());
                    model.addAttribute("showCurMoney3", lc.getEnCode() + " " + lc.getWriteOffBal());
                    model.addAttribute("writeOffStatus3", lc.getWriteOffStatus());
                    model.addAttribute("writeOffStatus4", tempMoney.compareTo(lc.getWriteOffBal()) >= 0?"2":"1");
                    BigDecimal sumExecuteMoney3 = BigDecimal.ZERO;
                    for (DocManageWriteOffVo temp : docList) {
                        temp.setExecuteMoney(temp.getWriteOffBal().compareTo(lc.getWriteOffBal()) > 0?lc.getWriteOffBal(): temp.getWriteOffBal());
                        temp.setNewWriteOffStatus(temp.getWriteOffBal().compareTo(lc.getWriteOffBal()) > 0?"1":"2");
                        sumExecuteMoney3 = sumExecuteMoney3.add(temp.getExecuteMoney());
                    }
                    model.addAttribute("sumWriteOffBal", doc.getEnCode() + " " + sumExecuteMoney3);
                    model.addAttribute("showCurMoney4", doc.getEnCode() + " " + (sumExecuteMoney3.compareTo(lc.getWriteOffBal()) > 0?lc.getWriteOffBal(): sumExecuteMoney3));
                    break;
            }
        }else if(!relationVo.getDocId().contains(",") && relationVo.getRelationId().contains(",")){
            DocManageWriteOffVo doc = docList.get(0);
            model.addAttribute("operationType", "OM");
            model.addAttribute("curType2", docList.get(0).getEnCode());
            model.addAttribute("writeOffStatus1", doc.getWriteOffStatus());
            model.addAttribute("docCode", doc.getCode());
            model.addAttribute("showCurMoney1", doc.getEnCode() + " " + doc.getWriteOffBal());
            switch (paymentMeans){
                case "1":
                    List<BisAccDtlVO> dtlList = writeOffService.getDtlsOfWriteOffByList(relationVo.getRelationId());
                    BisAccDtlVO dtl = dtlList.get(0);
                    model.addAttribute("curType1", dtl.getEnCode());
                    BigDecimal m0 = BigDecimal.ZERO;

                    for (BisAccDtlVO vo : dtlList) {
                        BigDecimal tm = doc.getWriteOffBal().compareTo(vo.getWriteOffBal()) > 0?vo.getWriteOffBal(): doc.getWriteOffBal();
                        vo.setCustomCol1(vo.getBankSerialNo());
                        vo.setCustomCol2(vo.getEnCode() + " " + vo.getWriteOffBal());
                        vo.setCustomCol3(doc.getEnCode() + " " + tm);
                        vo.setCustomCol4(vo.getWriteOffStatus());
                        vo.setCustomCol5(doc.getWriteOffBal().compareTo(vo.getWriteOffBal()) >= 0?"2":"1");
                        m0 = m0.add(tm);
                    }
                    model.addAttribute("showCurMoney2", doc.getEnCode() + " " + (doc.getWriteOffBal().compareTo(m0) > 0?m0: doc.getWriteOffBal()));
                    model.addAttribute("dtlList", dtlList);
                    model.addAttribute("writeOffStatus2", doc.getWriteOffBal().compareTo(m0) > 0?"1":"2");
                    model.addAttribute("sumWriteOffBal", doc.getEnCode() + " " + m0);
                    model.addAttribute("money0", m0);
                    break;
                case "2":
                    List<AbBillPayAndReceVO> billList = writeOffService.billRecesByIds(relationVo.getRelationId());
                    AbBillPayAndReceVO bill = billList.get(0);
                    bill.setEnCode("CNY");
                    model.addAttribute("curType1", bill.getEnCode());
                    BigDecimal m1 = BigDecimal.ZERO;
                    for (AbBillPayAndReceVO vo : billList) {
                        vo.setEnCode("CNY");
                        BigDecimal tm = doc.getWriteOffBal().compareTo(vo.getWriteOffBal()) > 0?vo.getWriteOffBal(): doc.getWriteOffBal();
                        vo.setCustomCol1(vo.getOrigNo());
                        vo.setCustomCol2(vo.getEnCode() + " " + vo.getWriteOffBal());
                        vo.setCustomCol3(doc.getEnCode() + " " + tm);
                        vo.setCustomCol4(vo.getWriteOffStatus());
                        vo.setCustomCol5(doc.getWriteOffBal().compareTo(vo.getWriteOffBal()) >= 0?"2":"1");
                        vo.setCustomCol6(vo.getSbbllRngStrtSn());
                        vo.setCustomCol7(vo.getSbbllRngEndSn());
                        m1 = m1.add(tm);
                    }
                    model.addAttribute("showCurMoney2", doc.getEnCode() + " " + (doc.getWriteOffBal().compareTo(m1) > 0?m1: doc.getWriteOffBal()));
                    model.addAttribute("dtlList", billList);
                    model.addAttribute("writeOffStatus2", doc.getWriteOffBal().compareTo(m1) > 0?"1":"2");
                    model.addAttribute("sumWriteOffBal", doc.getEnCode() + " " + m1);
                    model.addAttribute("money0", m1);

                    break;
                case "3":
                    List<LcBillArrivalVO> lcList = writeOffService.getLcsOfWriteOffByIds(relationVo.getRelationId());
                    LcBillArrivalVO lc = lcList.get(0);
                    model.addAttribute("curType1", lc.getEnCode());
                    BigDecimal m2 = BigDecimal.ZERO;
                    for (LcBillArrivalVO vo : lcList) {
                        BigDecimal tm = doc.getWriteOffBal().compareTo(vo.getWriteOffBal()) > 0?vo.getWriteOffBal(): doc.getWriteOffBal();
                        vo.setCustomCol1(vo.getArrivalCode());
                        vo.setCustomCol2(vo.getEnCode() + " " + vo.getWriteOffBal());
                        vo.setCustomCol3(doc.getEnCode() + " " + tm);
                        vo.setCustomCol4(vo.getWriteOffStatus());
                        vo.setCustomCol5(doc.getWriteOffBal().compareTo(vo.getWriteOffBal()) >= 0?"2":"1");
                        m2 = m2.add(tm);
                    }
                    model.addAttribute("showCurMoney2", doc.getEnCode() + " " + (doc.getWriteOffBal().compareTo(m2) > 0?m2: doc.getWriteOffBal()));
                    model.addAttribute("dtlList", lcList);
                    model.addAttribute("writeOffStatus2", doc.getWriteOffBal().compareTo(m2) > 0?"1":"2");
                    model.addAttribute("sumWriteOffBal", doc.getEnCode() + " " + m2);
                    model.addAttribute("money0", m2);
                    break;
            }
        }
        model.addAttribute("relationVo", relationVo);
        model.addAttribute("batchNo", writeOffService.getWriteOffBatchNo());
        model.addAttribute("paymentMeans", paymentMeans);
        return "writeOff/viewWriteOff2";
    }

    /**
     * 跳转自动核销确认页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转核销确认页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewSureAuto")
    public String viewSureAuto(Model model, DocWriteOffRelationVo relationVo) {
        model.addAttribute("relationVo",relationVo);
        return "writeOff/viewWriteOffAuto";
    }

    /**
     * 跳转自动核销详情页面
     * @return
     */
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转自动核销详情页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDetailAuto")
    public String viewDetailAuto(Model model,String docId) {
        model.addAttribute("docId",docId);
        return "writeOff/viewDetailAuto";
    }

    /**
     * 查询确认核销处理 列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/sureDatatable")
    @ResponseBody
    public DataTablesOutput sureDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getSureDataByInput(dataTablesInput);
    }

    /**
     * 查询自动核销处理 应收单和流水列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/sureAutoDatatable")
    @ResponseBody
    public DataTablesOutput sureAutoDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getSureAutoDataByInput(dataTablesInput);
    }

    /**
     * 跳转核销确认页面
     * @return
     */
//     @RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转核销确认页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewHis")
    public String viewHis(Model model, String docId) {
        List<DocWriteOffRelation> writeOffHis = writeOffService.getWriteOffHisByDocId1(docId, null);
        if (writeOffHis.size()==1){
            DocWriteOffRelation relation = writeOffHis.get(0);
            model.addAttribute("relation",relation);
        }
        model.addAttribute("writeOffHis",writeOffHis);
        model.addAttribute("writeOffSize",writeOffHis.size());
        return "writeOff/viewHisWriteOff";
    }

    /**
     * 跳转核销取消页面页面
     * @return
     */
    //@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转核销确认页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewHisV")
    public String viewHisV(Model model, String docId) {
        model.addAttribute("docId", docId);
        return "query/viewHisWrite";
    }

    /**
     * 查询历史核销处理 列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @PostMapping("/hisDatatable")
    @ResponseBody
    public DataTablesOutput hisDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getWriteOffHisDataTablesByDocId(dataTablesInput);
    }

    /**
     * 取消核销
     * @param relationVo
     * @return
     */
    @PostMapping("/cancelWriteOff")
    @ResponseBody
    public AjaxJson cancelWriteOff(DocWriteOffRelationVo relationVo){
        writeOffService.cancelWriteOff(relationVo, this.getCurrentUser());
        return success();
    }

    /**
     * 跳转强制核销页面
     * @return
     */
    //@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_WRITEOFF_OP)
    @LogMessage(content = "跳转强制核销页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewForce")
    public String viewForce(Model model, String docId) {
        DocManageVo docManageVo = writeOffService.getForceViewDataByDocId(docId);
        model.addAttribute("docId", docId);
        model.addAttribute("docManageVo", docManageVo);
        return "writeOff/editForceWriteOff";
    }

    /**
     * 自动核销 - 仅支持流水
     * @param relationVo
     * @return
     */
    @PostMapping("/forceWriteOff")
    @ResponseBody
    public AjaxJson forceWriteOff(DocWriteOffRelationVo relationVo){
        writeOffService.forceWriteOff(relationVo, this.getCurrentUser());
        return success();
    }

    /**
     * 核销处理审批 页面跳转
     * @return
     */
    @LogMessage(content = "操作列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDocWriteOffApprove")
    public String listDocWriteOffApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "writeOff/listDocWriteOffApprove";
    }

    /**
     * 查询核销处理审批 列表数据
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/writeOffApproveDatatable")
    @ResponseBody
    public DataTablesOutput writeOffApproveDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return writeOffService.getWriteOffBatchDataApprove(dataTablesInput);
    }

    /**
     * 核销处理审批 页面跳转
     * @return
     */
    @LogMessage(content = "操作列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if(StringUtils.isNotEmpty(businessId)){
            DataTablesInput input = new DataTablesInput();
            input.getFormdata().put("eqs_batchNo", businessId);
            DataTablesOutput out = writeOffService.getWriteOffBatchData(input);
            DocWriteOffRelationVo relationVo = (DocWriteOffRelationVo)out.getData().get(0);
            model.addAttribute("taskId", taskId);
            model.addAttribute("batchNo", businessId);
            model.addAttribute("relationVo", relationVo);
        }
        return "writeOff/viewWriteOffApprove";
    }

    /**
     * 核销处理审批 页面跳转
     * @return
     */
    @LogMessage(content = "操作列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewHisDetail")
    public String viewHisDetail(DocWriteOffRelationVo searchVo, Model model) {
        if(StringUtils.isNotEmpty(searchVo.getBatchNo())){
            DataTablesInput input = new DataTablesInput();
            DataTablesOutput out = writeOffService.getWriteOffBatchData(input);
            DocWriteOffRelationVo relationVo = (DocWriteOffRelationVo)out.getData().get(0);
            if(StringUtils.isNotEmpty(searchVo.getRelationId())){
                relationVo.setRelationId(searchVo.getRelationId());
            }
            model.addAttribute("batchNo", searchVo.getBatchNo());
            model.addAttribute("relationVo", relationVo);
        }
        return "writeOff/viewHisWriteOffDetail";
    }
    /**
     * 核销处理审批 页面跳转
     * @return
     */
    @LogMessage(content = "操作列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewHisDetailV2")
    public String viewHisDetail(String docId,String batchNo, Model model) {
        ForceWriteOffVo forceWriteOffVo = writeOffService.queryForceWriteOff(docId,batchNo);
        model.addAttribute("docManageVo", forceWriteOffVo);
        model.addAttribute("corpId", forceWriteOffVo.getCorpId());
        return "query/viewForceWriteOff";
    }
    /**
     * 获取取消核销历史记录列表长度
     * @return
     */
    @LogMessage(content = "获取取消核销历史记录列表长度", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryWriteOffHisSize")
    @ResponseBody
    public String queryWriteOffHisSize(String docId,String batchNo) {
        List<DocWriteOffRelation> writeOffHis = writeOffService.getWriteOffHisByDocId(docId, null);
        return String.valueOf(writeOffHis.size());

    }
}
