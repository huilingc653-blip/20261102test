package com.bytter.biz.foreign.utils;

import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.dto.NisExpenseDetailDTO;
import com.bytter.biz.payment.api.dto.NisWageDetailDTO;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 防篡改
 */
public class DistortUtil {

    public static Map<String, String> convertBisExcToMap(BisExcDTO bisExc){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("voucherNo", bisExc.getVoucherNo());
        paramMap.put("cbsBillId", bisExc.getCbsBillId());
        paramMap.put("reqDate", DateUtils.formatDate(bisExc.getReqDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("payerAcc", StringEscapeUtils.unescapeHtml(bisExc.getPayerAcc()));
        paramMap.put("payerAccName", StringEscapeUtils.unescapeHtml(bisExc.getPayerAccName()));
        paramMap.put("payerBank", StringEscapeUtils.unescapeHtml(bisExc.getPayerBank()));
        paramMap.put("payerCity", bisExc.getPayerCity());
        paramMap.put("payerProv", bisExc.getPayerProv());
        paramMap.put("curId", bisExc.getCurId());
        paramMap.put("payMoney", NumberUtil.formatDoubles(bisExc.getPayMoney(),2));
        paramMap.put("voucherTypeId", bisExc.getVoucherTypeId());
        paramMap.put("payeeAcc", StringEscapeUtils.unescapeHtml(bisExc.getPayeeAcc()));
        paramMap.put("payeeAccName", StringEscapeUtils.unescapeHtml(bisExc.getPayeeAccName()));
        paramMap.put("payeeBankCode", StringEscapeUtils.unescapeHtml(bisExc.getPayeeBankCode()));
        paramMap.put("payeeBank", StringEscapeUtils.unescapeHtml(bisExc.getPayeeBank()));
        paramMap.put("payeeProv", bisExc.getPayeeProv());
        paramMap.put("payeeCity", bisExc.getPayeeCity());
        paramMap.put("wishPayday", DateUtils.formatDate(bisExc.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("transMode", bisExc.getTransMode());
        paramMap.put("bifCode", bisExc.getBifCode());
        paramMap.put("difBank", bisExc.getDifBank());
        paramMap.put("areaSign", bisExc.getAreaSign());
        paramMap.put("purpose", StringEscapeUtils.unescapeHtml(bisExc.getPurpose()));
        paramMap.put("addWord", StringUtils.isBlank(bisExc.getAddWord())?"":StringEscapeUtils.unescapeHtml(bisExc.getAddWord()));
        paramMap.put("voucherFrom", bisExc.getVoucherFrom());
        paramMap.put("cbsBillNo", bisExc.getCbsBillNo());
        paramMap.put("resendSign", bisExc.getResendSign());
        paramMap.put("privateFrom", bisExc.getVoucherFromPrivate());
        paramMap.put("oldVoucherNo", bisExc.getOldVoucherNo());
        paramMap.put("wageDayMonth", bisExc.getWageDayMonth());
        //对私付款增加明细数
        if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExc.getTransMode())) {
            paramMap.put("detailNum", bisExc.getDetailNum() == null ? "0":bisExc.getDetailNum().toString());
        }
        Logger logger = LoggerFactory.getLogger(DistortUtil.class);
        logger.error("----付款指令（bisExc）签名begin----");
        for (String key:paramMap.keySet()) {
            logger.error(key+"："+paramMap.get(key));
        }
        logger.error("----付款指令（bisExc）签名end----");
        return paramMap;
    }

    /**
     * 校验指令表签名
     * @param bisExc
     * @return
     */
    public static boolean checkBisExc(BisExcDTO bisExc){
        return SmUtils.checkSignByPubKey(convertBisExcToMap(bisExc), bisExc.getSignatureCode());
    }

    /**
     * 指令签名
     * @param bisExc
     * @return
     */
    public static String signBisExc(BisExcDTO bisExc){
        return SmUtils.signByPriKey(convertBisExcToMap(bisExc));
    }


    /**
     * 对私指令验签
     * @param bisExc
     * @param detailNum
     * @return
     */
    public static boolean checkPrivateBisExc(BisExcDTO bisExc, int detailNum){
        Map<String, String> map = convertBisExcToMap(bisExc);
        map.put("detailNum", detailNum+"");
        return SmUtils.checkSignByPubKey(map, bisExc.getSignatureCode());
    }

    /**
     * 对私指令签名
     * @param bisExc
     * @param detailNum
     * @return
     */
    public static String signPrivateBisExc(BisExcDTO bisExc, int detailNum){
        Map<String, String> map = convertBisExcToMap(bisExc);
        map.put("detailNum", detailNum+"");
        return SmUtils.signByPriKey(map);
    }



    public static Map<String, String> convertBisForeignToMap(BisForeignExcDTO bisForeignExc){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("voucherNo", bisForeignExc.getVoucherNo());
        paramMap.put("cbsBillId", bisForeignExc.getCbsBillId());
        paramMap.put("cbsBillNo", bisForeignExc.getCbsBillNo());
        paramMap.put("reqDate", bisForeignExc.getReqDate() == null ? null: DateUtils.formatDate(bisForeignExc.getReqDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("payDate", bisForeignExc.getPayDate() == null ? null: DateUtils.formatDate(bisForeignExc.getPayDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("remitType", bisForeignExc.getRemitType());
        paramMap.put("bankTypeId", bisForeignExc.getBankTypeId());
        paramMap.put("bifCode", bisForeignExc.getBifCode());
        paramMap.put("payerAcc", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayerAcc()));
        paramMap.put("payerAccName", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayerAccName()));
        paramMap.put("payerBank", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayerBank()));
        paramMap.put("payerName", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayerName()));
        paramMap.put("payerTel", bisForeignExc.getPayerTel());
        paramMap.put("curId", bisForeignExc.getCurId());
        paramMap.put("payMoney", NumberUtil.formatDoubles(bisForeignExc.getPayMoney(), 2));
        paramMap.put("payeeAcc", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeAcc()));
        paramMap.put("payeeAccName", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeAccName()));
        paramMap.put("payeeSwiftCode", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeSwiftCode()));
        paramMap.put("payeeBank", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeBank()));
        paramMap.put("payeeCountryCode", bisForeignExc.getPayeeCountryCode());
        paramMap.put("payeeAgentBank", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeAgentBank()));
        paramMap.put("payeeAgentBankAdd", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeAgentBankAdd()));
        paramMap.put("payeeAgentAcc", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeAgentAcc()));
        paramMap.put("quickSign", bisForeignExc.getQuickSign());
        paramMap.put("remitNature", bisForeignExc.getRemitNature());
        paramMap.put("remitMethod", bisForeignExc.getRemitMethod());
        paramMap.put("costSign", bisForeignExc.getCostSign());
        paramMap.put("payType", bisForeignExc.getPayType());
        paramMap.put("shipDate", bisForeignExc.getShipDate() == null ? null: DateUtils.formatDate(bisForeignExc.getShipDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("isBonded", bisForeignExc.getIsBonded());
        paramMap.put("busNo", bisForeignExc.getBusNo());
        paramMap.put("addWord", StringUtils.isBlank(bisForeignExc.getAddWord())?"":StringEscapeUtils.unescapeHtml(bisForeignExc.getAddWord()));
        paramMap.put("contractNo", StringEscapeUtils.unescapeHtml(bisForeignExc.getContractNo()));
        paramMap.put("invoiceNo", StringEscapeUtils.unescapeHtml(bisForeignExc.getInvoiceNo()));
        paramMap.put("tranAmt1", bisForeignExc.getTranAmt1() == null ? null: NumberUtil.formatDoubles(bisForeignExc.getTranAmt1(), 2));
        paramMap.put("tranCode1", bisForeignExc.getTranCode1());
        paramMap.put("tranCurId1", bisForeignExc.getTranCurId1());
        paramMap.put("tranAddWord1", StringUtils.isBlank(bisForeignExc.getTranAddWord1())?"":StringEscapeUtils.unescapeHtml(bisForeignExc.getTranAddWord1()));
        paramMap.put("tranCode2", bisForeignExc.getTranCode2());
        paramMap.put("tranAmt2", bisForeignExc.getTranAmt2() == null ? null: NumberUtil.formatDoubles(bisForeignExc.getTranAmt2(), 2));
        paramMap.put("tranCurId2", bisForeignExc.getTranCurId2());
        paramMap.put("tranAddWord2", StringUtils.isBlank(bisForeignExc.getTranAddWord2())?"":StringEscapeUtils.unescapeHtml(bisForeignExc.getTranAddWord2()));
        paramMap.put("costMode", bisForeignExc.getCostMode());
        paramMap.put("dedAcc", StringEscapeUtils.unescapeHtml(bisForeignExc.getDedAcc()));
        paramMap.put("bankCurCode", bisForeignExc.getBankCurCode());
        paramMap.put("interestDate", bisForeignExc.getInterestDate() == null ? null: DateUtils.formatDate(bisForeignExc.getInterestDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("bankSerialNo", StringUtils.isBlank(bisForeignExc.getBankSerialNo())?"":StringEscapeUtils.unescapeHtml(bisForeignExc.getBankSerialNo()));
        paramMap.put("cmdType", bisForeignExc.getCmdType());
        paramMap.put("voucherFrom", bisForeignExc.getVoucherFrom());
        paramMap.put("difBank", bisForeignExc.getDifBank());
        paramMap.put("fexBusType", bisForeignExc.getFexBusType());
        paramMap.put("payeeBankCode", StringEscapeUtils.unescapeHtml(bisForeignExc.getPayeeBankCode()));
        paramMap.put("bankOrgCode", StringEscapeUtils.unescapeHtml(bisForeignExc.getBankOrgCode()));
        paramMap.put("payerCurId", bisForeignExc.getPayerCurId());
        paramMap.put("resendSign", bisForeignExc.getResendSign());
        paramMap.put("payeeProv", bisForeignExc.getPayeeProv());
        paramMap.put("payeeCity", bisForeignExc.getPayeeCity());
        paramMap.put("accType", bisForeignExc.getAccType());
        paramMap.put("payeeType", bisForeignExc.getPayeeType());
        paramMap.put("remitRoute", bisForeignExc.getRemitRoute());
        paramMap.put("remitUse", StringEscapeUtils.unescapeHtml(bisForeignExc.getRemitUse()));
        paramMap.put("fileName", StringEscapeUtils.unescapeHtml(bisForeignExc.getFileName()));
        paramMap.put("oldVoucherNo", bisForeignExc.getOldVoucherNo());
        Logger logger = LoggerFactory.getLogger(DistortUtil.class);
        logger.error("----外币汇款指令（bisForeignExc）签名begin----");
        for (String key:paramMap.keySet()) {
            logger.error(key+"："+paramMap.get(key));
        }
        logger.error("----外币汇款指令（bisForeignExc）签名end----");
        return paramMap;
    }

    /**
     * 校验外汇指令表签名
     * @param bisForeignExc
     * @return
     */
    public static boolean checkBisForeignExc(BisForeignExcDTO bisForeignExc){
        return SmUtils.checkSignByPubKey(convertBisForeignToMap(bisForeignExc), bisForeignExc.getSignatureCode());
    }

    /**
     * 外汇指令表签名
     * @param bisForeignExc
     * @return
     */
    public static String signBisForeignExc(BisForeignExcDTO bisForeignExc){
        return SmUtils.signByPriKey(convertBisForeignToMap(bisForeignExc));
    }


    public static Map<String, String> convertNisWageDetailToMap(NisWageDetailDTO nisWageDetail){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bankAcc", StringEscapeUtils.unescapeHtml(nisWageDetail.getBankAcc()));
        paramMap.put("accName", StringEscapeUtils.unescapeHtml(nisWageDetail.getAccName()));
        paramMap.put("wagesVerify", nisWageDetail.getWagesVerify());
        paramMap.put("purpose", StringEscapeUtils.unescapeHtml(nisWageDetail.getPurpose()));
        paramMap.put("prov", nisWageDetail.getProv());
        paramMap.put("city", nisWageDetail.getCity());
        paramMap.put("bankCode", StringEscapeUtils.unescapeHtml(nisWageDetail.getBankCode()));
        paramMap.put("bankName", StringEscapeUtils.unescapeHtml(nisWageDetail.getBankName()));
        paramMap.put("difBank", nisWageDetail.getDifBank());
        paramMap.put("areaSign", nisWageDetail.getAreaSign());
        //paramMap.put("voucherNo", nisWageDetail.getVoucherNo());
        Logger logger = LoggerFactory.getLogger(DistortUtil.class);
        logger.error("----代发工资明细（nisWageDetail）签名begin----");
        for (String key:paramMap.keySet()) {
            logger.error(key+"："+paramMap.get(key));
        }
        logger.error("----代发工资明细（nisWageDetail）签名end----");
        return paramMap;
    }



    /**
     * 校验工资发放明细表签名
     * @param nisWageDetail
     * @return
     */
    public static boolean checkNisWageDetail(NisWageDetailDTO nisWageDetail) {
        return SmUtils.checkSignByPubKey(convertNisWageDetailToMap(nisWageDetail), nisWageDetail.getSignatureCode());
    }

    /**
     * 工资明细表签名
     * @param nisWageDetail
     * @return
     */
    public static String signNisWageDetail(NisWageDetailDTO nisWageDetail) {
        return SmUtils.signByPriKey(convertNisWageDetailToMap(nisWageDetail));
    }

    public static Map<String, String> convertNisExpenseDetailToMap(NisExpenseDetailDTO nisExpenseDetail){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("expenseDate", DateUtils.formatDate(nisExpenseDetail.getExpenseDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("busType", nisExpenseDetail.getBusType());
        paramMap.put("bifCode", nisExpenseDetail.getBifCode());
        paramMap.put("difBank", nisExpenseDetail.getDifBank());
        paramMap.put("areaSign", nisExpenseDetail.getAreaSign());
        paramMap.put("expenseMoney", NumberUtil.formatDoubles(nisExpenseDetail.getExpenseMoney(),2));
        paramMap.put("bankCode", StringEscapeUtils.unescapeHtml(nisExpenseDetail.getBankCode()));
        paramMap.put("bankName",StringEscapeUtils.unescapeHtml( nisExpenseDetail.getBankName()));
        paramMap.put("bankAcc", StringEscapeUtils.unescapeHtml(nisExpenseDetail.getBankAcc()));
        paramMap.put("accName", StringEscapeUtils.unescapeHtml(nisExpenseDetail.getAccName()));
        paramMap.put("prov", nisExpenseDetail.getProv());
        paramMap.put("city", nisExpenseDetail.getCity());
        paramMap.put("purpose", StringEscapeUtils.unescapeHtml(nisExpenseDetail.getPurpose()));
        paramMap.put("billId", nisExpenseDetail.getBillId());
        //paramMap.put("voucherNo", nisExpenseDetail.getVoucherNo());
        Logger logger = LoggerFactory.getLogger(DistortUtil.class);
        logger.error("----费用报销明细（nisExpenseDetail）签名begin----");
        for (String key:paramMap.keySet()) {
            logger.error(key+"："+paramMap.get(key));
        }
        logger.error("----费用报销明细（nisExpenseDetail）签名end----");
        return paramMap;
    }


    /**
     * 校验费用报销明细表签名
     * @param nisExpenseDetail
     * @return
     */
    public static boolean checkNisExpenseDetail(NisExpenseDetailDTO nisExpenseDetail) {
        return SmUtils.checkSignByPubKey(convertNisExpenseDetailToMap(nisExpenseDetail), nisExpenseDetail.getSignatureCode());
    }

    /**
     * 签名费用报销
     * @param nisExpenseDetail
     * @return
     */
    public static String signNisExpenseDetail(NisExpenseDetailDTO nisExpenseDetail) {
        return SmUtils.signByPriKey(convertNisExpenseDetailToMap(nisExpenseDetail));
    }


    public static Map<String, String> convertBisGatherCmdToMap(BisGatherCmdDTO bisGatherCmd){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("voucherNo", bisGatherCmd.getVoucherNo());
        paramMap.put("bifCode", bisGatherCmd.getBifCode());
        paramMap.put("cbsBillId", bisGatherCmd.getCbsBillId());
        paramMap.put("childAccId", bisGatherCmd.getChildAccId());
        paramMap.put("parentAccId", bisGatherCmd.getParentAccId());
        paramMap.put("gatherAmt", NumberUtil.formatDoubles(bisGatherCmd.getGatherAmt(), 2));
        paramMap.put("gatherPolicyId", bisGatherCmd.getGatherPolicyId());
        paramMap.put("curId", bisGatherCmd.getCurId());
        paramMap.put("runTime", DateUtils.formatDate(bisGatherCmd.getRunTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        paramMap.put("isQuick", bisGatherCmd.getIsQuick());
        paramMap.put("difBank", bisGatherCmd.getDifBank());
        paramMap.put("areaSign", bisGatherCmd.getAreaSign());
        paramMap.put("purpose", StringEscapeUtils.unescapeHtml(bisGatherCmd.getPurpose()));
        paramMap.put("addWord", StringUtils.isBlank(bisGatherCmd.getAddWord())?"":StringEscapeUtils.unescapeHtml(bisGatherCmd.getAddWord()));
        paramMap.put("voucherFrom", bisGatherCmd.getVoucherFrom());
        paramMap.put("busOriType", bisGatherCmd.getBusOriType().toString());
        paramMap.put("dtlId", bisGatherCmd.getDtlId());
        paramMap.put("cmdType", bisGatherCmd.getCmdType());
        paramMap.put("cbsBillNo", bisGatherCmd.getCbsBillNo());
        paramMap.put("resendSign", bisGatherCmd.getResendSign());
        Logger logger = LoggerFactory.getLogger(DistortUtil.class);
        logger.error("----收款指令（bisGatherCmd）签名begin----");
        for (String key:paramMap.keySet()) {
            logger.error(key+"："+paramMap.get(key));
        }
        logger.error("----收款指令（bisGatherCmd）签名end----");
        return paramMap;
    }


    /**
     * 校验归集指令表签名
     * @param bisGatherCmd
     * @return
     */
    public static boolean checkBisGatherCmd(BisGatherCmdDTO bisGatherCmd) {
        return SmUtils.checkSignByPubKey(convertBisGatherCmdToMap(bisGatherCmd), bisGatherCmd.getSignatureCode());
    }

    /**
     * 签名归集指令表
     * @param bisGatherCmd
     * @return
     */
    public static String signBisGatherCmd(BisGatherCmdDTO bisGatherCmd) {
        return SmUtils.signByPriKey(convertBisGatherCmdToMap(bisGatherCmd));
    }

}
