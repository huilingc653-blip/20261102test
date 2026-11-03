package com.bytter.biz.receipt.service;

import com.alibaba.excel.util.ObjectUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.bdlink.api.dto.BisReceiptDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptBlendingServer;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlAutoVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.ReceiptConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 银行电子回单勾兑
 */
@Service("bisBankReceiptBlendingServer")
@Transactional
public class BisBankReceiptBlendingServer extends AbstractService {
    @DubboReference(check = false)
    private IBisBankReceiptBlendingServer bisBankReceiptBlendingServer;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        StringBuilder commonSql = new StringBuilder();
        String ins_corpId = (String) input.getFormdata().get("ins_corpId");
        String corpAuthSql;
        //若未选开户单位，则默认查询当前登录人所有管理单位
        if (StringUtils.isEmpty(ins_corpId)) {
            corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_RECEIPT_BISBANKRECEIPTBLENDING_QUERY, user.getId()), "acc.corp_id", true);
            commonSql.append(" and ").append(corpAuthSql);
        } else {
            corpAuthSql = SqlUtil.getInSql(ins_corpId.split(","), "acc.corp_id", true);
            commonSql.append(" and ").append(corpAuthSql);
            input.getFormdata().remove("ins_corpId");
        }
        List<BtBankAccDTO> accDTOS = bankAccService.getBankAccNotUnionByCorpIdList(null, commonSql.toString(), new HashMap<>());
        Map<String, BtBankAccDTO> accMap = new HashMap<>();
        for (BtBankAccDTO acc : accDTOS) {
            accMap.put(acc.getBankAcc(), acc);
        }
        DataTablesOutput<BisBankReceiptDtlVO> output = this.bisBankReceiptBlendingServer.getDataTablesUnion(input, user,accMap.keySet());
        List<BisBankReceiptDtlVO> receiptDtlVOS = output.getData();
        Map<String, Map<String, DictDTO>> dictMap = iDictService.listDictMap("btBankAccDtl.cdSign", "paymentblending_status");
        Map<String, String> currencyNameMap = btCurrencyService.getCurrencyNameByIds(receiptDtlVOS.stream().map(BisBankReceiptDtlVO::getCurId)
                .collect(Collectors.toList()));
        for (BisBankReceiptDtlVO receiptDtlVO : receiptDtlVOS) {
            receiptDtlVO.setCurName(currencyNameMap.get(receiptDtlVO.getCurId()));
            receiptDtlVO.setCdSignName(dictMap.getOrDefault("btBankAccDtl.cdSign", Maps.newHashMap())
                    .getOrDefault(receiptDtlVO.getCdSign(), DictDTO.EMPTY).getName());
            receiptDtlVO.setBlendSignName(dictMap.getOrDefault("paymentblending_status", Maps.newHashMap())
                    .getOrDefault(!ObjectUtils.isEmpty(receiptDtlVO.getBlendSign()) ? receiptDtlVO.getBlendSign() : BtBankAccConstant.BLENDSIGN_0, DictDTO.EMPTY).getName());
            BtBankAccDTO acc = accMap.get(receiptDtlVO.getPayAccNo());
            if(null != acc) {
                receiptDtlVO.setCorpId(acc.getCorpId());
                receiptDtlVO.setCorpName(acc.getCorpName());
            }
        }
        String eqs_blendSign = (String) input.getFormdata().get("eqs_blendSign");
        //如果查询的已勾兑数据则查询关联的明细id
        if (eqs_blendSign.equals(BtBankAccConstant.BLENDSIGN_1) || eqs_blendSign == "") {
            List<BisBankReceiptDtlVO> list = output.getData();
            for (BisBankReceiptDtlVO vo : list) {
                List<BisReceiptDtlDTO> dtlIdList = this.bisBankReceiptBlendingServer.getBisReceiptDtl(vo.getId());
                String dtlId = "";
                for (int i = 0; i < dtlIdList.size(); i++) {
                    BisReceiptDtlDTO bisReceiptDtl = dtlIdList.get(i);
                    vo.setPaymentBlendingDate(bisReceiptDtl.getPaymentblendingDate());
                    if (i == 0) {
                        dtlId += bisReceiptDtl.getDtlId();
                    } else {
                        dtlId += "," + bisReceiptDtl.getDtlId();
                    }
                }
                vo.setDtlId(dtlId);
            }
            output.setData(list);
        }
        return output;
    }
    /**
     * 银行流水列表
     *
     * @param input 查询条件输入
     * @return
     */
    public DataTablesOutput<BisAccDtlDTO> getDtlDataTablesUnion(DataTablesInput input) {
        DataTablesOutput<BisAccDtlDTO> result=new DataTablesOutput<>();
        DataTablesOutput<BisAccDtlVO> output= this.bisAccDtlService.getDataTablesUnionForReceiptBlending(input);
        BeanUtils.copyProperties(output, result);
        List<BisAccDtlVO> bisAccDtlDTOList=output.getData();
        List<BisAccDtlDTO> bisAccDtlDTOS = BeanUtils.copyListProperties(bisAccDtlDTOList, BisAccDtlDTO.class);
        if(bisAccDtlDTOS.size()>0) {
            Map<String, BtCurrencyDTO> currency = btCurrencyService.getCurrency();
            //查询账户
            StringBuilder commonSql = new StringBuilder("");
            List<String> accList = new ArrayList<>();
            for (BisAccDtlDTO accDtlDTO : bisAccDtlDTOS) {
                accList.add(accDtlDTO.getBankAcc());
            }
            String corpAuthSql = SqlUtil.getInSql(accList, "acc.bank_acc", true);
            commonSql.append(" and ").append(corpAuthSql);
            List<BtBankAccDTO> accDTOS = bankAccService.getBankAccNotUnionByCorpIdList(null, commonSql.toString(), new HashMap<>());
            Map<String, BtBankAccDTO> accMap = new HashMap<>();
            for (BtBankAccDTO acc : accDTOS) {
                accMap.put(acc.getBankAcc(), acc);
            }
            for (BisAccDtlDTO accDtlDTO : bisAccDtlDTOS) {
                BtBankAccDTO acc=accMap.get(accDtlDTO.getBankAcc());
                accDtlDTO.setCorpId(acc.getCorpId());
                accDtlDTO.setCorpName(acc.getCorpName());
                accDtlDTO.setAttributeId(acc.getAttributeId());
                accDtlDTO.setAttributeName(acc.getAttributeName());
                accDtlDTO.setNatureId(acc.getNatureId());
                accDtlDTO.setNatureName(acc.getNatureName());
                accDtlDTO.setAccType(acc.getAccType());
                accDtlDTO.setAccTypeName(acc.getAccTypeName());
                if(null != currency){
                    BtCurrencyDTO btCurrencyDTO = currency.get(accDtlDTO.getCurId());
                    if(null != btCurrencyDTO){
                        accDtlDTO.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            }
        }
        result.setData(bisAccDtlDTOS);
        return result;
    }
    /**
     * 自动勾兑
     *
     * @param ids
     * @param bankAccTypeStr
     */
    public String aotoBlending(String ids, String bankAccTypeStr) {
        String fuzzyMatching = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_FUZZY_MATCHING).getValue();
        String msg = "";
        String rules = "";
        List<BisBankReceiptDtlVO> dtlList = bisBankReceiptBlendingServer.listReceiptByids(ids);
        Map<String, BisBankReceiptDtlVO> tempMap = new HashMap<>();
        Map<String, BisBankReceiptDtlVO> repeatMap = new HashMap<>();
        Boolean eqg_payMoney = false;
        Boolean eqs_payerAcc = false;
        Boolean eqs_payeeAcc = false;
        Boolean wishPayday = false;
        Boolean eqs_curId = false;
        Boolean isHasData = false;
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_1)) {
            eqg_payMoney = true;
            rules = rules + iDictService.get("paymentblending_matching_rule", "1");
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_2)) {
            eqs_payerAcc = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "2");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "2");
            }
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_3)) {
            eqs_payeeAcc = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "3");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "3");
            }
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_4)) {
            wishPayday = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "4");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "4");
            }
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_5)) {
            eqs_curId = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "5");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "5");
            }
        }
        for (int i = 0; i < dtlList.size(); i++) {
            DataTablesInput input = new DataTablesInput();
            String cdSign = dtlList.get(i).getCdSign();
            input.getFormdata().put("eqs_cdSign", cdSign);
            if (eqg_payMoney) {
                if (EmptyUtils.isEmpty(dtlList.get(i).getTransAmt())) {
                    continue;
                }
                input.getFormdata().put("eqg_amt", dtlList.get(i).getTransAmt());
            }
            if (eqs_payerAcc) {
                //考虑流水账号为空的情况
                if (EmptyUtils.isEmpty(dtlList.get(i).getPayAccNo())) {
                    dtlList.get(i).setPayAccNo("null");
                }
                //如果是收款，付款账号为对方账号。收款账号为本方账号
                if (cdSign.equals("0")) {
                    input.getFormdata().put("eqs_oppAccNo", dtlList.get(i).getPayAccNo());
                } else {
                    input.getFormdata().put("eqs_bankAcc", dtlList.get(i).getPayAccNo());
                }
            }
            if (eqs_payeeAcc) {
                //考虑流水账号为空的情况
                if (EmptyUtils.isEmpty(dtlList.get(i).getPayeeAccNo())) {
                    dtlList.get(i).setPayeeAccNo("null");
                }
                //如果是收款，付款账号为对方账号。收款账号为本方账号
                if (cdSign.equals("0")) {
                    input.getFormdata().put("eqs_bankAcc", dtlList.get(i).getPayeeAccNo());
                } else {
                    input.getFormdata().put("eqs_oppAccNo", dtlList.get(i).getPayeeAccNo());
                }
            }
            if (wishPayday) {
                if (EmptyUtils.isEmpty(dtlList.get(i).getCharageDate())) {
                    continue;
                }
                Date date = dtlList.get(i).getCharageDate();
                String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
                String beginDate = dateStr;
                String endDate = dateStr;
                input.getFormdata().put("ged_transTime", beginDate);
                input.getFormdata().put("led_transTime", endDate);
            }
            if (eqs_curId) {
                if (EmptyUtils.isEmpty(dtlList.get(i).getCurId())) {
                    continue;
                }
                input.getFormdata().put("eqs_curId", dtlList.get(i).getCurId());
            }
            DataTablesOutput output = this.bisAccDtlService.getDataTablesUnionForReceiptBlending(input);
            if ("0".equals(fuzzyMatching)) {
                if (output.getData().size() == 1) {
                    List<com.bytter.biz.bdlink.api.dto.BisAccDtlDTO> list = output.getData();
                    if (!tempMap.containsKey(list.get(0).getId())) {
                        tempMap.put(list.get(0).getId(), dtlList.get(i));
                    } else {
                        repeatMap.put(list.get(0).getId(), dtlList.get(i));
                    }
                }
            } else {
                if (output.getData().size() >= 1) {
                    List<com.bytter.biz.bdlink.api.dto.BisAccDtlDTO> list = output.getData();
                    if (!tempMap.containsKey(list.get(0).getId())) {
                        tempMap.put(list.get(0).getId(), dtlList.get(i));
                    }
                }
            }
        }
        for (String key : tempMap.keySet()) {
            BisBankReceiptDtlVO dtl = tempMap.get(key);
            if (repeatMap.containsKey(key)) {
                continue;
            } else {
                isHasData = true;
                this.toUpdateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_1);
                this.saveBisReceiptDtl(key, dtl.getId(), rules);
            }
        }
        if (!isHasData) {
            msg = "bankacc.message.notDate";
        }
        return msg;
    }
    /*
    唯一标识勾兑
     */
