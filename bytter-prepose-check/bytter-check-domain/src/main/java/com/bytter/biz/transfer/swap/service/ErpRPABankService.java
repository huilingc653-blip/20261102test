package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptPdfDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
@Transactional
@Service("erpRPABankService")
public class ErpRPABankService {
    @Value("${rap.date}")
    private int date;
    @Value("${rap.bankType}")
    private String bankType;
    @Value("${rap.bankTypeName}")
    private String bankTypeName;
    @Value("${rap.dtlUrl}")
    private String dtlUrl;
    @Value("${rap.balUrl}")
    private String balUrl;
    @Value("${rap.billUrl}")
    private String billUrl;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 获取查询明细的查询条件
     *
     * @return
     * @throws Exception
     */
    public Map<String, Object> fetchDetailInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        ConfigDTO configNumber = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_RPA_DATE_NUMBER);
        int date = configNumber.toInteger();
        Date endDate = new Date();
        Date beginDate = DateUtils.addDay(endDate, -date);
        map.put("beginTime", beginDate);
        map.put("endTime", endDate);
        List<BtBankAccDTO> list = btBankAccService.getBankAccList(new BtBankAccDTO());
        List<String> account = new ArrayList<>();
        for (BtBankAccDTO v : list) {
            account.add(v.getBankAcc());
        }
        map.put("account", account);
        return map;
    }
    /**
     * 根据银行明细插入历史明细表
     *
     * @param mapListJson
     * @throws Exception
     */
    public void toSaveBankDtl(List<Map<String, Object>> mapListJson) throws Exception {
        if (null != mapListJson && mapListJson.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map<String, Object> map : mapListJson) {
                BisAccDtlDTO dtl = new BisAccDtlDTO();
                String id = map.get("id").toString();
                BisAccDtlDTO vdtl = bisAccDtlService.getById(id);
                //如果明细没有则插入明细
                if (EmptyUtils.isEmpty(vdtl)) {
                    dtl.setId(id);
                    dtl.setBankAcc(map.get("bankAccount") == null ? "" : map.get("bankAccount").toString());
                    dtl.setAccName(map.get("accountName") == null ? "" : map.get("accountName").toString());
                    BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(dtl.getBankAcc());
                    dtl.setCurId(btCurrencyService.getRMBCurrency().getId());
                    if (null != bankAccInfo) {
                        dtl.setBankName(bankAccInfo.getBankName());
                        dtl.setBifCode(bankAccInfo.getBifCode());
                    } else {
                        dtl.setBankName(map.get("bankName") == null ? "" : map.get("bankName").toString());
                    }
                    dtl.setOppAccNo(map.get("oppositeBankAccount") == null ? "" : map.get("oppositeBankAccount").toString());
                    dtl.setOppAccName(map.get("oppositeAccountName") == null ? "" : map.get("oppositeAccountName").toString());
                    BtBankAccDTO oppAccount = btBankAccService.getBankAccInfo(dtl.getOppAccNo());
                    if (null != oppAccount) {
                        dtl.setOppAccBank(oppAccount.getBankName());
                        if (null == bankAccInfo) {
                            dtl.setBifCode(oppAccount.getBifCode());
                        }
                    } else {
                        dtl.setOppAccBank(map.get("oppositeBankName") == null ? "" : map.get("oppositeBankName").toString());
                    }
                    dtl.setCdSign(map.get("cdSign").toString());
                    dtl.setRbSign("2");
                    dtl.setAmt(new BigDecimal(Double.parseDouble(map.get("amount").toString())));
                    dtl.setBal(new BigDecimal(Double.parseDouble(map.get("balance").toString())));
                    dtl.setUses(map.get("uses") == null ? "" : map.get("uses").toString());
                    dtl.setPostscript(map.get("postScript") == null ? "" : map.get("postScript").toString());
                    dtl.setRemark(map.get("remark") == null ? "" : map.get("remark").toString());
                    dtl.setTransTime(sdf.parse(map.get("tradeTime").toString()));
                    dtl.setCreateDate(DateUtils.getDate());
                    bisAccDtlService.saveDtlByDto(dtl);
                }
            }
        }
    }
    /**
     * RPA 银行账号余额条件接口
     *
     * @return
     * @throws Exception
     */
    public Map<String, Object> fetchBalanceInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        ConfigDTO configNumber = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_RPA_DATE_NUMBER);
        int date = configNumber.toInteger();
        //银行类型
        String[] bankTypeStr = null;
        List<String> bankTypeList = new ArrayList<>();
        if (null != bankType && bankType.length() > 0) {
            bankTypeStr = bankType.split(",");
            if (null != bankTypeStr && bankTypeStr.length > 0) {
                for (int i = 0; i < bankTypeStr.length; i++) {
                    bankTypeList.add(bankTypeStr[i]);
                }
            }
        }
        map.put("bankType", bankTypeList);
        Date endDate = new Date();
        Date beginDate = DateUtils.addDay(endDate, -date);
        map.put("beginTime", beginDate);
        map.put("endTime", endDate);
        List<BtBankAccDTO> list = btBankAccService.getBankAccList(new BtBankAccDTO());
        List<String> account = new ArrayList<>();
        for (BtBankAccDTO v : list) {
            account.add(v.getBankAcc());
        }
        map.put("account", account);
        return map;
    }
    /**
     * 根据银行余额插入历史余额表
     *
     * @param mapListJson
     * @throws Exception
     */
    public void toSaveBankBal(List<Map<String, Object>> mapListJson) throws Exception {
        if (null != mapListJson && mapListJson.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Map<String, Object> map : mapListJson) {
                String bankacc = map.get("bankAccount").toString();
                String curId = btCurrencyService.getRMBCurrency().getId();
                Date balDate = sdf.parse(map.get("getDate").toString());
                //如果明细没有则插入明细
                BisAccBalDTO balDTO = new BisAccBalDTO();
                BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(bankacc);
                if (null != bankAccInfo) {
                    balDTO.setCorpId(bankAccInfo.getCorpId());
                    balDTO.setBifCode(bankAccInfo.getBifCode());
                }
                BisAccBalDTO vbalDTO = bisAccBalService.getBisAccBalBy(bankacc, curId, balDate);
                if (EmptyUtils.isEmpty(vbalDTO)) {
                    balDTO.setBankAcc(bankacc);
                    balDTO.setCurId(curId);
                    balDTO.setBalDate(balDate);
                    balDTO.setGetDate(sdf.parse(map.get("getDate").toString()));
                    balDTO.setBal(BigDecimal.valueOf(Double.parseDouble(map.get("balance").toString())));
                    balDTO.setAvailBal(BigDecimal.valueOf(Double.parseDouble(map.get("freeBalance").toString())));
                    balDTO.setCreateDate(DateUtils.getDate());
                    bisAccBalService.saveBalByDto(balDTO);
                } else {
                    vbalDTO.setBal(BigDecimal.valueOf(Double.parseDouble(map.get("balance").toString())));
                    vbalDTO.setAvailBal(BigDecimal.valueOf(Double.parseDouble(map.get("freeBalance").toString())));
                    bisAccBalService.updateBalByDto(vbalDTO);
                }
            }
        }
    }
    /**
     * RPA 银行电子回单查询条件接口
     *
     * @return
     * @throws Exception
     */
    public Map<String, Object> fetchBillInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        ConfigDTO configNumber = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_RPA_DATE_NUMBER);
        int date = configNumber.toInteger();
        String[] bankTypeStr = null;
        List<String> bankTypeList = new ArrayList<>();
        if (null != bankType && bankType.length() > 0) {
            bankTypeStr = bankType.split(",");
            if (null != bankTypeStr && bankTypeStr.length > 0) {
                for (int i = 0; i < bankTypeStr.length; i++) {
                    bankTypeList.add(bankTypeStr[i]);
                }
            }
        }
        map.put("bankType", bankTypeList);
        Date endDate = new Date();
        Date beginDate = DateUtils.addDay(endDate, -date);
        map.put("beginTime", beginDate);
        map.put("endTime", endDate);
        List<BtBankAccDTO> list = btBankAccService.getBankAccList(new BtBankAccDTO());
        List<String> account = new ArrayList<>();
        for (BtBankAccDTO v : list) {
            account.add(v.getBankAcc());
        }
        map.put("account", account);
        return map;
    }
    /**
     * 插入银行电子回单
     *
     * @param mapListJson
     * @throws Exception
     */
    public void toSaveBankBill(List<Map<String, Object>> mapListJson) throws Exception {
        if (null != mapListJson && mapListJson.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Map<String, Object> map : mapListJson) {
                String id = map.get("id").toString();
                BisBankReceiptDtlDTO dto = bisBankReceiptDtlService.getById(id);
                if (StringUtils.isEmpty(dto.getId())) {
                    BisBankReceiptDtlDTO bisBankReceiptDtlDTO = new BisBankReceiptDtlDTO();
                    BisBankReceiptPdfDTO bisBankReceiptPdfDTO = new BisBankReceiptPdfDTO();
                    bisBankReceiptDtlDTO.setCdSign(map.get("cdSign") == null ? "" : map.get("cdSign").toString());
                    bisBankReceiptDtlDTO.setId(map.get("id").toString());
                    bisBankReceiptDtlDTO.setPayAccNo(map.get("bankAccount") == null ? "" : map.get("bankAccount").toString());
                    bisBankReceiptDtlDTO.setPayAccName(map.get("accountName") == null ? "" : map.get("accountName").toString());
                    bisBankReceiptDtlDTO.setPayeeAccNo(map.get("oppositeBankAccount") == null ? "" : map.get("oppositeBankAccount").toString());
                    bisBankReceiptDtlDTO.setPayeeAccName(map.get("oppositeAccountName") == null ? "" : map.get("oppositeAccountName").toString());
                    bisBankReceiptDtlDTO.setCurId(btCurrencyService.getRMBCurrency().getId());
                    if ("1".equals(bisBankReceiptDtlDTO.getCdSign())) {
                        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(bisBankReceiptDtlDTO.getPayAccNo());
                        if (null != bankAccInfo) {
                            bisBankReceiptDtlDTO.setPayBankName(bankAccInfo.getBankName());
                            bisBankReceiptDtlDTO.setBifCode(bankAccInfo.getBifCode());
                            bisBankReceiptDtlDTO.setCdSign("1");
                        } else {
                            bisBankReceiptDtlDTO.setPayBankName(map.get("bankName") == null ? "" : map.get("bankName").toString());
                        }
                    } else {
                        BtBankAccDTO bankAccInfo1 = btBankAccService.getBankAccInfo(bisBankReceiptDtlDTO.getPayeeAccNo());
                        if (null != bankAccInfo1) {
                            bisBankReceiptDtlDTO.setPayeeBankName(bankAccInfo1.getBankName());
                            bisBankReceiptDtlDTO.setBifCode(bankAccInfo1.getBifCode());
                            bisBankReceiptDtlDTO.setCdSign("0");
                        } else {
                            bisBankReceiptDtlDTO.setPayeeBankName(map.get("oppositeBankName") == null ? "" : map.get("oppositeBankName").toString());
                        }
                    }
                    bisBankReceiptDtlDTO.setTransAmt(BigDecimal.valueOf(Double.parseDouble(map.get("tranAmt").toString())));
                    bisBankReceiptDtlDTO.setRemark(map.get("remark") == null ? "" : map.get("remark").toString());
                    bisBankReceiptDtlDTO.setUsefor(map.get("uses") == null ? "" : map.get("uses").toString());
                    bisBankReceiptDtlDTO.setAbs(map.get("postScript") == null ? "" : map.get("postScript").toString());
                    bisBankReceiptDtlDTO.setCharageDate(map.get("tradeDate") == null ? null : sdf.parse(map.get("tradeDate").toString()));
                    bisBankReceiptDtlDTO.setReceiptNo(map.get("receiptNo") == null ? "" : map.get("receiptNo").toString());
                    bisBankReceiptDtlDTO.setReceiptCheckCode(map.get("receiptNo") == null ? "" : map.get("receiptNo").toString());
                    bisBankReceiptDtlDTO.setTransSerialNo(map.get("seqNo") == null ? "" : map.get("seqNo").toString());
                    bisBankReceiptDtlDTO.setPrints(0);
                    String receiptInfo = map.get("receiptInfo").toString();
                    if (receiptInfo.contains("data:image/png;base64,")) {
                        bisBankReceiptDtlDTO.setFiletype("png");
                        receiptInfo = receiptInfo.substring(22);
                        byte[] bytes = com.bytter.core.utils.Base64.base64ToByteArray(receiptInfo);
                        bisBankReceiptPdfDTO.setPdfToImg(this.byte2string(bytes).getBytes());
                        bisBankReceiptPdfDTO.setEbillimg(this.byte2string(bytes).getBytes());
//                        FileCopyUtils.copy(com.bytter.core.utils.Base64.base64ToByteArray(receiptInfo),new File("D:\\123.png"));
                    } else {
                        bisBankReceiptDtlDTO.setFiletype("pdf");
                        byte[] bytes = com.bytter.core.utils.Base64.base64ToByteArray(receiptInfo);
                        bisBankReceiptPdfDTO.setPdfToImg(this.byte2string(bytes).getBytes());
                        bisBankReceiptPdfDTO.setEbillimg(this.byte2string(bytes).getBytes());
//                        FileCopyUtils.copy(com.bytter.core.utils.Base64.base64ToByteArray(receiptInfo),new File("D:\\123.png"));
                    }
                    bisBankReceiptDtlService.saveBankBill(bisBankReceiptDtlDTO, bisBankReceiptPdfDTO);
                }
            }
        }
    }
    /**
     * set属性
     *
     * @param model
     */
    public void setModel(Model model) {
        ConfigDTO configNumber = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_RPA_DATE_NUMBER);
        int date = configNumber.toInteger();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = new Date();
        Date startDate = DateUtils.addDay(endDate, -date);
        String startDateStr = sdf.format(startDate);
        String endDateStr = sdf.format(endDate);
        //银行类型
        String[] bankTypeStr = null;
        String[] bankTypeNameStr = null;
        List<Map<String, String>> bankTypeList = new ArrayList<>();
        if (null != bankType && bankType.length() > 0) {
            bankTypeStr = bankType.split(",");
            bankTypeNameStr = bankTypeName.split(",");
            if (null != bankTypeStr && bankTypeStr.length > 0) {
                for (int i = 0; i < bankTypeStr.length; i++) {
                    Map<String, String> map = new HashMap<>();
                    map.put("bankType", bankTypeStr[i]);
                    map.put("bankTypeName", bankTypeNameStr[i]);
                    bankTypeList.add(map);
                }
            }
        }
        model.addAttribute("bankTypeList", bankTypeList);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("startDate", startDateStr);
    }
    /**
     * 更改byte格式
     *
     * @param data
     * @return
     */
    public String byte2string(byte[] data) {
        if (data == null || data.length <= 1) {
            return "0x";
        }
        if (data.length > 400000) {
            return "0x";
        }
        StringBuffer sb = new StringBuffer();
        int buf[] = new int[data.length];
        for (int k = 0; k < data.length; k++) {
            buf[k] = data[k] < 0 ? (data[k] + 256) : (data[k]);
        }
        for (int k = 0; k < buf.length; k++) {
            if (buf[k] < 16) {
                sb.append("0" + Integer.toHexString(buf[k]));
            } else {
                sb.append(Integer.toHexString(buf[k]));
            }
        }
        return sb.toString();
    }
    /**
     * 主动查询保存明细，余额，电子回单
     *
     * @param type
     * @param bankType
     * @param startDate
     * @param endDate
     * @throws Exception
     */
    public void toSearchBank(String type, String bankType, String startDate, String endDate) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("beginTime", startDate);
        param.put("endTime", endDate);
        if (null != bankType && !"".equals(bankType)) {
            param.put("bankType", bankType);
        }
        List<BtBankAccDTO> list = btBankAccService.getBankAccList(new BtBankAccDTO());
        int count = 0;
        if (list.size() < 100) {
            count = list.size();
            String account = "";
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    account = list.get(i).getBankAcc();
                } else {
                    account += "," + list.get(i).getBankAcc();
                }
            }
            if (null != account && !"".equals(account)) {
                param.put("account", account);
            }
            toSendGet(type, param);
        } else {//如果账号过多刚分多次发送请求
            int urlCount = list.size() / 100;
            for (int k = 0; k <= urlCount; k++) {
                Map<String, String> paramTemp = param;
                count = (k + 1) * 100;
                if (count > list.size()) {
                    count = list.size();
                }
                String account = "";
                for (int i = k * 100; i < count; i++) {
                    if (i == k * 100) {
                        account = list.get(i).getBankAcc();
                    } else {
                        account += "," + list.get(i).getBankAcc();
                    }
                }
                if (null != account && !"".equals(account)) {
                    paramTemp.put("account", account);
                }
                toSendGet(type, paramTemp);
            }
        }
    }
    /**
     * 发送请求
     *
     * @param type
     * @param param
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes", "unused"})
    public void toSendGet(String type, Map<String, String> param) throws Exception {
        String url = "";
        if ("dtl".equals(type)) {
            url = dtlUrl;
        } else if ("bal".equals(type)) {
            url = balUrl;
        } else if ("bankBill".equals(type)) {
            url = billUrl;
        }
        String result = HttpUtils.sendGet(url, param);
        if(!StringUtil.isBlank(result)){
            if ("dtl".equals(type) || "bankBill".equals(type)) {
                Map<String, Object> map = JsonUtils.json2Map(result);
                List<Map<String, Object>> mapListJson = new ArrayList<>();
                if (null != map) {
                    mapListJson = (List) map.get("content");
                }
                if ("dtl".equals(type)) {
                    this.toSaveBankDtl(mapListJson);
                } else if ("bankBill".equals(type)) {
                    this.toSaveBankBill(mapListJson);
                }
                // 总页数
                int totalPages = Integer.parseInt(map.get("totalPages").toString());
                //当前页数
                int number = Integer.parseInt(map.get("number").toString());
                for (; number < totalPages; number++) {
                    Map<String, String> nextParam = new HashMap<>();
                    nextParam.putAll(param);
                    nextParam.put("page", (number + 1) + "");
                    String nextResult = HttpUtils.sendGet(url, nextParam);
                    Map<String, Object> nextMap = JsonUtils.json2Map(nextResult);
                    List<Map<String, Object>> nextmapListJson = (List) nextMap.get("content");
                    if ("dtl".equals(type)) {
                        this.toSaveBankDtl(nextmapListJson);
                    } else if ("bankBill".equals(type)) {
                        this.toSaveBankBill(nextmapListJson);
                    }
                }
            } else if ("bal".equals(type)) {
                List<Map> list = JsonUtils.json2List(result, Map.class);
                List<Map<String, Object>> mapListJson = new ArrayList<>();
                if (null != list) {
                    for (Map map : list) {
                        mapListJson.add(map);
                    }
                }
                this.toSaveBankBal(mapListJson);
            }
        }
    }
}
