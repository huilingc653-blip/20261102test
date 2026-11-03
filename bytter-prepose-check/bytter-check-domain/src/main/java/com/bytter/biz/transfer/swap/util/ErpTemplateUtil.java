package com.bytter.biz.transfer.swap.util;
import com.bytter.biz.transfer.swap.entity.ErpTemplate;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtl;
import com.bytter.core.utils.StringUtils;
/**
 * Created by zhouj on 2018/5/26.
 */
public class ErpTemplateUtil {
    /**
     * 表头赋值
     * @param erpTemplate
     * @param key
     * @param value
     */
    public static void setHeadValueByMap(ErpTemplate erpTemplate, String key, Object value){
        if(StringUtils.isBlank(key)){
            return;
        }
        if(isKeyExist("voucherTypeCode",key)){
            erpTemplate.setVoucherTypeCode(String.valueOf(value));
        }
        if(isKeyExist("referCode",key)){
            erpTemplate.setReferCode(String.valueOf(value));
        }
        if(isKeyExist("voucherPreText",key)){
            erpTemplate.setVoucherPreText(String.valueOf(value));
        }
        if(isKeyExist("itemCode",key)){
            erpTemplate.setItemCode(String.valueOf(value));
        }
        if(isKeyExist("refer",key)){
            erpTemplate.setRefer(String.valueOf(value));
        }
        if(isKeyExist("abs",key)){
            erpTemplate.setAbs(String.valueOf(value));
        }
        if(isKeyExist("purpose",key)){
            erpTemplate.setPurpose(String.valueOf(value));
        }
        if(isKeyExist("payType",key)){
            erpTemplate.setPayType(String.valueOf(value));
        }
        if(isKeyExist("reverse1",key)){
            erpTemplate.setReverse1(String.valueOf(value));
        }
        if(isKeyExist("reverse2",key)){
            erpTemplate.setReverse2(String.valueOf(value));
        }
    }
    /**
     * 凭证赋值
     * @param templateDtl
     * @param key
     * @param value
     */
    public static void setChildValueByMap(ErpTemplateDtl templateDtl, String key, Object value){
        if(StringUtils.isBlank(key)){
            return;
        }
        if(isKeyExist("loanDirection",key)){
            if("1".equals(value)){
                templateDtl.setLoanDirection(true);
            }else{
                templateDtl.setLoanDirection(false);
            }
        }
        if(isKeyExist("subjectCode",key)){
            templateDtl.setSubjectCode(String.valueOf(value));
        }
        if(isKeyExist("subjectName",key)){
            templateDtl.setSubjectName(String.valueOf(value));
        }
        if(isKeyExist("subjectClass",key)){
            templateDtl.setSubjectClass(String.valueOf(value));
        }
        if(isKeyExist("subjectType",key)){
            templateDtl.setSubjectType(String.valueOf(value));
        }
        if(isKeyExist("costCenterCode",key)){
            templateDtl.setCostCenterCode(String.valueOf(value));
        }
        if(isKeyExist("cashFlowCode",key)){
            templateDtl.setCashFlowCode(String.valueOf(value));
        }
        if(isKeyExist("accountCode",key)){
            templateDtl.setAccountCode(String.valueOf(value));
        }
        if(isKeyExist("accountType",key)){
            templateDtl.setAccountType(String.valueOf(value));
        }
        if(isKeyExist("dtlAbs",key)){
            templateDtl.setDtlAbs(String.valueOf(value));
        }
        if(isKeyExist("paymentReasonCode",key)){
            templateDtl.setPaymentReasonCode(String.valueOf(value));
        }
        if(isKeyExist("ledgerSign",key)){
            templateDtl.setLedgerSign(String.valueOf(value));
        }
        if(isKeyExist("transType",key)){
            templateDtl.setTransType(String.valueOf(value));
        }
        if(isKeyExist("assign",key)){
            templateDtl.setAssign(String.valueOf(value));
        }
        if(isKeyExist("tradePartner",key)){
            templateDtl.setTradePartner(String.valueOf(value));
        }
        if(isKeyExist("taxCode",key)){
            templateDtl.setTaxCode(String.valueOf(value));
        }
        if(isKeyExist("dtlItemCode",key)){
            templateDtl.setDtlItemCode(String.valueOf(value));
        }
        if(isKeyExist("dtlReverse1",key)){
            templateDtl.setDtlReverse1(String.valueOf(value));
        }
        if(isKeyExist("dtlReverse2",key)){
            templateDtl.setDtlReverse2(String.valueOf(value));
        }
    }
    protected static boolean isKeyExist(String key,String target){
        return key.equalsIgnoreCase(target) || key.equalsIgnoreCase(target.replaceAll("_",""));
    }
}