//    public void JustOne(BisBankReceiptDtlAutoVO bisBankReceiptDtlAutoVO) {
//        String dtlONlyId = bisBankReceiptDtlAutoVO.getDtlOnlyId();
//        String receiptONlyId = bisBankReceiptDtlAutoVO.getReceiptOnlyId();
//        String checkString = "";
//        if (receiptONlyId.equals("BUS_NO")) {//业务单号
//            checkString = bisBankReceiptDtlAutoVO.getBusNo();
//        } else if (receiptONlyId.equals("TRANS_SERIAL_NO")) {//交易流水号
//            checkString = bisBankReceiptDtlAutoVO.getTransSerialNo();
//        }
//        List<BisBankReceiptDtlAutoVO> relist = bisBankReceiptBlendingServer.getForReceipt(receiptONlyId, null, checkString);
//        if (checkString != "") {
//            List<com.bytter.biz.bdlink.api.dto.BisAccDtlDTO> li = new ArrayList<>();
//            if (dtlONlyId.equals("ACC_NO")) {//业务单号
//                li = bisBankReceiptBlendingServer.getJust("accNo", checkString);
//            } else if (dtlONlyId.equals("BANK_SERIAL_NO")) {//银行流水号
//                li = bisBankReceiptBlendingServer.getJust("bankSerialNo", checkString);
//            }
//            if (li.size() == 1 && relist.size() == 1) {
//                bisBankReceiptDtlAutoVO.setRulesName(multiLangServiceI.getLang("table.bisBankExecution.unique"));
//                this.toUpdateBlendSign(bisBankReceiptDtlAutoVO.getReceiptId(), BtBankAccConstant.BLENDSIGN_1);
//                this.saveBisReceiptDtl(li.get(0).getId(), bisBankReceiptDtlAutoVO.getReceiptId(), bisBankReceiptDtlAutoVO.getRulesName());
//            }
//        }
//    }
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    public int toUpdateBlendSign(String id, String blendSign) {
        return this.bisBankReceiptBlendingServer.toUpdateBlendSign(id, blendSign);
    }
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    public int toUpdateBlendSign(String id, String blendSign, String oldBlendSign) {
        return this.bisBankReceiptBlendingServer.toUpdateBlendSign(id, blendSign, oldBlendSign);
    }
    /**
     * 插入银行电子回单和银行流水的关系
     *
     * @param dtlId
     * @param receiptId
     */
    public int saveBisReceiptDtl(String dtlId, String receiptId, String rulesName) {
        return this.bisBankReceiptBlendingServer.saveBisReceiptDtl(dtlId, receiptId, rulesName);
    }
    /**
     * 手工勾兑保存
     *
     * @param dtlId
     * @param receiptId
     */
    public void manualBlending(String dtlId, String receiptId) {
        String[] dtlIds = dtlId.split(",");
        String[] receiptIds = receiptId.split(",");
        for (String receiptIdStr : receiptIds) {
            for (String dtlIdStr : dtlIds) {
                //插入关系关联表
                this.bisBankReceiptBlendingServer.saveBisReceiptDtl(dtlIdStr, receiptIdStr, null);
            }
            //更新勾兑标志为已勾兑
            this.bisBankReceiptBlendingServer.toUpdateBlendSign(receiptIdStr, BtBankAccConstant.BLENDSIGN_1);
        }
    }
    /**
     * 取消勾兑
     *
     * @param receiptId
     */
    public void cancelBlending(String receiptId) {
        bisBankReceiptBlendingServer.cancelBlending(receiptId);
    }
}
