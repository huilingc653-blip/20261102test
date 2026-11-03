package com.bytter.biz.dataexchange.basic.controller;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
@Controller
@RequestMapping("/dataexchange2/fundata")
public class ErpFunDataController extends BaseController {
    @Autowired
    private IBtBankAccService bankAccService;
    @Autowired
    private INisBillheadService nisBillheadService;
    @Autowired
    private ICorpService corpService;
    /**
     * <br>
     * <b>功能：</b>查询单位/网点可用余额<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-9-11<br>
     * @param corpFlag
     * @param corpCode
     * @param days
     * @return
     * @throws Exception
     */
    @LogMessage(content = "查询单位/网点可用余额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/queryBankAccAvailableBal/{corpFlag}/{corpCode}/{days}")
    public AjaxJson queryBankAccAvailableBal(@PathVariable("corpFlag") String corpFlag, @PathVariable("corpCode") String corpCode,
                                             @PathVariable("days") String days, HttpServletResponse response) throws IOException {
        Integer day = 0;
        if(StringUtils.isNotEmpty(days)){
            day = Integer.valueOf(days);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        //获取可用余额
        Map<String, Object> balanceMap = new HashMap<>();
        balanceMap.put("balance", this.bankAccService.queryBankAccAvailableBal(corpFlag, corpCode));
        result.add(balanceMap);
        //获取付款计划
        Date dateNow = DateUtils.getDateYMD();
        for(int i=0; i<day; i++){
            Date date = DateUtils.addDay(dateNow, i);
            Map<String, Object> payMoneyMap = new HashMap<>();
            payMoneyMap.put("payMoney", this.nisBillheadService.queryNisBillhead(corpFlag, corpCode, date));
            payMoneyMap.put("wishPayday", DateUtils.formatDate(date, "yyyy-MM-dd"));
            result.add(payMoneyMap);
        }
        String json = JsonUtils.toJSONString(result);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().println(json);
        return null;
    }
    /**
     * <br>
     * <b>功能：</b>查询银行账号信息<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-9-11<br>
     * @param corpCode
     * @return
     * @throws Exception
     */
    @LogMessage(content = "查询银行账号信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/queryBankAcc/{corpCode}")
    public AjaxJson queryBankAcc(@PathVariable("corpCode") String corpCode, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        CorpDTO corp = corpService.getCorpByCode(corpCode);
        if(corp!=null) {
            BtBankAccDTO searchBean = new BtBankAccDTO();
            searchBean.setCorpId(corp.getId());
            searchBean.setIsOnline(BtBankAccConstant.ISONLINE_1);
            List<BtBankAccDTO> list = bankAccService.getBankAccList(searchBean);
            if (!EmptyUtils.isEmptyList(list)) {
                for (BtBankAccDTO bankAcc : list) {
                    Map<String, Object> bankAccMap = new HashMap<>();
                    bankAccMap.put("bankAcc", bankAcc.getBankAcc());
                    bankAccMap.put("accName", bankAcc.getAccName());
                    bankAccMap.put("bankName", bankAcc.getBankName());
                    bankAccMap.put("bankType", bankAcc.getBankTypeId());
                    bankAccMap.put("typeName", bankAcc.getBankTypeName());
                    bankAccMap.put("paysysbankcode", bankAcc.getBankCode());
                    result.add(bankAccMap);
                }
            }
        }
        String json = JsonUtils.toJSONString(result);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().println(json);
        return null;
    }
    /**
     * <b>功能：</b>查询付款信息<br>
     * <b>作者：</b>祁继鸿<br>
     * <b>日期：</b>2018-9-11<br>
     * @param beginDate
     * @param endDate
     * @return
     */
    @LogMessage(content = "查询付款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/queryNisbillHead/{beginDate}/{endDate}")
    public AjaxJson queryNisbillHead(@PathVariable("beginDate") String beginDate, @PathVariable("endDate") String endDate, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(beginDate)){
            beginDate = DateUtils.formatDateTime();
        }
        if(StringUtils.isEmpty(endDate)){
            endDate = DateUtils.formatDateTime();
        }
        List<NisBillheadDTO> list = nisBillheadService.listNisBillheadByTime(beginDate,endDate);
        List<Map<String, Object>> result = new ArrayList<>();
        if(!EmptyUtils.isEmptyList(list)){
            for(NisBillheadDTO dto :list){
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("payMoney",dto.getPayMoney());
                infoMap.put("voucherName",dto.getVoucherName());
                if("1".equals(dto.getIsForindividual())){
                    infoMap.put("isForindividual","对私付款");
                }else{
                    infoMap.put("isForindividual","对公付款");
                }
                infoMap.put("wishPayday", DateUtils.format(dto.getWishPayday()));
                infoMap.put("purpose",dto.getPurpose());
                infoMap.put("payeeAccName",dto.getPayeeAccName());
                infoMap.put("payeeAcc",dto.getPayeeAcc());
                infoMap.put("payeeBank",dto.getPayeeBank());
                infoMap.put("payerAccName",dto.getPayerAccName());
                infoMap.put("payerAcc",dto.getPayerAcc());
                infoMap.put("payerBank",dto.getPayerBank());
                infoMap.put("addWord",dto.getAddWord());
                infoMap.put("billNo",dto.getBillNo());
                result.add(infoMap);
            }
        }
        String json = JsonUtils.toJSONString(result);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().println(json);
        return null;
    }
}
